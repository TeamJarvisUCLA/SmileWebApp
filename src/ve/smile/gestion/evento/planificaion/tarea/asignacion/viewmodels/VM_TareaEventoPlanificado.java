package ve.smile.gestion.evento.planificaion.tarea.asignacion.viewmodels;

import java.util.ArrayList;
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
import ve.smile.dto.Actividad;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Tarea;
import ve.smile.dto.TsPlanActividad;
import ve.smile.payload.response.PayloadEventPlanPatrocinadorResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import lights.core.payload.response.IPayloadResponse;

public class VM_TareaEventoPlanificado extends
VM_WindowWizard<EventoPlanificado>{
	
	private List<Tarea> tareas = new ArrayList<>();
	private Set<Tarea> tareasSeleccionadas = new HashSet<>();
	private List<Tarea> eventoTareas = new ArrayList<>();
	private Set<Tarea> eventoTareasSeleccionadas = new HashSet<>();
	// forEachStatus.index
	private List<EventPlanTarea> eventoPlanificadotareas = new ArrayList<>();

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
			this.getEventoTareas().removeAll(
					eventoTareasSeleccionadas);
			this.getTareasSeleccionadas().clear();
			this.getEventoTareasSeleccionadas().clear();
		}
	}

	public boolean disabledActividad(Tarea tarea) {
		return this.getEventoTareas().contains(tarea);
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

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
	
	public void refreshDirectorio() {
		this.getEventoPlanificadotareas().get(indexTarea)
				.setFkDirectorio(directorio);
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart");
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
		if (currentStep == 2) {
			if (this.getEventoTareas().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos una <b>Tarea</b> al evento planificado.";
			} else {
				this.getEventoPlanificadotareas().clear();
				for (Tarea tarea : this.getEventoTareas()) {
					EventPlanTarea eventPlanTarea = new EventPlanTarea();
					eventPlanTarea.setFkTarea(tarea);;
					eventPlanTarea.setFkEventoPlanificado(this.getSelectedObject());
					this.getEventoPlanificadotareas().add(eventPlanTarea);
				}
				BindUtils
						.postNotifyChange(null, null, this, "indicadorTsPlans");
			}

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
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

	
	
	

}