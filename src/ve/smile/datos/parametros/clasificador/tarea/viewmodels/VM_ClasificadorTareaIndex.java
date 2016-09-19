package ve.smile.datos.parametros.clasificador.tarea.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorTarea;
import ve.smile.payload.response.PayloadClasificadorTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorTareaIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorTarea> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorTarea> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorTareaResponse payloadClasificadorTareaResponse = S.ClasificadorTareaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorTareaResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorTareaResponse payloadClasificadorTareaResponse = S.ClasificadorTareaService
				.eliminar(getSelectedObject().getIdClasificadorTarea());

		Alert.showMessage(payloadClasificadorTareaResponse);

		if (UtilPayload.isOK(payloadClasificadorTareaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/tarea/ClasificadorTareaFormBasic.zul";
	}

}
