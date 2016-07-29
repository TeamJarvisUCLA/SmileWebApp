package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;

import org.zkoss.bind.annotation.Init;

public class VMPTarea extends VM_WindowSimpleListPrincipal<Tarea> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Tarea> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTareaResponse payloadTareaResponse = 
				S.TareaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTareaResponse;
	}

	@Override
	public void doDelete() {
		PayloadTareaResponse payloadTareaResponse =
				S.TareaService.eliminar(getSelectedObject().getIdTarea());

		Alert.showMessage(payloadTareaResponse);

		if (UtilPayload.isOK(payloadTareaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/TareaFormBasic.zul";
	}

}
