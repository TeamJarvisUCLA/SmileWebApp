package lights.seguridad;

import java.util.Collection;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.enums.TipoNodoMenuEnum;
import lights.seguridad.payload.response.PayloadNodoMenuResponse;

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
public class MenuTreeFull extends Tree {

	private static final long serialVersionUID = -4680029582876559375L;
	
	protected void sendEvent(Object nodo) {
		Events.postEvent("onSelectNode", this, nodo);
	}

	protected void createChildren(Treeitem item, Collection<NodoMenu> nodos) {
		if (nodos != null && nodos.size() > 0) {
			Treechildren itemsHijos = item.getTreechildren();

			if (itemsHijos == null) {
				itemsHijos = new Treechildren();
			}
			itemsHijos.getChildren().clear();
			
			for (NodoMenu nodoHijo: nodos) {
				Treeitem itemHijo = null;
				
				if (nodoHijo.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.CARPETA)
						|| nodoHijo.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.RAIZ)) {
					itemHijo = createNodoCarpeta(nodoHijo);
				} else {
					itemHijo = createNodoTransaccion(nodoHijo);
				}
				
				itemHijo.setParent(itemsHijos);
			}

			itemsHijos.setParent(item);
		}

	}
	
	protected EventListener selectListener = new EventListener() {
		public void onEvent(Event event) throws UiException {
			try {
				Treecell cell = (Treecell) event.getTarget();
				Object value = ((Treeitem) cell.getParent().getParent())
						.getValue();
				
				if (value instanceof NodoMenu) {
					NodoMenu nodoMenu = (NodoMenu) value;
					sendEvent(nodoMenu);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	protected Treeitem createNodoCarpeta(NodoMenu carpeta) {
		Treeitem treeItem = new Treeitem();

		if (carpeta != null) {
			String srcImg = "/imagenes/folder.png";

			treeItem.setValue(carpeta);
			treeItem.setTooltiptext(carpeta.getNombre());

			Treerow treerow = new Treerow();
			treerow.setParent(treeItem);

			Treecell treecell = new Treecell(carpeta.getNombre(), srcImg);
			treecell.addEventListener(Events.ON_CLICK, selectListener);
			treecell.setParent(treerow);
		}
		
		if (carpeta.getHijos() == null
				|| carpeta.getHijos().size() == 0) {
			return treeItem;
		}
		
		createChildren(treeItem, carpeta.getHijos());

		return treeItem;
	}

	protected Treeitem createNodoTransaccion(NodoMenu transaccion) {

		Treeitem treeItem = new Treeitem();

		if (transaccion != null) {
			String srcImgHijo = "/imagenes/window.png";

			treeItem.setValue(transaccion);
			treeItem.setTooltiptext(transaccion.getNombre());

			Treerow treerowchild = new Treerow();
			treerowchild.setParent(treeItem);

			Treecell treecellchild = new Treecell(transaccion.getNombre(),
					srcImgHijo);
			treecellchild.addEventListener(Events.ON_CLICK, selectListener);
			treecellchild.setParent(treerowchild);
		}
		
		if (transaccion.getHijos() == null
				|| transaccion.getHijos().size() == 0) {
			return treeItem;
		}
		
		createChildren(treeItem, transaccion.getHijos());

		return treeItem;

	}

	public void createTree(Treechildren treeChildren, NodoMenu folder) {
		Treeitem createBranch = createNodoCarpeta(folder);
		createBranch.setParent(treeChildren);
	}

	public void refreshTree(PayloadNodoMenuResponse payloadNodoMenuResponse) {
		Treechildren children = this.getTreechildren();
		
		if (children == null) {
			children = new Treechildren();
			children.setParent(this);
		} else {
			children.getChildren().clear();
		}
		
		if (payloadNodoMenuResponse.getObjetos() == null 
				|| !UtilPayload.isOK(payloadNodoMenuResponse)
				|| payloadNodoMenuResponse.getObjetos().size() == 0) {
			Alert.showMessage(payloadNodoMenuResponse);
			
			return;
		} else {
			Alert.hideMessage();
		}
		
		createTree(children, payloadNodoMenuResponse.getObjetos().get(0));
	}

}
