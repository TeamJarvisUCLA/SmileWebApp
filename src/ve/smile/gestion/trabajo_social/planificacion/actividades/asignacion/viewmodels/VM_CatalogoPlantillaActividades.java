package ve.smile.gestion.trabajo_social.planificacion.actividades.asignacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.PlantillaTrabajoSocialActividad;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;

public class VM_CatalogoPlantillaActividades extends
		VM_ListPaginationCatalogueDialog<PlantillaTrabajoSocialActividad> {

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
	public IPayloadResponse<PlantillaTrabajoSocialActividad> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("fkTrabajoSocial.idTrabajoSocial",
				String.valueOf(trabajoSocial.getIdTrabajoSocial()));
		PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse = S.PlantillaTrabajoSocialActividadService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPlantillaTrabajoSocialActividadResponse;
	}

}
