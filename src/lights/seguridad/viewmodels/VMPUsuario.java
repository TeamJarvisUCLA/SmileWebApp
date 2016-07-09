package lights.seguridad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.dialog.message_box.events.listeners.MessageBoxDialogCloseListener;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import lights.core.encryptor.UtilEncryptor;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.dto.Usuario;
import lights.seguridad.payload.response.PayloadUsuarioResponse;
import lights.smile.consume.services.S;

import org.zkoss.bind.annotation.Init;

public class VMPUsuario extends VM_WindowSimpleListPrincipal<Usuario> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Usuario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		PayloadUsuarioResponse payloadUsuarioResponse = 
				S.UsuarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadUsuarioResponse;
	}

//	@Override
//	public void doDelete() {
//		PayloadUsuarioResponse payloadUsuarioResponse =
//				S.UsuarioService.eliminar(getSelectedObject().getIdUsuario());
//
//		Alert.showMessage(payloadUsuarioResponse);
//
//		if (UtilPayload.isOK(payloadUsuarioResponse)) {
//			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
//		}
//
//	}
	
	@Override
	public Map<OperacionEnum, Boolean> getScheduledsTo() {
		Map<OperacionEnum, Boolean> isScheduleds = new HashMap<OperacionEnum, Boolean>();

		isScheduleds.put(OperacionEnum.INCLUIR, true);
		isScheduleds.put(OperacionEnum.MODIFICAR, true);
//		isScheduleds.put(OperacionEnum.ELIMINAR, true);
		isScheduleds.put(OperacionEnum.CONSULTAR, true);
		isScheduleds.put(OperacionEnum.CUSTOM1, true);

		return isScheduleds;
	}

	@Override
	public void executeEliminar() {
		//NOTHING OK!
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "vista/viewUsuario.zul";
	}

	@Override
	public void executeCustom1() {
		UtilDialog.showMessageBoxConfirmation("¿Desea reestablecer la clave de este usuario?", new MessageBoxDialogCloseListener() {
			
			@Override
			public void onClose(MessageBoxDialogCloseEvent messageBoxDialogCloseEvent) {
				if (messageBoxDialogCloseEvent.getDialogAction().equals(DialogActionEnum.ACEPTAR)) {
					Usuario usuario = getSelectedObject();
					usuario.setClave(UtilEncryptor.encriptar("123456"));
					
					PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService.modificar(usuario);
					
					if (UtilPayload.isOK(payloadUsuarioResponse)) {
						payloadUsuarioResponse.setInformacion(IPayloadResponse.MENSAJE, "S:Success Code: 995-Clave reestablecida con éxito");
					}
					Alert.showMessage(payloadUsuarioResponse);
				}
			}
		});
	}

	@Override
	public String isValidPreconditionsCustom2() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un usuario";
		}
		return "";
	}
	
	@Override
	public String isValidPreconditionsCustom1() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un usuario";
		}
		return "";
	}
	
}
