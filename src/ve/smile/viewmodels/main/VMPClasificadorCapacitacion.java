package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorCapacitacion;
import ve.smile.payload.response.PayloadClasificadorCapacitacionResponse;

import org.zkoss.bind.annotation.Init;

public class VMPClasificadorCapacitacion extends VM_WindowSimpleListPrincipal<ClasificadorCapacitacion> {

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
		return "views/desktop/maestro/view/ClasificadorCapacitacionFormBasic.zul";
	}

}
