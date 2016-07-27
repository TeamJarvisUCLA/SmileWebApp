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
import ve.smile.payload.response.PayloadClasificadorActividadResponse;
import ve.smile.dto.ClasificadorActividad;
import ve.smile.dto.Notificacion;

public class VMVClasificadorActividad extends VM_WindowForm {

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
			PayloadClasificadorActividadResponse payloadClasificadorActividadResponse =
					S.ClasificadorActividadService.incluir(getClasificadorActividad());

			if(!UtilPayload.isOK(payloadClasificadorActividadResponse)) {
				Alert.showMessage(payloadClasificadorActividadResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadClasificadorActividadResponse payloadClasificadorActividadResponse =
					S.ClasificadorActividadService.modificar(getClasificadorActividad());

			if(!UtilPayload.isOK(payloadClasificadorActividadResponse)) {
				Alert.showMessage(payloadClasificadorActividadResponse);
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

	public ClasificadorActividad getClasificadorActividad() {
		return (ClasificadorActividad) DataCenter.getEntity();
	}

	public Notificacion getNotificacion() {
		return (Notificacion) DataCenter.getEntity();
	}

	public boolean isFormValidated() {

		try {
			
			UtilValidate.validateString(getNotificacion().getDescripcion(), "Descripcion", 200);
			UtilValidate.validateString(getNotificacion().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getNotificacion().getIcono(), "Icono", 200);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
		
	}

}
