package ve.smile.gestion.trabajo_social.planificacion.actividades.recursos.viewmodels;

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
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Recurso;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.dto.TsPlanActividadRecurso;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadTsPlanActividadRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ActividadesRecursosTrabajoSocialPlanificado extends
		VM_WindowWizard {

	private List<TsPlanActividad> listTsPlanActividads;
	private int indexActividad;
	private List<TsPlanActividadRecurso> listTsPlanActividadRecursoDelete;

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarRecursos")
	public void buscarRecursos(@BindingParam("index") int index) {
		this.indexActividad = index;
		CatalogueDialogData<Recurso> catalogueDialogData = new CatalogueDialogData<Recurso>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Recurso>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Recurso> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Recurso> listRecurso = new ArrayList<Recurso>();
						listRecurso = catalogueDialogCloseEvent.getEntities();

						refreshRecurso(listRecurso);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/recursos/catalogoRecursos.zul",
						catalogueDialogData);
	}

	public void refreshRecurso(List<Recurso> listRecurso) {

		boolean validar = true;
		TsPlanActividadRecurso tsPlanActividadRecurso = new TsPlanActividadRecurso();

		for (Recurso recurso : listRecurso) {

			for (TsPlanActividadRecurso tsPlanActividadRecurso2 : this
					.getListTsPlanActividads().get(indexActividad)
					.getTsPlanActividadRecursos()) {
				if (tsPlanActividadRecurso2.getFkRecurso().getIdRecurso()
						.equals(recurso.getIdRecurso())) {

					validar = false;

				}
			}

			if (validar) {
				tsPlanActividadRecurso = new TsPlanActividadRecurso();
				tsPlanActividadRecurso.setFkTsPlanActividad(this
						.getListTsPlanActividads().get(indexActividad));
				tsPlanActividadRecurso.setFkRecurso(recurso);
				tsPlanActividadRecurso.setFechaAsignacion(new Date().getTime());
				this.getListTsPlanActividads().get(indexActividad)
						.getTsPlanActividadRecursos()
						.add(tsPlanActividadRecurso);
			}
			validar = true;
		}
		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarRecurso")
	public void eliminarRecurso(
			@BindingParam("tsPlanActividadRecurso") TsPlanActividadRecurso tsPlanActividadRecurso,
			@BindingParam("index") int index) {
		this.getListTsPlanActividadRecursoDelete().add(tsPlanActividadRecurso);
		this.getListTsPlanActividads().get(index).getTsPlanActividadRecursos()
				.remove(tsPlanActividadRecurso);
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
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadTsPlanActividadRecursoResponse payloadTsPlanActividadRecursoResponse = new PayloadTsPlanActividadRecursoResponse();
			for (TsPlanActividad obj : this.getListTsPlanActividads()) {
				for (TsPlanActividadRecurso tsPlanActividadRecurso : obj
						.getTsPlanActividadRecursos()) {
					TsPlanActividadRecurso tsPlanActividadRecurso2 = new TsPlanActividadRecurso(
							new TsPlanActividad(obj.getIdTsPlanActividad()),
							tsPlanActividadRecurso.getFkRecurso(),
							tsPlanActividadRecurso.getCantidad(),
							tsPlanActividadRecurso.getFechaAsignacion());
					tsPlanActividadRecurso2
							.setIdTsPlanActividadRecurso(tsPlanActividadRecurso
									.getIdTsPlanActividadRecurso());
					if (tsPlanActividadRecurso2.getIdTsPlanActividadRecurso() == null) {

						payloadTsPlanActividadRecursoResponse = S.TsPlanActividadRecursoService
								.incluir(tsPlanActividadRecurso2);
					} else {
						payloadTsPlanActividadRecursoResponse = S.TsPlanActividadRecursoService
								.modificar(tsPlanActividadRecurso2);
					}

					if (!UtilPayload
							.isOK(payloadTsPlanActividadRecursoResponse)) {
						return (String) payloadTsPlanActividadRecursoResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}

			}
			for (TsPlanActividadRecurso tsPlanActividadRecurso : this
					.getListTsPlanActividadRecursoDelete()) {
				if (tsPlanActividadRecurso.getIdTsPlanActividadRecurso() != null) {
					payloadTsPlanActividadRecursoResponse = S.TsPlanActividadRecursoService
							.eliminar(tsPlanActividadRecurso
									.getIdTsPlanActividadRecurso());
					if (!UtilPayload
							.isOK(payloadTsPlanActividadRecursoResponse)) {
						return (String) payloadTsPlanActividadRecursoResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}

			}

			goToNextStep();
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 2) {
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {

				for (TsPlanActividadRecurso tsPlanActividadRecurso : tsPlanActividad
						.getTsPlanActividadRecursos()) {
					if (tsPlanActividadRecurso.getFechaAsignacion() == null) {
						if (!stringBuilder.toString().trim().isEmpty()) {
							stringBuilder.append(",  ");
						}
						stringBuilder.append(tsPlanActividadRecurso
								.getFkRecurso().getNombre());
					}

					if (tsPlanActividadRecurso.getCantidad() == null
							|| tsPlanActividadRecurso.getCantidad() <= 0) {
						if (!stringBuilder2.toString().trim().isEmpty()) {
							stringBuilder2.append(",  ");
						}
						stringBuilder2.append(tsPlanActividadRecurso
								.getFkRecurso().getNombre());
					}
				}
			}
			actividades = stringBuilder.toString();
			if (!actividades.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Fecha de Asignación </b> de los siguientes recursos: <b>"
						+ actividades + "</b>";
			}

			if (!stringBuilder2.toString().trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar la  <b> Cantidad </b> de los siguientes recursos: <b>"
						+ stringBuilder2.toString() + "</b>";
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

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/recursos/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/recursos/actividadesRecursos.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/recursos/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				this.getListTsPlanActividads().addAll(
						payloadTsPlanActividadResponse.getObjetos());
			}

			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkTsPlanActividad.idTsPlanActividad",
						String.valueOf(tsPlanActividad.getIdTsPlanActividad()));
				PayloadTsPlanActividadRecursoResponse payloadTsPlanActividadRecursoResponse = S.TsPlanActividadRecursoService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadRecursoResponse)) {
					tsPlanActividad
							.setTsPlanActividadRecursos(payloadTsPlanActividadRecursoResponse
									.getObjetos());
					if (tsPlanActividad.getTsPlanActividadRecursos() == null) {
						tsPlanActividad
								.setTsPlanActividadRecursos(new ArrayList<TsPlanActividadRecurso>());
					}
				}

			}

		}
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

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
	public String executeCustom1(Integer currentStep) {
		this.setListTsPlanActividads(null);
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	public List<TsPlanActividad> getListTsPlanActividads() {
		if (this.listTsPlanActividads == null) {
			listTsPlanActividads = new ArrayList<>();
		}
		return listTsPlanActividads;
	}

	public void setListTsPlanActividads(
			List<TsPlanActividad> listTsPlanActividads) {
		this.listTsPlanActividads = listTsPlanActividads;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

	public List<TsPlanActividadRecurso> getListTsPlanActividadRecursoDelete() {
		if (this.listTsPlanActividadRecursoDelete == null) {
			listTsPlanActividadRecursoDelete = new ArrayList<>();
		}
		return listTsPlanActividadRecursoDelete;
	}

	public void setListTsPlanActividadRecursoDelete(
			List<TsPlanActividadRecurso> listTsPlanActividadRecursoDelete) {
		this.listTsPlanActividadRecursoDelete = listTsPlanActividadRecursoDelete;
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
