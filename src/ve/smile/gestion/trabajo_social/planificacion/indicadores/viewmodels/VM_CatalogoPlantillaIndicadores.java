package ve.smile.gestion.trabajo_social.planificacion.indicadores.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.PlantillaTrabajoSocialIndicador;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;

public class VM_CatalogoPlantillaIndicadores extends
		VM_ListPaginationCatalogueDialog<PlantillaTrabajoSocialIndicador> {

	private TrabajoSocial trabajoSocial;

	@Init(superclass = true)
	public void childInit_VM_CatalogoPlantillaIndicadores() {
		// NOTHING OK!
		trabajoSocial = (TrabajoSocial) this.getDialogData().get(
				"trabajoSocial");
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<PlantillaTrabajoSocialIndicador> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("fkTrabajoSocial.idTrabajoSocial",
				String.valueOf(trabajoSocial.getIdTrabajoSocial()));
		PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse = S.PlantillaTrabajoSocialIndicadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPlantillaTrabajoSocialIndicadorResponse;
	}

}
