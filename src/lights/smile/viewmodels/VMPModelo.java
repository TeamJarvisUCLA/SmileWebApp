package lights.smile.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import lights.seguridad.enums.OperacionEnum;
import lights.smile.dto.Modelo;
import lights.smile.payload.response.PayloadModeloResponse;

import org.zkoss.bind.annotation.Init;

public class VMPModelo extends VM_WindowSimpleListPrincipal<Modelo> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Modelo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadModeloResponse payloadModeloResponse = 
				S.ModeloService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadModeloResponse;
	}

	@Override
	public void doDelete() {
		PayloadModeloResponse payloadModeloResponse =
				S.ModeloService.eliminar(getSelectedObject().getIdModelo());

		Alert.showMessage(payloadModeloResponse);

		if (UtilPayload.isOK(payloadModeloResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "vista/viewModelo.zul";
	}

}
