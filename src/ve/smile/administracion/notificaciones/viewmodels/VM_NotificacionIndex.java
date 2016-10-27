package ve.smile.administracion.notificaciones.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.EventPlanTareaTrabajador;
import ve.smile.dto.EventPlanTareaVoluntario;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividadTrabajador;
import ve.smile.dto.TsPlanActividadVoluntario;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_NotificacionIndex extends
		VM_WindowSimpleListPrincipal<NotificacionUsuario> {

	private EstatusNotificacionEnum estatusNotificacionEnum;
	private TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum;

	private List<EstatusNotificacionEnum> estatusNotificacionEnums;
	private List<TipoReferenciaNotificacionEnum> tipoReferenciaNotificacionEnums;

	private EventoPlanificado eventoPlanificado = new EventoPlanificado();
	private TsPlan tsPlan = new TsPlan();
	private TsPlanActividadTrabajador tsPlanActividadTrabajador = new TsPlanActividadTrabajador();

	private TsPlanActividadVoluntario tsplanActividadVoluntario = new TsPlanActividadVoluntario();

	private EventPlanTareaTrabajador eventPlanTareaTrabajador = new EventPlanTareaTrabajador();

	private EventPlanTareaVoluntario eventPlanTareaVoluntario = new EventPlanTareaVoluntario();

	private CapacitacionPlanificada capacitacionPlanificada = new CapacitacionPlanificada();

	private ReconocimientoPersona reconocimientoPersona = new ReconocimientoPersona();

	@Init(superclass = true)
	public void childInit() {
		estatusNotificacionEnum = EstatusNotificacionEnum.PENDIENTE;
	}

	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<NotificacionUsuario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		String correo = String.valueOf(DataCenter.getUserSecurityData()
				.getUsuario().getIdUsuario());
		Map<String, String> criterios = new HashMap<String, String>();
		if (this.getEstatusNotificacionEnum() != null) {
			criterios.put("estatusNotificacion",
					String.valueOf(this.estatusNotificacionEnum.ordinal()));
		}
		if (this.getTipoReferenciaNotificacionEnum() != null) {
			criterios.put("tipoReferenciaNotificacion", String
					.valueOf(this.tipoReferenciaNotificacionEnum.ordinal()));
		}
		criterios.put("fkUsuario.idUsuario", correo);
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.ILIKE, criterios);
		return payloadNotificacionUsuarioResponse;
	}

	// VALIDACION
	@Override
	public String isValidSearchDataModificar() {
		return super.isValidSearchDataModificar();
	}

	@Override
	public void executeConsultar() {
	}

	// ELIMINAR
	@Override
	public void doDelete() {
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
				.eliminar(getSelectedObject().getIdNotificacionUsuario());
		Alert.showMessage(payloadNotificacionUsuarioResponse);
		if (UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	// FORMULARIO
	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/difusion/notificaciones/NotificacionFormBasic.zul";
	}

	// FILTRO
	@Command
	public void changeFilter() {
		super.getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
	}

	// ESTATUS NOTIFICACION
	public List<EstatusNotificacionEnum> getEstatusNotificacionEnums() {
		if (this.estatusNotificacionEnums == null) {
			this.estatusNotificacionEnums = new ArrayList<>();
		}
		if (this.estatusNotificacionEnums.isEmpty()) {
			for (EstatusNotificacionEnum estatusNotificacionEnum : EstatusNotificacionEnum
					.values()) {
				this.estatusNotificacionEnums.add(estatusNotificacionEnum);
			}
		}
		return estatusNotificacionEnums;
	}

	public void setEstatusNotificacionEnums(
			List<EstatusNotificacionEnum> estatusNotificacionEnums) {
		this.estatusNotificacionEnums = estatusNotificacionEnums;
	}

	public EstatusNotificacionEnum getEstatusNotificacionEnum() {
		return estatusNotificacionEnum;
	}

	public void setEstatusNotificacionEnum(
			EstatusNotificacionEnum estatusNotificacionEnum) {
		this.estatusNotificacionEnum = estatusNotificacionEnum;
	}

	@Command("changeNotificacion")
	@NotifyChange({ "tsPlan, tsPlanActividadVoluntario, tsPlanActividadTrabajador, eventoPlanificado " })
	public void changeNotificacion() {
		eventoPlanificado = new EventoPlanificado();
		tsPlan = new TsPlan();
		tsPlanActividadTrabajador = new TsPlanActividadTrabajador();

		tsplanActividadVoluntario = new TsPlanActividadVoluntario();

		eventPlanTareaTrabajador = new EventPlanTareaTrabajador();

		eventPlanTareaVoluntario = new EventPlanTareaVoluntario();

		capacitacionPlanificada = new CapacitacionPlanificada();

		reconocimientoPersona = new ReconocimientoPersona();
		if (this.selectedObject.getEstatusNotificacion().equals(
				EstatusNotificacionEnum.PENDIENTE.ordinal())) {
			this.selectedObject
					.setEstatusNotificacionEnum(EstatusNotificacionEnum.LEIDA);
			PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
					.modificar(selectedObject);
			if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
				Alert.showMessage(payloadNotificacionUsuarioResponse);
				return;
			}
		}

		if (selectedObject.getTipoReferenciaNotificacionEnum().equals(
				TipoReferenciaNotificacionEnum.EVENTO)) {
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultarUno(this.selectedObject.getReferenciaGenericoId());
			if (!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				Alert.showMessage(payloadEventoPlanificadoResponse);
				return;
			}
			if (payloadEventoPlanificadoResponse.getObjetos() != null
					&& payloadEventoPlanificadoResponse.getObjetos().size() > 0) {
				this.setEventoPlanificado(payloadEventoPlanificadoResponse
						.getObjetos().get(0));

			}

		}

		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.TRABAJO_SOCIAL.ordinal())) {
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultarUno(this.selectedObject.getReferenciaGenericoId());
			if (!UtilPayload.isOK(payloadTsPlanResponse)) {
				Alert.showMessage(payloadTsPlanResponse);
				return;
			}
			if (payloadTsPlanResponse.getObjetos() != null
					&& payloadTsPlanResponse.getObjetos().size() > 0) {
				this.setTsPlan(payloadTsPlanResponse.getObjetos().get(0));
			}

		}

		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.RECONOCIMIENTO.ordinal())) {
			PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService
					.consultarUno(this.selectedObject.getReferenciaGenericoId());
			if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
				Alert.showMessage(payloadReconocimientoPersonaResponse);
				return;
			}
			if (payloadReconocimientoPersonaResponse.getObjetos() != null
					&& payloadReconocimientoPersonaResponse.getObjetos().size() > 0) {
				this.setReconocimientoPersona(payloadReconocimientoPersonaResponse
						.getObjetos().get(0));
			}

		}

		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.CAPACITACION.ordinal())) {
			PayloadCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService
					.consultarUno(this.selectedObject.getReferenciaGenericoId());
			if (!UtilPayload.isOK(payloadVolCapacitacionPlanificadaResponse)) {
				Alert.showMessage(payloadVolCapacitacionPlanificadaResponse);
				return;
			}
			if (payloadVolCapacitacionPlanificadaResponse.getObjetos() != null
					&& payloadVolCapacitacionPlanificadaResponse.getObjetos()
							.size() > 0) {
				this.setCapacitacionPlanificada(payloadVolCapacitacionPlanificadaResponse
						.getObjetos().get(0));
			}

		}
		HashMap<String, String> criterios = new HashMap<>();
		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.TAREA.ordinal())) {
			criterios = new HashMap<>();
			criterios.put("idEventPlanTareaTrabajador",
					selectedObject.getReferenciaGenericoId() + "");
			criterios.put("fkTrabajador.fkPersona.fkUsuario.idUsuario",
					selectedObject.getFkUsuario().getIdUsuario() + "");
			PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse = S.EventPlanTareaTrabajadorService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
				Alert.showMessage(payloadEventPlanTareaTrabajadorResponse);
				return;
			}
			if (payloadEventPlanTareaTrabajadorResponse.getObjetos() != null
					&& payloadEventPlanTareaTrabajadorResponse.getObjetos()
							.size() > 0) {
				this.setEventPlanTareaTrabajador(payloadEventPlanTareaTrabajadorResponse
						.getObjetos().get(0));
			}
			if (this.getEventPlanTareaTrabajador()
					.getIdEventPlanTareaTrabajador() == null) {
				criterios = new HashMap<>();
				criterios.put("idEventPlanTareaVoluntario",
						selectedObject.getReferenciaGenericoId() + "");
				criterios.put("fkVoluntario.fkPersona.fkUsuario.idUsuario",
						selectedObject.getFkUsuario().getIdUsuario() + "");
				PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse = S.EventPlanTareaVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (!UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
					Alert.showMessage(payloadEventPlanTareaVoluntarioResponse);
					return;
				}
				if (payloadEventPlanTareaVoluntarioResponse.getObjetos() != null
						&& payloadEventPlanTareaVoluntarioResponse.getObjetos()
								.size() > 0) {
					this.setEventPlanTareaVoluntario(payloadEventPlanTareaVoluntarioResponse
							.getObjetos().get(0));
				}
			}

		}

		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.RECONOCIMIENTO.ordinal())) {
			PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService
					.consultarUno(this.selectedObject.getReferenciaGenericoId());
			if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
				Alert.showMessage(payloadReconocimientoPersonaResponse);
				return;
			}
			if (payloadReconocimientoPersonaResponse.getObjetos() != null
					&& payloadReconocimientoPersonaResponse.getObjetos().size() > 0) {
				this.setReconocimientoPersona(payloadReconocimientoPersonaResponse
						.getObjetos().get(0));
			}

		}

		if (selectedObject.getTipoReferenciaNotificacion().equals(
				TipoReferenciaNotificacionEnum.ACTIVIDAD.ordinal())) {
			criterios.put("idTsPlanActividadTrabajador",
					selectedObject.getReferenciaGenericoId() + "");
			criterios.put("fkTrabajador.fkPersona.fkUsuario.idUsuario",
					selectedObject.getFkUsuario().getIdUsuario() + "");
			PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
				Alert.showMessage(payloadTsPlanActividadTrabajadorResponse);
				return;
			}
			if (payloadTsPlanActividadTrabajadorResponse.getObjetos() != null
					&& payloadTsPlanActividadTrabajadorResponse.getObjetos()
							.size() > 0) {
				this.setTsPlanActividadTrabajador(payloadTsPlanActividadTrabajadorResponse
						.getObjetos().get(0));
			}
			if (this.getTsPlanActividadTrabajador()
					.getIdTsPlanActividadTrabajador() == null) {
				criterios = new HashMap<>();
				criterios.put("idTsPlanActividadVoluntario",
						selectedObject.getReferenciaGenericoId() + "");
				criterios.put("fkVoluntario.fkPersona.fkUsuario.idUsuario",
						selectedObject.getFkUsuario().getIdUsuario() + "");
				PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (!UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
					Alert.showMessage(payloadTsPlanActividadVoluntarioResponse);
					return;
				}
				if (payloadTsPlanActividadVoluntarioResponse.getObjetos() != null
						&& payloadTsPlanActividadVoluntarioResponse
								.getObjetos().size() > 0) {
					this.setTsplanActividadVoluntario(payloadTsPlanActividadVoluntarioResponse
							.getObjetos().get(0));
				}
			}

		}

		HashMap<String, String> criterio = new HashMap<>();
		criterio.put(
				"fkUsuario.idUsuario",
				String.valueOf(DataCenter.getUserSecurityData().getUsuario()
						.getIdUsuario()));

		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse1 = S.NotificacionUsuarioService
				.consultarCriterios(TypeQuery.EQUAL, criterio);
		if (UtilPayload.isOK(payloadNotificacionUsuarioResponse1)
				&& payloadNotificacionUsuarioResponse1.getObjetos() != null) {
			DataCenter
					.getUserSecurityData()
					.getUsuario()
					.setNotificacionUsuarios(
							payloadNotificacionUsuarioResponse1.getObjetos());
		}

		criterio.put("estatusNotificacion",
				String.valueOf(EstatusNotificacionEnum.PENDIENTE.ordinal()));
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
				.consultarCriterios(TypeQuery.EQUAL, criterio);

		if (UtilPayload.isOK(payloadNotificacionUsuarioResponse)
				&& payloadNotificacionUsuarioResponse.getObjetos() != null) {
			DataCenter
					.getUserSecurityData()
					.getUsuario()
					.setNotificacionUsuariosPendientes(
							payloadNotificacionUsuarioResponse.getObjetos());
		}
		BindUtils.postNotifyChange(null, null, this, "*");
		BindUtils.postGlobalCommand(null, null, "updateNotificaciones", null);
	}

	// TIPO REFERENCIA
	public List<TipoReferenciaNotificacionEnum> getTipoReferenciaNotificacionEnums() {
		if (this.tipoReferenciaNotificacionEnums == null) {
			this.tipoReferenciaNotificacionEnums = new ArrayList<>();
		}
		if (this.tipoReferenciaNotificacionEnums.isEmpty()) {
			for (TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum : TipoReferenciaNotificacionEnum
					.values()) {
				this.tipoReferenciaNotificacionEnums
						.add(tipoReferenciaNotificacionEnum);
			}
		}
		return tipoReferenciaNotificacionEnums;
	}

	public void setTipoReferenciaNotificacionEnums(
			List<TipoReferenciaNotificacionEnum> tipoReferenciaNotificacionEnums) {
		this.tipoReferenciaNotificacionEnums = tipoReferenciaNotificacionEnums;
	}

	public TipoReferenciaNotificacionEnum getTipoReferenciaNotificacionEnum() {
		return tipoReferenciaNotificacionEnum;
	}

	public void setTipoReferenciaNotificacionEnum(
			TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum) {
		this.tipoReferenciaNotificacionEnum = tipoReferenciaNotificacionEnum;
	}

	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
	}

	public TsPlanActividadTrabajador getTsPlanActividadTrabajador() {
		return tsPlanActividadTrabajador;
	}

	public void setTsPlanActividadTrabajador(
			TsPlanActividadTrabajador tsPlanActividadTrabajador) {
		this.tsPlanActividadTrabajador = tsPlanActividadTrabajador;
	}

	public TsPlanActividadVoluntario getTsplanActividadVoluntario() {
		return tsplanActividadVoluntario;
	}

	public void setTsplanActividadVoluntario(
			TsPlanActividadVoluntario tsplanActividadVoluntario) {
		this.tsplanActividadVoluntario = tsplanActividadVoluntario;
	}

	public EventPlanTareaTrabajador getEventPlanTareaTrabajador() {
		return eventPlanTareaTrabajador;
	}

	public void setEventPlanTareaTrabajador(
			EventPlanTareaTrabajador eventPlanTareaTrabajador) {
		this.eventPlanTareaTrabajador = eventPlanTareaTrabajador;
	}

	public EventPlanTareaVoluntario getEventPlanTareaVoluntario() {
		return eventPlanTareaVoluntario;
	}

	public void setEventPlanTareaVoluntario(
			EventPlanTareaVoluntario eventPlanTareaVoluntario) {
		this.eventPlanTareaVoluntario = eventPlanTareaVoluntario;
	}

	public CapacitacionPlanificada getCapacitacionPlanificada() {
		return capacitacionPlanificada;
	}

	public void setCapacitacionPlanificada(
			CapacitacionPlanificada capacitacionPlanificada) {
		this.capacitacionPlanificada = capacitacionPlanificada;
	}

	public ReconocimientoPersona getReconocimientoPersona() {
		return reconocimientoPersona;
	}

	public void setReconocimientoPersona(
			ReconocimientoPersona reconocimientoPersona) {
		this.reconocimientoPersona = reconocimientoPersona;
	}

}
