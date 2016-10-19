package ve.smile.administracion.portalweb.comentarios.album.viewmodels;


import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.enums.EstatusComentarioAlbumEnum;
import ve.smile.payload.response.PayloadComentarioAlbumResponse;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_ComentariosAlbumIndex extends VM_WindowSimpleListPrincipal<ComentarioAlbum>{

	@Init(superclass = true)
	public void childInit() {
		
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {	
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "views/desktop/administracion/portalweb/comentarios/album/ComentariosAlbumFormBasic.zul";
			}
			return "";
	}

	@Override
	public IPayloadResponse<ComentarioAlbum> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatusComentario", String.valueOf(EstatusComentarioAlbumEnum.PENDIENTE.ordinal()));
		PayloadComentarioAlbumResponse payloadComentarioAlbumResponse = S.ComentarioAlbumService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,TypeQuery.EQUAL,criterios);
		return payloadComentarioAlbumResponse;
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
		PayloadComentarioAlbumResponse payloadComentarioAlbumResponse =S.ComentarioAlbumService.eliminar(getSelectedObject().getIdComentarioAlbum());
		Alert.showMessage(payloadComentarioAlbumResponse);
		if (UtilPayload.isOK(payloadComentarioAlbumResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

}
