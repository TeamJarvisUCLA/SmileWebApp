package ve.smile.gestion.donativo.egreso.viewmodels;

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
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.dto.Colaborador;

public class VM_EgresoColaboradorFormBasic extends VM_WindowForm {
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

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

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadColaboradorResponse payloadColaboradorResponse =
					S.ColaboradorService.modificar(getColaborador());

			if(!UtilPayload.isOK(payloadColaboradorResponse)) {
				Alert.showMessage(payloadColaboradorResponse);
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

	public Colaborador getColaborador() {
		return (Colaborador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try
		{
			//UtilValidate.validateDate(getColaborador().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
			//UtilValidate.validateDate(getColaborador().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
