package ve.smile.web.viewmodels.albumes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.Command;

import karen.core.crux.session.DataCenter;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.MultimediaAlbum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaAlbumResponse;

public class VM_Album {
	
	private List<MultimediaAlbum> multimediaAlbum;
	private List<EventoPlanificado> eventoPlanificado;
	private Integer cant;
	private Date fechaEjecutado;
	private String fechaPublicacionFormat;
	private String directorio;
	
	public Date getFechaPublicacion() {
		if (getevento() != null){
			this.fechaEjecutado = new Date(getevento().getFechaEjecutada());
		}else{
			this.fechaEjecutado = new Date(123123123);
		}
		return fechaEjecutado;
	}

	public Integer getCant() {
		this.cant = getmultimediaalbum().size();
		return cant;
	}

	public Album getalbum() {
		return (Album) DataCenter.getEntity();
	}
	
	public List<MultimediaAlbum> getmultimediaalbum() {
		if (this.multimediaAlbum == null) {
			this.multimediaAlbum = new ArrayList<>();
		}
		this.multimediaAlbum.clear();
		if (this.multimediaAlbum.isEmpty()) {

			PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
					.consultarMultimediaAlbum(6, getalbum().getIdAlbum());

			if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
				this.multimediaAlbum.addAll(payloadMultimediaAlbumResponse
						.getObjetos());
			}

		}
		return multimediaAlbum;
	}

	@Command
	public void galeria() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/galeria.zul");
		
	}

	public String getFechaPublicacionFormat() {
		this.fechaPublicacionFormat = new SimpleDateFormat("dd-MM-yyyy").format(getFechaPublicacion());
		return fechaPublicacionFormat;
	}
	
	public EventoPlanificado getevento(){
		if (this.eventoPlanificado == null) {
			this.eventoPlanificado = new ArrayList<>();
		}
		this.eventoPlanificado.clear();
		if (this.eventoPlanificado.isEmpty()) {

			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultaEventoPlanificadoAlbum(getalbum().getIdAlbum());

			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				this.eventoPlanificado.addAll(payloadEventoPlanificadoResponse
						.getObjetos());
			}

		}
		if (eventoPlanificado.size() == 0) {
			return null;
		}else {
			return eventoPlanificado.get(0);
		}
	}
	
	public String getdirectorio(){
		
		if (getevento() != null){
			this.directorio = getevento().getFkDirectorio().getNombre();
		}else{
			this.directorio = "direccion del trabajo social";
		}
		
		return directorio;
				
	}


}
