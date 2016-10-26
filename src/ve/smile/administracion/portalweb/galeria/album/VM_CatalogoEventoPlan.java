package ve.smile.administracion.portalweb.galeria.album;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;

public class VM_CatalogoEventoPlan extends VM_ListPaginationCatalogueDialog<EventoPlanificado>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<EventoPlanificado> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse  = 
				S.EventoPlanificadoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}
	
}
