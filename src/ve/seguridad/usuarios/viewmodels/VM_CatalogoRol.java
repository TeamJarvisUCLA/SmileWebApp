package ve.seguridad.usuarios.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.payload.response.PayloadRolResponse;

public class VM_CatalogoRol extends VM_ListPaginationCatalogueDialog<Rol> {

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Rol> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		
		PayloadRolResponse payloadIconSclassResponse  = 
				S.RolService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadIconSclassResponse;
	}
	
}
