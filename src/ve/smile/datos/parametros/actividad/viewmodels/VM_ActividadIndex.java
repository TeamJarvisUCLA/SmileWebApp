package ve.smile.datos.parametros.actividad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ActividadIndex extends VM_WindowSimpleListPrincipal<Actividad> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Actividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadActividadResponse payloadActividadResponse = S.ActividadService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadActividadResponse payloadActividadResponse = S.ActividadService
				.eliminar(getSelectedObject().getIdActividad());

		Alert.showMessage(payloadActividadResponse);

		if (UtilPayload.isOK(payloadActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/actividad/ActividadFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Actividad";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkActividad.idActividad", String.valueOf(getSelectedObject().getIdActividad()));

		//Table Relation TsPlanActividad
		PayloadTsPlanActividadResponse payloadTsPlanActividadResponse =
				S.TsPlanActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadResponse)) {
			return String.valueOf(payloadTsPlanActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanActividades = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadRecurso
		PayloadTsPlanActividadRecursoResponse payloadTsPlanActividadRecursoResponse =
				S.TsPlanActividadRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadRecursoResponse)) {
			return String.valueOf(payloadTsPlanActividadRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanActividadRecursos = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadTrabajador
		PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse =
				S.TsPlanActividadTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
			return String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanActividadTrabajadores = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadVoluntario
		PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse =
				S.TsPlanActividadVoluntarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
			return String.valueOf(payloadTsPlanActividadVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTsPlanActividadVoluntarios = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadVoluntarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countTsPlanActividades > 0) {
			return "E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está siendo utilizada en " + countTsPlanActividades + " Trabajo Social Planificado";
		}
		
		if (countTsPlanActividadRecursos > 0) {
			return "E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está siendo utilizada en " + countTsPlanActividadRecursos + " Trabajo Social Planificado Recurso";
		}
		
		if (countTsPlanActividadTrabajadores > 0) {
			return "E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está siendo utilizada en " + countTsPlanActividadTrabajadores + " Trabajo Social Planificado Trabajador";
		}
		
		if (countTsPlanActividadVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar la <b>Actividad</b> seleccionada ya que está siendo utilizada en " + countTsPlanActividadVoluntarios + " Trabajo Social Planificado Recurso";
		}

		return "";
	}

}
