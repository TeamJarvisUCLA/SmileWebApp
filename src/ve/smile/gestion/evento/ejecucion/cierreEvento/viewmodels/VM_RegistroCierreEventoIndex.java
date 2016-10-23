package ve.smile.gestion.evento.ejecucion.cierreEvento.viewmodels;

import java.text.SimpleDateFormat;
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
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.IndicadorEventoPlanificado;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.Motivo;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_RegistroCierreEventoIndex extends VM_WindowWizard {

	private boolean estatus = true;
	private List<IndicadorEventoPlanificado> indicadorEventoPlanificado = new ArrayList<>();
	private Date fechaPlanificadaInicio;
	private Date fechaEjecucionInicio;
	private Date fechaPlanificadaFin;
	private Date fechaEjecucionFin;
	private Motivo motivo = new Motivo();

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
						"views/desktop/gestion/evento/ejecucion/cierreEvento/catalogoMotivo.zul",
						catalogueDialogData);
	}

	public void refreshMotivo() {
		((EventoPlanificado) this.selectedObject).setFkMotivo(motivo);
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
		this.setIndicadorEventoPlanificado(new ArrayList<IndicadorEventoPlanificado>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this,
				"indicadorEventoPlanificado");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";

	}
	
	@Override
	public String executeCancelar(Integer currentStep) {
		this.setIndicadorEventoPlanificado(new ArrayList<IndicadorEventoPlanificado>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "indicadorEventoPlanificado");
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

		urls.add("views/desktop/gestion/evento/ejecucion/cierreEvento/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/cierreEvento/registroCierreEvento.zul");
		urls.add("views/desktop/gestion/evento/ejecucion/cierreEvento/registroCompletado.zul");
		return urls;
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.estatus = true;
			this.setIndicadorEventoPlanificado(new ArrayList<IndicadorEventoPlanificado>());
			Map<String, String> criterios = new HashMap<>();
			EventoPlanificado eventoPlanificado = (EventoPlanificado) selectedObject;
			criterios.put("fkEventoPlanificado.idEventoPlanificado",
					eventoPlanificado.getIdEventoPlanificado() + "");
			PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventPlanResponse = S.IndicadorEventoPlanificadoService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadIndicadorEventPlanResponse.getObjetos() != null
					&& payloadIndicadorEventPlanResponse.getObjetos().size() > 0) {
				for (IndicadorEventoPlanificado indicadorEventPlan : payloadIndicadorEventPlanResponse
						.getObjetos()) {

					if (indicadorEventPlan.getValorReal() == null) {
						indicadorEventPlan.setValorReal(indicadorEventPlan
								.getValorEsperado());
					}
					this.indicadorEventoPlanificado.add(indicadorEventPlan);
				}

			}
			this.setFechaPlanificadaInicio(new Date(eventoPlanificado
					.getFechaInicio()));
			this.setFechaPlanificadaFin(new Date(eventoPlanificado
					.getFechaFin()));
			this.setFechaEjecucionInicio(new Date(eventoPlanificado
					.getFechaInicio()));
			this.setFechaEjecucionFin(new Date(eventoPlanificado
					.getFechaFin()));
			
			
