package ve.smile.administracion.portalweb.correo;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_ValidarCorreoIndex extends VM_WindowSimpleListPrincipal<Configuracion>{
	
	private List<Configuracion> Configuracion = new ArrayList<Configuracion>();
	

	@Override
	public IPayloadResponse<Configuracion> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			this.Configuracion.addAll(payloadConfiguracionResponse
					.getObjetos());
		}
		
		for(Iterator<Configuracion> i = this.Configuracion.iterator(); i.hasNext(); ) {
			Configuracion item = i.next();
		    if  (item.getPropiedad() <4 ) {
		    	i.remove();
		    }
		}
		payloadConfiguracionResponse.setObjetos(this.Configuracion);
		
		
		return payloadConfiguracionResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/portalweb/correo/formValidarCorreo.zul";
	}

	@Override
	public void doDelete() {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService.eliminar(getSelectedObject().getIdConfiguracion());
		Alert.showMessage(payloadConfiguracionResponse);
		if (UtilPayload.isOK(payloadConfiguracionResponse)){
			
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
}
