package ve.smile.datos.configuracion.clasificacionPregunta.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.payload.response.PayloadClasificadorPreguntaResponse;
import ve.smile.payload.response.PayloadPreguntaClasificadaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VM_ClasificacionPreguntaIndex extends VM_WindowSimpleListPrincipal<ClasificadorPregunta> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorPregunta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse = 
				S.ClasificadorPreguntaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorPreguntaResponse;
	}


	@Override
	public void doDelete() {
		PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse =
				S.ClasificadorPreguntaService.eliminar(getSelectedObject().getIdClasificadorPregunta());

		Alert.showMessage(payloadClasificadorPreguntaResponse);

		if (UtilPayload.isOK(payloadClasificadorPreguntaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/configuracion/clasificacionPregunta/clasificacionPreguntaFormBasic.zul";
	}
		
	@Command("onSelectClasificadorPregunta")
	public void onSelectClasificadorPregunta() {
		ClasificadorPregunta clasificadorPregunta = getSelectedObject();
		
		if (clasificadorPregunta.getPreguntaClasificadas() == null || clasificadorPregunta.getPreguntaClasificadas().size() == 0) {
			
			PayloadPreguntaClasificadaResponse payloadPreguntaClasificadaResponse = 
					S.PreguntaClasificadaService.consultarPorClasificador(clasificadorPregunta.getIdClasificadorPregunta());
			
			if (!(Boolean)payloadPreguntaClasificadaResponse.getInformacion(IPayloadResponse.IS_OK)) {
				Alert.showMessage(payloadPreguntaClasificadaResponse);
				return;
			}
			
			clasificadorPregunta.setPreguntaClasificadas(payloadPreguntaClasificadaResponse.getObjetos());
		}	
	}
}