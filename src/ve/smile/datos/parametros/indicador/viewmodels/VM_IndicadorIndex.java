package ve.smile.datos.parametros.indicador.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_IndicadorIndex extends VM_WindowSimpleListPrincipal<Indicador> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Indicador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadIndicadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.eliminar(getSelectedObject().getIdIndicador());

		Alert.showMessage(payloadIndicadorResponse);

		if (UtilPayload.isOK(payloadIndicadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/indicador/IndicadorFormBasic.zul";
	}

}
