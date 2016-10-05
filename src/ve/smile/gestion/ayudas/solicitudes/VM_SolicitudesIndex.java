package ve.smile.gestion.ayudas.solicitudes;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_SolicitudesIndex extends VM_WindowSimpleListPrincipal<SolicitudAyuda> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadSolicitudAyudaResponse;
	}

	@Override
	public void doDelete() {
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.eliminar(getSelectedObject().getIdSolicitudAyuda());

		Alert.showMessage(payloadSolicitudAyudaResponse);

		if (UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop//gestion/ayudas/solicitudes/SolicitudesFormBasic.zul";
	}

}
