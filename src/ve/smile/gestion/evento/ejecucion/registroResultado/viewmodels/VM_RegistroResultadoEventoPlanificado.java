package ve.smile.gestion.evento.ejecucion.registroResultado.viewmodels;

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
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.IndicadorEventoPlanTarea;
import ve.smile.dto.IndicadorTsPlanActividad;
import ve.smile.dto.TsPlanActividad;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;

public class VM_RegistroResultadoEventoPlanificado extends VM_WindowWizard {

	private List<EventPlanTarea> listEventPlanTareas = new ArrayList<>();
	private int indexTarea;

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
			for (EventPlanTarea obj : listEventPlanTareas) {
				if (obj.getIndicadorEventoPlanTareas() != null
						& obj.getIndicadorEventoPlanTareas().size() > 0) {
					for (IndicadorEventoPlanTarea indicadorTareaPlanEvent : obj
							.getIndicadorEventoPlanTareas()) {

						PayloadIndicadorEventoPlanTareaResponse payloadIndicadorEventPlanResponse = S.IndicadorEventoPlanTareaService
								.modificar(indicadorTareaPlanEvent);
						if (!UtilPayload
								.isOK(payloadIndicadorEventPlanResponse)) {
							return (String) payloadIndicadorEventPlanResponse
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
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusEvento", EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal()+"");
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = 
				S.EventoPlanificadoService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);

		return payloadEventoPlanificadoResponse;
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

		urls.add("views/desktop/gestion/evento/ejecucion/registrarResultado/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/registrarResultado/registroTareaIndicadoresEventoPs.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/registrarResultado/registroCompletado.zul");

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
	public String executeCancelar(Integer currentStep) {
		this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
		BindUtils.postNotifyChange(null, null, this, "*");

		restartWizard();
		return "";
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelectedObject().getIdEventoPlanificado()));

			this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadEventPlanTareaResponse)) {
				return (String) payloadEventPlanTareaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countEventPlanTarea = Double.valueOf(
					String.valueOf(payloadEventPlanTareaResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countEventPlanTarea <= 0) {
				return "E:Error 0:El evento planificado seleccionado <b>no tiene tareas asignadas</b>, debe asignarle al menos una.";
			}

			parametro = new HashMap<String, String>();
			parametro.put("fkEventPlanTarea.fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelectedObject().getIdEventoPlanificado()));

			PayloadIndicadorEventoPlanTareaResponse payloadIndicadorEventPlanTareResponse = S.IndicadorEventoPlanTareaService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadIndicadorEventPlanTareResponse)) {
				return (String) payloadIndicadorEventPlanTareResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countPlIndicadores = Double.valueOf(
					String.valueOf(payloadIndicadorEventPlanTareResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			System.err.println(countPlIndicadores);
			if (countPlIndicadores <= 0) {
				return "E:Error 0:Las Tarea del evento planificado seleccionado <b>no tiene indicadores asociados.</b>";
			}
			
			

		}

		if (currentStep == 2) {
			return "E:Error Code 5-No hay otro paso";
		}

		return "";
	}
	
	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelectedObject().getIdEventoPlanificado()));

			this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadEventPlanTareaResponse)) {
				this.listEventPlanTareas.addAll(
						payloadEventPlanTareaResponse.getObjetos());
			}

			for (EventPlanTarea eventPlanTarea : this
					.listEventPlanTareas) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkEventPlanTarea.idEventPlanTarea",
						String.valueOf(eventPlanTarea.getIdEventPlanTarea()));
				PayloadIndicadorEventoPlanTareaResponse payloadIndicadorEventPlanTareaResponse = S.IndicadorEventoPlanTareaService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadEventPlanTareaResponse)) {
					eventPlanTarea
							.setIndicadorEventoPlanTareas(new ArrayList<IndicadorEventoPlanTarea>());
					if (payloadIndicadorEventPlanTareaResponse.getObjetos() != null) {

						for (IndicadorEventoPlanTarea indicadorEventPlanTarea : payloadIndicadorEventPlanTareaResponse
								.getObjetos()) {
							if (indicadorEventPlanTarea.getValorReal() == null) {
								indicadorEventPlanTarea
										.setValorReal(indicadorEventPlanTarea
												.getValorEsperado());
							}
							eventPlanTarea.getIndicadorEventoPlanTareas().add(
									indicadorEventPlanTarea);
						}
					}

				}

			}

			BindUtils.postNotifyChange(null, null, this, "*");

		}

		return "";
	}


	@Override
	public String executeCustom1(Integer currentStep) {
		this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	public List<EventPlanTarea> getListEventPlanTareas() {
		return listEventPlanTareas;
	}

	public void setListEventPlanTareas(List<EventPlanTarea> listEventPlanTareas) {
		this.listEventPlanTareas = listEventPlanTareas;
	}

	public int getIndexTarea() {
		return indexTarea;
	}

	public void setIndexTarea(int indexTarea) {
		this.indexTarea = indexTarea;
	}


	public EventoPlanificado getEventoPlanificadoSelectedObject() {
		// TODO Auto-generated method stub
		return (EventoPlanificado) getSelectedObject();
	}
	
	

}
