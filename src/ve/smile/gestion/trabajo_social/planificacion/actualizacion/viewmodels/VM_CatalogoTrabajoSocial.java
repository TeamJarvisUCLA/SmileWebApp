package ve.smile.gestion.trabajo_social.planificacion.actualizacion.viewmodels;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;

public class VM_CatalogoTrabajoSocial extends
		VM_ListPaginationCatalogueDialog<TrabajoSocial> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoTrabajoSocial() {
		// NOTHING OK!
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajoSocialResponse;
	}

}
