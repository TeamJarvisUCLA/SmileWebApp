package ve.smile.administracion.organizacion.viewmodels;

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
import ve.smile.dto.Organizacion;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_OrganizacionFormBasic extends VM_WindowForm implements
		UploadImageSingle {
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		if (this.getOrganizacion() != null
				&& this.getOrganizacion().getFkMultimedia() != null
				&& this.getOrganizacion().getFkMultimedia().getIdMultimedia() != null) {

			this.setUrlImage(this.getOrganizacion().getFkMultimedia().getUrl());

			this.nameImage = this.getOrganizacion().getFkMultimedia()
					.getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getOrganizacion().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getOrganizacion().setFkMultimedia(new Multimedia());
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
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService
					.incluir(getOrganizacion());
			if (!UtilPayload.isOK(payloadOrganizacionResponse)) {
				Alert.showMessage(payloadOrganizacionResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			if (this.getBytes() != null) {

				if (this.getOrganizacion().getFkMultimedia() == null
						|| this.getOrganizacion().getFkMultimedia()
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
					Zki.save(Zki.ORGANIZACION, this.getOrganizacion()
							.getIdOrganizacion(), extensionImage, bytes);

					this.getOrganizacion().setFkMultimedia(multimedia);

				} else {
					Multimedia multimedia = this.getOrganizacion()
							.getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
					Zki.save(Zki.ORGANIZACION, this.getOrganizacion()
							.getIdOrganizacion(), extensionImage, bytes);
				}

			}

			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService
					.modificar(getOrganizacion());
			if (!UtilPayload.isOK(payloadOrganizacionResponse)) {
				Alert.showMessage(payloadOrganizacionResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	public Organizacion getOrganizacion() {
		return (Organizacion) DataCenter.getEntity();
	}

	private boolean isFormValidated() {
		try {
			UtilValidate.validateString(getOrganizacion().getRif(), "Rif", 30);
			UtilValidate.validateString(getOrganizacion().getNombre(),
					"Nombre", 500);
			UtilValidate.validateString(getOrganizacion().getDireccion(),
					"Dirección", 500);
			UtilValidate.validateString(getOrganizacion().getTelefono(),
					"Teléfono", 25);
			UtilValidate.validateString(getOrganizacion().getMision(),
					"Misión", 500);
			UtilValidate.validateString(getOrganizacion().getVision(),
					"Visión", 500);
			UtilValidate.validateNull(this.getBytes(), "Imagen");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}

	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();
		return true;
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
				if (this.getOrganizacion().getIdOrganizacion() != null) {
					this.urlImage = new StringBuilder()
							.append(Zki.ORGANIZACION)
							.append(this.getOrganizacion().getIdOrganizacion())
							.append(".").append(extensionImage).toString();
					this.nameImage = new StringBuilder()
							.append(Zki.ORGANIZACION)
							.append(this.getOrganizacion().getIdOrganizacion())
							.append(".").append(extensionImage).toString();
				}

			} else {
				this.getOrganizacion().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getOrganizacion().setFkMultimedia(null);
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
