package ve.smile.gestion.trabajo_social.planificacion.actividades.asignacion.viewmodels;

import java.util.ArrayList;
import java.util.Date;
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
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.dto.Directorio;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ActividadesTrabajoSocialPlanificadoIndex extends
		VM_WindowWizard {

	private List<Actividad> actividades;
	private Set<Actividad> actividadesSeleccionadas;
	private List<Actividad> trabajoSocialActividades;
	private Set<Actividad> trabajoSocialActividadesSeleccionadas;
	// forEachStatus.index
	private List<TsPlanActividad> tsPlanActividads;

	private Directorio directorio;

	private int indexActividad;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Command
	@NotifyChange({ "actividades", "trabajoSocialActividades",
			"actividadesSeleccionadas", "trabajoSocialActividadesSeleccionadas" })
	public void agregarActividadesPlantilla() {
		if (this.getActividadesSeleccionadas() != null
				&& this.getActividadesSeleccionadas().size() > 0) {
			this.getTrabajoSocialActividades().addAll(actividadesSeleccionadas);
			this.getTrabajoSocialActividadesSeleccionadas().clear();
			this.getActividadesSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "actividades", "trabajoSocialActividades",
			"actividadesSeleccionadas", "trabajoSocialActividadesSeleccionadas" })
	public void removerActividadesPlantilla() {
		if (this.getTrabajoSocialActividadesSeleccionadas() != null
				&& this.getTrabajoSocialActividadesSeleccionadas().size() > 0) {
			this.getTrabajoSocialActividades().removeAll(
					trabajoSocialActividadesSeleccionadas);
			this.getActividadesSeleccionadas().clear();
			this.getTrabajoSocialActividadesSeleccionadas().clear();
		}
	}

	public boolean disabledActividad(Actividad actividad) {
		return this.getTrabajoSocialActividades().contains(actividad);
	}

	@Command("buscarDirectorio")
	public void buscarDirectorio(@BindingParam("index") int index) {
		this.indexActividad = index;
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
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
	}

	public void refreshDirectorio() {
		this.getTsPlanActividads().get(indexActividad)
				.setFkDirectorio(directorio);
		BindUtils.postNotifyChange(null, null, this, "tsPlanActividads");
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

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/selectActividadesTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/editActividadesTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/registroCompletado.zul");
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
			if (this.getTrabajoSocialActividades().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos una <b>Actividad</b> al trabajo social planificado.";
			}

		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 2) {
			if (this.getTrabajoSocialActividades().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al trabajo social planificado.";
			} else {
				this.getTsPlanActividads().clear();
				for (Actividad actividad : this.getTrabajoSocialActividades()) {
					TsPlanActividad tsPlanActividad = new TsPlanActividad();
					tsPlanActividad.setFkActividad(actividad);
					tsPlanActividad.setFkTsPlan((TsPlan) this
							.getSelectedObject());
					tsPlanActividad.setFechaPlanificada(new Date().getTime());
					this.getTsPlanActividads().add(tsPlanActividad);
				}
				BindUtils
						.postNotifyChange(null, null, this, "indicadorTsPlans");
			}

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			for (TsPlanActividad tsPlanActividad : this.getTsPlanActividads()) {
				PayloadTsPlanActividadResponse payloadTsPlanResponse = S.TsPlanActividadService
						.incluir(tsPlanActividad);
				if (!UtilPayload.isOK(payloadTsPlanResponse)) {
					return (String) payloadTsPlanResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}

			goToNextStep();
		}

		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setTsPlanActividads(null);
		this.setTrabajoSocialActividades(null);
		this.setSelectedObject(new TsPlan());

		BindUtils.postNotifyChange(null, null, this, "tsPlanActividads");
		BindUtils
				.postNotifyChange(null, null, this, "trabajoSocialActividades");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	public List<Actividad> getActividades() {
		if (this.actividades == null) {
			this.actividades = new ArrayList<>();
		}
		if (this.actividades.isEmpty()) {
			PayloadActividadResponse payloadActividadResponse = S.ActividadService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadActividadResponse)) {
				Alert.showMessage(payloadActividadResponse);
			} else {
				actividades.addAll(payloadActividadResponse.getObjetos());
			}
		}
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	public Set<Actividad> getActividadesSeleccionadas() {
		if (this.actividadesSeleccionadas == null) {
			this.actividadesSeleccionadas = new HashSet<>();
		}
		return actividadesSeleccionadas;
	}

	public void setActividadesSeleccionadas(
			Set<Actividad> actividadesSeleccionadas) {
		this.actividadesSeleccionadas = actividadesSeleccionadas;
	}

	public List<Actividad> getTrabajoSocialActividades() {
		if (this.trabajoSocialActividades == null) {
			this.trabajoSocialActividades = new ArrayList<>();
		}
		return trabajoSocialActividades;
	}

	public void setTrabajoSocialActividades(
			List<Actividad> trabajoSocialActividades) {
		this.trabajoSocialActividades = trabajoSocialActividades;
	}

	public Set<Actividad> getTrabajoSocialActividadesSeleccionadas() {
		if (this.trabajoSocialActividadesSeleccionadas == null) {
			this.trabajoSocialActividadesSeleccionadas = new HashSet<>();
		}
		return trabajoSocialActividadesSeleccionadas;
	}

	public void setTrabajoSocialActividadesSeleccionadas(
			Set<Actividad> trabajoSocialActividadesSeleccionadas) {
		this.trabajoSocialActividadesSeleccionadas = trabajoSocialActividadesSeleccionadas;
	}

	public List<TsPlanActividad> getTsPlanActividads() {
		if (this.tsPlanActividads == null) {
			this.tsPlanActividads = new ArrayList<>();
		}
		return tsPlanActividads;
	}

	public void setTsPlanActividads(List<TsPlanActividad> tsPlanActividads) {
		this.tsPlanActividads = tsPlanActividads;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

}
