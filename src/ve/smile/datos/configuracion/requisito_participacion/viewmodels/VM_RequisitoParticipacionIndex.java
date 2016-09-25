package ve.smile.datos.configuracion.requisito_participacion.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Participacion;
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoParticipacionIndex extends
		VM_WindowSimpleListPrincipal<Participacion> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Participacion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadParticipacionResponse;
	}

	@Override
	public void doDelete() {
		PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
				.eliminar(getSelectedObject().getIdParticipacion());

		Alert.showMessage(payloadParticipacionResponse);

		if (UtilPayload.isOK(payloadParticipacionResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/requisitoParticipacion/requisitoParticipacionFormBasic.zul";
	}

	@Command("onSelectParticipacion")
	public void onSelectParticipacion() {
		Participacion participacion = getSelectedObject();

		if (participacion.getRequisitos() == null
				|| participacion.getRequisitos().size() == 0) {

			PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
					.consultarPorParticipacion(participacion
							.getIdParticipacion());

			if (!UtilPayload.isOK(payloadRequisitoResponse)) {
				Alert.showMessage(payloadRequisitoResponse);
				return;
			}

			participacion.setRequisitos(payloadRequisitoResponse.getObjetos());
		}
	}
}