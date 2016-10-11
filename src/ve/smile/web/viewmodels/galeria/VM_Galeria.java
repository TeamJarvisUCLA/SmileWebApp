package ve.smile.web.viewmodels.galeria;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import karen.core.crux.session.DataCenter;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.Multimedia;
import ve.smile.dto.MultimediaAlbum;
import ve.smile.enums.EstatusAlbumEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadAlbumResponse;
import ve.smile.payload.response.PayloadMultimediaAlbumResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class VM_Galeria {

	private List<Album> albumes;
	private List<MultimediaAlbum> multimediaAlbum;
	private List<Multimedia> multiemdiaGaleria;
	private Integer cantMultGaleria = 6;

	public List<Album> getalbumes() {
		if (this.albumes == null) {
			this.albumes = new ArrayList<>();
		}
		if (this.albumes.isEmpty()) {

			PayloadAlbumResponse payloadAlbumResponse = S.AlbumService
					.consultarAlbumCantidad(5, EstatusAlbumEnum.PUBLICADO.ordinal());

			this.albumes.addAll(payloadAlbumResponse.getObjetos());
		}

		return albumes;
	}
	
	public List<Multimedia> getmultiemdiaGaleria() {
		if (this.multiemdiaGaleria == null) {
			this.multiemdiaGaleria = new ArrayList<>();
		}
		if (this.multiemdiaGaleria.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(cantMultGaleria, TipoMultimediaEnum.GALERIA.ordinal());

			this.multiemdiaGaleria.addAll(payloadMultimediaResponse.getObjetos());
		}

		return multiemdiaGaleria;
	}

	public List<MultimediaAlbum> getmultimediaalbum(Integer album) {
		if (this.multimediaAlbum == null) {
			this.multimediaAlbum = new ArrayList<>();
		}
		this.multimediaAlbum.clear();
		if (this.multimediaAlbum.isEmpty()) {

			PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
					.consultarMultimediaAlbum(100, album);

			if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
				this.multimediaAlbum.addAll(payloadMultimediaAlbumResponse
						.getObjetos());
			}

		}
		return multimediaAlbum;
	}

	public String cant(Integer idAlbum ) {
		return String.valueOf(getmultimediaalbum(idAlbum).size());
	}
	
	public String primerImgAlbum(Integer idAlbum ) {
		if (getmultimediaalbum(idAlbum).size() == 0) {
			return "";
		} else {
			return String.valueOf(getmultimediaalbum(idAlbum).get(0).getFkMultimedia().getUrl());
		}
		
	}
	
	@Command
	public void album(@BindingParam("album") Album album ) {
		DataCenter.updateSrcPageContent(album, null, "/views/web/album.zul");
		
	}
	
	@Command
	@NotifyChange({"multiemdiaGaleria"})
	public void verMas() {
		cantMultGaleria += 3;
		if (this.multiemdiaGaleria == null) {
			this.multiemdiaGaleria = new ArrayList<>();
		}
		this.multiemdiaGaleria.clear();
		if (this.multiemdiaGaleria.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(cantMultGaleria, TipoMultimediaEnum.GALERIA.ordinal());

			this.multiemdiaGaleria.addAll(payloadMultimediaResponse.getObjetos());
		}
		Clients.evalJavaScript("f_zoom();");
	}

}
