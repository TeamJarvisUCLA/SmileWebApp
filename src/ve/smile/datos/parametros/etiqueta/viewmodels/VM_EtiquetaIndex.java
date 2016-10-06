package ve.smile.datos.parametros.etiqueta.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Etiqueta;
import ve.smile.payload.response.PayloadEtiqAlbumResponse;
import ve.smile.payload.response.PayloadEtiqCarteleraResponse;
import ve.smile.payload.response.PayloadEtiqEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadEtiqTsPlanificadoResponse;
import ve.smile.payload.response.PayloadEtiquetaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EtiquetaIndex extends VM_WindowSimpleListPrincipal<Etiqueta> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Etiqueta> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadEtiquetaResponse payloadEtiquetaResponse = S.EtiquetaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadEtiquetaResponse;
	}

	@Override
	public void doDelete() {
		PayloadEtiquetaResponse payloadEtiquetaResponse = S.EtiquetaService
				.eliminar(getSelectedObject().getIdEtiqueta());

		Alert.showMessage(payloadEtiquetaResponse);

		if (UtilPayload.isOK(payloadEtiquetaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/etiqueta/EtiquetaFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar una Etiqueta";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkEtiqueta.idEtiqueta", String.valueOf(getSelectedObject().getIdEtiqueta()));

		//Table Relation EtiqCartelera
		PayloadEtiqCarteleraResponse payloadEtiqCarteleraResponse =
				S.EtiqCarteleraService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEtiqCarteleraResponse)) {
			return String.valueOf(payloadEtiqCarteleraResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEtiquetasCartelera = 
				Double.valueOf(String.valueOf(payloadEtiqCarteleraResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EtiqEventoPlanificado
		PayloadEtiqEventoPlanificadoResponse payloadEtiqEventoPlanificadoResponse =
				S.EtiqEventoPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEtiqEventoPlanificadoResponse)) {
			return String.valueOf(payloadEtiqEventoPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEtiquetasEvento = 
				Double.valueOf(String.valueOf(payloadEtiqEventoPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EtiqTsPlanificado
		PayloadEtiqTsPlanificadoResponse payloadEtiqTsPlanificadoResponse =
				S.EtiqTsPlanificadoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEtiqTsPlanificadoResponse)) {
			return String.valueOf(payloadEtiqTsPlanificadoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEtiquetasTs = 
				Double.valueOf(String.valueOf(payloadEtiqTsPlanificadoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		//Table Relation EtiqAlbum
		PayloadEtiqAlbumResponse payloadEtiqAlbumResponse =
				S.EtiqAlbumService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEtiqAlbumResponse)) {
			return String.valueOf(payloadEtiqAlbumResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEtiquetasAlbum = 
				Double.valueOf(String.valueOf(payloadEtiqAlbumResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countEtiquetasAlbum > 0) {
			return "E:Error 0:No se puede eliminar la <b>Etiqueta</b> seleccionada ya que está siendo utilizada en " + countEtiquetasAlbum + " Albumes";
		}

		if (countEtiquetasTs > 0) {
			return "E:Error 0:No se puede eliminar la <b>Etiqueta</b> seleccionada ya que está siendo utilizada en " + countEtiquetasTs + " Trabajos Sociales Planificados";
		}

		if (countEtiquetasEvento > 0) {
			return "E:Error 0:No se puede eliminar la <b>Etiqueta</b> seleccionada ya que está siendo utilizada en " + countEtiquetasEvento + " Eventos Planificados";
		}

		if (countEtiquetasCartelera > 0) {
			return "E:Error 0:No se puede eliminar la <b>Etiqueta</b> seleccionada ya que está siendo utilizada en " + countEtiquetasCartelera + " Carteleras";
		}

		return "";
	}

}
