package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;

import org.zkoss.bind.annotation.Init;

public class VMPIndicador extends VM_WindowSimpleListPrincipal<Indicador> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Indicador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadIndicadorResponse payloadIndicadorResponse = 
				S.IndicadorService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadIndicadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadIndicadorResponse payloadIndicadorResponse =
				S.IndicadorService.eliminar(getSelectedObject().getIdIndicador());

		Alert.showMessage(payloadIndicadorResponse);

		if (UtilPayload.isOK(payloadIndicadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/IndicadorFormBasic.zul";
	}

}
