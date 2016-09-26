package ve.smile.gestion.apadrinamiento.egreso.viewmodels;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_EgresoPadrinoIndex extends VM_WindowSimpleListPrincipal<Padrino> {
	
	@Init(superclass = true)
	public void childInit(){
		
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		
		return "views/desktop/apadrinamiento/egreso/EgresoPadrinoIndex.zul";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadPadrinoResponse;
	}

	@Override
	public void doDelete() {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.eliminar(getSelectedObject().getIdPadrino());
		Alert.showMessage(payloadPadrinoResponse);
		if (UtilPayload.isOK(payloadPadrinoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	
	}
	
	

}
