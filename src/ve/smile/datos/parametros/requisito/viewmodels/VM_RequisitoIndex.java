package ve.smile.datos.parametros.requisito.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Requisito;
import ve.smile.payload.response.PayloadRequisitoAyudaResponse;
import ve.smile.payload.response.PayloadRequisitoParticipacionResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoIndex extends VM_WindowSimpleListPrincipal<Requisito> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Requisito> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRequisitoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
				.eliminar(getSelectedObject().getIdRequisito());

		Alert.showMessage(payloadRequisitoResponse);

		if (UtilPayload.isOK(payloadRequisitoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/requisito/RequisitoFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Requisito";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkRequisito.idRequisito", String.valueOf(getSelectedObject().getIdRequisito()));

		//Table Relation RequisitoAyuda
		PayloadRequisitoAyudaResponse payloadRequisitoAyudaResponse =
				S.RequisitoAyudaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadRequisitoAyudaResponse)) {
			return String.valueOf(payloadRequisitoAyudaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countAyudas = 
				Double.valueOf(String.valueOf(payloadRequisitoAyudaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation RequisitoParticipacion
		PayloadRequisitoParticipacionResponse payloadRequisitoParticipacionResponse =
				S.RequisitoParticipacionService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadRequisitoParticipacionResponse)) {
			return String.valueOf(payloadRequisitoParticipacionResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countParticipaciones = 
				Double.valueOf(String.valueOf(payloadRequisitoParticipacionResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countParticipaciones > 0) {
			return "E:Error 0:No se puede eliminar el <b>Requisito</b> seleccionado ya que está siendo utilizado en " + countParticipaciones + " Participaciones";
		}

		if (countAyudas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Requisito</b> seleccionado ya que está siendo utilizado en " + countAyudas + " Ayudas";
		}

		return "";
	}

}
