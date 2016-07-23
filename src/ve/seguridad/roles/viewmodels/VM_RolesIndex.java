package ve.seguridad.roles.viewmodels;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.payload.response.PayloadRolResponse;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VM_RolesIndex extends VM_WindowSimpleListPrincipal<Rol> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Rol> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadRolResponse payloadRolResponse = 
				S.RolService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadRolResponse;
	}

	@Override
	public void doDelete() {
		PayloadRolResponse payloadRolResponse =
				S.RolService.eliminar(getSelectedObject().getIdRol());

		Alert.showMessage(payloadRolResponse);

		if (UtilPayload.isOK(payloadRolResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/seguridad/roles/rolesFormBasic.zul";
	}

	@Command("onSelectRol")
	public void onSelectRol() {
		Rol rol = getSelectedObject();
		
		if (rol.getUsuarios() == null || rol.getUsuarios().size() == 0) {
			
			PayloadUsuarioResponse payloadUsuarioResponse = 
					S.UsuarioService.consultarPorRol(rol.getIdRol());
			
			if (!(Boolean)payloadUsuarioResponse.getInformacion(IPayloadResponse.IS_OK)) {
				Alert.showMessage(payloadUsuarioResponse);
				return;
			}
			
			rol.setUsuarios(payloadUsuarioResponse.getObjetos());
		}	
	}
}