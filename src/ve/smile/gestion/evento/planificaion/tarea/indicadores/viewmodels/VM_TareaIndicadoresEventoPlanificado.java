package ve.smile.gestion.evento.planificaion.tarea.indicadores.viewmodels;

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
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;

public class VM_TareaIndicadoresEventoPlanificado extends
		VM_WindowWizard {

	private List<Indicador> auxlistIndicadors = new ArrayList<>();
	private List<EventPlanTarea> listEventPlanTareas = new ArrayList<>();
	private int indexTarea;

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarIndicadores")
	public void buscarVoluntario(@BindingParam("index") int index) {
		this.indexTarea = index;
		CatalogueDialogData<Indicador> catalogueDialogData = new CatalogueDialogData<Indicador>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Indicador>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Indicador> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Indicador> listIndicador = new ArrayList<Indicador>();
						listIndicador = catalogueDialogCloseEvent.getEntities();

						refreshIndicador(listIndicador);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/tareas/indicadores/catalogoIndicadores.zul",
						catalogueDialogData);
	}

	public void refreshIndicador(List<Indicador> listIndicado) {

		boolean validar = true;
		List<IndicadorEventoPlanTarea> listAux2 = new ArrayList<>();
		IndicadorEventoPlanTarea indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();

		if (listEventPlanTareas.get(indexTarea).getIndicadorEventoPlanTareas() != null) {
			for (IndicadorEventoPlanTarea iet : listEventPlanTareas.get(
					indexTarea).getIndicadorEventoPlanTareas()) {
				indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
				indicadorEventoPlanTarea
						.setFkEventPlanTarea(listEventPlanTareas
								.get(indexTarea));
				indicadorEventoPlanTarea.setFkIndicador(iet.getFkIndicador());
				indicadorEventoPlanTarea.setValorEsperado(iet.getValorEsperado());
				listAux2.add(indicadorEventoPlanTarea);
			}
		}

		for (Indicador indicador : listIndicado) {
			if (listEventPlanTareas.get(indexTarea)
					.getIndicadorEventoPlanTareas() != null) {

				for (IndicadorEventoPlanTarea iet : listEventPlanTareas.get(
						indexTarea).getIndicadorEventoPlanTareas()) {
					if (iet.getFkIndicador().getIdIndicador()
							.equals(indicador.getIdIndicador())) {

						validar = false;

					}
				}

				if (validar) {
					indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
					indicadorEventoPlanTarea
							.setFkEventPlanTarea(listEventPlanTareas
									.get(indexTarea));
					indicadorEventoPlanTarea.setFkIndicador(indicador);
					listAux2.add(indicadorEventoPlanTarea);

				}

			} else {
				indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
				indicadorEventoPlanTarea
						.setFkEventPlanTarea(listEventPlanTareas
								.get(indexTarea));
				indicadorEventoPlanTarea.setFkIndicador(indicador);
				listAux2.add(indicadorEventoPlanTarea);

			}

		}

		listEventPlanTareas.get(indexTarea).setIndicadorEventoPlanTareas(
				listAux2);
		BindUtils.postNotifyChange(null, null, this, "listEventPlanTareas");
	}

	@Command("eliminarIndicador")
	public void eliminarIndicador(
			@BindingParam("indicadorEventoPs") IndicadorEventoPlanTarea indicadorEventoPlanTarea,
			@BindingParam("index") int index) {
		this.getListEventPlanTareas().get(index)
				.getIndicadorEventoPlanTareas()
				.remove(indicadorEventoPlanTarea);
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

		urls.add("views/desktop/gestion/evento/planificacion/tareas/indicadores/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/indicadores/tareaIndicadores.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/indicadores/registroCompletado.zul");

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
						listEventPlanTareas.get(i)
								.setIndicadorEventoPlanTareas(
										indicadorEventoPlanTareaResponse
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
