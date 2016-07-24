package ve.seguridad.roles.viewmodels;

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
import ve.smile.seguridad.payload.response.PayloadRolResponse;
import ve.smile.seguridad.dto.Rol;

public class VM_RolesFormBasic extends VM_WindowForm {
	
	@Init(superclass = true)
	public void childInit() {
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

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadRolResponse payloadRolResponse =
							S.RolService.incluir(getRol());

			if(!UtilPayload.isOK(payloadRolResponse)) {
				Alert.showMessage(payloadRolResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadRolResponse payloadRolResponse =
							S.RolService.modificar(getRol());

			if(!UtilPayload.isOK(payloadRolResponse)) {
				Alert.showMessage(payloadRolResponse);
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

	public Rol getRol() {
		return (Rol) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getRol().getNombre(), "Nombre", 99);
			
			//toUpper
			getRol().setNombre(getRol().getNombre().toUpperCase());
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

}
