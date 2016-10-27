package ve.smile.datos.parametros.evento.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventPlanPatrocinadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadEventPlanTareaVoluntarioResponse;
import ve.smile.payload.response.PayloadEventoFavoritoUsuarioResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;
import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EventoIndex extends VM_WindowSimpleListPrincipal<Evento> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEventoResponse payloadEventoResponse = S.EventoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEventoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEventoResponse payloadEventoResponse = S.EventoService
				.eliminar(getSelectedObject().getIdEvento());

		Alert.showMessage(payloadEventoResponse);

		if (UtilPayload.isOK(payloadEventoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/evento/EventoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Evento";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkEvento.idEvento", String.valueOf(getSelectedObject().getIdEvento()));

		//Table Relation EventPlanPatrocinador
		PayloadEventPlanPatrocinadorResponse payloadEventPlanPatrocinadorResponse =
				S.EventPlanPatrocinadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanPatrocinadorResponse)) {
			return String.valueOf(payloadEventPlanPatrocinadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPatrocinadores = 
				Double.valueOf(String.valueOf(payloadEventPlanPatrocinadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTareaTrabajador
		PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse =
				S.EventPlanTareaTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
			return String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareasTrabajador = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventPlanTareaVoluntario
		PayloadEventPlanTareaVoluntarioResponse payloadEventPlanTareaVoluntarioResponse =
				S.EventPlanTareaVoluntarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaVoluntarioResponse)) {
			return String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countTareasVoluntario = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaVoluntarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventoFavoritoUsuario
		PayloadEventoFavoritoUsuarioResponse payloadEventoFavoritoUsuarioResponse =
				S.EventoFavoritoUsuarioService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventoFavoritoUsuarioResponse)) {
			return String.valueOf(payloadEventoFavoritoUsuarioResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventosFavoritos = 
				Double.valueOf(String.valueOf(payloadEventoFavoritoUsuarioResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EventoPlanificado
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse =
				S.EventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
			return String.valueOf(payloadEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventosPlanificados = 
				Double.valueOf(String.valueOf(payloadEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation IndicadorEventoPlanificado
		PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse =
				S.IndicadorEventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadIndicadorEventoPlanificadoResponse)) {
			return String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countIndicadoresEventoPlanificado = 
				Double.valueOf(String.valueOf(payloadIndicadorEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaEventoIndicador
		PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse =
				S.PlantillaEventoIndicadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaEventoIndicadorResponse)) {
			return String.valueOf(payloadPlantillaEventoIndicadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlatillasIndicadores = 
				Double.valueOf(String.valueOf(payloadPlantillaEventoIndicadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation PlantillaEventoTarea
		PayloadPlantillaEventoTareaResponse payloadPlantillaEventoTareaResponse =
				S.PlantillaEventoTareaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadPlantillaEventoTareaResponse)) {
			return String.valueOf(payloadPlantillaEventoTareaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countPlatillasTareas = 
				Double.valueOf(String.valueOf(payloadPlantillaEventoTareaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countPlatillasTareas > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countPlatillasTareas + " Plantillas de Tareas";
		}

		if (countPlatillasIndicadores > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countPlatillasIndicadores + " Plantillas de Indicadores";
		}

		if (countIndicadoresEventoPlanificado > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countIndicadoresEventoPlanificado + " Indicadores de Eventos Planificados";
		}

		if (countEventosPlanificados > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countEventosPlanificados + " Eventos Planificados";
		}

		if (countEventosFavoritos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countEventosFavoritos + " Eventos Favoritos";
		}

		if (countTareasVoluntario > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countTareasVoluntario + " Tareas de Voluntarios";
		}

		if (countTareasTrabajador > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countTareasTrabajador + " Tareas de Trabajadores";
		}

		if (countPatrocinadores > 0) {
			return "E:Error 0:No se puede eliminar el <b>Evento</b> seleccionado ya que está siendo utilizado en " + countPatrocinadores + " Patrocinadores de Eventos Planificados";
		}

		return "";
	}

}
