package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Estado;
import ve.smile.payload.response.PayloadEstadoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPEstado extends VM_WindowSimpleListPrincipal<Estado> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Estado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEstadoResponse payloadEstadoResponse = 
				S.EstadoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEstadoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEstadoResponse payloadEstadoResponse =
				S.EstadoService.eliminar(getSelectedObject().getIdEstado());

		Alert.showMessage(payloadEstadoResponse);

		if (UtilPayload.isOK(payloadEstadoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/EstadoFormBasic.zul";
	}

}
