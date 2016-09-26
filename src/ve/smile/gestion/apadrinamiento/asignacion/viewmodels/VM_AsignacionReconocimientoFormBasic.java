package ve.smile.gestion.apadrinamiento.asignacion.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ReconocimientoPersona;

import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

public class VM_AsignacionReconocimientoFormBasic extends VM_WindowForm {
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR)) {
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
		if (!isFormValidated()) {
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService.incluir(getRePersona());
			if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
				Alert.showMessage(payloadReconocimientoPersonaResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService.modificar(getRePersona());
			if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
				Alert.showMessage(payloadReconocimientoPersonaResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	public ReconocimientoPersona getRePersona() {
		return (ReconocimientoPersona) DataCenter.getEntity();
	}

	private boolean isFormValidated() {
		try {
			UtilValidate.validateString(getRePersona().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getRePersona().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateString(getRePersona().getFkMultimedia().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getRePersona().getFkClasificadorReconocimiento().getNombre(), "Nombre", 255);
			UtilValidate.validateString(getRePersona().getContenido(), "Contenido", 200);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}
	
	

}
