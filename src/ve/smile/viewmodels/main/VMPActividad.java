package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;
import org.zkoss.bind.annotation.Init;

public class VMPActividad extends VM_WindowSimpleListPrincipal<Actividad> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Actividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadActividadResponse payloadActividadResponse = 
				S.ActividadService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadActividadResponse payloadActividadResponse =
				S.ActividadService.eliminar(getSelectedObject().getIdActividad());

		Alert.showMessage(payloadActividadResponse);

		if (UtilPayload.isOK(payloadActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/ActividadFormBasic.zul";
	}

}
