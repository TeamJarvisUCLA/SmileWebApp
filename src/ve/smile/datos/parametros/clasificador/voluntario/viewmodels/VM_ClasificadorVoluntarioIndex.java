package ve.smile.datos.parametros.clasificador.voluntario.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.payload.response.PayloadClasificadorVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorVoluntarioIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorVoluntario> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorVoluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorVoluntarioResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
				.eliminar(getSelectedObject().getIdClasificadorVoluntario());

		Alert.showMessage(payloadClasificadorVoluntarioResponse);

		if (UtilPayload.isOK(payloadClasificadorVoluntarioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/voluntario/ClasificadorVoluntarioFormBasic.zul";
	}

}
