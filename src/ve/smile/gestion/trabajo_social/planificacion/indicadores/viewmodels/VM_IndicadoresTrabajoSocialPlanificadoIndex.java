package ve.smile.gestion.trabajo_social.planificacion.indicadores.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.PlantillaTrabajoSocialIndicador;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_IndicadoresTrabajoSocialPlanificadoIndex extends
		VM_WindowWizard {

	private List<Indicador> indicadores;
	private Set<Indicador> indicadoresSeleccionados;
	private List<Indicador> trabajoSocialIndicadores;
	private Set<Indicador> trabajoSocialIndicadoresSeleccionados;

	private List<IndicadorTsPlan> indicadorTsPlans;
	private List<IndicadorTsPlan> indicadorTsPlansDelete;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Command("agregarIndicadoresPlantilla")
	@NotifyChange({ "indicadores", "trabajoSocialIndicadores",
			"indicadoresSeleccionados", "trabajoSocialIndicadoresSeleccionados" })
	public void agregarIndicadoresPlantilla() {
		if (this.getIndicadoresSeleccionados() != null
				&& this.getIndicadoresSeleccionados().size() > 0) {
			this.getTrabajoSocialIndicadores().addAll(indicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getTrabajoSocialIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "indicadores", "trabajoSocialIndicadores",
			"indicadoresSeleccionados", "trabajoSocialIndicadoresSeleccionados" })
	public void removerIndicadoresPlantilla() {
		if (this.getTrabajoSocialIndicadoresSeleccionados() != null
				&& this.getTrabajoSocialIndicadoresSeleccionados().size() > 0) {
			this.getTrabajoSocialIndicadores().removeAll(
					trabajoSocialIndicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getTrabajoSocialIndicadoresSeleccionados().clear();
		}
	}

	public boolean disabledIndicador(Indicador indicador) {
		return this.getTrabajoSocialIndicadores().contains(indicador);
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

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/indicadores/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/indicadores/selectIndicadoresTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/indicadores/editIndicadoresTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/indicadores/registroCompletado.zul");
		return urls;
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
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTsPlan", String
				.valueOf(EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO
						.ordinal()));

		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTsPlanResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
		}

		if (currentStep == 2) {
			if (this.getTrabajoSocialIndicadores().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al trabajo social planificado.";
			}

		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.indicadorTsPlans = new ArrayList<>();
			Map<String, String> criterios = new HashMap<>();

			criterios.put("fkTsPlan.idTsPlan", String.valueOf(this
					.getTsPlanificadoSelected().getIdTsPlan()));
			PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			setIndicadorTsPlans(null);
			setTrabajoSocialIndicadores(null);
			if (payloadIndicadorTsPlanResponse.getObjetos() != null) {

				for (IndicadorTsPlan indicadorEventoPlanificado : payloadIndicadorTsPlanResponse
						.getObjetos()) {
					this.getIndicadorTsPlans().add(indicadorEventoPlanificado);
					this.getTrabajoSocialIndicadores().add(
							indicadorEventoPlanificado.getFkIndicador());
				}
			}

			BindUtils.postNotifyChange(null, null, this,
					"trabajoSocialIndicadores");
		}

		if (currentStep == 2) {
			if (this.getTrabajoSocialIndicadores().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al trabajo social planificado.";
			} else {
				boolean validar = true;
				List<IndicadorTsPlan> indicadorTsPlans2 = new ArrayList<>();
				indicadorTsPlans2.addAll(new ArrayList<>(this
						.getIndicadorTsPlans()));
				for (Indicador indicador : this.getTrabajoSocialIndicadores()) {
					for (IndicadorTsPlan indicadorTsPlan : this
							.getIndicadorTsPlans()) {
						if (indicadorTsPlan.getFkIndicador().getIdIndicador()
								.equals(indicador.getIdIndicador())) {
							validar = false;
							indicadorTsPlans2.remove(indicadorTsPlan);
							break;
						}
					}
					if (validar) {
						IndicadorTsPlan indicadorTsPlan = new IndicadorTsPlan();
						indicadorTsPlan.setFkIndicador(indicador);
						indicadorTsPlan.setFkTsPlan((TsPlan) this
								.getSelectedObject());
						this.getIndicadorTsPlans().add(indicadorTsPlan);
					}
					validar = true;
				}

				this.getIndicadorTsPlansDelete().addAll(indicadorTsPlans2);
				this.getIndicadorTsPlans().removeAll(indicadorTsPlans2);

				BindUtils.postNotifyChange(null, null, this, "*");
			}

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			String indicadores = new String();
			StringBuilder stringBuilder = new StringBuilder();
			for (IndicadorTsPlan indicadorTsPlan : this.getIndicadorTsPlans()) {
				if (indicadorTsPlan.getValorEsperado() == null
						|| indicadorTsPlan.getValorEsperado() == 0) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(indicadorTsPlan.getFkIndicador()
							.getNombre());
				}
			}
			indicadores = stringBuilder.toString();
			if (!indicadores.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar el valor esperado de los siguientes indicadores: <b>"
						+ indicadores + "</b>";
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			for (IndicadorTsPlan indicadorTsPlan : this.getIndicadorTsPlans()) {
				PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse;
				if (indicadorTsPlan.getIdIndicadorTsPlan() == null) {
					payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
							.incluir(indicadorTsPlan);
				} else {
					payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
							.modificar(indicadorTsPlan);
				}

				if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
					return (String) payloadIndicadorTsPlanResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}
			for (IndicadorTsPlan indicadorTsPlan : this
					.getIndicadorTsPlansDelete()) {
				if (indicadorTsPlan.getIdIndicadorTsPlan() != null) {
					PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse;
					payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
							.eliminar(indicadorTsPlan.getIdIndicadorTsPlan());
					if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
						return (String) payloadIndicadorTsPlanResponse
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
		executeCancelar(currentStep);
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {

		this.setIndicadores(null);
		this.setIndicadoresSeleccionados(null);
		this.setTrabajoSocialIndicadores(null);
		this.setTrabajoSocialIndicadoresSeleccionados(null);
		this.setIndicadorTsPlans(null);
		this.setIndicadorTsPlansDelete(null);
		BindUtils.postNotifyChange(null, null, this, "*");

		restartWizard();
		return "";
	}

	@Command("buscarPlantilla")
	public void buscarPlantilla() {
		CatalogueDialogData<PlantillaTrabajoSocialIndicador> catalogueDialogData = new CatalogueDialogData<PlantillaTrabajoSocialIndicador>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<PlantillaTrabajoSocialIndicador>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<PlantillaTrabajoSocialIndicador> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<PlantillaTrabajoSocialIndicador> listIndicadoresPlantillaTrabajoSocial = new ArrayList<>();
						listIndicadoresPlantillaTrabajoSocial = catalogueDialogCloseEvent
								.getEntities();

						refreshIndicadores(listIndicadoresPlantillaTrabajoSocial);
					}
				});

		catalogueDialogData.put("trabajoSocial", this
				.getTsPlanificadoSelected().getFkTrabajoSocial());
		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/indicadores/catalogoPlantillaIndicadoresTrabajoSocial.zul",
						catalogueDialogData);
	}

	public void refreshIndicadores(
			List<PlantillaTrabajoSocialIndicador> listIndicadoresPlantillaTrabajoSocial) {

		for (PlantillaTrabajoSocialIndicador plantillaTrabajoSocialIndicador : listIndicadoresPlantillaTrabajoSocial) {
			if (!this.getTrabajoSocialIndicadores().contains(
					plantillaTrabajoSocialIndicador.getFkIndicador())) {
				this.getTrabajoSocialIndicadores().add(
						plantillaTrabajoSocialIndicador.getFkIndicador());
			}
		}

		BindUtils
				.postNotifyChange(null, null, this, "trabajoSocialIndicadores");
	}

	public List<Indicador> getIndicadores() {
		if (this.indicadores == null) {
			this.indicadores = new ArrayList<>();
		}
		if (this.indicadores.isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			} else {
				indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public Set<Indicador> getIndicadoresSeleccionados() {
		if (this.indicadoresSeleccionados == null) {
			this.indicadoresSeleccionados = new HashSet<>();
		}
		return indicadoresSeleccionados;
	}

	public void setIndicadoresSeleccionados(
			Set<Indicador> indicadoresSeleccionados) {
		this.indicadoresSeleccionados = indicadoresSeleccionados;
	}

	public List<Indicador> getTrabajoSocialIndicadores() {
		if (this.trabajoSocialIndicadores == null) {
			trabajoSocialIndicadores = new ArrayList<>();
		}
		return trabajoSocialIndicadores;
	}

	public void setTrabajoSocialIndicadores(
			List<Indicador> trabajoSocialIndicadores) {
		this.trabajoSocialIndicadores = trabajoSocialIndicadores;
	}

	public Set<Indicador> getTrabajoSocialIndicadoresSeleccionados() {
		if (this.trabajoSocialIndicadoresSeleccionados == null) {
			this.trabajoSocialIndicadoresSeleccionados = new HashSet<>();
		}
		return trabajoSocialIndicadoresSeleccionados;
	}

	public void setTrabajoSocialIndicadoresSeleccionados(
			Set<Indicador> trabajoSocialIndicadoresSeleccionados) {
		this.trabajoSocialIndicadoresSeleccionados = trabajoSocialIndicadoresSeleccionados;
	}

	public List<IndicadorTsPlan> getIndicadorTsPlans() {
		if (this.indicadorTsPlans == null) {
			indicadorTsPlans = new ArrayList<>();
		}
		return indicadorTsPlans;
	}

	public void setIndicadorTsPlans(List<IndicadorTsPlan> indicadorTsPlans) {
		this.indicadorTsPlans = indicadorTsPlans;
	}

	public List<IndicadorTsPlan> getIndicadorTsPlansDelete() {
		if (this.indicadorTsPlansDelete == null) {
			indicadorTsPlansDelete = new ArrayList<>();
		}
		return indicadorTsPlansDelete;
	}

	public void setIndicadorTsPlansDelete(
			List<IndicadorTsPlan> indicadorTsPlansDelete) {
		this.indicadorTsPlansDelete = indicadorTsPlansDelete;
	}

	public TsPlan getTsPlanificadoSelected() {
		return (TsPlan) this.getSelectedObject();
	}
}
