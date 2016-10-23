package ve.smile.gestion.voluntariado.voluntario.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Profesion;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Voluntario;
import ve.smile.dto.VoluntarioProfesion;
import ve.smile.enums.SexoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadVoluntarioProfesionResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_VoluntarioFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private Date fechaNacimiento = new Date();
	private Date fechaIngreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private Estado estado;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String typeMedia;

	private List<Profesion> profesiones;
	private Set<Profesion> profesionesSeleccionadas;
	private List<Profesion> voluntarioProfesiones;
	private Set<Profesion> voluntarioProfesionesSeleccionadas;

	@Init
	public void childInit() {
		if (this.getVoluntario().getFkPersona().getSexo() != null) {
			this.setSexoEnum(SexoEnum.values()[this.getVoluntario()
					.getFkPersona().getSexo()]);
		}

		if (this.getVoluntario().getFkPersona().getTipoPersona() != null) {
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this
					.getVoluntario().getFkPersona().getTipoPersona()]);
		}

		if (this.getVoluntario().getFkPersona() != null
				&& this.getVoluntario().getFkPersona().getFkMultimedia() != null
				&& this.getVoluntario().getFkPersona().getFkMultimedia()
						.getIdMultimedia() != null) {
			this.setUrlImage(this.getVoluntario().getFkPersona()
					.getFkMultimedia().getUrl());
			this.nameImage = this.getVoluntario().getFkPersona()
					.getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getVoluntario().getFkPersona()
					.getFkMultimedia().getDescripcion();
		} else {
			this.getVoluntario().getFkPersona()
					.setFkMultimedia(new Multimedia());
		}

		if (this.getVoluntario().getFechaIngreso() != null) {
			this.setFechaIngreso(new Date(this.getVoluntario()
					.getFechaIngreso()));
		} else {
			this.setFechaIngreso(new Date());
		}

		if (this.getVoluntario().getFkPersona().getFkCiudad() != null) {
			this.setEstado(this.getVoluntario().getFkPersona().getFkCiudad()
					.getFkEstado());
			this.getCiudades().clear();
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(estado.getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
		}

		// BUSCAR PROFESIONES DEL VOLUNTARIO
		this.setVoluntarioProfesiones(null);
		Map<String, String> criterios = new HashMap<>();
		criterios.put("fkVoluntario.idVoluntario",
				String.valueOf(this.getVoluntario().getIdVoluntario()));
		PayloadVoluntarioProfesionResponse payloadVoluntarioProfesionResponse = S.VoluntarioProfesionService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (payloadVoluntarioProfesionResponse.getObjetos() != null) {
			for (VoluntarioProfesion vP : payloadVoluntarioProfesionResponse
					.getObjetos()) {
				this.getVoluntarioProfesiones().add(vP.getFkProfesion());
			}
		}
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	// M�TODOS DE LAS LISTAS
	public boolean disabledProfesion(Profesion profesion) {
		return this.getVoluntarioProfesiones().contains(profesion);
	}

	public List<Profesion> getProfesiones() {
		if (this.profesiones == null) {
			this.profesiones = new ArrayList<>();
		}
		if (this.profesiones.isEmpty()) {
			PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
					.consultarTodos();
			this.profesiones.addAll(payloadProfesionResponse.getObjetos());
		}
		return profesiones;
	}

	public void setProfesiones(List<Profesion> profesiones) {
		this.profesiones = profesiones;
	}

	public Set<Profesion> getProfesionesSeleccionadas() {
		if (this.profesionesSeleccionadas == null) {
			this.profesionesSeleccionadas = new HashSet<>();
		}
		return profesionesSeleccionadas;
	}

	public void setProfesionesSeleccionadas(
			Set<Profesion> profesionesSeleccionadas) {
		this.profesionesSeleccionadas = profesionesSeleccionadas;
	}

	public List<Profesion> getVoluntarioProfesiones() {
		if (this.voluntarioProfesiones == null) {
			voluntarioProfesiones = new ArrayList<>();
		}
		return voluntarioProfesiones;
	}

	public void setVoluntarioProfesiones(List<Profesion> voluntarioProfesiones) {
		this.voluntarioProfesiones = voluntarioProfesiones;
	}

	public Set<Profesion> getVoluntarioProfesionesSeleccionadas() {
		if (this.voluntarioProfesionesSeleccionadas == null) {
			this.voluntarioProfesionesSeleccionadas = new HashSet<>();
		}
		return voluntarioProfesionesSeleccionadas;
	}

	public void setVoluntarioProfesionesSeleccionadas(
			Set<Profesion> voluntarioProfesionesSeleccionadas) {
		this.voluntarioProfesionesSeleccionadas = voluntarioProfesionesSeleccionadas;
	}

	@Command("agregarProfesiones")
	@NotifyChange({ "profesiones", "voluntarioProfesiones",
			"profesionesSeleccionadas", "voluntarioProfesionesSeleccionadas" })
	public void agregarProfesiones() {
		if (this.getProfesionesSeleccionadas() != null
				&& this.getProfesionesSeleccionadas().size() > 0) {
			this.getVoluntarioProfesiones().addAll(profesionesSeleccionadas);
			this.getProfesionesSeleccionadas().clear();
			this.getVoluntarioProfesionesSeleccionadas().clear();
		}
	}

	@Command("removerProfesiones")
	@NotifyChange({ "profesiones", "voluntarioProfesiones",
			"profesionesSeleccionadas", "voluntarioProfesionesSeleccionadas" })
	public void removerProfesiones() {
		if (this.getVoluntarioProfesionesSeleccionadas() != null
				&& this.getVoluntarioProfesionesSeleccionadas().size() > 0) {
			this.getVoluntarioProfesiones().removeAll(
					voluntarioProfesionesSeleccionadas);
			this.getProfesionesSeleccionadas().clear();
			this.getVoluntarioProfesionesSeleccionadas().clear();
		}
	}

	// TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getVoluntario().getFkPersona()
				.setTipoPersona(tipoPersonaEnum.ordinal());
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

	// CIUDADES
	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	// ESTADOS
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

	// FECHA NACIMIENTO
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getVoluntario().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	// FECHA INGRESO
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getVoluntario().setFechaIngreso(fechaIngreso.getTime());
	}

	// SEXO
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
		this.getVoluntario().getFkPersona().setSexo(sexoEnum.ordinal());
	}

	// FILTRADO DE CIUDADES POR ESTADO
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getVoluntario().getFkPersona().setFkCiudad(null);
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

	// M�TODOS DEL FORMULARIO
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

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			// MULTIMEDIA
			if (bytes != null) {
				if (this.getVoluntario().getFkPersona().getFkMultimedia() == null
						|| this.getVoluntario().getFkPersona()
								.getFkMultimedia().getIdMultimedia() == null) {
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
					Zki.save(Zki.PERSONAS, this.getVoluntario().getFkPersona()
							.getIdPersona(), extensionImage, bytes);
					this.getVoluntario().getFkPersona()
							.setFkMultimedia(multimedia);
				} else {
					Multimedia multimedia = this.getVoluntario().getFkPersona()
							.getFkMultimedia();
					multimedia.setNombre(nameImage);
					multimedia.setDescripcion(typeMedia);
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);
					if (!UtilPayload.isOK(payloadMultimediaResponse)) {
						Alert.showMessage(payloadMultimediaResponse);
						return true;
					}
					Zki.save(Zki.PERSONAS, this.getVoluntario().getFkPersona()
							.getIdPersona(), extensionImage, bytes);
				}
			}

			Multimedia multimedia = this.getVoluntario().getFkPersona()
					.getFkMultimedia();

			if (bytes == null
					&& this.getVoluntario().getFkPersona().getFkMultimedia() != null) {
				Zki.remove(this.getVoluntario().getFkPersona()
						.getFkMultimedia().getUrl());
				this.getVoluntario().getFkPersona().setFkMultimedia(null);
			}

			if (bytes == null && multimedia != null
					&& multimedia.getIdMultimedia() != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

			// PERSONA
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(this.getVoluntario().getFkPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				return true;
			}

			// PROFESIONES
			// this.getVoluntario().getProfesiones().addAll(new
			// ArrayList<Profesion>());
			// this.getVoluntario().getProfesiones().addAll(this.getVoluntarioProfesiones());

			// VOLUNTARIO
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.modificar(this.getVoluntario());
			if (!UtilPayload.isOK(payloadVoluntarioResponse)) {
				Alert.showMessage(payloadVoluntarioResponse);
				return true;
			}

			Alert.showMessage(payloadVoluntarioResponse);
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

	public Voluntario getVoluntario() {
		return (Voluntario) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateNull(this.getVoluntario().getFkPersona()
					.getTipoPersona(), "Tipo de persona");
			UtilValidate.validateInteger(this.getVoluntario().getFkPersona()
					.getTipoPersona(), "Tipo de persona",
					ValidateOperator.LESS_THAN, 2);
			if (this.getTipoPersonaEnum().equals(TipoPersonaEnum.NATURAL)) {
				UtilValidate.validateString(this.getVoluntario().getFkPersona()
						.getIdentificacion(), "C�dula", 35);
				UtilValidate.validateString(this.getVoluntario().getFkPersona()
						.getNombre(), "Nombre", 150);
				UtilValidate.validateString(this.getVoluntario().getFkPersona()
						.getApellido(), "Apellido", 150);
				UtilValidate.validateInteger(this.getVoluntario()
						.getFkPersona().getSexo(), "Sexo",
						ValidateOperator.LESS_THAN, 2);
				UtilValidate.validateDate(this.getVoluntario().getFkPersona()
						.getFechaNacimiento(), "Fecha de nacimiento",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
			} else {
				UtilValidate.validateString(this.getVoluntario().getFkPersona()
						.getIdentificacion(), "RIF", 35);
				UtilValidate.validateString(this.getVoluntario().getFkPersona()
						.getNombre(), "Nombre", 150);
			}
			UtilValidate.validateNull(this.getVoluntario().getFkPersona()
					.getFkCiudad(), "Ciudad");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			UtilValidate.validateDate(this.getVoluntario().getFechaIngreso(),
					"Fecha de ingreso", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(calendar
							.getTime()), "dd/MM/yyyy");
			UtilValidate.validateString(this.getVoluntario().getFkPersona()
					.getDireccion(), "Direcci�n", 250);
			UtilValidate.validateString(this.getVoluntario().getFkPersona()
					.getTelefono1(), "Tel�fono 1", 25);
			UtilValidate.validateString(this.getVoluntario().getFkPersona()
					.getCorreo(), "Correo", 100);
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
		return true;
	}

	// PROPIEDADES DEL MULTIMEDIA
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
				this.nameImage = new StringBuilder()
						.append(Zki.PERSONAS)
						.append(this.getVoluntario().getFkPersona()
								.getIdPersona()).append(".")
						.append(extensionImage).toString();
				this.bytes = media.getByteData();

				this.urlImage = new StringBuilder()
						.append(Zki.PERSONAS)
						.append(this.getVoluntario().getFkPersona()
								.getIdPersona()).append(".")
						.append(extensionImage).toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getVoluntario().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inv�lido");
			}
		} else {
			this.getVoluntario().getFkPersona().setFkMultimedia(null);
			Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inv�lido");
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
