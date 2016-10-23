package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.Difusion;
import ve.smile.payload.response.PayloadDifusionResponse;

public class VM_CatalogoDifusion extends VM_ListPaginationCatalogueDialog<Difusion>
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
	public IPayloadResponse<Difusion> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadDifusionResponse payloadDifusionResponse = S.DifusionService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDifusionResponse;
	}
}