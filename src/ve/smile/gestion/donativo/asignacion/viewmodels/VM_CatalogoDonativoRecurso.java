package ve.smile.gestion.donativo.asignacion.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.DonativoRecurso;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;

public class VM_CatalogoDonativoRecurso extends VM_ListPaginationCatalogueDialog<DonativoRecurso>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<DonativoRecurso> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse  = 
				S.DonativoRecursoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDonativoRecursoResponse;
	}
	
}
