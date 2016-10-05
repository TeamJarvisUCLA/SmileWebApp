package ve.smile.gestion.apadrinamiento.asignacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_AsignacionReconocimientoIndex extends VM_WindowSimpleListPrincipal<ReconocimientoPersona> {
	
	@Init(superclass = true)
	public void childInit() {
		
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return "views/desktop/gestion/apadrinamiento/asignacion/AsignacionReconocimientoFormBasic.zul";
	}

	@Override
	public IPayloadResponse<ReconocimientoPersona> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusPadrino", String.valueOf(EstatusPadrinoEnum.ACTIVO.ordinal()));
		PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadReconocimientoPersonaResponse;
	}

	@Override
	public void doDelete() {
		PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService.eliminar(getSelectedObject().getIdReconocimientoPersona());
		Alert.showMessage(payloadReconocimientoPersonaResponse);
		if (UtilPayload.isOK(payloadReconocimientoPersonaResponse)){
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
	
}
