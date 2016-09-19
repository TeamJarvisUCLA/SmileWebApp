package ve.smile.datos.parametros.tarea.viewmodels;

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
import ve.smile.dto.ClasificadorTarea;
import ve.smile.dto.Tarea;
import ve.smile.payload.response.PayloadClasificadorTareaResponse;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TareaFormBasic extends VM_WindowForm {

	private List<ClasificadorTarea> clasificadorTareas;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<ClasificadorTarea> getClasificadorTareas() {
		if (this.clasificadorTareas == null) {
			this.clasificadorTareas = new ArrayList<>();
		}
		if (this.clasificadorTareas.isEmpty()) {
			PayloadClasificadorTareaResponse payloadClasificadorTareaResponse = S.ClasificadorTareaService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorTareaResponse)) {
				Alert.showMessage(payloadClasificadorTareaResponse);
			}

			this.clasificadorTareas.addAll(payloadClasificadorTareaResponse
					.getObjetos());
		}
		return clasificadorTareas;
	}

	public void setClasificadorTareas(List<ClasificadorTarea> clasificadorTareas) {
		this.clasificadorTareas = clasificadorTareas;
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
			PayloadTareaResponse payloadTareaResponse = S.TareaService
					.incluir(getTarea());

			if (!UtilPayload.isOK(payloadTareaResponse)) {
				Alert.showMessage(payloadTareaResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadTareaResponse payloadTareaResponse = S.TareaService
					.modificar(getTarea());

			if (!UtilPayload.isOK(payloadTareaResponse)) {
				Alert.showMessage(payloadTareaResponse);
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

	public Tarea getTarea() {
		return (Tarea) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getTarea().getNombre(), "Nombre", 100);
			UtilValidate.validateString(getTarea().getDescripcion(),
					"Descripción", 250);
			UtilValidate.validateNull(getTarea().getFkClasificadorTarea(),
					"Clasificador de Tarea");

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
