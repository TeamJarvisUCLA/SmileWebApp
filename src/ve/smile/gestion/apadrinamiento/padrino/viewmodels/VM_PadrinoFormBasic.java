package ve.smile.gestion.apadrinamiento.padrino.viewmodels;

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
import ve.smile.dto.Estado;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.SexoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_PadrinoFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private Date fechaNacimiento = new Date();
	private Date fechaIngreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private Estado estado;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private List<FrecuenciaAporte> frecuenciaAporte;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init
	public void childInit() {
		if (this.getPadrino().getFkPersona().getSexo() != null) {
			this.setSexoEnum(SexoEnum.values()[this.getPadrino().getFkPersona()
					.getSexo()]);
		}

		if (this.getPadrino().getFkPersona().getTipoPersona() != null) {
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this.getPadrino()
					.getFkPersona().getTipoPersona()]);
		}
		if (this.getPadrino().getFkPersona() != null
				&& this.getPadrino().getFkPersona().getFkMultimedia() != null) {
			this.setUrlImage(this.getPadrino().getFkPersona().getFkMultimedia()
					.getUrl());
			this.nameImage = this.getPadrino().getFkPersona().getFkMultimedia()
					.getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getPadrino().getFkPersona().getFkMultimedia()
					.getDescripcion();
		} else {
			this.getPadrino().getFkPersona().setFkMultimedia(new Multimedia());

		}
		if (this.getPadrino().getFechaIngreso() != null) {
			this.setFechaIngreso(new Date(this.getPadrino().getFechaIngreso()));
		} else {
			this.setFechaIngreso(new Date());
		}

		if (this.getPadrino().getFkPersona().getFkCiudad() != null) {
			this.setEstado(this.getPadrino().getFkPersona().getFkCiudad()
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

	}

	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPadrino().getFkPersona()
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getPadrino().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getPadrino().setFechaIngreso(fechaIngreso.getTime());
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
		this.getPadrino().getFkPersona().setSexo(sexoEnum.ordinal());
	}

	public List<FrecuenciaAporte> getFrecuenciaAporte() {
		if (this.frecuenciaAporte == null) {
			this.frecuenciaAporte = new ArrayList<>();
		}
		if (this.frecuenciaAporte.isEmpty()) {
			PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
					.consultarTodos();

			this.frecuenciaAporte.addAll(payloadFrecuenciaAporteResponse
					.getObjetos());
		}

		return frecuenciaAporte;
	}

	public void setFrecuenciaAporte(List<FrecuenciaAporte> frecuenciaAporte) {
		this.frecuenciaAporte = frecuenciaAporte;
	}

	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getPadrino().getFkPersona().setFkCiudad(null);
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
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (bytes != null) {
				if (this.getPadrino().getFkPersona().getFkMultimedia() == null
						|| this.getPadrino().getFkPersona().getFkMultimedia()
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
					Zki.save(Zki.PERSONAS, this.getPadrino().getFkPersona()
							.getIdPersona(), extensionImage, bytes);
					this.getPadrino().getFkPersona()
							.setFkMultimedia(multimedia);
				} else {
					Multimedia multimedia = this.getPadrino().getFkPersona()
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
					Zki.save(Zki.PERSONAS, this.getPadrino().getFkPersona()
							.getIdPersona(), extensionImage, bytes);
				}

			}

			Multimedia multimedia = this.getPadrino().getFkPersona()
					.getFkMultimedia();

			if (bytes == null
					&& this.getPadrino().getFkPersona().getFkMultimedia() != null) {
				Zki.remove(this.getPadrino().getFkPersona().getFkMultimedia()
						.getUrl());
				this.getPadrino().getFkPersona().setFkMultimedia(null);
			}

			// Padrino
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(this.getPadrino().getFkPersona());
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
			this.getPadrino().setEstatusPadrino(
					EstatusPadrinoEnum.ACTIVO.ordinal());
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.modificar(this.getPadrino());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				Alert.showMessage(payloadPadrinoResponse);
				return true;
			}

			Alert.showMessage(payloadPadrinoResponse);
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

	public Padrino getPadrino() {
		return (Padrino) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateNull(this.getPadrino().getFkPersona()
					.getTipoPersona(), "Tipo de persona");
			UtilValidate.validateInteger(this.getPadrino().getFkPersona()
					.getTipoPersona(), "Tipo de persona",
					ValidateOperator.LESS_THAN, 2);

			if (this.getTipoPersonaEnum().equals(TipoPersonaEnum.NATURAL)) {
				UtilValidate.validateString(this.getPadrino().getFkPersona()
						.getIdentificacion(), "Cédula", 35);
				UtilValidate.validateString(this.getPadrino().getFkPersona()
						.getNombre(), "Nombre", 150);
				UtilValidate.validateString(this.getPadrino().getFkPersona()
						.getApellido(), "Apellido", 150);
				UtilValidate.validateInteger(this.getPadrino().getFkPersona()
						.getSexo(), "Sexo", ValidateOperator.LESS_THAN, 2);
				UtilValidate.validateDate(this.getPadrino().getFkPersona()
						.getFechaNacimiento(), "Fecha de nacimiento",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
			} else {
				UtilValidate.validateString(this.getPadrino().getFkPersona()
						.getIdentificacion(), "RIF", 35);
				UtilValidate.validateString(this.getPadrino().getFkPersona()
						.getNombre(), "Nombre", 150);
			}

			UtilValidate.validateNull(this.getPadrino().getFkPersona()
					.getFkCiudad(), "Ciudad");

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			UtilValidate.validateDate(this.getPadrino().getFechaIngreso(),
					"Fecha de ingreso", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(calendar
							.getTime()), "dd/MM/yyyy");
			UtilValidate.validateString(this.getPadrino().getFkPersona()
					.getDireccion(), "Dirección", 250);
			UtilValidate.validateString(this.getPadrino().getFkPersona()
					.getTelefono1(), "Teléfono 1", 25);
			UtilValidate.validateString(this.getPadrino().getFkPersona()
					.getCorreo(), "Correo", 100);

		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
		return true;
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

				this.extensionImage = media.getName().substring(
						media.getName().lastIndexOf(".") + 1);
				this.nameImage = new StringBuilder()
						.append(Zki.PERSONAS)
						.append(this.getPadrino().getFkPersona().getIdPersona())
						.append(".").append(extensionImage).toString();
				this.bytes = media.getByteData();

				this.urlImage = new StringBuilder()
						.append(Zki.PERSONAS)
						.append(this.getPadrino().getFkPersona().getIdPersona())
						.append(".").append(extensionImage).toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getPadrino().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getPadrino().getFkPersona().setFkMultimedia(null);
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
