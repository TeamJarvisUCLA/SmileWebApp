package ve.smile.datos.parametros.clasificador.actividad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorActividad;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.payload.response.PayloadClasificadorActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorActividadIndex extends
		VM_WindowSimpleListPrincipal<ClasificadorActividad> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<ClasificadorActividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadClasificadorActividadResponse payloadClasificadorActividadResponse = S.ClasificadorActividadService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadClasificadorActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadClasificadorActividadResponse payloadClasificadorActividadResponse = S.ClasificadorActividadService
				.eliminar(getSelectedObject().getIdClasificadorActividad());

		Alert.showMessage(payloadClasificadorActividadResponse);

		if (UtilPayload.isOK(payloadClasificadorActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/clasificador/actividad/ClasificadorActividadFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Clasificador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkClasificadorActividad.idClasificadorActividad", String.valueOf(getSelectedObject().getIdClasificadorActividad()));

		//Table Relation Actividad
		PayloadActividadResponse payloadActividadResponse =
				S.ActividadService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadActividadResponse)) {
			return String.valueOf(payloadActividadResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countActividades = 
				Double.valueOf(String.valueOf(payloadActividadResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
	
		if (countActividades > 0) {
			return "E:Error 0:No se puede eliminar el <b>Clasificador</b> seleccionado ya que está siendo utilizado en " + countActividades + " Registros de Actividades";
		}

		return "";
	}

}
