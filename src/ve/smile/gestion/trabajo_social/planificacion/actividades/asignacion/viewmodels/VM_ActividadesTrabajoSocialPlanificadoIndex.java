package ve.smile.gestion.trabajo_social.planificacion.actividades.asignacion.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
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
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.dto.Directorio;
import ve.smile.dto.PlantillaTrabajoSocialActividad;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ActividadesTrabajoSocialPlanificadoIndex extends
		VM_WindowWizard {

	private List<Actividad> actividades;
	private Set<Actividad> actividadesSeleccionadas;
	private List<Actividad> trabajoSocialActividades;
	private Set<Actividad> trabajoSocialActividadesSeleccionadas;
	private List<TsPlanActividad> tsPlanActividads;
	private List<TsPlanActividad> tsPlanActividadsDelete;

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
			for (Actividad actividad : trabajoSocialActividadesSeleccionadas) {
				Map<String, String> criterios = new HashMap<String, String>();

				criterios
						.put("fkTsPlanActividad.fkTsPlan.idTsPlan", String
								.valueOf(this.getTsPlanificadoSelected()
										.getIdTsPlan()));
				criterios.put("fkTsPlanActividad.fkActividad.idActividad",
						String.valueOf(actividad.getIdActividad()));
				// Table Relation TsPlanActividadTrabajadores
				PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
					Alert.showMessage(payloadTsPlanActividadTrabajadorResponse
							.getInformacion(IPayloadResponse.MENSAJE));
					return;
				}

				Integer countTsPlanActividadesTrabajadores = Double.valueOf(
						String.valueOf(payloadTsPlanActividadTrabajadorResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();
				if (countTsPlanActividadesTrabajadores > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que se encuentra asignada a "
							+ countTsPlanActividadesTrabajadores
							+ " Trabajadores");
					return;
				}
				// Table Relation TsPlanActividadVoluntario
				PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
					Alert.showMessage(payloadTsPlanActividadVoluntarioResponse
							.getInformacion(IPayloadResponse.MENSAJE));
					return;
				}

				Integer countTsPlanActividadVoluntarios = Double.valueOf(
						String.valueOf(payloadTsPlanActividadVoluntarioResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countTsPlanActividadVoluntarios > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que se encuentra asignada a "
							+ countTsPlanActividadVoluntarios + " Voluntarios");
					return;
				}

				// Table Relation TsPlanActividadVoluntario
				PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse = S.IndicadorTsPlanActividadService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadIndicadorTsPlanActividadResponse)) {
					Alert.showMessage(payloadIndicadorTsPlanActividadResponse);
				}

				Integer countIndicadorTsPlanActividad = Double.valueOf(
						String.valueOf(payloadIndicadorTsPlanActividadResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countIndicadorTsPlanActividad > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está asociada a "
							+ countIndicadorTsPlanActividad + " Indicadores");
					return;
				}
				// Table Relation TsPlanActividadRecurso
				PayloadTsPlanActividadRecursoResponse payloadTsPlanActividadRecursoResponse = S.TsPlanActividadRecursoService
						.contarCriterios(TypeQuery.EQUAL, criterios);

				if (!UtilPayload.isOK(payloadTsPlanActividadRecursoResponse)) {
					Alert.showMessage(payloadTsPlanActividadRecursoResponse);
					return;
				}

				Integer countTsPlanActividadRecursos = Double.valueOf(
						String.valueOf(payloadTsPlanActividadRecursoResponse
								.getInformacion(IPayloadResponse.COUNT)))
						.intValue();

				if (countTsPlanActividadRecursos > 0) {
					Alert.showMessage("E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está asociada a "
							+ countTsPlanActividadRecursos + " Recursos");
					return;
				}
				this.getTrabajoSocialActividades().remove(actividad);
			}

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

	@Command("removerDirectorio")
	public void removerDirectorio(@BindingParam("index") int index) {
		this.getTsPlanActividads().get(indexActividad)
				.setFkDirectorio(new Directorio());
		BindUtils.postNotifyChange(null, null, this, "tsPlanActividads");
	}

	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
	}

	public void refreshDirectorio() {
		this.getTsPlanActividads().get(indexActividad)
				.setFkDirectorio(directorio);
		BindUtils.postNotifyChange(null, null, this, "tsPlanActividads");
	}

	@Command("buscarPlantilla")
	public void buscarPlantilla() {
		CatalogueDialogData<PlantillaTrabajoSocialActividad> catalogueDialogData = new CatalogueDialogData<PlantillaTrabajoSocialActividad>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<PlantillaTrabajoSocialActividad>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<PlantillaTrabajoSocialActividad> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<PlantillaTrabajoSocialActividad> listActividadesPlantillaTrabajoSocial = new ArrayList<>();
						listActividadesPlantillaTrabajoSocial = catalogueDialogCloseEvent
								.getEntities();

						refreshActividades(listActividadesPlantillaTrabajoSocial);
					}
				});

		catalogueDialogData.put("trabajoSocial", this
				.getTsPlanificadoSelected().getFkTrabajoSocial());
		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/asignacion/catalogoPlantillaActividadesTrabajoSocial.zul",
						catalogueDialogData);
	}

	public void refreshActividades(
			List<PlantillaTrabajoSocialActividad> listActividadesPlantillaTrabajoSocial) {

		for (PlantillaTrabajoSocialActividad plantillaTrabajoSocialActividad : listActividadesPlantillaTrabajoSocial) {
			if (!this.getTrabajoSocialActividades().contains(
					plantillaTrabajoSocialActividad.getFkActividad())) {
				this.getTrabajoSocialActividades().add(
						plantillaTrabajoSocialActividad.getFkActividad());
			}
		}

		BindUtils
				.postNotifyChange(null, null, this, "trabajoSocialActividades");
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
			if (this.getTrabajoSocialActividades().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos una <b>Actividad</b> al trabajo social planificado.";
			}
		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> criterios = new HashMap<>();

			criterios.put("fkTsPlan.idTsPlan", String.valueOf(this
					.getTsPlanificadoSelected().getIdTsPlan()));
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			this.setTsPlanActividads(null);
			this.setTrabajoSocialActividades(null);
			if (payloadTsPlanActividadResponse.getObjetos() != null) {

				for (TsPlanActividad tsPlanActividad : payloadTsPlanActividadResponse
						.getObjetos()) {
					this.getTsPlanActividads().add(tsPlanActividad);
					this.getTrabajoSocialActividades().add(
							tsPlanActividad.getFkActividad());
				}
			}

			BindUtils.postNotifyChange(null, null, this,
					"trabajoSocialIndicadores");
			BindUtils.postNotifyChange(null, null, this,
					"tsPlanificadoSelected");
		}

		if (currentStep == 2) {
			if (this.getTrabajoSocialActividades().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos una <b>Actividad</b> al trabajo social planificado.";
			} else {
				boolean validar = true;
				List<TsPlanActividad> tsPlanActividads2 = new ArrayList<>();
				tsPlanActividads2.addAll(new ArrayList<>(this
						.getTsPlanActividads()));
				for (Actividad actividad : this.getTrabajoSocialActividades()) {
					for (TsPlanActividad tsPlanActividad : this
							.getTsPlanActividads()) {
						if (tsPlanActividad.getFkActividad().getIdActividad()
								.equals(actividad.getIdActividad())) {
							validar = false;
							tsPlanActividads2.remove(tsPlanActividad);
							break;
						}
					}
					if (validar) {
						TsPlanActividad tsPlanActividad = new TsPlanActividad();
						tsPlanActividad.setFkActividad(actividad);
						tsPlanActividad.setFkTsPlan((TsPlan) this
								.getSelectedObject());
						tsPlanActividad.setEjecucion(false);
						this.getTsPlanActividads().add(tsPlanActividad);
					}
					validar = true;
				}

				this.getTsPlanActividadsDelete().addAll(tsPlanActividads2);
				this.getTsPlanActividads().removeAll(tsPlanActividads2);

				BindUtils.postNotifyChange(null, null, this, "*");
			}

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			for (TsPlanActividad tsPlanActividad : this.getTsPlanActividads()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, -1);

				if (tsPlanActividad.getFechaPlanificada() == null
						|| !tsPlanActividad.getFechaPlanificadaDate().after(
								calendar.getTime())) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(tsPlanActividad.getFkActividad()
							.getNombre());
				}
			}
			actividades = stringBuilder.toString();
			if (!actividades.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Fecha de Planificación </b> de los siguientes actividades: <b>"
						+ actividades + "</b>";
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			for (TsPlanActividad tsPlanActividad : this.getTsPlanActividads()) {
				PayloadTsPlanActividadResponse payloadTsPlanActividadResponse;
				if (tsPlanActividad.getIdTsPlanActividad() == null) {
					payloadTsPlanActividadResponse = S.TsPlanActividadService
							.incluir(tsPlanActividad);
				} else {
					payloadTsPlanActividadResponse = S.TsPlanActividadService
							.modificar(tsPlanActividad);
				}

				if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
					return (String) payloadTsPlanActividadResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}
			for (TsPlanActividad tsPlanActividad : this
					.getTsPlanActividadsDelete()) {
				if (tsPlanActividad.getIdTsPlanActividad() != null) {
					PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
							.eliminar(tsPlanActividad.getIdTsPlanActividad());
					if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
						return (String) payloadTsPlanActividadResponse
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

		this.setActividades(null);
		this.setActividadesSeleccionadas(null);
		this.setTrabajoSocialActividades(null);
		this.setTrabajoSocialActividadesSeleccionadas(null);
		this.setTsPlanActividads(null);
		this.setTsPlanActividadsDelete(null);
		BindUtils.postNotifyChange(null, null, this, "*");

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

	public List<TsPlanActividad> getTsPlanActividadsDelete() {
		if (tsPlanActividadsDelete == null) {
			tsPlanActividadsDelete = new ArrayList<>();
		}
		return tsPlanActividadsDelete;
	}

	public void setTsPlanActividadsDelete(
			List<TsPlanActividad> tsPlanActividadsDelete) {
		this.tsPlanActividadsDelete = tsPlanActividadsDelete;
	}

	public TsPlan getTsPlanificadoSelected() {
		return (TsPlan) this.getSelectedObject();
	}
}
