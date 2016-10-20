package ve.smile.gestion.trabajo_social.ejecucion.evaluarActividades.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
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
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.Motivo;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_EvaluarActividadIndex extends VM_WindowWizard {

	private List<TsPlanActividad> listTsPlanActividads;
	private int indexActividad;
	private Motivo motivo = new Motivo();

	@Command("changeCheck")
	public void changeCheck(@BindingParam("index") int index,
			@BindingParam("check") boolean check) {
		this.getListTsPlanActividads().get(index).setEjecucion(check);
		if (check) {
			this.getListTsPlanActividads().get(index).setFkMotivo(null);
		}
	}

	@Command("removerMotivo")
	public void removerMotivo(@BindingParam("index") int index) {
		this.getListTsPlanActividads().get(index).setFkMotivo(null);
	}

	@Command("buscarMotivo")
	public void buscarMotivo(@BindingParam("index") int index) {
		this.indexActividad = index;
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
						motivo = catalogueDialogCloseEvent.getEntity();
						refreshMotivo();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/ejecucion/evaluarActividades/catalogoMotivo.zul",
						catalogueDialogData);
	}

	public void refreshMotivo() {
		this.getListTsPlanActividads().get(indexActividad).setFkMotivo(motivo);
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

		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/evaluarActividades/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/evaluarActividades/evaluarActividadTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/evaluarActividades/registroCompletado.zul");
		return urls;
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {

		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				if (payloadTsPlanActividadResponse.getObjetos() != null) {
					this.getListTsPlanActividads().addAll(
							payloadTsPlanActividadResponse.getObjetos());
				}

			}
			BindUtils
					.postNotifyChange(null, null, this, "listTsPlanActividads");
		}

		return "";
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				return (String) payloadTsPlanActividadResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countTsPlanActividadesTrabajadores = Double.valueOf(
					String.valueOf(payloadTsPlanActividadResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countTsPlanActividadesTrabajadores <= 0) {
				return "E:Error 0:El trabajo social planificado seleccionado <b>no tiene actividades asignadas</b>, debe asignarle al menos una.";
			}
		}

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
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, -1);

				if (tsPlanActividad.getFechaEjecutada() == null
						|| !tsPlanActividad.getFechaEjecutadaDate().before(
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
				return "E:Error Code 5-Debe verificar la  <b> Fecha de Ejecución </b> de los siguientes actividades: <b>"
						+ actividades + "</b>";
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {
				PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
						.modificar(tsPlanActividad);
				if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
					return String.valueOf(payloadTsPlanActividadResponse
							.getInformacion(IPayloadResponse.MENSAJE));
				}
			}
			goToNextStep();
		}

		return "";
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
	public String executeCustom1(Integer currentStep) {
		restartWizard();
		return super.executeCustom1(currentStep);
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

	public List<TsPlanActividad> getListTsPlanActividads() {
		if (listTsPlanActividads == null) {
			listTsPlanActividads = new ArrayList<>();
		}
		return listTsPlanActividads;
	}

	public void setListTsPlanActividads(
			List<TsPlanActividad> listTsPlanActividads) {
		this.listTsPlanActividads = listTsPlanActividads;
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
