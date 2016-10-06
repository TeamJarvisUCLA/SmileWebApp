package ve.smile.datos.parametros.indicador.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorEventoPlanTareaResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanActividadResponse;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_IndicadorIndex extends VM_WindowSimpleListPrincipal<Indicador> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Indicador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadIndicadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.eliminar(getSelectedObject().getIdIndicador());

		Alert.showMessage(payloadIndicadorResponse);

		if (UtilPayload.isOK(payloadIndicadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/indicador/IndicadorFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Indicador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkIndicador.idIndicador", String.valueOf(getSelectedObject().getIdIndicador()));

		//Table Relation IndicadorEventoPlanificado
		PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse =
				S.IndicadorEventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorEventoPlanificadoResponse)) {
			return String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventos = 
				Double.valueOf(String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorEventoPlanTarea
		PayloadIndicadorEventoPlanTareaResponse payloadIndicadorEventoPlanTareaResponse =
				S.IndicadorEventoPlanTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorEventoPlanTareaResponse)) {
			return String.valueOf(payloadIndicadorEventoPlanTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareas = 
				Double.valueOf(String.valueOf(payloadIndicadorEventoPlanTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorTsPlan
		PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse =
				S.IndicadorTsPlanService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorTsPlanResponse)) {
			return String.valueOf(payloadIndicadorTsPlanResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTs = 
				Double.valueOf(String.valueOf(payloadIndicadorTsPlanResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorTsPlanActividad
		PayloadIndicadorTsPlanActividadResponse payloadIndicadorTsPlanActividadResponse =
				S.IndicadorTsPlanActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorTsPlanActividadResponse)) {
			return String.valueOf(payloadIndicadorTsPlanActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividades = 
				Double.valueOf(String.valueOf(payloadIndicadorTsPlanActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaEventoIndicador
		PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse =
				S.PlantillaEventoIndicadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaEventoIndicadorResponse)) {
			return String.valueOf(payloadPlantillaEventoIndicadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillasEvento = 
				Double.valueOf(String.valueOf(payloadPlantillaEventoIndicadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaTsIndicador
		PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse =
		S.PlantillaTrabajoSocialIndicadorService.contarCriterios(TypeQuery.EQUAL,
				criterios);

		if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialIndicadorResponse)) {
			return String.valueOf(payloadPlantillaTrabajoSocialIndicadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlantillasTs = 
				Double.valueOf(String.valueOf(payloadPlantillaTrabajoSocialIndicadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPlantillasTs > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countPlantillasTs + " Plantillas de Trabajos Sociales";
		}

		if (countPlantillasEvento > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countPlantillasEvento + " Plantillas de Eventos";
		}

		if (countActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countActividades + " Actividades del Trabajo Social";
		}

		if (countTs > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countTs + " Trabajos Sociales Planificados";
		}

		if (countTareas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countTareas + " Tareas de Eventos";
		}

		if (countEventos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Indicador</b> seleccionado ya que está siendo utilizado en " + countEventos + " Eventos Planificados";
		}

		return "";
	}

}
