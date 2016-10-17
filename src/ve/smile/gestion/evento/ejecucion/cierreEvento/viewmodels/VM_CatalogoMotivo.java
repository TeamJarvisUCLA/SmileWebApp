package ve.smile.gestion.evento.ejecucion.cierreEvento.viewmodels;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Motivo;
import ve.smile.payload.response.PayloadMotivoResponse;

public class VM_CatalogoMotivo extends
		VM_ListPaginationCatalogueDialog<Motivo> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Motivo> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadMotivoResponse;
	}

}
