package ve.smile.gestion.evento.planificaion.tarea.participantes.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_CatalogoVoluntario extends
		VM_ListPaginationCatalogueDialog<Voluntario> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoVoluntario() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Voluntario> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusVoluntario",
				String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		return payloadVoluntarioResponse;
	}

	@Override
	public void afterChildInit() {
	}

	@Command("changeFilter")
	public void changeFilter() {
		getControllerListPaginationCatalogueDialog().updateListBoxAndFooter();
	}

}
