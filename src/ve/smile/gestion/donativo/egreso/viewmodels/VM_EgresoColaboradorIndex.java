package ve.smile.gestion.donativo.egreso.viewmodels;

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
import ve.smile.dto.Colaborador;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusColaboradorEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_EgresoColaboradorIndex extends VM_WindowWizard {
	private List<EstatusPadrinoEnum> estatusPadrinoEnums;
	private EstatusPadrinoEnum estatusPadrinoEnum;

	private Estado estado;

	private Date fechaNacimiento = new Date();
	private Date fechaEgreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<Motivo> motivos;

	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	@Init(superclass = true)
	public void childInit() {
		estado = new Estado();
		fechaNacimiento = new Date();
	}

	// ENUM SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getColaboradorSelected().getFkPersona()
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
		this.getColaboradorSelected().getFkPersona()
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

	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getColaboradorSelected().getFkPersona().setFkCiudad(null);
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
		this.getColaboradorSelected().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
		this.getColaboradorSelected().setFechaEgreso(fechaEgreso.getTime());
	}

	// ESTATUS PADRINO
	public EstatusPadrinoEnum getEstatusPadrinoEnum() {
		return estatusPadrinoEnum;
	}

	public void setEstatusPadrinoEnum(EstatusPadrinoEnum estatusPadrinoEnum) {
		this.estatusPadrinoEnum = estatusPadrinoEnum;
		this.getColaboradorSelected().setEstatusColaborador(
				estatusPadrinoEnum.ordinal());
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
		OperacionWizard operacionWizardCustom6 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "ACEPTAR", "Custom1",
				"fa fa-check", "indigo", "ACEPTAR");
		listOperacionWizard4.add(operacionWizardCustom6);
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
		urls.add("views/desktop/gestion/donativo/egreso/selectPadrino.zul");
		urls.add("views/desktop/gestion/donativo/egreso/datosPersonales.zul");
		urls.add("views/desktop/gestion/donativo/egreso/registroMotivo.zul");
		urls.add("views/desktop/gestion/donativo/egreso/registroCompletado.zul");
		return urls;
	}

	// CARGAR OBJETOS

	@Override
	public IPayloadResponse<Colaborador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusColaborador",
				String.valueOf(EstatusColaboradorEnum.ACTIVO.ordinal()));
		PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadColaboradorResponse;
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
		if (currentStep == 2) {
			this.getColaboradorSelected().setEstatusColaborador(
					EstatusPadrinoEnum.INACTIVO.ordinal());
			goToNextStep();
		}
		if (currentStep == 4) {
			restartWizard();
		}

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
				UtilValidate.validateDate(this.getColaboradorSelected()
						.getFechaEgreso(), "Fecha de Egreso",
						ValidateOperator.LESS_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(calendar.getTime()),
						"dd/MM/yyyy");
				UtilValidate.validateNull(this.getColaboradorSelected()
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
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.modificar(this.getColaboradorSelected());
			if (UtilPayload.isOK(payloadColaboradorResponse)) {
				goToNextStep();
				this.setSelectedObject(new Padrino());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
			}
			return (String) payloadColaboradorResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	public Colaborador getColaboradorSelected() {
		return (Colaborador) this.selectedObject;
	}

}