package ve.smile.datos.parametros.profesion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Profesion;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.payload.response.PayloadTrabajadorProfesionResponse;
import ve.smile.payload.response.PayloadVoluntarioProfesionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ProfesionIndex extends VM_WindowSimpleListPrincipal<Profesion> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Profesion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadProfesionResponse;
	}

	@Override
	public void doDelete() {
		PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
				.eliminar(getSelectedObject().getIdProfesion());

		Alert.showMessage(payloadProfesionResponse);

		if (UtilPayload.isOK(payloadProfesionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/profesion/ProfesionFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Profesión";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkProfesion.idProfesion", String.valueOf(getSelectedObject().getIdProfesion()));

		//Table Relation TrabajadorProfesion
		PayloadTrabajadorProfesionResponse payloadTrabajadorProfesionResponse =
				S.TrabajadorProfesionService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTrabajadorProfesionResponse)) {
			return String.valueOf(payloadTrabajadorProfesionResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajadores = 
				Double.valueOf(String.valueOf(payloadTrabajadorProfesionResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation VoluntarioProfesion
		PayloadVoluntarioProfesionResponse payloadVoluntarioProfesionResponse =
				S.VoluntarioProfesionService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadVoluntarioProfesionResponse)) {
			return String.valueOf(payloadVoluntarioProfesionResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countVoluntarios = 
				Double.valueOf(String.valueOf(payloadVoluntarioProfesionResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countVoluntarios > 0) {
			return "E:Error 0:No se puede eliminar la <b>Profesión</b> seleccionada ya que está siendo utilizada por " + countVoluntarios + " Voluntarios";
		}

		if (countTrabajadores > 0) {
			return "E:Error 0:No se puede eliminar la <b>Profesión</b> seleccionada ya que está siendo utilizada por " + countTrabajadores + " Trabajadores";
		}

		return "";
	}

}
