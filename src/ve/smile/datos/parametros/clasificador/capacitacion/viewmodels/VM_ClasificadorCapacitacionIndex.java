package ve.smile.datos.parametros.clasificador.capacitacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorCapacitacion;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.payload.response.PayloadClasificadorCapacitacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorCapacitacionIndex extends VM_WindowSimpleListPrincipal<ClasificadorCapacitacion> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorCapacitacion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorCapacitacionResponse payloadClasificadorCapacitacionResponse = 
				S.ClasificadorCapacitacionService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorCapacitacionResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorCapacitacionResponse payloadClasificadorCapacitacionResponse =
				S.ClasificadorCapacitacionService.eliminar(getSelectedObject().getIdClasificadorCapacitacion());

		Alert.showMessage(payloadClasificadorCapacitacionResponse);

		if (UtilPayload.isOK(payloadClasificadorCapacitacionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/capacitacion/ClasificadorCapacitacionFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorCapacitacion.idClasificadorCapacitacion", String.valueOf(getSelectedObject().getIdClasificadorCapacitacion()));

		//Table Relation Capacitacion
		PayloadCapacitacionResponse payloadCapacitacionResponse =
				S.CapacitacionService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadCapacitacionResponse)) {
			return String.valueOf(payloadCapacitacionResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCapacitaciones = 
				Double.valueOf(String.valueOf(payloadCapacitacionResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countCapacitaciones > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que est� siendo utilizado en " + countCapacitaciones + " Registros de Capacitaciones";
		}

		return "";
	}

}
