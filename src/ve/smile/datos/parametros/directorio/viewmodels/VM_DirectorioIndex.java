package ve.smile.datos.parametros.directorio.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_DirectorioIndex extends
		VM_WindowSimpleListPrincipal<Directorio> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Directorio> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadDirectorioResponse;
	}

	@Override
	public void doDelete() {
		PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
				.eliminar(getSelectedObject().getIdDirectorio());

		Alert.showMessage(payloadDirectorioResponse);

		if (UtilPayload.isOK(payloadDirectorioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/directorio/DirectorioFormBasic.zul";
	}

}
