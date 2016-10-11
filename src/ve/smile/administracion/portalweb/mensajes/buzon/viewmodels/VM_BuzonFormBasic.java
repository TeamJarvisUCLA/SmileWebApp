package ve.smile.administracion.portalweb.mensajes.buzon.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ContactoPortal;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.payload.response.PayloadContactoPortalResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

public class VM_BuzonFormBasic extends VM_WindowForm {
	private String respuesta;

	@Init(superclass = true)
	public void childInit() {

	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			OperacionForm operacionForm = new OperacionForm(
					OperacionEnum.CUSTOM1.ordinal(), "Procesar", "Custom1",
					"fa fa-cog", "indigo");
			operacionesForm.add(operacionForm);
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionCustom1(OperacionEnum operacionEnum) {
		getContactoPortal().setEstatusContacto(
				EstatusContactoEnum.PROCESADA.ordinal());
		getContactoPortal().setRespuesta(respuesta);
		PayloadContactoPortalResponse payloadContactoPortalResponse = S.ContactoPortalService
				.modificar(getContactoPortal());
		if (!UtilPayload.isOK(payloadContactoPortalResponse)) {
			Alert.showMessage(payloadContactoPortalResponse);
			return true;
		}
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	public ContactoPortal getContactoPortal() {
		return (ContactoPortal) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getContactoPortal().getFkComunidad()
					.getNombre(), "Nombre", 200);
			UtilValidate.validateString(getContactoPortal()
					.getFkClasificadorSugerencia().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getContactoPortal().getContenido(),
					"Contenido", 300);
			// UtilValidate.validateDate(getContactoPortal().getFecha(),
			// "Fecha", validateOperator, date_8601, formatToShow);
			// UtilValidate.validateInteger(getContactoPortal().getTipoContactoPortal(),
			// "Tipo Contacto", validateOperator, valueToCompare);
			UtilValidate.validateNull(getContactoPortal()
					.getEstatusContactoEnum(), "Estatus");

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

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
}
