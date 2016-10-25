package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;

public class VM_CatalogoEvento extends VM_ListPaginationCatalogueDialog<EventoPlanificado>
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
	public IPayloadResponse<EventoPlanificado> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}
}