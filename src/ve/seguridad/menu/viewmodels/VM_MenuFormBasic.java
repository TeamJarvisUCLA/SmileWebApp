package ve.seguridad.menu.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.smile.consume.services.S;
import ve.smile.seguridad.dto.IconSclass;
import ve.smile.seguridad.dto.NodoMenu;
import ve.smile.seguridad.dto.Vista;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.TipoNodoMenuEnum;
import ve.smile.seguridad.payload.response.PayloadNodoMenuResponse;

@SuppressWarnings("unchecked")
public class VM_MenuFormBasic extends VM_WindowForm {
	
	private List<Vista> vistas;
	
	@Init(superclass = true)
	public void childInit_VM_PermisosFormBasic() {
		vistas = (List<Vista>) DataCenter.getDatoAdicional("vistas");
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}
		
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadNodoMenuResponse payloadNodoMenuResponse =
					S.NodoMenuService.incluir(getNodoMenu());
			
			Alert.showMessage(payloadNodoMenuResponse);

			if(!UtilPayload.isOK(payloadNodoMenuResponse)) {
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadNodoMenuResponse payloadNodoMenuResponse =
					S.NodoMenuService.modificar(getNodoMenu());
			
			Alert.showMessage(payloadNodoMenuResponse);

			if(!UtilPayload.isOK(payloadNodoMenuResponse)) {
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	public NodoMenu getNodoMenu() {
		return (NodoMenu) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getNodoMenu().getNombre(), "Nombre", 99);
			
			if ((getNodoMenu().getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.TRANSACCION))) {
				UtilValidate.validateNullOrEmpty(getNodoMenu().getFkVista(), "Archivo Zul");
			}
			
			UtilValidate.validateNullOrEmpty(getNodoMenu().getFkIconSclass(), "Icono");
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

	public boolean getVisibleByTipoNodoMenu() {
		return (getNodoMenu().getTipoNodoMenuEnum().equals(TipoNodoMenuEnum.TRANSACCION))?true:false;
	}

	public List<Vista> getVistas() {
		return vistas;
	}

	public void setVistas(List<Vista> vistas) {
		this.vistas = vistas;
	}
	
	@Command("buscarIcono")
	public void buscarIcono() {
		CatalogueDialogData<IconSclass> catalogueDialogData = new CatalogueDialogData<IconSclass>();
		
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<IconSclass>() {
			
			@Override
			public void onClose(CatalogueDialogCloseEvent<IconSclass> catalogueDialogCloseEvent) {
				if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR)) {
					return;
				}
				
				getNodoMenu().setFkIconSclass(catalogueDialogCloseEvent.getEntity());
				
				refreshNodoMenu();
			}
		});
		
		UtilDialog.showDialog("views/seguridad/menu/catalogoIconSclass.zul", catalogueDialogData);
	}
	
	public void refreshNodoMenu() {
		BindUtils.postNotifyChange(null, null, this, "nodoMenu");
		BindUtils.postNotifyChange(null, null, this, "fkIconSclass2x");
	}
	
	public String getFkIconSclass2x() {
		if (getNodoMenu() == null ||
				getNodoMenu().getFkIconSclass() == null) {
			return "";
		}
		
		return getNodoMenu().getFkIconSclass().getNombre() + " fa-2x";
	}
	
}
