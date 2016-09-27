package ve.smile.gestion.voluntariado.capacitacion.planificacion.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;

import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;

import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CapacitacionPlanificadaIndex extends VM_WindowSimpleListPrincipal<CapacitacionPlanificada>
{

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<CapacitacionPlanificada> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadCapacitacionPlanificadaResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.eliminar(getSelectedObject().getIdCapacitacionPlanificada());
		Alert.showMessage(payloadCapacitacionPlanificadaResponse);
		if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum)
	{
		return "views/desktop/gestion/voluntariado/capacitacion/planificacion/CapacitacionPlanificadaFormBasic.zul";
	}

}
