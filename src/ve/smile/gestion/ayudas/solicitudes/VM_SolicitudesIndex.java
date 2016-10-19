package ve.smile.gestion.ayudas.solicitudes;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_SolicitudesIndex extends VM_WindowSimpleListPrincipal<SolicitudAyuda> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusSolicitud",
				String.valueOf(EstatusSolicitudEnum.PENDIENTE.ordinal()));
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		/*Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatus",
		String.valueOf(EstatusSolicitudEnum.EN_PROCESO.ordinal()));

		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = 

		S.SolicitudAyudaService.consultarCriterios(TypeQuery.EQUAL, criterios);*/

		/*PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
*/
		return payloadSolicitudAyudaResponse;
	}

	@Override
	public void doDelete() {
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.eliminar(getSelectedObject().getIdSolicitudAyuda());

		Alert.showMessage(payloadSolicitudAyudaResponse);

		if (UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop//gestion/ayudas/solicitudes/SolicitudesFormBasic.zul";
	}

}
