package ve.smile.administracion.portalweb.aplicacionMovil.apk.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;

import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;

import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.seguridad.enums.OperacionEnum;


public class VM_CargarApkIndex extends VM_WindowSimpleListPrincipal<Configuracion> {


	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Configuracion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadConfiguracionResponse;
	}

	@Override
	public void doDelete() {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.eliminar(getSelectedObject().getIdConfiguracion());

		Alert.showMessage(payloadConfiguracionResponse);

		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/aplicacionMovil/apk/CargarApkFormBasic.zul";
	}

}

