package ve.smile.gestion.eventosOcaionales.registroSolicitud.viewmodels;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoEvento extends VM_ListPaginationCatalogueDialog<Evento> {

	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		//NOTHING OK!
    }
	
	@Override
	public IPayloadResponse<Evento> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadEventoResponse payloadEventoResponse  = 
				S.EventoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoResponse;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}

}
