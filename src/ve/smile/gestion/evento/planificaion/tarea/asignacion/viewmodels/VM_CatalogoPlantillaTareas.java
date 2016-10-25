package ve.smile.gestion.evento.planificaion.tarea.asignacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.dto.PlantillaEventoTarea;
import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;

public class VM_CatalogoPlantillaTareas extends
		VM_ListPaginationCatalogueDialog<PlantillaEventoTarea> {

	private Evento evento;

	@Init(superclass = true)
	public void childInit_VM_CatalogoPlantillaTarea() {
		// NOTHING OK!
		evento = (Evento) this.getDialogData().get(
				"evento");
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<PlantillaEventoTarea> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("fkEvento.idEvento",
				String.valueOf(evento.getIdEvento()));
		PayloadPlantillaEventoTareaResponse payloadPlantillaEventoTareaResponse = S.PlantillaEventoTareaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPlantillaEventoTareaResponse;
	}

}
