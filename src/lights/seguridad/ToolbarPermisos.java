package lights.seguridad;

import java.util.List;

import karen.core.toolbar.ButtonToolbar;
import lights.seguridad.dto.NodoMenu;
import lights.seguridad.dto.Operacion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbar;

public class ToolbarPermisos extends Toolbar {

	private static final long serialVersionUID = -5209902371014745184L;
	
	private List<Operacion> operacionesNodoMenu;
	
	private MenuTreeFullPermisos menuTreeFullPermisos;
	private NodoMenu nodoMenu;

	@SuppressWarnings("unchecked")
	public void createButtons(List<Operacion> operacionesVista,
			List<Operacion> operacionesNodoMenu, MenuTreeFullPermisos menuTreeFullPermisos,
			NodoMenu nodoMenu) {
		
		this.operacionesNodoMenu = operacionesNodoMenu;
		this.menuTreeFullPermisos = menuTreeFullPermisos;
		this.nodoMenu = nodoMenu;
		
		this.getChildren().clear();
		this.setSclass("ka-toolbar toolbar-permisos");
		
		for (Operacion operacion : operacionesVista) {
			ButtonToolbar button = new ButtonToolbar(operacion);
			
			if (!operacionesNodoMenu.contains(operacion)) {
				button.setSclass(button.getSclass() + " button_blur");
			}
			
			button.addEventListener(Events.ON_CLICK, funcionesListener);
			this.getChildren().add(button);
		}
	}	
	
	private void lanzarEvento(Operacion operacion) {
		menuTreeFullPermisos.onSelectButtonToolbar(operacion, this);
	}	

	@SuppressWarnings("rawtypes")
	private EventListener funcionesListener = new EventListener() {
		
		@Override
		public void onEvent(Event event) throws Exception {
			Object t = event.getTarget();

			if (t instanceof ButtonToolbar) {
				ButtonToolbar buttonToolbar = (ButtonToolbar) t;

				Operacion operacion = buttonToolbar.getOperacion();
				lanzarEvento(operacion);
			} 	
		}		
	};
	
	public void removePermission(Operacion operacion) {
		for (Component component : this.getChildren()) {
			if (component instanceof ButtonToolbar) {
				ButtonToolbar button = (ButtonToolbar) component;
				
				if (button.getOperacion().equals(operacion)) {
					
					if (!button.getSclass().contains("button_blur")) {
						button.setSclass(button.getSclass() + " button_blur");
					}
					
					break;
				}
			}
		}
		
		operacionesNodoMenu.remove(operacion);
	}
	
	public void addPermission(Operacion operacion) {
		for (Component component : this.getChildren()) {
			if (component instanceof ButtonToolbar) {
				ButtonToolbar button = (ButtonToolbar) component;
				
				if (button.getOperacion().equals(operacion)) {
					
					if (button.getSclass().contains("button_blur")) {
						button.setSclass(button.getSclass().replace("button_blur", ""));
					}
					
					break;
				}
			}
		}
		
		operacionesNodoMenu.add(operacion);
	}
	
	public Boolean hasPermission(Operacion operacion) {
		return operacionesNodoMenu.contains(operacion);
	}

	public NodoMenu getNodoMenu() {
		return nodoMenu;
	}
	
	
}
