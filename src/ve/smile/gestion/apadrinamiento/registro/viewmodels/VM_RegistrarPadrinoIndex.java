package ve.smile.gestion.apadrinamiento.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_RegistrarPadrinoIndex extends VM_WindowWizard<Padrino>
		implements UploadImageSingle {
	private Estado estado;
	private Padrino padrino = new Padrino();

	private Date fechaNacimiento = new Date();
	private Date fechaIngreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private byte[] bytes;
	private String nameImage;
	private String urlImagen;

	@Init(superclass = true)
	public void childInit() {
		padrino = new Padrino();
		estado = new Estado();
		fechaNacimiento = new Date();
		fechaIngreso = new Date();
	}

	public Padrino getPadrino() {
		return padrino;
	}

	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
	}

	// ENUN SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		selectedObject.getFkPersona().setSexo(this.sexoEnum.ordinal());
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
		selectedObject.getFkPersona().setTipoPersona(
				this.tipoPersonaEnum.ordinal());
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

	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getSelectedObject().getFkPersona().setFkCiudad(null);
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
		this.getSelectedObject().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getSelectedObject().setFechaIngreso(fechaIngreso.getTime());
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

	/*
	 * @Override public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
	 * Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer,
	 * List<OperacionWizard>>();
	 * 
	 * OperacionWizard operacionWizardCustom1 = new OperacionWizard(
	 * OperacionWizardEnum.CUSTOM1.ordinal(), "APROBAR", "Custom1",
	 * "fa fa-check-square-o", "green", "APROBAR"); OperacionWizard
	 * operacionWizardCustom2 = new OperacionWizard(
	 * OperacionWizardEnum.CUSTOM2.ordinal(), "RECHAZAR", "Custom2",
	 * "z-icon-times", "deep-orange", "RECHAZAR"); OperacionWizard
	 * operacionWizardCustom3 = new OperacionWizard(
	 * OperacionWizardEnum.CUSTOM3.ordinal(), "CANCELAR", "Custom2",
	 * "z-icon-times", "red", "CANCELAR");
	 * 
	 * List<OperacionWizard> listOperacionWizard1 = new
	 * ArrayList<OperacionWizard>();
	 * listOperacionWizard1.add(OperacionWizardHelper
	 * .getPorType(OperacionWizardEnum.SIGUIENTE));
	 * 
	 * botones.put(1, listOperacionWizard1);
	 * 
	 * List<OperacionWizard> listOperacionWizard2 = new
	 * ArrayList<OperacionWizard>();
	 * listOperacionWizard2.add(OperacionWizardHelper
	 * .getPorType(OperacionWizardEnum.ATRAS)); //
	 * listOperacionWizard2.add(operacionWizardCustom1); //
	 * listOperacionWizard2.add(operacionWizardCustom2);
	 * listOperacionWizard2.add(OperacionWizardHelper
	 * .getPorType(OperacionWizardEnum.SIGUIENTE));
	 * listOperacionWizard2.add(operacionWizardCustom3); botones.put(2,
	 * listOperacionWizard2);
	 * 
	 * List<OperacionWizard> listOperacionWizard3 = new
	 * ArrayList<OperacionWizard>();
	 * listOperacionWizard3.add(OperacionWizardHelper
	 * .getPorType(OperacionWizardEnum.ATRAS));
	 * listOperacionWizard3.add(OperacionWizardHelper
	 * .getPorType(OperacionWizardEnum.FINALIZAR));
	 * listOperacionWizard3.add(operacionWizardCustom3); botones.put(3,
	 * listOperacionWizard3);
	 * 
	 * return botones; }
	 */

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
		urls.add("views/desktop/gestion/apadrinamiento/registro/DatosPersonales.zul");
		urls.add("views/desktop/gestion/apadrinamiento/registro/DatosContacto.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.setSexoEnum(SexoEnum.values()[selectedObject.getFkPersona()
					.getSexo()]);
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[selectedObject
					.getFkPersona().getTipoPersona()]);
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
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);
		return payloadPadrinoResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			}
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				/*
				 * UtilValidate.validateNull(this.getPadrino().getFkPersona().
				 * getTelefono1(), "Tel�fono 1");
				 * UtilValidate.validateNull(this.
				 * getPadrino().getFkPersona().getCorreo(), "Correo");
				 */
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			/*
			 * this.getPadrino().getFkPersona().setTelefono1(this.getPadrino().
			 * getFkPersona().getTelefono1());
			 * this.getPadrino().getFkPersona().setCorreo
			 * (this.getPadrino().getFkPersona().getCorreo());
			 * 
			 * // IMAGEN Multimedia multimedia =
			 * this.getPadrino().getFkPersona().getFkMultimedia();
			 * PayloadMultimediaResponse payloadMultimediaResponse =
			 * S.MultimediaService.incluir(multimedia); if
			 * (!UtilPayload.isOK(payloadMultimediaResponse)) { return (String)
			 * payloadMultimediaResponse
			 * .getInformacion(IPayloadResponse.MENSAJE); }
			 * multimedia.setIdMultimedia(((Double)
			 * payloadMultimediaResponse.getInformacion("id")).intValue());
			 * this.getPadrino().getFkPersona().setFkMultimedia(multimedia);
			 */

			// Padrino
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(this.selectedObject.getFkPersona());
			if (UtilPayload.isOK(payloadPersonaResponse)) {
				// OK
			}
			selectedObject.setEstatusPadrino(EstatusPadrinoEnum.ACTIVO
					.ordinal());
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				restartWizard();
				this.setSelectedObject(new Padrino());
				this.setPadrino(new Padrino());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "Padrino");
			}
			return (String) payloadPadrinoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
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
			bytes = media.getByteData();
			this.nameImage = media.getName();
			if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage
					.lastIndexOf(".") + 1))) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(new StringBuilder()
						.append("/Smile/Voluntario/").append(nameImage)
						.toString());
				multimedia.setExtension(UtilMultimedia
						.stringToExtensionEnum(
								nameImage.substring(this.nameImage
										.lastIndexOf(".") + 1)).ordinal());
				multimedia.setDescripcion("Imagen del voluntario");
				this.getPadrino().getFkPersona().setFkMultimedia(multimedia);
			} else {
				this.getPadrino().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inv�lido");
			}
		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
		this.getPadrino().getFkPersona().setFkMultimedia(null);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	private BufferedImage loadImage() throws Exception {
		try {
			Path path = Paths.get(this.getUrlImagen());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImagen()));
		} catch (Exception e) {
			return null;
		}
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

}