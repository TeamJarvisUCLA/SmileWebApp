package ve.smile.gestion.voluntariado.capacitacion.ejecucion.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.Motivo;
import ve.smile.dto.VolCapacitacionPlanificada;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;

public class VM_CapacitacionEjecutadaIndex extends VM_WindowWizard {
	private Motivo motivo = new Motivo();
	private Date fechaEjecutada = new Date();
	private String observacion = new String();
	private List<Motivo> motivos;

	private int index;
	private List<VolCapacitacionPlanificada> voluntariosInscritos;

	@Init(superclass = true)
	public void childInit() {
		motivo = new Motivo();
		fechaEjecutada = new Date();
	}

	// VOLUNTARIOS INSCRITOS
	public List<VolCapacitacionPlanificada> getVoluntariosInscritos() {
		return voluntariosInscritos;
	}

	public void setVoluntariosInscritos(
			List<VolCapacitacionPlanificada> voluntariosInscritos) {
		this.voluntariosInscritos = voluntariosInscritos;
	}

	// FECHA EJECUTADA
	public Date getFechaEjecutada() {
		return fechaEjecutada;
	}

	public void setFechaEjecutada(Date fechaEjecutada) {
		this.fechaEjecutada = fechaEjecutada;
		if (this.getCapacitacionPlanificadaSelected() != null) {
			this.getCapacitacionPlanificadaSelected().setFechaEjecutada(
					this.fechaEjecutada.getTime());
		}

	}

	// MOTIVO DE LA CAPACITACION EJECUTADA
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
		if (this.getCapacitacionPlanificadaSelected() != null) {
			this.getCapacitacionPlanificadaSelected().setFkMotivo(this.motivo);
		}

	}

	// OBSERVACIONES DE LA CAPACITACION EJECUTADA
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
		if (this.getCapacitacionPlanificadaSelected() != null) {
			this.getCapacitacionPlanificadaSelected().setObservacion(
					this.observacion);
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
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
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
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/registroCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/registroCompletado.zul");
		return urls;
	}

	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<CapacitacionPlanificada> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusCapacitacionPlanificada", String
				.valueOf(EstatusCapacitacionPlanificadaEnum.PLANIFICADA
						.ordinal()));
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadCapacitacionPlanificadaResponse;
	}

	// ATR�S
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
				return "E:Error Code 5-Debe seleccionar una <b>Capacitación Planificada</b>";
			}
		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();
		return "";
	}

	@Command("buscarMotivo")
	public void buscarMotivo(@BindingParam("index") int index) {
		this.index = index;
		CatalogueDialogData<Motivo> catalogueDialogData = new CatalogueDialogData<Motivo>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Motivo>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Motivo> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						refreshMotivo(catalogueDialogCloseEvent.getEntity());

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/voluntariado/capacitacion/ejecucion/catalogoMotivo.zul",
						catalogueDialogData);
	}

	public void refreshMotivo(Motivo motivo) {
		this.getVoluntariosInscritos().get(index).setFkMotivo(motivo);
		BindUtils.postNotifyChange(null, null, this, "voluntariosInscritos");
	}

	@Command("changeCheck")
	public void changeCheck(@BindingParam("index") int index,
			@BindingParam("check") boolean check) {
		this.getVoluntariosInscritos().get(index).setAsistencia(check);
		if (check) {
			this.getVoluntariosInscritos().get(index).setFkMotivo(null);
		}
		BindUtils.postNotifyChange(null, null, this, "voluntariosInscritos");
	}

	// FINALIZAR
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			// VOLUNTARIOS CAPACITACION PLANIFICADA
			for (VolCapacitacionPlanificada volCapacitacionPlanificada : this
					.getVoluntariosInscritos()) {

				// SETEAR VALORES DE TABLA
				PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService
						.modificar(volCapacitacionPlanificada);
				if (!UtilPayload
						.isOK(payloadVolCapacitacionPlanificadaResponse)) {
					Alert.showMessage(payloadVolCapacitacionPlanificadaResponse);
				}
				Alert.showMessage(payloadVolCapacitacionPlanificadaResponse);
			}
			if (this.getCapacitacionPlanificadaSelected().getEjecucion()) {
				this.getCapacitacionPlanificadaSelected()
						.setEstatusCapacitacionPlanificada(
								EstatusCapacitacionPlanificadaEnum.EJECUTADA);
				this.getCapacitacionPlanificadaSelected().setFkMotivo(null);
			} else {
				this.getCapacitacionPlanificadaSelected()
						.setEstatusCapacitacionPlanificada(
								EstatusCapacitacionPlanificadaEnum.CANCELADA);
			}

			// CAPACITACION PLANIFICADA
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService
					.modificar(this.getCapacitacionPlanificadaSelected());

			goToNextStep();
			return (String) payloadCapacitacionPlanificadaResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			// BUSCAR VOLUNTARIOS INSCRITOS
			this.setFechaEjecutada(new Date(this
					.getCapacitacionPlanificadaSelected().getFechaPlanificada()));
			this.getCapacitacionPlanificadaSelected().setEjecucion(true);
			this.setVoluntariosInscritos(new ArrayList<VolCapacitacionPlanificada>());
			Map<String, String> criterios = new HashMap<>();
			criterios.put(
					"fkCapacitacionPlanificada.idCapacitacionPlanificada",
					String.valueOf(this.getCapacitacionPlanificadaSelected()
							.getIdCapacitacionPlanificada()));
			PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadVolCapacitacionPlanificadaResponse.getObjetos() != null) {
				this.getVoluntariosInscritos().addAll(
						payloadVolCapacitacionPlanificadaResponse.getObjetos());
				for (VolCapacitacionPlanificada volCapacitacionPlanificada : getVoluntariosInscritos()) {
					volCapacitacionPlanificada.setAsistencia(true);
				}
			}
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		restartWizard();
		this.setMotivo(new Motivo());
		this.setObservacion(new String());
		this.setFechaEjecutada(new Date());
		this.setSelectedObject(new CapacitacionPlanificada());
		BindUtils.postNotifyChange(null, null, this, "*");

		return "";
	}

	public List<Motivo> getMotivos() {
		if (this.motivos == null) {
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty()) {
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
			}
			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}
		return motivos;
	}

	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	// CAPACITACION PLANIFICADA SELECTED
	public CapacitacionPlanificada getCapacitacionPlanificadaSelected() {
		return (CapacitacionPlanificada) this.selectedObject;
	}

}
