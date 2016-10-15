package ve.smile.datos.parametros.clasificador.trabajo_social.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.payload.response.PayloadClasificadorTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorTrabajoSocialIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorTrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorTrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorTrabajoSocialResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
				.eliminar(getSelectedObject().getIdClasificadorTrabajoSocial());

		Alert.showMessage(payloadClasificadorTrabajoSocialResponse);

		if (UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/trabajoSocial/ClasificadorTrabajoSocialFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorTrabajoSocial.idClasificadorTrabajoSocial", String.valueOf(getSelectedObject().getIdClasificadorTrabajoSocial()));

		//Table Relation TrabajoSocial
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse =
				S.TrabajoSocialService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
			return String.valueOf(payloadTrabajoSocialResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajos = 
				Double.valueOf(String.valueOf(payloadTrabajoSocialResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countTrabajos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countTrabajos + " Registros de Trabajos Sociales";
		}

		return "";
	}

}
