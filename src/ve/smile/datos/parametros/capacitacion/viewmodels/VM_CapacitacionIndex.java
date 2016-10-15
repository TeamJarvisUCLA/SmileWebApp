package ve.smile.datos.parametros.capacitacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Capacitacion;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CapacitacionIndex extends
VM_WindowSimpleListPrincipal<Capacitacion> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Capacitacion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCapacitacionResponse;
	}

	@Override
	public void doDelete() {
		PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService
				.eliminar(getSelectedObject().getIdCapacitacion());

		Alert.showMessage(payloadCapacitacionResponse);

		if (UtilPayload.isOK(payloadCapacitacionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/capacitacion/CapacitacionFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Capacitación";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkCapacitacion.idCapacitacion", String.valueOf(getSelectedObject().getIdCapacitacion()));

		//Table Relation CapacitacionPlanificada
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse =
				S.CapacitacionPlanificadaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadCapacitacionPlanificadaResponse)) {
			return String.valueOf(payloadCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCapacitacionesPlanificadas = 
				Double.valueOf(String.valueOf(payloadCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countCapacitacionesPlanificadas > 0) {
			return "E:Error 0:No se puede eliminar la <b>Capacitación</b> seleccionada ya que está siendo utilizada en " + countCapacitacionesPlanificadas + " Capacitaciones Planificadas";
		}

		return "";
	}


}