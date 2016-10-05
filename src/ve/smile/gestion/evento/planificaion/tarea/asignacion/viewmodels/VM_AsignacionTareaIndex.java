package ve.smile.gestion.evento.planificaion.tarea.asignacion.viewmodels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

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
import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;


public class VM_AsignacionTareaIndex extends VM_WindowWizard<Tarea>{
	
	private EventPlanTarea eventPlanTarea = new EventPlanTarea();
	private List<EventPlanTarea> listEventPlanTareas = new ArrayList<>();
	private Collection<Tarea> collectionTareas = new HashSet<>();
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
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

		botones.put(2, listOperacionWizard2);
		return botones;
	}

	

	@Override
	public IPayloadResponse<Tarea> getDataToTable(Integer cantidadRegistro, Integer cantidad) {
		PayloadTareaResponse payloadTareaResponse = S.TareaService.consultarPaginacion(cantidadRegistro, cantidad);
		return payloadTareaResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/selectTarea.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/asignacion/tareaSeleccionada.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroTrabajoSocialPlanificado.zul");

		return urls;
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}
	
	

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (this.collectionTareas.size()==0) {
				return "E:Error Code 5-Debe seleccionar almenos una <b>Tarea</b>";
			}
		}

		if (currentStep == 2) {
			return "E:Error Code 5-No hay otro paso";
		}

		return "";
	}



	@Override
	public String executeSiguiente(Integer currentStep) {
		if(currentStep ==1){
			this.listEventPlanTareas = new ArrayList<>();
			for (Tarea objeto: collectionTareas) {
				EventPlanTarea eventPlanTarea = new EventPlanTarea();
				eventPlanTarea.setFkTarea(objeto);
				this.listEventPlanTareas.add(eventPlanTarea);
			}
		}
		goToNextStep();
		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}
	
	@Command("buscarDirectorio")
	public void buscarDirectorio(@BindingParam("eventoTarea") EventPlanTarea eventPlanTarea) {
		this.eventPlanTarea = eventPlanTarea;
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
					

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/asignacion/catalogoDirectorio.zul",
						catalogueDialogData);
	}
	
	public void refreshEventPlanTarea(Directorio directorio, EventPlanTarea eventPlanTarea) {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
	}
	
	@Command("date")
	public void asignarFecha(@BindingParam("tarea") EventPlanTarea eventPlanTarea){
	for (int i = 0; i<listEventPlanTareas.size(); i++) {
		if(listEventPlanTareas.get(i).getFkTarea().equals(eventPlanTarea.getFkTarea())){
			listEventPlanTareas.get(i).setFechaPlanificada(this.fechaPlanificada.getTime());
		}
		
	}
		
	}

	public EventPlanTarea getEventPlanTarea() {
		return eventPlanTarea;
	}

	public void setEventPlanTarea(EventPlanTarea eventPlanTarea) {
		this.eventPlanTarea = eventPlanTarea;
	}

	public List<EventPlanTarea> getListEventPlanTareas() {
		return listEventPlanTareas;
	}

	public void setListEventPlanTareas(List<EventPlanTarea> listEventPlanTareas) {
		this.listEventPlanTareas = listEventPlanTareas;
	}

	public Collection<Tarea> getCollectionTareas() {
		return collectionTareas;
	}

	public void setCollectionTareas(Collection<Tarea> collectionTareas) {
		this.collectionTareas = collectionTareas;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public Date getFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}

	
	
	

	
}
