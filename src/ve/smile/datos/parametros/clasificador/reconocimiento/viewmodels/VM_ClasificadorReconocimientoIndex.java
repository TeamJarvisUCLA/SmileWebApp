package ve.smile.datos.parametros.clasificador.reconocimiento.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorReconocimientoIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorReconocimiento> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorReconocimiento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorReconocimientoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
				.eliminar(getSelectedObject().getIdClasificadorReconocimiento());

		Alert.showMessage(payloadClasificadorReconocimientoResponse);

		if (UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/reconocimiento/ClasificadorReconocimientoFormBasic.zul";
	}

}
