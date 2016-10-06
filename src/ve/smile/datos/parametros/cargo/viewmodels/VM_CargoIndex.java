package ve.smile.datos.parametros.cargo.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Cargo;
import ve.smile.payload.response.PayloadCargoResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CargoIndex extends VM_WindowSimpleListPrincipal<Cargo> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Cargo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCargoResponse payloadCargoResponse = S.CargoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCargoResponse;
	}

	@Override
	public void doDelete() {
		PayloadCargoResponse payloadCargoResponse = S.CargoService
				.eliminar(getSelectedObject().getIdCargo());

		Alert.showMessage(payloadCargoResponse);

		if (UtilPayload.isOK(payloadCargoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/cargo/CargoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Cargo";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkCargo.idCargo", String.valueOf(getSelectedObject().getIdCargo()));

		//Table Relation Trabajador
		PayloadTrabajadorResponse payloadTrabajadorResponse =
				S.TrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
			return String.valueOf(payloadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajadores = 
				Double.valueOf(String.valueOf(payloadTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countTrabajadores > 0) {
			return "E:Error 0:No se puede eliminar el <b>Cargo</b> seleccionado ya que está siendo utilizado en " + countTrabajadores + " Trabajadores";
		}

		return "";
	}

}
