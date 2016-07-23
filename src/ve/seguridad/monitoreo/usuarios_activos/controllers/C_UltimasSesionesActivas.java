package ve.seguridad.monitoreo.usuarios_activos.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;

import ve.seguridad.monitoreo.usuarios_activos.viewmodels.VM_UltimasSesionesActivas;
import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.listfoot.ListFoot;
import karen.core.listfoot.enums.HowToSeeEnum;
import karen.core.util.payload.UtilPayload;
import lights.seguridad.payload.response.PayloadSesionResponse;

public class C_UltimasSesionesActivas extends C_WindowDialog {
	
	private static final long serialVersionUID = 6106455817093704622L;
	
	protected ListFoot listFoot;
	
	public static final Integer CANTIDAD_REGISTROS_PAGINA_DEFECTO = 10;

	@Override
	public void doOnCreate() {
		updateListBoxAndFooter();
	}
	
	public void updateListBoxAndFooter() {
		HowToSeeEnum howToSeeEnum = HowToSeeEnum.NORMAL;
		
		if (listFoot != null) {
			howToSeeEnum = listFoot.getHowToSeeCurrent();
		}
		
		updateListBoxAndFooter(1, howToSeeEnum);
	}
	
	public void updateListBoxAndFooter(Integer page, HowToSeeEnum howToSeeEnum) {
		PayloadSesionResponse iPayload = ((VM_UltimasSesionesActivas) vm()).updateListBox(page, howToSeeEnum);
		
		if (!UtilPayload.isOK(iPayload)) {
			Alert.showMessage(iPayload);
			updateFooter(null);
			return;
		}

		updateFooter(iPayload);
	}
	
	public void updateFooter(PayloadSesionResponse iPayload) {
		if (listFoot != null) {
			listFoot.updateFooter(iPayload);
		}
	}
	
	public void onClickPaginacion$listFoot(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}
		
		ForwardEvent forwardEvent = (ForwardEvent) event;

		Integer page = (Integer) forwardEvent.getOrigin().getData();
		
		updateListBoxAndFooter(page, listFoot.getHowToSeeCurrent());
	}

	@Override
	public void onAccept(Event event) {
//		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event, DialogActionEnum.ACEPTAR);
//		
//		close(dialogCloseEvent);
	}

	@Override
	public void onCancel(Event event) {
		close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));
	}
}
