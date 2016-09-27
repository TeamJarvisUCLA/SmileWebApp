package ve.smile.datos.configuracion.requisito_ayuda.viewmodels;

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
import ve.smile.dto.Ayuda;
import ve.smile.dto.Requisito;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoAyudaFormBasic extends VM_WindowForm {



	private List<Requisito> requisitos;
	private Set<Requisito> requisitosSeleccionadas;
	private List<Requisito> ayudaRequisitos;
	private Set<Requisito> ayudaRequisitosSeleccionadas;

	
	@Init(superclass = true)
	public void childInit() {
		

		this.getAyudaRequisitos().addAll(this.getAyuda().getRequisitos());

		

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
	@NotifyChange({ "requisitos", "ayudaRequisitos", "requisitosSeleccionadas",
			"ayudaRequisitosSeleccionadas" })
	public void agregarRequisitosPlantilla() {
		if (this.getRequisitosSeleccionadas() != null
				&& this.getRequisitosSeleccionadas().size() > 0) {
			this.getAyudaRequisitos().addAll(requisitosSeleccionadas);
			this.getAyudaRequisitosSeleccionadas().clear();
			this.getRequisitosSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "requisitos", "ayudaRequisitos", "requisitosSeleccionadas",
			"ayudaRequisitosSeleccionadas" })
	public void removerRequisitosPlantilla() {
		if (this.getAyudaRequisitosSeleccionadas() != null
				&& this.getAyudaRequisitosSeleccionadas().size() > 0) {
			this.getAyudaRequisitos().removeAll(ayudaRequisitosSeleccionadas);
			this.getRequisitosSeleccionadas().clear();
			this.getAyudaRequisitosSeleccionadas().clear();
		}
	}

	

	public boolean disabledRequisito(Requisito requisito) {
		return this.getAyudaRequisitos().contains(requisito);
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
			this.getAyuda().getRequisitos().clear();
			this.getAyuda().getRequisitos().addAll(this.getAyudaRequisitos());
			PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
					.modificar(getAyuda());
			if (!UtilPayload.isOK(payloadAyudaResponse)) {
				Alert.showMessage(payloadAyudaResponse);
				return true;
			}
			Alert.showMessage(payloadAyudaResponse);
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

	public Ayuda getAyuda() {
		return (Ayuda) DataCenter.getEntity();
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

	public void setRequisitosSeleccionadas(Set<Requisito> requisitosSeleccionadas) {
		this.requisitosSeleccionadas = requisitosSeleccionadas;
	}

	public List<Requisito> getAyudaRequisitos() {
		if (this.ayudaRequisitos == null) {
			this.ayudaRequisitos = new ArrayList<>();
		}
		return ayudaRequisitos;
	}

	public void setAyudaRequisitos(List<Requisito> ayudaRequisitos) {
		this.ayudaRequisitos = ayudaRequisitos;
	}

	public Set<Requisito> getAyudaRequisitosSeleccionadas() {
		if (this.ayudaRequisitosSeleccionadas == null) {
			this.ayudaRequisitosSeleccionadas = new HashSet<>();
		}
		return ayudaRequisitosSeleccionadas;
	}

	public void setAyudaRequisitosSeleccionadas(
			Set<Requisito> ayudaRequisitosSeleccionadas) {
		this.ayudaRequisitosSeleccionadas = ayudaRequisitosSeleccionadas;
	}

}
