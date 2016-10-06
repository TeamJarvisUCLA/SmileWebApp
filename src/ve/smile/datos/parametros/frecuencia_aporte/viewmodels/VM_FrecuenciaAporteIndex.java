package ve.smile.datos.parametros.frecuencia_aporte.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
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

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Frecuencia de Aporte";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkFrecuenciaAporte.idFrecuenciaAporte", String.valueOf(getSelectedObject().getIdFrecuenciaAporte()));

		//Table Relation Padrino
		PayloadPadrinoResponse payloadPadrinoResponse =
				S.PadrinoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPadrinoResponse)) {
			return String.valueOf(payloadPadrinoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPadrinos = 
				Double.valueOf(String.valueOf(payloadPadrinoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPadrinos > 0) {
			return "E:Error 0:No se puede eliminar la <b>Frecuecia del Aporte</b> seleccionada ya que está siendo utilizada en " + countPadrinos + " Registros de Padrino";
		}

		return "";
	}

}
