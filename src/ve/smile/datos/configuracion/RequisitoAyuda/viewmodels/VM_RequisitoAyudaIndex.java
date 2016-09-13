package ve.smile.datos.configuracion.RequisitoAyuda.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.dto.RequisitoAyuda;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.payload.response.PayloadRequisitoAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VM_RequisitoAyudaIndex extends VM_WindowSimpleListPrincipal<Ayuda> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ayuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadAyudaResponse payloadAyudaResponse = 
				S.AyudaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadAyudaResponse;
	}


	@Override
	public void doDelete() {
		PayloadAyudaResponse payloadAyudaResponse =
				S.AyudaService.eliminar(getSelectedObject().getIdAyuda());

		Alert.showMessage(payloadAyudaResponse);

		if (UtilPayload.isOK(payloadAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/configuracion/requisitoAyuda/requisitoAyudaFormBasic.zul";
	}

	@Command("onSelectAyuda")
	public void onSelectAyuda() {
		Ayuda ayuda = getSelectedObject();
		
		if (ayuda.getRequisitoAyudas() == null || ayuda.getRequisitoAyudas().size() == 0) {
			
			PayloadRequisitoAyudaResponse payloadRequisitoAyudaResponse = 
					S.RequisitoAyudaService.consultarPorAyuda(ayuda.getIdAyuda());
			
			if (!(Boolean)payloadRequisitoAyudaResponse.getInformacion(IPayloadResponse.IS_OK)) {
				Alert.showMessage(payloadRequisitoAyudaResponse);
				return;
			}
			
			ayuda.setRequisitoAyudas(payloadRequisitoAyudaResponse.getObjetos());
		}	
	}
}