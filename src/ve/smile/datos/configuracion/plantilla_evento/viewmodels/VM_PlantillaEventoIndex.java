package ve.smile.datos.configuracion.plantilla_evento.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PlantillaEventoIndex extends
		VM_WindowSimpleListPrincipal<Evento> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoResponse payloadEventoResponse = S.EventoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoResponse;
	}

	@Override
	public void doDelete() {
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/plantillaEvento/PlantillaEventoFormBasic.zul";
	}

	@Command("onSelectEvento")
	public void onSelectEvento() {
		Evento evento = getSelectedObject();

		if (evento.getEventoTareas() == null
				|| evento.getEventoTareas().isEmpty()) {
			PayloadTareaResponse payloadTareaResponse = S.TareaService
					.consultarPorEvento(evento.getIdEvento());
			if (!UtilPayload.isOK(payloadTareaResponse)) {
				Alert.showMessage(payloadTareaResponse);
			}
			evento.setEventoTareas(payloadTareaResponse.getObjetos());

		}
		if (evento.getEventoIndicadores() == null
				|| evento.getEventoIndicadores().isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarPorEvento(evento.getIdEvento());
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			}
			evento.setEventoIndicadores(payloadIndicadorResponse.getObjetos());
		}

	}
}