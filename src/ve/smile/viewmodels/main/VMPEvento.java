package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;
import org.zkoss.bind.annotation.Init;

public class VMPEvento extends VM_WindowSimpleListPrincipal<Evento> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEventoResponse payloadEventoResponse = 
				S.EventoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEventoResponse;
	}

	@Override
	public void doDelete() {
		PayloadEventoResponse payloadEventoResponse =
				S.EventoService.eliminar(getSelectedObject().getIdEvento());

		Alert.showMessage(payloadEventoResponse);

		if (UtilPayload.isOK(payloadEventoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/EventoFormBasic.zul";
	}

}
