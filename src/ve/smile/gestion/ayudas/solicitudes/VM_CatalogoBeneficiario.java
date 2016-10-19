package ve.smile.gestion.ayudas.solicitudes;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.enums.EstatusBeneficiarioEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
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
		
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusBeneficiario",
				String.valueOf(EstatusBeneficiarioEnum.ACTIVO.ordinal()));
		PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		
		/*PayloadBeneficiarioResponse payloadBeneficiarioResponse  = 
				S.BeneficiarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);*/
		return payloadBeneficiarioResponse;
	}
	
}
