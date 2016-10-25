package ve.smile.gestion.voluntariado.capacitacion.planificacion.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.Directorio;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadCapacitacionResponse;

public class VM_CapacitacionPlanificadaIndex extends VM_WindowWizard {
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();
	private CapacitacionPlanificada capacitacionPlanificada = new CapacitacionPlanificada();

	@Init(superclass = true)
	public void childInit() {
		directorio = new Directorio();
		fechaPlanificada = new Date();
		capacitacionPlanificada = new CapacitacionPlanificada();
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	@Command("buscarDirectorio")
	public void buscarDirectorio() {
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>() {
					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						directorio = catalogueDialogCloseEvent.getEntity();
						refreshDirectorio();
					}
				});
		UtilDialog
				.showDialog(
						"views/desktop/gestion/voluntariado/capacitacion/planificacion/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}

	// FECHA PLANIFICADA
	public Date getFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}

	// CAPACITACION PLANIFICADA
	public CapacitacionPlanificada getCapacitacionPlanificada() {
		return capacitacionPlanificada;
	}

	public void setCapacitacionPlanificada(
			CapacitacionPlanificada capacitacionPlanificada) {
		this.capacitacionPlanificada = capacitacionPlanificada;
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
		urls.add("views/desktop/gestion/voluntariado/capacitacion/planificacion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/planificacion/registroCapacitacion.zul");
		return urls;
	}

	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<Capacitacion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadCapacitacionResponse;
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
			if (this.getCapacitacionSelected() == null) {
				return "E:Error Code 5-Debe seleccionar una <b>capacitaci�n</b>";
			}
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
				UtilValidate.validateDate(this.getFechaPlanificada().getTime(),
						"Fecha planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				UtilValidate.validateNull(this.getDirectorio()
						.getIdDirectorio(), "Directorio");
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getCapacitacionPlanificada().setFkCapacitacion(
					this.getCapacitacionSelected());
			this.getCapacitacionPlanificada().setFkDirectorio(
					this.getDirectorio());
			this.getCapacitacionPlanificada().setFechaPlanificada(
					this.getFechaPlanificada().getTime());
			this.getCapacitacionPlanificada()
					.setEstatusCapacitacionPlanificada(
							EstatusCapacitacionPlanificadaEnum.PLANIFICADA);
			this.getCapacitacionPlanificada().setEjecucion(new Boolean(false));
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService
					.modificar(this.getCapacitacionPlanificada());
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse)) {
				restartWizard();
				this.setSelectedObject(new Capacitacion());
				this.setDirectorio(new Directorio());
				this.setFechaPlanificada(new Date());
				this.setCapacitacionPlanificada(new CapacitacionPlanificada());
				BindUtils.postNotifyChange(null, null, this,
						"voluntariosSeleccionados");
				BindUtils.postNotifyChange(null, null, this,
						"voluntariosInscritos");
				BindUtils.postNotifyChange(null, null, this,
						"voluntariosInscritosSeleccionados");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				return (String) payloadCapacitacionPlanificadaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
		}
		return "";
	}

	// CAPACITACION SELECTED
	public Capacitacion getCapacitacionSelected() {
		return (Capacitacion) this.selectedObject;
	}
}