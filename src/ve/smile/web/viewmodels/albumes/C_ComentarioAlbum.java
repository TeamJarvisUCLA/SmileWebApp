package ve.smile.web.viewmodels.albumes;

import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.dto.Comunidad;
import ve.smile.payload.response.PayloadComentarioAlbumResponse;

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

		comentarioAlbum.setFkComunidad(comunidad);
		comunidad.setApellido(vmComentarioAlbum.getComunidad()
				.getApellido());
		comunidad.setCorreo(vmComentarioAlbum.getComunidad()
				.getCorreo());
		comunidad.setNombre(vmComentarioAlbum.getComunidad()
				.getNombre());

		PayloadComentarioAlbumResponse payloadComentarioAlbumResponse = S.ComentarioAlbumService
				.incluirComentarioCartelera(comentarioAlbum);

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

}
