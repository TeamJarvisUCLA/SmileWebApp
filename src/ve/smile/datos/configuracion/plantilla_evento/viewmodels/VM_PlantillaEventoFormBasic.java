package ve.smile.datos.configuracion.plantilla_evento.viewmodels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.dto.Evento;
import ve.smile.dto.Indicador;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadClasificadorEventoResponse;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PlantillaEventoFormBasic extends VM_WindowForm {

	private List<Indicador> indicadores;
	private Set<Indicador> indicadoresSeleccionados;
	private List<Indicador> eventoIndicadores;
	private Set<Indicador> eventoIndicadoresSeleccionados;

	private List<Tarea> tareas;
	private Set<Tarea> tareasSeleccionadas;
	private List<Tarea> eventoTareas;
	private Set<Tarea> eventoTareasSeleccionadas;

	private List<ClasificadorEvento> clasificadorEventos;
	
	@Init(superclass = true)
	public void childInit() {
		this.getEventoIndicadores().addAll(
				this.getEvento().getEventoIndicadores());

		this.getEventoTareas().addAll(this.getEvento().getEventoTareas());

		if (this.getIndicadores().isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			} else {
				indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
		}

		if (this.getTareas().isEmpty()) {
			PayloadTareaResponse payloadTareaResponse = S.TareaService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadTareaResponse)) {
				Alert.showMessage(payloadTareaResponse);
			} else {
				tareas.addAll(payloadTareaResponse.getObjetos());
			}
		}
	}

	@Command("agregarIndicadoresPlantilla")
	@NotifyChange({ "indicadores", "eventoIndicadores",
			"indicadoresSeleccionados", "eventoIndicadoresSeleccionados" })
	public void agregarIndicadoresPlantilla() {
		if (this.getIndicadoresSeleccionados() != null
				&& this.getIndicadoresSeleccionados().size() > 0) {
			this.getEventoIndicadores().addAll(indicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getEventoIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "indicadores", "eventoIndicadores",
			"indicadoresSeleccionados", "eventoIndicadoresSeleccionados" })
	public void removerIndicadoresPlantilla() {
		if (this.getEventoIndicadoresSeleccionados() != null
				&& this.getEventoIndicadoresSeleccionados().size() > 0) {
			this.getEventoIndicadores().removeAll(
					eventoIndicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getEventoIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "tareas", "eventoTareas", "tareasSeleccionadas",
			"eventoTareasSeleccionadas" })
	public void agregarTareasPlantilla() {
		if (this.getTareasSeleccionadas() != null
				&& this.getTareasSeleccionadas().size() > 0) {
			this.getEventoTareas().addAll(tareasSeleccionadas);
			this.getEventoTareasSeleccionadas().clear();
			this.getTareasSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "tareas", "eventoTareas", "tareasSeleccionadas",
			"eventoTareasSeleccionadas" })
	public void removerTareasPlantilla() {
		if (this.getEventoTareasSeleccionadas() != null
				&& this.getEventoTareasSeleccionadas().size() > 0) {
			this.getEventoTareas().removeAll(eventoTareasSeleccionadas);
			this.getTareasSeleccionadas().clear();
			this.getEventoTareasSeleccionadas().clear();
		}
	}

	public boolean disabledIndicador(Indicador indicador) {
		return this.getEventoIndicadores().contains(indicador);
	}

	public boolean disabledTarea(Tarea tarea) {
		return this.getEventoTareas().contains(tarea);
	}

	// Propiedades del formulario
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.getEvento().getEventoIndicadores().clear();
			this.getEvento().getEventoIndicadores()
					.addAll(this.getEventoIndicadores());
			this.getEvento().getEventoTareas().clear();
			this.getEvento().getEventoTareas().addAll(this.getEventoTareas());
			PayloadEventoResponse payloadEventoResponse = S.EventoService
					.modificar(getEvento());
			if (!UtilPayload.isOK(payloadEventoResponse)) {
				Alert.showMessage(payloadEventoResponse);
				return true;
			}
			Alert.showMessage(payloadEventoResponse);
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

	public List<ClasificadorEvento> getClasificadorEventos() {
		if (this.clasificadorEventos == null) {
			this.clasificadorEventos = new ArrayList<>();
		}
		if (this.clasificadorEventos.isEmpty()) {
			PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorEventoResponse)) {
				Alert.showMessage(payloadClasificadorEventoResponse);
			}

			this.clasificadorEventos
					.addAll(payloadClasificadorEventoResponse.getObjetos());
		}
		return clasificadorEventos;
	}

	public void setClasificadorEventos(
			List<ClasificadorEvento> clasificadorEventos) {
		this.clasificadorEventos = clasificadorEventos;
	}

	public List<Indicador> getIndicadores() {
		if (this.indicadores == null) {
			this.indicadores = new ArrayList<>();
		}

		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public Set<Indicador> getIndicadoresSeleccionados() {
		if (this.indicadoresSeleccionados == null) {
			this.indicadoresSeleccionados = new HashSet<>();
		}
		return indicadoresSeleccionados;
	}

	public void setIndicadoresSeleccionados(
			Set<Indicador> indicadoresSeleccionados) {
		this.indicadoresSeleccionados = indicadoresSeleccionados;
	}

	public List<Indicador> getEventoIndicadores() {
		if (this.eventoIndicadores == null) {
			eventoIndicadores = new ArrayList<>();
		}
		return eventoIndicadores;
	}

	public void setEventoIndicadores(List<Indicador> eventoIndicadores) {
		this.eventoIndicadores = eventoIndicadores;
	}

	public Set<Indicador> getEventoIndicadoresSeleccionados() {
		if (this.eventoIndicadoresSeleccionados == null) {
			this.eventoIndicadoresSeleccionados = new HashSet<>();
		}
		return eventoIndicadoresSeleccionados;
	}

	public void setEventoIndicadoresSeleccionados(
			Set<Indicador> eventoIndicadoresSeleccionados) {
		this.eventoIndicadoresSeleccionados = eventoIndicadoresSeleccionados;
	}

	public List<Tarea> getTareas() {
		if (this.tareas == null) {
			this.tareas = new ArrayList<>();
		}
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}

	public Set<Tarea> getTareasSeleccionadas() {
		if (this.tareasSeleccionadas == null) {
			this.tareasSeleccionadas = new HashSet<>();
		}
		return tareasSeleccionadas;
	}

	public void setTareasSeleccionadas(Set<Tarea> tareasSeleccionadas) {
		this.tareasSeleccionadas = tareasSeleccionadas;
	}

	public List<Tarea> getEventoTareas() {
		if (this.eventoTareas == null) {
			this.eventoTareas = new ArrayList<>();
		}
		return eventoTareas;
	}

	public void setEventoTareas(List<Tarea> eventoTareas) {
		this.eventoTareas = eventoTareas;
	}

	public Set<Tarea> getEventoTareasSeleccionadas() {
		if (this.eventoTareasSeleccionadas == null) {
			this.eventoTareasSeleccionadas = new HashSet<>();
		}
		return eventoTareasSeleccionadas;
	}

	public void setEventoTareasSeleccionadas(
			Set<Tarea> eventoTareasSeleccionadas) {
		this.eventoTareasSeleccionadas = eventoTareasSeleccionadas;
	}

}
