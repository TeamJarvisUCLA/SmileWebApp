package ve.smile.gestion.apadrinamiento.egreso.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Motivo;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_EgresoPadrinoIndex extends VM_WindowWizard {
	private List<EstatusPadrinoEnum> estatusPadrinoEnums;
	private EstatusPadrinoEnum estatusPadrinoEnum;

	private Estado estado;
	private Motivo motivo;

	private Date fechaNacimiento = new Date();
	private Date fechaEgreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<Motivo> motivos;

	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<FrecuenciaAporte> frecuenciaAporte;

	@Init(superclass = true)
	public void childInit() {
		estado = new Estado();
		motivo = new Motivo();
		fechaNacimiento = new Date();
	}

	// ENUM SEXO
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
		if (this.ciudades.isEmpty()) {
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
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
		this.getPadrinoSelected().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
		this.getPadrinoSelected().setFechaSalida(fechaEgreso.getTime());
	}

	// ESTATUS PADRINO
	public EstatusPadrinoEnum getEstatusPadrinoEnum() {
		return estatusPadrinoEnum;
	}

	public void setEstatusPadrinoEnum(EstatusPadrinoEnum estatusPadrinoEnum) {
		this.estatusPadrinoEnum = estatusPadrinoEnum;
		this.getPadrinoSelected().setEstatusPadrinoEnum(estatusPadrinoEnum);
	}

	public List<EstatusPadrinoEnum> getEstatusPadrinoEnums() {
		if (this.estatusPadrinoEnums == null) {
			this.estatusPadrinoEnums = new ArrayList<>();
		}
		if (this.estatusPadrinoEnums.isEmpty()) {
			for (EstatusPadrinoEnum estatusPadrinoEnum : EstatusPadrinoEnum
					.values()) {
				this.estatusPadrinoEnums.add(estatusPadrinoEnum);
			}
		}
		return estatusPadrinoEnums;
	}

	public void setEstatusPadrinoEnums(
			List<EstatusPadrinoEnum> estatusPadrinoEnums) {
		this.estatusPadrinoEnums = estatusPadrinoEnums;
	}

	// MOTIVOS
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public List<Motivo> getMotivos() {
		if (this.motivos == null) {
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty()) {
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
			}
			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}
		return motivos;
	}

	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}

	// M�TODOS DEL WIZARD

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "EGRESAR", "Custom1",
				"z-icon-times", "deep-orange", "EGRESAR");
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(operacionWizardCustom1);
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

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/apadrinamiento/egreso/selectPadrino.zul");
		urls.add("views/desktop/gestion/apadrinamiento/egreso/datosPersonales.zul");
		urls.add("views/desktop/gestion/apadrinamiento/egreso/registroMotivo.zul");
		urls.add("views/desktop/gestion/apadrinamiento/egreso/registroCompletado.zul");
		return urls;
	}

	// CARGAR OBJETOS
	
	
	@Override
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.ACTIVO.ordinal();
		criterios.put("estatusPadrino",
				String.valueOf(EstatusPadrinoEnum.ACTIVO.ordinal()));
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPadrinoResponse;
	}

	// ATR�S

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	// SIGUIENTE

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>padrino</b>";
			}
		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 2) {
			// NOTHING
		}

		goToNextStep();
		return "";
	}

	// CUSTOMS

	@Override
	public String executeCustom1(Integer currentStep) {
		// RECHAZADO
		this.getPadrinoSelected().setEstatusPadrino(
				EstatusPadrinoEnum.INACTIVO.ordinal());
		goToNextStep();
		return "";
	}

	// FINALIZAR

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				Calendar calendar = Calendar.getInstance();

				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				UtilValidate.validateDate(this.getPadrinoSelected()
						.getFechaSalida(), "Fecha de Egreso",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(calendar.getTime()),
						"dd/MM/yyyy");
				UtilValidate.validateNull(this.getPadrinoSelected()
						.getFkMotivo(), "Motivo de Rechazo");
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.modificar(this.getPadrinoSelected());
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				restartWizard();
				this.setSelectedObject(new Padrino());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
			}
			return (String) payloadPadrinoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	public Padrino getPadrinoSelected() {
		return (Padrino) this.selectedObject;
	}

}