package ve.smile.datos.configuracion.plantilla_trabajo_social.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PlantillaTrabajoSocialIndex extends
		VM_WindowSimpleListPrincipal<TrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajoSocialResponse;
	}

	@Override
	public void doDelete() {
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/plantillaTrabajoSocial/PlantillaTrabajoSocialFormBasic.zul";
	}

	@Command("onSelectTrabajoSocial")
	public void onSelectTrabajoSocial() {
		TrabajoSocial trabajoSocial = getSelectedObject();

		if (trabajoSocial.getTrabajoSocialActividades() == null
				|| trabajoSocial.getTrabajoSocialActividades().isEmpty()) {
			PayloadActividadResponse payloadActividadResponse = S.ActividadService
					.consultarPorTrabajoSocial(trabajoSocial
							.getIdTrabajoSocial());
			if (!UtilPayload.isOK(payloadActividadResponse)) {
				Alert.showMessage(payloadActividadResponse);
			}
			trabajoSocial.setTrabajoSocialActividades(payloadActividadResponse
					.getObjetos());

		}
		if (trabajoSocial.getTrabajoSocialIndicadores() == null
				|| trabajoSocial.getTrabajoSocialIndicadores().isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarPorTrabajoSocial(trabajoSocial
							.getIdTrabajoSocial());
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			}
			trabajoSocial.setTrabajoSocialIndicadores(payloadIndicadorResponse
					.getObjetos());
		}

	}
}