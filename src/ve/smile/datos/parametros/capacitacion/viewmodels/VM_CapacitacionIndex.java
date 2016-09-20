package ve.smile.datos.parametros.capacitacion.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Capacitacion;
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

}
