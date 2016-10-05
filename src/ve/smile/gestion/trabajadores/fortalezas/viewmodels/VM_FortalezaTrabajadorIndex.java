package ve.smile.gestion.trabajadores.fortalezas.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Trabajador;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FortalezaTrabajadorIndex extends
		VM_WindowSimpleListPrincipal<Trabajador> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTrabajadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.eliminar(getSelectedObject().getIdTrabajador());

		Alert.showMessage(payloadTrabajadorResponse);

		if (UtilPayload.isOK(payloadTrabajadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/trabajadores/fortalezas/FortalezaTrabajadorFormBasic.zul";
	}

	@Command("onSelectTrabajador")
	public void onSelectTrabajador() {
		Trabajador trabajador = getSelectedObject();

		if (trabajador.getFortalezas() == null
				|| trabajador.getFortalezas().size() == 0) {

			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarPorTrabajador(trabajador
							.getIdTrabajador());

			if (!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
				return;
			}
			
	trabajador.setFortalezas(payloadFortalezaResponse.getObjetos());
		}
	}
}