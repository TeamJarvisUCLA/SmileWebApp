package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.EventPlanTarea;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;

public class VM_CatalogoTarea extends VM_ListPaginationCatalogueDialog<EventPlanTarea>
{
	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass()
	{
		// NOTHING OK!
	}

	@Override
	public void afterChildInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<EventPlanTarea> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadEventPlanTareaResponse payloadEventPlanTareaResponse = S.EventPlanTareaService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventPlanTareaResponse;
	}
}