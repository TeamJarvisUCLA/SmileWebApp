package ve.seguridad.permisos.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.dto.Operacion;
import lights.seguridad.enums.TipoNodoMenuEnum;
import lights.seguridad.payload.response.PayloadNodoMenuResponse;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class C_MenuTreeFullPermisos extends Tree {

	private static final long serialVersionUID = -4680029582876559375L;
	
	public C_MenuTreeFullPermisos() {
		super();
		
		this.setSclass("menu-tree-full-permisos");
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
	
	protected Treeitem createNodoCarpeta(NodoMenu carpeta) {
		Treeitem treeItem = new Treeitem();

		if (carpeta != null) {
			String srcImg = "/imagenes/folder.png";

			treeItem.setValue(carpeta);
			treeItem.setTooltiptext(carpeta.getNombre());

			Treerow treerow = new Treerow();
			treerow.setParent(treeItem);

			Treecell treecell = new Treecell(carpeta.getNombre(), srcImg);
			treecell.setParent(treerow);
			
			Div divEmpty = new Div();
			Treecell cellToolbar = new Treecell();
			cellToolbar.appendChild(divEmpty);
			cellToolbar.setParent(treerow);
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
			treecellchild.setParent(treerowchild);
			
			C_ToolbarPermisos toolbarPermisos = new C_ToolbarPermisos();
			toolbarPermisos.createButtons(transaccion.getFkVista().getOperaciones(), 
					transaccion.getOperaciones(), this, transaccion);
			Treecell cellToolbar = new Treecell();
			cellToolbar.appendChild(toolbarPermisos);
			cellToolbar.setParent(treerowchild);
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

	public void onSelectButtonToolbar(Operacion operacion, C_ToolbarPermisos toolbarPermisos) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("toolbar", toolbarPermisos);
		data.put("operacion", operacion);
		data.put("nodoMenu", toolbarPermisos.getNodoMenu());
		
		Events.postEvent("onSelectButtonToolbar", this, data);
	}
}
