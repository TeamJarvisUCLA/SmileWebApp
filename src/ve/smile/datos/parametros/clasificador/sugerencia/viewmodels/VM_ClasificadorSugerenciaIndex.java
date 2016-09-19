package ve.smile.datos.parametros.clasificador.sugerencia.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.payload.response.PayloadClasificadorSugerenciaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorSugerenciaIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorSugerencia> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorSugerencia> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorSugerenciaResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService
				.eliminar(getSelectedObject().getIdClasificadorSugerencia());

		Alert.showMessage(payloadClasificadorSugerenciaResponse);

		if (UtilPayload.isOK(payloadClasificadorSugerenciaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/sugerencia/ClasificadorSugerenciaFormBasic.zul";
	}

}
