package app;

import org.zkoss.zk.ui.event.Event;

import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;

public class C_ChangeImage extends C_WindowDialog {
	
	private static final long serialVersionUID = 6106455817093704622L;

	@Override
	public void doOnCreate() {
		
	}

	@Override
	public void onAccept(Event event) {
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event, DialogActionEnum.ACEPTAR);
		
		VMChangeImage vmChangeImage = (VMChangeImage) vm();
		
		dialogCloseEvent.put("bytes", vmChangeImage.getBytes());
		
		close(dialogCloseEvent);
	}

	@Override
	public void onCancel(Event event) {
		close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));
	}
}
