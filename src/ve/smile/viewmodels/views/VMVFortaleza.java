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
import ve.smile.dto.Fortaleza;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMVFortaleza extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

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
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadFortalezaResponse payloadFortalezaResponse =
					S.FortalezaService.incluir(getFortaleza());
			Alert.showMessage(payloadFortalezaResponse);
			if(!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadFortalezaResponse payloadFortalezaResponse =
					S.FortalezaService.modificar(getFortaleza());
			Alert.showMessage(payloadFortalezaResponse);
			if(!UtilPayload.isOK(payloadFortalezaResponse)) {
				Alert.showMessage(payloadFortalezaResponse);
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

	public Fortaleza getFortaleza() {
		return (Fortaleza) DataCenter.getEntity();
	}

	

	public boolean isFormValidated() {

		try {
			UtilValidate.validateString(getFortaleza().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getFortaleza().getDescripcion(), "Descripcion", 200);
		
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
		
	}

}
