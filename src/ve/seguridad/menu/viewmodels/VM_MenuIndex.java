package ve.seguridad.menu.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.dialog.message_box.events.listeners.MessageBoxDialogCloseListener;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.TipoNodoMenuEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;
import ve.smile.seguridad.payload.response.PayloadIconSclassResponse;
import ve.smile.seguridad.payload.response.PayloadNodoMenuResponse;
import ve.smile.seguridad.payload.response.PayloadOperacionResponse;
import ve.smile.seguridad.payload.response.PayloadVistaResponse;
import ve.smile.seguridad.dto.NodoMenu;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.dto.Rol;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.seguridad.menu.controllers.C_MenuIndex;

public class VM_MenuIndex extends VM_WindowSimpleListPrincipal<NodoMenu> {

	private Rol rolSelected = null;
	private Operacion operacionSelected = null;
	
	private List<Operacion> operaciones = new ArrayList<Operacion>();
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<NodoMenu> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		return null;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/seguridad/menu/menuFormBasic.zul";
	}

	public Rol getRolSelected() {
		return rolSelected;
	}

	public void setRolSelected(Rol rolSelected) {
		this.rolSelected = rolSelected;
	}

	@Command("onSelectRol")
	public void onSelectRol() {
		PayloadOperacionResponse payloadOperacionResponse = 
				S.OperacionService.consultarByNodoMenuAndRoles(selectedObject.getIdNodoMenu(), String.valueOf(rolSelected.getIdRol()));
		
		if (!UtilPayload.isOK(payloadOperacionResponse)) {
			Alert.showMessage(payloadOperacionResponse);
			
			return;
		}
		
		setOperacionesAndRefresh(payloadOperacionResponse.getObjetos());
	}
	
	public void setOperacionesAndRefresh(List<Operacion> operaciones) {
		this.operaciones = operaciones;
		
		BindUtils.postNotifyChange(null, null, this, "operaciones");
	}
	
	public void setRolSelectedAndRefresh(Rol rol) {
		this.rolSelected = rol;
		
		BindUtils.postNotifyChange(null, null, this, "rolSelected");
	}
	
	public void refreshIsTransaccion() {
		BindUtils.postNotifyChange(null, null, this, "isTransaccion");
	}
	
	public void setOperacionSelectedAndRefresh(Operacion operacion) {
		this.operacionSelected = operacion;
		
		BindUtils.postNotifyChange(null, null, this, "operacionSelected");
	}

	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	public Operacion getOperacionSelected() {
		return operacionSelected;
	}

	public void setOperacionSelected(Operacion operacionSelected) {
		this.operacionSelected = operacionSelected;
	}
	
	public Boolean getIsTransaccion() {
		if (selectedObject == null) {
			return false;
		}
		
		if (selectedObject.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.TRANSACCION)) {
			return true;
		}
		
		return false;
	}

	@Override
	public void executeIncluir() throws Exception {
		CatalogueDialogData<TipoNodoMenuEnum> catalogueDialogData = new CatalogueDialogData<TipoNodoMenuEnum>();
		
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<TipoNodoMenuEnum>() {
			
			@Override
			public void onClose(CatalogueDialogCloseEvent<TipoNodoMenuEnum> catalogueDialogCloseEvent) {
				if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR)) {
					return;
				}
				
				Operacion operacion = OperacionHelper.getPorType(OperacionEnum.INCLUIR);
				
				NodoMenu nodoMenu = new NodoMenu(selectedObject, "", 
						catalogueDialogCloseEvent.getEntity().ordinal(), null, null);
				
				DataCenter.updateSrcPageContent(nodoMenu, operacion, 
						getSrcFileZulForm(OperacionEnum.INCLUIR));
			}
		});
		
		UtilDialog.showDialog("views/seguridad/menu/catalogoTipoNodoMenu.zul", catalogueDialogData);
	}

	@Override
	public String isValidPreconditionsIncluir() {
		if (selectedObject == null || 
				selectedObject.getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.TRANSACCION)) {
			return "E:Error Code 111-Debe seleccionar un Menú de tipo 'RAIZ' o 'CARPETA'";
		}
		
		return "";
	}

	@Override
	public String isValidSearchDataIncluir() {
		PayloadVistaResponse payloadVistaResponse =
				S.VistaService.consultarTodos();
		
		if (!UtilPayload.isOK(payloadVistaResponse)) {
			return (String) payloadVistaResponse.getInformacion(IPayloadResponse.MENSAJE);
		}
		
		if (payloadVistaResponse.getObjetos() == null ||
				payloadVistaResponse.getObjetos().size() == 0) {
			return "E:Error Code: 333-No hay vistas registradas. No podrá incluir un nuevo Menú";
		}
		
		PayloadIconSclassResponse payloadIconoSclassResponse =
				S.IconSclassService.contar();

		if (!UtilPayload.isOK(payloadIconoSclassResponse)) {
			return (String) payloadVistaResponse.getInformacion(IPayloadResponse.MENSAJE);
		}

		Long count = 
				((Double)payloadIconoSclassResponse.getInformacion(IPayloadResponse.COUNT)).longValue();
		
		if (count <= 0) {
			return "E:Error Code: 333-No hay iconos registrados. No podrá incluir un nuevo Menú";
		}
		
		DataCenter.putDatoAdicional("vistas", payloadVistaResponse.getObjetos());
		
		return "";
	}
	
	@Override
	public String isValidSearchDataModificar() {
		PayloadVistaResponse payloadVistaResponse =
				S.VistaService.consultarTodos();
		
		if (!UtilPayload.isOK(payloadVistaResponse)) {
			return (String) payloadVistaResponse.getInformacion(IPayloadResponse.MENSAJE);
		}
		
		if (payloadVistaResponse.getObjetos() == null ||
				payloadVistaResponse.getObjetos().size() == 0) {
			return "E:Error Code: 333-No hay vistas registradas. No podrá modificar éste Menú";
		}
		
		PayloadIconSclassResponse payloadIconoSclassResponse =
				S.IconSclassService.contar();

		if (!UtilPayload.isOK(payloadIconoSclassResponse)) {
			return (String) payloadVistaResponse.getInformacion(IPayloadResponse.MENSAJE);
		}

		Long count = 
				((Double) payloadIconoSclassResponse.getInformacion(IPayloadResponse.COUNT)).longValue();
		
		if (count <= 0) {
			return "E:Error Code: 333-No hay iconos registrados. No podrá incluir un nuevo Menú";
		}
		
		DataCenter.putDatoAdicional("vistas", payloadVistaResponse.getObjetos());
		
		return "";
	}
	
	public String isValidPreconditionsEliminar() {
		if (selectedObject == null) {
			return "I:Information Code: 103-Debe seleccionar el registro a eliminar";
		}
		
		PayloadNodoMenuResponse payloadNodoMenuResponse = 
				S.NodoMenuService.contarHijos(selectedObject.getIdNodoMenu());
		
		if (!UtilPayload.isOK(payloadNodoMenuResponse)) {
			return (String) payloadNodoMenuResponse.getInformacion(IPayloadResponse.MENSAJE);
		}
		
		Long count = 
				((Double) payloadNodoMenuResponse.getInformacion(IPayloadResponse.COUNT)).longValue();
		
		if (count > 0) {
			return "E:Error Code: 334-Este menú tiene Menues hijos asociados. No puede ser eliminado";
		}
		
		return "";
	}

	public void executeEliminar() {
		UtilDialog.showMessageBoxConfirmation("¿Desea eliminar el registro seleccionado? (Se eliminarán todos los permisos asociados)", new MessageBoxDialogCloseListener() {
			
			@Override
			public void onClose(MessageBoxDialogCloseEvent messageBoxDialogCloseEvent) {
				if (messageBoxDialogCloseEvent.getDialogAction().equals(DialogActionEnum.ACEPTAR)) {
					PayloadNodoMenuResponse payloadNodoMenuResponse = 
							S.NodoMenuService.eliminarEnCascada(selectedObject.getIdNodoMenu());
					
					Alert.showMessage(payloadNodoMenuResponse);

					if (UtilPayload.isOK(payloadNodoMenuResponse)) {
						((C_MenuIndex) getControllerWindowSimpleListPrincipal()).refreshTree();
					}
				}
			}
		});
	}

}
