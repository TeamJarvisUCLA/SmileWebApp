package ve.smile.gestion.evento.planificaion.tarea.participantes.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorEventoPlanTarea;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;

public class VM_TareaParticipanteEventoPlanificado extends VM_WindowWizard{

	private List<EventPlanTarea> listEventPlanTareas;
	private int indexTarea;
	
	@Init(superclass = true)
	public void childInit() {
		
	}
	
	@Command("buscarIndicadores")
	public void buscarVoluntario(@BindingParam("index") int index) {
		this.indexTarea = index;
		CatalogueDialogData<Persona> catalogueDialogData = new CatalogueDialogData<Persona>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Persona>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Persona> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Persona> listPersona = new ArrayList<Persona>();
						listPersona = catalogueDialogCloseEvent.getEntities();

						refreshPersona(listPersona);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/participantes/catalogoParticipantes.zul",
						catalogueDialogData);
	}

	public void refreshPersona(List<Persona> listPersona) {

		boolean validar = true;
		List<Persona> listAux2 = new ArrayList<>();
		Persona persona = new Persona();

		if (listEventPlanTareas.get(indexTarea).getListPersonas() != null) {
			for (Persona iet : listEventPlanTareas.get(
					indexTarea).getListPersonas()) {
//				persona = new Persona();
//				persona
//						.setFkEventPlanTarea(listEventPlanTareas
//								.get(indexTarea));
//				persona.setFkIndicador(iet.getFkIndicador());
//				persona.setValorEsperado(iet.getValorEsperado());
				listAux2.add(iet);
			}
		}

		for (Persona persona2 : listPersona) {
			if (listEventPlanTareas.get(indexTarea)
					.getIndicadorEventoPlanTareas() != null) {

				for (Persona iet : listEventPlanTareas.get(
						indexTarea).getListPersonas()) {
					if (iet.getIdPersona()
							.equals(persona2.getIdPersona())) {

						validar = false;

					}
				}

				if (validar) {
					//indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
					//indicadorEventoPlanTarea
							//.setFkEventPlanTarea(listEventPlanTareas
							//		.get(indexTarea));
					//indicadorEventoPlanTarea.setFkIndicador(indicador);
					listAux2.add(persona2);

				}

			} else {
//				indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
//				indicadorEventoPlanTarea
//						.setFkEventPlanTarea(listEventPlanTareas
//								.get(indexTarea));
//				indicadorEventoPlanTarea.setFkIndicador(indicador);
				listAux2.add(persona2);

			}

		}

		listEventPlanTareas.get(indexTarea).setListPersonas(
				listAux2);
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

		return botones;
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

		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/tareaParticipantes.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/registroCompletado.zul");

		return urls;
	}
	
	@Override
	public String executeSiguiente(Integer currentStep) {
		if(currentStep == 1){
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado", ((EventoPlanificado) selectedObject).getIdEventoPlanificado()+"");
			this.listEventPlanTareas = S.EventPlanTareaService.consultarCriterios(TypeQuery.EQUAL, parametro).getObjetos();
		}
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
		}

		if (currentStep == 2) {
			return "E:Error Code 5-No hay otro paso";
		}

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
	
	

}
