package ve.smile.basedatos.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;

import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.dto.Tabla;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadTablaResponse;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;

public class VM_ImportarIndex extends VM_WindowSimpleListPrincipal<Tabla>  {
	List<Tabla> listsTabla = new ArrayList<>();
	List<Tabla> listTablaAux = new ArrayList<>();
	PayloadTablaResponse payloadTablaResponse = null;
	boolean controla = true;

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPayloadResponse<Tabla> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		// TODO Auto-generated method stub
		
		if(controla){
			 payloadTablaResponse = S.TablaService.consultarTodos();
			 listTablaAux  =    payloadTablaResponse.getObjetos();
			 controla = false;
		}
		//System.out.println(payloadTablaResponse.getInformacion());
		
		for(Tabla tabla: listsTabla){
			listTablaAux.remove(tabla);
		}
		S.TablaService.respaldarTablas(listsTabla, new Tabla());
		//{totalElementsReturned=10.0, totalElementsInTable=119.0, lastPage=false, firstPage=true, isOK=true, page=1.0, mensaje=S:Success Code: 002-Registros consultados con exito, totalPaginas=12.0, totalElementsByPage=10.0}
		payloadTablaResponse.setInformacion("totalElementsReturned", (double)listTablaAux.size());
		payloadTablaResponse.setInformacion("totalElementsInTable", (double)listTablaAux.size());
		payloadTablaResponse.setInformacion("lastPage", false);
		payloadTablaResponse.setInformacion("firstPage", true);
		payloadTablaResponse.setInformacion("page", (double)1);

		payloadTablaResponse.setInformacion("totalPaginas", (double)12);
		payloadTablaResponse.setInformacion("totalElementsByPage", (double)10);
		
		
		return payloadTablaResponse;
	}
	
	@Command("onSelectTabla")
	public void onSelectTabla() {
		Tabla tabla = getSelectedObject();
		System.out.println("Tabla Seleccionada  "+ tabla.getNombre());
	}
	
	 @Command("onChooseItem")
	    public void onChooseItem() {
	     	if(getSelectedObject()!=null){
	     		listsTabla.add(getSelectedObject());
	     		getDataToTable(10, 12);
	     		System.out.println(objectsList.remove(getSelectedObject()));
	     		refreshListsTabla();
	     	}
	    }
	 
	 public void refreshListsTabla() {
			BindUtils.postNotifyChange(null, null, this, "objectsList");
			BindUtils.postNotifyChange(null, null, this, "listsTabla");
		}

	public List<Tabla> getListsTabla() {
		return listsTabla;
	}

	public void setListsTabla(List<Tabla> listsTabla) {
		this.listsTabla = listsTabla;
	}
	 

}
