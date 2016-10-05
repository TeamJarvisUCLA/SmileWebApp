package ve.smile.gestion.donativo.colaborador.viewmodels;

//import java.util.HashMap;
//import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Colaborador;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ColaboradorIndex extends VM_WindowSimpleListPrincipal<Colaborador>
{

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Colaborador> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadColaboradorResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadColaboradorResponse payloadPatrocinadorResponse = S.ColaboradorService.eliminar(getSelectedObject().getIdColaborador());
		Alert.showMessage(payloadPatrocinadorResponse);
		if (UtilPayload.isOK(payloadPatrocinadorResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/donativo/colaborador/ColaboradorFormBasic.zul";
	}

}
