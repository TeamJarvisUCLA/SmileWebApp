package ve.smile.basedatos.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;

import bsh.util.Util;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Tabla;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadTablaResponse;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;

public class VM_ExportarIndex extends VM_WindowSimpleListPrincipal<Tabla> {
	List<Tabla> listsTabla = new ArrayList<>();
	List<Tabla> listTablaAux = new ArrayList<>();
	Tabla posibleTabla = new Tabla();
	PayloadTablaResponse payloadTablaResponse = null;
	boolean controla = true;

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPayloadResponse<Tabla> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		payloadTablaResponse = S.TablaService.consultarTodos();
		return payloadTablaResponse;
	}

	@Command("onSelectTabla")
	public void onSelectTabla() {
		Tabla tabla = getSelectedObject();
		System.out.println(listsTabla.size());
	}

	@Command("onSelectTablaRespaldo")
	public void onSelectTablaRespaldo() {
		Tabla tabla = getPosibleTabla();
		// Alert.showSuccessMessage("Bien hecho bro", "vamos a guerriar");
		System.out.println("Tabla Seleccionada  " + tabla.getNombre());
	}

	@Command("onChooseItem")
	public void onChooseItem() {
		if (getSelectedObject() != null) {
			listsTabla.add(getSelectedObject());
			System.out.println(objectsList.remove(getSelectedObject()));
			System.out.println(listsTabla.size());
			S.TablaService.respaldarTablas(listsTabla, new Tabla());		
			refreshListsTabla();
		} else {
			Alert.showErrorMessage("099", "Debe seleccionar una Tabla");
		}
	}

	@Command("onRemoveItem")
	public void onRemoveItem() {
		if (getPosibleTabla() != null) {
			objectsList.add(getPosibleTabla());
			System.out.println(listsTabla.remove(getPosibleTabla()));
			refreshListsTabla();
		} else {
			Alert.showErrorMessage("099", "Debe seleccionar una Tabla");
		}
	}

	@Command("onChooseAllItem")
	public void onChooseAllItem() {
		if (!objectsList.isEmpty()) {
			listsTabla.addAll(objectsList);
			objectsList = new ArrayList<Tabla>();
			refreshListsTabla();
		}

	}

	@Command("onRemoveAllItem")
	public void onRemoveAllItem() {
		if (!listsTabla.isEmpty()) {
			objectsList.addAll(listsTabla);
			refreshListsTabla();
			listsTabla = new ArrayList<Tabla>();
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

	public List<Tabla> getListTablaAux() {
		return listTablaAux;
	}

	public void setListTablaAux(List<Tabla> listTablaAux) {
		this.listTablaAux = listTablaAux;
	}

	public Tabla getPosibleTabla() {
		return posibleTabla;
	}

	public void setPosibleTabla(Tabla posibleTabla) {
		this.posibleTabla = posibleTabla;
	}

}
