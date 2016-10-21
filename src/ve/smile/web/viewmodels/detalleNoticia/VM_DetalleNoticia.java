package ve.smile.web.viewmodels.detalleNoticia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.session.DataCenter;
import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;

public class VM_DetalleNoticia {
	
	private List<Cartelera> noticias;
	
	public Cartelera getnoticia() {
		return (Cartelera) DataCenter.getEntity();
	}
	
	public List<Cartelera> getnoticias(){
		if (this.noticias == null) {
			this.noticias = new ArrayList<>();
		}
		if (this.noticias.isEmpty()) {
		
			PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService
					.consultaCarteleraPorParametro(1000, TipoCarteleraEnum.NOTICIAS.ordinal());
		
			this.noticias.addAll(payloadCarteleraResponse.getObjetos());
		}

		return noticias;
	}
	
	@Command
	@NotifyChange({"noticia"})
	public void siguiente() {
		
		Cartelera noticia = getnoticia();
		
		if ( getnoticias().indexOf(noticia) == getnoticias().size()-1 ) {
			noticia = getnoticias().get(0);			
		}else {
			noticia = getnoticias().get(getnoticias().indexOf(noticia)+1);
		}
		DataCenter.updateSrcPageContent(noticia, null, "/views/web/detalleNoticia.zul");
		
		
	}
	
	
	@Command
	@NotifyChange({"noticia"})
	public void volver() {
		
		Cartelera noticia = getnoticia();
		
		if ( getnoticias().indexOf(noticia) == 0 ) {
			noticia = getnoticias().get(getnoticias().size()-1);			
		}else {
			noticia = getnoticias().get(getnoticias().indexOf(noticia)-1);
		}
		DataCenter.updateSrcPageContent(noticia, null, "/views/web/detalleNoticia.zul");
	
	}
	
	@Command
	public void noticias() {
		
		DataCenter.updateSrcPageContent(null, null, "/views/web/noticias.zul");
	
	}

	private String fecha;	
	private Date fechaI;
	
	private Cartelera carteleras;

	public Cartelera getCarteleras() {
			this.carteleras = (Cartelera) DataCenter.getEntity();
		return carteleras;
	}

	public void setCartelera(Cartelera carteleras) {
		this.carteleras = carteleras;
	}
	public Date getFechaI() {
		this.fechaI = new Date(getCarteleras().getFechaInicio());		
		return fechaI;
	}
	
	public String getFecha() {
	
			this.fecha=new SimpleDateFormat("dd-MM-yyyy").format(getFechaI());
			return fecha;
		
	}


	
}
