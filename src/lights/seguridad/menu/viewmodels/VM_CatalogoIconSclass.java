package lights.seguridad.menu.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.dto.IconSclass;
import lights.seguridad.payload.response.PayloadIconSclassResponse;
import lights.smile.consume.services.S;

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
