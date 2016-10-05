package ve.smile.web.viewmodels.main;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Participacion;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.payload.response.PayloadParticipacionResponse;

public class VM_Main {
	
//	se declara una variable noticias de tipo List<Cartelera>	
	private List<Cartelera> noticias;
	private List<Participacion> participacion;
	private List<Organizacion> organizacion;
	
//  metodo get que retorna la coleccion de elementos a renderizar en la vista	
	public List<Cartelera> getnoticias(){
		if (this.noticias == null) {
			this.noticias = new ArrayList<>();
		}
		if (this.noticias.isEmpty()) {
// uso de servicio consultaCarteleraPorParametro en CarteleraService, aqui se instancian los objetos 
// devueltos por consultaCarteleraPorParametro como una variable de tipo PayloadCarteleraResponse			
			PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService
					.consultaCarteleraPorParametro(3, TipoCarteleraEnum.NOTICIAS.ordinal());
// agregar en lista de noticias todos los objetos contenidos en la variable payloadCarteleraResponse 			
			this.noticias.addAll(payloadCarteleraResponse.getObjetos());
		}
// retornar lista
		return noticias;
	}
	
	public List<Organizacion> getorganizacion(){
		if (this.organizacion == null) {
			this.organizacion = new ArrayList<>();
		}
		if (this.organizacion.isEmpty()) {
			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService
					.buscarOrganizacion();
			
			this.organizacion.addAll(payloadOrganizacionResponse.getObjetos());
		}

		return organizacion;
	}
	
	public List<Participacion> getparticipacion(){
		if (this.participacion == null) {
			this.participacion = new ArrayList<>();
		}
		if (this.participacion.isEmpty()) {
			PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService.consultaCantidadParticipacion(3);
			
			this.participacion.addAll(payloadParticipacionResponse.getObjetos());
		}

		return participacion;
	}
	
	@Command
	public void noticias() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/noticias.zul");
		
	}
	
	@Command
	public void conocenos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/conocenos.zul");
		
	}
	
	@Command
	public void eventos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/eventos.zul");
		
	}
	
	@Command
	public void participa() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/participa.zul");
		
	}
	
	@Command
	public void galeria() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/galeria.zul");
		
	}
	
	@Command
	public void detalleNoticia(@BindingParam("noticia") Cartelera noticia ) {

		DataCenter.updateSrcPageContent(noticia, null, "/views/web/detalleNoticia.zul");
		
	}
	
	@Command
	public void album() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/album.zul");
		
	}

	
	@Command
	public void calendario() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/calendario.zul");
		
	}

}
