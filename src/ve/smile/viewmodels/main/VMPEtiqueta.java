package ve.smile.viewmodels.main;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Etiqueta;
import ve.smile.payload.response.PayloadEtiquetaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPEtiqueta extends VM_WindowSimpleListPrincipal<Etiqueta> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Etiqueta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEtiquetaResponse payloadEtiquetaResponse = S.EtiquetaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEtiquetaResponse;
	}

	@Override
	public void doDelete() {
		PayloadEtiquetaResponse payloadEtiquetaResponse = S.EtiquetaService
				.eliminar(getSelectedObject().getIdEtiqueta());

		Alert.showMessage(payloadEtiquetaResponse);

		if (UtilPayload.isOK(payloadEtiquetaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/EtiquetaFormBasic.zul";
	}

}
