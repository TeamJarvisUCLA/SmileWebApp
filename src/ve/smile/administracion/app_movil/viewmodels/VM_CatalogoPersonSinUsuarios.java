package ve.smile.administracion.app_movil.viewmodels;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;

public class VM_CatalogoPersonSinUsuarios extends
		VM_ListPaginationCatalogueDialog<Persona> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoTrabajadorVoluntario() {

	}

	@Override
	public IPayloadResponse<Persona> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
				.consultarPersonaSinUsuarioMovilPaginacion(
						cantidadRegistrosPagina, pagina, "");

		return payloadPersonaResponse;
	}

	@Override
	public void afterChildInit() {
	}

}
