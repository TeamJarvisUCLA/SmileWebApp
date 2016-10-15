package ve.smile.datos.parametros.recurso.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaRecursoResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanActividadRecursoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RecursoIndex extends VM_WindowSimpleListPrincipal<Recurso> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Recurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRecursoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.eliminar(getSelectedObject().getIdRecurso());

		Alert.showMessage(payloadRecursoResponse);

		if (UtilPayload.isOK(payloadRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/recurso/RecursoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Recurso";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkRecurso.idRecurso", String.valueOf(getSelectedObject().getIdRecurso()));

		//Table Relation DonativoRecurso
		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse =
				S.DonativoRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadDonativoRecursoResponse)) {
			return String.valueOf(payloadDonativoRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countDonativos = 
				Double.valueOf(String.valueOf(payloadDonativoRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTareaRecurso
		PayloadEventPlanTareaRecursoResponse payloadEventPlanTareaRecursoResponse =
				S.EventPlanTareaRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaRecursoResponse)) {
			return String.valueOf(payloadEventPlanTareaRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareas = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation SolicitudAyudaRecurso
		PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse =
				S.SolicitudAyudaRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
			return String.valueOf(payloadSolicitudAyudaRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countAyudas = 
				Double.valueOf(String.valueOf(payloadSolicitudAyudaRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlanActividadRecurso
		PayloadTsPlanActividadRecursoResponse payloadTsPlanActividadRecursoResponse =
				S.TsPlanActividadRecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanActividadRecursoResponse)) {
			return String.valueOf(payloadTsPlanActividadRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividades = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Recurso</b> seleccionado ya que está siendo utilizado en " + countActividades + " Actividades de Trabajo Social";
		}

		if (countAyudas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Recurso</b> seleccionado ya que está siendo utilizado en " + countAyudas + " Solicitudes de Ayuda";
		}

		if (countTareas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Recurso</b> seleccionado ya que está siendo utilizado en " + countTareas + " Tareas en Eventos";
		}

		if (countDonativos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Recurso</b> seleccionado ya que está siendo utilizado en " + countDonativos + " Donativos";
		}

		return "";
	}

}
