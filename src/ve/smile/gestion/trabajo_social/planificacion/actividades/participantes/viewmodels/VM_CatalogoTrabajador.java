package ve.smile.gestion.trabajo_social.planificacion.actividades.participantes.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import karen.core.listfoot.enums.HowToSeeEnum;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadTrabajadorResponse;

public class VM_CatalogoTrabajador extends
		VM_ListPaginationCatalogueDialog<Trabajador> {

	@Init(superclass = true)
	public void childInit_VM_CatalogoTrabajadorVoluntario() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Trabajador> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusTrabajador",
				String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		return payloadTrabajadorResponse;
	}

	@Override
	public void afterChildInit() {
	}

	@Command("changeFilter")
	public void changeFilter() {
		updateListBox(1, HowToSeeEnum.NORMAL);
	}

}
