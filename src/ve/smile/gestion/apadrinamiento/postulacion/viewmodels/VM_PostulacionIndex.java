package ve.smile.gestion.apadrinamiento.postulacion.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_PostulacionIndex extends VM_WindowSimpleListPrincipal<Padrino>{
	
	@Init(superclass = true)
	public void childInit(){
		
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {	
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "views/desktop/gestion/apadrinamiento/postulacion/PostulacionFormBasic.zul";
			}
			return "";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusPadrino", String.valueOf(EstatusPadrinoEnum.POSTULADO.ordinal()));
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,TypeQuery.EQUAL,criterios);
		return payloadPadrinoResponse;
	}

	@Override
	public void executeCustom1() {
		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);
		DataCenter.updateSrcPageContent(selectedObject, operacion, getSrcFileZulForm(OperacionEnum.CUSTOM1));
		
	}
	

	@Override
	public String isValidPreconditionsCustom1() {
		if (selectedObject == null) {
			return "I:Information Code: 107-Debe seleccionar un Padrino";
		}
		return "";
	}

	@Override
	public void doDelete() {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.eliminar(getSelectedObject().getIdPadrino());
		Alert.showMessage(payloadPadrinoResponse);
		if (UtilPayload.isOK(payloadPadrinoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
	
}
