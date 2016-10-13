package ve.smile.administracion.portalweb.noticias.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.Cartelera;
import ve.smile.dto.Multimedia;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

import org.zkoss.bind.annotation.Init;

public class VM_CarteleraIndex extends VM_WindowSimpleListPrincipal<Cartelera> {

	private List<Multimedia> multimedia;
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}
	
	public List<Multimedia> getMultimedia()
	{
		if (this.multimedia == null)
		{
			this.multimedia = new ArrayList<>();
		}
		if (this.multimedia.isEmpty())
		{
			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService.consultarTodos();
			if (!UtilPayload.isOK(payloadMultimediaResponse)) {
				Alert.showMessage(payloadMultimediaResponse);
			}
			this.multimedia.addAll(payloadMultimediaResponse.getObjetos());
		}
		return multimedia;
	}
	
	public void setMultimedia(List<Multimedia> multimedia)
	{
		this.multimedia = multimedia;
	}

	@Override
	public IPayloadResponse<Cartelera> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadCarteleraResponse payloadCarteleraResponse = 
				S.CarteleraService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadCarteleraResponse;
	}

	@Override
	public void doDelete() {
		PayloadCarteleraResponse payloadCarteleraResponse =
				S.CarteleraService.eliminar(getSelectedObject().getIdCartelera());

		Alert.showMessage(payloadCarteleraResponse);

		if (UtilPayload.isOK(payloadCarteleraResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/noticias/CarteleraFormBasic.zul";
	}

}
