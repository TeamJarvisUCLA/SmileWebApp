package ve.smile.datos.parametros.ciudad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CiudadIndex extends VM_WindowSimpleListPrincipal<Ciudad> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ciudad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCiudadResponse;
	}

	@Override
	public void doDelete() {
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.eliminar(getSelectedObject().getIdCiudad());

		Alert.showMessage(payloadCiudadResponse);

		if (UtilPayload.isOK(payloadCiudadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/ciudad/CiudadFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Ciudad";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkCiudad.idCiudad", String.valueOf(getSelectedObject().getIdCiudad()));

		//Table Relation Persona
		PayloadPersonaResponse payloadPersonaResponse =
				S.PersonaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPersonaResponse)) {
			return String.valueOf(payloadPersonaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPersonas = 
				Double.valueOf(String.valueOf(payloadPersonaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation Directorio
		PayloadDirectorioResponse payloadDirectorioResponse =
				S.DirectorioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadDirectorioResponse)) {
			return String.valueOf(payloadDirectorioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countDirectorios = 
				Double.valueOf(String.valueOf(payloadDirectorioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPersonas > 0) {
			return "E:Error 0:No se puede eliminar la <b>Ciudad</b> seleccionada ya que está siendo utilizada en " + countPersonas + " Registros de Persona";
		}

		if (countDirectorios > 0) {
			return "E:Error 0:No se puede eliminar la <b>Ciudad</b> seleccionada ya que está siendo utilizada en " + countDirectorios + " Directorios";
		}

		return "";
	}
}