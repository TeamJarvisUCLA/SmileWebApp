package ve.smile.datos.parametros.trabajo_social.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTsPlanPersonaResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TrabajoSocialIndex extends
VM_WindowSimpleListPrincipal<TrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTrabajoSocialResponse;
	}

	@Override
	public void doDelete() {
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.eliminar(getSelectedObject().getIdTrabajoSocial());

		Alert.showMessage(payloadTrabajoSocialResponse);

		if (UtilPayload.isOK(payloadTrabajoSocialResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/trabajoSocial/TrabajoSocialFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Trabajo Social";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkTsPlan.idTrabajoSocial", String.valueOf(getSelectedObject().getIdTrabajoSocial()));

		//Table Relation TsPlanPersona
		PayloadTsPlanPersonaResponse payloadTsPlanPersonaResponse=
				S.TsPlanPersonaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanPersonaResponse)) {
			return String.valueOf(payloadTsPlanPersonaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPersonas = 
				Double.valueOf(String.valueOf(payloadTsPlanPersonaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation TsPlan
		PayloadTsPlanResponse payloadTsPlanResponse=
				S.TsPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadTsPlanResponse)) {
			return String.valueOf(payloadTsPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTrabajos = 
				Double.valueOf(String.valueOf(payloadTsPlanResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaTsActividad
		PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse=
				S.PlantillaTrabajoSocialActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialActividadResponse)) {
			return String.valueOf(payloadPlantillaTrabajoSocialActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillasActividades = 
				Double.valueOf(String.valueOf(payloadPlantillaTrabajoSocialActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaTsIndicador
		PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse=
				S.PlantillaTrabajoSocialIndicadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialIndicadorResponse)) {
			return String.valueOf(payloadPlantillaTrabajoSocialIndicadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillasIndicadores = 
				Double.valueOf(String.valueOf(payloadPlantillaTrabajoSocialIndicadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();


		if (countPersonas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Trabajo Social</b> seleccionado ya que está asigando a " + countPersonas + " Personas en Trabajos Sociales";
		}

		if (countTrabajos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Trabajo Social</b> seleccionado ya que está siendo utilizado en " + countTrabajos + " Trabajos Sociales Planificados";
		}

		if (countPlantillasActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Trabajo Social</b> seleccionado ya que está siendo utilizado en " + countPlantillasActividades + " Plantillas de Actividades en Trabajos Sociales";
		}
		
		if (countPlantillasIndicadores > 0) {
			return "E:Error 0:No se puede eliminar el <b>Trabajo Social</b> seleccionado ya que está siendo utilizado en " + countPlantillasIndicadores + " Plantillas de Indicadores en Trabajos Sociales";
		}

		return "";
	}

}
