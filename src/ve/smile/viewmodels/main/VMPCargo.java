package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Cargo;
import ve.smile.payload.response.PayloadCargoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPCargo extends VM_WindowSimpleListPrincipal<Cargo> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Cargo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCargoResponse payloadCargoResponse = 
				S.CargoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCargoResponse;
	}

	@Override
	public void doDelete() {
		PayloadCargoResponse payloadCargoResponse =
				S.CargoService.eliminar(getSelectedObject().getIdCargo());

		Alert.showMessage(payloadCargoResponse);

		if (UtilPayload.isOK(payloadCargoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/CargoFormBasic.zul";
	}

}
