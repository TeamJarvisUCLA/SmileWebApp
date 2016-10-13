package ve.smile.gestion.trabajo_social.planificacion.actividades.indicadores.viewmodels;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;

public class VM_CatalogoIndicadores extends
		VM_ListPaginationCatalogueDialog<Indicador> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Indicador> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadIndicadorResponse;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}

}
