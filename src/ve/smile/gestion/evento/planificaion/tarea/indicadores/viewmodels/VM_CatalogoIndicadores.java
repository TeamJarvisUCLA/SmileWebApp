package ve.smile.gestion.evento.planificaion.tarea.indicadores.viewmodels;

import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.data.DialogData;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoIndicadores extends VM_ListPaginationCatalogueDialog<Indicador>{

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }
	
	
	
	@Override
	public IPayloadResponse<Indicador> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		
			PayloadIndicadorResponse payloadIndicadorResponse  = 
					S.IndicadorService.consultarPaginacion(cantidadRegistrosPagina, pagina);
			return payloadIndicadorResponse;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}

	
	

	
	
	
	
	

}
