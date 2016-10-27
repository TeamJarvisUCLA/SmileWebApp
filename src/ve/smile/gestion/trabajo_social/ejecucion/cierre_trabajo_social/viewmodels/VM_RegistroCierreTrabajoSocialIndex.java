package ve.smile.gestion.trabajo_social.ejecucion.cierre_trabajo_social.viewmodels;

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
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.Motivo;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_RegistroCierreTrabajoSocialIndex extends VM_WindowWizard {

	private boolean estatus = true;
	private List<IndicadorTsPlan> indicadorTsPlans;
	private Date fechaPlanificadaInicio;
	private Date fechaPlanificadaFin;
	private Date fechaEjecucionInicio;
	private Date fechaEjecucionFin;
	private Motivo motivo = new Motivo();

	@Init(superclass = true)
	public void childInit() {
		this.setEstatus(true);
	}

	@Command("buscarMotivo")
	public void buscarMotivo() {
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
						"views/desktop/gestion/trabajoSocial/ejecucion/cierreTrabajoSocial/catalogoMotivo.zul",
						catalogueDialogData);
	}

	public void refreshMotivo() {
		this.getTsPlanSelected().setFkMotivo(motivo);
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
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
	public String executeCustom1(Integer currentStep) {
		this.setIndicadorTsPlans(new ArrayList<IndicadorTsPlan>());
		this.setSelectedObject(new TsPlan());

		BindUtils.postNotifyChange(null, null, this, "indicadorTsPlans");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setIndicadorTsPlans(new ArrayList<IndicadorTsPlan>());
		this.setSelectedObject(new TsPlan());

		BindUtils.postNotifyChange(null, null, this, "indicadorTsPlans");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return super.executeCancelar(currentStep);
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

		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/cierreTrabajoSocial/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/cierreTrabajoSocial/registroCierreTrabajoSocial.zul");
		urls.add("views/desktop/gestion/trabajoSocial/ejecucion/cierreTrabajoSocial/registroCompletado.zul");
		return urls;
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
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setIndicadorTsPlans(null);

			PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
				return (String) payloadIndicadorTsPlanResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countTsPlanIndicadores = Double.valueOf(
					String.valueOf(payloadIndicadorTsPlanResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countTsPlanIndicadores <= 0) {
				return "E:Error 0:El trabajo social planificado seleccionado <b>no tiene indicadores asociados.</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.estatus = true;
			this.setIndicadorTsPlans(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkTsPlan.idTsPlan",
					String.valueOf(this.getTsPlanSelected().getIdTsPlan()));
			PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadIndicadorTsPlanResponse.getObjetos() != null
					&& payloadIndicadorTsPlanResponse.getObjetos().size() > 0) {
				for (IndicadorTsPlan indicadorTsPlan : payloadIndicadorTsPlanResponse
						.getObjetos()) {

					if (indicadorTsPlan.getValorReal() == null) {
						indicadorTsPlan.setValorReal(indicadorTsPlan
								.getValorEsperado());
					}
					this.getIndicadorTsPlans().add(indicadorTsPlan);
				}

			}
			this.setFechaPlanificadaInicio(new Date(this.getTsPlanSelected()
					.getFechaInicio()));
			this.setFechaPlanificadaFin(new Date(this.getTsPlanSelected()
					.getFechaFin()));
			this.setFechaEjecucionInicio(new Date(this.getTsPlanSelected()
					.getFechaInicio()));
			this.setFechaEjecucionFin(new Date(this.getTsPlanSelected()
					.getFechaFin()));
			BindUtils.postNotifyChange(null, null, this, "indicadorTsPlans");
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			if (this.estatus) {
				for (IndicadorTsPlan indicadorTsPlan : this
						.getIndicadorTsPlans()) {

					PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
							.modificar(indicadorTsPlan);
					if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
						return (String) payloadIndicadorTsPlanResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

					TsPlan tsPlan = this.getTsPlanSelected();
					tsPlan.setFechaInicioEjecucion(this.fechaEjecucionFin
							.getTime());
					tsPlan.setFechaInicioEjecucion(this.fechaEjecucionInicio
							.getTime());
					tsPlan.setFkMotivo(null);
					tsPlan.setEstatusTsPlan(EstatusTrabajoSocialPlanificadoEnum.EJECUTADO
							.ordinal());
					PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
							.modificar(tsPlan);
					if (!UtilPayload.isOK(payloadTsPlanResponse)) {
						return (String) payloadTsPlanResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}

				}
			} else {
				TsPlan tsPlan = this.getTsPlanSelected();
				tsPlan.setEstatusTsPlan(EstatusTrabajoSocialPlanificadoEnum.CANCELADO
						.ordinal());
				PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
						.modificar(tsPlan);
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
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			StringBuilder stringBuilder = new StringBuilder();
			for (IndicadorTsPlan indicadorTsPlan : this.getIndicadorTsPlans()) {
				if (indicadorTsPlan.getValorReal() == null
						|| indicadorTsPlan.getValorReal() == 0) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(indicadorTsPlan.getFkIndicador()
							.getNombre());
				}
			}
			String indicadores = stringBuilder.toString();
			if (!indicadores.trim().isEmpty()) {
				return "E:Error Code 5-Debe verificar el valor real de los siguientes indicadores: <b>"
						+ indicadores + "</b>";
			}

		}
		return super.isValidPreconditionsFinalizar(currentStep);
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

	public boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public List<IndicadorTsPlan> getIndicadorTsPlans() {
		if (indicadorTsPlans == null) {
			indicadorTsPlans = new ArrayList<>();
		}
		return indicadorTsPlans;
	}

	public void setIndicadorTsPlans(List<IndicadorTsPlan> indicadorTsPlans) {
		this.indicadorTsPlans = indicadorTsPlans;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public Date getFechaPlanificadaInicio() {
		return fechaPlanificadaInicio;
	}

	public void setFechaPlanificadaInicio(Date fechaPlanificadaInicio) {
		this.fechaPlanificadaInicio = fechaPlanificadaInicio;
	}

	public Date getFechaPlanificadaFin() {
		return fechaPlanificadaFin;
	}

	public void setFechaPlanificadaFin(Date fechaPlanificadaFin) {
		this.fechaPlanificadaFin = fechaPlanificadaFin;
	}

	public Date getFechaEjecucionInicio() {
		return fechaEjecucionInicio;
	}

	public void setFechaEjecucionInicio(Date fechaEjecucionInicio) {
		this.fechaEjecucionInicio = fechaEjecucionInicio;
	}

	public Date getFechaEjecucionFin() {
		return fechaEjecucionFin;
	}

	public void setFechaEjecucionFin(Date fechaEjecucionFin) {
		this.fechaEjecucionFin = fechaEjecucionFin;
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}
}
