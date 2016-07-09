package lights.smile.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.smile.consume.services.S;
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
	public Map<OperacionEnum, Boolean> getScheduledsTo() {
		Map<OperacionEnum, Boolean> isScheduleds = new HashMap<OperacionEnum, Boolean>();

		isScheduleds.put(OperacionEnum.INCLUIR, true);
		isScheduleds.put(OperacionEnum.MODIFICAR, true);
		isScheduleds.put(OperacionEnum.ELIMINAR, true);
		isScheduleds.put(OperacionEnum.CONSULTAR, true);

		return isScheduleds;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "vista/viewModelo.zul";
	}

}
