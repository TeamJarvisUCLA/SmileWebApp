package ve.smile.gestion.ayudas.asignacion_ayuda;

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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.SolicitudAyudaRecurso;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.payload.response.PayloadSolicitudAyudaRecursoResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;

public class VM_AsignacionAyudaIndex extends VM_WindowWizard {

	private List<SolicitudAyudaRecurso> solicitudAyudaRecursos;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!

	}

	public List<SolicitudAyudaRecurso> getSolicitudAyudaRecursos() {

		if (this.solicitudAyudaRecursos == null) {
			this.solicitudAyudaRecursos = new ArrayList<>();
		}

		return solicitudAyudaRecursos;
	}

	public void setSolicitudAyudaRecursos(
			List<SolicitudAyudaRecurso> solicitudAyudaRecursos) {
		this.solicitudAyudaRecursos = solicitudAyudaRecursos;
	}

	@Command("buscarRecurso")
	public void buscarRecurso() {
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

						List<Recurso> recursos = catalogueDialogCloseEvent
								.getEntities();

						refreshRecursos(recursos);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/ayudas/asignacionAyuda/catalogoRecurso.zul",
						catalogueDialogData);
	}

	public void refreshRecursos(List<Recurso> recursos) {
		boolean validar = true;
		SolicitudAyudaRecurso solicitudAyudaRecurso = new SolicitudAyudaRecurso();

		for (Recurso recurso : recursos) {

			for (SolicitudAyudaRecurso solicitudAyudaRecurso2 : this
					.getSolicitudAyudaRecursos()) {
				if (solicitudAyudaRecurso2.getFkRecurso().getIdRecurso()
						.equals(recurso.getIdRecurso())) {

					validar = false;

				}
			}

			if (validar) {
				solicitudAyudaRecurso = new SolicitudAyudaRecurso();
				solicitudAyudaRecurso.setFkSolicitudAyuda(this
						.getSolicitudAyuda());
				solicitudAyudaRecurso.setFkRecurso(recurso);
				solicitudAyudaRecurso.setFechaAsignacion(new Date().getTime());
				this.getSolicitudAyudaRecursos().add(solicitudAyudaRecurso);
			}
			validar = true;
		}
		BindUtils.postNotifyChange(null, null, this, "solicitudAyudaRecursos");
	}

	@Command("eliminarRecurso")
	public void eliminarRecurso(
			@BindingParam("solicitudAyudaRecurso") SolicitudAyudaRecurso solicitudAyudaRecurso) {
		this.getSolicitudAyudaRecursos().remove(solicitudAyudaRecurso);
		BindUtils.postNotifyChange(null, null, this, "solicitudAyudaRecursos");
	}

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));

		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);

		botones.put(3, listOperacionWizard4);
		return botones;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSolicitudAyudaRecursos(null);
		restartWizard();
		BindUtils.postNotifyChange(null, null, this, "*");
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setSolicitudAyudaRecursos(null);
		restartWizard();
		BindUtils.postNotifyChange(null, null, this, "*");
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/selectSolicitudAyuda.zul");
		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/AsignacionAyudaFinalizarFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/registroCompletado.zul");

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
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();

		// usar nuevo enum recurso asignado
		criterios.put("estatusSolicitud",
				String.valueOf(EstatusSolicitudEnum.APROBADA.ordinal()));
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		return payloadSolicitudAyudaResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar una <b>Solicitud de Ayuda</b>";
			}
		}

		if (currentStep == 2) {
			if (this.getSolicitudAyudaRecursos().isEmpty()) {
				return "E:Error Code 5-Seleccione Un Recurso";
			}

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 2) {
			if (this.solicitudAyudaRecursos.isEmpty()) {
				return "E:Error Code 5-Debe seleccionar al menos un recurso";
			}
			String actividades = new String();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();
			for (SolicitudAyudaRecurso solicitudAyudaRecurso : this
					.getSolicitudAyudaRecursos()) {

				if (solicitudAyudaRecurso.getFechaAsignacion() == null) {
					if (!stringBuilder.toString().trim().isEmpty()) {
						stringBuilder.append(",  ");
					}
					stringBuilder.append(solicitudAyudaRecurso.getFkRecurso()
							.getNombre());
				}

				if (solicitudAyudaRecurso.getCantidad() == null
						|| solicitudAyudaRecurso.getCantidad() <= 0) {
					if (!stringBuilder2.toString().trim().isEmpty()) {
						stringBuilder2.append(",  ");
					}
					stringBuilder2.append(solicitudAyudaRecurso.getFkRecurso()
							.getNombre());
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
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {

			for (SolicitudAyudaRecurso elemento : this
					.getSolicitudAyudaRecursos()) {

				PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse = S.SolicitudAyudaRecursoService
						.incluir(elemento);

				if (!UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
					return (String) payloadSolicitudAyudaRecursoResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}

			}
			getSolicitudAyuda().setEstatusSolicitud(
					EstatusSolicitudEnum.PROCESADA.ordinal());
			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.modificar(getSolicitudAyuda());

			if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				return (String) payloadSolicitudAyudaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			goToNextStep();
		}
		return "";
	}

	public SolicitudAyuda getSolicitudAyuda() {
		return (SolicitudAyuda) this.selectedObject;
	}
}
