package ve.smile.gestion.evento.planificaion.registro.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoVoluntario extends VM_ListPaginationCatalogueDialog<Voluntario>{
	
	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Voluntario> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusPostulado", EstatusVoluntarioEnum.ACTIVO.ordinal()+"");
		PayloadVoluntarioResponse payloadVoluntarioResponse  = 
				S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}


}
