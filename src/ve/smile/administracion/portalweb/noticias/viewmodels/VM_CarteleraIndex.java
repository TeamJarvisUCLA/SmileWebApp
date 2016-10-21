package ve.smile.administracion.portalweb.noticias.viewmodels;

import java.util.HashMap;
import java.util.Map;

import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadEventPlanPatrocinadorResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_CarteleraIndex extends VM_WindowSimpleListPrincipal<Cartelera>{

	@Override
	public IPayloadResponse<Cartelera> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadCarteleraResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/noticias/CarteleraFormBasic.zul";
	}

	@Override
	public void doDelete() {
		PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService.eliminar(getSelectedObject().getIdCartelera());
		Alert.showMessage(payloadCarteleraResponse);
		if (UtilPayload.isOK(payloadCarteleraResponse)){
			
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	

	
}
