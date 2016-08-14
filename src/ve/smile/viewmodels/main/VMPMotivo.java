package ve.smile.viewmodels.main;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Motivo;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPMotivo extends VM_WindowSimpleListPrincipal<Motivo> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Motivo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadMotivoResponse;
	}

	@Override
	public void doDelete() {
		PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
				.eliminar(getSelectedObject().getIdMotivo());

		Alert.showMessage(payloadMotivoResponse);

		if (UtilPayload.isOK(payloadMotivoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/MotivoFormBasic.zul";
	}

}
