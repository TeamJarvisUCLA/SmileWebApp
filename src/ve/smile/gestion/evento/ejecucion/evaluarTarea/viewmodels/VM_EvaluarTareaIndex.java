package ve.smile.gestion.evento.ejecucion.evaluarTarea.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventPlanTareaTrabajador;
import ve.smile.dto.EventPlanTareaVoluntario;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Motivo;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
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

public class VM_EvaluarTareaIndex extends VM_WindowWizard{

	private List<EventPlanTarea> eventoPlanificadotareas = new ArrayList<>();
	private int indexTarea;
	private Motivo motivo = new Motivo();
	
	@Command("buscarMotivo")
	public void buscarMotivo(@BindingParam("index") int index) {
		this.indexTarea = index;
		CatalogueDialogData<Motivo> catalogueDialogData = new CatalogueDialogData<Motivo>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Motivo>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Motivo> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						motivo = catalogueDialogCloseEvent.getEntity();
						refreshMotivo();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/ejecucion/evaluarTareas/catalogoMotivo.zul",
						catalogueDialogData);
	}
	
	public void refreshMotivo() {
		this.getEventoPlanificadotareas().get(indexTarea)
				.setFkMotivo(motivo);;
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
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

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard3.add(operacionWizardCustom);
		botones.put(3, listOperacionWizard3);

		return botones;
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

		urls.add("views/desktop/gestion/evento/ejecucion/evaluarTareas/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/evaluarTareas/evaluarTareaEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/evaluarTareas/registroCompletado.zul");
		return urls;
	}
	
	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		
		if(currentStep == 1){			
			Map<String, String> criterios = new HashMap<>();
			EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
			criterios.put("fkEventoPlanificado.idEventoPlanificado", eventoPlanificado.getIdEventoPlanificado()+"");
			PayloadEventPlanTareaResponse eventPlanTareaResponse = S.EventPlanTareaService.consultarCriterios(TypeQuery.EQUAL, criterios);
			
			if(eventPlanTareaResponse.getObjetos() != null & eventPlanTareaResponse.getObjetos().size() > 0){				
				for(EventPlanTarea eventPlanTarea: eventPlanTareaResponse.getObjetos()){
					EventPlanTarea planTarea = new EventPlanTarea();
					planTarea.setFechaEjecutada(eventPlanTarea.getFechaPlanificada());
					planTarea.setFechaPlanificada(eventPlanTarea.getFechaPlanificada());
					planTarea.setFkDirectorio(eventPlanTarea.getFkDirectorio());
					planTarea.setFkEventoPlanificado(eventPlanTarea.getFkEventoPlanificado());
					planTarea.setFkTarea(eventPlanTarea.getFkTarea());
					planTarea.setIdEventPlanTarea(eventPlanTarea.getIdEventPlanTarea());
					
					//eventPlanTarea.setFechaEjecutada(eventPlanTarea.getFechaEjecutada());
					this.getEventoPlanificadotareas().add(planTarea); 
					
				}								
			}
			BindUtils.postNotifyChange(null, null, this, "eventoPlanificadotareas");
		}

		return "";
	}
	
	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}
	
	@Override
	public String executeFinalizar(Integer currentStep) {
		
		for(EventPlanTarea tarea: this.eventoPlanificadotareas){
			EventPlanTarea eventPlanTarea = S.EventPlanTareaService.consultarUno(tarea.getIdEventPlanTarea()).getObjetos().get(0);
			eventPlanTarea.setEjecucion(tarea.isEjecucion());
			eventPlanTarea.setFechaEjecutada(tarea.getFechaEjecutadaDate().getTime());
			PayloadEventPlanTareaResponse eventPlanTareaResponse = S.EventPlanTareaService.modificar(eventPlanTarea);
			if (!UtilPayload.isOK(eventPlanTareaResponse)) {
				 return (String) eventPlanTareaResponse.getInformacion(IPayloadResponse.MENSAJE);
			 }
		}
		goToNextStep();
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
	
	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";

	}

	public List<EventPlanTarea> getEventoPlanificadotareas() {
		return eventoPlanificadotareas;
	}

	public void setEventoPlanificadotareas(
			List<EventPlanTarea> eventoPlanificadotareas) {
		this.eventoPlanificadotareas = eventoPlanificadotareas;
	}
	
	

}
