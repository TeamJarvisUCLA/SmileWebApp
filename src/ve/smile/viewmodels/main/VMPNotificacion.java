package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Notificacion;
import ve.smile.payload.response.PayloadNotificacionResponse;

import org.zkoss.bind.annotation.Init;

public class VMPNotificacion extends VM_WindowSimpleListPrincipal<Notificacion> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Notificacion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadNotificacionResponse payloadNotificacionResponse = 
				S.NotificacionService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadNotificacionResponse;
	}

	@Override
	public void doDelete() {
		PayloadNotificacionResponse payloadNotificacionResponse =
				S.NotificacionService.eliminar(getSelectedObject().getIdNotificacion());

		Alert.showMessage(payloadNotificacionResponse);

		if (UtilPayload.isOK(payloadNotificacionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/NotificacionFormBasic.zul";
	}

}
