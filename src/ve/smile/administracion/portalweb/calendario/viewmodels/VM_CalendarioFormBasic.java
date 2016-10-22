package ve.smile.administracion.portalweb.calendario.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CalendarioFormBasic extends VM_WindowForm {
	private boolean publicar;
	@Init(superclass = true)
	public void childInit() {
		
	}
	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if(operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			OperacionForm operacionForm = new OperacionForm(OperacionEnum.CUSTOM1.ordinal(), "Procesar", "Custom1", "fa fa-cog", "indigo");
			operacionesForm.add(operacionForm);
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionCustom1(OperacionEnum operacionEnum) {	
	
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService.modificar(getEventoPlanificado());
		if (!UtilPayload.isOK(payloadEventoPlanificadoResponse)){
			Alert.showMessage(payloadEventoPlanificadoResponse);
			return true;
		}
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	public EventoPlanificado getEventoPlanificado() {
		return (EventoPlanificado) DataCenter.getEntity();
	}

	private boolean isFormValidated() {
		// TODO Auto-generated method stub
		return false;
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

	public boolean isPublicar() {
		return publicar;
	}

	public void setPublicar(boolean publicar) {
		this.publicar = publicar;
	}
	
}
