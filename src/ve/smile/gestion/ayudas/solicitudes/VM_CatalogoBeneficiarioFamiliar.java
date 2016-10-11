package ve.smile.gestion.ayudas.solicitudes;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.BeneficiarioFamiliar;
import ve.smile.payload.response.PayloadBeneficiarioFamiliarResponse;

public class VM_CatalogoBeneficiarioFamiliar extends VM_ListPaginationCatalogueDialog<BeneficiarioFamiliar>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<BeneficiarioFamiliar> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadBeneficiarioFamiliarResponse payloadBeneficiarioFamiliarResponse  = 
				S.BeneficiarioFamiliarService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadBeneficiarioFamiliarResponse;
	}
	
}
