package ve.smile.datos.configuracion.requisito_ayuda.viewmodels;

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
import ve.smile.dto.Ayuda;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RequisitoAyudaFormBasic extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
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

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
					.incluir(getAyuda());

			if (!UtilPayload.isOK(payloadAyudaResponse)) {
				Alert.showMessage(payloadAyudaResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadAyudaResponse payloadAyudaResponse = S.AyudaService
					.modificar(getAyuda());

			if (!UtilPayload.isOK(payloadAyudaResponse)) {
				Alert.showMessage(payloadAyudaResponse);
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

	public Ayuda getAyuda() {
		return (Ayuda) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getAyuda().getNombre(), "Nombre", 99);

			// toUpper
			getAyuda().setNombre(getAyuda().getNombre().toUpperCase());

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
