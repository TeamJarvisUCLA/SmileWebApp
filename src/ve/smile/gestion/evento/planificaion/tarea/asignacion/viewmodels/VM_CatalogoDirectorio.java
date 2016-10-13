package ve.smile.gestion.evento.planificaion.tarea.asignacion.viewmodels;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.payload.response.PayloadDirectorioResponse;

public class VM_CatalogoDirectorio extends
		VM_ListPaginationCatalogueDialog<Directorio> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Directorio> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDirectorioResponse;
	}

}
