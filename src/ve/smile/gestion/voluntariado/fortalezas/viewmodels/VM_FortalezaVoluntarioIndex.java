package ve.smile.gestion.voluntariado.fortalezas.viewmodels;

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
import ve.smile.dto.Voluntario;
import ve.smile.dto.VoluntarioFortaleza;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadVoluntarioFortalezaResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_FortalezaVoluntarioIndex extends VM_WindowWizard {
	private List<Fortaleza> fortalezas;
	private Set<Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> voluntarioFortalezas;
	private Set<Fortaleza> voluntarioFortalezasSeleccionadas;

	@Init(superclass = true)
	public void childInit() {
		// FORTALEZAS
		if (this.getFortalezas().isEmpty()) {
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
			} else {
				fortalezas.addAll(payloadFortalezaResponse.getObjetos());
			}
		}
	}

	// M�TODOS DE LAS LISTAS
	public boolean disabledFortaleza(Fortaleza fortaleza) {
		return this.getVoluntarioFortalezas().contains(fortaleza);
	}

	public List<Fortaleza> getFortalezas() {
		if (this.fortalezas == null) {
			this.fortalezas = new ArrayList<>();
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

	public List<Fortaleza> getVoluntarioFortalezas() {
		if (this.voluntarioFortalezas == null) {
			this.voluntarioFortalezas = new ArrayList<>();
		}
		return voluntarioFortalezas;
	}

	public void setVoluntarioFortalezas(List<Fortaleza> voluntarioFortalezas) {
		this.voluntarioFortalezas = voluntarioFortalezas;
	}

	public Set<Fortaleza> getVoluntarioFortalezasSeleccionadas() {
		if (this.voluntarioFortalezasSeleccionadas == null) {
			this.voluntarioFortalezasSeleccionadas = new HashSet<>();
		}
		return voluntarioFortalezasSeleccionadas;
	}

	public void setVoluntarioFortalezasSeleccionadas(
			Set<Fortaleza> voluntarioFortalezasSeleccionadas) {
		this.voluntarioFortalezasSeleccionadas = voluntarioFortalezasSeleccionadas;
	}

	@Command("agregarFortalezas")
	@NotifyChange({ "fortalezas", "voluntarioFortalezas",
			"fortalezasSeleccionadas", "voluntarioFortalezasSeleccionadas" })
	public void agregarFortalezas() {
		if (this.getFortalezasSeleccionadas() != null
				&& this.getFortalezasSeleccionadas().size() > 0) {
			this.getVoluntarioFortalezas().addAll(fortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getVoluntarioFortalezasSeleccionadas().clear();
		}
	}

	@Command("removerFortalezas")
	@NotifyChange({ "fortalezas", "voluntarioFortalezas",
			"fortalezasSeleccionadas", "voluntarioFortalezasSeleccionadas" })
	public void removerFortalezas() {
		if (this.getVoluntarioFortalezasSeleccionadas() != null
				&& this.getVoluntarioFortalezasSeleccionadas().size() > 0) {
			this.getVoluntarioFortalezas().removeAll(
					voluntarioFortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getVoluntarioFortalezasSeleccionadas().clear();
		}
	}

	// METODOS DEL WIZARD
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
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/fortalezas/selectVoluntario.zul");
		urls.add("views/desktop/gestion/voluntariado/fortalezas/listaFortalezas.zul");
		return urls;
	}

	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<Voluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusVoluntario",
				String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	// ATRAS
	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return "";
	}

	// SIGUIENTE
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>voluntario</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			// BUSCAR FORTALEZAS DEL VOLUNTARIO
			this.setVoluntarioFortalezas(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkVoluntario.idVoluntario", String.valueOf(this
					.getVoluntarioSelected().getIdVoluntario()));
			PayloadVoluntarioFortalezaResponse payloadVoluntarioFortalezaResponse = S.VoluntarioFortalezaService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadVoluntarioFortalezaResponse.getObjetos() != null) {
				for (VoluntarioFortaleza vF : payloadVoluntarioFortalezaResponse
						.getObjetos()) {
					this.getVoluntarioFortalezas().add(vF.getFkFortaleza());
				}
			}
			BindUtils.postNotifyChange(null, null, this, "*");

		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();
		return "";
	}

	// FINALIZAR
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
			this.getVoluntarioSelected().setFortalezas(
					new ArrayList<Fortaleza>());
			this.getVoluntarioSelected().getFortalezas().clear();
			this.getVoluntarioSelected().getFortalezas()
					.addAll(this.getVoluntarioFortalezas());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.modificar(this.getVoluntarioSelected());
			if (UtilPayload.isOK(payloadVoluntarioResponse)) {
				restartWizard();
				this.setSelectedObject(new Voluntario());
				this.getFortalezasSeleccionadas().clear();
				this.setVoluntarioFortalezas(new ArrayList<Fortaleza>());
				this.getVoluntarioFortalezasSeleccionadas().clear();
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "fortalezas");
				BindUtils.postNotifyChange(null, null, this,
						"fortalezasSeleccionadas");
				BindUtils.postNotifyChange(null, null, this,
						"voluntarioFortalezas");
				BindUtils.postNotifyChange(null, null, this,
						"voluntarioFortalezasSeleccionadas");
				return (String) payloadVoluntarioResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
		}
		return "";
	}

	// VOLUNTARIO SELECTED
	public Voluntario getVoluntarioSelected() {
		return (Voluntario) this.selectedObject;
	}
}
