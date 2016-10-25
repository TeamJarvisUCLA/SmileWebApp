package ve.smile.administracion.portalweb.noticias.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.dto.Cartelera;
import ve.smile.dto.Multimedia;
import ve.smile.enums.EstatusCarteleraEnum;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.enums.TipoMultimediaEnum;

public class VM_CarteleraFormBasic extends VM_WindowForm {
	
	

	private List<TipoCarteleraEnum> TipoEnums;
	private TipoCarteleraEnum tipoEnum;
	
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	private Date fechaFinalizacion = new Date();
	private Date fechaInicio=new Date();
	Cartelera cartelera= new Cartelera();
	
	
	@Init(superclass = true)
	public void childInit_VM_CarteleraFormBasic() {		
		//NOTHING OK!

		if (this.getCartelera().getFechaInicio() != null) {
			this.setFechaInicio(new Date(this.getCartelera().getFechaInicio()));
		} else {
			this.setFechaInicio(new Date());
		}
		
		if (this.getCartelera() != null
				&& this.getCartelera().getFkMultimedia() != null) {

			this.setUrlImage(this.getCartelera().getFkMultimedia().getUrl());

			this.nameImage = this.getCartelera().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getCartelera().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getCartelera().setFkMultimedia(new Multimedia());
		}
		
		if (this.getCartelera().getFechaFinalizacion() != null) {
			this.setFechaFinalizacion(new Date(this.getCartelera().getFechaFinalizacion()));
		} else {
			this.setFechaFinalizacion(new Date());
		}
		
		
	
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}
		
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			
			this.getCartelera().setTipoCartelera(TipoCarteleraEnum.NOTICIAS.ordinal());
			this.getCartelera().setEstatusCartelera(EstatusCarteleraEnum.PUBLICADO.ordinal());
			
			this.getCartelera().setFechaFinalizacion(
					this.getFechaFinalizacion().getTime());
			
			this.getCartelera().setFechaInicio(
					this.getFechaInicio().getTime());
			
			if (bytes != null) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
						extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);

				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);
			
				
				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());
				this.getCartelera().setFkMultimedia(multimedia);
			}
			
			PayloadCarteleraResponse payloadCarteleraResponse =
					S.CarteleraService.incluir(getCartelera());
			this.getCartelera().setIdCartelera(
					((Double) payloadCarteleraResponse.getInformacion("id"))
							.intValue());

			
			
			if (bytes != null) {
				Zki.save(Zki.NOTICIAS, getCartelera().getIdCartelera(),
						extensionImage, bytes);
				Multimedia multimedia = this.getCartelera().getFkMultimedia();
				multimedia.setNombre(Zki.NOTICIAS
						+ this.getCartelera().getIdCartelera() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.NOTICIAS + getCartelera().getIdCartelera()
						+ "." + this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			Alert.showMessage(payloadCarteleraResponse);
			if (!UtilPayload.isOK(payloadCarteleraResponse)) {
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))  {
			

			if (this.getBytes() != null ) {
				
				System.out.println("entre ");
				if (this.getCartelera().getFkMultimedia() == null
						|| this.getCartelera().getFkMultimedia()
								.getIdMultimedia() == null) {
					
					System.out.println("entre 2");

					Multimedia multimedia = new Multimedia();
					multimedia.setNombre(nameImage);
					multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
							.ordinal());
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					multimedia.setDescripcion(typeMedia);
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.incluir(multimedia);
					multimedia
							.setIdMultimedia(((Double) payloadMultimediaResponse
									.getInformacion("id")).intValue());
					Zki.save(Zki.NOTICIAS, this.getCartelera().getIdCartelera(),
							extensionImage, bytes);
					this.getCartelera().setFkMultimedia(multimedia);
				} else {
					System.out.println("entre 3");

					Multimedia multimedia = this.getCartelera().getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
				
					Zki.save(Zki.NOTICIAS, this.getCartelera().getIdCartelera(),
							extensionImage, bytes);
				}

			}

			this.getCartelera().setTipoCartelera(TipoCarteleraEnum.NOTICIAS.ordinal());
			this.getCartelera().setEstatusCartelera(EstatusCarteleraEnum.PUBLICADO.ordinal());

			Multimedia multimedia = this.getCartelera().getFkMultimedia();
			
			if (bytes == null && this.getCartelera().getFkMultimedia() != null) {
				Zki.remove(this.getCartelera().getFkMultimedia().getUrl());
				getCartelera().setFkMultimedia(null);
			}

			PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService
					.modificar(getCartelera());
			
		
			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			
			if (!UtilPayload.isOK(payloadCarteleraResponse)) {
				Alert.showMessage(payloadCarteleraResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
	
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}




	
	
	public List<TipoCarteleraEnum> getTipoEnums() {
		if (this.TipoEnums == null) {
			TipoEnums = new ArrayList<TipoCarteleraEnum>();
		}
		if (this.TipoEnums.isEmpty()) {
			for (TipoCarteleraEnum tipoEnum : TipoCarteleraEnum.values()) {
				this.TipoEnums.add(tipoEnum);
			}
		}
		return TipoEnums;
	}

	public void setTipoEnums(List<TipoCarteleraEnum> TipoEnums) {
		this.TipoEnums = TipoEnums;
	}

	public TipoCarteleraEnum getTipoEnum() {
		return tipoEnum;
	}

	public void setTipoCarteleraEnum(TipoCarteleraEnum tipoEnum) {
		this.tipoEnum = tipoEnum;
		this.getCartelera().setTipoCartelera(tipoEnum.ordinal());

	}

	public Cartelera getCartelera() {
		return cartelera;
	}

	public void setCartelera(Cartelera cartelera) {
		this.cartelera = cartelera;
	}

	public String getNameImage() {
		return nameImage;
	}

	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	public String getExtensionImage() {
		return extensionImage;
	}

	public void setExtensionImage(String extensionImage) {
		this.extensionImage = extensionImage;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}

//	@Override
	public BufferedImage getImageContent() {
		if (bytes != null) {
			try {
				return ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {
				return null;
			}
		}

		if (urlImage != null) {
			bytes = Zki.getBytes(urlImage);
			return Zki.getBufferedImage(urlImage);
		}

		return null;
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();

		if (media instanceof org.zkoss.image.Image) {

			if (UtilMultimedia.validateImage(media.getName().substring(
					media.getName().lastIndexOf(".") + 1))) {

				this.extensionImage = media.getName().substring(
						media.getName().lastIndexOf(".") + 1);

				this.bytes = media.getByteData();
				this.typeMedia = media.getContentType();

			} else {
				this.getCartelera().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getCartelera().setFkMultimedia(null);
			Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	
	
	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	
	
	
	
	public boolean isFormValidated() {
		try {

			
			
			UtilValidate.validateString(this.getCartelera().getDescripcion(),
					"Descripcion", 200);
			
			UtilValidate.validateString(this.getCartelera().getTitulo(),
					"Titulo", 100);

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
