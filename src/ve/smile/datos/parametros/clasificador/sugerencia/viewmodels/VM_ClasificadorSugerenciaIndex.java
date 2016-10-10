package ve.smile.datos.parametros.clasificador.sugerencia.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.payload.response.PayloadClasificadorSugerenciaResponse;
import ve.smile.payload.response.PayloadContactoPortalResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorSugerenciaIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorSugerencia> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorSugerencia> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorSugerenciaResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService
				.eliminar(getSelectedObject().getIdClasificadorSugerencia());

		Alert.showMessage(payloadClasificadorSugerenciaResponse);

		if (UtilPayload.isOK(payloadClasificadorSugerenciaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/sugerencia/ClasificadorSugerenciaFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorSugerencia.idClasificadorSugerencia", String.valueOf(getSelectedObject().getIdClasificadorSugerencia()));

		//Table Relation ContactoPortal
		PayloadContactoPortalResponse payloadContactoPortalResponse =
				S.ContactoPortalService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadContactoPortalResponse)) {
			return String.valueOf(payloadContactoPortalResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countContactos = 
				Double.valueOf(String.valueOf(payloadContactoPortalResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countContactos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countContactos + " Contactos hechos en el portal";
		}

		return "";
	}

}
