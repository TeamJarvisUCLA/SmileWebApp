package ve.smile.datos.parametros.pregunta.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Pregunta;
import ve.smile.payload.response.PayloadPreguntaClasificadaResponse;
import ve.smile.payload.response.PayloadPreguntaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PreguntaIndex extends VM_WindowSimpleListPrincipal<Pregunta> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Pregunta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPreguntaResponse payloadPreguntaResponse = S.PreguntaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadPreguntaResponse;
	}

	@Override
	public void doDelete() {
		PayloadPreguntaResponse payloadPreguntaResponse = S.PreguntaService
				.eliminar(getSelectedObject().getIdPregunta());

		Alert.showMessage(payloadPreguntaResponse);

		if (UtilPayload.isOK(payloadPreguntaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/pregunta/PreguntaFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Pregunta";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkPregunta.idPregunta", String.valueOf(getSelectedObject().getIdPregunta()));

		//Table Relation PreguntaClasificada
		PayloadPreguntaClasificadaResponse payloadPreguntaClasificadaResponse =
				S.PreguntaClasificadaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPreguntaClasificadaResponse)) {
			return String.valueOf(payloadPreguntaClasificadaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPreguntas = 
				Double.valueOf(String.valueOf(payloadPreguntaClasificadaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPreguntas > 0) {
			return "E:Error 0:No se puede eliminar la <b>Pregunta</b> seleccionada ya que está siendo utilizada en " + countPreguntas + " Clasificaciones de Preguntas";
		}

		return "";
	}

}
