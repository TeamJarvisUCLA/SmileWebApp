package ve.apariencia.botones.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.enums.DialogResultEnum;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.viewmodels.VM_GenericCatalogueDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Sclass;
import ve.smile.seguridad.payload.response.PayloadSclassResponse;

public class VM_CatalogoSclass extends VM_GenericCatalogueDialog<Sclass> {

	@Init(superclass=true)
    public void childInit_VM_CatalogoSclass() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
	public List<List<Sclass>> getListasColores() {
		PayloadSclassResponse payloadSclassResponse  = 
				S.SclassService.consultarTodos();
		
		if (!UtilPayload.isOK(payloadSclassResponse)) {
			Alert.showMessage(payloadSclassResponse);
			
			return new ArrayList<List<Sclass>>();
		}
		
		List<Sclass> objectList = payloadSclassResponse.getObjetos();
		
		List<List<Sclass>> listas = new ArrayList<List<Sclass>>();
		
		for (int i = 1; i < 21; i++) {
			List<Sclass> colores;
			
			if (i < 17) {
				colores = new ArrayList<Sclass>(objectList.subList((i - 1) * 14, (i * 14)));
			} else if (i < 20) {
				colores = new ArrayList<Sclass>(objectList.subList(225 + (i - 17) * 10, 224 + ((i - 16) * 10)));
			} else {
				colores = new ArrayList<Sclass>(objectList.subList(objectList.size() - 3, objectList.size()));
			}
			
			listas.add(colores);
		}
		
		return listas;
	}

	@Command("colorSelected")
	public void colorSelected(@BindingParam("event") Event event, 
			@BindingParam("sclass") Sclass sclass) {
		
		if (sclass == null) {
			UtilDialog.showMessageBoxError("Ha ocurrido un error al selecconar el color");
			return;
		}
		
		CatalogueDialogCloseEvent<Sclass> catalogueDialogCloseEvent =
				getDialogCloseEventOnAccept(event);
		
		catalogueDialogCloseEvent.setEntity(sclass);
		
		getControllerWindowDialog().close(catalogueDialogCloseEvent);
	}
	
	@Override
	public CatalogueDialogCloseEvent<Sclass> getDialogCloseEventOnAccept(
			Event event) {
		return new CatalogueDialogCloseEvent<Sclass>(event, DialogActionEnum.ACEPTAR, DialogResultEnum.ONE);
	}

	@Override
	public String validateData() {
		return null;
	}
	
}
