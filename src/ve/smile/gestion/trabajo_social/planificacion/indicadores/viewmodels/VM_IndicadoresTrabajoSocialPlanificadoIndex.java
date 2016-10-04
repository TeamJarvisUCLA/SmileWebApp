package ve.smile.gestion.trabajo_social.planificacion.indicadores.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.TsPlan;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_IndicadoresTrabajoSocialPlanificadoIndex extends
		VM_WindowWizard<TsPlan> {

	private List<Indicador> indicadores;
	private Set<Indicador> indicadoresSeleccionados;
	private List<Indicador> trabajoSocialIndicadores;
	private Set<Indicador> trabajoSocialIndicadoresSeleccionados;

	private List<IndicadorTsPlan> indicadorTsPlans;

	private List<Indicador> trabajoSocialIndicadoresAux;

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

		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
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
		if (currentStep == 2) {
			if (this.getTrabajoSocialIndicadores().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al trabajo social planificado.";
			} else {
				this.getIndicadorTsPlans().clear();
				for (Indicador indicador : this.getTrabajoSocialIndicadores()) {
					IndicadorTsPlan indicadorTsPlan = new IndicadorTsPlan();
					indicadorTsPlan.setFkIndicador(indicador);
					this.getIndicadorTsPlans().add(indicadorTsPlan);
				}
				BindUtils
						.postNotifyChange(null, null, this, "indicadorTsPlans");
			}

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 2) {
			try {
				// UtilValidate.validateDate(this.getFechaPlanificada().getTime(),
				// "Fecha Planificada", ValidateOperator.GREATER_THAN,
				// new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				// "dd/MM/yyyy");
				// UtilValidate.validateNull(this.getVoluntario()
				// .getIdVoluntario(), "Responsable");
				// UtilValidate.validateNull(this.getDirectorio()
				// .getIdDirectorio(), "Directorio");
				// UtilValidate.validateNull(this.getTsPlan().getFkMultimedia(),
				// "Imagen");
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			// this.getTsPlan().setFechaPlanificada(
			// this.getFechaPlanificada().getTime());
			// this.getTsPlan().setFkDirectorio(this.getDirectorio());
			// this.getTsPlan().setFkPersona(this.getVoluntario().getFkPersona());
			// this.getTsPlan().setPublicoPortal(true);
			//
			// Multimedia multimedia = this.getTsPlan().getFkMultimedia();
			//
			// PayloadMultimediaResponse payloadMultimediaResponse =
			// S.MultimediaService
			// .incluir(multimedia);
			// if (!UtilPayload.isOK(payloadMultimediaResponse)) {
			// return (String) payloadMultimediaResponse
			// .getInformacion(IPayloadResponse.MENSAJE);
			// }
			// multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
			// .getInformacion("id")).intValue());
			// this.getTsPlan().setFkMultimedia(multimedia);
			// PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
			// .incluir(this.tsPlan);
			// if (UtilPayload.isOK(payloadTsPlanResponse)) {
			// restartWizard();
			// this.setTsPlan(new TsPlan());
			// this.setDirectorio(new Directorio());
			// this.setFechaPlanificada(new Date());
			// this.setSelectedObject(new TsPlan());
			// this.setVoluntario(new Voluntario());
			// BindUtils.postNotifyChange(null, null, this, "directorio");
			// BindUtils.postNotifyChange(null, null, this, "tsPlan");
			// BindUtils
			// .postNotifyChange(null, null, this, "fechaPlanificada");
			// BindUtils.postNotifyChange(null, null, this, "selectedObject");
			// BindUtils.postNotifyChange(null, null, this, "voluntario");
			// }
			// return (String) payloadTsPlanResponse
			// .getInformacion(IPayloadResponse.MENSAJE);
			goToNextStep();
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

}
