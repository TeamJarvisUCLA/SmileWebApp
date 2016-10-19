package ve.smile.web.viewmodels.evento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.PathParam;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.dto.Evento;
import ve.smile.dto.EventoPlanificado;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class VM_Evento {
	
	private List<EventoPlanificado> eventoPlanificado;
	private Long fechaDesde;
	private String fecha;	
	
	private Date fechaPlan;
	private Integer cant=3;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	public List<EventoPlanificado> getEventoPlanificado() {
		if (this.eventoPlanificado == null) {
			this.eventoPlanificado = new ArrayList<>();
		}
		if (this.eventoPlanificado.isEmpty()) {
		
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService.
					consultaEventoPlanificadoPublicable(true, EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal(), fechaDesde= new Date().getTime(), cant);
		
			this.eventoPlanificado.addAll(payloadEventoPlanificadoResponse.getObjetos());

		}

		return eventoPlanificado;
	}
	
	
	public long getFechaDesde() {
		return fechaDesde;
	}
	
	
	public void setFechaDesde(long fechaDesde) {
		this.fechaDesde = fechaDesde;		
	}
	
	public EventoPlanificado getevento() {
		return (EventoPlanificado) DataCenter.getEntity();
	}
	
	public Date getFechaPlan() {
		
			this.fechaPlan = new Date(getevento().getFechaPlanificada());		
			return fechaPlan;
	}
	
	
	public String getFecha() {
		this.fecha=new SimpleDateFormat("dd-MM-yyyy").format(getFechaPlan());
		return fecha;
	}
	

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}	
	
	@Command
	@NotifyChange({"eventoPlanificado"})
	public void verMas() {
		cant += 3;
		if (this.eventoPlanificado == null) {
			this.eventoPlanificado = new ArrayList<>();
		}
		this.eventoPlanificado.clear();
		if (this.eventoPlanificado.isEmpty()) {

			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService.
					consultaEventoPlanificadoPublicable(true, EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal(), fechaDesde= new Date().getTime(), cant);
		
			this.eventoPlanificado.addAll(payloadEventoPlanificadoResponse.getObjetos());
		}
		Clients.evalJavaScript("f_zoom();");
	}
	
	@Command
	public void calendario(@BindingParam("evento") EventoPlanificado evento ) {

		DataCenter.updateSrcPageContent(evento, null, "/views/web/calendario.zul");
		
		
	}
	

	

}
