package ve.smile.datos.configuracion.plantilla_evento.viewmodels;

import java.util.List;
import java.util.ArrayList;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;

import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;

import ve.smile.dto.PlantillaEventoIndicador;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;

import ve.smile.dto.PlantillaEventoTarea;
import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;


public class VM_PlantillaEventoIndex extends VM_WindowSimpleListPrincipal<Evento> {

	private List <PlantillaEventoTarea> eventoTareas;
	private List <PlantillaEventoIndicador> eventoIndicadores;
	
	@Init(superclass = true)
	public void childInit()
	{
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadEventoResponse payloadEventoResponse = S.EventoService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoResponse;
	}

	@Override
	public void doDelete()
	{
		/*
		CONSULTAR LA ELIMINACI�N O NO DE PLANTILLAS
		*/
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/configuracion/plantillaEvento/PlantillaEventoFormBasic.zul";
	}

	@Command("onSelectEvento")
	public void onSelectEvento() {
		Evento evento = getSelectedObject();
		if (evento.getEventoIndicadors() == null || evento.getEventoTareas() == null)
		{
			if (this.eventoIndicadores == null)
			{
				this.eventoIndicadores = new ArrayList<>();
			}
			if (this.eventoIndicadores.isEmpty())
			{
				PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse = S.PlantillaEventoIndicadorService.consultarPorEvento(evento.getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaEventoIndicadorResponse))
				{
					Alert.showMessage(payloadPlantillaEventoIndicadorResponse);
				}
				evento.setEventoIndicadors(payloadPlantillaEventoIndicadorResponse.getObjetos());
				this.eventoIndicadores = evento.getEventoIndicadors();				
			}
			if (this.eventoTareas == null)
			{
				this.eventoTareas = new ArrayList<>();
			}
			if (this.eventoTareas.isEmpty())
			{
				PayloadPlantillaEventoTareaResponse payloadPlantillaEventoTareaResponse = S.PlantillaEventoTareaService.consultarPorEvento(evento.getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaEventoTareaResponse))
				{
					Alert.showMessage(payloadPlantillaEventoTareaResponse);
				}
				evento.setEventoTareas(payloadPlantillaEventoTareaResponse.getObjetos());
				this.eventoTareas = evento.getEventoTareas();				
			}
		}
	}
}