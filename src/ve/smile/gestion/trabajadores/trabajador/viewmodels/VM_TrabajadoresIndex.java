package ve.smile.gestion.trabajadores.trabajador.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;








import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadPreguntaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TrabajadoresIndex extends VM_WindowSimpleListPrincipal<Trabajador>
{

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador", String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.eliminar(getSelectedObject().getIdTrabajador());
		
		Alert.showMessage(payloadTrabajadorResponse);
		
		if (UtilPayload.isOK(payloadTrabajadorResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/trabajadores/trabajador/TrabajadorFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Trabajador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkTrabajador.idTrabajador", String.valueOf(getSelectedObject().getIdTrabajador()));

		//Table Relation Estudiosocieconómico
		PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse =
				S.EstudioSocioEconomicoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEstudioSocioEconomicoResponse)) {
			return String.valueOf(payloadEstudioSocioEconomicoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}
		
		//Table Relation EventPlanTareaTrabajador
		PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse =
				S.EventPlanTareaTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
			return String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}
		
		//Table Relation TsPlanActividadTrabajador
				PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse =
						S.TsPlanActividadTrabajadorService.contarCriterios(TypeQuery.EQUAL,
								criterios);

				if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
					return String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
				}
		

		Integer countEstudiosSocieconomicos = 
				Double.valueOf(String.valueOf(payloadEstudioSocioEconomicoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
		Integer countEventPlanTareasTrabajador = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
		Integer countTsPlanActividadesTrabajador = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countEstudiosSocieconomicos > 0) {
			if(countEstudiosSocieconomicos == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en un estudio socieconómico";	
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countEstudiosSocieconomicos + " estudios socieconómicos";
			}
		}
		if (countEventPlanTareasTrabajador > 0) {
			if(countEventPlanTareasTrabajador == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en una tarea de evento";
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countEventPlanTareasTrabajador + " tareas de eventos";
			}
		}
		if (countTsPlanActividadesTrabajador > 0) {
			if(countTsPlanActividadesTrabajador == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en una actividad del trabajo social";
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countTsPlanActividadesTrabajador + " actividades del trabajo social";
			}
		}
		
		return "";
	}	
}