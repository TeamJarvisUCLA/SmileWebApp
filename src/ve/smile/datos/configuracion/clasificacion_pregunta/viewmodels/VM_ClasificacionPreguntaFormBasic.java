package ve.smile.datos.configuracion.clasificacion_pregunta.viewmodels;

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
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.dto.Pregunta;
import ve.smile.payload.response.PayloadClasificadorPreguntaResponse;
import ve.smile.payload.response.PayloadPreguntaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificacionPreguntaFormBasic extends VM_WindowForm {

	private List<Pregunta> preguntas;
	private Set<Pregunta> preguntasSeleccionadas;
	private List<Pregunta> clasificadorPreguntas;
	private Set<Pregunta> clasificadorPreguntasSeleccionadas;

	@Init(superclass = true)
	public void childInit() {
		this.getClasificadorPreguntas().addAll(
				this.getClasificadorPregunta().getPreguntasClasificadas());

		if (this.getPreguntas().isEmpty()) {
			PayloadPreguntaResponse payloadPreguntaResponse = S.PreguntaService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadPreguntaResponse)) {
				Alert.showMessage(payloadPreguntaResponse);
			} else {
				preguntas.addAll(payloadPreguntaResponse.getObjetos());
			}
		}
	}

	@Command("agregarPreguntasClasificador")
	@NotifyChange({ "preguntas", "clasificadorPreguntas",
			"preguntasSeleccionadas", "clasificadorPreguntasSeleccionadas" })
	public void agregarPreguntasClasificador() {
		if (this.getPreguntasSeleccionadas() != null
				&& this.getPreguntasSeleccionadas().size() > 0) {
			this.getClasificadorPreguntas().addAll(preguntasSeleccionadas);
			this.getclasificadorPreguntasSeleccionadas().clear();
			this.getPreguntasSeleccionadas().clear();
		}
	}

	@Command("removerPreguntasClasificador")
	@NotifyChange({ "preguntas", "clasificadorPreguntas",
			"preguntasSeleccionadas", "clasificadorPreguntasSeleccionadas" })
	public void removerPreguntasClasificador() {
		if (this.getPreguntasSeleccionadas() != null
				&& this.getPreguntasSeleccionadas().size() > 0) {
			this.getClasificadorPreguntas().removeAll(preguntasSeleccionadas);
			this.getclasificadorPreguntasSeleccionadas().clear();
			this.getPreguntasSeleccionadas().clear();
		}
	}

	public boolean disabledPregunta(Pregunta pregunta) {
		return this.getClasificadorPreguntas().contains(pregunta);
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
			this.getClasificadorPregunta().getPreguntasClasificadas().clear();
			this.getClasificadorPregunta().getPreguntasClasificadas()
					.addAll(this.getClasificadorPreguntas());
			PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse = S.ClasificadorPreguntaService
					.modificar(getClasificadorPregunta());
			if (!UtilPayload.isOK(payloadClasificadorPreguntaResponse)) {
				Alert.showMessage(payloadClasificadorPreguntaResponse);
				return true;
			}
			Alert.showMessage(payloadClasificadorPreguntaResponse);
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

	public ClasificadorPregunta getClasificadorPregunta() {
		return (ClasificadorPregunta) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	public List<Pregunta> getPreguntas() {
		if (this.preguntas == null) {
			this.preguntas = new ArrayList<>();
		}
		return preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Set<Pregunta> getPreguntasSeleccionadas() {
		if (this.preguntasSeleccionadas == null) {
			this.preguntasSeleccionadas = new HashSet<>();
		}
		return preguntasSeleccionadas;
	}

	public void setPreguntasSeleccionadas(Set<Pregunta> preguntasSeleccionadas) {
		this.preguntasSeleccionadas = preguntasSeleccionadas;
	}

	public List<Pregunta> getClasificadorPreguntas() {
		if (this.clasificadorPreguntas == null) {
			this.clasificadorPreguntas = new ArrayList<>();
		}
		return clasificadorPreguntas;
	}

	public void setClasificadorPreguntas(List<Pregunta> clasificadorPreguntas) {
		this.clasificadorPreguntas = clasificadorPreguntas;
	}

	public Set<Pregunta> getclasificadorPreguntasSeleccionadas() {
		if (this.clasificadorPreguntasSeleccionadas == null) {
			this.clasificadorPreguntasSeleccionadas = new HashSet<>();
		}
		return clasificadorPreguntasSeleccionadas;
	}

	public void setClasificadorPreguntasSeleccionadas(
			Set<Pregunta> clasificadorPreguntasSeleccionadas) {
		this.clasificadorPreguntasSeleccionadas = clasificadorPreguntasSeleccionadas;
	}
}
