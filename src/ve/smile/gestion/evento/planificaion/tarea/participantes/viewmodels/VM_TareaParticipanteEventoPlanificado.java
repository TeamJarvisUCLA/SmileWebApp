package ve.smile.gestion.evento.planificaion.tarea.participantes.viewmodels;

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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilConverterDataList;
import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventPlanTareaTrabajador;
import ve.smile.dto.EventPlanTareaVoluntario;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.Trabajador;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;

public class VM_TareaParticipanteEventoPlanificado extends VM_WindowWizard{

	private List<EventPlanTarea> listEventPlanTarea;
	private List<EventPlanTareaTrabajador> listEventPlanTareaTrabajadors = new ArrayList<EventPlanTareaTrabajador>();

	private List<EventPlanTareaVoluntario> listevPlanTareaVoluntarios = new ArrayList<EventPlanTareaVoluntario>();
	private int indexTarea;

	public List<EventPlanTarea> getListEventPlanTarea() {
		if (this.listEventPlanTarea == null) {
			listEventPlanTarea = new ArrayList<>();
		}
		return listEventPlanTarea;
	}

	public void setListEventPlanTarea(
			List<EventPlanTarea> listEventPlanTarea) {
		this.listEventPlanTarea = listEventPlanTarea;
	}

	public int getIndexTarea() {
		return indexTarea;
	}

