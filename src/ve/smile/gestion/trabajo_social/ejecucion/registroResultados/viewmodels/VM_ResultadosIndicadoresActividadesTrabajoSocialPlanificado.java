package ve.smile.gestion.trabajo_social.ejecucion.registroResultados.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.IndicadorTsPlanActividad;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ResultadosIndicadoresActividadesTrabajoSocialPlanificado extends
		VM_WindowWizard {

	private List<TsPlanActividad> listTsPlanActividads;
	private int indexActividad;

	@Init(superclass = true)
	public void childInit() {

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
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard3.add(operacionWizardCustom);

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			for (TsPlanActividad obj : listTsPlanActividads) {
				if (obj.getIndicadorTsPlanActividads() != null
						& obj.getIndicadorTsPlanActividads().size() > 0) {
					for (IndicadorTsPlanActividad indicadorTsPlanActividad : obj
							.getIndicadorTsPlanActividads()) {

						PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
								.modificar(indicadorTsPlanActividad);
						if (!UtilPayload
								.isOK(payloadIndicadorTsPlanActividadResponse)) {
							return (String) payloadIndicadorTsPlanActividadResponse
									.getInformacion(IPayloadResponse.MENSAJE);
						}
					}
				}
			}
			goToNextStep();
		}

		return "";
	}

	@Override
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTsPlan", String
				.valueOf(EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO
						.ordinal()));

		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTsPlanResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/registrarResultados/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/registrarResultados/registroActividadesIndicadoresTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/registrarResultados/registroCompletado.zul");

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
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				return (String) payloadTsPlanActividadResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countTsPlanActividades = Double.valueOf(
					String.valueOf(payloadTsPlanActividadResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countTsPlanActividades <= 0) {
				return "E:Error 0:El trabajo social planificado seleccionado <b>no tiene actividades asignadas</b>, debe asignarle al menos una.";
			}

			parametro = new HashMap<String, String>();
			parametro.put("fkTsPlanActividad.fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadIndicadorTsPlanActividadResponse)) {
				return (String) payloadIndicadorTsPlanActividadResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countTsPlanActividadesIndicadores = Double.valueOf(
					String.valueOf(payloadIndicadorTsPlanActividadResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			System.err.println(countTsPlanActividadesIndicadores);
			if (countTsPlanActividadesIndicadores <= 0) {
				return "E:Error 0:Las actividades del trabajo social planificado seleccionado <b>no tiene indicadores asociados.</b>";
			}
		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				this.getListTsPlanActividads().addAll(
						payloadTsPlanActividadResponse.getObjetos());
			}

			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkTsPlanActividad.idTsPlanActividad",
						String.valueOf(tsPlanActividad.getIdTsPlanActividad()));
				PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
					tsPlanActividad
							.setIndicadorTsPlanActividads(new ArrayList<IndicadorTsPlanActividad>());
					if (payloadIndicadorTsPlanActividadResponse.getObjetos() != null) {
						tsPlanActividad.getIndicadorTsPlanActividads().addAll(
								payloadIndicadorTsPlanActividadResponse
										.getObjetos());
						System.err.println(payloadIndicadorTsPlanActividadResponse
										.getObjetos().size());
					}

				}

			}

			BindUtils
					.postNotifyChange(null, null, this, "*");

		}

		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setListTsPlanActividads(null);
		this.setSelectedObject(new TsPlan());

		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	public List<TsPlanActividad> getListTsPlanActividads() {
		if (this.listTsPlanActividads == null) {
			listTsPlanActividads = new ArrayList<>();
		}
		return listTsPlanActividads;
	}

	public void setListTsPlanActividads(
			List<TsPlanActividad> listTsPlanActividads) {
		this.listTsPlanActividads = listTsPlanActividads;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
