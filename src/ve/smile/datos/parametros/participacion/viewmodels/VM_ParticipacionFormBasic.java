package ve.smile.datos.parametros.participacion.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Participacion;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_ParticipacionFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		if (this.getParticipacion() != null
				&& this.getParticipacion().getFkMultimedia() != null) {

			this.setUrlImage(this.getParticipacion().getFkMultimedia().getUrl());

			this.nameImage = this.getParticipacion().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getParticipacion().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getParticipacion().setFkMultimedia(new Multimedia());
		}
	}

	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			
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
				this.getParticipacion().setFkMultimedia(multimedia);
			}
			
			PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
					.incluir(getParticipacion());

			this.getParticipacion().setIdParticipacion(
					((Double) payloadParticipacionResponse.getInformacion("id"))
							.intValue());
			if (bytes != null) {
				Zki.save(Zki.PARTICIPACION, getParticipacion().getIdParticipacion(),
						extensionImage, bytes);
				Multimedia multimedia = this.getParticipacion().getFkMultimedia();
				multimedia.setNombre(Zki.PARTICIPACION
						+ this.getParticipacion().getIdParticipacion() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.PARTICIPACION
						+ getParticipacion().getIdParticipacion() + "."
						+ this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			if (!UtilPayload.isOK(payloadParticipacionResponse)) {
				Alert.showMessage(payloadParticipacionResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			
			if (this.getBytes() != null) {

				if (this.getParticipacion().getFkMultimedia() == null
						|| this.getParticipacion().getFkMultimedia()
								.getIdMultimedia() == null) {

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
					Zki.save(Zki.PARTICIPACION, this.getParticipacion()
							.getIdParticipacion(), extensionImage, bytes);

					this.getParticipacion().setFkMultimedia(multimedia);

				} else {
					Multimedia multimedia = this.getParticipacion()
							.getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
					Zki.save(Zki.PARTICIPACION, this.getParticipacion()
							.getIdParticipacion(), extensionImage, bytes);
				}

			}
			Multimedia multimedia = this.getParticipacion().getFkMultimedia();

			if (bytes == null && this.getParticipacion().getFkMultimedia() != null) {
				Zki.remove(this.getParticipacion().getFkMultimedia().getUrl());
				getParticipacion().setFkMultimedia(null);
			}
			
			PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
					.modificar(getParticipacion());

			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			
			if (!UtilPayload.isOK(payloadParticipacionResponse)) {
				Alert.showMessage(payloadParticipacionResponse);
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

	public Participacion getParticipacion() {
		return (Participacion) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		// TODO: Validar multimedia
		try {
			UtilValidate.validateString(getParticipacion().getNombre(),
					"Nombre", 100);
			UtilValidate.validateString(getParticipacion().getDescripcion(),
					"Descripción", 250);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
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

	@Override
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
				if (this.getParticipacion().getIdParticipacion() != null) {
					this.urlImage = new StringBuilder().append(Zki.PARTICIPACION)
							.append(this.getParticipacion().getIdParticipacion())
							.append(".").append(extensionImage).toString();
					this.nameImage = new StringBuilder().append(Zki.PARTICIPACION)
							.append(this.getParticipacion().getIdParticipacion())
							.append(".").append(extensionImage).toString();
				}

			} else {
				this.getParticipacion().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getParticipacion().setFkMultimedia(null);
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

}
