package ve.smile.datos.configuracion.requisito_ayuda.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoAyudaIndex extends VM_WindowSimpleListPrincipal<Ayuda> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ayuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadAyudaResponse;
	}

	@Override
	public void doDelete() {
		PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
				.eliminar(getSelectedObject().getIdAyuda());

		Alert.showMessage(payloadAyudaResponse);

		if (UtilPayload.isOK(payloadAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/requisitoAyuda/requisitoAyudaFormBasic.zul";
	}

	@Command("onSelectAyuda")
	public void onSelectAyuda() {
		Ayuda ayuda = getSelectedObject();

		if (ayuda.getRequisitos() == null || ayuda.getRequisitos().size() == 0) {

			PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
					.consultarPorAyuda(ayuda.getIdAyuda());

			if (!UtilPayload.isOK(payloadRequisitoResponse)) {
				Alert.showMessage(payloadRequisitoResponse);
				return;
			}

			ayuda.setRequisitos(payloadRequisitoResponse.getObjetos());
		}
	}
}