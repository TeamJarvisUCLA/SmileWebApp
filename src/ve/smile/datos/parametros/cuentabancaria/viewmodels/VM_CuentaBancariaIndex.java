package ve.smile.datos.parametros.cuentabancaria.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.CuentaBancaria;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CuentaBancariaIndex extends
		VM_WindowSimpleListPrincipal<CuentaBancaria> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<CuentaBancaria> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCuentaBancariaResponse payloadCuentaBancariaResponse = S.CuentaBancariaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCuentaBancariaResponse;
	}

	@Override
	public void doDelete() {
		PayloadCuentaBancariaResponse payloadCuentaBancariaResponse = S.CuentaBancariaService
				.eliminar(getSelectedObject().getIdCuentaBancaria());

		Alert.showMessage(payloadCuentaBancariaResponse);

		if (UtilPayload.isOK(payloadCuentaBancariaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/cuentaBancaria/CuentaBancariaFormBasic.zul";
	}

}
