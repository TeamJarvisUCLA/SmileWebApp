package ve.smile.datos.parametros.clasificador.trabajo_social.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.payload.response.PayloadClasificadorTrabajoSocialResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorTrabajoSocialIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorTrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorTrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorTrabajoSocialResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
				.eliminar(getSelectedObject().getIdClasificadorTrabajoSocial());

		Alert.showMessage(payloadClasificadorTrabajoSocialResponse);

		if (UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/trabajoSocial/ClasificadorTrabajoSocialFormBasic.zul";
	}

}
