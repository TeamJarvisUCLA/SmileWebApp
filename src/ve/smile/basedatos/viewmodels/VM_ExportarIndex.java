package ve.smile.basedatos.viewmodels;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.util.validate.UtilValidate;
import lights.smile.util.Zki;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;

import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Tabla;
import ve.smile.seguridad.payload.response.PayloadTablaResponse;

/**
 * @author Willandherg
 * 
 */
public class VM_ExportarIndex {

	List<Tabla> listsTabla = new ArrayList<>();
	List<Tabla> listTablaAux = new ArrayList<>();
	private List<Tabla> objectsList;
	private String nombre, descripcion;
	private Tabla selectedObject;
	Tabla posibleTabla = new Tabla();
	PayloadTablaResponse payloadTablaResponse = null;
	HashMap<String, Object> parametros = new HashMap<>();
	boolean pendiente = false;

	@Init(superclass = true)
	public void childInit() {
		payloadTablaResponse = S.TablaService.consultarTodos();
		objectsList = payloadTablaResponse.getObjetos();
	}

	@Command("respaldarTablas")
	@NotifyChange({ "objectsList", "listsTabla", "nombre", "descripcion" })
	public void respaldarTablas() {
		if (!isFormValidated()) {
			return;
		}
		if (!listsTabla.isEmpty()) {
			parametros.put("completo", pendiente);
			parametros.put("nombre", getNombre());
			parametros.put("descripcion", getDescripcion());
			S.TablaService.respaldarTablas(listsTabla, parametros);
			payloadTablaResponse = S.TablaService.consultarTodos();
			objectsList = payloadTablaResponse.getObjetos();
			Alert.showSuccessMessage("055", "Tablas Respaldadas con Exito");
			nombre = new String();
			descripcion = new String();
			listsTabla = new ArrayList<>();
		} else {
			Alert.showErrorMessage("099", "No existen Tablas para Respaldar");
		}
	}

	@Command("onChooseItem")
	@NotifyChange({ "objectsList", "listsTabla" })
	public void onChooseItem() {
		if (getSelectedObject() != null) {
			pendiente = false;
			listsTabla.add(getSelectedObject());
		} else {
			Alert.showErrorMessage("099", "Debe seleccionar una Tabla");
		}
	}

	@Command("onRemoveItem")
	@NotifyChange({ "objectsList", "listsTabla" })
	public void onRemoveItem() {
		if (getPosibleTabla() != null) {
			pendiente = false;
			listsTabla.remove(getPosibleTabla());
			objectsList.add(getPosibleTabla());
		} else {
			Alert.showErrorMessage("099", "Debe seleccionar una Tabla");
		}
	}

	@Command("onChooseAllItem")
	@NotifyChange({ "objectsList", "listsTabla" })
	public void onChooseAllItem() {
		if (!objectsList.isEmpty()) {
			pendiente = true;
			listTablaAux.removeAll(objectsList);
			listsTabla.addAll(objectsList);
			objectsList = new ArrayList<Tabla>();
		}

	}

	@Command("onRemoveAllItem")
	@NotifyChange({ "objectsList", "listsTabla" })
	public void onRemoveAllItem() {
		if (!listsTabla.isEmpty()) {
			pendiente = false;
			objectsList.addAll(listsTabla);
			listsTabla = new ArrayList<Tabla>();
		}

	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

	}

	@Command
	public void onUploadFile(
			@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {

		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null) {
			Media media = upEvent.getMedia();
			try {
				System.out.println(media.getName());
				Files.copy(new File(Zki.getPath() + media.getName()),
						media.getStreamData());
				String url = media.getName();
				// ImportExcel(media);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			System.out.println(media.getByteData());
			// fileuploaded = true;
			// nombre = media.getName();
		}
	}

	public void ImportExcel(Media media) throws Exception {

		/* We should now load excel objects and loop through the worksheet data */
		FileInputStream input_document = new FileInputStream(new java.io.File(
				"C:\\Smie\\" + media.getName()));
		Workbook my_xls_workbook = WorkbookFactory.create(input_document);
		Sheet my_worksheet = my_xls_workbook.getSheetAt(0);

		Iterator<Row> rowIterator = my_worksheet.iterator();

		while (rowIterator.hasNext()) {
			System.out.println("row");
			Row row = rowIterator.next();
			Cell cell = row.getCell(0);
			System.out.println(cell.getStringCellValue());
			// while (cellIterator.hasNext()) {
			// Cell cell = cellIterator.next();
			//
			// switch (cell.getCellType()) {
			// case Cell.CELL_TYPE_STRING: // handle string columns
			// cell.getStringCellValue();
			// break;
			// case Cell.CELL_TYPE_NUMERIC: // handle double data
			// cell.getNumericCellValue();
			// // li.setValue(cell.getNumericCellValue());
			// // li.appendChild(new
			// // Listcell(String.valueOf(cell.getNumericCellValue())));
			// break;
			// }

			// }
			// list.appendChild(li);
		}

	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getNombre(), "Nombre", 100);
			UtilValidate.validateString(getDescripcion(), "Descripcion", 255);
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
