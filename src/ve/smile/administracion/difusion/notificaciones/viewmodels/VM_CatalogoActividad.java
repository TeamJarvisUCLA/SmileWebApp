package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.TsPlanActividad;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;

public class VM_CatalogoActividad extends VM_ListPaginationCatalogueDialog<TsPlanActividad>
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
	public IPayloadResponse<TsPlanActividad> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTsPlanActividadResponse;
	}
}