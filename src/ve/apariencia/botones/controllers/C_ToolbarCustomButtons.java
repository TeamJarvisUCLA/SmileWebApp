package ve.apariencia.botones.controllers;

import java.util.List;

import karen.core.toolbar.ButtonToolbar;
import lights.seguridad.dto.Operacion;
import lights.seguridad.dto.Vista;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbar;

public class C_ToolbarCustomButtons extends Toolbar {

	private static final long serialVersionUID = -5209902371014745184L;
	
	private C_TreeFullVentanas treeFullVentanas;
	private Vista vista;

	@SuppressWarnings("unchecked")
	public void createButtons(List<Operacion> operacionesVista, C_TreeFullVentanas treeFullVentanas,
			Vista vista) {
		
		this.treeFullVentanas = treeFullVentanas;
		this.vista = vista;
		
		this.getChildren().clear();
		this.setSclass("ka-toolbar toolbar-custom-buttons");
		
		for (Operacion operacion : operacionesVista) {
			ButtonToolbar button = new ButtonToolbar(operacion);
			
			button.addEventListener(Events.ON_CLICK, funcionesListener);
			this.getChildren().add(button);
		}
	}	
	
	private void lanzarEvento(Operacion operacion) {
		treeFullVentanas.onSelectButtonToolbar(operacion, this);
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

	public Vista getVista() {
		return vista;
	}
	
	public void refrescarBoton(Operacion operacion) {
		for (Component component : this.getChildren()) {
			if (component instanceof ButtonToolbar) {
				ButtonToolbar button = (ButtonToolbar) component;
				
				if (button.getOperacion().equals(operacion)) {
					button.setOperacion(operacion);
					button.setSclass("btn-floating btn-small btn waves-effect waves-light " + operacion.getFkSclass().getNombre());
					button.setIconSclass(operacion.getFkIconSclass().getNombre());
					button.setTooltiptext(operacion.getTooltiptext());
					
					break;
				}
			}
		}
	}
}
