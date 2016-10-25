package ve.smile.gestion.trabajadores.fortalezas.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.crux.alert.Alert;
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
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;

public class VM_FortalezaTrabajadorIndex extends VM_WindowWizard {

	private List<Fortaleza> fortalezas;
	private Set<Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> trabajadorFortalezas;
	private Set<Fortaleza> trabajadorFortalezasSeleccionadas;

	@Init(superclass = true)
	public void childInit() {

	}

	// M�TODOS DE LAS LISTAS
	public boolean disabledFortaleza(Fortaleza fortaleza) {
		return this.getTrabajadorFortalezas().contains(fortaleza);
	}

	public List<Fortaleza> getFortalezas() {
		if (this.fortalezas == null) {
			this.fortalezas = new ArrayList<>();
		}
		if (this.fortalezas.isEmpty()) {
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
			} else {
				fortalezas.addAll(payloadFortalezaResponse.getObjetos());
			}
		}

		return fortalezas;
	}

	public void setFortalezas(List<Fortaleza> fortalezas) {
		this.fortalezas = fortalezas;
	}

	public Set<Fortaleza> getFortalezasSeleccionadas() {
		if (this.fortalezasSeleccionadas == null) {
			this.fortalezasSeleccionadas = new HashSet<>();
		}
		return fortalezasSeleccionadas;
	}

	public void setFortalezasSeleccionadas(
			Set<Fortaleza> fortalezasSeleccionadas) {
		this.fortalezasSeleccionadas = fortalezasSeleccionadas;
	}

	public List<Fortaleza> getTrabajadorFortalezas() {
		if (this.trabajadorFortalezas == null) {
			trabajadorFortalezas = new ArrayList<>();
		}
		return trabajadorFortalezas;
	}

	public void setTrabajadorFortalezas(List<Fortaleza> trabajadorFortalezas) {
		this.trabajadorFortalezas = trabajadorFortalezas;
	}

	public Set<Fortaleza> getTrabajadorFortalezasSeleccionadas() {
		if (this.trabajadorFortalezasSeleccionadas == null) {
			this.trabajadorFortalezasSeleccionadas = new HashSet<>();
		}
		return trabajadorFortalezasSeleccionadas;
	}

	public void setTrabajadorFortalezasSeleccionadas(
			Set<Fortaleza> trabajadorFortalezasSeleccionadas) {
		this.trabajadorFortalezasSeleccionadas = trabajadorFortalezasSeleccionadas;
	}

	@Command("agregarFortalezas")
	@NotifyChange({ "fortalezas", "trabajadorFortalezas",
			"fortalezasSeleccionadas", "trabajadorFortalezasSeleccionadas" })
	public void agregarFortalezas() {
		if (this.getFortalezasSeleccionadas() != null
				&& this.getFortalezasSeleccionadas().size() > 0) {
			this.getTrabajadorFortalezas().addAll(fortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getTrabajadorFortalezasSeleccionadas().clear();
		}
	}

	@Command("removerFortalezas")
	@NotifyChange({ "fortalezas", "trabajadorFortalezas",
			"fortalezasSeleccionadas", "trabajadorFortalezasSeleccionadas" })
	public void removerFortalezas() {
		if (this.getTrabajadorFortalezasSeleccionadas() != null
				&& this.getTrabajadorFortalezasSeleccionadas().size() > 0) {
			this.getTrabajadorFortalezas().removeAll(
					trabajadorFortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getTrabajadorFortalezasSeleccionadas().clear();
		}
	}

	// MÉTODOS DEL WIZARD
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
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(operacionWizardCustom);
		botones.put(3, listOperacionWizard3);
		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/trabajadores/fortalezas/selectTrabajador.zul");
		urls.add("views/desktop/gestion/trabajadores/fortalezas/listaFortalezas.zul");
		urls.add("views/desktop/gestion/trabajadores/fortalezas/registroCompletado.zul");
		return urls;
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.setTrabajadorFortalezas(null);
			this.setFortalezasSeleccionadas(null);
			this.setTrabajadorFortalezasSeleccionadas(null);
			this.setFortalezas(null);
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarPorTrabajador(getTrabajadorSelected()
							.getIdTrabajador());

			if (!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
			}

			getTrabajadorSelected().setFortalezas(
					payloadFortalezaResponse.getObjetos());

			this.getTrabajadorFortalezas().addAll(
					getTrabajadorSelected().getFortalezas());
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		goToNextStep();
		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		this.getTrabajadorFortalezas().clear();
		goToPreviousStep();
		return "";
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador",
				String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 3) {
			restartWizard();
		}
		return "";
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>trabajador</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			try {
				// NOTHING
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getTrabajadorSelected().setFortalezas(
					new ArrayList<Fortaleza>());
			this.getTrabajadorSelected().getFortalezas().clear();
			this.getTrabajadorSelected().getFortalezas()
					.addAll(this.getTrabajadorFortalezas());
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.modificar(getTrabajadorSelected());
			if (UtilPayload.isOK(payloadTrabajadorResponse)) {
				goToNextStep();
				this.setSelectedObject(new Trabajador());
				this.getTrabajadorFortalezas().clear();
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "trabajador");
			}
		}
		return "";
	}

	public Trabajador getTrabajadorSelected() {
		return (Trabajador) selectedObject;
	}

}
