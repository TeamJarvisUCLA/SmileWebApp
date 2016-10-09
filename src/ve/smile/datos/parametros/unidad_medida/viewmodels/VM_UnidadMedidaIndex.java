package ve.smile.datos.parametros.unidad_medida.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.UnidadMedida;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.payload.response.PayloadUnidadMedidaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_UnidadMedidaIndex extends
VM_WindowSimpleListPrincipal<UnidadMedida> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<UnidadMedida> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadUnidadMedidaResponse payloadUnidadMedidaResponse = S.UnidadMedidaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadUnidadMedidaResponse;
	}

	@Override
	public void doDelete() {
		PayloadUnidadMedidaResponse payloadUnidadMedidaResponse = S.UnidadMedidaService
				.eliminar(getSelectedObject().getIdUnidadMedida());

		Alert.showMessage(payloadUnidadMedidaResponse);

		if (UtilPayload.isOK(payloadUnidadMedidaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/unidadMedida/UnidadMedidaFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Unidad de Medida";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkUnidadMedida.idUnidadMedida", String.valueOf(getSelectedObject().getIdUnidadMedida()));

		//Table Relation Indicador
		PayloadIndicadorResponse payloadIndicadorResponse =
				S.IndicadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorResponse)) {
			return String.valueOf(payloadIndicadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadores = 
				Double.valueOf(String.valueOf(payloadIndicadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation Recurso
		PayloadRecursoResponse payloadRecursoResponse =
				S.RecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadRecursoResponse)) {
			return String.valueOf(payloadRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countRecursos = 
				Double.valueOf(String.valueOf(payloadRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countIndicadores > 0) {
			return "E:Error 0:No se puede eliminar la <b>Unidad de Medida</b> seleccionada ya que está siendo utilizada en " + countIndicadores + " Indicadores";
		}

		if (countRecursos > 0) {
			return "E:Error 0:No se puede eliminar la <b>Unidad de Medida</b> seleccionada ya que está siendo utilizada en " + countRecursos + " Recursos";
		}

		return "";
	}

}
