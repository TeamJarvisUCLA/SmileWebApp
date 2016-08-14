package ve.smile.viewmodels.main;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Fortaleza;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPFortaleza extends VM_WindowSimpleListPrincipal<Fortaleza> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Fortaleza> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadFortalezaResponse;
	}

	@Override
	public void doDelete() {
		PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
				.eliminar(getSelectedObject().getIdFortaleza());

		Alert.showMessage(payloadFortalezaResponse);

		if (UtilPayload.isOK(payloadFortalezaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/FortalezaFormBasic.zul";
	}

}
