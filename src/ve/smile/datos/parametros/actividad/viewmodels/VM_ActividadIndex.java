package ve.smile.datos.parametros.actividad.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ActividadIndex extends VM_WindowSimpleListPrincipal<Actividad> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Actividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadActividadResponse payloadActividadResponse = S.ActividadService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadActividadResponse payloadActividadResponse = S.ActividadService
				.eliminar(getSelectedObject().getIdActividad());

		Alert.showMessage(payloadActividadResponse);

		if (UtilPayload.isOK(payloadActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/actividad/ActividadFormBasic.zul";
	}

}
