package ve.smile.web.viewmodels.main;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Participacion;
import ve.smile.dto.Patrocinador;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;

public class VM_Main {
		
	private List<Cartelera> noticias;
	private List<Participacion> participacion;
	private List<Organizacion> organizacion;
	private List<Multimedia> multiemdiaGaleria;
	private List<Patrocinador> patrocinadores;
	private List<Multimedia> imagenPrincipal;
	
	public List<Multimedia> getImagenPrincipal() {
		if (this.imagenPrincipal == null) {
			this.imagenPrincipal = new ArrayList<>();
		}
		this.imagenPrincipal.clear();
		if (this.imagenPrincipal.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(1, TipoMultimediaEnum.BANNER.ordinal());

			this.imagenPrincipal.addAll(payloadMultimediaResponse.getObjetos());
		}

		return imagenPrincipal;
	}
		
	public List<Patrocinador> getPatrocinadores() {
		if (this.patrocinadores == null) {
			this.patrocinadores = new ArrayList<>();
		}
		if (this.patrocinadores.isEmpty()) {		
			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.consultarAllPatrocinador();
			this.patrocinadores.addAll(payloadPatrocinadorResponse.getObjetos());
		}
		return patrocinadores;
	}

	public void setPatrocinadores(List<Patrocinador> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public List<Cartelera> getnoticias(){
		if (this.noticias == null) {
			this.noticias = new ArrayList<>();
		}
		if (this.noticias.isEmpty()) {		
			PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService
					.consultaCarteleraPorParametro(3, TipoCarteleraEnum.NOTICIAS.ordinal());			
			this.noticias.addAll(payloadCarteleraResponse.getObjetos());
		}
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
	
	public List<Multimedia> getmultiemdiaGaleria() {
		if (this.multiemdiaGaleria == null) {
			this.multiemdiaGaleria = new ArrayList<>();
		}
		if (this.multiemdiaGaleria.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(5, TipoMultimediaEnum.GALERIA.ordinal());

			this.multiemdiaGaleria.addAll(payloadMultimediaResponse.getObjetos());
		}

		return multiemdiaGaleria;
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
