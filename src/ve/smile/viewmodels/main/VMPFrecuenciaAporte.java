package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import org.zkoss.bind.annotation.Init;

public class VMPFrecuenciaAporte extends VM_WindowSimpleListPrincipal<FrecuenciaAporte> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<FrecuenciaAporte> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = 
				S.FrecuenciaAporteService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadFrecuenciaAporteResponse;
	}

	@Override
	public void doDelete() {
		PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse =
				S.FrecuenciaAporteService.eliminar(getSelectedObject().getIdFrecuenciaAporte());

		Alert.showMessage(payloadFrecuenciaAporteResponse);

		if (UtilPayload.isOK(payloadFrecuenciaAporteResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/FrecuenciaAporteFormBasic.zul";
	}

}
