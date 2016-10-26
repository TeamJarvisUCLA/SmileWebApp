package ve.smile.administracion.portalweb.galeria.album;


import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.payload.response.PayloadAlbumResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_albumesIndex extends VM_WindowSimpleListPrincipal<Album>{

	@Override
	public IPayloadResponse<Album> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadAlbumResponse payloadAlbumResponse = S.AlbumService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadAlbumResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/galeria/album/albumesFormBasic.zul";
	}

	@Override
	public void doDelete() {
		PayloadAlbumResponse payloadAlbumResponse = S.AlbumService.eliminar(getSelectedObject().getIdAlbum());
		Alert.showMessage(payloadAlbumResponse);
		if (UtilPayload.isOK(payloadAlbumResponse)){
			
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
}
