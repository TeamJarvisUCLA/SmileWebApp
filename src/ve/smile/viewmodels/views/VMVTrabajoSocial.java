package ve.smile.viewmodels.views;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.dto.Notificacion;
import ve.smile.dto.TrabajoSocial;

public class VMVTrabajoSocial extends VM_WindowForm {

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
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse =
					S.TrabajoSocialService.incluir(getTrabajoSocial());
			Alert.showMessage(payloadTrabajoSocialResponse);
			if(!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse =
					S.TrabajoSocialService.modificar(getTrabajoSocial());
			Alert.showMessage(payloadTrabajoSocialResponse);
			if(!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
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

	public TrabajoSocial getTrabajoSocial() {
		return (TrabajoSocial) DataCenter.getEntity();
	}


	public boolean isFormValidated() {

		try {
			
			UtilValidate.validateString(getTrabajoSocial().getDescripcion(), "Descripcion", 200);
			UtilValidate.validateString(getTrabajoSocial().getNombre(), "Nombre", 200);
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
		
	}

}
