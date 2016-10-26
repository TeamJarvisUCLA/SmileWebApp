package ve.smile.administracion.portalweb.correo;


import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_ValidarCorreoIndex extends VM_WindowSimpleListPrincipal<Configuracion>{

	@Override
	public IPayloadResponse<Configuracion> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadConfiguracionResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/correo/formValidarCorreo.zul";
	}

	@Override
	public void doDelete() {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService.eliminar(getSelectedObject().getIdConfiguracion());
		Alert.showMessage(payloadConfiguracionResponse);
		if (UtilPayload.isOK(payloadConfiguracionResponse)){
			
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
}
