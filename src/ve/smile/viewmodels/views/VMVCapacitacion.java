package ve.smile.viewmodels.views;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.ClasificadorCapacitacion;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.payload.response.PayloadClasificadorCapacitacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMVCapacitacion extends VM_WindowForm {

	private List<ClasificadorCapacitacion> clasificadorCapacitacions;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<ClasificadorCapacitacion> getClasificadorCapacitacions() {
		if (this.clasificadorCapacitacions == null) {
			this.clasificadorCapacitacions = new ArrayList<>();
		}
		if (this.clasificadorCapacitacions.isEmpty()) {
			PayloadClasificadorCapacitacionResponse payloadClasificadorCapacitacionResponse = S.ClasificadorCapacitacionService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadClasificadorCapacitacionResponse)) {
				Alert.showMessage(payloadClasificadorCapacitacionResponse);
			} else {
				this.clasificadorCapacitacions
						.addAll(payloadClasificadorCapacitacionResponse
								.getObjetos());
			}

		}
		return clasificadorCapacitacions;
	}

	public void setClasificadorCapacitacions(
			List<ClasificadorCapacitacion> clasificadorCapacitacions) {
		this.clasificadorCapacitacions = clasificadorCapacitacions;
	}

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

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

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
			PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService
					.incluir(getCapacitacion());

			if (!UtilPayload.isOK(payloadCapacitacionResponse)) {
				Alert.showMessage(payloadCapacitacionResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService
					.modificar(getCapacitacion());

			if (!UtilPayload.isOK(payloadCapacitacionResponse)) {
				Alert.showMessage(payloadCapacitacionResponse);
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

	public Capacitacion getCapacitacion() {
		return (Capacitacion) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getCapacitacion().getNombre(),
					"Nombre", 100);
			UtilValidate.validateString(getCapacitacion().getDescripcion(),
					"Descripción", 250);

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
