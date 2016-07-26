package ve.smile.web.viewmodels;



import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;







import org.zkoss.zk.ui.event.Event;









import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import lights.core.payload.response.IPayloadResponse;



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
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event, DialogActionEnum.ACEPTAR);
		VM_Postulado postuladoVoluntario = (VM_Postulado) vm();
		Persona persona = postuladoVoluntario.getPersona();
		persona.setFacebook("face.com");
		persona.setEstatus('P');
		System.out.println(persona.getNombre());
		PayloadPersonaResponse payloadPersonaResponse =  S.PersonaService.incluir(persona);
		System.out.println(payloadPersonaResponse.getInformacion("id"));
		System.out.println(payloadPersonaResponse.getInformacion());
		close(dialogCloseEvent);

	
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
