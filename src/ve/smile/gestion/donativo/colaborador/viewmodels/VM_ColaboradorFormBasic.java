package ve.smile.gestion.donativo.colaborador.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Colaborador;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusColaboradorEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_ColaboradorFormBasic extends VM_WindowForm implements
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

	private Persona persona;

	@Init(superclass = true)
	public void childInit() {

		this.setPersona(this.getColaborador().getFkPersona());
		if (this.getPersona().getSexo() != null) {
			this.setSexoEnum(SexoEnum.values()[this.getPersona().getSexo()]);
		}

		if (this.getPersona().getTipoPersona() != null) {
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this.getPersona()
					.getTipoPersona()]);
		}
		if (this.getColaborador().getFkPersona() != null
				&& this.getColaborador().getFkPersona() != null) {
			this.setUrlImage(this.getPersona().getFkMultimedia().getUrl());
			this.nameImage = this.getPersona().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getPersona().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getPersona().setFkMultimedia(new Multimedia());

		}
		if (this.getColaborador().getFechaIngreso() != null) {
			this.setFechaIngreso(new Date(this.getColaborador()
					.getFechaIngreso()));
		} else {
			this.setFechaIngreso(new Date());
		}

		if (this.getPersona().getFkCiudad() != null) {
			this.setEstado(this.getPersona().getFkCiudad().getFkEstado());
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
		this.getColaborador().setFechaIngreso(fechaIngreso.getTime());
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
		this.getColaborador().setFkPersona(persona);
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			Colaborador colaborador = this.getColaborador();

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
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
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
			colaborador.setFkPersona(this.getPersona());
			colaborador.setEstatusColaborador(EstatusColaboradorEnum.ACTIVO
					.ordinal());
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.incluir(getColaborador());
			if (!UtilPayload.isOK(payloadColaboradorResponse)) {
				Alert.showMessage(payloadColaboradorResponse);
				return true;
			}

			if (bytes != null) {
				Zki.save(Zki.PERSONAS, getPersona().getIdPersona(),
						extensionImage, bytes);
				Multimedia multimedia = this.getPersona().getFkMultimedia();
				multimedia.setNombre(Zki.PERSONAS
						+ this.getPersona().getIdPersona() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.PERSONAS + getPersona().getIdPersona()
						+ "." + this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

			Alert.showMessage(payloadColaboradorResponse);
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (bytes != null) {
				if (this.getPersona().getFkMultimedia() == null
						|| this.getPersona().getFkMultimedia()
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
					Zki.save(Zki.PERSONAS, this.getPersona().getIdPersona(),
							extensionImage, bytes);
					this.getPersona().setFkMultimedia(multimedia);
				} else {
					Multimedia multimedia = this.getPersona().getFkMultimedia();
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
					Zki.save(Zki.PERSONAS, this.getPersona().getIdPersona(),
							extensionImage, bytes);
				}

			}

			Multimedia multimedia = this.getPersona().getFkMultimedia();

			if (bytes == null && this.getPersona().getFkMultimedia() != null) {
				Zki.remove(this.getPersona().getFkMultimedia().getUrl());
				this.getPersona().setFkMultimedia(null);
			}

			// Padrino
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(this.getPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				return true;
			}
			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}
			this.getColaborador().setFkPersona(this.getPersona());
			this.getColaborador().setEstatusColaborador(
					EstatusColaboradorEnum.ACTIVO.ordinal());
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.modificar(this.getColaborador());
			Alert.showMessage(payloadColaboradorResponse);
			if (!UtilPayload.isOK(payloadColaboradorResponse)) {
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

	public Colaborador getColaborador() {
		return (Colaborador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateNull(this.getPersona().getTipoPersona(),
					"Tipo de persona");
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
								"yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
			} else {
				UtilValidate.validateString(this.getPersona()
						.getIdentificacion(), "RIF", 35);
				UtilValidate.validateString(this.getPersona().getNombre(),
						"Nombre", 150);
			}

			UtilValidate
					.validateNull(this.getPersona().getFkCiudad(), "Ciudad");

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			UtilValidate.validateDate(this.getColaborador().getFechaIngreso(),
					"Fecha de ingreso", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(calendar
							.getTime()), "dd/MM/yyyy");
			UtilValidate.validateString(this.getPersona().getDireccion(),
					"Dirección", 250);
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

	// Propiedades de la Imagen
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
				this.bytes = media.getByteData();
				this.extensionImage = media.getName().substring(
						media.getName().lastIndexOf(".") + 1);
				this.typeMedia = media.getContentType();

				if (this.getPersona().getIdPersona() != null) {
					this.nameImage = new StringBuilder().append(Zki.PERSONAS)
							.append(this.getPersona().getIdPersona())
							.append(".").append(extensionImage).toString();

					this.urlImage = new StringBuilder().append(Zki.PERSONAS)
							.append(this.getPersona().getIdPersona())
							.append(".").append(extensionImage).toString();

				}
			} else {
				this.getPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getPersona().setFkMultimedia(null);
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
