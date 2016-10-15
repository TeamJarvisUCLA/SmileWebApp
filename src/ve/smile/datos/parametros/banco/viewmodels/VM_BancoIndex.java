package ve.smile.datos.parametros.banco.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S ;
import ve.smile.dto.Banco;
import ve.smile.payload.response.PayloadBancoResponse;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_BancoIndex extends VM_WindowSimpleListPrincipal<Banco> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Banco> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadBancoResponse payloadBancoResponse = 
				S.BancoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadBancoResponse;
	}

	@Override
	public void doDelete() {
		PayloadBancoResponse payloadBancoResponse =
				S.BancoService.eliminar(getSelectedObject().getIdBanco());

		Alert.showMessage(payloadBancoResponse);

		if (UtilPayload.isOK(payloadBancoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/banco/BancoFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Actividad";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkBanco.idBanco", String.valueOf(getSelectedObject().getIdBanco()));

		//Table Relation CuentaBancaria
		PayloadCuentaBancariaResponse payloadCuentaBancariaResponse =
				S.CuentaBancariaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadCuentaBancariaResponse)) {
			return String.valueOf(payloadCuentaBancariaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCuentaBancaria = 
				Double.valueOf(String.valueOf(payloadCuentaBancariaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countCuentaBancaria > 0) {
			return "E:Error 0:No se puede eliminar la <b>Ayuda</b> seleccionada ya que está siendo utilizada en " + countCuentaBancaria + " Cuentas Bancarias";
		}

		return "";
	}

}
