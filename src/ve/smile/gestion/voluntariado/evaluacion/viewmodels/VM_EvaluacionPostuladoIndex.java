package ve.smile.gestion.voluntariado.evaluacion.viewmodels;

import java.util.HashMap;
import java.util.Map;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EvaluacionPostuladoIndex extends
		VM_WindowSimpleListPrincipal<Voluntario> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		EstatusVoluntarioEnum.POSTULADO.ordinal();
		criterios.put("estatusPostulado",String.valueOf(EstatusVoluntarioEnum.POSTULADO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	@Override
	public void doDelete() {
		// No
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/voluntariado/evaluacion/EvaluacionPostuladoFormBasic.zul";
	}

}
