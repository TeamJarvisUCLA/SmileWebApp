package ve.smile.gestion.evento.planificaion.tarea.recursos.viewmodels;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadRecursoResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoRecursos extends
VM_ListPaginationCatalogueDialog<Recurso>{

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<Recurso> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadRecursoResponse;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}

}
