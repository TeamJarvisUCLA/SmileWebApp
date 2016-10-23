package ve.smile.gestion.voluntariado.clasificacion.viewmodels;

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
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Voluntario;
import ve.smile.dto.VoluntarioClasificado;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadClasificadorVoluntarioResponse;
import ve.smile.payload.response.PayloadVoluntarioClasificadoResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_ClasificacionVoluntarioIndex extends VM_WindowWizard {
	private List<ClasificadorVoluntario> clasificaciones;
	private Set<ClasificadorVoluntario> clasificacionesSeleccionadas;
	private List<ClasificadorVoluntario> voluntarioClasificaciones;
	private Set<ClasificadorVoluntario> voluntarioClasificacionesSeleccionadas;

	@Init(superclass = true)
	public void childInit() {
		// CLASIFICACIONES
		if (this.getClasificaciones().isEmpty()) {
			PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadClasificadorVoluntarioResponse)) {
				Alert.showMessage(payloadClasificadorVoluntarioResponse);
			} else {
				clasificaciones.addAll(payloadClasificadorVoluntarioResponse
						.getObjetos());
			}
		}
	}

	// M�TODOS DE LAS LISTAS

	public boolean disabledClasificaciones(
			ClasificadorVoluntario clasificaciones) {
		return this.getVoluntarioClasificaciones().contains(clasificaciones);
	}

	public List<ClasificadorVoluntario> getClasificaciones() {
		if (this.clasificaciones == null) {
			this.clasificaciones = new ArrayList<>();
		}
		return clasificaciones;
	}

	public void setClasificaciones(List<ClasificadorVoluntario> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}

	public Set<ClasificadorVoluntario> getClasificacionesSeleccionadas() {
		if (this.clasificacionesSeleccionadas == null) {
			this.clasificacionesSeleccionadas = new HashSet<>();
		}
		return clasificacionesSeleccionadas;
	}

	public void setClasificacionesSeleccionadas(
			Set<ClasificadorVoluntario> clasificacionesSeleccionadas) {
		this.clasificacionesSeleccionadas = clasificacionesSeleccionadas;
	}

	public List<ClasificadorVoluntario> getVoluntarioClasificaciones() {
		if (this.voluntarioClasificaciones == null) {
			voluntarioClasificaciones = new ArrayList<>();
		}
		return voluntarioClasificaciones;
	}

	public void setVoluntarioClasificaciones(
			List<ClasificadorVoluntario> voluntarioClasificaciones) {
		this.voluntarioClasificaciones = voluntarioClasificaciones;
	}

	public Set<ClasificadorVoluntario> getVoluntarioClasificacionesSeleccionadas() {
		if (this.voluntarioClasificacionesSeleccionadas == null) {
			this.voluntarioClasificacionesSeleccionadas = new HashSet<>();
		}
		return voluntarioClasificacionesSeleccionadas;
	}

	public void setVoluntarioClasificacionesSeleccionadas(
			Set<ClasificadorVoluntario> voluntarioClasificacionesSeleccionadas) {
		this.voluntarioClasificacionesSeleccionadas = voluntarioClasificacionesSeleccionadas;
	}

	@Command("agregarClasificacion")
	@NotifyChange({ "clasificaciones", "voluntarioClasificaciones",
			"clasificacionesSeleccionadas",
			"voluntarioClasificacionesSeleccionadas" })
	public void agregarClasificacion() {
		if (this.getClasificacionesSeleccionadas() != null
				&& this.getClasificacionesSeleccionadas().size() > 0) {
			this.getVoluntarioClasificaciones().addAll(
					clasificacionesSeleccionadas);
			this.getClasificacionesSeleccionadas().clear();
			this.getVoluntarioClasificacionesSeleccionadas().clear();
		}
	}

	@Command("removerClasificacion")
	@NotifyChange({ "clasificaciones", "voluntarioClasificaciones",
			"clasificacionesSeleccionadas",
			"voluntarioClasificacionesSeleccionadas" })
	public void removerClasificacion() {
		if (this.getVoluntarioClasificacionesSeleccionadas() != null
				&& this.getVoluntarioClasificacionesSeleccionadas().size() > 0) {
			this.getVoluntarioClasificaciones().removeAll(
					voluntarioClasificacionesSeleccionadas);
			this.getClasificacionesSeleccionadas().clear();
			this.getVoluntarioClasificacionesSeleccionadas().clear();
		}
	}

	// M�TODOS DEL WIZARD
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
		urls.add("views/desktop/gestion/voluntariado/clasificacion/selectVoluntario.zul");
		urls.add("views/desktop/gestion/voluntariado/clasificacion/listaClasificacion.zul");
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
			if (this.getVoluntarioSelected() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>voluntario</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			// BUSCAR CLASIFICACIONES DEL VOLUNTARIO
			this.setVoluntarioClasificaciones(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkVoluntario.idVoluntario", String.valueOf(this
					.getVoluntarioSelected().getIdVoluntario()));
			PayloadVoluntarioClasificadoResponse payloadVoluntarioClasificadoResponse = S.VoluntarioClasificadoService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadVoluntarioClasificadoResponse.getObjetos() != null) {
				for (VoluntarioClasificado vC : payloadVoluntarioClasificadoResponse
						.getObjetos()) {
					this.getVoluntarioClasificaciones().add(
							vC.getFkClasificadorVoluntario());
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
		if (currentStep == 1) {
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
			this.getVoluntarioSelected().setClasificaciones(
					new ArrayList<ClasificadorVoluntario>());
			this.getVoluntarioSelected().getClasificaciones().clear();
			this.getVoluntarioSelected().getClasificaciones()
					.addAll(this.getVoluntarioClasificaciones());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.modificar(this.getVoluntarioSelected());
			if (UtilPayload.isOK(payloadVoluntarioResponse)) {
				restartWizard();
				this.setSelectedObject(new Voluntario());
				this.getClasificacionesSeleccionadas().clear();
				this.setVoluntarioClasificaciones(new ArrayList<ClasificadorVoluntario>());
				this.getVoluntarioClasificacionesSeleccionadas().clear();
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "clasificaciones");
				BindUtils.postNotifyChange(null, null, this,
						"clasificacionesSeleccionadas");
				BindUtils.postNotifyChange(null, null, this,
						"voluntarioClasificaciones");
				BindUtils.postNotifyChange(null, null, this,
						"voluntarioClasificacionesSeleccionadas");
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
