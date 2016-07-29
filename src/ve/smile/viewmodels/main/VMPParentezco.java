package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Parentezco;
import ve.smile.payload.response.PayloadParentezcoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPParentezco extends VM_WindowSimpleListPrincipal<Parentezco> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Parentezco> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadParentezcoResponse payloadParentezcoResponse = 
				S.ParentezcoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadParentezcoResponse;
	}

	@Override
	public void doDelete() {
		PayloadParentezcoResponse payloadParentezcoResponse =
				S.ParentezcoService.eliminar(getSelectedObject().getIdParentezco());

		Alert.showMessage(payloadParentezcoResponse);

		if (UtilPayload.isOK(payloadParentezcoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ParentezcoFormBasic.zul";
	}

}
