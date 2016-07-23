package ve.mensajes.mensajes_sistema.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import ve.smile.seguridad.dto.MensajeSistema;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.payload.response.PayloadMensajeSistemaResponse;

import org.zkoss.bind.annotation.Init;

public class VM_MensajesSistemaIndex extends VM_WindowSimpleListPrincipal<MensajeSistema> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<MensajeSistema> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadMensajeSistemaResponse payloadMensajeSistemaResponse = 
				S.MensajeSistemaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadMensajeSistemaResponse;
	}

	@Override
	public void doDelete() {
		PayloadMensajeSistemaResponse payloadMensajeSistemaResponse =
				S.MensajeSistemaService.eliminar(getSelectedObject().getIdMensajeSistema());

		Alert.showMessage(payloadMensajeSistemaResponse);

		if (UtilPayload.isOK(payloadMensajeSistemaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/mensajes/mensajesSistema/mensajesSistemaFormBasic.zul";
	}

	@Override
	public void executeCustom5() {
		MensajeSistema mensajeSistema = new MensajeSistema();

		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM5);

		DataCenter.updateSrcPageContent(mensajeSistema, operacion, getSrcFileZulForm(OperacionEnum.CUSTOM5));
	}
}