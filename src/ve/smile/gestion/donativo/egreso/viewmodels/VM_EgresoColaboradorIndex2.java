package ve.smile.gestion.donativo.egreso.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Colaborador;
import ve.smile.payload.response.PayloadColaboradorResponse;

import org.zkoss.bind.annotation.Init;

public class VM_EgresoColaboradorIndex2 extends VM_WindowSimpleListPrincipal<Colaborador>{
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Colaborador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadColaboradorResponse payloadColaboradorResponse = 
				S.ColaboradorService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadColaboradorResponse;
	}

	@Override
	public void doDelete() {
		PayloadColaboradorResponse payloadColaboradorResponse =
				S.ColaboradorService.eliminar(getSelectedObject().getIdColaborador());

		Alert.showMessage(payloadColaboradorResponse);

		if (UtilPayload.isOK(payloadColaboradorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/donativo/egreso/EgresoColaboradorFormBasic.zul";
	}

}
