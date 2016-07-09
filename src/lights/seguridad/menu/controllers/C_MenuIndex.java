package lights.seguridad.menu.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.controllers.C_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.seguridad.MenuTreeOnDemand;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.menu.viewmodels.VM_MenuIndex;
import lights.seguridad.payload.response.PayloadRolResponse;
import lights.smile.consume.services.S;

@SuppressWarnings({"rawtypes", "unchecked"})
public class C_MenuIndex extends C_WindowSimpleListPrincipal<NodoMenu> {

	private static final long serialVersionUID = 5268239355072291952L;
	
	private MenuTreeOnDemand tree;
	//private C_MenuIndex me;
	
	public C_MenuIndex() {
		super();
		
		//me = this;
		this.addEventListener("onCreate", onCreateC_MenuIndex);
	}

	protected EventListener<Event> onCreateC_MenuIndex = new EventListener() {
		public void onEvent(Event event) throws Exception {
			tree.refreshTree(S.NodoMenuService.consultarUno(0));
		}
	};
	
	public void onSelectNode$tree(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}
		
		ForwardEvent forwardEvent = (ForwardEvent) event;
		
		NodoMenu nodoMenu = (NodoMenu) forwardEvent.getOrigin().getData();
		
		PayloadRolResponse payloadRolResponse = 
				S.RolService.consultarDeUnNodoMenuEnPermiso(nodoMenu.getIdNodoMenu());
		
		if (!UtilPayload.isOK(payloadRolResponse)) {
			Alert.showMessage(payloadRolResponse);
		} else if (payloadRolResponse.getObjetos() == null ||
				payloadRolResponse.getObjetos().size() == 0) {
			Alert.showWarningMessage("020", "Ningún rol tiene permisos sobre el menú <b>" + nodoMenu.getNombre() + "</b>");
		} else {
			Alert.hideMessage();
		}
		
		nodoMenu.setRoles(payloadRolResponse.getObjetos());
		
		getVmPermisosIndex().setSelectedObject(nodoMenu);
		getVmPermisosIndex().refreshSelectedObject();
		getVmPermisosIndex().setOperacionesAndRefresh(null);
		getVmPermisosIndex().setRolSelectedAndRefresh(null);
		getVmPermisosIndex().setOperacionSelected(null);
	}
	
	public VM_MenuIndex getVmPermisosIndex() {
		return (VM_MenuIndex) vm();
	}
	
	public void refreshTree() {
		tree.refreshTree(S.NodoMenuService.consultarUno(0));
	}
}
