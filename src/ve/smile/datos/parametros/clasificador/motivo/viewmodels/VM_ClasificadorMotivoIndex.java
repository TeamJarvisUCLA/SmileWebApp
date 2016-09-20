package ve.smile.datos.parametros.clasificador.motivo.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorMotivo;
import ve.smile.payload.response.PayloadClasificadorMotivoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorMotivoIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorMotivo> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorMotivo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = S.ClasificadorMotivoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorMotivoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = S.ClasificadorMotivoService
				.eliminar(getSelectedObject().getIdClasificadorMotivo());

		Alert.showMessage(payloadClasificadorMotivoResponse);

		if (UtilPayload.isOK(payloadClasificadorMotivoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/motivo/ClasificadorMotivoFormBasic.zul";
	}

}
