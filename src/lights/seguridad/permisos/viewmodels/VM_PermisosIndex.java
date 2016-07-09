package lights.seguridad.permisos.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.enums.helper.OperacionHelper;
import lights.seguridad.consume.services.NodoMenuService;
import lights.seguridad.dto.Operacion;
import lights.seguridad.dto.Rol;
import lights.seguridad.payload.response.PayloadNodoMenuResponse;
import lights.seguridad.payload.response.PayloadRolResponse;
import lights.seguridad.permisos.controllers.C_PermisosIndex;
import lights.smile.consume.services.S;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VM_PermisosIndex extends VM_WindowSimpleListPrincipal<Rol> {

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
	public void doDelete() {}

	@Override
	public Map<OperacionEnum, Boolean> getScheduledsTo() {
		Map<OperacionEnum, Boolean> isScheduleds = new HashMap<OperacionEnum, Boolean>();

		isScheduleds.put(OperacionEnum.INCLUIR, true);
		isScheduleds.put(OperacionEnum.MODIFICAR, true);
		isScheduleds.put(OperacionEnum.ELIMINAR, true);
		isScheduleds.put(OperacionEnum.CONSULTAR, true);
		isScheduleds.put(OperacionEnum.CUSTOM1, true);

		return isScheduleds;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "vista/seguridad/permisos/form_basic.zul";
		}
		
		return "";
	}

	@Command("onSelectRol")
	public void onSelectRol() {
		Rol rol = getSelectedObject();

		PayloadNodoMenuResponse payloadNodoMenu =
				new NodoMenuService().consultarNodoMenuPorRoles(String.valueOf(rol.getIdRol()));
		
		((C_PermisosIndex) getControllerWindowSimpleListPrincipal()).refreshTree(payloadNodoMenu);
	}

	@Override
	public void executeCustom1() {
		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);

		DataCenter.updateSrcPageContent(selectedObject, operacion, getSrcFileZulForm(OperacionEnum.CUSTOM1));
	}

	@Override
	public String isValidPreconditionsCustom1() {
		if (selectedObject == null) {
			return "I:Information Code: 102-Debe seleccionar un rol";
		}
		return "";
	}
	
}