	public void setIndexTarea(int indexTarea) {
		this.indexTarea = indexTarea;
	}

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarVoluntarios")
	public void buscarVoluntarios(@BindingParam("index") int index) {
		this.setIndexTarea(index);
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
						List<Voluntario> listVoluntarios = new ArrayList<Voluntario>();
						listVoluntarios = catalogueDialogCloseEvent
								.getEntities();

						refreshVoluntarios(listVoluntarios);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/catalogoVoluntarios.zul",
						catalogueDialogData);
	}

	public void refreshVoluntarios(List<Voluntario> listVoluntarios) {

		boolean validar = true;
		List<EventPlanTareaVoluntario> listAux2 = new ArrayList<>();
		EventPlanTareaVoluntario evenPlTareV = new EventPlanTareaVoluntario();

		if (listEventPlanTarea.get(indexTarea)
				.getListPlanTareaVoluntarios() != null) {
			for (EventPlanTareaVoluntario eventPlanTarVol : listEventPlanTarea
					.get(indexTarea).getListPlanTareaVoluntarios()) {
				evenPlTareV = new EventPlanTareaVoluntario();
				evenPlTareV
						.setFkEventPlanTarea(listEventPlanTarea
								.get(indexTarea));
				evenPlTareV.setFkVoluntario(eventPlanTarVol
						.getFkVoluntario());
				listAux2.add(evenPlTareV);
			}
		}

		for (Voluntario voluntario : listVoluntarios) {
			if (listEventPlanTarea.get(indexTarea)
					.getListPlanTareaVoluntarios() != null) {

				for (EventPlanTareaVoluntario iet : listEventPlanTarea.get(
						indexTarea).getListPlanTareaVoluntarios()) {
					if (iet.getFkVoluntario().getIdVoluntario()
							.equals(voluntario.getIdVoluntario())) {

						validar = false;

					}
				}

				if (validar) {
					evenPlTareV = new EventPlanTareaVoluntario();
					evenPlTareV
							.setFkEventPlanTarea(listEventPlanTarea
									.get(indexTarea));

					evenPlTareV.setFkVoluntario(voluntario);
					listAux2.add(evenPlTareV);

				}

			} else {
				evenPlTareV = new EventPlanTareaVoluntario();
				evenPlTareV
						.setFkEventPlanTarea(listEventPlanTarea
								.get(indexTarea));

				evenPlTareV.setFkVoluntario(voluntario);
				listAux2.add(evenPlTareV);

			}

		}

		this.listEventPlanTarea.get(indexTarea)
				.setListPlanTareaVoluntarios(listAux2);

		BindUtils.postNotifyChange(null, null, this, "listEventPlanTarea");
	}

	@Command("eliminarVoluntario")
	public void eliminarVoluntario(
			@BindingParam("eventPlanTareaVoluntario") EventPlanTareaVoluntario planTareaVoluntario,
			@BindingParam("index") int index) {
		this.listevPlanTareaVoluntarios.add(planTareaVoluntario);
		this.getListEventPlanTarea().get(index)
				.getListPlanTareaVoluntarios()
				.remove(planTareaVoluntario);
		BindUtils.postNotifyChange(null, null, this, "listEventPlanTarea");
	}

	// Trabajadores
	@Command("buscarTrabajadores")
	public void buscarTrabajadores(@BindingParam("index") int index) {
		this.setIndexTarea(index);
		CatalogueDialogData<Trabajador> catalogueDialogData = new CatalogueDialogData<Trabajador>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Trabajador>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Trabajador> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Trabajador> listTrabajadors = new ArrayList<>();
						listTrabajadors = catalogueDialogCloseEvent
								.getEntities();

						refreshTrabajadores(listTrabajadors);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/catalogoTrabajadores.zul",
						catalogueDialogData);
	}

	public void refreshTrabajadores(List<Trabajador> lisTrabajadors) {

		boolean validar = true;
		List<EventPlanTareaTrabajador> listAux2 = new ArrayList<>();
		EventPlanTareaTrabajador evntPlaTareaTrab = new EventPlanTareaTrabajador();

		if (listEventPlanTarea.get(indexTarea)
				.getListEventPlanTareaTrabajadors() != null) {
			for (EventPlanTareaTrabajador evetPsTaTra : listEventPlanTarea
					.get(indexTarea).getListEventPlanTareaTrabajadors()) {
				evntPlaTareaTrab = new EventPlanTareaTrabajador();
				evntPlaTareaTrab
						.setFkEventPlanTarea(listEventPlanTarea
								.get(indexTarea));
				evntPlaTareaTrab
						.setFkTrabajador(evetPsTaTra
								.getFkTrabajador());
				listAux2.add(evntPlaTareaTrab);
			}
		}

		for (Trabajador trabajador : lisTrabajadors) {
			if (listEventPlanTarea.get(indexTarea)
					.getListEventPlanTareaTrabajadors() != null) {

				for (EventPlanTareaTrabajador iet : listEventPlanTarea.get(
						indexTarea).getListEventPlanTareaTrabajadors()) {
					if (iet.getFkTrabajador().getIdTrabajador()
							.equals(trabajador.getIdTrabajador())) {

						validar = false;

					}
				}

				if (validar) {
					evntPlaTareaTrab = new EventPlanTareaTrabajador();
					evntPlaTareaTrab
							.setFkEventPlanTarea(listEventPlanTarea
									.get(indexTarea));

					evntPlaTareaTrab.setFkTrabajador(trabajador);
					listAux2.add(evntPlaTareaTrab);

				}

			} else {
				evntPlaTareaTrab = new EventPlanTareaTrabajador();
				evntPlaTareaTrab
						.setFkEventPlanTarea(listEventPlanTarea
								.get(indexTarea));

				evntPlaTareaTrab.setFkTrabajador(trabajador);
				listAux2.add(evntPlaTareaTrab);

			}

		}

		this.listEventPlanTarea.get(indexTarea)
				.setListEventPlanTareaTrabajadors(listAux2);

		BindUtils.postNotifyChange(null, null, this, "listEventPlanTarea");
	}

	@Command("eliminarTrabajador")
	public void eliminarTrabajador(
			@BindingParam("eventPlanTareaTrabajador") EventPlanTareaTrabajador eventPlanTareaTrabajador,
			@BindingParam("index") int index) {
		this.listEventPlanTareaTrabajadors.add(eventPlanTareaTrabajador);
		this.getListEventPlanTarea().get(index)
				.getListEventPlanTareaTrabajadors()
				.remove(eventPlanTareaTrabajador);
		BindUtils.postNotifyChange(null, null, this, "listEventPlanTarea");
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
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
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

		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/tareaParticipantes.zul");
		urls.add("views/desktop/gestion/evento/planificacion/tareas/participantes/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelected().getIdEventoPlanificado()));

			this.setListEventPlanTarea(null);
			PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadEventPlanTareaResponse)) {
				this.getListEventPlanTarea().addAll(
						payloadEventPlanTareaResponse.getObjetos());
			}

			for (EventPlanTarea evePsT : this
					.getListEventPlanTarea()) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkEventPlanTarea.idEventPlanTarea",
						String.valueOf(evePsT.getIdEventPlanTarea()));
				PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
					evePsT
							.setListPlanTareaVoluntarios(payloadEventPlanTareaVoluntarioResponse
									.getObjetos());
				}

				PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {

					evePsT
							.setListEventPlanTareaTrabajadors(payloadEventPlanTareaTrabajadorResponse
									.getObjetos());
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
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkEventoPlanificado.idEventoPlanificado",
					String.valueOf(getEventoPlanificadoSelected().getIdEventoPlanificado()));

			this.setListEventPlanTarea(new ArrayList<EventPlanTarea>());
			PayloadEventPlanTareaResponse payloadEventPsResponse = S.EventPlanTareaService
					.contarCriterios(TypeQuery.EQUAL, parametro);
			if (!UtilPayload.isOK(payloadEventPsResponse)) {
				return (String) payloadEventPsResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			Integer countEventPsTarea = Double.valueOf(
					String.valueOf(payloadEventPsResponse
							.getInformacion(IPayloadResponse.COUNT)))
					.intValue();
			if (countEventPsTarea <= 0) {
				return "E:Error 0:El evento planificado seleccionado <b>no tiene tarea asignadas</b>, debe asignarle al menos una.";
			}
		
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		
//			if (currentStep == 2) {
//					for(EventPlanTarea eventPlanTarea: this.listEventPlanTarea){
//						if(eventPlanTarea.getListPlanTareaVoluntarios().size()>0 & eventPlanTarea.getListPlanTareaVoluntarios() != null){
//							for(EventPlanTareaVoluntario eventPlanTareaVoluntario: eventPlanTarea.getListPlanTareaVoluntarios()){
//								EventPlanTareaVoluntario obj = new EventPlanTareaVoluntario();
//								obj.setFkEventPlanTarea(new EventPlanTarea(eventPlanTareaVoluntario.getFkEventPlanTarea().getIdEventPlanTarea()));
//								obj.setFkVoluntario(eventPlanTareaVoluntario.getFkVoluntario());
//								PayloadEventPlanTareaVoluntarioResponse eventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService.incluir(obj);
//								if (!UtilPayload.isOK(eventPlanTareaVoluntarioResponse)) {
//									 return (String) eventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE);
//								 }
//								
//							}
//						}
//					   if(eventPlanTarea.getListEventPlanTareaTrabajadors().size()>0 & eventPlanTarea.getListEventPlanTareaTrabajadors() != null){
//						 for(EventPlanTareaTrabajador eventPlanTareaTrabajador: eventPlanTarea.getListEventPlanTareaTrabajadors()){
//							 EventPlanTareaTrabajador objT = new EventPlanTareaTrabajador();
//							 objT.setFkEventPlanTarea(new EventPlanTarea(eventPlanTareaTrabajador.getFkEventPlanTarea().getIdEventPlanTarea()));
//							 objT.setFkTrabajador(eventPlanTareaTrabajador.getFkTrabajador());
//							 PayloadEventPlanTareaTrabajadorResponse eventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService.incluir(objT);
//							 if (!UtilPayload.isOK(eventPlanTareaTrabajadorResponse)) {
//								 return (String) eventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE);
//							 }
//						 }
//					   }
//					}
//				}
			

		goToNextStep();
		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			if (currentStep == 2) {
				PayloadEventPlanTareaTrabajadorResponse payloadEventTsTareaTrabajadorResponse = new PayloadEventPlanTareaTrabajadorResponse();
				PayloadEventPlanTareaVoluntarioResponse payloadEventTsTareaVoluntarioResponse = new PayloadEventPlanTareaVoluntarioResponse();
				for (EventPlanTarea obj : this.listEventPlanTarea) {

					for (EventPlanTareaTrabajador EventPlanTareaTrabajador : obj
							.getListEventPlanTareaTrabajadors()) {
						EventPlanTareaTrabajador eventoTareaPlanTrabajador2 = new EventPlanTareaTrabajador();
						eventoTareaPlanTrabajador2
								.setFkTrabajador(EventPlanTareaTrabajador
										.getFkTrabajador());
						eventoTareaPlanTrabajador2
								.setFkEventPlanTarea(new EventPlanTarea(obj
										.getIdEventPlanTarea()));

						eventoTareaPlanTrabajador2
								.setIdEventPlanTareaTrabajador(EventPlanTareaTrabajador
										.getIdEventPlanTareaTrabajador());
						if (eventoTareaPlanTrabajador2
								.getIdEventPlanTareaTrabajador() == null) {

							payloadEventTsTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
									.incluir(eventoTareaPlanTrabajador2);
						} else {
							payloadEventTsTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
									.modificar(eventoTareaPlanTrabajador2);
						}

						if (!UtilPayload
								.isOK(payloadEventTsTareaTrabajadorResponse)) {
							return (String) payloadEventTsTareaTrabajadorResponse
									.getInformacion(IPayloadResponse.MENSAJE);
						}
						if (EventPlanTareaTrabajador.getFkTrabajador()
								.getFkPersona().getFkUsuario() != null) {
							String contenido = "Se le ha asignado la Tarea "
									+ obj.getFkTarea().getNombre()
									+ " del Evento "
									+ obj.getFkEventoPlanificado().getFkEvento()
											.getNombre()
									+ " que debe realizar el "
									+ UtilConverterDataList.convertirLongADate(obj
											.getFechaPlanificada());
							NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
									EventPlanTareaTrabajador.getFkTrabajador()
											.getFkPersona().getFkUsuario(),
									new Date().getTime(), obj.getFkEventoPlanificado()
											.getIdEventoPlanificado(),
									EstatusNotificacionEnum.PENDIENTE.ordinal(),
									TipoReferenciaNotificacionEnum.ACTIVIDAD
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

					for (EventPlanTareaVoluntario eventPlanTareaVoluntario : obj
							.getListPlanTareaVoluntarios()) {
						EventPlanTareaVoluntario eventPlanVoluntario2 = new EventPlanTareaVoluntario();

						eventPlanVoluntario2
								.setFkEventPlanTarea(new EventPlanTarea(obj
										.getIdEventPlanTarea()));

						eventPlanVoluntario2
								.setFkVoluntario(eventPlanTareaVoluntario
										.getFkVoluntario());
						eventPlanVoluntario2
								.setIdEventPlanTareaVoluntario(eventPlanTareaVoluntario
										.getIdEventPlanTareaVoluntario());
						if (eventPlanVoluntario2
								.getIdEventPlanTareaVoluntario() == null) {

							payloadEventTsTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
									.incluir(eventPlanVoluntario2);
						} else {
							payloadEventTsTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
									.modificar(eventPlanVoluntario2);
						}

						if (!UtilPayload
								.isOK(payloadEventTsTareaVoluntarioResponse)) {
							return (String) payloadEventTsTareaVoluntarioResponse
									.getInformacion(IPayloadResponse.MENSAJE);
						}

						if (eventPlanTareaVoluntario.getFkVoluntario()
								.getFkPersona().getFkUsuario() != null) {
							String contenido = "Se le ha asignado la Tarea "
									+ obj.getFkTarea().getNombre()
									+ " del Evento "
									+ obj.getFkEventoPlanificado().getFkEvento()
											.getNombre()
									+ " que debe realizar el "
									+ UtilConverterDataList.convertirLongADate(obj
											.getFechaPlanificada());
							NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
									eventPlanTareaVoluntario.getFkVoluntario()
											.getFkPersona().getFkUsuario(),
									new Date().getTime(), obj.getFkEventoPlanificado()
											.getIdEventoPlanificado(),
									EstatusNotificacionEnum.PENDIENTE.ordinal(),
									TipoReferenciaNotificacionEnum.ACTIVIDAD
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
				for (EventPlanTareaTrabajador evePlanTareaTrabajador : this
						.listEventPlanTareaTrabajadors) {
					if (evePlanTareaTrabajador.getIdEventPlanTareaTrabajador() != null) {
						payloadEventTsTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
								.eliminar(evePlanTareaTrabajador
										.getIdEventPlanTareaTrabajador());
					}

				}

				for (EventPlanTareaVoluntario eventoPlanVoluntario : this
						.listevPlanTareaVoluntarios) {
					if (eventoPlanVoluntario.getIdEventPlanTareaVoluntario() != null) {
						payloadEventTsTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
								.eliminar(eventoPlanVoluntario
										.getIdEventPlanTareaVoluntario());
					}

				}
			}


		}
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.listEventPlanTarea.clear();
		this.listEventPlanTareaTrabajadors.clear();
		this.listevPlanTareaVoluntarios.clear();
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	public EventoPlanificado getEventoPlanificadoSelected() {
		return (EventoPlanificado) this.getSelectedObject();
	}

	public List<EventPlanTareaTrabajador> getListEventPlanTareaTrabajadors() {
		return listEventPlanTareaTrabajadors;
	}

	public void setListEventPlanTareaTrabajadors(
			List<EventPlanTareaTrabajador> listEventPlanTareaTrabajadors) {
		this.listEventPlanTareaTrabajadors = listEventPlanTareaTrabajadors;
	}

	public List<EventPlanTareaVoluntario> getListevPlanTareaVoluntarios() {
		return listevPlanTareaVoluntarios;
	}

	public void setListevPlanTareaVoluntarios(
			List<EventPlanTareaVoluntario> listevPlanTareaVoluntarios) {
		this.listevPlanTareaVoluntarios = listevPlanTareaVoluntarios;
	}
	@Override
	public String executeCustom1(Integer currentStep) {
		executeCancelar(currentStep);
		return "";

	}
	
}
