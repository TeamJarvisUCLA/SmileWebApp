package ve.smile.administracion.portalweb.comentarios.noticias.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ComentarioCartelera;
import ve.smile.enums.EstatusComentarioCarteleraEnum;
import ve.smile.payload.response.PayloadComentarioCarteleraResponse;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;


public class VM_ComentariosCarteleraIndex extends VM_WindowSimpleListPrincipal<ComentarioCartelera>{

	@Init(superclass = true)
	public void childInit() {
		
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {	
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "views/desktop/administracion/portalweb/comentarios/noticias/ComentariosCarteleraFormBasic.zul";
			}
			return "";
	}

	@Override
	public IPayloadResponse<ComentarioCartelera> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusComentario", String.valueOf(EstatusComentarioCarteleraEnum.PENDIENTE.ordinal()));
		PayloadComentarioCarteleraResponse payloadComentarioCarteleraResponse = S.ComentarioCarteleraService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,TypeQuery.EQUAL,criterios);
		return payloadComentarioCarteleraResponse;
	}

	@Override
	public void executeCustom1() {
		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);
		DataCenter.updateSrcPageContent(selectedObject, operacion, getSrcFileZulForm(OperacionEnum.CUSTOM1));
	}

	@Override
	public String isValidPreconditionsCustom1() {
		if (selectedObject == null) {
			return "I:Information Code: 107-Debe seleccionar un Registro";
		}
		return "";
	}

	@Override
	public void doDelete() {
		PayloadComentarioCarteleraResponse payloadComentarioCarteleraResponse =S.ComentarioCarteleraService.eliminar(getSelectedObject().getIdComentarioCartelera());
		Alert.showMessage(payloadComentarioCarteleraResponse);
		if (UtilPayload.isOK(payloadComentarioCarteleraResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

}
