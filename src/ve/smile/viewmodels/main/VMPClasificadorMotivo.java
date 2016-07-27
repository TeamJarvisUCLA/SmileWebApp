package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorMotivo;
import ve.smile.payload.response.PayloadClasificadorMotivoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPClasificadorMotivo extends VM_WindowSimpleListPrincipal<ClasificadorMotivo> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorMotivo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = 
				S.ClasificadorMotivoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorMotivoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse =
				S.ClasificadorMotivoService.eliminar(getSelectedObject().getIdClasificadorMotivo());

		Alert.showMessage(payloadClasificadorMotivoResponse);

		if (UtilPayload.isOK(payloadClasificadorMotivoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ClasificadorMotivoFormBasic.zul";
	}

}
