package ve.smile.web.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Comboitem;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;

public class VM_PostuladoVoluntario extends C_WindowDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public void onAccept(Event event) {
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event,
				DialogActionEnum.ACEPTAR);
		VM_Postulado postuladoVoluntario = (VM_Postulado) vm();
		Persona persona = postuladoVoluntario.getPersona();
		persona.setFacebook("face.com");
		persona.setEstatus('P');
		PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
				.savePostuladoVoluntario(persona);
		close(dialogCloseEvent);
	}

	public void onEvent(Event event) {
		Persona p = new Persona();
		Comboitem ci = new Comboitem();
		p.setSexo(Integer.parseInt(ci.getValue().toString()));

	}

	public Persona getPersona() {
		return (Persona) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		// TODO
		try {
			UtilValidate
					.validateString(getPersona().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getPersona().getDireccion(), "Direccion");
			UtilValidate.validateNull(getPersona().getNombre(), "Nombre");
			// fecha
			// string today = getPersona().getFechaNacimiento();
			// DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
			// Date date= ft.;
			// long dateInLong = date.getTime();
			// java.util.Date utilDate = new java.util.Date();
			// java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			// String variable =(String) ComboBox.getSelectedItem();

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	@Override
	public void onCancel(Event event) {
		close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));

	}

	@Override
	public void doOnCreate() {
		// TODO Auto-generated method stub

	}

}
