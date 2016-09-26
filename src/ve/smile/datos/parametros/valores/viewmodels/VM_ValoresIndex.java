package ve.smile.datos.parametros.valores.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Valores;
import ve.smile.payload.response.PayloadValoresResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ValoresIndex extends VM_WindowSimpleListPrincipal<Valores> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Valores> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadValoresResponse payloadValoresResponse = S.ValoresService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadValoresResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadValoresResponse payloadValoresResponse = S.ValoresService.eliminar(getSelectedObject().getIdValores());
		Alert.showMessage(payloadValoresResponse);
		if (UtilPayload.isOK(payloadValoresResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum)
	{
		return "views/desktop/datos/parametros/valores/ValoresFormBasic.zul";
	}

}
