package ve.smile.web.viewmodels;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.components.ComboBox;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Comboitem;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;

public class VM_PostuladoPadrino extends C_WindowDialog {

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
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event, DialogActionEnum.ACEPTAR);
		VM_Postulado postuladoPadrino = (VM_Postulado) vm();
		Persona persona = postuladoPadrino.getPersona();
		persona.setFacebook("face.com");
		persona.setEstatus('P');		
		PayloadPersonaResponse payloadPersonaResponse =  S.PersonaService.savePostuladoPadrino(persona);
		close(dialogCloseEvent);		

	}
	public void onEvent(Event event) {		
    	            Persona p= new Persona();
		            Comboitem ci=new Comboitem();
		            p.setSexo(Integer.parseInt(ci.getValue().toString()));
		           
		       }
		                  
	
	public Persona getPersona() {
		return (Persona) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		// TODO
		try {
			UtilValidate.validateString(getPersona().getNombre(), "Nombre", 100 );
			UtilValidate.validateInteger(getPersona().getEdad(),
					"Edad", ValidateOperator.GREATER_THAN, 0);
			UtilValidate.validateNull(getPersona().getEdad(), "Edad");
			UtilValidate.validateNull(getPersona().getDireccion(), "Direccion");
			UtilValidate.validateNull(getPersona().getNombre(), "Nombre");
			//fecha
			   // string today = getPersona().getFechaNacimiento();
			    //DateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
			    //Date date= ft.;
			    //long dateInLong = date.getTime();			    
			//java.util.Date utilDate = new java.util.Date();
			//java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			//String variable =(String) ComboBox.getSelectedItem(); 
			
			
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
