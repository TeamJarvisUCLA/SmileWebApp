package ve.smile.gestion.evento.planificaion.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilConverterDataList;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EventoTarea extends VM_WindowSimpleListPrincipal<Tarea> {

	private EventoPlanificado eventoPlanificado = new EventoPlanificado();
	private String fechaPlanificada;
	@Init(superclass = true)
	public void childInit() {
		eventoPlanificado = (EventoPlanificado) DataCenter.getEntity();
		fechaPlanificada = UtilConverterDataList.convertirLongADate(eventoPlanificado.getFechaPlanificada());
	}
	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPayloadResponse<Tarea> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTareaResponse payloadTareaResponse = S.TareaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTareaResponse;
	}
	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}
	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}
	public String getFechaPlanificada() {
		return fechaPlanificada;
	}
	public void setFechaPlanificada(String fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}
	
	

}
