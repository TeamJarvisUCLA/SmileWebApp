package lights.seguridad;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.enums.TipoNodoMenuEnum;
import lights.seguridad.payload.response.PayloadNodoMenuResponse;
import lights.smile.consume.services.S;

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
public class MenuTreeOnDemand extends Tree {

	private static final long serialVersionUID = -78699237474490231L;
	
	protected void sendEvent(Object nodo) {
		
		if (nodo instanceof NodoMenu) {
			NodoMenu nodoMenu = (NodoMenu) nodo;
			
			if (nodoMenu.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.CARPETA)
					|| nodoMenu.getTipoNodoMenuEnum().equals(
							TipoNodoMenuEnum.RAIZ)) {
				Treeitem item = this.getSelectedItem();

				HashMap<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkNodoMenu.idNodoMenu", ((NodoMenu) nodo).getIdNodoMenu().toString());

				PayloadNodoMenuResponse payloadNodoMenuResponse = 
						S.NodoMenuService.consultarCriterios(TypeQuery.EQUAL, criterios);
				
				if (!UtilPayload.isOK(payloadNodoMenuResponse)) {
					Alert.showMessage(payloadNodoMenuResponse);
					
					return;
				}
				
				List<NodoMenu> nodosHijos = payloadNodoMenuResponse.getObjetos();
				
				if (nodosHijos != null && nodosHijos.size() > 0) {
					Collections.sort(nodosHijos, new Comparator<NodoMenu>() {

						@Override
						public int compare(NodoMenu o1, NodoMenu o2) {
							int i1 = ((NodoMenu) o1).getIdNodoMenu();
							int i2 = ((NodoMenu) o2).getIdNodoMenu();

							return Math.abs(i1) - Math.abs(i2);
						}

					});

					createChildren(item, nodosHijos);
				}
			}
		}

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
	
	protected Treerow createRow(NodoMenu nodo) {
		Treerow treerow = new Treerow();
		Treecell treecell = null;
		
		treecell = new Treecell(nodo.getNombre());	
		treecell.setTooltiptext(nodo.getNombre());
		treecell.addEventListener(Events.ON_CLICK, selectListener);
		treecell.setParent(treerow);
		
		return treerow;
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

		return treeItem;

	}

	public void createTree(Treechildren treeChildren, NodoMenu folder) {
		Treeitem createBranch = createNodoCarpeta(folder);
		createBranch.setParent(treeChildren);

	}

//	public Object getRootValue() {
//		Object rootValue = null;
//		Component root = this.getTreechildren().getFirstChild();
//
//		if (root instanceof Treeitem) {
//			Treeitem rootItem = (Treeitem) root;
//			rootValue = rootItem.getValue();
//		}
//
//		return rootValue;
//	}

	public void refreshTree(PayloadNodoMenuResponse payloadNodoMenuResponse) {
		Treechildren children = this.getTreechildren();
		
		if (children == null) {
			children = new Treechildren();
			children.setParent(this);
		} else {
			children.getChildren().clear();
		}
		
		if (!UtilPayload.isOK(payloadNodoMenuResponse)) {
			Alert.showMessage(payloadNodoMenuResponse);
			
			return;
		}

		createTree(children, payloadNodoMenuResponse.getObjetos().get(0));
	}
}
