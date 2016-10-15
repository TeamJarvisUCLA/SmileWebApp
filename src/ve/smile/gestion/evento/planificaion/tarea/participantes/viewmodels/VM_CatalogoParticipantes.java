package ve.smile.gestion.evento.planificaion.tarea.participantes.viewmodels;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoParticipantes extends VM_ListPaginationCatalogueDialog<Persona> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}
	
	@Override
	public IPayloadResponse<Persona> getObjectListToLoad(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadPersonaResponse;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}

}
