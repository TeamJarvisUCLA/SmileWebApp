package ve.seguridad.usuarios.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.dialog.message_box.events.listeners.MessageBoxDialogCloseListener;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import lights.core.encryptor.UtilEncryptor;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.dto.Usuario;
import lights.seguridad.payload.response.PayloadUsuarioResponse;

import org.zkoss.bind.annotation.Init;

public class VM_UsuariosIndex extends VM_WindowSimpleListPrincipal<Usuario> {

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
	


	@Override
	public void executeEliminar() {
		//NOTHING OK!
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/seguridad/usuarios/usuariosFormBasic.zul";
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
