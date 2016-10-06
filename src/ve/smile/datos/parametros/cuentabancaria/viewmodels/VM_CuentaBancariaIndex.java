package ve.smile.datos.parametros.cuentabancaria.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.CuentaBancaria;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;
import ve.smile.payload.response.PayloadDonativoCuentaBancariaResponse;
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

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Cuenta Bancaria";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkCuentaBancaria.idCuentaBancaria", String.valueOf(getSelectedObject().getIdCuentaBancaria()));

		//Table Relation DonativoCuentaBancaria
		PayloadDonativoCuentaBancariaResponse payloadDonativoCuentaBancariaResponse =
				S.DonativoCuentaBancariaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadDonativoCuentaBancariaResponse)) {
			return String.valueOf(payloadDonativoCuentaBancariaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countDonativosCuentaBancaria = 
				Double.valueOf(String.valueOf(payloadDonativoCuentaBancariaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countDonativosCuentaBancaria > 0) {
			return "E:Error 0:No se puede eliminar la <b>Cuenta Bancaria</b> seleccionada ya que está siendo utilizada en " + countDonativosCuentaBancaria + " Donativos";
		}

		return "";
	}

}
