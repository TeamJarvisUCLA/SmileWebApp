package ve.smile.datos.parametros.patrocinador.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Patrocinador;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PatrocinadorIndex extends
		VM_WindowSimpleListPrincipal<Patrocinador> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Patrocinador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadPatrocinadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
				.eliminar(getSelectedObject().getIdPatrocinador());

		Alert.showMessage(payloadPatrocinadorResponse);

		if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/patrocinador/PatrocinadorFormBasic.zul";
	}

}
