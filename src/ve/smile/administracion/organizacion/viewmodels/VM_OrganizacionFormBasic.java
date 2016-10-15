package ve.smile.administracion.organizacion.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

public class VM_OrganizacionFormBasic extends VM_WindowForm {
	
	@Init(superclass=true)
	public void childInit(){
		
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
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}
		if(operacionEnum.equals(OperacionEnum.INCLUIR)){
			PayloadOrganizacionResponse payloadOrganizacionResponse =S.OrganizacionService.incluir(getOrganizacion());
			if(!UtilPayload.isOK(payloadOrganizacionResponse)){
				Alert.showMessage(payloadOrganizacionResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
	
		if(operacionEnum.equals(OperacionEnum.MODIFICAR)){
			PayloadOrganizacionResponse payloadOrganizacionResponse =S.OrganizacionService.modificar(getOrganizacion());
			if(!UtilPayload.isOK(payloadOrganizacionResponse)){
				Alert.showMessage(payloadOrganizacionResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	public Organizacion getOrganizacion() {
		return (Organizacion) DataCenter.getEntity();
	}

	private boolean isFormValidated() {
		try {
			UtilValidate.validateString(getOrganizacion().getRif(), "Rif", 30);
			UtilValidate.validateString(getOrganizacion().getNombre(), "Nombre", 500);
			UtilValidate.validateString(getOrganizacion().getDireccion(), "Dirección", 500);
			UtilValidate.validateString(getOrganizacion().getTelefono(), "Teléfono", 25);
			UtilValidate.validateString(getOrganizacion().getMision(), "Misión", 500);
			UtilValidate.validateString(getOrganizacion().getVision(), "Visión", 500);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
		
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	
}
