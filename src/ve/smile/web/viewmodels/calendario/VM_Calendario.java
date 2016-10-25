package ve.smile.web.viewmodels.calendario;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.session.DataCenter;
import ve.smile.dto.Album;
import ve.smile.dto.EventoPlanificado;

public class VM_Calendario {
	
	private String fecha;	
	private Date fechaPlan;
	private EventoPlanificado eventoPlan;

	public EventoPlanificado getEventoPlan() {
		if (this.eventoPlan == null)
			this.eventoPlan = (EventoPlanificado) DataCenter.getEntity();
		return eventoPlan;
	}

	public void setEventoPlan(EventoPlanificado eventoPlan) {
		this.eventoPlan = eventoPlan;
	}
	
	public Date getFechaPlan() {
		this.fechaPlan = new Date(getEventoPlan().getFechaInicio());		
		return fechaPlan;
	}
	
	public String getFecha() {
		if (eventoPlan == null){
			return null;
		}else {
			this.fecha=new SimpleDateFormat("dd-MM-yyyy").format(getFechaPlan());
			return fecha;
		}
	}
	
	@Command
	public void eventos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/eventos.zul");
	}
	
	@GlobalCommand
	@NotifyChange({"eventoPlan","fecha"})
	public void refreshDetalleAlbum(@BindingParam("eventoPlan") EventoPlanificado eventoPlan){
		this.eventoPlan = eventoPlan;
	}
	
	@Command
	public void album(@BindingParam("album") Album album ) {
		DataCenter.updateSrcPageContent(album, null, "/views/web/album.zul");
	}

}
