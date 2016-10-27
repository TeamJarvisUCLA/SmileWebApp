package ve.smile.web.viewmodels.recuperacion;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import lights.smile.util.UtillMail;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

public class C_Recuperacion extends C_WindowDialog {
	private static final long serialVersionUID = 1L;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public void onAccept(Event event) {
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event,
				DialogActionEnum.ACEPTAR);
		VM_recuperacion vmRecuperacion = (VM_recuperacion) vm();
		Usuario usuario = vmRecuperacion.getUsuario();
		
		PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
				.recuperar(usuario);

		if (UtilPayload.isOK(payloadUsuarioResponse)){
			CreatesendEmail(usuario.getCorreo());
			close(dialogCloseEvent);
			UtilDialog
			.showMessageBoxSuccess("Hemos enviado a tu correo la nueva contraseña.");
		}else{
			UtilDialog.showMessage(payloadUsuarioResponse);
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
	
	public void CreatesendEmail(String correo){
		String asunto = "Recuperacion de Contraseña";
		String contenido = "Recibe un cordial saludo tu nueva contraseña es 123456";
		try {
			new UtillMail().generateAndSendEmail(correo,asunto,contenido);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
