package ve.smile.gestion.donativo.reconocimiento.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;

import org.zkoss.bind.annotation.Init;

public class VM_ReconocimientoColaboradorIndex extends VM_WindowSimpleListPrincipal<ReconocimientoPersona> {
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ReconocimientoPersona> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = 
				S.ReconocimientoPersonaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadReconocimientoPersonaResponse;
	} 

	
	@Override
	public void doDelete() {
		PayloadColaboradorResponse payloadColaboradorResponse =
				S.ColaboradorService.eliminar(getSelectedObject().getIdReconocimientoPersona());

		Alert.showMessage(payloadColaboradorResponse);

		if (UtilPayload.isOK(payloadColaboradorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/donativo/reconocimiento/ReconocimientoColaboradorFormBasic.zul";
	}

}
