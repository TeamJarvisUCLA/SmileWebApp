package ve.smile.gestion.ayudas.estudio_socio_economico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EstudioSocioEconomico;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusEstudioSocioEconomicoEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.enums.UrgenciaEnum;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;

public class VM_EstudioSocioEconomicoIndex extends VM_WindowWizard {

	private EstudioSocioEconomico estudioSocioEconomico;

	private SolicitudAyuda solicitudAyuda;

	private Date fecha = new Date();

	private List<Trabajador> trabajadores;

	private Trabajador trabajador;

	private List<EstatusSolicitudEnum> estatusSolicitudEnums;
	private EstatusSolicitudEnum estatusSolicitudEnum;

	private List<UrgenciaEnum> urgenciaEnums;
	private UrgenciaEnum urgenciaEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		setSolicitudAyuda(new SolicitudAyuda());
		estudioSocioEconomico = new EstudioSocioEconomico();
		fecha = new Date();
	}

	public EstatusSolicitudEnum getEstatusSolicitudEnum() {
		return estatusSolicitudEnum;
	}

	public void setEstatusSolicitudEnum(
			EstatusSolicitudEnum estatusSolicitudEnum) {
		this.estatusSolicitudEnum = estatusSolicitudEnum;
		this.getSolicitudAyuda().setEstatusSolicitud(
				estatusSolicitudEnum.ordinal());
	}

	public List<EstatusSolicitudEnum> getEstatusSolicitudEnums() {
		if (this.estatusSolicitudEnums == null) {
			this.estatusSolicitudEnums = new ArrayList<>();
		}
		if (this.estatusSolicitudEnums.isEmpty()) {
			for (EstatusSolicitudEnum estatusSolicitudEnum : EstatusSolicitudEnum
					.values()) {
				this.estatusSolicitudEnums.add(estatusSolicitudEnum);
			}
		}
		return estatusSolicitudEnums;
	}

	public void setEstatusSolicitudEnums(
			List<EstatusSolicitudEnum> estatusSolicitudEnums) {
		this.estatusSolicitudEnums = estatusSolicitudEnums;
	}

	public UrgenciaEnum getUrgenciaEnum() {
		return urgenciaEnum;
	}

	public void setUrgenciaEnum(UrgenciaEnum urgenciaEnum) {
		this.urgenciaEnum = urgenciaEnum;
		this.getSolicitudAyuda().setUrgencia(urgenciaEnum.ordinal());
	}

	public List<UrgenciaEnum> getUrgenciaEnums() {
		if (this.urgenciaEnums == null) {
			this.urgenciaEnums = new ArrayList<>();
		}
		if (this.urgenciaEnums.isEmpty()) {
			for (UrgenciaEnum urgenciaEnum : UrgenciaEnum.values()) {
				this.urgenciaEnums.add(urgenciaEnum);
			}
		}
		return urgenciaEnums;
	}

	public void setUrgenciaEnums(List<UrgenciaEnum> urgenciaEnums) {
		this.urgenciaEnums = urgenciaEnums;
	}

	public List<Trabajador> getTrabajadores() {
		if (this.trabajadores == null) {
			this.trabajadores = new ArrayList<>();
		}
		if (this.trabajadores.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			EstatusPadrinoEnum.POSTULADO.ordinal();
			criterios.put("estatusTrabajador",
					String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultarCriterios(TypeQuery.EQUAL, criterios);

			/*
			 * PayloadTrabajadorResponse payloadTrabajadorResponse =
			 * S.TrabajadorService .consultarTodos();
			 */

			if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
				Alert.showMessage(payloadTrabajadorResponse);
			}

			this.trabajadores.addAll(payloadTrabajadorResponse.getObjetos());
		}

		return trabajadores;
	}

	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public EstudioSocioEconomico getEstudioSocioEconomico() {
		return estudioSocioEconomico;
	}

	public void setEstudioSocioEconomico(
			EstudioSocioEconomico estudioSocioEconomico) {
		this.estudioSocioEconomico = estudioSocioEconomico;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

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

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);

		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		executeCancelar(currentStep);
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setEstudioSocioEconomico(new EstudioSocioEconomico());
		this.setFecha(new Date());
		this.setSelectedObject(new SolicitudAyuda());
		BindUtils.postNotifyChange(null, null, this, "*");
		restartWizard();
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/selectSolicitudAyuda.zul");
		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/selectEvaluador.zul");
		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/EstudioSocioEconomicoFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusSolicitud",
				String.valueOf(EstatusSolicitudEnum.PENDIENTE.ordinal()));
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		return payloadSolicitudAyudaResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {

		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar una <b>Solicitud de Ayuda</b>";
			}
		}

		if (currentStep == 2) {
			if (this.getEstudioSocioEconomico().getFkTrabajador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evaluador</b>";
			}
		}

		if (currentStep == 3) {
			return "E:Error Code 5-No hay otro paso";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			try {
				UtilValidate.validateDate(this.getFecha().getTime(),
						"Fecha Planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(this
								.getSolicitudAyuda().getFecha()), "dd/MM/yyyy");
			} catch (Exception e) {
				return e.getMessage();
			}

		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			this.getEstudioSocioEconomico().setFecha(this.getFecha().getTime());

			this.getEstudioSocioEconomico().setFkSolicitudAyuda(
					getSolicitudAyudaSelected());
			this.getEstudioSocioEconomico().setEstatusEstudio(
					EstatusEstudioSocioEconomicoEnum.NO_REALIZADO.ordinal());
			this.getSolicitudAyuda().setEstatusSolicitud(
					EstatusSolicitudEnum.EN_PROCESO.ordinal());

			getSolicitudAyudaSelected().setEstatusSolicitud(
					EstatusSolicitudEnum.EN_PROCESO.ordinal());
			PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse = S.EstudioSocioEconomicoService
					.incluir(this.estudioSocioEconomico);
			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.modificar(getSolicitudAyudaSelected());
			if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				return (String) payloadSolicitudAyudaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			if (UtilPayload.isOK(payloadEstudioSocioEconomicoResponse)) {

				this.setEstudioSocioEconomico(new EstudioSocioEconomico());
				this.setFecha(new Date());
				this.setSelectedObject(new SolicitudAyuda());
				BindUtils.postNotifyChange(null, null, this,
						"estudioSocioEconomico");
				BindUtils.postNotifyChange(null, null, this, "fecha");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "solicitudAyuda");
				goToNextStep();
			}
			return (String) payloadEstudioSocioEconomicoResponse
					.getInformacion(IPayloadResponse.MENSAJE);

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 2) {
			this.setSolicitudAyuda(getSolicitudAyudaSelected());
		}

		if (currentStep == 3) {
			this.setTrabajador(this.estudioSocioEconomico.getFkTrabajador());
		}
	}

	public SolicitudAyuda getSolicitudAyuda() {
		return solicitudAyuda;
	}

	public void setSolicitudAyuda(SolicitudAyuda solicitudAyuda) {
		this.solicitudAyuda = solicitudAyuda;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public SolicitudAyuda getSolicitudAyudaSelected() {
		return (SolicitudAyuda) this.selectedObject;
	}
}
