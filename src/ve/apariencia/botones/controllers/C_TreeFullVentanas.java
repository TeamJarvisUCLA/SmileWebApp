package ve.apariencia.botones.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.dto.Vista;
import ve.smile.seguridad.payload.response.PayloadVistaResponse;

import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

@SuppressWarnings({"rawtypes", "unchecked"})
public class C_TreeFullVentanas extends Tree {

	private static final long serialVersionUID = -4680029582876559375L;
	
	public C_TreeFullVentanas() {
		super();
		
		this.setSclass("tree-full-ventanas");
	}
	
	protected void sendEvent(Object nodo) {
		Events.postEvent("onSelectNode", this, nodo);
	}
	
	protected EventListener selectListener = new EventListener() {

		public void onEvent(Event event) throws UiException {
			try {
				Treecell cell = (Treecell) event.getTarget();

				Object value = ((Treeitem) cell.getParent().getParent())
						.getValue();
				
				if (value instanceof Vista) {
					Vista vista = (Vista) value;
					sendEvent(vista);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	protected Treeitem createNodo(Vista vista) {
		Treeitem treeItem = new Treeitem();

		if (vista != null) {

			treeItem.setValue(vista);
			treeItem.setTooltiptext(vista.getNombre());

			Treerow treerow = new Treerow();
			treerow.setParent(treeItem);

			Treecell treecell = new Treecell(vista.getNombre());
			treecell.addEventListener(Events.ON_CLICK, selectListener);
			treecell.setParent(treerow);
			
			Treecell treeCellDescripcion = new Treecell(vista.getDescripcion());
			treeCellDescripcion.setParent(treerow);
			
			C_ToolbarCustomButtons toolbarCustomButtons = new C_ToolbarCustomButtons();
			toolbarCustomButtons.createButtons(vista.getOperaciones(), this, vista);
			
			Treecell cellToolbar = new Treecell();
			cellToolbar.appendChild(toolbarCustomButtons);
			cellToolbar.setParent(treerow);
		}

		return treeItem;
	}

	public void createTree(Treechildren treeChildren, List<Vista> vistas) {
		for (Vista vista : vistas) {
			Treeitem createBranch = createNodo(vista);
			createBranch.setParent(treeChildren);
		}
	}

	public void refreshTree(PayloadVistaResponse payloadVistaResponse) {
		Treechildren children = this.getTreechildren();
		
		if (children == null) {
			children = new Treechildren();
			children.setParent(this);
		} else {
			children.getChildren().clear();
		}
		
		if (payloadVistaResponse.getObjetos() == null 
				|| !UtilPayload.isOK(payloadVistaResponse)
				|| payloadVistaResponse.getObjetos().size() == 0) {
			Alert.showMessage(payloadVistaResponse);
			
			return;
		} else {
			Alert.hideMessage();
		}
		
		createTree(children, payloadVistaResponse.getObjetos());
	}

	public void onSelectButtonToolbar(Operacion operacion, C_ToolbarCustomButtons toolbarCustomButtons) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("toolbar", toolbarCustomButtons);
		data.put("operacion", operacion);
		data.put("vista", toolbarCustomButtons.getVista());
		
		Events.postEvent("onSelectButtonToolbar", this, data);
	}
}
