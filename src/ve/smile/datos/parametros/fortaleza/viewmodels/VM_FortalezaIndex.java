package ve.smile.datos.parametros.fortaleza.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Fortaleza;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadTrabajadorFortalezaResponse;
import ve.smile.payload.response.PayloadVoluntarioFortalezaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FortalezaIndex extends VM_WindowSimpleListPrincipal<Fortaleza> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Fortaleza> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadFortalezaResponse;
	}

	@Override
	public void doDelete() {
		PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
				.eliminar(getSelectedObject().getIdFortaleza());

		Alert.showMessage(payloadFortalezaResponse);

		if (UtilPayload.isOK(payloadFortalezaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/fortaleza/FortalezaFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Fortaleza";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkFortaleza.idFortaleza", String.valueOf(getSelectedObject().getIdFortaleza()));

		//Table Relation TrabajadorFortaleza
		PayloadTrabajadorFortalezaResponse payloadTrabajadorFortalezaResponse =
				S.TrabajadorFortalezaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTrabajadorFortalezaResponse)) {
			return String.valueOf(payloadTrabajadorFortalezaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajadores = 
				Double.valueOf(String.valueOf(payloadTrabajadorFortalezaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation VoluntarioFortaleza
		PayloadVoluntarioFortalezaResponse payloadVoluntarioFortalezaResponse =
				S.VoluntarioFortalezaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadVoluntarioFortalezaResponse)) {
			return String.valueOf(payloadVoluntarioFortalezaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countVoluntarios = 
				Double.valueOf(String.valueOf(payloadVoluntarioFortalezaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar la <b>Fortaleza</b> seleccionada ya que está siendo utilizada en " + countVoluntarios + " Voluntarios";
		}

		if (countTrabajadores > 0) {
			return "E:Error 0:No se puede eliminar la <b>Fortaleza</b> seleccionada ya que está siendo utilizada en " + countTrabajadores + " Trabajadores";
		}

		return "";
	}

}
