package ve.smile.datos.parametros.recurso.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RecursoIndex extends VM_WindowSimpleListPrincipal<Recurso> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Recurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRecursoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.eliminar(getSelectedObject().getIdRecurso());

		Alert.showMessage(payloadRecursoResponse);

		if (UtilPayload.isOK(payloadRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/recurso/RecursoFormBasic.zul";
	}

}
