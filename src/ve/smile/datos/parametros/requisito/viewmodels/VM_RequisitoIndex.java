package ve.smile.datos.parametros.requisito.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Requisito;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoIndex extends VM_WindowSimpleListPrincipal<Requisito> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Requisito> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRequisitoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
				.eliminar(getSelectedObject().getIdRequisito());

		Alert.showMessage(payloadRequisitoResponse);

		if (UtilPayload.isOK(payloadRequisitoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/requisito/RequisitoFormBasic.zul";
	}

}
