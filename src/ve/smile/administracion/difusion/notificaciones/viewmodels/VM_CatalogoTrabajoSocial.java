package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.TsPlan;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_CatalogoTrabajoSocial extends VM_ListPaginationCatalogueDialog<TsPlan>
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
	public IPayloadResponse<TsPlan> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTsPlanResponse;
	}
}