package ve.smile.datos.parametros.estado.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Estado;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EstadoIndex extends VM_WindowSimpleListPrincipal<Estado> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Estado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEstadoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
				.eliminar(getSelectedObject().getIdEstado());

		Alert.showMessage(payloadEstadoResponse);

		if (UtilPayload.isOK(payloadEstadoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/estado/EstadoFormBasic.zul";
	}

}
