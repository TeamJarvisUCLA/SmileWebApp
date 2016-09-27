package ve.smile.gestion.voluntariado.voluntario.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_VoluntarioIndex extends VM_WindowSimpleListPrincipal<Voluntario>
{

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadVoluntarioResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.eliminar(getSelectedObject().getIdVoluntario());
		Alert.showMessage(payloadVoluntarioResponse);
		if (UtilPayload.isOK(payloadVoluntarioResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum)
	{
		return "views/desktop/gestion/voluntariado/voluntario/VoluntarioFormBasic.zul";
	}

}
