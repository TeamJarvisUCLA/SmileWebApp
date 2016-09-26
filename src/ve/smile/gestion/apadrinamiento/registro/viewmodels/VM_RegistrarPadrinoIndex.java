package ve.smile.gestion.apadrinamiento.registro.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RegistrarPadrinoIndex extends VM_WindowSimpleListPrincipal<Padrino>{
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	public void doDelete() {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.eliminar(getSelectedObject().getIdPadrino());
		Alert.showMessage(payloadPadrinoResponse);
		if (UtilPayload.isOK(payloadPadrinoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
	
	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/apadrinamiento/registro/RegistrarPadrinoFormBasic.zul";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadPadrinoResponse;
	}
	
}
