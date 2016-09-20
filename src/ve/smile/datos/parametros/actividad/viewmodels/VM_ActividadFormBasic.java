package ve.smile.datos.parametros.actividad.viewmodels;

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
import ve.smile.dto.Actividad;
import ve.smile.dto.ClasificadorActividad;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.payload.response.PayloadClasificadorActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ActividadFormBasic extends VM_WindowForm {

	private List<ClasificadorActividad> clasificadorActividads;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<ClasificadorActividad> getClasificadorActividads() {
		if (this.clasificadorActividads == null) {
			this.clasificadorActividads = new ArrayList<>();
		}
		if (this.clasificadorActividads.isEmpty()) {
			PayloadClasificadorActividadResponse payloadClasificadorActividadResponse = S.ClasificadorActividadService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorActividadResponse)) {
				Alert.showMessage(payloadClasificadorActividadResponse);
			}

			this.clasificadorActividads
					.addAll(payloadClasificadorActividadResponse.getObjetos());
		}
		return clasificadorActividads;
	}

	public void setClasificadorActividads(
			List<ClasificadorActividad> clasificadorActividads) {
		this.clasificadorActividads = clasificadorActividads;
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
			PayloadActividadResponse payloadActividadResponse = S.ActividadService
					.incluir(getActividad());

			if (!UtilPayload.isOK(payloadActividadResponse)) {
				Alert.showMessage(payloadActividadResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadActividadResponse payloadActividadResponse = S.ActividadService
					.modificar(getActividad());

			if (!UtilPayload.isOK(payloadActividadResponse)) {
				Alert.showMessage(payloadActividadResponse);
				return true;
			}

			Alert.hideMessage();
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

	public Actividad getActividad() {
		return (Actividad) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getActividad().getNombre(), "Nombre",
					100);
			UtilValidate.validateNull(getActividad().getFkClasificadorActividad(),
					"Clasificador de Actividad");
			UtilValidate.validateString(getActividad().getDescripcion(),
					"Descripción", 250);
		return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
