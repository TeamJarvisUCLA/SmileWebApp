package ve.smile.gestion.evento.planificaion.indicadores.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.dto.PlantillaEventoIndicador;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;

public class VM_CatalogoPlantillaIndicadores extends
		VM_ListPaginationCatalogueDialog<PlantillaEventoIndicador> {

	private Evento evento;

	@Init(superclass = true)
	public void childInit_VM_CatalogoPlantillaIndicadores() {
		// NOTHING OK!
		evento = (Evento) this.getDialogData().get(
				"evento");
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<PlantillaEventoIndicador> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("fkEvento.idEvento",
				String.valueOf(evento.getIdEvento()));
		PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse = S.PlantillaEventoIndicadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPlantillaEventoIndicadorResponse;
	}

}
