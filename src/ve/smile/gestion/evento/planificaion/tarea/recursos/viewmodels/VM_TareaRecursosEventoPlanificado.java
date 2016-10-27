package ve.smile.gestion.evento.planificaion.tarea.recursos.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventPlanTareaRecurso;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.dto.Recurso;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventPlanTareaRecursoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;

public class VM_TareaRecursosEventoPlanificado extends
		VM_WindowWizard {

	private List<Indicador> auxlistIndicadors = new ArrayList<>();
	private List<EventPlanTarea> listEventPlanTareas = new ArrayList<>();
	private List<EventPlanTareaRecurso> listEventPlanTareasRecursoDelete = new ArrayList<EventPlanTareaRecurso>();
	private int indexTarea;

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarIndicadores")
	public void buscarVoluntario(@BindingParam("index") int index) {
		this.indexTarea = index;
		CatalogueDialogData<Recurso> catalogueDialogData = new CatalogueDialogData<Recurso>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Recurso>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Recurso> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Recurso> listRecurso = new ArrayList<Recurso>();
						listRecurso = catalogueDialogCloseEvent.getEntities();

						refreshRecurso(listRecurso);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/recursos/catalogoRecursos.zul",
						catalogueDialogData);
	}

	public void refreshRecurso(List<Recurso> listRecurso) {

		boolean validar = true;
		List<EventPlanTareaRecurso> listAux2 = new ArrayList<>();
		EventPlanTareaRecurso eventPlanTareaRecurso = new EventPlanTareaRecurso();

		if (listEventPlanTareas.get(indexTarea).getIndicadorEventoPlanTareas() != null) {
			for (EventPlanTareaRecurso iet : listEventPlanTareas.get(
					indexTarea).getListEventPlanTareaRecursos()) {
				eventPlanTareaRecurso = new EventPlanTareaRecurso();
				eventPlanTareaRecurso.setFkEventPlanTarea(listEventPlanTareas
								.get(indexTarea));
				eventPlanTareaRecurso.setFkRecurso(iet.getFkRecurso());
				eventPlanTareaRecurso.setCantidad(iet.getCantidad());
				eventPlanTareaRecurso.setFechaAsignacion(iet.getFechaAsignacion());
				listAux2.add(eventPlanTareaRecurso);
			}
		}

		for (Recurso recurso : listRecurso) {
			if (listEventPlanTareas.get(indexTarea)
					.getListEventPlanTareaRecursos() != null) {

				for (EventPlanTareaRecurso iet : listEventPlanTareas.get(
						indexTarea).getListEventPlanTareaRecursos()) {
					if (iet.getFkRecurso().getIdRecurso()
							.equals(recurso.getIdRecurso())) {

						validar = false;

					}
				}

				if (validar) {
					eventPlanTareaRecurso = new EventPlanTareaRecurso();
					eventPlanTareaRecurso
							.setFkEventPlanTarea(listEventPlanTareas
									.get(indexTarea));
					eventPlanTareaRecurso.setFkRecurso(recurso);
					eventPlanTareaRecurso.setFechaAsignacion(new Date().getTime());
					listAux2.add(eventPlanTareaRecurso);

				}

			} else {
				eventPlanTareaRecurso = new EventPlanTareaRecurso();
				eventPlanTareaRecurso
						.setFkEventPlanTarea(listEventPlanTareas
								.get(indexTarea));
				eventPlanTareaRecurso.setFkRecurso(recurso);
				eventPlanTareaRecurso.setFechaAsignacion(new Date().getTime());
				listAux2.add(eventPlanTareaRecurso);

			}

		}

		listEventPlanTareas.get(indexTarea).setListEventPlanTareaRecursos(
				listAux2);
		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
	}

	@Command("eliminarRecurso")
	public void eliminarRecurso(
			@BindingParam("indicadorEventoPsTarea") EventPlanTareaRecurso eventPlanTareaRecurso,
			@BindingParam("index") int index) {
		this.listEventPlanTareasRecursoDelete.add(eventPlanTareaRecurso);
		this.getListEventPlanTareas().get(index)
				.getListEventPlanTareaRecursos()
				.remove(eventPlanTareaRecurso);
		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
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
			PayloadEventPlanTareaRecursoResponse payloadEventPlanRecursoResponse = new PayloadEventPlanTareaRecursoResponse();
			for (EventPlanTarea obj : this.listEventPlanTareas) {
				if(obj.getListEventPlanTareaRecursos()!=null){
				for (EventPlanTareaRecurso eventPlanTareaRecurso : obj
						.getListEventPlanTareaRecursos()) {
					EventPlanTareaRecurso eventPlanTareaRecurso2 = new EventPlanTareaRecurso();
					eventPlanTareaRecurso2
							.setFkEventPlanTarea(new EventPlanTarea(obj
									.getIdEventPlanTarea()));
					eventPlanTareaRecurso2.setFkRecurso(eventPlanTareaRecurso
							.getFkRecurso());
					eventPlanTareaRecurso2.setCantidad(eventPlanTareaRecurso
							.getCantidad());
					eventPlanTareaRecurso2
							.setFechaAsignacion(eventPlanTareaRecurso
									.getFechaAsignacion());
					eventPlanTareaRecurso2
							.setIdEventPlanTareaRecurso(eventPlanTareaRecurso
									.getIdEventPlanTareaRecurso());
					if (eventPlanTareaRecurso2.getIdEventPlanTareaRecurso() == null) {
						payloadEventPlanRecursoResponse = S.EventPlanTareaRecursoService.incluir(eventPlanTareaRecurso2);
					} else {
						payloadEventPlanRecursoResponse = S.EventPlanTareaRecursoService
								.modificar(eventPlanTareaRecurso2);
					}

					if (!UtilPayload
							.isOK(payloadEventPlanRecursoResponse)) {
						
						return (String) payloadEventPlanRecursoResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}
			}

			}
			if(this.listEventPlanTareasRecursoDelete.size()>0 & this.listEventPlanTareasRecursoDelete != null){
			for (EventPlanTareaRecurso eventTareaRecurso : this
					.listEventPlanTareasRecursoDelete) {
				if (eventTareaRecurso.getIdEventPlanTareaRecurso() != null) {
					payloadEventPlanRecursoResponse = S.EventPlanTareaRecursoService
							.eliminar(eventTareaRecurso
									.getIdEventPlanTareaRecurso());
					if (!UtilPayload
							.isOK(payloadEventPlanRecursoResponse)) {
						return (String) payloadEventPlanRecursoResponse
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

		urls.add("views/desktop/gestion/evento/planificacion/tareas/recursos/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/recursos/tareaRecursos.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/recursos/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					((EventoPlanificado) selectedObject).getIdEventoPlanificado() + "");
			this.listEventPlanTareas = new ArrayList<>();
			this.listEventPlanTareas = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro)
					.getObjetos();
			if (this.listEventPlanTareas.size() > 0) {
				for (EventPlanTarea eventPlanTarea : this.listEventPlanTareas) {
					Map<String, String> criterios = new HashMap<String, String>();
					criterios.put("fkEventPlanTarea.idEventPlanTarea",
							eventPlanTarea.getIdEventPlanTarea()
									+ "");
					PayloadEventPlanTareaRecursoResponse eventPlanTareaRecursoResponse = S.EventPlanTareaRecursoService
							.consultarCriterios(TypeQuery.EQUAL, criterios);
					if (eventPlanTareaRecursoResponse.getObjetos().size() > 0) {

						
						eventPlanTarea.setListEventPlanTareaRecursos(eventPlanTareaRecursoResponse.getObjetos());
					}

				}
			}

		}
		goToNextStep();

		return "";
	}
	
	@Override
	public String executeCancelar(Integer currentStep) {
		this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 2) {
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			for (EventPlanTarea eventPlanTarea : this.listEventPlanTareas) {
				if(eventPlanTarea.getListEventPlanTareaRecursos() != null){
				for (EventPlanTareaRecurso eventPlanTareaRecur : eventPlanTarea
						.getListEventPlanTareaRecursos()) {
					if (eventPlanTareaRecur.getFechaAsignacion() == null) {
						if (!stringBuilder.toString().trim().isEmpty()) {
							stringBuilder.append(",  ");
						}
						stringBuilder.append(eventPlanTareaRecur
								.getFkRecurso().getNombre());
					}

					if (eventPlanTareaRecur.getCantidad() == null
							|| eventPlanTareaRecur.getCantidad() <= 0) {
						if (!stringBuilder2.toString().trim().isEmpty()) {
							stringBuilder2.append(",  ");
						}
						stringBuilder2.append(eventPlanTareaRecur
								.getFkRecurso().getNombre());
					}
				}
				}
			}
			actividades = stringBuilder.toString();
			if (!actividades.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Fecha de Asignación </b> de los siguientes recursos: <b>"
						+ actividades + "</b>";
			}

			if (!stringBuilder2.toString().trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Cantidad </b> de los siguientes recursos: <b>"
						+ stringBuilder2.toString() + "</b>";
			}

		}
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
					String.valueOf(getEventoPlanificadoPlanSelected().getIdEventoPlanificado()));

			this.setListEventPlanTareas(new ArrayList<EventPlanTarea>());
			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadEventPlanTareaResponse)) {
				return (String) payloadEventPlanTareaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countEvenPlanEvento = Double.valueOf(
					String.valueOf(payloadEventPlanTareaResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countEvenPlanEvento <= 0) {
				return "E:Error 0:El evento planificado seleccionado <b>no tiene tarea asignadas</b>, debe asignarle al menos una.";
			}

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

	public List<Indicador> getAuxlistIndicadors() {
		return auxlistIndicadors;
	}

	public void setAuxlistIndicadors(List<Indicador> auxlistIndicadors) {
		this.auxlistIndicadors = auxlistIndicadors;
	}

	public List<EventPlanTareaRecurso> getListEventPlanTareasRecursoDelete() {
		return listEventPlanTareasRecursoDelete;
	}

	public void setListEventPlanTareasRecursoDelete(
			List<EventPlanTareaRecurso> listEventPlanTareasRecursoDelete) {
		this.listEventPlanTareasRecursoDelete = listEventPlanTareasRecursoDelete;
	}
	
	public EventoPlanificado getEventoPlanificadoPlanSelected() {
		return (EventoPlanificado) this.getSelectedObject();
	}

	

}
