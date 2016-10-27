package ve.smile.gestion.evento.planificaion.tarea.asignacion.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.PlantillaEventoTarea;
import ve.smile.dto.Tarea;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventPlanTareaRecursoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;
import karen.core.crux.alert.Alert;
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

public class VM_TareaEventoPlanificado extends VM_WindowWizard{
	
	private List<Tarea> tareas = new ArrayList<>();
	private Set<Tarea> tareasSeleccionadas = new HashSet<>();
	private List<Tarea> eventoTareas = new ArrayList<>();
	private Set<Tarea> eventoTareasSeleccionadas = new HashSet<>();
	// forEachStatus.index
	private List<EventPlanTarea> eventoPlanificadotareas = new ArrayList<>();
	private List<EventPlanTarea> eventoPlanificadotareasDelete = new ArrayList<>();
	private List<Tarea> eventoTareasAux = new ArrayList<>();

	private Directorio directorio;

	private int indexTarea;

	@Init(superclass = true)
	public void childInit() {
		tareas = S.TareaService.consultarTodos().getObjetos();
	}
	
	@Command
	@NotifyChange({ "tareas", "eventoTareas",
			"tareasSeleccionadas", "eventoTareasSeleccionadas" })
	public void agregarActividadesPlantilla() {
		if (this.getTareasSeleccionadas() != null
				&& this.getTareasSeleccionadas().size() > 0) {
			this.getEventoTareas().addAll(tareasSeleccionadas);
			this.getEventoTareasSeleccionadas().clear();
			this.getTareasSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "tareas", "eventoTareas",
			"tareasSeleccionadas", "eventoTareasSeleccionadas" })
	public void removerActividadesPlantilla() {	
		if (this.getEventoTareasSeleccionadas() != null
				&& this.getEventoTareasSeleccionadas().size() > 0) {
			for (Tarea tarea : this.eventoTareasSeleccionadas) {
				Map<String, String> criterios = new HashMap<String, String>();

				criterios
						.put("fkEventPlanTarea.fkEventoPlanificado.idEventoPlanificado", String
								.valueOf(this.getEventoPlanificadoSelected()
										.getIdEventoPlanificado()));
				criterios.put("fkEventPlanTarea.fkTarea.idTarea",
						String.valueOf(tarea.getIdTarea()));
				// Table Relation TsPlanActividadTrabajadores
				PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajador = S.EventPlanTareaTrabajadorService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadEventPlanTareaTrabajador)) {
					Alert.showMessage(payloadEventPlanTareaTrabajador
							.getInformacion(IPayloadResponse.MENSAJE));
					return;
				}

				Integer countEvenPsTar = Double.valueOf(
						String.valueOf(payloadEventPlanTareaTrabajador
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();
				if (countEvenPsTar > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que se encuentra asignada a "
							+ countEvenPsTar
							+ " Trabajadores");
					return;
				}
				// Table Relation TsPlanActividadVoluntario
				PayloadEventPlanTareaVoluntarioResponse payloadEventPlaTarVol = S.EventPlanTareaVoluntarioService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadEventPlaTarVol)) {
					Alert.showMessage(payloadEventPlaTarVol
							.getInformacion(IPayloadResponse.MENSAJE));
					return;
				}

