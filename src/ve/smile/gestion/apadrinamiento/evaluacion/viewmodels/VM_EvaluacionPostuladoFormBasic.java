package ve.smile.gestion.apadrinamiento.evaluacion.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import com.sun.org.apache.regexp.internal.recompile;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;

public class VM_EvaluacionPostuladoFormBasic extends VM_WindowForm {
	
	@Init(superclass = true)
	public void childInit() {
		
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if(operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR)) {
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
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.incluir(getPadrino());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				Alert.showMessage(payloadPadrinoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.modificar(getPadrino());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				Alert.showMessage(payloadPadrinoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	public Padrino getPadrino() {
		// TODO Auto-generated method stub
		return (Padrino) DataCenter.getEntity();
	}

	private boolean isFormValidated() {
		try {
			UtilValidate.validateString(getPadrino().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getPadrino().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateString(getPadrino().getFkFrecuenciaAporte().getNombre(), "Nombre", 150);
		//	UtilValidate.validateDate(getPadrino().getFechaIngreso(), "Fecha de Ingreso", validateOperator, date_8601, formatToShow);
		//	UtilValidate.validateString(getPadrino().getContenido(), "Contenido", 200);
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
