package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Ayuda;
import ve.smile.payload.response.PayloadAyudaResponse;

import org.zkoss.bind.annotation.Init;

public class VMPAyuda extends VM_WindowSimpleListPrincipal<Ayuda> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ayuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadAyudaResponse payloadAyudaResponse = 
				S.AyudaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadAyudaResponse;
	}

	@Override
	public void doDelete() {
		PayloadAyudaResponse payloadAyudaResponse =
				S.AyudaService.eliminar(getSelectedObject().getIdAyuda());

		Alert.showMessage(payloadAyudaResponse);

		if (UtilPayload.isOK(payloadAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/AyudaFormBasic.zul";
	}

}
