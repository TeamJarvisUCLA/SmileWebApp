package ve.smile.datos.parametros.clasificador.pregunta.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.payload.response.PayloadClasificadorPreguntaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorPreguntaIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorPregunta> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorPregunta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse = S.ClasificadorPreguntaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorPreguntaResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse = S.ClasificadorPreguntaService
				.eliminar(getSelectedObject().getIdClasificadorPregunta());

		Alert.showMessage(payloadClasificadorPreguntaResponse);

		if (UtilPayload.isOK(payloadClasificadorPreguntaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/pregunta/ClasificadorPreguntaFormBasic.zul";
	}

}
