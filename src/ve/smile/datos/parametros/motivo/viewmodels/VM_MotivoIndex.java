package ve.smile.datos.parametros.motivo.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Motivo;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIncidenciaEventoPlanResponse;
import ve.smile.payload.response.PayloadIncidenciaTsPlanResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_MotivoIndex extends VM_WindowSimpleListPrincipal<Motivo> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Motivo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadMotivoResponse;
	}

	@Override
	public void doDelete() {
		PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
				.eliminar(getSelectedObject().getIdMotivo());

		Alert.showMessage(payloadMotivoResponse);

		if (UtilPayload.isOK(payloadMotivoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/motivo/MotivoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Motivo";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkMotivo.idMotivo", String.valueOf(getSelectedObject().getIdMotivo()));

		//Table Relation CapacitacionPlanificada
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse =
				S.CapacitacionPlanificadaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadCapacitacionPlanificadaResponse)) {
			return String.valueOf(payloadCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCapacitaciones = 
				Double.valueOf(String.valueOf(payloadCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTareaTrabajador
		PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse =
				S.EventPlanTareaTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
			return String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareasTrabajador = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTareaVoluntario
		PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse =
				S.EventPlanTareaVoluntarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
			return String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareasVoluntarios = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTarea
		PayloadEventPlanTareaResponse payloadEventPlanTareaResponse =
				S.EventPlanTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaResponse)) {
			return String.valueOf(payloadEventPlanTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareas = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventoPlanificado
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse =
				S.EventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
			return String.valueOf(payloadEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventos = 
				Double.valueOf(String.valueOf(payloadEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorEventoPlanificado
		PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse =
				S.IndicadorEventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorEventoPlanificadoResponse)) {
			return String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadoresEvento = 
				Double.valueOf(String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorEventoPlanTarea
		PayloadIndicadorEventoPlanTareaResponse payloadIndicadorEventoPlanTareaResponse =
				S.IndicadorEventoPlanTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorEventoPlanTareaResponse)) {
			return String.valueOf(payloadIndicadorEventoPlanTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadoresTareasEvento = 
				Double.valueOf(String.valueOf(payloadIndicadorEventoPlanTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorTsPlan
		PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse =
				S.IndicadorTsPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
			return String.valueOf(payloadIndicadorTsPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadoresTs = 
				Double.valueOf(String.valueOf(payloadIndicadorTsPlanResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorTsPlanActividad
		PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse =
				S.IndicadorTsPlanActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorTsPlanActividadResponse)) {
			return String.valueOf(payloadIndicadorTsPlanActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadoresActividades = 
				Double.valueOf(String.valueOf(payloadIndicadorTsPlanActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation SolicitudAyuda
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse =
				S.SolicitudAyudaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			return String.valueOf(payloadSolicitudAyudaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countSolicitudes = 
				Double.valueOf(String.valueOf(payloadSolicitudAyudaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();


		//Table Relation TsPlan
		PayloadTsPlanResponse payloadTsPlanResponse =
				S.TsPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanResponse)) {
			return String.valueOf(payloadTsPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTs = 
				Double.valueOf(String.valueOf(payloadSolicitudAyudaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividad
		PayloadTsPlanActividadResponse payloadTsPlanActividadResponse =
				S.TsPlanActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
			return String.valueOf(payloadTsPlanActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividades = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadTrabajador
		PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse =
				S.TsPlanActividadTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
			return String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividadesTrabajador = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadVoluntario
		PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse =
				S.TsPlanActividadVoluntarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
			return String.valueOf(payloadTsPlanActividadVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividadesVoluntario = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadVoluntarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsVolCapacitacionPlanificada
		PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse =
				S.VolCapacitacionPlanificadaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadVolCapacitacionPlanificadaResponse)) {
			return String.valueOf(payloadVolCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCapacitacionesVoluntarios = 
				Double.valueOf(String.valueOf(payloadVolCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IncidenciaTsPlan
		PayloadIncidenciaTsPlanResponse payloadIncidenciaTsPlanResponse =
				S.IncidenciaTsPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIncidenciaTsPlanResponse)) {
			return String.valueOf(payloadIncidenciaTsPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIncidenciasTs = 
				Double.valueOf(String.valueOf(payloadIncidenciaTsPlanResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IncidenciaEventoPlan
		PayloadIncidenciaEventoPlanResponse payloadIncidenciaEventoPlanResponse =
				S.IncidenciaEventoPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIncidenciaEventoPlanResponse)) {
			return String.valueOf(payloadIncidenciaEventoPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIncidenciasEventos = 
				Double.valueOf(String.valueOf(payloadIncidenciaEventoPlanResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countIncidenciasEventos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIncidenciasEventos + " Incidencias de Eventos";
		}

		if (countIncidenciasTs > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIncidenciasTs + " Incidencias de Trabajos Sociales";
		}

		if (countCapacitacionesVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countCapacitacionesVoluntarios + " Capacitaciones para Voluntarios";
		}

		if (countActividadesVoluntario > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countActividadesVoluntario + " Actividades de Voluntarios";
		}

		if (countActividadesTrabajador > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countActividadesTrabajador + " Actividades de Trabajadores";
		}

		if (countActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countActividades + " Actividades de Trabajo Social";
		}

		if (countTs > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countTs + " Trabajos Sociales Planificados";
		}

		if (countSolicitudes > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countSolicitudes + " Solicitudes de Ayuda";
		}

		if (countIndicadoresActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIndicadoresActividades + " Indicadores de Actividades";
		}

		if (countIndicadoresTs > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIndicadoresTs + " Indicadores del Trabajo Social";
		}

		if (countIndicadoresTareasEvento > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIndicadoresTareasEvento + " Indicadores de Tareas de Eventos";
		}

		if (countIndicadoresEvento > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countIndicadoresEvento + " Indicadores de Eventos";
		}

		if (countEventos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countEventos + " Eventos Planificados";
		}

		if (countTareas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countTareas + " Tareas de Eventos";
		}

		if (countTareasVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countTareasVoluntarios + " Tareas de los Voluntarios";
		}

		if (countTareasTrabajador > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countTareasTrabajador + " Tareas de los Trabajadores";
		}

		if (countCapacitaciones > 0) {
			return "E:Error 0:No se puede eliminar el <b>Motivo/b> seleccionado ya que está siendo utilizado en " + countCapacitaciones + " Capacitaciones";
		}

		return "";
	}
}
