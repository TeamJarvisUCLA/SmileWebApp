package ve.smile.datos.configuracion;

import java.util.Map;
import java.util.HashMap;
import karen.core.crux.alert.Alert;
import ve.smile.consume.services.S ;
import karen.core.util.payload.UtilPayload;
import ve.smile.seguridad.enums.OperacionEnum;
import lights.core.payload.response.IPayloadResponse;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;

import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;

import org.zkoss.bind.annotation.Init;

public class VMPPlantillaEvento extends VM_WindowSimpleListPrincipal<Evento> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoResponse payloadEventoResponse = S.EventoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/configuracion/plantillaEvento/PlantillaEventoFormBasic.zul";
	}

}
