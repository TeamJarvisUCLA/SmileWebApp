package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Requisito;
import ve.smile.payload.response.PayloadRequisitoResponse;
import org.zkoss.bind.annotation.Init;

public class VMPRequisito extends VM_WindowSimpleListPrincipal<Requisito> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Requisito> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRequisitoResponse payloadRequisitoResponse = 
				S.RequisitoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRequisitoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRequisitoResponse payloadRequisitoResponse =
				S.RequisitoService.eliminar(getSelectedObject().getIdRequisito());

		Alert.showMessage(payloadRequisitoResponse);

		if (UtilPayload.isOK(payloadRequisitoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/RequisitoFormBasic.zul";
	}

}
