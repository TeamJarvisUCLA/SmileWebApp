package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Directorio;
import ve.smile.payload.response.PayloadDirectorioResponse;

import org.zkoss.bind.annotation.Init;

public class VMPDirectorio extends VM_WindowSimpleListPrincipal<Directorio> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Directorio> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDirectorioResponse payloadDirectorioResponse = 
				S.DirectorioService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadDirectorioResponse;
	}

	@Override
	public void doDelete() {
		PayloadDirectorioResponse payloadDirectorioResponse =
				S.DirectorioService.eliminar(getSelectedObject().getIdDirectorio());

		Alert.showMessage(payloadDirectorioResponse);

		if (UtilPayload.isOK(payloadDirectorioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/DirectorioFormBasic.zul";
	}

}
