package ve.smile.datos.parametros.clasificador.evento.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.payload.response.PayloadClasificadorEventoResponse;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorEventoIndex extends
VM_WindowSimpleListPrincipal<ClasificadorEvento> {

	private static final String brackgroundColor = "background: ";

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public String getBrackgroundColor(String color) {
		return brackgroundColor.concat(color);
	}

	@Override
	public IPayloadResponse<ClasificadorEvento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorEventoResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
				.eliminar(getSelectedObject().getIdClasificadorEvento());

		Alert.showMessage(payloadClasificadorEventoResponse);

		if (UtilPayload.isOK(payloadClasificadorEventoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/evento/ClasificadorEventoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorEvento.idClasificadorEvento", String.valueOf(getSelectedObject().getIdClasificadorEvento()));

		//Table Relation Evento
		PayloadEventoResponse payloadEventoResponse =
				S.EventoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventoResponse)) {
			return String.valueOf(payloadEventoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventos = 
				Double.valueOf(String.valueOf(payloadEventoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countEventos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countEventos + " Registros de Eventos";
		}

		return "";
	}


}
