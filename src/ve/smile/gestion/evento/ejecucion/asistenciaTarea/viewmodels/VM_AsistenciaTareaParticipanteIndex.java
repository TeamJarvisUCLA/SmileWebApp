package ve.smile.gestion.evento.ejecucion.asistenciaTarea.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventPlanTareaTrabajador;
import ve.smile.dto.EventPlanTareaVoluntario;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Trabajador;
import ve.smile.dto.TsPlanActividad;
import ve.smile.dto.TsPlanActividadTrabajador;
import ve.smile.dto.TsPlanActividadVoluntario;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_AsistenciaTareaParticipanteIndex extends VM_WindowWizard{
	private List<EventPlanTarea> listEventPlanTarea;
	private int indexTarea;

	public List<EventPlanTarea> getListEventPlanTarea() {
		if (this.listEventPlanTarea == null) {
			listEventPlanTarea = new ArrayList<>();
		}
		return listEventPlanTarea;
	}

	public void setListEventPlanTarea(
			List<EventPlanTarea> listEventPlanTarea) {
		this.listEventPlanTarea = listEventPlanTarea;
	}

	public int getIndexTarea() {
		return indexTarea;
	}

	public void setIndexTarea(int indexTarea) {
		this.indexTarea = indexTarea;
	}

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

		urls.add("views/desktop/gestion/evento/ejecucion/actualizarAsistencia/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/actualizarAsistencia/tareaParticipantes.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/actualizarAsistencia/registroCompletado.zul");

		return urls;
	}
	
	

	@Override
	public String executeSiguiente(Integer currentStep) {
//		if (currentStep == 1) {
//			Map<String, String> parametro = new HashMap<String, String>();
//			parametro.put("fkEventoPlanificado.idEventoPlanificado",
//					String.valueOf(getEventoPlanificadoSelected().getIdEventoPlanificado()));
//
//			this.setListEventPlanTarea(null);
//			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
//					.consultarCriterios(TypeQuery.EQUAL, parametro);
//			if (UtilPayload.isOK(payloadEventPlanTareaResponse)) {
//				this.getListEventPlanTarea().addAll(
//						payloadEventPlanTareaResponse.getObjetos());
//			}
//
//			for (EventPlanTarea evePsT : this
//					.getListEventPlanTarea()) {
//
//				Map<String, String> criterios = new HashMap<String, String>();
//				criterios.put("fkEventPlanTarea.idEventPlanTarea",
//						String.valueOf(evePsT.getIdEventPlanTarea()));
//				PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
//						.consultarCriterios(TypeQuery.EQUAL, criterios);
//				if (UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
//					evePsT
//							.setListPlanTareaVoluntarios(payloadEventPlanTareaVoluntarioResponse
//									.getObjetos());
//				}
//
//				PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
//						.consultarCriterios(TypeQuery.EQUAL, criterios);
//				if (UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
//
//					evePsT
//							.setListEventPlanTareaTrabajadors(payloadEventPlanTareaTrabajadorResponse
//									.getObjetos());
//				}
//
//			}
//
//		}
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
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}
				Map<String, String> parametro = new HashMap<String, String>();
				parametro.put("fkEventoPlanificado.idEventoPlanificado",
						String.valueOf(getEventoPlanificadoSelected().getIdEventoPlanificado()));

				this.setListEventPlanTarea(new ArrayList<EventPlanTarea>());
				PayloadEventPlanTareaResponse payloadEventPanTareaResponse = S.EventPlanTareaService
						.contarCriterios(TypeQuery.EQUAL, parametro);
				if (!UtilPayload.isOK(payloadEventPanTareaResponse)) {
					return (String) payloadEventPanTareaResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}

				Integer countTsPlanActividades = Double.valueOf(
						String.valueOf(payloadEventPanTareaResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();
				if (countTsPlanActividades <= 0) {
					return "E:Error 0:El evento planificado seleccionado <b>no tiene tareas asignadas</b>, debe asignarle al menos una.";
				}
				Map<String, String> criterios = new HashMap<String, String>();

				criterios.put("fkEventPlanTarea.fkEventoPlanificado.idEventoPlanificado",
						String.valueOf(this.getEventoPlanificadoSelected().getIdEventoPlanificado()));

				// Table Relation TsPlanActividadTrabajador
				PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
					return String.valueOf(payloadEventPlanTareaTrabajadorResponse
							.getInformacion(IPayloadResponse.MENSAJE));
				}
				Integer countTsPlanActividadTrabajadores = Double.valueOf(
						String.valueOf(payloadEventPlanTareaTrabajadorResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				// Table Relation TsPlanActividadVoluntario
				PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
					return String.valueOf(payloadEventPlanTareaVoluntarioResponse
							.getInformacion(IPayloadResponse.MENSAJE));
				}

				Integer countTsPlanActividadVoluntarios = Double.valueOf(
						String.valueOf(payloadEventPlanTareaVoluntarioResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countTsPlanActividadTrabajadores <= 0
						&& countTsPlanActividadVoluntarios <= 0) {
					return "E:Error 0:El evento planificado seleccionado <b>no tiene tarea con trabajadores y voluntarios asignados</b>.";
				}
			}		

		return "";
	}
	
	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelected().getIdEventoPlanificado()));
			this.setListEventPlanTarea(new ArrayList<EventPlanTarea>());
			int cant = 0;
			List<EventPlanTarea> auxList = new ArrayList<>();
			PayloadEventPlanTareaResponse payloadEvePlanTareaResponse = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadEvePlanTareaResponse)) {
				for (EventPlanTarea eventPlanTarea : payloadEvePlanTareaResponse
						.getObjetos()) {
					if (eventPlanTarea.isEjecucion()) {
						this.listEventPlanTarea.add(eventPlanTarea);
					}
				}
			}
			if (this.listEventPlanTarea.size() <= 0) {
				return "E:Error 0:El evento planificado seleccionado <b>no tiene tarea ejecutadas</b>.";
			}
			for (EventPlanTarea eventPlanEvento : this
					.listEventPlanTarea) {
				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkEventPlanTarea.idEventPlanTarea",
						String.valueOf(eventPlanEvento.getIdEventPlanTarea()));
				PayloadEventPlanTareaVoluntarioResponse payloadEvenPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadEvenPlanTareaVoluntarioResponse)) {
					eventPlanEvento
							.setListPlanTareaVoluntarios(payloadEvenPlanTareaVoluntarioResponse
									.getObjetos());
					if (eventPlanEvento.getListPlanTareaVoluntarios() == null) {
						eventPlanEvento
								.setListPlanTareaVoluntarios(new ArrayList<EventPlanTareaVoluntario>());
					}
					for (EventPlanTareaVoluntario eventPlanTareVoluntario : eventPlanEvento
							.getListPlanTareaVoluntarios()) {
						if (String.valueOf(eventPlanTareVoluntario
								.getEjecucion()) == null) {
							eventPlanTareVoluntario.setEjecucion(true);
						}
					}
				}

				PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {

					eventPlanEvento
							.setListEventPlanTareaTrabajadors(payloadEventPlanTareaTrabajadorResponse
									.getObjetos());
					if (eventPlanEvento.getListEventPlanTareaTrabajadors() == null) {
						eventPlanEvento
								.setListEventPlanTareaTrabajadors(new ArrayList<EventPlanTareaTrabajador>());
					}
					for (EventPlanTareaTrabajador eventPlanTareaTrabajador : eventPlanEvento
							.getListEventPlanTareaTrabajadors()) {
						if (String.valueOf(eventPlanTareaTrabajador
								.getEjecucion()) == null) {
							eventPlanTareaTrabajador.setEjecucion(true);
						}
					}
				}
				cant += eventPlanEvento.getListEventPlanTareaTrabajadors().size();
				cant += eventPlanEvento.getListPlanTareaVoluntarios().size();
				if (eventPlanEvento.getListEventPlanTareaTrabajadors().size() <= 0
						&& eventPlanEvento.getListPlanTareaVoluntarios()
								.size() <= 0) {
					auxList.add(eventPlanEvento);
				}
			}

			this.getListEventPlanTarea().removeAll(auxList);

			if (cant <= 0) {
				return "E:Error 0:Las tareas ejecutadas del evento seleccionado <b>no tiene participantes asociados</b>.";
			}

		}
		return "";
	}
	
	

	@Override
	public String executeFinalizar(Integer currentStep) {
		
//		for(EventPlanTarea tarea: this.listEventPlanTarea){
//			if(tarea.getListPlanTareaVoluntarios()!= null & tarea.getListPlanTareaVoluntarios().size()>0){
//				for(EventPlanTareaVoluntario evePsTaVol: tarea.getListPlanTareaVoluntarios()){
//					EventPlanTareaVoluntario eventPlanTareaVoluntario = S.EventPlanTareaVoluntarioService.consultarUno(evePsTaVol.getIdEventPlanTareaVoluntario()).getObjetos().get(0);
//					eventPlanTareaVoluntario.setEjecucion(evePsTaVol.getEjecucion());
//					PayloadEventPlanTareaVoluntarioResponse eventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService.modificar(eventPlanTareaVoluntario);
//					if (!UtilPayload.isOK(eventPlanTareaVoluntarioResponse)) {
//						 return (String) eventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE);
//					 }
//				}
//			}
//			if(tarea.getListEventPlanTareaTrabajadors() != null & tarea.getListEventPlanTareaTrabajadors().size() > 0){
//				for(EventPlanTareaTrabajador evePsTareaTra: tarea.getListEventPlanTareaTrabajadors()){
//					EventPlanTareaTrabajador eventPlanTareaTrabajador = S.EventPlanTareaTrabajadorService.consultarUno(evePsTareaTra.getIdEventPlanTareaTrabajador()).getObjetos().get(0);
//					eventPlanTareaTrabajador.setEjecucion(evePsTareaTra.getEjecucion());
//					PayloadEventPlanTareaTrabajadorResponse eventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService.modificar(eventPlanTareaTrabajador);
//					if (!UtilPayload.isOK(eventPlanTareaTrabajadorResponse)) {
//						 return (String) eventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE);
//					 }
//				}
//			}
//		}
		goToNextStep();
		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = new PayloadEventPlanTareaTrabajadorResponse();
			PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse = new PayloadEventPlanTareaVoluntarioResponse();
			for (EventPlanTarea obj : this.getListEventPlanTarea()) {

				for (EventPlanTareaTrabajador evntPlanTareaTrabajador : obj
						.getListEventPlanTareaTrabajadors()) {
					if (evntPlanTareaTrabajador.getEjecucion()) {
						evntPlanTareaTrabajador.setFkMotivo(null);
					}
					payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
							.modificar(evntPlanTareaTrabajador);

					if (!UtilPayload
							.isOK(payloadEventPlanTareaTrabajadorResponse)) {
						return (String) payloadEventPlanTareaTrabajadorResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

				}

				for (EventPlanTareaVoluntario tsPlanActividadVoluntario : obj
						.getListPlanTareaVoluntarios()) {
					if (tsPlanActividadVoluntario.getEjecucion()) {
						tsPlanActividadVoluntario.setFkMotivo(null);
					}
					payloadEventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
							.modificar(tsPlanActividadVoluntario);

					if (!UtilPayload
							.isOK(payloadEventPlanTareaVoluntarioResponse)) {
						return (String) payloadEventPlanTareaVoluntarioResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

				}

			}
		}
		return "";
	}
	
	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	public EventoPlanificado getEventoPlanificadoSelected() {
		return (EventoPlanificado) this.getSelectedObject();
	}



	
	
}