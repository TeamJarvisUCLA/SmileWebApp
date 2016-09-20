package ve.smile.datos.parametros.tarea.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TareaIndex extends VM_WindowSimpleListPrincipal<Tarea> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Tarea> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTareaResponse payloadTareaResponse = S.TareaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTareaResponse;
	}

	@Override
	public void doDelete() {
		PayloadTareaResponse payloadTareaResponse = S.TareaService
				.eliminar(getSelectedObject().getIdTarea());

		Alert.showMessage(payloadTareaResponse);

		if (UtilPayload.isOK(payloadTareaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/tarea/TareaFormBasic.zul";
	}

}
