package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.payload.response.PayloadClasificadorEventoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPClasificadorEvento extends VM_WindowSimpleListPrincipal<ClasificadorEvento> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorEvento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = 
				S.ClasificadorEventoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorEventoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse =
				S.ClasificadorEventoService.eliminar(getSelectedObject().getIdClasificadorEvento());

		Alert.showMessage(payloadClasificadorEventoResponse);

		if (UtilPayload.isOK(payloadClasificadorEventoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ClasificadorEventoFormBasic.zul";
	}

}
