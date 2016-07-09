package lights.seguridad.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.dto.Rol;
import lights.seguridad.payload.response.PayloadRolResponse;
import lights.seguridad.payload.response.PayloadUsuarioResponse;
import lights.smile.consume.services.S;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VMPRol extends VM_WindowSimpleListPrincipal<Rol> {

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
				S.RolService.eliminarConUsuarios(getSelectedObject().getIdRol());

		Alert.showMessage(payloadRolResponse);

		if (UtilPayload.isOK(payloadRolResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public Map<OperacionEnum, Boolean> getScheduledsTo() {
		Map<OperacionEnum, Boolean> isScheduleds = new HashMap<OperacionEnum, Boolean>();

		isScheduleds.put(OperacionEnum.INCLUIR, true);
		isScheduleds.put(OperacionEnum.MODIFICAR, true);
		isScheduleds.put(OperacionEnum.ELIMINAR, true);
		isScheduleds.put(OperacionEnum.CONSULTAR, true);

		return isScheduleds;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "vista/viewRol.zul";
	}

	@Command("onSelectRol")
	public void onSelectRol() {
		Rol rol = getSelectedObject();
		
		if (rol.getUsuarios() == null || rol.getUsuarios().size() == 0) {
			
			PayloadUsuarioResponse payloadUsuarioResponse = 
					S.UsuarioService.consultarPorRoles(String.valueOf(rol.getIdRol()));
			
			if (!(Boolean)payloadUsuarioResponse.getInformacion(IPayloadResponse.IS_OK)) {
				Alert.showMessage(payloadUsuarioResponse);
				return;
			}
			
			rol.setUsuarios(payloadUsuarioResponse.getObjetos());
		}	
	}
}
