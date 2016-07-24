package ve.seguridad.menu.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.IconSclass;
import ve.smile.seguridad.payload.response.PayloadIconSclassResponse;

public class VM_CatalogoIconSclass extends VM_ListPaginationCatalogueDialog<IconSclass> {

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<IconSclass> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		
		PayloadIconSclassResponse payloadIconSclassResponse  = 
				S.IconSclassService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadIconSclassResponse;
	}
	
}
