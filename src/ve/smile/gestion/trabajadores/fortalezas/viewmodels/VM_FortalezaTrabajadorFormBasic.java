package ve.smile.gestion.trabajadores.fortalezas.viewmodels;

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
import ve.smile.dto.Trabajador;
import ve.smile.dto.Fortaleza;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FortalezaTrabajadorFormBasic extends VM_WindowForm {

	private List<Fortaleza> fortalezas;
	private Set<Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> trabajadorFortalezas;
	private Set<Fortaleza> trabajadorFortalezasSeleccionadas;
	
	@Init(superclass = true)
	public void childInit() {
		
			//this.getTrabajadorFortalezas().addAll(this.getTrabajador().getFortalezas());

		if (this.getFortalezas().isEmpty()) {
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
			} else {
			fortalezas.addAll(payloadFortalezaResponse.getObjetos());
			}
		}
	}

	@Command("agregarFortalezasTrabajador")
	@NotifyChange({ "fortalezas", "trabajadorFortalezas", "fortalezaSeleccionadas",
			"trabajadorFortalezasSeleccionadas" })
	public void agregarFortalezasTrabajador() {
		if (this.getFortalezasSeleccionadas() != null
				&& this.getFortalezasSeleccionadas().size() > 0) {
			this.getTrabajadorFortalezas().addAll(fortalezasSeleccionadas);
			this.getTrabajadorFortalezasSeleccionadas().clear();
			this.getFortalezasSeleccionadas().clear();
		}
	}

	@Command("removerFortalezasTrabajador")
	@NotifyChange({ "fortalezas", "trabajadorFortalezas", "fortalezasSeleccionadas",
			"trabajadorFortalezasSeleccionadas" })
	public void removerFortalezasTrabajador() {
		if (this.getTrabajadorFortalezasSeleccionadas() != null
				&& this.getTrabajadorFortalezasSeleccionadas().size() > 0) {
			this.getTrabajadorFortalezas().removeAll(trabajadorFortalezasSeleccionadas);
			this.getTrabajadorFortalezasSeleccionadas().clear();
			this.getFortalezasSeleccionadas().clear();
		}
	}

	public boolean disabledFortaleza(Fortaleza fortaleza) {
		return this.getTrabajadorFortalezas().contains(fortaleza);
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
			this.getTrabajador().getFortalezas().addAll(this.getTrabajadorFortalezas());
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.modificar(getTrabajador());
			if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
				Alert.showMessage(payloadTrabajadorResponse);
				return true;
			}
			Alert.showMessage(payloadTrabajadorResponse);
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

	public Trabajador getTrabajador() {
		return (Trabajador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	public List<Fortaleza> getFortalezas() {
		if (this.fortalezas == null) {
			this.fortalezas = new ArrayList<>();
		}
		return fortalezas;
	}

	public void setFortalezas(List<Fortaleza> fortalezas) {
		this.fortalezas = fortalezas;
	}

	public Set<Fortaleza> getFortalezasSeleccionadas() {
		if (this.fortalezasSeleccionadas == null) {
			this.fortalezasSeleccionadas = new HashSet<>();
		}
		return fortalezasSeleccionadas;
	}

	public void setFortalezasSeleccionadas (Set<Fortaleza> fortalezasSeleccionadas) {
		this.fortalezasSeleccionadas = fortalezasSeleccionadas;
	}

	public List<Fortaleza> getTrabajadorFortalezas() {
		if (this.trabajadorFortalezas == null) {
			this.trabajadorFortalezas = new ArrayList<>();
		}
		return trabajadorFortalezas;
	}

	public void setTrabajadorFortalezas(List<Fortaleza> trabajadorFortalezas) {
		this.trabajadorFortalezas = trabajadorFortalezas;
	}

	public Set<Fortaleza> getTrabajadorFortalezasSeleccionadas() {
		if (this.trabajadorFortalezasSeleccionadas == null) {
			this.trabajadorFortalezasSeleccionadas = new HashSet<>();
		}
		return trabajadorFortalezasSeleccionadas;
	}

	public void setTrabajadorFortalezasSeleccionadas(
				Set<Fortaleza> trabajadorFortalezasSeleccionadas) {
			this.trabajadorFortalezasSeleccionadas = trabajadorFortalezasSeleccionadas;
		}
}
