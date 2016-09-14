package ve.smile.datos.configuracion;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Evento;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.dto.PlantillaEventoIndicador;
import ve.smile.payload.response.PayloadPlantillaEventoIndicadorResponse;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.dto.PlantillaEventoTarea;
import ve.smile.payload.response.PayloadPlantillaEventoTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMVPlantillaEvento extends VM_WindowForm {

	private List <Indicador> indicadores;
	private Set  <Indicador> indicadoresSel;
	private List <PlantillaEventoIndicador> plantillaeventoindicadores;
	private Set  <PlantillaEventoIndicador> plantillaeventoindicadoresSel;
	
	private List <Tarea> tareas;
	private Set  <Tarea> tareasSel;
	private List <PlantillaEventoTarea> plantillaeventotareas;
	private Set  <PlantillaEventoTarea> plantillaeventotareasSel;
	
	public VMVPlantillaEvento () {
		indicadores = new ArrayList <Indicador>();
		indicadoresSel = new HashSet <Indicador> ();
		plantillaeventoindicadores = new ArrayList <PlantillaEventoIndicador>();
		plantillaeventoindicadoresSel = new HashSet <PlantillaEventoIndicador>();
	}
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	// INDICADORES
		public List<Indicador> getIndicadores() {
			if (this.indicadores == null) {
				this.indicadores = new ArrayList<>();
			}
			if (this.indicadores.isEmpty()) {
				PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService.consultarTodos();

				if (!UtilPayload.isOK(payloadIndicadorResponse)) {
					Alert.showMessage(payloadIndicadorResponse);
				}

				this.indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
			return indicadores;
		}

		public void setIndicadores(List<Indicador> indicadores) {
			this.indicadores = indicadores;
		}
	
	// INDICADORES DEL EVENTO
		public List<PlantillaEventoIndicador> getPlantillaEventoIndicadores() {
			if (this.plantillaeventoindicadores == null) {
				this.plantillaeventoindicadores = new ArrayList<>();
			}
			if (this.plantillaeventoindicadores.isEmpty()) {
				PayloadPlantillaEventoIndicadorResponse payloadPlantillaEventoIndicadorResponse = S.PlantillaEventoIndicadorService.consultarPorEvento(getEvento().getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaEventoIndicadorResponse)) {
					Alert.showMessage(payloadPlantillaEventoIndicadorResponse);
				}
				Evento e = new Evento ();				
				e.setEventoIndicadors(payloadPlantillaEventoIndicadorResponse.getObjetos());
				this.plantillaeventoindicadores = e.getEventoIndicadors();
			}
			return plantillaeventoindicadores;
		}

		public void setPlantillaEventoIndicadores(List<PlantillaEventoIndicador> plantillaeventoindicadores) {
			this.plantillaeventoindicadores = plantillaeventoindicadores;
		}
	
	// TAREAS
		public List<Tarea> getTareas() {
			if (this.tareas == null) {
				this.tareas = new ArrayList<>();
			}
			if (this.tareas.isEmpty()) {
				PayloadTareaResponse payloadTareaResponse = S.TareaService.consultarTodos();

				if (!UtilPayload.isOK(payloadTareaResponse)) {
					Alert.showMessage(payloadTareaResponse);
				}
				this.tareas.addAll(payloadTareaResponse.getObjetos());
			}
			return tareas;
		}

		public void setTareas(List<Tarea> tareas) {
			this.tareas = tareas;
		}
	
	// TAREAS DE EVENTO
		public List<PlantillaEventoTarea> getPlantillaEventoTareas() {
			if (this.plantillaeventotareas == null) {
				this.plantillaeventotareas = new ArrayList<>();
			}
			if (this.plantillaeventotareas.isEmpty()) {
				PayloadPlantillaEventoTareaResponse payloadPlantillaEventoTareaResponse = S.PlantillaEventoTareaService.consultarPorEvento(getEvento().getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaEventoTareaResponse)) {
					Alert.showMessage(payloadPlantillaEventoTareaResponse);
				}
				Evento e = new Evento ();				
				e.setEventoTareas(payloadPlantillaEventoTareaResponse.getObjetos());
				this.plantillaeventotareas = e.getEventoTareas();
			}
			return plantillaeventotareas;
		}

		public void setPlantillaEventoTareas(List<PlantillaEventoTarea> plantillaeventotareas) {
			this.plantillaeventotareas = plantillaeventotareas;
		}
	
	// MÉTODOS DE LAS LISTAS
	
	public Set<Indicador> getIndicadoresSel() {
		return indicadoresSel;
	}

	public void setIndicadoresSel (Set<Indicador> indicadoresSel) {
		this.indicadoresSel = indicadoresSel;
	}

	public List <Indicador> getListaIndicadores() {
		return indicadores;
	}
	
	public Set<PlantillaEventoIndicador> getPlantillaEventoIndicadoresSel() {
		return plantillaeventoindicadoresSel;
	}

	public void setPlantillaEventoIndicadoresSel (Set<PlantillaEventoIndicador> plantillaeventoindicadoresSel) {
		this.plantillaeventoindicadoresSel = plantillaeventoindicadoresSel;
	}

	public List <PlantillaEventoIndicador> getListaPlantillaEventoIndicadores() {
		return plantillaeventoindicadores;
	}
	
	public Set <Tarea> getTareasSel() {
		return tareasSel;
	}

	public void setTareasSel (Set<Tarea> tareasSel) {
		this.tareasSel = tareasSel;
	}

	public List <Tarea> getListaTareas() {
		return tareas;
	}
	
	public Set <PlantillaEventoTarea> getPlantillaEventoTareasSel() {
		return plantillaeventotareasSel;
	}

	public void setPlantillaEventoTareasSel (Set<PlantillaEventoTarea> plantillaeventotareasSel) {
		this.plantillaeventotareasSel = plantillaeventotareasSel;
	}

	public List <PlantillaEventoTarea> getListaPlantillaEventoTareaes() {
		return plantillaeventotareas;
	}
			
	@Command
	@NotifyChange({"indicadores","plantillaeventoindicadores","indicadoresSel","plantillaeventoindicadoresSel"})
	public void indicadoresAPlantilla(){
				if(indicadoresSel != null && indicadoresSel.size() > 0){
					plantillaeventoindicadores.addAll(indicadoresSel);
					indicadores.removeAll(indicadoresSel);
					plantillaeventoindicadoresSel.addAll(indicadoresSel);
					indicadoresSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"indicadores","plantillaeventoindicadores","indicadoresSel","plantillaeventoindicadoresSel"})
	public void indicadoresFueraPlantilla(){
		if(plantillaeventoindicadoresSel != null && plantillaeventoindicadoresSel.size() > 0){
			indicadores.addAll(plantillaeventoindicadoresSel);
			plantillaeventoindicadores.removeAll(plantillaeventoindicadoresSel);
			indicadoresSel.addAll(plantillaeventoindicadoresSel);
			plantillaeventoindicadoresSel.clear();
		}
	}
	
	@Command
	@NotifyChange({"tareas","plantillaeventotareas","tareasSel","plantillaeventotareasSel"})
	public void tareasAPlantilla(){
				if(tareasSel != null && tareasSel.size() > 0){
					plantillaeventotareas.addAll(tareasSel);
					tareas.removeAll(tareasSel);
					plantillaeventotareasSel.addAll(tareasSel);
					tareasSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"tareas","plantillaeventotareas","tareasSel","plantillaeventotareasSel"})
	public void tareasFueraPlantilla(){
		if(plantillaeventotareasSel != null && plantillaeventotareasSel.size() > 0){
			tareas.addAll(plantillaeventotareasSel);
			plantillaeventotareas.removeAll(plantillaeventotareasSel);
			tareasSel.addAll(plantillaeventotareasSel);
			plantillaeventotareasSel.clear();
		}
	}
	
	// MÉTODOS DEL FORMULARIO

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadEventoResponse payloadEventoResponse = S.EventoService.incluir(getEvento());

			if (!UtilPayload.isOK(payloadEventoResponse)) {
				Alert.showMessage(payloadEventoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadEventoResponse payloadEventoResponse = S.EventoService.modificar(getEvento());

			if (!UtilPayload.isOK(payloadEventoResponse)) {
				Alert.showMessage(payloadEventoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	public Evento getEvento() {
		return (Evento) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
