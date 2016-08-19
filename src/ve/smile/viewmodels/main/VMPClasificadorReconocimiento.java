package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPClasificadorReconocimiento extends VM_WindowSimpleListPrincipal<ClasificadorReconocimiento> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorReconocimiento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = 
				S.ClasificadorReconocimientoService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorReconocimientoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse =
				S.ClasificadorReconocimientoService.eliminar(getSelectedObject().getIdClasificadorReconocimiento());

		Alert.showMessage(payloadClasificadorReconocimientoResponse);

		if (UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ClasificadorReconocimientoFormBasic.zul";
	}

}
