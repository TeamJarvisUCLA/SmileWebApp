package ve.smile.datos.configuracion.plantilla_trabajo_social.viewmodels;

import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
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
		/*
		 * CONSULTAR LA ELIMINACI�N O NO DE PLANTILLAS
		 */
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/plantillaTrabajoSocial/PlantillaTrabajoSocialFormBasic.zul";
	}

	@Command("onSelectTrabajoSocial")
	public void onSelectTrabajoSocial() {
		TrabajoSocial trabajo = getSelectedObject();
//		if (this.trabajoSocialIndicadores == null) {
//			this.trabajoSocialIndicadores = new ArrayList<>();
//		}
//		if (this.trabajoSocialIndicadores.isEmpty()) {
//			PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse = S.PlantillaTrabajoSocialIndicadorService
//					.consultarPorEvento(evento.getIdEvento());
//			if (!UtilPayload
//					.isOK(payloadPlantillaTrabajoSocialIndicadorResponse)) {
//				Alert.showMessage(payloadPlantillaTrabajoSocialIndicadorResponse);
//			}
//			trabajo.setTrabajoSocialIndicadors(payloadPlantillaTrabajoSocialIndicadorResponse
//					.getObjetos());
//			this.trabajoSocialIndicadores = trabajo
//					.getTrabajoSocialIndicadors();
//		}
//		if (this.trabajoSocialActividades == null) {
//			this.trabajoSocialActividades = new ArrayList<>();
//		}
//		if (this.trabajoSocialActividades.isEmpty()) {
//			PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse = S.PlantillaTrabajoSocialActividadService
//					.consultarPorEvento(evento.getIdEvento());
//			if (!UtilPayload
//					.isOK(payloadPlantillaTrabajoSocialActividadResponse)) {
//				Alert.showMessage(payloadPlantillaTrabajoSocialActividadResponse);
//			}
//			trabajo.setTrabajoSocialActividades(payloadPlantillaTrabajoSocialActividadResponse
//					.getObjetos());
//			this.trabajoSocialActividades = trabajo
//					.getTrabajoSocialActividades();
//		}
	}
}