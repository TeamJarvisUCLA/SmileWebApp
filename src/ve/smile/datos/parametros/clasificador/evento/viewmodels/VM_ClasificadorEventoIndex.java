package ve.smile.datos.parametros.clasificador.evento.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.payload.response.PayloadClasificadorEventoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorEventoIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorEvento> {

	private static final String brackgroundColor = "background: ";

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public String getBrackgroundColor(String color) {
		return brackgroundColor.concat(color);
	}

	@Override
	public IPayloadResponse<ClasificadorEvento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorEventoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
				.eliminar(getSelectedObject().getIdClasificadorEvento());

		Alert.showMessage(payloadClasificadorEventoResponse);

		if (UtilPayload.isOK(payloadClasificadorEventoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/evento/ClasificadorEventoFormBasic.zul";
	}

}
