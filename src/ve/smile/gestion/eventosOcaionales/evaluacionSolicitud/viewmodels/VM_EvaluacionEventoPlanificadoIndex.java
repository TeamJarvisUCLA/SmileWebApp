package ve.smile.gestion.eventosOcaionales.evaluacionSolicitud.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EvaluacionEventoPlanificadoIndex extends VM_WindowSimpleListPrincipal<EventoPlanificado>{

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusEvento", EstatusEventoPlanificadoEnum.POSTULADO.ordinal()+"");
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = 
				S.EventoPlanificadoService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);

		return payloadEventoPlanificadoResponse;
	}
	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		if(operacionEnum.equals(OperacionEnum.CUSTOM1)){
		return "views/desktop/gestion/evento/eventosOcacionales/evaluacionSolicitud/postulacioEventoPlanificadoFormBasic.zul";
		}
		return "views/desktop/gestion/evento/eventosOcacionales/evaluacionSolicitud/postulacioEventoPlanificadoFormBasic.zul";

		}


	@Override
	public String isValidPreconditionsCustom1() {
		if(getSelectedObject()==null){
			return "I:Información Code 5-Debe seleccionar una registro";
		}
		return "";
	}

	@Override
	public void executeCustom1() {
		getSrcFileZulForm(OperacionEnum.CUSTOM1);
		
		DataCenter.updateSrcPageContent(selectedObject, null, "views/desktop/gestion/evento/eventosOcacionales/evaluacionSolicitud/postulacioEventoPlanificadoFormBasic.zul");
		//DataCenter.updateSrcPageContent(selectedObject, OperacionEnum.CUSTOM1, "views/desktop/gestion/evento/eventosOcacionales/evaluacionSolicitud/postulacioEventoPlanificadoFormBasic.zul");
	}

	
	
}
