package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.UnidadMedida;
import ve.smile.payload.response.PayloadUnidadMedidaResponse;

import org.zkoss.bind.annotation.Init;

public class VMPUnidadMedida extends VM_WindowSimpleListPrincipal<UnidadMedida> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<UnidadMedida> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadUnidadMedidaResponse payloadUnidadMedidaResponse = 
				S.UnidadMedidaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadUnidadMedidaResponse;
	}

	@Override
	public void doDelete() {
		PayloadUnidadMedidaResponse payloadUnidadMedidaResponse =
				S.UnidadMedidaService.eliminar(getSelectedObject().getIdUnidadMedida());

		Alert.showMessage(payloadUnidadMedidaResponse);

		if (UtilPayload.isOK(payloadUnidadMedidaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/UnidadMedidaFormBasic.zul";
	}

}
