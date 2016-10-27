package ve.smile.basedatos.importar.viewmodels;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.exolab.castor.types.Date;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ImportarVoluntarioIndex extends
		VM_WindowSimpleListPrincipal<Voluntario> {

	private String archivo = "";

	private List<Persona> voluntarios = new ArrayList<Persona>();

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Command("aceptar")
	public void aceptar() {
		persistir();
		UtilDialog
				.showMessageBoxSuccess("Voluntarios guardados satisfactoriamente");
		voluntarios.clear();
		archivo = "";
		BindUtils.postNotifyChange(null, null, this, "*");
		refreshWindows();
	}

	@Command("cancelar")
	public void cancelar() {
		voluntarios.clear();
		archivo = "";
		BindUtils.postNotifyChange(null, null, this, "*");
		refreshWindows();
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
			archivo = media.getName();
			try {
				System.out.println(media.getName());
				Files.copy(new File("C:\\Smile\\" + media.getName()),
						media.getStreamData());
				ImportExcel(media);
				refreshWindows();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			BindUtils.postNotifyChange(null, null, this, "archivo");
		}
	}

	public void ImportExcel(Media media) throws Exception {
		FileInputStream input_document = new FileInputStream(new java.io.File(
				"C:\\Smile\\" + media.getName()));
		Workbook my_xls_workbook = WorkbookFactory.create(input_document);
		Sheet my_worksheet = my_xls_workbook.getSheetAt(0);
		Iterator<Row> rowIterator = my_worksheet.iterator();
		while (rowIterator.hasNext()) {
			Persona v = new Persona();
			Row row = rowIterator.next();
			Cell cell = row.getCell(0);
			v.setIdentificacion(cell.getStringCellValue());
			System.out.println(v.getNombre());

			cell = row.getCell(1);
			v.setNombre(cell.getStringCellValue());

			cell = row.getCell(2);
			v.setApellido(cell.getStringCellValue());

			cell = row.getCell(3);
			v.setCorreo(cell.getStringCellValue());

			cell = row.getCell(4);
			v.setDireccion(cell.getStringCellValue());
			cell = row.getCell(5);
			v.setTelefono1(cell.getStringCellValue());

			voluntarios.add(v);
		}
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadVoluntarioResponse payloadVoluntarioResponse = new PayloadVoluntarioResponse();

		return payloadVoluntarioResponse;
	}

	public List<Persona> getVoluntarios() {
		return voluntarios;
	}

	public void setVoluntarios(List<Persona> voluntarios) {
		this.voluntarios = voluntarios;
	}

	public void refreshWindows() {
		BindUtils.postNotifyChange(null, null, this, "voluntarios");
	}

	public void persistir() {
		for (Persona voluntario : voluntarios) {
			voluntario.setTipoPersona(TipoPersonaEnum.NATURAL.ordinal());
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.incluir(voluntario);
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
			}
			voluntario.setIdPersona(((Double) payloadPersonaResponse
					.getInformacion("id")).intValue());
			Voluntario vol = new Voluntario();
			vol.setFkPersona(voluntario);
			vol.setFechaIngreso(new Date().toLong());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.incluir(vol);
			if (!UtilPayload.isOK(payloadVoluntarioResponse)) {
				Alert.showMessage(payloadVoluntarioResponse);
			}
			return;
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
