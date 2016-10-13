package ve.smile.datos.parametros.directorio.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

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

			this.setUrlImage(this.getDirectorio().getFkMultimedia().getUrl());
		} else {
			this.getDirectorio().setFkMultimedia(new Multimedia());
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

				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);

				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());
				this.getDirectorio().setFkMultimedia(multimedia);
			}

			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.incluir(getDirectorio());
			this.getDirectorio().setIdDirectorio(
					((Double) payloadDirectorioResponse.getInformacion("id"))
							.intValue());
			if (bytes != null) {
				Zki.save(Zki.DIRECTORIO, getDirectorio().getIdDirectorio(),
						extensionImage, bytes);
				Multimedia multimedia = this.getDirectorio().getFkMultimedia();
				multimedia.setNombre(Zki.DIRECTORIO
						+ this.getDirectorio().getIdDirectorio() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.DIRECTORIO
						+ getDirectorio().getIdDirectorio() + "."
						+ this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);
			}

			Alert.showMessage(payloadDirectorioResponse);
			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (this.getBytes() != null) {

				if (this.getDirectorio().getFkMultimedia() == null
						|| this.getDirectorio().getFkMultimedia()
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
					Zki.save(Zki.DIRECTORIO, this.getDirectorio()
							.getIdDirectorio(), extensionImage, bytes);

					this.getDirectorio().setFkMultimedia(multimedia);

				} else {
					Multimedia multimedia = this.getDirectorio()
							.getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
					Zki.save(Zki.DIRECTORIO, this.getDirectorio()
							.getIdDirectorio(), extensionImage, bytes);
				}

			}
			Multimedia multimedia = this.getDirectorio().getFkMultimedia();

			if (bytes == null && this.getDirectorio().getFkMultimedia() != null) {
				Zki.remove(this.getDirectorio().getFkMultimedia().getUrl());
				getDirectorio().setFkMultimedia(null);
			}

			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.modificar(getDirectorio());

			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

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

				if (this.getDirectorio().getIdDirectorio() != null) {
					this.urlImage = new StringBuilder().append(Zki.DIRECTORIO)
							.append(this.getDirectorio().getIdDirectorio())
							.append(".").append(extensionImage).toString();
					this.nameImage = new StringBuilder().append(Zki.DIRECTORIO)
							.append(this.getDirectorio().getIdDirectorio())
							.append(".").append(extensionImage).toString();
				}
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

}
