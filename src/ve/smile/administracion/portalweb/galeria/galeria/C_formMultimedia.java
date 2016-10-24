package ve.smile.administracion.portalweb.galeria.galeria;

import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dto.Multimedia;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class C_formMultimedia extends C_WindowDialog {
	private static final long serialVersionUID = 1L;

	Multimedia multimedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public void onAccept(Event event) {
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event,
				DialogActionEnum.ACEPTAR);
		VM_formMultimediaAlbum vmMutimedia = (VM_formMultimediaAlbum) vm();
		
		Multimedia multimedia = vmMutimedia.getMultimedia();
		
		multimedia.setTipoMultimedia(TipoMultimediaEnum.GALERIA.ordinal());
		multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
				vmMutimedia.getExtensionImage()).ordinal());

		PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
				.incluir(multimedia);
		
		multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
				.getInformacion("id")).intValue());

		
		Zki.save(Zki.GALERIA, multimedia.getIdMultimedia(), vmMutimedia.getExtensionImage(), vmMutimedia.getBytes());

		multimedia.setUrl(Zki.GALERIA
				+ multimedia.getIdMultimedia() + "."
				+ vmMutimedia.getExtensionImage());
		payloadMultimediaResponse = S.MultimediaService
				.modificar(multimedia);
		
		vmMutimedia.limpiar();
	
		close(dialogCloseEvent);

	}
	
	public void onClick$btnDelete(Event event) {
		
		VM_formMultimediaAlbum vmMutimedia = (VM_formMultimediaAlbum) vm();
		this.multimedia = vmMutimedia.getMultimedia();
		vmMutimedia.limpiar();
		onCancel(event);

		PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
				.eliminar(this.multimedia.getIdMultimedia());

		Alert.showMessage(payloadMultimediaResponse);
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
