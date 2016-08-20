package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.ClasificadorActividad;
import ve.smile.payload.response.PayloadClasificadorActividadResponse;
import org.zkoss.bind.annotation.Init;

public class VMPClasificadorActividad extends VM_WindowSimpleListPrincipal<ClasificadorActividad> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorActividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorActividadResponse payloadClasificadorActividadResponse = 
				S.ClasificadorActividadService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorActividadResponse payloadClasificadorActividadResponse =
				S.ClasificadorActividadService.eliminar(getSelectedObject().getIdClasificadorActividad());

		Alert.showMessage(payloadClasificadorActividadResponse);

		if (UtilPayload.isOK(payloadClasificadorActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ClasificadorActividadFormBasic.zul";
	}

}