				Integer contVolTareEven = Double.valueOf(
						String.valueOf(payloadEventPlaTarVol
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (contVolTareEven > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que se encuentra asignada a "
							+ contVolTareEven + " Voluntarios");
					return;
				}

				// Table Relation TsPlanActividadVoluntario
				PayloadIndicadorEventoPlanTareaResponse payloadIndicadorIndEventPsTarea = S.IndicadorEventoPlanTareaService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadIndicadorIndEventPsTarea)) {
					Alert.showMessage(payloadIndicadorIndEventPsTarea);
				}

				Integer countIndicadorEventPlanTarea = Double.valueOf(
						String.valueOf(payloadIndicadorIndEventPsTarea
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countIndicadorEventPlanTarea > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está asociada a "
							+ countIndicadorEventPlanTarea + " Indicadores");
					return;
				}
				// Table Relation TsPlanActividadRecurso
				PayloadEventPlanTareaRecursoResponse payloadEventPsRecurso = S.EventPlanTareaRecursoService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadEventPsRecurso)) {
					Alert.showMessage(payloadEventPsRecurso);
					return;
				}

				Integer countEventPlaRecursos = Double.valueOf(
						String.valueOf(payloadEventPsRecurso
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countEventPlaRecursos > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está asociada a "
							+ countEventPlaRecursos + " Recursos");
					return;
				}
				this.getEventoTareas().remove(tarea);
			}

			this.getTareasSeleccionadas().clear();
			this.getEventoTareasSeleccionadas().clear();
		}
	}

	public boolean disabledActividad(Tarea tarea) {
		return this.getEventoTareas().contains(tarea);
	}

	@Command("buscarPlantilla")
	public void buscarPlantilla() {
		CatalogueDialogData<PlantillaEventoTarea> catalogueDialogData = new CatalogueDialogData<PlantillaEventoTarea>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<PlantillaEventoTarea>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<PlantillaEventoTarea> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<PlantillaEventoTarea> listPlantillaEventoTarea = new ArrayList<>();
						listPlantillaEventoTarea = catalogueDialogCloseEvent
								.getEntities();

						refreshPlantillaEventoTarea(listPlantillaEventoTarea);
					}
				});

		catalogueDialogData.put("evento", this
				.getEventoPlanificadoSelected().getFkEvento());
		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/asignacion/plantillaEvento.zul",
						catalogueDialogData);
	}

	public void refreshPlantillaEventoTarea(
			List<PlantillaEventoTarea> listPlantillaEventoTarea) {

		for (PlantillaEventoTarea plantillaEvenPs : listPlantillaEventoTarea) {
			if (!this.getEventoTareas().contains(
					plantillaEvenPs.getFkTarea())) {
				this.getEventoTareas().add(plantillaEvenPs.getFkTarea());
			}
		}

		BindUtils
				.postNotifyChange(null, null, this, "trabajoSocialActividades");
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
	
	@Command("buscarDirectorio")
	public void buscarDirectorio(@BindingParam("index") int index) {
		this.indexTarea = index;
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						directorio = catalogueDialogCloseEvent.getEntity();
						refreshDirectorio();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/asignacion/catalogoDirectorio.zul",
						catalogueDialogData);
	}
	
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}
		}
		return "";
	}
	
	@Command("removerDirectorio")
	public void removerDirectorio(@BindingParam("index") int index) {
		this.getEventoPlanificadotareas().get(indexTarea)
				.setFkDirectorio(null);
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
	}

	
	public void refreshDirectorio() {
		this.getEventoPlanificadotareas().get(indexTarea)
				.setFkDirectorio(directorio);
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
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
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/selectTareaEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/editTareaEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/registroCompletado.zul");
		return urls;
	}
	
	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		
		if(currentStep == 1){
			this.eventoTareas = new ArrayList<>();
			Map<String, String> criterios = new HashMap<>();
			EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
			criterios.put("fkEventoPlanificado.idEventoPlanificado", eventoPlanificado.getIdEventoPlanificado()+"");
			PayloadEventPlanTareaResponse eventPlanTareaResponse = S.EventPlanTareaService.consultarCriterios(TypeQuery.EQUAL, criterios);
			
			if(eventPlanTareaResponse.getObjetos() != null & eventPlanTareaResponse.getObjetos().size() > 0){				
				for(EventPlanTarea eventPlanTarea: eventPlanTareaResponse.getObjetos()){
					
					this.eventoTareas.add(eventPlanTarea.getFkTarea()); 
					this.eventoPlanificadotareas.add(eventPlanTarea);
				}								
			}
			BindUtils.postNotifyChange(null, null, this, "eventoTareas");
			BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
		}
		
		if (currentStep == 2) {
			if (this.getEventoTareas().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos una <b>Tarea</b> al evento planificado.";
			} else {
				boolean validar = true;
				List<EventPlanTarea> eventoPsTarea2 = new ArrayList<>();
				eventoPsTarea2.addAll(new ArrayList<>(this
						.getEventoPlanificadotareas()));
				for (Tarea tarea : this.eventoTareas) {
					for (EventPlanTarea eventPsTrea : this
							.getEventoPlanificadotareas()) {
						if (eventPsTrea.getFkTarea().getIdTarea()
								.equals(tarea.getIdTarea())) {
							validar = false;
							eventoPsTarea2.remove(eventPsTrea);
							break;
						}
					}
					if (validar) {
						EventPlanTarea eventTarePla = new EventPlanTarea();
						eventTarePla.setFkTarea(tarea);
						eventTarePla.setFkEventoPlanificado((EventoPlanificado) this
								.getSelectedObject());
						eventTarePla.setEjecucion(false);
						this.getEventoPlanificadotareas().add(eventTarePla);
					}
					validar = true;
				}

				this.getEventoPlanificadotareasDelete().addAll(eventoPsTarea2);
				this.getEventoPlanificadotareas().removeAll(eventoPsTarea2);

				BindUtils.postNotifyChange(null, null, this, "*");
			}			

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			for (EventPlanTarea eventPsTarea : this.eventoPlanificadotareas) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, -1);

				if (eventPsTarea.getFechaPlanificada() == null
						|| !eventPsTarea.getFechaPlanificadaDate().after(
								calendar.getTime())) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(eventPsTarea.getFkTarea()
							.getNombre());
				}
			}
			actividades = stringBuilder.toString();
			if (!actividades.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Fecha de Planificación </b> de los siguientes tareas: <b>"
						+ actividades + "</b>";
			}

		}
		return "";
	}
	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			for (EventPlanTarea eventoPsTarea : this.getEventoPlanificadotareas()) {
				PayloadEventPlanTareaResponse payloadEventPsTareaResponse;
				if (eventoPsTarea.getIdEventPlanTarea() == null) {
					payloadEventPsTareaResponse = S.EventPlanTareaService
							.incluir(eventoPsTarea);
				} else {
					payloadEventPsTareaResponse = S.EventPlanTareaService
							.modificar(eventoPsTarea);
				}

				if (!UtilPayload.isOK(payloadEventPsTareaResponse)) {
					return (String) payloadEventPsTareaResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}
			for (EventPlanTarea eveTarePls : this
					.getEventoPlanificadotareasDelete()) {
				if (eveTarePls.getIdEventPlanTarea() != null) {
					PayloadEventPlanTareaResponse payloadEvTarePs
					= S.EventPlanTareaService
							.eliminar(eveTarePls.getIdEventPlanTarea());
					if (!UtilPayload.isOK(payloadEvTarePs)) {
						return (String) payloadEvTarePs
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}
			}		

			goToNextStep();
		}

		return "";
	}
	
	@Override
	public String executeCustom1(Integer currentStep) {
		this.setEventoPlanificadotareas(new ArrayList<EventPlanTarea>());
		this.setEventoTareas(new ArrayList<Tarea>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
		BindUtils
				.postNotifyChange(null, null, this, "eventoTareas");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setEventoPlanificadotareas(new ArrayList<EventPlanTarea>());
		this.setEventoTareas(new ArrayList<Tarea>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
		BindUtils
				.postNotifyChange(null, null, this, "eventoTareas");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		

		restartWizard();
		return "";
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
	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}

	public Set<Tarea> getTareasSeleccionadas() {
		return tareasSeleccionadas;
	}

	public void setTareasSeleccionadas(Set<Tarea> tareasSeleccionadas) {
		this.tareasSeleccionadas = tareasSeleccionadas;
	}

	public List<Tarea> getEventoTareas() {
		return eventoTareas;
	}

	public void setEventoTareas(List<Tarea> eventoTareas) {
		this.eventoTareas = eventoTareas;
	}

	public Set<Tarea> getEventoTareasSeleccionadas() {
		return eventoTareasSeleccionadas;
	}

	public void setEventoTareasSeleccionadas(Set<Tarea> eventoTareasSeleccionadas) {
		this.eventoTareasSeleccionadas = eventoTareasSeleccionadas;
	}

	public List<EventPlanTarea> getEventoPlanificadotareas() {
		return eventoPlanificadotareas;
	}

	public void setEventoPlanificadotareas(
			List<EventPlanTarea> eventoPlanificadotareas) {
		this.eventoPlanificadotareas = eventoPlanificadotareas;
	}

	public List<Tarea> getEventoTareasAux() {
		return eventoTareasAux;
	}

	public void setEventoTareasAux(List<Tarea> eventoTareasAux) {
		this.eventoTareasAux = eventoTareasAux;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public int getIndexTarea() {
		return indexTarea;
	}

	public void setIndexTarea(int indexTarea) {
		this.indexTarea = indexTarea;
	}

	public EventoPlanificado getEventoPlanificadoSelected() {
		return (EventoPlanificado) this.getSelectedObject();
	}

	public List<EventPlanTarea> getEventoPlanificadotareasDelete() {
		return eventoPlanificadotareasDelete;
	}

	public void setEventoPlanificadotareasDelete(
			List<EventPlanTarea> eventoPlanificadotareasDelete) {
		this.eventoPlanificadotareasDelete = eventoPlanificadotareasDelete;
	}
	
	

}
