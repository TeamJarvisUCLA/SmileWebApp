package ve.smile.datos.parametros.directorio.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Directorio;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_DirectorioFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private List<Ciudad> ciudads;
	private Estado estado;

	private List<Estado> estados;
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String urlImagenAnterior;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit_VM_DirectorioFormBasic() {
		// NOTHING OK!
		if (this.getDirectorio().getLongitud() == null
				|| this.getDirectorio().getLatitud() == null) {
			this.getDirectorio().setLatitud(10.066560);
			this.getDirectorio().setLongitud(-69.312565);
		}

		if (this.getDirectorio().getFkCiudad() != null) {
			this.setEstado(this.getDirectorio().getFkCiudad().getFkEstado());
			this.setCiudads(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(estado.getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudads().addAll(payloadCiudadResponse.getObjetos());
		}

		if (this.getDirectorio() != null
				&& this.getDirectorio().getFkMultimedia() != null) {
			this.setUrlImagenAnterior(this.getDirectorio().getFkMultimedia()
					.getUrl());
			this.setUrlImage(this.getDirectorio().getFkMultimedia().getUrl());
		} else {
			this.getDirectorio().setFkMultimedia(new Multimedia());
			this.setUrlImagenAnterior(new String());
		}

	}

	@Command("changeEstado")
	@NotifyChange({ "ciudads" })
	public void changeEstado() {
		this.setCiudads(null);
		this.getDirectorio().setFkCiudad(new Ciudad());
		Map<String, String> criterios = new HashMap<>();
		criterios
				.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudads().addAll(payloadCiudadResponse.getObjetos());
	}

	@SuppressWarnings("deprecation")
	@Command
	@NotifyChange({ "directorio" })
	public void changeMarker(@BindingParam("event") MapMouseEvent event) {
		Gmarker gmarker = event.getGmarker();
		if (gmarker != null) {
			gmarker.setOpen(true);
		} else {
			this.getDirectorio().setLatitud(event.getLat());
			this.getDirectorio().setLongitud(event.getLng());
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
				this.getDirectorio().setFkMultimedia(multimedia);
				try {
					FileUtils.writeByteArrayToFile(
							new File(multimedia.getUrl()), bytes);
				} catch (IOException e) {
					UtilDialog
							.showMessageBoxError("Ha ocurrido un error al guardar la imagen");
					return true;
				}

				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);
				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());
				this.getDirectorio().setFkMultimedia(multimedia);
			}

			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.incluir(getDirectorio());
			Alert.showMessage(payloadDirectorioResponse);
			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (this.getBytes() != null
					&& !this.getUrlImagenAnterior().equalsIgnoreCase(
							this.getUrlImage())) {
				try {

					if (this.getUrlImagenAnterior() != null
							&& !this.getUrlImagenAnterior()
									.equalsIgnoreCase("")) {

						File file = new File(this.getUrlImagenAnterior());

						if (file.exists()) {

							if (!file.delete()) {

								UtilDialog
										.showMessageBoxError("Ha ocurrido un error al sustituir la imagen");
								return true;
							}
						}
					}
					try {
						FileUtils.writeByteArrayToFile(new File(urlImage),
								bytes);
					} catch (IOException e) {
						UtilDialog
								.showMessageBoxError("Ha ocurrido un error al guardar la imagen");
						return true;
					}

					if (this.getDirectorio().getFkMultimedia() == null
							|| this.getDirectorio().getFkMultimedia()
									.getIdMultimedia() == null) {

						Multimedia multimedia = new Multimedia();
						multimedia.setNombre(nameImage);
						multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
								.ordinal());
						multimedia.setUrl(this.getUrlImage());
						multimedia.setExtension(UtilMultimedia
								.stringToExtensionEnum(extensionImage)
								.ordinal());
						multimedia.setDescripcion(typeMedia);
						PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
								.incluir(multimedia);
						if (!UtilPayload.isOK(payloadMultimediaResponse)) {
							Alert.showMessage(payloadMultimediaResponse);
							return true;
						}
						multimedia
								.setIdMultimedia(((Double) payloadMultimediaResponse
										.getInformacion("id")).intValue());
						this.getDirectorio().setFkMultimedia(multimedia);

					} else {
						Multimedia multimedia = this.getDirectorio()
								.getFkMultimedia();
						multimedia.setNombre(nameImage);
						multimedia.setDescripcion(typeMedia);
						multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
								.ordinal());
						multimedia.setUrl(this.getUrlImage());
						multimedia.setExtension(UtilMultimedia
								.stringToExtensionEnum(extensionImage)
								.ordinal());
						PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
								.modificar(multimedia);
						if (!UtilPayload.isOK(payloadMultimediaResponse)) {
							Alert.showMessage(payloadMultimediaResponse);
							return true;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (bytes == null && this.getDirectorio().getFkMultimedia() != null) {
				File file = new File(this.getUrlImagenAnterior());

				if (file.exists()) {

					if (!file.delete()) {

						UtilDialog
								.showMessageBoxError("Ha ocurrido un error al sustituir la imagen");
						return true;
					}
				}
				// Multimedia multimedia =
				// this.getDirectorio().getFkMultimedia();
				getDirectorio().setFkMultimedia(null);
			}

			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.modificar(getDirectorio());

			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				Alert.showMessage(payloadDirectorioResponse);
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

	public Directorio getDirectorio() {
		return (Directorio) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getDirectorio().getDireccion(),
					"Dirección", 250);
			UtilValidate.validateString(getDirectorio().getNombre(), "Nombre",
					100);
			UtilValidate.validateString(getDirectorio().getTelefono(),
					"Teléfono", 80);
			UtilValidate.validateString(getDirectorio().getUrl(), "URL", 200);
			UtilValidate.validateNull(getDirectorio().getFkCiudad(), "Ciudad");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

	public List<Ciudad> getCiudads() {
		if (this.ciudads == null) {
			this.ciudads = new ArrayList<>();
		}
		return ciudads;
	}

	public void setCiudads(List<Ciudad> ciudads) {
		this.ciudads = ciudads;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		if (this.estados == null) {
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty()) {
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse)) {
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
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

	public String getUrlImagenAnterior() {
		return urlImagenAnterior;
	}

	public void setUrlImagenAnterior(String urlImagenAnterior) {
		this.urlImagenAnterior = urlImagenAnterior;
	}

	public String getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}

	@Override
	public BufferedImage getImageContent() {
		try {
			return loadImage();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();

		if (media instanceof org.zkoss.image.Image) {

			if (UtilMultimedia.validateImage(media.getName().substring(
					media.getName().lastIndexOf(".") + 1))) {
				this.nameImage = media.getName();
				this.extensionImage = nameImage.substring(this.nameImage
						.lastIndexOf(".") + 1);
				System.out.println(extensionImage);
				this.bytes = media.getByteData();

				this.urlImage = new StringBuilder()
						.append("/Smile/Directorio/d_").append(nameImage)
						.toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getDirectorio().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getDirectorio().setFkMultimedia(null);
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

	private BufferedImage loadImage() throws Exception {
		try {
			Path path = Paths.get(this.getUrlImage());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImage()));
		} catch (Exception e) {
			return null;
		}
	}

}
