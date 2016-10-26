package ve.smile.datos.configuracion.requisito_participacion.viewmodels;

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
import ve.smile.dto.Participacion;
import ve.smile.dto.Requisito;
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoParticipacionFormBasic extends VM_WindowForm {

	private List<Requisito> requisitos;
	private Set<Requisito> requisitosSeleccionadas;
	private List<Requisito> participacionRequisitos;
	private Set<Requisito> participacionRequisitosSeleccionadas;

	@Init(superclass = true)
	public void childInit() {

		this.getParticipacionRequisitos().addAll(
				this.getParticipacion().getRequisitos());

		if (this.getRequisitos().isEmpty()) {
			PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadRequisitoResponse)) {
				Alert.showMessage(payloadRequisitoResponse);
			} else {
				requisitos.addAll(payloadRequisitoResponse.getObjetos());
			}
		}
	}

	@Command
	@NotifyChange({ "requisitos", "participacionRequisitos",
			"requisitosSeleccionadas", "participacionRequisitosSeleccionadas" })
	public void agregarRequisitosPlantilla() {
		if (this.getRequisitosSeleccionadas() != null
				&& this.getRequisitosSeleccionadas().size() > 0) {
			this.getParticipacionRequisitos().addAll(requisitosSeleccionadas);
			this.getParticipacionRequisitosSeleccionadas().clear();
			this.getRequisitosSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "requisitos", "participacionRequisitos",
			"requisitosSeleccionadas", "participacionRequisitosSeleccionadas" })
	public void removerRequisitosPlantilla() {
		if (this.getParticipacionRequisitosSeleccionadas() != null
				&& this.getParticipacionRequisitosSeleccionadas().size() > 0) {
			this.getParticipacionRequisitos().removeAll(
					participacionRequisitosSeleccionadas);
			this.getRequisitosSeleccionadas().clear();
			this.getParticipacionRequisitosSeleccionadas().clear();
		}
	}

	public boolean disabledRequisito(Requisito requisito) {
		return this.getParticipacionRequisitos().contains(requisito);
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
			this.getParticipacion().getRequisitos().clear();
			this.getParticipacion().getRequisitos()
					.addAll(this.getParticipacionRequisitos());
			PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
					.modificar(getParticipacion());
			if (!UtilPayload.isOK(payloadParticipacionResponse)) {
				Alert.showMessage(payloadParticipacionResponse);
				return true;
			}
			Alert.showMessage(payloadParticipacionResponse);
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

	public Participacion getParticipacion() {
		return (Participacion) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	public List<Requisito> getRequisitos() {
		if (this.requisitos == null) {
			this.requisitos = new ArrayList<>();
		}
		return requisitos;
	}

	public void setRequisitos(List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}

	public Set<Requisito> getRequisitosSeleccionadas() {
		if (this.requisitosSeleccionadas == null) {
			this.requisitosSeleccionadas = new HashSet<>();
		}
		return requisitosSeleccionadas;
	}

	public void setRequisitosSeleccionadas(
			Set<Requisito> requisitosSeleccionadas) {
		this.requisitosSeleccionadas = requisitosSeleccionadas;
	}

	public List<Requisito> getParticipacionRequisitos() {
		if (this.participacionRequisitos == null) {
			this.participacionRequisitos = new ArrayList<>();
		}
		return participacionRequisitos;
	}

	public void setParticipacionRequisitos(
			List<Requisito> participacionRequisitos) {
		this.participacionRequisitos = participacionRequisitos;
	}

	public Set<Requisito> getParticipacionRequisitosSeleccionadas() {
		if (this.participacionRequisitosSeleccionadas == null) {
			this.participacionRequisitosSeleccionadas = new HashSet<>();
		}
		return participacionRequisitosSeleccionadas;
	}

	public void setParticipacionRequisitosSeleccionadas(
			Set<Requisito> participacionRequisitosSeleccionadas) {
		this.participacionRequisitosSeleccionadas = participacionRequisitosSeleccionadas;
	}
}