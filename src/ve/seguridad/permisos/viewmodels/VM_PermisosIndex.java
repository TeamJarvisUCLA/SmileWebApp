package ve.seguridad.permisos.viewmodels;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import ve.smile.seguridad.consume.services.NodoMenuService;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.payload.response.PayloadNodoMenuResponse;
import ve.smile.seguridad.payload.response.PayloadRolResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.seguridad.permisos.controllers.C_PermisosIndex;

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
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "views/seguridad/permisos/permisosFormBasic.zul";
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
