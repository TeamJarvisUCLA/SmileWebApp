package ve.apariencia.botones.viewmodels;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.dto.IconSclass;
import lights.seguridad.dto.Operacion;
import lights.seguridad.dto.Sclass;
import lights.seguridad.dto.Vista;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.apariencia.botones.controllers.C_BotonesIndex;

public class VM_BotonesIndex extends VM_WindowSimpleListPrincipal<Vista> {

	private C_BotonesIndex cBotonesIndex;
	private Operacion operacion;
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Vista> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

//		PayloadRolResponse payloadRolResponse = 
//				S.RolService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return null;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return null;
	}

	public C_BotonesIndex getcBotonesIndex() {
		return cBotonesIndex;
	}

	public void setcBotonesIndex(C_BotonesIndex cBotonesIndex) {
		this.cBotonesIndex = cBotonesIndex;
	}

	public Operacion getOperacion() {
		return operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}
	
	public void setOperacionAndRefresh(Operacion operacion) {
		this.operacion = operacion;
		
		refreshOperation();
	}
	
	public String getTooltiptext() {
		if (getOperacion() == null) {
			return "";
		}
		
		return getOperacion().getTooltiptext();
	}
	
	public void setTooltiptext(String tooltiptext) {
		if (getOperacion() == null) {
			return;
		}
		
		getOperacion().setTooltiptext(tooltiptext);
		
		getcBotonesIndex().setTooltiptextToolbarPreview(getOperacion(), tooltiptext);
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
				
				getOperacion().setFkIconSclass(catalogueDialogCloseEvent.getEntity());
				refreshOperation();
				
				getcBotonesIndex().setIconSclassToolbarPreview(getOperacion(), catalogueDialogCloseEvent.getEntity().getNombre());
			}
		});
		
		UtilDialog.showDialog("views/seguridad/menu/catalogoIconSclass.zul", catalogueDialogData);
	}
	
	@Command("buscarColor")
	public void buscarColor() {
		CatalogueDialogData<Sclass> catalogueDialogData = new CatalogueDialogData<Sclass>();
		
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Sclass>() {
			
			@Override
			public void onClose(CatalogueDialogCloseEvent<Sclass> catalogueDialogCloseEvent) {
				if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR)) {
					return;
				}

				getOperacion().setFkSclass(catalogueDialogCloseEvent.getEntity());
				refreshOperation();
				
				getcBotonesIndex().setSclassToolbarPreview(getOperacion(), catalogueDialogCloseEvent.getEntity().getNombre());
			}
		});
		
		UtilDialog.showDialog("views/apariencia/botones/catalogoSclass.zul", catalogueDialogData);
	}
	
	public void refreshOperation() {
		BindUtils.postNotifyChange(null, null, this, "operacion");
		BindUtils.postNotifyChange(null, null, this, "fkIconSclass2x");
		BindUtils.postNotifyChange(null, null, this, "fkSclassCircle");
		BindUtils.postNotifyChange(null, null, this, "tooltiptext");
		BindUtils.postNotifyChange(null, null, this, "isOperationSelected");
	}
	
	public String getFkIconSclass2x() {
		if (getOperacion() == null ||
				getOperacion().getFkIconSclass() == null) {
			return "";
		}
		
		return getOperacion().getFkIconSclass().getNombre() + " fa-2x";
	}
	
	public String getfkSclassCircle() {
		if (getOperacion() == null ||
				getOperacion().getFkSclass() == null) {
			return "";
		}
		
		return getOperacion().getFkSclass().getNombre() + " estilo-color-selected";
	}
	
	public boolean getIsOperationSelected() {
		return !(getOperacion() == null);
	}
}
