package ve.smile.gestion.voluntariado.capacitacion.inscripcion.viewmodels;

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
import lights.smile.util.UtilConverterDataList;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.VolCapacitacionPlanificada;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;

public class VM_InscripcionCapacitacionIndex extends VM_WindowWizard {
	private List<VolCapacitacionPlanificada> volCapacitacionPlanificadas;
	private List<VolCapacitacionPlanificada> volCapacitacionPlanificadasDelete;

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarVoluntarios")
	public void buscarVoluntarios() {
		CatalogueDialogData<Voluntario> catalogueDialogData = new CatalogueDialogData<Voluntario>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Voluntario>() {
					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Voluntario> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Voluntario> voluntariosInscritos = new ArrayList<Voluntario>();
						voluntariosInscritos = catalogueDialogCloseEvent
								.getEntities();
						refreshVoluntarios(voluntariosInscritos);
					}
				});
		UtilDialog
				.showDialog(
						"views/desktop/gestion/voluntariado/capacitacion/inscripcion/catalogoVoluntarios.zul",
						catalogueDialogData);
	}

	public void refreshVoluntarios(List<Voluntario> listVoluntarios) {
		boolean validar = true;
		for (Voluntario voluntario : listVoluntarios) {
			for (VolCapacitacionPlanificada volCapacitacionPlanificada : this
					.getVolCapacitacionPlanificadas()) {
				if (volCapacitacionPlanificada.getFkVoluntario()
						.getIdVoluntario().equals(voluntario.getIdVoluntario())) {
					validar = false;
					break;
				}
			}
			if (validar) {
				VolCapacitacionPlanificada volCapacitacionPlanificada = new VolCapacitacionPlanificada();
				volCapacitacionPlanificada
						.setFkCapacitacionPlanificada(new CapacitacionPlanificada(
								this.getCapacitacionPlanificadaSelected()
										.getIdCapacitacionPlanificada()));
				volCapacitacionPlanificada.setFkVoluntario(voluntario);
				this.getVolCapacitacionPlanificadas().add(
						volCapacitacionPlanificada);
			}
			validar = true;
		}

		BindUtils.postNotifyChange(null, null, this,
				"volCapacitacionPlanificadas");
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
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/registroVoluntarios.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/registroCompletado.zul");
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

	// ATRAS
	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep) {
		this.setVolCapacitacionPlanificadas(null);
		this.setVolCapacitacionPlanificadasDelete(null);
		this.setSelectedObject(new CapacitacionPlanificada());
		BindUtils.postNotifyChange(null, null, this, "*");
		restartWizard();
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setVolCapacitacionPlanificadas(null);
		this.setVolCapacitacionPlanificadasDelete(null);
		this.setSelectedObject(new CapacitacionPlanificada());
		BindUtils.postNotifyChange(null, null, this, "*");
		restartWizard();
		return "";
	}

	// SIGUIENTE
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (this.getCapacitacionPlanificadaSelected() == null) {
				return "E:Error Code 5-Debe seleccionar una <b>capacitacion planificada</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			// BUSCAR VOLUNTARIOS INSCRITOS
			this.setVolCapacitacionPlanificadas(new ArrayList<VolCapacitacionPlanificada>());
			this.setVolCapacitacionPlanificadasDelete(new ArrayList<VolCapacitacionPlanificada>());
			Map<String, String> criterios = new HashMap<>();
			criterios.put(
					"fkCapacitacionPlanificada.idCapacitacionPlanificada",
					String.valueOf(this.getCapacitacionPlanificadaSelected()
							.getIdCapacitacionPlanificada()));
			PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadVolCapacitacionPlanificadaResponse.getObjetos() != null) {
				this.getVolCapacitacionPlanificadas().addAll(
						payloadVolCapacitacionPlanificadaResponse.getObjetos());
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
		if (currentStep == 2) {
			if (this.getVolCapacitacionPlanificadas() == null
					|| this.getVolCapacitacionPlanificadas().isEmpty()) {
				return "E:Error Code 5-Debe inscribir <b>al menos un voluntario</b> en la capacitaci�n planificada";
			}
		}
		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			boolean validar = true;
			for (VolCapacitacionPlanificada volCapacitacionPlanificadaDelete : this
					.getVolCapacitacionPlanificadasDelete()) {
				for (VolCapacitacionPlanificada volCapacitacionPlanificada : this
						.getVolCapacitacionPlanificadas()) {
					if (volCapacitacionPlanificada
							.getFkVoluntario()
							.getIdVoluntario()
							.equals(volCapacitacionPlanificadaDelete
									.getFkVoluntario().getIdVoluntario())) {
						volCapacitacionPlanificada
								.setIdVolCapacitacionPlanificada(volCapacitacionPlanificadaDelete
										.getIdVolCapacitacionPlanificada());
						validar = false;
						break;
					}

				}
				if (validar) {
					if (volCapacitacionPlanificadaDelete
							.getIdVolCapacitacionPlanificada() != null) {
						PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService
								.eliminar(volCapacitacionPlanificadaDelete
										.getIdVolCapacitacionPlanificada());
						if (!UtilPayload
								.isOK(payloadVolCapacitacionPlanificadaResponse)) {
							return (String) payloadVolCapacitacionPlanificadaResponse
									.getInformacion(IPayloadResponse.MENSAJE);
						}

						if (volCapacitacionPlanificadaDelete.getFkVoluntario()
								.getFkPersona().getFkUsuario() != null
								&& volCapacitacionPlanificadaDelete
										.getFkVoluntario().getFkPersona()
										.getFkUsuario().getIdUsuario() != null) {
							String contenido = "Ha sido removido de la Capacitación "
									+ this.getCapacitacionPlanificadaSelected()
											.getFkCapacitacion().getNombre()
									+ " que se realizará el "
									+ UtilConverterDataList
											.convertirLongADate(this
													.getCapacitacionPlanificadaSelected()
													.getFechaPlanificada());
							NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
									volCapacitacionPlanificadaDelete
											.getFkVoluntario().getFkPersona()
											.getFkUsuario(),
									new Date().getTime(),
									this.getCapacitacionPlanificadaSelected()
											.getIdCapacitacionPlanificada(),
									EstatusNotificacionEnum.PENDIENTE.ordinal(),
									TipoReferenciaNotificacionEnum.CAPACITACION
											.ordinal(), contenido);
							PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
									.incluir(notificacionUsuario);
							if (!UtilPayload
									.isOK(payloadNotificacionUsuarioResponse)) {
								return (String) payloadNotificacionUsuarioResponse
										.getInformacion(IPayloadResponse.MENSAJE);
							}
						}
					}
				}
				validar = true;
			}
			for (VolCapacitacionPlanificada volCapacitacionPlanificada : this
					.getVolCapacitacionPlanificadas()) {
				if (volCapacitacionPlanificada
						.getIdVolCapacitacionPlanificada() == null) {
					PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService
							.incluir(volCapacitacionPlanificada);
					if (!UtilPayload
							.isOK(payloadVolCapacitacionPlanificadaResponse)) {
						return (String) payloadVolCapacitacionPlanificadaResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
					if (volCapacitacionPlanificada.getFkVoluntario()
							.getFkPersona().getFkUsuario() != null
							&& volCapacitacionPlanificada.getFkVoluntario()
									.getFkPersona().getFkUsuario()
									.getIdUsuario() != null) {
						String contenido = "Ha sido inscrito en la Capacitación "
								+ this.getCapacitacionPlanificadaSelected()
										.getFkCapacitacion().getNombre()
								+ " que se realizará el "
								+ UtilConverterDataList.convertirLongADate(this
										.getCapacitacionPlanificadaSelected()
										.getFechaPlanificada());
						NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
								volCapacitacionPlanificada.getFkVoluntario()
										.getFkPersona().getFkUsuario(),
								new Date().getTime(), this
										.getCapacitacionPlanificadaSelected()
										.getIdCapacitacionPlanificada(),
								EstatusNotificacionEnum.PENDIENTE.ordinal(),
								TipoReferenciaNotificacionEnum.CAPACITACION
										.ordinal(), contenido);
						PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
								.incluir(notificacionUsuario);
						if (!UtilPayload
								.isOK(payloadNotificacionUsuarioResponse)) {
							return (String) payloadNotificacionUsuarioResponse
									.getInformacion(IPayloadResponse.MENSAJE);
						}
					}
				}
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			goToNextStep();
		}
		return "";
	}

	@Command("eliminarVoluntario")
	public void eliminarVoluntario(@BindingParam("index") int index) {
		this.getVolCapacitacionPlanificadasDelete().add(
				this.getVolCapacitacionPlanificadas().get(index));
		this.getVolCapacitacionPlanificadas().remove(index);
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public List<VolCapacitacionPlanificada> getVolCapacitacionPlanificadas() {
		if (this.volCapacitacionPlanificadas == null) {
			volCapacitacionPlanificadas = new ArrayList<>();
		}
		return volCapacitacionPlanificadas;
	}

	public void setVolCapacitacionPlanificadas(
			List<VolCapacitacionPlanificada> volCapacitacionPlanificadas) {
		this.volCapacitacionPlanificadas = volCapacitacionPlanificadas;
	}

	public List<VolCapacitacionPlanificada> getVolCapacitacionPlanificadasDelete() {
		if (this.volCapacitacionPlanificadasDelete == null) {
			volCapacitacionPlanificadasDelete = new ArrayList<>();
		}
		return volCapacitacionPlanificadasDelete;
	}

	public void setVolCapacitacionPlanificadasDelete(
			List<VolCapacitacionPlanificada> volCapacitacionPlanificadasDelete) {
		this.volCapacitacionPlanificadasDelete = volCapacitacionPlanificadasDelete;
	}

	// CAPACITACION PLANIFICADA SELECTED
	public CapacitacionPlanificada getCapacitacionPlanificadaSelected() {
		return (CapacitacionPlanificada) this.selectedObject;
	}
}