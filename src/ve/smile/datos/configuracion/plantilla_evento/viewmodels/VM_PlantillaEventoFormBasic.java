package ve.smile.datos.configuracion.plantilla_evento.viewmodels;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;

import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;

import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;

import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;

import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;

public class VM_PlantillaEventoFormBasic extends VM_WindowForm {
	
	private List <Indicador> indicadores;
	private Set  <Indicador> indicadoresSel;
	private List <Indicador> eventoIndicadores;
	private Set  <Indicador> eventoIndicadoresSel;
	
	private List <Tarea> tareas;
	private Set  <Tarea> tareasSel;
	private List <Tarea> eventoTareas;
	private Set  <Tarea> eventoTareasSel;
	
	public VM_PlantillaEventoFormBasic () {
		indicadores = new ArrayList <Indicador>();
		indicadoresSel = new HashSet <Indicador> ();
		eventoIndicadores = new ArrayList <Indicador> ();
		eventoIndicadoresSel = new HashSet <Indicador>();
	}
	
	@Init(superclass = true)
	public void childInit()
	{
		
	}

	// PROPIEDADES DE LAS LISTAS
	// Indicadores
	public List<Indicador> getIndicadores()
	{
		if (this.indicadores == null)
		{
			this.indicadores = new ArrayList<>();
		}	
		if (this.indicadores.isEmpty())
		{
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse))
			{
				Alert.showMessage(payloadIndicadorResponse);
			}
			this.indicadores.addAll(payloadIndicadorResponse.getObjetos());
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores)
	{
		this.indicadores = indicadores;
	}
	
	public Set<Indicador> getIndicadoresSel()
	{
		return indicadoresSel;
	}

	public void setIndicadoresSel (Set<Indicador> indicadoresSel)
	{
		this.indicadoresSel = indicadoresSel;
	}

	public List <Indicador> getListaIndicadores()
	{
		return indicadores;
	}
	
	// Indicadores del evento
	public Set<Indicador> getEventoIndicadoresSel()
	{
		return  eventoIndicadoresSel;
	}

	public void setEventoIndicadoresSel (Set<Indicador> eventoIndicadoresSel)
	{
		this.eventoIndicadoresSel = eventoIndicadoresSel;
	}

	public List <Indicador> getListaEventoIndicadores()
	{
		if (this.eventoIndicadores == null)
		{
			this.eventoIndicadores = new ArrayList<>();
		}
		if (this.eventoIndicadores.isEmpty())
		{
			PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse = S.PlantillaEventoIndicadorService.consultarPorEvento(getEvento().getIdEvento());	
			if (!UtilPayload.isOK(payloadPlantillaEventoIndicadorResponse)) {
				Alert.showMessage(payloadPlantillaEventoIndicadorResponse);
			}
			Evento e = new Evento ();				
			e.setEventoIndicadors(payloadPlantillaEventoIndicadorResponse.getObjetos());
			//this.eventoIndicadores = e.getEventoIndicadors(); Listas de tipo diferentes 
		}
		return eventoIndicadores;
	}
	
	// Tareas
	
	public List<Tarea> getTareas()
	{
		if (this.tareas == null)
		{
			this.tareas = new ArrayList<>();
		}
		
		if (this.tareas.isEmpty())
		{
			PayloadTareaResponse payloadTareaResponse = S.TareaService.consultarTodos();
			if (!UtilPayload.isOK(payloadTareaResponse))
			{
				Alert.showMessage(payloadTareaResponse);
			}
			this.tareas.addAll(payloadTareaResponse.getObjetos());
		}
		return tareas;
	}

	public void setTareas(List<Tarea> tareas)
	{
		this.tareas = tareas;
	}
	
	public Set<Tarea> getTareasSel()
	{
		return tareasSel;
	}

	public void setTareasSel (Set<Tarea> tareasSel) {
		this.tareasSel = tareasSel;
	}

	public List <Tarea> getListaTareas() {
		return tareas;
	}
	
	// Tareas del evento
	public Set<Tarea> getEventoTareasSel()
	{
		return  eventoTareasSel;
	}

	public void setEventoTareasSel (Set<Tarea> eventoTareasSel)
	{
		this.eventoTareasSel = eventoTareasSel;
	}

	public List <Tarea> getListaEventoTareas()
	{
		return eventoTareas;
	}
	
	@Command
	@NotifyChange({"indicadores","eventoIndicadores","indicadoresSel","eventoIndicadoresSel"})
	public void indicadoresAPlantilla(){
				if(indicadoresSel != null && indicadoresSel.size() > 0){
					eventoIndicadores.addAll(indicadoresSel);
					indicadores.removeAll(indicadoresSel);
					eventoIndicadoresSel.addAll(indicadoresSel);
					indicadoresSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"indicadores","eventoIndicadores","indicadoresSel","eventoIndicadoresSel"})
	public void indicadoresFueraPlantilla(){
		if(eventoIndicadoresSel != null && eventoIndicadoresSel.size() > 0){
			indicadores.addAll(eventoIndicadoresSel);
			eventoIndicadores.removeAll(eventoIndicadoresSel);
			indicadoresSel.addAll(eventoIndicadoresSel);
			eventoIndicadoresSel.clear();
		}
	}
	
	@Command
	@NotifyChange({"tareas","eventoTareas","tareasSel","eventoTareasSel"})
	public void tareasAPlantilla(){
				if(tareasSel != null && tareasSel.size() > 0){
					eventoTareas.addAll(tareasSel);
					tareas.removeAll(tareasSel);
					eventoTareasSel.addAll(tareasSel);
					tareasSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"tareas","eventoTareas","tareasSel","eventoTareasSel"})
	public void tareasFueraPlantilla(){
		if(eventoTareasSel != null && eventoTareasSel.size() > 0){
			tareas.addAll(eventoTareasSel);
			eventoTareas.removeAll(eventoTareasSel);
			tareasSel.addAll(eventoTareasSel);
			eventoTareasSel.clear();
		}
	}
	
	// Propiedades del formulario
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum)
	{
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if(!isFormValidated())
		{
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			/*
			PayloadPlantillaEventoResponse payloadPlantillaEventoResponse = S.PlantillaEventoService.incluir(getEvento());
			if(!UtilPayload.isOK(payloadPlantillaEventoResponse))
			{
				Alert.showMessage(payloadPlantillaEventoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
			*/
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			/*
			PayloadPlantillaEventoResponse payloadPlantillaEventoResponse = S.PlantillaEventoService.modificar(getEvento());
			if(!UtilPayload.isOK(payloadRolResponse))
			{
				Alert.showMessage(payloadPlantillaEventoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
			*/
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum)
	{
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum)
	{
		return actionSalir(operacionEnum);
	}

	public Evento getEvento()
	{
		return (Evento) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
