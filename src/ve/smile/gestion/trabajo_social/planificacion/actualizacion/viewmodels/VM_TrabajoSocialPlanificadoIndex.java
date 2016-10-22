package ve.smile.gestion.trabajo_social.planificacion.actualizacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TrabajoSocialPlanificadoIndex extends
		VM_WindowSimpleListPrincipal<TsPlan> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTsPlan", String
				.valueOf(EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO
						.ordinal()));

		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTsPlanResponse;
	}

	@Override
	public void doDelete() {

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/trabajoSocial/planificacion/actualizacion/TrabajoSocialPlanificadoFormBasic.zul";
	}
}
