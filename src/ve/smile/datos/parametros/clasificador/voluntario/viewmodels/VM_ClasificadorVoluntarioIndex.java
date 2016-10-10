package ve.smile.datos.parametros.clasificador.voluntario.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.payload.response.PayloadClasificadorVoluntarioResponse;
import ve.smile.payload.response.PayloadVoluntarioClasificadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorVoluntarioIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorVoluntario> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorVoluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorVoluntarioResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
				.eliminar(getSelectedObject().getIdClasificadorVoluntario());

		Alert.showMessage(payloadClasificadorVoluntarioResponse);

		if (UtilPayload.isOK(payloadClasificadorVoluntarioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/voluntario/ClasificadorVoluntarioFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorVoluntario.idClasificadorVoluntario", String.valueOf(getSelectedObject().getIdClasificadorVoluntario()));

		//Table Relation VoluntarioClasificado
		PayloadVoluntarioClasificadoResponse payloadVoluntarioClasificadoResponse =
				S.VoluntarioClasificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadVoluntarioClasificadoResponse)) {
			return String.valueOf(payloadVoluntarioClasificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countVoluntarios = 
				Double.valueOf(String.valueOf(payloadVoluntarioClasificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countVoluntarios + " Registros de Voluntarios";
		}

		return "";
	}

}
