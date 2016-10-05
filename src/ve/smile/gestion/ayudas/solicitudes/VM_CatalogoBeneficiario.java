package ve.smile.gestion.ayudas.solicitudes;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.payload.response.PayloadBeneficiarioResponse;

public class VM_CatalogoBeneficiario extends VM_ListPaginationCatalogueDialog<Beneficiario>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<Beneficiario> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadBeneficiarioResponse payloadBeneficiarioResponse  = 
				S.BeneficiarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadBeneficiarioResponse;
	}
	
}
