package ve.smile.datos.parametros.frecuencia_aporte.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FrecuenciaAporteIndex extends
		VM_WindowSimpleListPrincipal<FrecuenciaAporte> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<FrecuenciaAporte> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadFrecuenciaAporteResponse;
	}

	@Override
	public void doDelete() {
		PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
				.eliminar(getSelectedObject().getIdFrecuenciaAporte());

		Alert.showMessage(payloadFrecuenciaAporteResponse);

		if (UtilPayload.isOK(payloadFrecuenciaAporteResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/frecuenciaAporte/FrecuenciaAporteFormBasic.zul";
	}

}
