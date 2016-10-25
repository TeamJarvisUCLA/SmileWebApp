package ve.smile.web.viewmodels.albumes;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.util.payload.UtilPayload;
import lights.smile.util.UtillMail;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dao.ConfiguracionDAO;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.dto.Comunidad;
import ve.smile.dto.Configuracion;
import ve.smile.enums.EstatusComentarioAlbumEnum;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadComentarioAlbumResponse;
import ve.smile.payload.response.PayloadConfiguracionResponse;

public class C_ComentarioAlbum extends C_WindowDialog {
	private static final long serialVersionUID = 1L;

	ComentarioAlbum comentarioAlbum;
	Comunidad comunidad;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public void onAccept(Event event) {
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event,
				DialogActionEnum.ACEPTAR);
		VM_ComentarioAlbum vmComentarioAlbum = (VM_ComentarioAlbum) vm();
		ComentarioAlbum comentarioAlbum = vmComentarioAlbum
				.getComentarioAlbum();
		Comunidad comunidad = new Comunidad();
		comunidad.setFechaCreacion(vmComentarioAlbum.getMyFecha());
		comentarioAlbum.setFkAlbum(vmComentarioAlbum.getalbum());
		comentarioAlbum.setEstatusComentarioAlbumEnum(EstatusComentarioAlbumEnum
				.PENDIENTE);

		comentarioAlbum.setFkComunidad(comunidad);
		comunidad.setApellido(vmComentarioAlbum.getComunidad()
				.getApellido());
		comunidad.setCorreo(vmComentarioAlbum.getComunidad()
				.getCorreo());
		comunidad.setNombre(vmComentarioAlbum.getComunidad()
				.getNombre());

		PayloadComentarioAlbumResponse payloadComentarioAlbumResponse = S.ComentarioAlbumService
				.incluirComentarioCartelera(comentarioAlbum);
		if(vmComentarioAlbum.isEmailComentarAlbum()){
			CreatesendEmail(comunidad.getCorreo(),
					comunidad.getNombre(),
					comunidad.getApellido());
		}

		vmComentarioAlbum.limpiar();
		BindUtils.postGlobalCommand(null,null,"refreshComentarios",null);
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
	
	public void CreatesendEmail(String correo, String nombre, String apellido){
		String asunto = "Recibimos tu Comentario";
		String contenido = "Recibe un cordial saludo " + nombre + " " + apellido + ", Gracias por tu comentario" ;
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
