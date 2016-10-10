package ve.smile.gestion.evento.planificaion.tarea.indicadores.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
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
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;

public class VM_TareaIndicadoresEventoPlanificado extends
		VM_WindowWizard<EventoPlanificado> {

	// private List<Indicador> listIndicadors = new ArrayList<>();
	private List<Indicador> auxlistIndicadors = new ArrayList<>();
	private List<EventPlanTarea> listEventPlanTareas;
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
		// for (Indicador obj : listIndicadors) {
		// auxlistIndicadors.add(obj);
		// }

		List<Indicador> listaux = new ArrayList<Indicador>();

		for (Iterator iterator2 = listIndicado.iterator(); iterator2.hasNext();) {
			Indicador indicador = (Indicador) iterator2.next();
			indicador.setFkUnidadMedida(indicador.getFkUnidadMedida());
			indicador.setDescripcion(indicador.getDescripcion());
			indicador.setNombre(indicador.getNombre());
			indicador.setIdIndicador(indicador.getIdIndicador());
			indicador.setValorEsperado(new Double(0));
			listaux.add(indicador);
			iterator2.remove();

		}

		listEventPlanTareas.get(indexTarea).setListIndicadors(
				new ArrayList<Indicador>(listaux));

		// BindUtils.postNotifyChange(null, null, this, "listIndicadors");
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
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadIndicadorEventoPlanTareaResponse indicadorEventoPlanTareaResponse = null;
			for (EventPlanTarea obj : listEventPlanTareas) {
				IndicadorEventoPlanTarea indicadorEventoPlanTarea = new IndicadorEventoPlanTarea();
				Map<String, String> criterios = new HashMap<String, String>();
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

				}
				for (Indicador indicador : obj.getListIndicadors()) {
					indicadorEventoPlanTarea.setFkEventPlanTarea(obj);
					indicadorEventoPlanTarea.setFkIndicador(indicador);
					indicadorEventoPlanTarea.setValorEsperado(indicador
							.getValorEsperado());
					indicadorEventoPlanTareaResponse = S.IndicadorEventoPlanTareaService
							.modificar(indicadorEventoPlanTarea);
				}

			}
			return (String) indicadorEventoPlanTareaResponse
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
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/tareas/indicadores/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/indicadores/tareaIndicadores.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroTrabajoSocialPlanificado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					selectedObject.getIdEventoPlanificado() + "");
			this.listEventPlanTareas = new ArrayList<>();
			this.listEventPlanTareas = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro)
					.getObjetos();
			for (int i = 0; i < listEventPlanTareas.size(); i++) {
				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkEventPlanTarea.idEventPlanTarea",
						listEventPlanTareas.get(i).getIdEventPlanTarea() + "");
				PayloadIndicadorEventoPlanTareaResponse indicadorEventoPlanTareaResponse = S.IndicadorEventoPlanTareaService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (indicadorEventoPlanTareaResponse.getObjetos().size() > 0) {
					List<Indicador> listIndicadors = new ArrayList<>();
					for (int j = 0; j < indicadorEventoPlanTareaResponse
							.getObjetos().size(); j++) {
						listIndicadors.add(indicadorEventoPlanTareaResponse
								.getObjetos().get(j).getFkIndicador());
						listIndicadors.get(j).setValorEsperado(
								indicadorEventoPlanTareaResponse.getObjetos()
										.get(j).getValorEsperado());
					}
					listEventPlanTareas.get(i)
							.setListIndicadors(listIndicadors);
				}

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

	// public List<Indicador> getListIndicadors() {
	// return listIndicadors;
	// }
	//
	// public void setListIndicadors(List<Indicador> listIndicadors) {
	// this.listIndicadors = listIndicadors;
	// }

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
