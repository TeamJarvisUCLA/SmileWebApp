package ve.smile.datos.parametros.tarea.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadEventPlanTareaRecursoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TareaIndex extends VM_WindowSimpleListPrincipal<Tarea> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Tarea> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTareaResponse payloadTareaResponse = S.TareaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTareaResponse;
	}

	@Override
	public void doDelete() {
		PayloadTareaResponse payloadTareaResponse = S.TareaService
				.eliminar(getSelectedObject().getIdTarea());

		Alert.showMessage(payloadTareaResponse);

		if (UtilPayload.isOK(payloadTareaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/tarea/TareaFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Tarea";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkTarea.idTarea", String.valueOf(getSelectedObject().getIdTarea()));

		//Table Relation event_plan_tarea
		PayloadEventPlanTareaResponse payloadEventPlanTareaResponse =
				S.EventPlanTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaResponse)) {
			return String.valueOf(payloadEventPlanTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventos = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//event_plan_tarea_recurso
		PayloadEventPlanTareaRecursoResponse payloadEventPlanTareaRecursoResponse =
				S.EventPlanTareaRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaRecursoResponse)) {
			return String.valueOf(payloadEventPlanTareaRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countRecursos = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//event_plan_tarea_trabajador
		PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse =
				S.EventPlanTareaTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
			return String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajadores = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//event_plan_tarea_voluntario
		PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse =
				S.EventPlanTareaVoluntarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
			return String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countVoluntarios = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//plantilla_ts_actividad
		PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse =
				S.PlantillaTrabajoSocialActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialActividadResponse)) {
			return String.valueOf(payloadPlantillaTrabajoSocialActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillaActividades = 
				Double.valueOf(String.valueOf(payloadPlantillaTrabajoSocialActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//plantilla_evento_tarea
		PayloadPlantillaEventoTareaResponse payloadPlantillaEventoTareaResponse =
				S.PlantillaEventoTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaEventoTareaResponse)) {
			return String.valueOf(payloadPlantillaEventoTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillaTareas = 
				Double.valueOf(String.valueOf(payloadPlantillaEventoTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPlantillaTareas > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countPlantillaTareas + " Plantillas de Tareas para Eventos";
		}

		if (countPlantillaActividades > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countPlantillaActividades + " Plantillas de Actividades para el Trabajo Social";
		}

		if (countVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countVoluntarios + " Eventos Planificados";
		}

		if (countTrabajadores > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countTrabajadores + " Eventos Planificados";
		}

		if (countRecursos > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countRecursos + " Eventos Planificados";
		}

		if (countEventos > 0) {
			return "E:Error 0:No se puede eliminar la <b>Tarea</b> seleccionada ya que está siendo utilizada en " + countEventos + " Eventos Planificados";
		}

		return "";
	}
}
