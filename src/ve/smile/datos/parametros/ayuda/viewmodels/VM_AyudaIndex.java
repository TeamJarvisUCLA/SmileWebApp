package ve.smile.datos.parametros.ayuda.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.payload.response.PayloadRequisitoAyudaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_AyudaIndex extends VM_WindowSimpleListPrincipal<Ayuda> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Ayuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadAyudaResponse;
	}

	@Override
	public void doDelete() {
		PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
				.eliminar(getSelectedObject().getIdAyuda());

		Alert.showMessage(payloadAyudaResponse);

		if (UtilPayload.isOK(payloadAyudaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/ayuda/AyudaFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Ayuda";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkAyuda.idAyuda", String.valueOf(getSelectedObject().getIdAyuda()));

		//Table Relation RequisitoAyuda
		PayloadRequisitoAyudaResponse payloadRequisitoAyudaResponse =
				S.RequisitoAyudaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadRequisitoAyudaResponse)) {
			return String.valueOf(payloadRequisitoAyudaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countRequisitosAyuda = 
				Double.valueOf(String.valueOf(payloadRequisitoAyudaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation SolicitudAyuda
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse =
				S.SolicitudAyudaService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			return String.valueOf(payloadSolicitudAyudaResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countSolicitudesAyuda = 
				Double.valueOf(String.valueOf(payloadSolicitudAyudaResponse.getInformacion(IPayloadResponse.COUNT))).intValue();


		if (countRequisitosAyuda > 0) {
			return "E:Error 0:No se puede eliminar la <b>Ayuda</b> seleccionada ya que está siendo utilizada en " + countRequisitosAyuda + " Requisitos de Ayuda";
		}
		
		if (countSolicitudesAyuda > 0) {
			return "E:Error 0:No se puede eliminar la <b>Ayuda</b> seleccionada ya que está siendo utilizada en " + countSolicitudesAyuda + " Solicitudes de Ayuda";
		}

		return "";
	}
}
