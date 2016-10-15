package ve.smile.gestion.ayudas.solicitudes;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.payload.response.PayloadAyudaResponse;

public class VM_CatalogoAyuda extends VM_ListPaginationCatalogueDialog<Ayuda>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<Ayuda> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadAyudaResponse payloadAyudaResponse  = 
				S.AyudaService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadAyudaResponse;
	}
	
}
