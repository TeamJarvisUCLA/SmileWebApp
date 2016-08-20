package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadRecursoResponse;
import org.zkoss.bind.annotation.Init;

public class VMPRecurso extends VM_WindowSimpleListPrincipal<Recurso> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Recurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRecursoResponse payloadRecursoResponse = 
				S.RecursoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRecursoResponse;
	}

	@Override
	public void doDelete() {
		PayloadRecursoResponse payloadRecursoResponse =
				S.RecursoService.eliminar(getSelectedObject().getIdRecurso());

		Alert.showMessage(payloadRecursoResponse);

		if (UtilPayload.isOK(payloadRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/RecursoFormBasic.zul";
	}

}
