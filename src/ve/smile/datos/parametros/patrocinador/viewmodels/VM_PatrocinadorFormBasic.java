package ve.smile.datos.parametros.patrocinador.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Persona;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_PatrocinadorFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private List<Ciudad> ciudades;

	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;

	private Date fechaNacimiento;
	private Date fechaIngreso;
	private Persona persona;

	private Estado estado;

	private List<Estado> estados;
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String urlImagenAnterior;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {

		this.setPersona(this.getPatrocinador().getFkPersona());
		if (this.getPersona().getSexo() != null) {
			this.setSexoEnum(SexoEnum.values()[this.persona.getSexo()]);
		}

		if (this.getPersona().getFkCiudad() != null) {
			this.setEstado(this.getPersona().getFkCiudad().getFkEstado());
			this.setCiudades(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(this.getEstado().getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
		}
		if (this.getPersona().getTipoPersona() != null) {
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this.persona
					.getTipoPersona()]);
		}
		if (this.getPatrocinador().getFkPersona() != null
				&& this.getPatrocinador().getFkPersona().getFkMultimedia() != null) {
			this.setUrlImagenAnterior(this.getPatrocinador().getFkPersona()
					.getFkMultimedia().getUrl());
			this.setUrlImage(this.getPatrocinador().getFkPersona()
					.getFkMultimedia().getUrl());
		} else {
			this.getPersona().setFkMultimedia(new Multimedia());
			this.setUrlImagenAnterior(new String());
		}
	}

	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPersona().setTipoPersona(tipoPersonaEnum.ordinal());
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums() {
		if (this.tipoPersonaEnums == null) {
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty()) {
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values()) {
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums) {
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		if (persona == null) {
			persona = new Persona();
		}
		this.persona = persona;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getPatrocinador().setFechaIngreso(fechaIngreso.getTime());
	}

	public List<SexoEnum> getSexoEnums() {
		if (this.sexoEnums == null) {
			this.sexoEnums = new ArrayList<>();
		}
		if (this.sexoEnums.isEmpty()) {
			for (SexoEnum sexoEnum : SexoEnum.values()) {
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums) {
		this.sexoEnums = sexoEnums;
	}

	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPersona().setSexo(sexoEnum.ordinal());
	}

	@Command("changeEstado")
	@NotifyChange({ "ciudades", "persona" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();

		criterios
				.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}

	// @Override
	// public BufferedImage getImageContent() {
	// try {
	// return loadImage();
	// } catch (Exception e) {
	// return null;
	// }
	// }
	//
	// @Override
	// public void onUploadImageSingle(UploadEvent event, String idUpload) {
	// org.zkoss.util.media.Media media = event.getMedia();
	// if (media instanceof org.zkoss.image.Image) {
	// bytes = media.getByteData();
	// this.nameImage = media.getName();
	// this.extensionImage = media.getFormat();
	// if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage
	// .lastIndexOf(".") + 1))) {
	// Multimedia multimedia = new Multimedia();
	// multimedia.setNombre(nameImage);
	// multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
	// .ordinal());
	// multimedia.setUrl(new StringBuilder()
	// .append("/Smile/Patrocinador/").append(nameImage)
	// .toString());
	// multimedia.setExtension(UtilMultimedia
	// .stringToExtensionEnum(
	// nameImage.substring(this.nameImage
	// .lastIndexOf(".") + 1)).ordinal());
	// multimedia.setDescripcion("Imgen del patrocinador.");
	// this.getPersona().setFkMultimedia(multimedia);
	// } else {
	// this.getPersona().setFkMultimedia(null);
	// Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
	//
	// }
	//
	// }
	// }
	//
	// @Override
	// public void onRemoveImageSingle(String idUpload) {
	// bytes = null;
	// this.getPersona().setFkMultimedia(null);
	// }
	//
	// public byte[] getBytes() {
	// return bytes;
	// }
	//
	// public void setBytes(byte[] bytes) {
	// this.bytes = bytes;
	// }
	//
	// private BufferedImage loadImage() throws Exception {
	// try {
	// Path path = Paths.get(this.getUrlImagen());
	// bytes = Files.readAllBytes(path);
	// return ImageIO.read(new File(this.getUrlImagen()));
	// } catch (Exception e) {
	// return null;
	// }
	// }

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
		this.getPatrocinador().setFkPersona(persona);
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			Patrocinador patrocinador = this.getPatrocinador();
			patrocinador.setFechaIngreso(this.getFechaIngreso().getTime());
			patrocinador.setFechaSalida(new Date().getTime());

			if (bytes != null) {
				Multimedia multimedia = this.getPersona().getFkMultimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
						extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);
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
				this.getPersona().setFkMultimedia(multimedia);
			}
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.incluir(this.getPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				return true;
			}
			this.getPersona().setIdPersona(
					((Double) payloadPersonaResponse.getInformacion("id"))
							.intValue());
			patrocinador.setFkPersona(this.getPersona());
			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.incluir(getPatrocinador());
			if (!UtilPayload.isOK(payloadPatrocinadorResponse)) {
				Alert.showMessage(payloadPatrocinadorResponse);
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

					if (this.getPersona().getFkMultimedia() == null
							|| this.getPersona().getFkMultimedia()
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

						this.getPersona().setFkMultimedia(multimedia);

					} else {
						Multimedia multimedia = this.getPersona()
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
			if (bytes == null && this.getPersona().getFkMultimedia() != null) {
				File file = new File(this.getUrlImagenAnterior());

				if (file.exists()) {

					if (!file.delete()) {

						UtilDialog
								.showMessageBoxError("Ha ocurrido un error al sustituir la imagen");
						return true;
					}
				}
				// Multimedia multimedia = this.getPersona().getFkMultimedia();
				getPersona().setFkMultimedia(null);
			}

			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(getPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				return true;
			}

			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.modificar(getPatrocinador());
			if (!UtilPayload.isOK(payloadPatrocinadorResponse)) {
				Alert.showMessage(payloadPatrocinadorResponse);
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

	public Patrocinador getPatrocinador() {
		return (Patrocinador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateInteger(this.getPersona().getTipoPersona(),
					"Tipo de persona", ValidateOperator.LESS_THAN, 2);

			if (this.getTipoPersonaEnum().equals(TipoPersonaEnum.NATURAL)) {
				UtilValidate.validateString(this.getPersona()
						.getIdentificacion(), "Cédula", 35);
				UtilValidate.validateString(this.getPersona().getNombre(),
						"Nombre", 150);
				UtilValidate.validateString(this.getPersona().getApellido(),
						"Apellido", 150);
				UtilValidate.validateInteger(this.getPersona().getSexo(),
						"Sexo", ValidateOperator.LESS_THAN, 2);
				UtilValidate.validateDate(this.getPersona()
						.getFechaNacimiento(), "Fecha de nacimiento",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(new Date()), "DD/MM/YYYY");
			} else {
				UtilValidate.validateString(this.getPersona()
						.getIdentificacion(), "RIF", 35);
				UtilValidate.validateString(this.getPersona().getNombre(),
						"Nombre", 150);
			}

			UtilValidate
					.validateNull(this.getPersona().getFkCiudad(), "Ciudad");
			UtilValidate.validateDate(this.getPatrocinador().getFechaIngreso(),
					"Fecha de ingreso", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
					"dd/MM/yyyy");
			UtilValidate.validateString(this.getPersona().getDireccion(),
					"Dirección", 250);
			UtilValidate.validateNull(this.getPersona().getFkMultimedia(),
					"Imagen");
			UtilValidate.validateString(this.getPersona().getTelefono1(),
					"Teléfono 1", 25);

			UtilValidate.validateString(this.getPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(this.getPersona().getCorreo(),
					"Correo", 100);

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
						.append("/Smile/Patrocinador/p_").append(nameImage)
						.toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getPatrocinador().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getPatrocinador().getFkPersona().setFkMultimedia(null);
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
