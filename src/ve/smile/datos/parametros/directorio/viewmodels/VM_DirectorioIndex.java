package ve.smile.datos.parametros.directorio.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_DirectorioIndex extends
		VM_WindowSimpleListPrincipal<Directorio> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Directorio> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadDirectorioResponse;
	}

	@Override
	public void doDelete() {
		PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
				.eliminar(getSelectedObject().getIdDirectorio());

		Alert.showMessage(payloadDirectorioResponse);

		if (UtilPayload.isOK(payloadDirectorioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/directorio/DirectorioFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Directorio";
		}

		Map<String, String> criterios = new HashMap<String, String>();

		criterios.put("fkDirectorio.idDirectorio",
				String.valueOf(getSelectedObject().getIdDirectorio()));

		// Table Relation CapacitacionPlanificada
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService
				.contarCriterios(TypeQuery.EQUAL, criterios);

		if (!UtilPayload.isOK(payloadCapacitacionPlanificadaResponse)) {
			return String.valueOf(payloadCapacitacionPlanificadaResponse
					.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCapacitacionesPlanificadas = Double.valueOf(
				String.valueOf(payloadCapacitacionPlanificadaResponse
						.getInformacion(IPayloadResponse.COUNT))).intValue();

		// Table Relation EventPlanTarea
		PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService
				.contarCriterios(TypeQuery.EQUAL, criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaResponse)) {
			return String.valueOf(payloadEventPlanTareaResponse
					.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventosPlanificadosTareas = Double.valueOf(
				String.valueOf(payloadEventPlanTareaResponse
						.getInformacion(IPayloadResponse.COUNT))).intValue();

		// Table Relation EventoPlanificado
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.contarCriterios(TypeQuery.EQUAL, criterios);

		if (!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
			return String.valueOf(payloadEventoPlanificadoResponse
					.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventosPlanificados = Double.valueOf(
				String.valueOf(payloadEventoPlanificadoResponse
						.getInformacion(IPayloadResponse.COUNT))).intValue();

		// Table Relation TsPlan
		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.contarCriterios(TypeQuery.EQUAL, criterios);

		if (!UtilPayload.isOK(payloadTsPlanResponse)) {
			return String.valueOf(payloadTsPlanResponse
					.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanificados = Double.valueOf(
				String.valueOf(payloadTsPlanResponse
						.getInformacion(IPayloadResponse.COUNT))).intValue();

		// Table Relation TsPlanActividad
		PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
				.contarCriterios(TypeQuery.EQUAL, criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
			return String.valueOf(payloadTsPlanActividadResponse
					.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanActividades = Double.valueOf(
				String.valueOf(payloadTsPlanActividadResponse
						.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countTsPlanActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Directorio</b> seleccionado ya que est� siendo utilizado en "
					+ countTsPlanActividades
					+ " Actividades de Trabajos Sociales Planificados";
		}

		if (countTsPlanificados > 0) {
			return "E:Error 0:No se puede eliminar el <b>Directorio</b> seleccionado ya que est� siendo utilizado en "
					+ countTsPlanificados + " Trabajos Sociales Planificados";
		}

		if (countEventosPlanificados > 0) {
			return "E:Error 0:No se puede eliminar el <b>Directorio</b> seleccionado ya que est� siendo utilizado en "
					+ countEventosPlanificados + " Eventos Planificados";
		}

		if (countEventosPlanificadosTareas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Directorio</b> seleccionado ya que est� siendo utilizado en "
					+ countEventosPlanificadosTareas
					+ " Tareas de Eventos Planificados";
		}

		if (countCapacitacionesPlanificadas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Directorio</b> seleccionado ya que est� siendo utilizado en "
					+ countCapacitacionesPlanificadas
					+ " Capacitaciones Planificadas";
		}

		return "";
	}

}
