package lights.seguridad.viewmodels;

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
import lights.core.encryptor.UtilEncryptor;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.payload.response.PayloadUsuarioResponse;
import lights.seguridad.dto.Usuario;
import lights.smile.consume.services.S;

public class VMVUsuario extends VM_WindowForm {

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
			getUsuario().setClave(UtilEncryptor.encriptar("123456"));
			
			PayloadUsuarioResponse payloadUsuarioResponse =
					S.UsuarioService.incluir(getUsuario());

			if(!UtilPayload.isOK(payloadUsuarioResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadUsuarioResponse payloadUsuarioResponse =
					S.UsuarioService.modificar(getUsuario());

			if(!UtilPayload.isOK(payloadUsuarioResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
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

	public Usuario getUsuario() {
		return (Usuario) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getUsuario().getCorreo(), "Correo", 59);
			
			getUsuario().setCorreo(getUsuario().getCorreo().toUpperCase());
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

	public boolean getEstatus() {
		return getUsuario().getEstatus().equals(Usuario.ACTIVO);
	}
	
	public void setEstatus(boolean estatus) {
		getUsuario().setEstatus((estatus)?Usuario.ACTIVO:Usuario.INACTIVO);
	}

	@Override
	public void onIncluir() {
		getUsuario().setEstatus(Usuario.ACTIVO);
	}
}
