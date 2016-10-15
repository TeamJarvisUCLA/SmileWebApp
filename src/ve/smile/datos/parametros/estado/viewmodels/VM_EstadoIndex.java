package ve.smile.datos.parametros.estado.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Estado;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EstadoIndex extends VM_WindowSimpleListPrincipal<Estado> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Estado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEstadoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
				.eliminar(getSelectedObject().getIdEstado());

		Alert.showMessage(payloadEstadoResponse);

		if (UtilPayload.isOK(payloadEstadoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/estado/EstadoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Estado";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkEstado.idEstado", String.valueOf(getSelectedObject().getIdEstado()));

		//Table Relation Ciudad
		PayloadCiudadResponse payloadCiudadResponse =
				S.CiudadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			return String.valueOf(payloadCiudadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countCiudades = 
				Double.valueOf(String.valueOf(payloadCiudadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countCiudades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Estado</b> seleccionado ya que está siendo utilizado en " + countCiudades + " Registros de Ciudades";
		}

		return "";
	}

}
