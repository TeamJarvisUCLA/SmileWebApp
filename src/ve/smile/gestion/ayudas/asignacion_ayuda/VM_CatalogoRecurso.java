package ve.smile.gestion.ayudas.asignacion_ayuda;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadRecursoResponse;

public class VM_CatalogoRecurso extends VM_ListPaginationCatalogueDialog<Recurso>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<Recurso> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadRecursoResponse payloadRecursoResponse  = 
				S.RecursoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadRecursoResponse;
	}
	
}
