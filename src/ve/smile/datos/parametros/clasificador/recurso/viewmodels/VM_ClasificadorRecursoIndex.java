package ve.smile.datos.parametros.clasificador.recurso.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorRecurso;
import ve.smile.payload.response.PayloadClasificadorRecursoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorRecursoIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorRecurso> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorRecursoResponse payloadClasificadorRecursoResponse = S.ClasificadorRecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorRecursoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorRecursoResponse payloadClasificadorRecursoResponse = S.ClasificadorRecursoService
				.eliminar(getSelectedObject().getIdClasificadorRecurso());

		Alert.showMessage(payloadClasificadorRecursoResponse);

		if (UtilPayload.isOK(payloadClasificadorRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/recurso/ClasificadorRecursoFormBasic.zul";
	}

}
