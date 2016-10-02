package ve.smile.gestion.apadrinamiento.padrino.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_PadrinoIndex extends VM_WindowSimpleListPrincipal<Padrino> {
	
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
		return "views/desktop/gestion/apadrinamiento/padrino/PadrinoFormBasic.zul";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusPadrino", String.valueOf(EstatusPadrinoEnum.ACTIVO.ordinal()));
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,TypeQuery.EQUAL,criterios);
		return payloadPadrinoResponse;
	}
	

}
