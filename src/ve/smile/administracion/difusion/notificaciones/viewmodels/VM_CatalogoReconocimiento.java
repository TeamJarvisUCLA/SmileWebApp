package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;

public class VM_CatalogoReconocimiento extends VM_ListPaginationCatalogueDialog<ReconocimientoPersona>
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
	public IPayloadResponse<ReconocimientoPersona> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadReconocimientoPersonaResponse;
	}
}