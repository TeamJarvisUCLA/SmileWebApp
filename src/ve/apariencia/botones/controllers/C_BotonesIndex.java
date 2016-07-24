package ve.apariencia.botones.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tabbox;

import ve.apariencia.botones.viewmodels.VM_BotonesIndex;
import karen.core.crux.alert.Alert;
import karen.core.form.buttons.containers.DivButtonsForm;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.simple_list_principal.controllers.C_WindowSimpleListPrincipal;
import karen.core.toolbar.Toolbar;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.dto.Vista;
import ve.smile.seguridad.dto.VistaOperacionCustom;
import ve.smile.seguridad.payload.response.PayloadOperacionResponse;
import ve.smile.seguridad.payload.response.PayloadRolResponse;
import ve.smile.seguridad.payload.response.PayloadVistaOperacionCustomResponse;

@SuppressWarnings({"rawtypes", "unchecked"})
public class C_BotonesIndex extends C_WindowSimpleListPrincipal<Vista> {

	private static final long serialVersionUID = 5268239355072291952L;
	
	private Toolbar toolbarAparienciasComunes;
	private Toolbar toolbarPreview;
	protected DivButtonsForm divButtonsForm;
	private C_TreeFullVentanas tree;
	private C_BotonesIndex me;
	
	private C_ToolbarCustomButtons toolbarCustomButtons;
	private Vista vistaSelected;
	private Tabbox tabbox;
	
	public C_BotonesIndex() {
		super();
		
		me = this;
		this.addEventListener("onCreate", onCreateC_BotonesIndex);
	}

	protected EventListener<Event> onCreateC_BotonesIndex = new EventListener() {
		public void onEvent(Event event) throws Exception {
			getVmBotonesIndex().setcBotonesIndex(me);
			
			PayloadOperacionResponse payloadOperacionResponse = 
					S.OperacionService.consultarBotonesComunes();
			
			if (!UtilPayload.isOK(payloadOperacionResponse)) {
				Alert.showMessage(payloadOperacionResponse);
				
				return;
			}
			
			toolbarAparienciasComunes.createButtons(payloadOperacionResponse.getObjetos());
			
			tree.refreshTree(S.VistaService.consultarTodosConBotonesCustom());
			
			tabbox.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					getVmBotonesIndex().setOperacionAndRefresh(null);
				}
			});
		}
	};
	
	public void onSelectButtonFormulario$divButtonsForm(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}
		ForwardEvent forwardEvent = (ForwardEvent) event;

		OperacionForm operacionForm = (OperacionForm) forwardEvent.getOrigin().getData();

		if (operacionForm.getIdOperacion().equals(OperacionFormEnum.GUARDAR.ordinal())) {
			PayloadOperacionResponse payloadOperacionResponse = 
					S.OperacionService.modificarBotonesComunes(getVmBotonesIndex().getOperacion());
			
			if (!UtilPayload.isOK(payloadOperacionResponse)) {
				Alert.showMessage(payloadOperacionResponse);
				
				return;
			} 
			
			payloadOperacionResponse.setInformacion(PayloadRolResponse.MENSAJE,
					"S:Success Code: 005-Boton común modificado con exito");
				
			Alert.showMessage(payloadOperacionResponse);
			
			toolbarAparienciasComunes.refrescarBoton(getVmBotonesIndex().getOperacion());
			
			getVmBotonesIndex().setOperacionAndRefresh(null);
			
			return;
		}
		
		if (operacionForm.getIdOperacion().equals(OperacionFormEnum.CUSTOM1.ordinal())) {
			VistaOperacionCustom vistaOperacionCustom = new VistaOperacionCustom(getVmBotonesIndex().getOperacion().getIdOperacion(), vistaSelected, 
					getVmBotonesIndex().getOperacion().getFkIconSclass(), 
					getVmBotonesIndex().getOperacion().getFkSclass(), 
					getVmBotonesIndex().getOperacion().getTooltiptext(),
					getVmBotonesIndex().getOperacion().getNombre());
			
			PayloadVistaOperacionCustomResponse payloadVistaOperacionCustomResponse = 
					S.VistaOperacionCustomService.modificarSinId(vistaOperacionCustom);

			if (!UtilPayload.isOK(payloadVistaOperacionCustomResponse)) {
				Alert.showMessage(payloadVistaOperacionCustomResponse);
				
				return;
			} 

			payloadVistaOperacionCustomResponse.setInformacion(PayloadRolResponse.MENSAJE,
					"Success Code: 005-Boton custom modificado con exito");
				
			Alert.showMessage(payloadVistaOperacionCustomResponse);
			
			toolbarCustomButtons.refrescarBoton(getVmBotonesIndex().getOperacion());
			
			getVmBotonesIndex().setOperacionAndRefresh(null);
			
			return;
		}
	}
	
	public void onSelectButtonToolbar$toolbarAparienciasComunes(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}

		ForwardEvent forwardEvent = (ForwardEvent) event;

		Operacion operacion = (Operacion) forwardEvent.getOrigin().getData();
		
		getVmBotonesIndex().setOperacionAndRefresh(operacion);
		
		List<Operacion> operaciones = new ArrayList<Operacion>();
		operaciones.add(operacion);
		
		toolbarPreview.createButtons(operaciones);
		
		List<OperacionForm> operationsForm = new ArrayList<OperacionForm>();
		operationsForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
		
		divButtonsForm.createButtons(operationsForm);
	}
	
	public void onSelectButtonToolbar$tree(Event event) throws InterruptedException {
		if (!(event instanceof ForwardEvent)) {
			return;
		}

		ForwardEvent forwardEvent = (ForwardEvent) event;

		Map<String, Object> data = (Map<String, Object>) forwardEvent.getOrigin().getData();
		
		this.toolbarCustomButtons = (C_ToolbarCustomButtons) data.get("toolbar");
		this.vistaSelected = (Vista) data.get("vista");
		Operacion operacion = (Operacion) data.get("operacion");

		getVmBotonesIndex().setOperacionAndRefresh(operacion);
		
		List<Operacion> operaciones = new ArrayList<Operacion>();
		operaciones.add(operacion);
		
		toolbarPreview.createButtons(operaciones);
		
		List<OperacionForm> operationsForm = new ArrayList<OperacionForm>();
		OperacionForm operacionFormGuardar = OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR);
		
		OperacionForm operacionFormCustom1 = OperacionFormHelper.getPorType(OperacionFormEnum.CUSTOM1); 
		operacionFormCustom1.setIconSclass(operacionFormGuardar.getIconSclass());
		operacionFormCustom1.setLabel(operacionFormGuardar.getLabel());
		operacionFormCustom1.setSclass(operacionFormGuardar.getSclass());
		
		operationsForm.add(operacionFormCustom1);
		
		divButtonsForm.createButtons(operationsForm);
	}
	
	public VM_BotonesIndex getVmBotonesIndex() {
		return (VM_BotonesIndex) vm();
	}
	
	public void setIconSclassToolbarPreview(Operacion operacion, String iconSclass) {
		toolbarPreview.setIconSclassAUnBoton(operacion, iconSclass);
	}
	
	public void setSclassToolbarPreview(Operacion operacion, String sclass) {
		toolbarPreview.setSclassColorAUnBoton(operacion, sclass);
	}
	
	public void setTooltiptextToolbarPreview(Operacion operacion, String tooltiptext) {
		toolbarPreview.setTooltiptextAUnBoton(operacion, tooltiptext);
	}
}
