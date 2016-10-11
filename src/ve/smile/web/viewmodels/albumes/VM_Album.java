package ve.smile.web.viewmodels.albumes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.MultimediaAlbum;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusComentarioAlbumEnum;
import ve.smile.payload.response.PayloadComentarioAlbumResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaAlbumResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_Album {
	
	private List<MultimediaAlbum> multimediaAlbum;
	private List<EventoPlanificado> eventoPlanificado;
	private List<TsPlan> tsPlan;
	private List<ComentarioAlbum> comentarioAlbum;
	private Integer cant;
	private Date fechaEjecutado;
	private String fechaPublicacionFormat;
	private String directorio;
	
	public Date getFechaPublicacion() {
		if (getevento() != null){
			this.fechaEjecutado = new Date(getevento().getFechaEjecutada());
		}else{
			this.fechaEjecutado = new Date(getts().getFechaEjecutada());
		}
		return fechaEjecutado;
	}
	
	public String getdirectorio(){
		
		if (getevento() != null){
			this.directorio = getevento().getFkDirectorio().getNombre();
		}else{
			this.directorio = getts().getFkDirectorio().getNombre();
		}
		
		return directorio;
				
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
	
	public TsPlan getts(){
		if (this.tsPlan == null) {
			this.tsPlan = new ArrayList<>();
		}
		this.tsPlan.clear();
		if (this.tsPlan.isEmpty()) {

			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultaTSPlanificadoAlbum(getalbum().getIdAlbum());

			if (UtilPayload.isOK(payloadTsPlanResponse)) {
				this.tsPlan.addAll(payloadTsPlanResponse
						.getObjetos());
			}

		}
		if (tsPlan.size() == 0) {
			return null;
		}else {
			return tsPlan.get(0);
		}
	}
	
	public List<ComentarioAlbum> getcomentarioalbum() {
		if (this.comentarioAlbum == null) {
			this.comentarioAlbum = new ArrayList<>();
		}
		this.comentarioAlbum.clear();
		if (this.comentarioAlbum.isEmpty()) {

			PayloadComentarioAlbumResponse payloadComentarioAlbumResponse = S.ComentarioAlbumService
					.consultaComentariosAlbum(getalbum().getIdAlbum(), EstatusComentarioAlbumEnum.PUBLICADO.ordinal());

			if (UtilPayload.isOK(payloadComentarioAlbumResponse)) {
				this.comentarioAlbum.addAll(payloadComentarioAlbumResponse
						.getObjetos());
			}

		}
		return comentarioAlbum;
	}

	@Command
	public void abrirComentar(){
		DialogData dialogData = new DialogData();
		UtilDialog.showDialog("/views/web/comentarAlbum.zul", dialogData);
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void refreshComentarios(){}
}
