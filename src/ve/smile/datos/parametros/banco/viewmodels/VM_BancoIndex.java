package ve.smile.datos.parametros.banco.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S ;
import ve.smile.dto.Banco;
import ve.smile.payload.response.PayloadBancoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_BancoIndex extends VM_WindowSimpleListPrincipal<Banco> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Banco> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadBancoResponse payloadBancoResponse = 
				S.BancoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadBancoResponse;
	}

	@Override
	public void doDelete() {
		PayloadBancoResponse payloadBancoResponse =
				S.BancoService.eliminar(getSelectedObject().getIdBanco());

		Alert.showMessage(payloadBancoResponse);

		if (UtilPayload.isOK(payloadBancoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/banco/BancoFormBasic.zul";
	}

}
