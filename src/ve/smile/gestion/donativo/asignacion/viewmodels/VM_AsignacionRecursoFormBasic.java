package ve.smile.gestion.donativo.asignacion.viewmodels;

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
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.dto.DonativoRecurso;

public class VM_AsignacionRecursoFormBasic extends VM_WindowForm {
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
			
			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse =
					S.DonativoRecursoService.incluir(getDonativoRecurso());

			if(!UtilPayload.isOK(payloadDonativoRecursoResponse)) {
				Alert.showMessage(payloadDonativoRecursoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse =
					S.DonativoRecursoService.modificar(getDonativoRecurso());

			if(!UtilPayload.isOK(payloadDonativoRecursoResponse)) {
				Alert.showMessage(payloadDonativoRecursoResponse);
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
	
	public DonativoRecurso getDonativoRecurso() {
		return (DonativoRecurso) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try
		{  			
			UtilValidate.validateString(getDonativoRecurso().getCantidad(), "Cantidad", 200);
			UtilValidate.validateString(getDonativoRecurso().getDescripcion(), "Descripción", 200);
			UtilValidate.validateString(getDonativoRecurso().getFkEventoPlanificado().getObservacion(), "Observación", 255);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getCorreo(), "Correo", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getDireccion(), "Dirección",250);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getFacebook(), "Facebook",100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getIdentificacion(), "Identificación", 35);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getInstagram(), "Instagram", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getLinkedin(), "Linkedin", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getSitioWeb(), "Sitio Web", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getTelefono1(), "Telefono 1", 25);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getTelefono2(), "Telefono 2", 25);
			UtilValidate.validateString(getDonativoRecurso().getFkPersona().getTwitter(), "Twitter", 100);
			UtilValidate.validateString(getDonativoRecurso().getFkTsPlan().getDescripcion(), "Descripción Trabajo Social", 300);
			UtilValidate.validateString(getDonativoRecurso().getFkTsPlan().getObservacion(), "Observación Trabajo Social", 255);
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
