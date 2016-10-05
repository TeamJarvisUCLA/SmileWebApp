package ve.smile.gestion.donativo.asignacion.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.DonativoRecurso;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;

import org.zkoss.bind.annotation.Init;

public class VM_AsignacionRecursoIndex extends VM_WindowSimpleListPrincipal<DonativoRecurso> {
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<DonativoRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = 
				S.DonativoRecursoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadDonativoRecursoResponse;
	} 

	
	@Override
	public void doDelete() {
		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse =
				S.DonativoRecursoService.eliminar(getSelectedObject().getIdDonativoRecurso());

		Alert.showMessage(payloadDonativoRecursoResponse);

		if (UtilPayload.isOK(payloadDonativoRecursoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/donativo/asignacion/AsignacionRecursoFormBasic.zul";
	}

}
