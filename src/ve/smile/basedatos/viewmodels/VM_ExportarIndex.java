package ve.smile.basedatos.viewmodels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Tabla;
import ve.smile.seguridad.payload.response.PayloadTablaResponse;
import karen.core.crux.alert.Alert;
import karen.core.util.validate.UtilValidate;


/**
 * @author Willandherg
 *
 */
public class VM_ExportarIndex {
	List<Tabla> listsTabla = new ArrayList<>();
	List<Tabla> listTablaAux = new ArrayList<>();
	private List<Tabla> objectsList;
	private String nombre,descripcion;
	private Tabla selectedObject;
	Tabla posibleTabla = new Tabla();
	PayloadTablaResponse payloadTablaResponse = null;
	boolean controla = true;
	@Init(superclass = true)
	public void childInit() {
		System.out.println("syso childInit");
		payloadTablaResponse = S.TablaService.consultarTodos();
		objectsList = payloadTablaResponse.getObjetos();
	}
	@Command("respaldarTablas")
	public void respaldarTablas() {
		if(!isFormValidated()){
			return;
		}
		if(!listsTabla.isEmpty()){
			HashMap<String, Object> parametros = new HashMap<>();
			parametros.put("nombre", getNombre());
			parametros.put("descripcion", getDescripcion());
		PayloadTablaResponse p =	S.TablaService.respaldarTablas(listsTabla,parametros);	
		Alert.showSuccessMessage("055", "Tablas Respaldadas con Exito");
		listsTabla = new ArrayList<>();
		}
		else{
			Alert.showErrorMessage("099", "No existen Tablas para Respaldar");
		}
	}

	@Command("onChooseItem")
	public void onChooseItem() {
		if (getSelectedObject() != null) {
			listsTabla.add(getSelectedObject());
			System.out.println(objectsList.remove(getSelectedObject()));
			System.out.println(listsTabla.size());		
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
	
	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getNombre(), "Nombre", 100);
			UtilValidate.validateString(getDescripcion(), "Descripcion",255);
			UtilValidate.validateList(listsTabla, "lista a respaldar");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
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

	public List<Tabla> getObjectsList() {
		return objectsList;
	}

	public void setObjectsList(List<Tabla> objectsList) {
		this.objectsList = objectsList;
	}

	public Tabla getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Tabla selectedObject) {
		this.selectedObject = selectedObject;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

}
