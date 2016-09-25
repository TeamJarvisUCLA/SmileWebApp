package ve.smile.gestion.evento.planificaion.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EventoPlanificadoTareaParticiapanteIndex extends VM_WindowSimpleListPrincipal<EventoPlanificado>{
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = 
				S.EventoPlanificadoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEventoPlanificadoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse =
				S.EventoPlanificadoService.eliminar(getSelectedObject().getIdEventoPlanificado());

		Alert.showMessage(payloadEventoPlanificadoResponse);

		if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/evento/planificacion/eventoIndicador.zul";
	}


}
