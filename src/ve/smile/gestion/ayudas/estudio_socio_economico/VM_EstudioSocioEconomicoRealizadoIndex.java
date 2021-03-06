package ve.smile.gestion.ayudas.estudio_socio_economico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EstudioSocioEconomico;
import ve.smile.enums.EstatusEstudioSocioEconomicoEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;

public class VM_EstudioSocioEconomicoRealizadoIndex extends
		VM_WindowWizard<EstudioSocioEconomico> {

	private EstudioSocioEconomico estudioSocioEconomico;

	private Date fechaEjecutada = new Date();

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		setEstudioSocioEconomico(new EstudioSocioEconomico());
		fechaEjecutada = new Date();
	}

	public Date getFechaEjecutada() {
		return fechaEjecutada;
	}

	public void setFechaEjecutada(Date fechaEjecutada) {
		this.fechaEjecutada = fechaEjecutada;
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
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		// TODO Auto-generated method stub
		restartWizard();
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/selectEstudioSocioEconomico.zul");
		urls.add("views/desktop/gestion/ayudas/estudioSocioEconomico/EstudioSocioEconomicoRealizadoFormBasic.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroEstudioSocioEconomicoPlanificado.zul");

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
	public IPayloadResponse<EstudioSocioEconomico> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusEstudio", String
				.valueOf(EstatusEstudioSocioEconomicoEnum.NO_REALIZADO
						.ordinal()));
		PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse = S.EstudioSocioEconomicoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		/*
		 * PayloadEstudioSocioEconomicoResponse
		 * payloadEstudioSocioEconomicoResponse = S.EstudioSocioEconomicoService
		 * .consultarPaginacion(cantidadRegistrosPagina, pagina)
		 */;
		return payloadEstudioSocioEconomicoResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {

		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Estudio Socio Economico</b>";
			}
		}

		if (currentStep == 2) {
			return "E:Error Code 5-No hay otro paso";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			try {
				UtilValidate.validateDate(this.getFechaEjecutada().getTime(),
						"FechaEjecutada Planificada",
						ValidateOperator.GREATER_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
			} catch (Exception e) {
				return e.getMessage();
			}

		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getEstudioSocioEconomico().setFechaEjecutada(
					this.getFechaEjecutada().getTime());

			this.getEstudioSocioEconomico().setEstatusEstudio(
					EstatusEstudioSocioEconomicoEnum.REALIZADO.ordinal());
			PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse = S.EstudioSocioEconomicoService
					.modificar(selectedObject);
			if (UtilPayload.isOK(payloadEstudioSocioEconomicoResponse)) {
				restartWizard();

			}
			return (String) payloadEstudioSocioEconomicoResponse
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

		if (currentStep == 2) {

			this.setEstudioSocioEconomico(selectedObject);

		}
	}

	public EstudioSocioEconomico getEstudioSocioEconomico() {
		return estudioSocioEconomico;
	}

	public void setEstudioSocioEconomico(
			EstudioSocioEconomico estudioSocioEconomico) {
		this.estudioSocioEconomico = estudioSocioEconomico;
	}

}
