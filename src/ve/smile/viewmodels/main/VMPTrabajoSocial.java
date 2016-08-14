package ve.smile.viewmodels.main;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPTrabajoSocial extends
		VM_WindowSimpleListPrincipal<TrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTrabajoSocialResponse;
	}

	@Override
	public void doDelete() {
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.eliminar(getSelectedObject().getIdTrabajoSocial());

		Alert.showMessage(payloadTrabajoSocialResponse);

		if (UtilPayload.isOK(payloadTrabajoSocialResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/TrabajoSocialFormBasic.zul";
	}

}
