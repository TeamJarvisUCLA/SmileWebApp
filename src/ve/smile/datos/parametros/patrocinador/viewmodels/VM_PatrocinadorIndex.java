package ve.smile.datos.parametros.patrocinador.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Patrocinador;
import ve.smile.payload.response.PayloadEventPlanPatrocinadorResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PatrocinadorIndex extends VM_WindowSimpleListPrincipal<Patrocinador>
{

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Patrocinador> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadPatrocinadorResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService.eliminar(getSelectedObject().getIdPatrocinador());
		Alert.showMessage(payloadPatrocinadorResponse);
		if (UtilPayload.isOK(payloadPatrocinadorResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/patrocinador/PatrocinadorFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Patrocinador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkPatrocinador.idPatrocinador", String.valueOf(getSelectedObject().getIdPatrocinador()));

		//Table Relation EventPlanPatrocinador
		PayloadEventPlanPatrocinadorResponse payloadEventPlanPatrocinadorResponse =
				S.EventPlanPatrocinadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanPatrocinadorResponse)) {
			return String.valueOf(payloadEventPlanPatrocinadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countEventos = 
				Double.valueOf(String.valueOf(payloadEventPlanPatrocinadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countEventos > 0) {
			return "E:Error 0:No se puede eliminar el <b>Parentesco</b> seleccionado ya que está siendo utilizado en " + countEventos + " Eventos Planificados";
		}

		return "";
	}

}
