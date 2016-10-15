package ve.smile.gestion.evento.ejecucion.registroResultado.viewmodels;

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
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorEventoPlanTarea;
import ve.smile.dto.IndicadorTsPlanActividad;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;

public class VM_RegistroResultadoEventoPlanificado extends
		VM_WindowWizard {

	private List<Indicador> auxlistIndicadors = new ArrayList<>();
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadIndicadorEventoPlanTareaResponse indicadorEventoPlanTareaResponse = null;
			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = new PayloadEventPlanTareaResponse();
			for (EventPlanTarea obj : listEventPlanTareas) {
				/*Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkEventPlanTarea.idEventPlanTarea",
						obj.getIdEventPlanTarea() + "");

				indicadorEventoPlanTareaResponse = S.IndicadorEventoPlanTareaService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (indicadorEventoPlanTareaResponse.getObjetos().size() > 0) {
					for (IndicadorEventoPlanTarea indicEvenTarea : indicadorEventoPlanTareaResponse
							.getObjetos()) {
						S.IndicadorEventoPlanTareaService
								.eliminar(indicEvenTarea
										.getIdIndicadorEventoPlanTarea());
					}

				}*/
				if (obj.getIndicadorEventoPlanTareas() != null) {
					for (IndicadorEventoPlanTarea indicadorEventoPlanTarea : obj
							.getIndicadorEventoPlanTareas()) {
						IndicadorEventoPlanTarea eventoPlanTarea = new IndicadorEventoPlanTarea();
						EventPlanTarea eventPlanTarea = new EventPlanTarea();
						eventPlanTarea
								.setIdEventPlanTarea(indicadorEventoPlanTarea
										.getFkEventPlanTarea()
										.getIdEventPlanTarea());
						eventoPlanTarea.setFkEventPlanTarea(eventPlanTarea);
						eventoPlanTarea.setFkIndicador(indicadorEventoPlanTarea
								.getFkIndicador());
						eventoPlanTarea
								.setValorEsperado(indicadorEventoPlanTarea
										.getValorEsperado());
						indicadorEventoPlanTareaResponse = S.IndicadorEventoPlanTareaService
								.incluir(eventoPlanTarea);
					}
				}
			}
			goToNextStep();
			return (String) payloadEventPlanTareaResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}

		return "";
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
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
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					((EventoPlanificado) selectedObject).getIdEventoPlanificado() + "");
			this.listEventPlanTareas = new ArrayList<>();
			this.listEventPlanTareas = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro)
					.getObjetos();
			if (this.listEventPlanTareas.size() > 0) {
				for (int i = 0; i < this.listEventPlanTareas.size(); i++) {
					Map<String, String> criterios = new HashMap<String, String>();
					criterios.put("fkEventPlanTarea.idEventPlanTarea",
							listEventPlanTareas.get(i).getIdEventPlanTarea()
									+ "");
					PayloadIndicadorEventoPlanTareaResponse indicadorEventoPlanTareaResponse = S.IndicadorEventoPlanTareaService
							.consultarCriterios(TypeQuery.EQUAL, criterios);
					if (indicadorEventoPlanTareaResponse.getObjetos().size() > 0) {
						
						listEventPlanTareas.get(i).setIndicadorEventoPlanTareas(indicadorEventoPlanTareaResponse
												.getObjetos());
					}

				}
			}

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

}
