package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.dto.Pregunta;
import ve.smile.payload.response.PayloadPreguntaResponse;
import org.zkoss.bind.annotation.Init;

public class VMPPregunta extends VM_WindowSimpleListPrincipal<Pregunta> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Pregunta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPreguntaResponse payloadPreguntaResponse = 
				S.PreguntaService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadPreguntaResponse;
	}

	@Override
	public void doDelete() {
		PayloadPreguntaResponse payloadPreguntaResponse =
				S.PreguntaService.eliminar(getSelectedObject().getIdPregunta());

		Alert.showMessage(payloadPreguntaResponse);

		if (UtilPayload.isOK(payloadPreguntaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}


	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/maestro/view/PreguntaFormBasic.zul";
	}

}
