package ve.smile.gestion.trabajo_social.planificacion.actividades.indicadores.viewmodels;

import java.util.ArrayList;
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
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorTsPlanActividad;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_IndicadoresActividadesTrabajoSocialPlanificado extends
		VM_WindowWizard {

	private List<TsPlanActividad> listTsPlanActividads;
	private int indexActividad;

	public List<TsPlanActividad> getListTsPlanActividads() {
		if (this.listTsPlanActividads == null) {
			listTsPlanActividads = new ArrayList<>();
		}
		return listTsPlanActividads;
	}

	public void setListTsPlanActividads(
			List<TsPlanActividad> listTsPlanActividads) {
		this.listTsPlanActividads = listTsPlanActividads;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarIndicadores")
	public void buscarIndicadores(@BindingParam("index") int index) {
		this.setIndexActividad(index);
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
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/indicadores/catalogoIndicadores.zul",
						catalogueDialogData);
	}

	public void refreshIndicador(List<Indicador> listIndicado) {
		List<IndicadorTsPlanActividad> listTsPlanActividads = new ArrayList<>();

		for (Indicador indicador : listIndicado) {

			IndicadorTsPlanActividad indicadorTsPlanActividad = new IndicadorTsPlanActividad();
			indicadorTsPlanActividad.setFkTsPlanActividad(this
					.getListTsPlanActividads().get(indexActividad));
			indicadorTsPlanActividad.setFkIndicador(indicador);
			listTsPlanActividads.add(indicadorTsPlanActividad);

		}

		this.listTsPlanActividads.get(indexActividad)
				.setIndicadorTsPlanActividads(listTsPlanActividads);
		this.listTsPlanActividads.get(indexActividad).setListIndicadors(
				new ArrayList<Indicador>(listIndicado));

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarIndicador")
	public void eliminarIndicador(
			@BindingParam("indicadorTsPlanActividad") IndicadorTsPlanActividad indicadorTsPlanActividad,
			@BindingParam("index") int index) {
		this.getListTsPlanActividads().get(index)
				.getIndicadorTsPlanActividads()
				.remove(indicadorTsPlanActividad);
		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
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
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTsPlanResponse;
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

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/indicadores/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/indicadores/actividadesIndicadores.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/indicadores/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				this.getListTsPlanActividads().addAll(
						payloadTsPlanActividadResponse.getObjetos());
			}

			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkTsPlanActividad.idTsPlanActividad",
						String.valueOf(tsPlanActividad.getIdTsPlanActividad()));
				PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
					List<Indicador> listIndicadors = new ArrayList<>();

					if (payloadIndicadorTsPlanActividadResponse.getObjetos() != null) {

						for (IndicadorTsPlanActividad indicadorTsPlanActividad : payloadIndicadorTsPlanActividadResponse
								.getObjetos()) {
							listIndicadors.add(indicadorTsPlanActividad
									.getFkIndicador());
						}
					}
					tsPlanActividad.setListIndicadors(listIndicadors);
					tsPlanActividad
							.setIndicadorTsPlanActividads(payloadIndicadorTsPlanActividadResponse
									.getObjetos());
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
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {

		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = new PayloadIndicadorTsPlanActividadResponse();
			for (TsPlanActividad obj : this.getListTsPlanActividads()) {

				for (IndicadorTsPlanActividad indicadorTsPlanActividad : obj
						.getIndicadorTsPlanActividads()) {

					IndicadorTsPlanActividad indicadorTsPlanActividad2 = new IndicadorTsPlanActividad(
							new TsPlanActividad(obj.getIdTsPlanActividad()),
							indicadorTsPlanActividad.getFkIndicador(), null,
							new String(),
							indicadorTsPlanActividad.getValorEsperado(),
							new Double(0));

					payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
							.incluir(indicadorTsPlanActividad2);

					if (!UtilPayload
							.isOK(payloadIndicadorTsPlanActividadResponse)) {
						return (String) payloadIndicadorTsPlanActividadResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}
				goToNextStep();
				return (String) payloadIndicadorTsPlanActividadResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

		}
		return "";
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
