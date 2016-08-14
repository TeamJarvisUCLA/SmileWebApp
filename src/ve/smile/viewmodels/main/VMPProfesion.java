package ve.smile.viewmodels.main;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Profesion;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPProfesion extends VM_WindowSimpleListPrincipal<Profesion> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Profesion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadProfesionResponse;
	}

	@Override
	public void doDelete() {
		PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
				.eliminar(getSelectedObject().getIdProfesion());

		Alert.showMessage(payloadProfesionResponse);

		if (UtilPayload.isOK(payloadProfesionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ProfesionFormBasic.zul";
	}

}
