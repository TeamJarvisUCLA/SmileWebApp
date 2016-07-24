package ve.seguridad.permisos.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;

import ve.seguridad.permisos.viewmodels.VM_PermisosIndex;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.controllers.C_WindowSimpleListPrincipal;
import karen.core.toolbar.Toolbar;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.NodoMenu;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.enums.TipoNodoMenuEnum;
import ve.smile.seguridad.payload.response.PayloadNodoMenuResponse;
import ve.smile.seguridad.payload.response.PayloadOperacionResponse;

@SuppressWarnings({"rawtypes", "unchecked"})
public class C_PermisosIndex extends C_WindowSimpleListPrincipal<Rol> {

	private static final long serialVersionUID = 5268239355072291952L;
	
	private C_MenuTreeFull tree;
	private Toolbar toolbarToSee;
	//private C_MenuIndex me;
	
	public C_PermisosIndex() {
		super();
		
		//me = this;
		this.addEventListener("onCreate", onCreateC_PermisosIndex);
	}

	protected EventListener<Event> onCreateC_PermisosIndex = new EventListener() {
		public void onEvent(Event event) throws Exception {
//			tree.refreshTree(S.NodoMenuService.consultarUno(0));
		}
	};
	
	public void onSelectNode$tree(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}
		
		ForwardEvent forwardEvent = (ForwardEvent) event;
		
		NodoMenu nodoMenu = (NodoMenu) forwardEvent.getOrigin().getData();
		
		if (!nodoMenu.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.TRANSACCION)) {
			if (toolbarToSee != null) {
				toolbarToSee.createButtons(new ArrayList<Operacion>());
			}
			
			Alert.hideMessage();
			
			return;
		}
		
		PayloadOperacionResponse payloadOperacionResponse = 
				S.OperacionService.consultarByNodoMenuAndRoles(nodoMenu.getIdNodoMenu(), String.valueOf(vm().getSelectedObject().getIdRol()));
		
		if (!UtilPayload.isOK(payloadOperacionResponse)) {
			Alert.showMessage(payloadOperacionResponse);
			
			return;
		}
		
		if (toolbarToSee != null) {
			List<Operacion> operaciones = payloadOperacionResponse.getObjetos();
			
			Collections.sort(operaciones);
			toolbarToSee.createButtons(operaciones);
		}
	}
	
	public VM_PermisosIndex getVmPermisosIndex() {
		return (VM_PermisosIndex) vm();
	}
	
	public void refreshTree(PayloadNodoMenuResponse payloadNodoMenuResponse) {
		tree.refreshTree(payloadNodoMenuResponse);
	}
}
