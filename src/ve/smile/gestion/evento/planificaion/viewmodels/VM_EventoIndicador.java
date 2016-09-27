package ve.smile.gestion.evento.planificaion.viewmodels;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilConverterDataList;
import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EventoIndicador extends VM_WindowSimpleListPrincipal<Indicador> {
	
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
	public IPayloadResponse<Indicador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadIndicadorResponse;
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
