package ve.smile.datos.parametros.valores.viewmodels;

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
import ve.smile.dto.Valores;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.payload.response.PayloadValoresResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_ValoresFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!

		if (this.getValores() != null
				&& this.getValores().getFkMultimedia() != null) {

			this.setUrlImage(this.getValores().getFkMultimedia().getUrl());

			this.nameImage = this.getValores().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getValores().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getValores().setFkMultimedia(new Multimedia());
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
			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService
					.buscarOrganizacion();
			if (!UtilPayload.isOK(payloadOrganizacionResponse)) {
				Alert.showMessage(payloadOrganizacionResponse);
				return true;
			}
			this.getValores().setFkOrganizacion(
					payloadOrganizacionResponse.getObjetos().get(0));

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
				this.getValores().setFkMultimedia(multimedia);
			}

			PayloadValoresResponse payloadValoresResponse = S.ValoresService
					.incluir(getValores());
			if (!UtilPayload.isOK(payloadValoresResponse)) {
				return true;
			}

			if (bytes != null) {
				Zki.save(Zki.VALORES, getValores().getIdValores(),
						extensionImage, bytes);
				Multimedia multimedia = this.getValores().getFkMultimedia();
				multimedia.setNombre(Zki.VALORES
						+ this.getValores().getIdValores() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.VALORES + getValores().getIdValores()
						+ "." + this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			Alert.showMessage(payloadValoresResponse);
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (this.getBytes() != null) {

				if (this.getValores().getFkMultimedia() == null
						|| this.getValores().getFkMultimedia()
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
					if (!UtilPayload.isOK(payloadMultimediaResponse)) {
						Alert.showMessage(payloadMultimediaResponse);
					}
					multimedia
							.setIdMultimedia(((Double) payloadMultimediaResponse
									.getInformacion("id")).intValue());
					Zki.save(Zki.VALORES, this.getValores().getIdValores(),
							extensionImage, bytes);

					this.getValores().setFkMultimedia(multimedia);

				} else {
					Multimedia multimedia = this.getValores().getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
					Zki.save(Zki.VALORES, this.getValores().getIdValores(),
							extensionImage, bytes);
				}

			}
			Multimedia multimedia = this.getValores().getFkMultimedia();

			if (bytes == null && this.getValores().getFkMultimedia() != null) {
				Zki.remove(this.getValores().getFkMultimedia().getUrl());
				getValores().setFkMultimedia(null);
			}

			PayloadValoresResponse payloadValoresResponse = S.ValoresService
					.modificar(getValores());

			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

			if (!UtilPayload.isOK(payloadValoresResponse)) {
				Alert.showMessage(payloadValoresResponse);
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

	public Valores getValores() {
		return (Valores) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate
					.validateString(getValores().getNombre(), "Nombre", 100);
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
				if (this.getValores().getIdValores() != null) {
					this.urlImage = new StringBuilder().append(Zki.VALORES)
							.append(this.getValores().getIdValores())
							.append(".").append(extensionImage).toString();
					this.nameImage = new StringBuilder().append(Zki.VALORES)
							.append(this.getValores().getIdValores())
							.append(".").append(extensionImage).toString();
				}

			} else {
				this.getValores().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getValores().setFkMultimedia(null);
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