//			this.estatus = false;
//			this.indicadorEventoPlanificado = new ArrayList<IndicadorEventoPlanificado>();
//			Map<String, String> criterios = new HashMap<>();
//			EventoPlanificado eventoPlanificado = (EventoPlanificado) selectedObject;
//			criterios.put("fkEventoPlanificado.idEventoPlanificado",
//					eventoPlanificado.getIdEventoPlanificado() + "");
//			PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService
//					.consultarCriterios(TypeQuery.EQUAL, criterios);
//			if (payloadIndicadorEventoPlanificadoResponse.getObjetos().size() > 0
//					& payloadIndicadorEventoPlanificadoResponse.getObjetos() != null) {
//				 for(IndicadorEventoPlanificado indicaEventPlanificado:
//				 payloadIndicadorEventoPlanificadoResponse.getObjetos()){
//				 IndicadorEventoPlanificado indEventPla = new
//				 IndicadorEventoPlanificado();
//				 indEventPla.setFkEventoPlanificado(indicaEventPlanificado.getFkEventoPlanificado());
//				 indEventPla.setFkIndicador(indicaEventPlanificado.getFkIndicador());
//				 indEventPla.setIdIndicadorEventoPlanificado(indicaEventPlanificado.getIdIndicadorEventoPlanificado());
//				 indEventPla.setValorEsperado(indicaEventPlanificado.getValorEsperado());
//				 indEventPla.setValorReal(indicaEventPlanificado.getValorEsperado());
//				 this.indicadorEventoPlanificado.add(indEventPla);
//				 }
//				this.setFechaEjecucionInicio(new Date(eventoPlanificado
//						.getFechaInicio()));
//				this.setFechaPlanificada(new Date(eventoPlanificado
//						.getFechaPlanificada()));
//				BindUtils.postNotifyChange(null, null, this,
//						"indicadorEventoPlanificado");
//			}

		}

		return "";
	}
	
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			StringBuilder stringBuilder = new StringBuilder();
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_YEAR, -2);
				UtilValidate.validateDate(this.getFechaEjecucionInicio()
						.getTime(), "Fecha Inicio de Ejecución",
						ValidateOperator.GREATER_THAN, new SimpleDateFormat(
								"yyyy-MM-dd").format(calendar.getTime()),
						"dd/MM/yyyy");
				if (this.getFechaEjecucionInicio().compareTo(
						this.getFechaEjecucionFin()) != 0) {
					UtilValidate.validateDate(this.getFechaEjecucionFin()
							.getTime(), "Fecha Fin de Ejecución",
							ValidateOperator.GREATER_THAN,
							new SimpleDateFormat("yyyy-MM-dd").format(this
									.getFechaEjecucionFin()), "dd/MM/yyyy");
				}

			} catch (Exception e) {
				return e.getMessage();
			}
			for (IndicadorEventoPlanificado indicadorEventPlan : this.indicadorEventoPlanificado) {
				if (indicadorEventPlan.getValorReal() == null
						|| indicadorEventPlan.getValorReal() == 0) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(indicadorEventPlan.getFkIndicador()
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
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {			
			if(this.estatus){
			for (IndicadorEventoPlanificado indicadorEventPlan : this.getIndicadorEventoPlanificado()) {
				PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService
						.consultarUno(indicadorEventPlan
								.getIdIndicadorEventoPlanificado());
				if (payloadIndicadorEventoPlanificadoResponse.getObjetos()
						.size() > 0
						& payloadIndicadorEventoPlanificadoResponse
								.getObjetos() != null) {
					IndicadorEventoPlanificado indicadorEventoPlanificado = payloadIndicadorEventoPlanificadoResponse
							.getObjetos().get(0);
					indicadorEventoPlanificado.setValorReal(indicadorEventPlan
							.getValorReal());
					PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventPlanResponse = S.IndicadorEventoPlanificadoService
							.modificar(indicadorEventoPlanificado);
					if (!UtilPayload.isOK(payloadIndicadorEventPlanResponse)) {
						return (String) payloadIndicadorEventPlanResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}
				
				 EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
				 eventoPlanificado.setFechaEjecutada(this.fechaEjecucionInicio.getTime());
				 eventoPlanificado.setFkMotivo(null);
				 PayloadEventoPlanificadoResponse eventoPlanificadoResponse =
				 S.EventoPlanificadoService
				 .modificar(eventoPlanificado);
				 if (!UtilPayload.isOK(eventoPlanificadoResponse)) {
					 return (String) eventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE);
				 }
				
			}
			}else{
				 EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
				 PayloadEventoPlanificadoResponse eventoPlanificadoResponse =
				 S.EventoPlanificadoService
				 .modificar(eventoPlanificado);
				 if (!UtilPayload.isOK(eventoPlanificadoResponse)) {
					 return (String) eventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE);
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

	public boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public List<IndicadorEventoPlanificado> getIndicadorEventoPlanificado() {
		return indicadorEventoPlanificado;
	}

	public void setIndicadorEventoPlanificado(
			List<IndicadorEventoPlanificado> indicadorEventoPlanificado) {
		this.indicadorEventoPlanificado = indicadorEventoPlanificado;
	}

	public Date getFechaPlanificadaInicio() {
		return fechaPlanificadaInicio;
	}

	public void setFechaPlanificadaInicio(Date fechaPlanificadaInicio) {
		this.fechaPlanificadaInicio = fechaPlanificadaInicio;
	}

	public Date getFechaEjecucionInicio() {
		return fechaEjecucionInicio;
	}

	public void setFechaEjecucionInicio(Date fechaEjecucionInicio) {
		this.fechaEjecucionInicio = fechaEjecucionInicio;
	}

	public Date getFechaPlanificadaFin() {
		return fechaPlanificadaFin;
	}

	public void setFechaPlanificadaFin(Date fechaPlanificadaFin) {
		this.fechaPlanificadaFin = fechaPlanificadaFin;
	}

	public Date getFechaEjecucionFin() {
		return fechaEjecucionFin;
	}

	public void setFechaEjecucionFin(Date fechaEjecucionFin) {
		this.fechaEjecucionFin = fechaEjecucionFin;
	}



}
