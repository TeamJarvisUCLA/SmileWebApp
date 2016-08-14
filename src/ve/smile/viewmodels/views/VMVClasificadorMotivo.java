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
import ve.smile.dto.ClasificadorMotivo;
import ve.smile.payload.response.PayloadClasificadorMotivoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMVClasificadorMotivo extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
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
			PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = S.ClasificadorMotivoService
					.incluir(getClasificadorMotivo());
			Alert.showMessage(payloadClasificadorMotivoResponse);
			if (!UtilPayload.isOK(payloadClasificadorMotivoResponse)) {
				Alert.showMessage(payloadClasificadorMotivoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = S.ClasificadorMotivoService
					.modificar(getClasificadorMotivo());
			Alert.showMessage(payloadClasificadorMotivoResponse);
			if (!UtilPayload.isOK(payloadClasificadorMotivoResponse)) {
				Alert.showMessage(payloadClasificadorMotivoResponse);
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

	public ClasificadorMotivo getClasificadorMotivo() {
		return (ClasificadorMotivo) DataCenter.getEntity();
	}

	public boolean isFormValidated() {

		try {
			UtilValidate.validateString(getClasificadorMotivo().getNombre(),
					"Nombre", 200);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}

	}

}
