package lights.seguridad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.dto.MensajeSistema;
import lights.seguridad.payload.response.PayloadMensajeSistemaResponse;
import lights.smile.consume.services.S;

import org.zkoss.bind.annotation.Init;

public class VMPMensajeSistema extends VM_WindowSimpleListPrincipal<MensajeSistema> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<MensajeSistema> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadMensajeSistemaResponse payloadMensajeSistemaResponse = 
				S.MensajeSistemaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadMensajeSistemaResponse;
	}

	@Override
	public void doDelete() {
		PayloadMensajeSistemaResponse payloadMensajeSistemaResponse =
				S.MensajeSistemaService.eliminar(getSelectedObject().getIdMensajeSistema());

		Alert.showMessage(payloadMensajeSistemaResponse);

		if (UtilPayload.isOK(payloadMensajeSistemaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public Map<OperacionEnum, Boolean> getScheduledsTo() {
		Map<OperacionEnum, Boolean> isScheduleds = new HashMap<OperacionEnum, Boolean>();

		isScheduleds.put(OperacionEnum.INCLUIR, true);
		isScheduleds.put(OperacionEnum.MODIFICAR, true);
		isScheduleds.put(OperacionEnum.ELIMINAR, true);
		isScheduleds.put(OperacionEnum.CONSULTAR, true);

		return isScheduleds;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "vista/viewMensajeSistema.zul";
	}

}
