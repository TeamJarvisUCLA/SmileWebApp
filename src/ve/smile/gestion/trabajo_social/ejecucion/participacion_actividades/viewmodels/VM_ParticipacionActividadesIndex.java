package ve.smile.gestion.trabajo_social.ejecucion.participacion_actividades.viewmodels;

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
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.dto.TsPlanActividadTrabajador;
import ve.smile.dto.TsPlanActividadVoluntario;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ParticipacionActividadesIndex extends VM_WindowWizard {
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

		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/participacionActividades/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/participacionActividades/actividadesParticipantes.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/participacionActividades/registroCompletado.zul");

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
			Map<String, String> criterios = new HashMap<String, String>();

			criterios.put("fkTsPlanActividad.fkTsPlan.idTsPlan",
					String.valueOf(this.getTsPlanSelected().getIdTsPlan()));

			// Table Relation TsPlanActividadTrabajador
			PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
					.contarCriterios(TypeQuery.EQUAL, criterios);

			if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
				return String.valueOf(payloadTsPlanActividadTrabajadorResponse
						.getInformacion(IPayloadResponse.MENSAJE));
			}
			Integer countTsPlanActividadTrabajadores = Double.valueOf(
					String.valueOf(payloadTsPlanActividadTrabajadorResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();

			// Table Relation TsPlanActividadVoluntario
			PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
					.contarCriterios(TypeQuery.EQUAL, criterios);

			if (!UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
				return String.valueOf(payloadTsPlanActividadVoluntarioResponse
						.getInformacion(IPayloadResponse.MENSAJE));
			}

			Integer countTsPlanActividadVoluntarios = Double.valueOf(
					String.valueOf(payloadTsPlanActividadVoluntarioResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();

			if (countTsPlanActividadTrabajadores <= 0
					&& countTsPlanActividadVoluntarios <= 0) {
				return "E:Error 0:El trabajo social planificado seleccionado <b>no tiene actividades con trabajadores y voluntarios asignados</b>.";
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
			int cant = 0;
			List<TsPlanActividad> auxList = new ArrayList<>();
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				for (TsPlanActividad tsPlanActividad : payloadTsPlanActividadResponse
						.getObjetos()) {
					if (tsPlanActividad.getEjecucion()) {
						this.getListTsPlanActividads().add(tsPlanActividad);
					}
				}
			}
			if (this.getListTsPlanActividads().size() <= 0) {
				return "E:Error 0:El trabajo social planificado seleccionado <b>no tiene actividades ejecutadas</b>.";
			}
			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {
				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkTsPlanActividad.idTsPlanActividad",
						String.valueOf(tsPlanActividad.getIdTsPlanActividad()));
				PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
					tsPlanActividad
							.setTsPlanActividadVoluntarios(payloadTsPlanActividadVoluntarioResponse
									.getObjetos());
					if (tsPlanActividad.getTsPlanActividadVoluntarios() == null) {
						tsPlanActividad
								.setTsPlanActividadVoluntarios(new ArrayList<TsPlanActividadVoluntario>());
					}
					for (TsPlanActividadVoluntario tsPlanActividadVoluntario : tsPlanActividad
							.getTsPlanActividadVoluntarios()) {
						if (String.valueOf(tsPlanActividadVoluntario
								.getEjecucion()) == null) {
							tsPlanActividadVoluntario.setEjecucion(true);
						}
					}
				}

				PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {

					tsPlanActividad
							.setTsPlanActividadTrabajadors(payloadTsPlanActividadTrabajadorResponse
									.getObjetos());
					if (tsPlanActividad.getTsPlanActividadTrabajadors() == null) {
						tsPlanActividad
								.setTsPlanActividadTrabajadors(new ArrayList<TsPlanActividadTrabajador>());
					}
					for (TsPlanActividadTrabajador tsPlanActividadTrabajador : tsPlanActividad
							.getTsPlanActividadTrabajadors()) {
						if (String.valueOf(tsPlanActividadTrabajador
								.getEjecucion()) == null) {
							tsPlanActividadTrabajador.setEjecucion(true);
						}
					}
				}
				cant += tsPlanActividad.getTsPlanActividadTrabajadors().size();
				cant += tsPlanActividad.getTsPlanActividadVoluntarios().size();
				if (tsPlanActividad.getTsPlanActividadTrabajadors().size() <= 0
						&& tsPlanActividad.getTsPlanActividadVoluntarios()
								.size() <= 0) {
					auxList.add(tsPlanActividad);
				}
			}

			this.getListTsPlanActividads().removeAll(auxList);

			if (cant <= 0) {
				return "E:Error 0:Las actividades ejecutadas del trabajo social seleccionado <b>no tiene participantes asociados</b>.";
			}

		}
		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = new PayloadTsPlanActividadTrabajadorResponse();
			PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = new PayloadTsPlanActividadVoluntarioResponse();
			for (TsPlanActividad obj : this.getListTsPlanActividads()) {

				for (TsPlanActividadTrabajador tsPlanActividadTrabajador : obj
						.getTsPlanActividadTrabajadors()) {
					if (tsPlanActividadTrabajador.getEjecucion()) {
						tsPlanActividadTrabajador.setFkMotivo(null);
					}
					payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
							.modificar(tsPlanActividadTrabajador);

					if (!UtilPayload
							.isOK(payloadTsPlanActividadTrabajadorResponse)) {
						return (String) payloadTsPlanActividadTrabajadorResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

				}

				for (TsPlanActividadVoluntario tsPlanActividadVoluntario : obj
						.getTsPlanActividadVoluntarios()) {
					if (tsPlanActividadVoluntario.getEjecucion()) {
						tsPlanActividadVoluntario.setFkMotivo(null);
					}
					payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
							.modificar(tsPlanActividadVoluntario);

					if (!UtilPayload
							.isOK(payloadTsPlanActividadVoluntarioResponse)) {
						return (String) payloadTsPlanActividadVoluntarioResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

				}

			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		goToNextStep();
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		executeCancelar(currentStep);
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setListTsPlanActividads(null);
		BindUtils.postNotifyChange(null, null, this, "*");

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