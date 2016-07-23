package ve.seguridad.permisos.controllers;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;

import ve.seguridad.permisos.viewmodels.VM_PermisosFormBasic;
import karen.core.crux.alert.Alert;
import karen.core.form.controllers.C_WindowForm;
import karen.core.util.payload.UtilPayload;
import lights.smile.consume.services.S;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.dto.Operacion;
import lights.seguridad.dto.PermisoSeguridad;
import lights.seguridad.payload.response.PayloadPermisoSeguridadResponse;

@SuppressWarnings({"rawtypes", "unchecked"})
public class C_PermisosFormBasic extends C_WindowForm {

	private static final long serialVersionUID = 5268239355072291952L;
	
	private C_MenuTreeFullPermisos tree;
	
	public C_PermisosFormBasic() {
		super();
		
		this.addEventListener("onCreate", onCreateC_PermisosFormBasic);
	}

	protected EventListener<Event> onCreateC_PermisosFormBasic = new EventListener() {
		public void onEvent(Event event) throws Exception {
			tree.refreshTree(S.NodoMenuService.consultarArbol(getVmPermisosFormBasic().getRol().getIdRol()));
		}
	};
	
	public void onSelectButtonToolbar$tree(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}

		ForwardEvent forwardEvent = (ForwardEvent) event;

		Map<String, Object> data = (Map<String, Object>) forwardEvent.getOrigin().getData();
		
		C_ToolbarPermisos toolbarPermisos = (C_ToolbarPermisos) data.get("toolbar");
		NodoMenu nodoMenuSelected = (NodoMenu) data.get("nodoMenu");
		Operacion operacion = (Operacion) data.get("operacion");
		
		if (toolbarPermisos.hasPermission(operacion)) {
			PayloadPermisoSeguridadResponse payloadPermisoSeguridadResponse =
					S.PermisoSeguridadService.eliminarByRolAndNodoMenuAndOperacion(getVmPermisosFormBasic().getRol().getIdRol(), 
							nodoMenuSelected.getIdNodoMenu(), operacion.getIdOperacion());
			
			if (!UtilPayload.isOK(payloadPermisoSeguridadResponse)) {
				Alert.showMessage(payloadPermisoSeguridadResponse);
				
				return;
			} else {
				Alert.showSuccessMessage("555", "Se ha removido el permiso de la operación <b>" + operacion.getNombre() + "</b> en el menú <b>" 
						+ nodoMenuSelected.getNombre() + "</b> al rol <b>" + getVmPermisosFormBasic().getRol().getNombre() + "</b>");
			}
			
			toolbarPermisos.removePermission(operacion);
		} else {		
			PermisoSeguridad permisoSeguridad =
					new PermisoSeguridad(nodoMenuSelected, getVmPermisosFormBasic().getRol(), operacion.getIdOperacion());
			
			PayloadPermisoSeguridadResponse payloadPermisoSeguridadResponse =
					S.PermisoSeguridadService.incluir(permisoSeguridad);
			
			if (!UtilPayload.isOK(payloadPermisoSeguridadResponse)) {
				Alert.showMessage(payloadPermisoSeguridadResponse);
				
				return;
			} else {
				Alert.showSuccessMessage("555", "Se ha agregado el permiso de la operación <b>" + operacion.getNombre() + "</b> en el menú <b>" 
						+ nodoMenuSelected.getNombre() + "</b> al rol " + getVmPermisosFormBasic().getRol().getNombre());
			}
			
			toolbarPermisos.addPermission(operacion);
		}
	}
	
	public VM_PermisosFormBasic getVmPermisosFormBasic() {
		return (VM_PermisosFormBasic) vm();
	}
}
