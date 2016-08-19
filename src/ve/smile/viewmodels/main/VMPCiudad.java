package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Ciudad;
import ve.smile.payload.response.PayloadCiudadResponse;

import org.zkoss.bind.annotation.Init;

public class VMPCiudad extends VM_WindowSimpleListPrincipal<Ciudad> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ciudad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCiudadResponse payloadCiudadResponse = 
				S.CiudadService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCiudadResponse;
	}

	@Override
	public void doDelete() {
		PayloadCiudadResponse payloadCiudadResponse =
				S.CiudadService.eliminar(getSelectedObject().getIdCiudad());

		Alert.showMessage(payloadCiudadResponse);

		if (UtilPayload.isOK(payloadCiudadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/CiudadFormBasic.zul";
	}

}
