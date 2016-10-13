package ve.smile.gestion.apadrinamiento.registro.viewmodels;

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
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
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
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_RegistrarPadrinoIndex extends VM_WindowWizard implements
		UploadImageSingle {
	private Estado estado;

	private Date fechaNacimiento = new Date();
	private Date fechaIngreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<FrecuenciaAporte> frecuenciaAporte;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		estado = new Estado();
		fechaNacimiento = new Date();
		fechaIngreso = new Date();
	}

	// ENUN SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPadrinoSelected().getFkPersona()
				.setSexo(this.sexoEnum.ordinal());
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

	// ENUM TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPadrinoSelected().getFkPersona()
				.setTipoPersona(this.tipoPersonaEnum.ordinal());
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
		this.getPadrinoSelected().getFkPersona().setFkCiudad(null);
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

	// FECHAS
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		if (fechaNacimiento != null) {
			this.getPadrinoSelected().getFkPersona()
					.setFechaNacimiento(fechaNacimiento.getTime());
		} else {
			this.getPadrinoSelected().getFkPersona().setFechaNacimiento(null);
		}

	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getPadrinoSelected().setFechaIngreso(fechaIngreso.getTime());
	}

	// Wizard

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(3, listOperacionWizard3);
		return botones;

	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/apadrinamiento/registro/selectPadrino.zul");
		urls.add("views/desktop/gestion/apadrinamiento/registro/datosPersonales.zul");
		urls.add("views/desktop/gestion/apadrinamiento/registro/datosContacto.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this
					.getPadrinoSelected().getFkPersona().getTipoPersona()]);

			this.setSexoEnum(SexoEnum.values()[this.getPadrinoSelected()
					.getFkPersona().getSexo()]);

			this.setEstado(this.getPadrinoSelected().getFkPersona()
					.getFkCiudad().getFkEstado());
			if (this.getPadrinoSelected().getFechaIngreso() != null) {
				this.setFechaIngreso(new Date(this.getPadrinoSelected()
						.getFechaIngreso()));
			} else {
				this.setFechaIngreso(new Date());
			}

			this.setFechaNacimiento(new Date(this.getPadrinoSelected()
					.getFkPersona().getFechaNacimiento()));

			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(estado.getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
			BindUtils.postNotifyChange(null, null, this, "estado");
			BindUtils.postNotifyChange(null, null, this, "ciudades");
			BindUtils.postNotifyChange(null, null, this, "sexoEnum");
			BindUtils.postNotifyChange(null, null, this, "ciudad");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
		}
		goToNextStep();
		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return "";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusPadrino",
				String.valueOf(EstatusPadrinoEnum.POR_COMPLETAR.ordinal()));
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPadrinoResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			}
		}
		if (currentStep == 2) {
			try {
				UtilValidate.validateInteger(this.getPadrinoSelected()
						.getFkPersona().getTipoPersona(), "Tipo de persona",
						ValidateOperator.LESS_THAN, 2);

				if (this.getTipoPersonaEnum().equals(TipoPersonaEnum.NATURAL)) {
					UtilValidate.validateString(this.getPadrinoSelected()
							.getFkPersona().getIdentificacion(), "Cédula", 35);
					UtilValidate.validateString(this.getPadrinoSelected()
							.getFkPersona().getNombre(), "Nombre", 150);
					UtilValidate.validateString(this.getPadrinoSelected()
							.getFkPersona().getApellido(), "Apellido", 150);
					UtilValidate.validateInteger(this.getPadrinoSelected()
							.getFkPersona().getSexo(), "Sexo",
							ValidateOperator.LESS_THAN, 2);
					UtilValidate.validateDate(this.getPadrinoSelected()
							.getFkPersona().getFechaNacimiento(),
							"Fecha de nacimiento", ValidateOperator.LESS_THAN,
							new SimpleDateFormat("yyyy-MM-dd")
									.format(new Date()), "dd/MM/yyyy");
				} else {
					UtilValidate.validateString(this.getPadrinoSelected()
							.getFkPersona().getIdentificacion(), "RIF", 35);
					UtilValidate.validateString(this.getPadrinoSelected()
							.getFkPersona().getNombre(), "Nombre", 150);
				}

				UtilValidate.validateNull(this.getPadrinoSelected()
						.getFkPersona().getFkCiudad(), "Ciudad");

				Calendar calendar = Calendar.getInstance();

				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				UtilValidate.validateDate(this.getPadrinoSelected()
						.getFechaIngreso(), "Fecha de ingreso",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(calendar.getTime()),
						"dd/MM/yyyy");
				UtilValidate.validateString(this.getPadrinoSelected()
						.getFkPersona().getDireccion(), "Dirección", 250);

			} catch (Exception e) {
				return e.getMessage();

			}
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				UtilValidate.validateString(this.getPadrinoSelected()
						.getFkPersona().getTelefono1(), "Teléfono 1", 25);

				UtilValidate.validateString(this.getPadrinoSelected()
						.getFkPersona().getFax(), "Fax", 100);
				UtilValidate.validateString(this.getPadrinoSelected()
						.getFkPersona().getCorreo(), "Correo", 100);
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
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
				Zki.save(Zki.PADRINOS,
						this.getPadrinoSelected().getIdPadrino(),
						extensionImage, bytes);
				this.getPadrinoSelected().getFkPersona()
						.setFkMultimedia(multimedia);

			}
			// Padrino
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(this.getPadrinoSelected().getFkPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				return (String) payloadPersonaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			this.getPadrinoSelected().setEstatusPadrino(
					EstatusPadrinoEnum.ACTIVO.ordinal());
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.modificar(this.getPadrinoSelected());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				return (String) payloadPadrinoResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			restartWizard();
			this.setSelectedObject(new Padrino());
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
			BindUtils.postNotifyChange(null, null, this, "Padrino");
			return (String) payloadPadrinoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	public Padrino getPadrinoSelected() {
		return (Padrino) this.selectedObject;
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
				this.nameImage = new StringBuilder().append(Zki.PADRINOS)
						.append(this.getPadrinoSelected().getIdPadrino())
						.append(".").append(this.extensionImage).toString();
				this.bytes = media.getByteData();

				this.urlImage = new StringBuilder().append(Zki.PADRINOS)
						.append(this.getPadrinoSelected().getIdPadrino())
						.append(".").append(extensionImage).toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getPadrinoSelected().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getPadrinoSelected().getFkPersona().setFkMultimedia(null);
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