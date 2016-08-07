package ve.smile.web.viewmodels;

import karen.core.simple_list_elements.viewmodels.VM_WindowSimpleListElements;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;

public class VMListPrueba extends VM_WindowSimpleListElements<Actividad> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Actividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadActividadResponse payloadActividadResponse = 
				S.ActividadService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadActividadResponse;
	}



}
