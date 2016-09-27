package ve.smile.gestion.evento.planificaion.viewmodels;


import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.dto.EventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;

import org.zkoss.bind.annotation.Init;

public class VM_EventoPlanificadoTareaIndicadorIndex extends VM_WindowSimpleListPrincipal<EventoPlanificado> {

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
	public void executeCustom1() {
		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);

		DataCenter.updateSrcPageContent(selectedObject, operacion, getSrcFileZulForm(OperacionEnum.CUSTOM1));
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
		return "views/desktop/gestion/evento/planificacion/eventoTareaIndicador.zul";
		}
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1() {
		if (selectedObject == null) {
			return "I:Information Code: 107-Debe seleccionar un evento planificado";
		}
		return "";
	}

	

}
