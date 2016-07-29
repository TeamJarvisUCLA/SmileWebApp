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
import ve.smile.payload.response.PayloadClasificadorTrabajoSocialResponse;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.dto.Notificacion;

public class VMVClasificadorTrabajoSocial extends VM_WindowForm {

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
			PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse =
					S.ClasificadorTrabajoSocialService.incluir(getClasificadorTrabajoSocial());
			Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
			if(!UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
				Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse =
					S.ClasificadorTrabajoSocialService.modificar(getClasificadorTrabajoSocial());
			Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
			if(!UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
				Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
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

	public ClasificadorTrabajoSocial getClasificadorTrabajoSocial() {
		return (ClasificadorTrabajoSocial) DataCenter.getEntity();
	}

	

	public boolean isFormValidated() {

		try {
			UtilValidate.validateString(getClasificadorTrabajoSocial().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getClasificadorTrabajoSocial().getDescripcion(), "Descripcion", 200);
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
		
	}

}
