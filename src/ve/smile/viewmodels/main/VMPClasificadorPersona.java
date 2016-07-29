package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorPersona;
import ve.smile.payload.response.PayloadClasificadorPersonaResponse;

import org.zkoss.bind.annotation.Init;

public class VMPClasificadorPersona extends VM_WindowSimpleListPrincipal<ClasificadorPersona> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorPersona> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorPersonaResponse payloadClasificadorPersonaResponse = 
				S.ClasificadorPersonaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorPersonaResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorPersonaResponse payloadClasificadorPersonaResponse =
				S.ClasificadorPersonaService.eliminar(getSelectedObject().getIdClasificadorPersona());

		Alert.showMessage(payloadClasificadorPersonaResponse);

		if (UtilPayload.isOK(payloadClasificadorPersonaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ClasificadorPersonaFormBasic.zul";
	}

}
