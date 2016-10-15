package ve.smile.datos.parametros.clasificador.recurso.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorRecurso;
import ve.smile.payload.response.PayloadClasificadorRecursoResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorRecursoIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorRecurso> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorRecursoResponse payloadClasificadorRecursoResponse = S.ClasificadorRecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorRecursoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorRecursoResponse payloadClasificadorRecursoResponse = S.ClasificadorRecursoService
				.eliminar(getSelectedObject().getIdClasificadorRecurso());

		Alert.showMessage(payloadClasificadorRecursoResponse);

		if (UtilPayload.isOK(payloadClasificadorRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/recurso/ClasificadorRecursoFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorRecurso.idClasificadorRecurso", String.valueOf(getSelectedObject().getIdClasificadorRecurso()));

		//Table Relation Recurso
		PayloadRecursoResponse payloadRecursoResponse =
				S.RecursoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadRecursoResponse)) {
			return String.valueOf(payloadRecursoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countRecursos = 
				Double.valueOf(String.valueOf(payloadRecursoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countRecursos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countRecursos + " Registros de Recursos";
		}

		return "";
	}


}
