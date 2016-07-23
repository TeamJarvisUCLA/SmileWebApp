package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;

import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMPdf extends VM_WindowForm implements UploadFileSingle {

	private byte[] bytes = null;
	
	private String nombreArchivo;
	
    private String nombre = "";
    private String cedula = "";
    private String edad = "";
    private String fechaNacimiento = "";
    private String direccion = "";
    private String telefono = "";
    private String celular = "";
    private String correo = "";
    private String formacionAcademica = "";
    private String experienciaLaboral = "";
    private String cursosRealizados = "";
    private String presentacionesRealizadas = "";
    private String informacionAdicional = "";
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		
		OperacionForm operacionForm = OperacionFormHelper.getPorType(OperacionFormEnum.CUSTOM1);
		operacionForm.setLabel("Procesar");
		operacionForm.setIconSclass("fa fa-tasks");
		
		operacionesForm.add(operacionForm);
		operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GENERAR));
		
		return operacionesForm;

	}

	@Override
	public boolean actionCustom1(OperacionEnum operacionEnum) {
		if (bytes == null) {
			UtilDialog.showMessageBoxWarning("Debe subir el archivo pdf");;
			return true;
		}
		try {
			FileUtils.writeByteArrayToFile(
					new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/" + nombreArchivo), bytes);
			
			String[] datos = new PrintTextLocations().procesar("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/" + nombreArchivo);
			
			if (datos == null) {
				return true;
			}
			
			nombre = datos[0].trim();
			cedula = datos[1].trim();
			edad = datos[2].trim();
			fechaNacimiento = datos[3].trim();
			direccion = datos[4].trim();
			telefono = datos[5].trim();
			celular = datos[6].trim();
			correo = datos[7].trim();
			formacionAcademica = datos[8].trim();
			experienciaLaboral = datos[9].trim();
			cursosRealizados = datos[10].trim();
			presentacionesRealizadas = datos[11].trim();
			informacionAdicional = datos[12].trim();
			formacionAcademica = formacionAcademica.replace("\n", "<br>");
			experienciaLaboral = experienciaLaboral.replace("\n", "<br>");
			cursosRealizados = cursosRealizados.replace("\n", "<br>");
			presentacionesRealizadas = presentacionesRealizadas.replace("\n", "<br>");
			informacionAdicional = informacionAdicional.replace("\n", "<br>");
			
			BindUtils.postGlobalCommand(null, null, "global", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	@GlobalCommand
	@NotifyChange({"nombre", "cedula", "edad", "fechaNacimiento", "direccion", "telefono", "celular", "correo", 
		"formacionAcademica", "experienciaLaboral", "cursosRealizados", "presentacionesRealizadas", "informacionAdicional"})
	public void global() {
		
	}

	@Override
	public void onNone() {
		
	}

	@Override
	public void onUploadFileSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();
		
        bytes = media.getByteData();
        nombreArchivo = media.getName();
	}
	
	@Override
	public void onRemoveFileSingle(String idUpload) {
		bytes = null;
		nombreArchivo = null;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getFormacionAcademica() {
		return formacionAcademica;
	}

	public void setFormacionAcademica(String formacionAcademica) {
		this.formacionAcademica = formacionAcademica;
	}

	public String getExperienciaLaboral() {
		return experienciaLaboral;
	}

	public void setExperienciaLaboral(String experienciaLaboral) {
		this.experienciaLaboral = experienciaLaboral;
	}

	public String getCursosRealizados() {
		return cursosRealizados;
	}

	public void setCursosRealizados(String cursosRealizados) {
		this.cursosRealizados = cursosRealizados;
	}

	public String getPresentacionesRealizadas() {
		return presentacionesRealizadas;
	}

	public void setPresentacionesRealizadas(String presentacionesRealizadas) {
		this.presentacionesRealizadas = presentacionesRealizadas;
	}

	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	public void setInformacionAdicional(String informacionAdicional) {
		this.informacionAdicional = informacionAdicional;
	}

	@Override
	public boolean actionGenerar(OperacionEnum operacionEnum) {
//		Filedownload.save(arregloByte,
//				EXPORTBUNDLE.getString(tipoReporte.ordinal()+".mime"), nombreArchivo);
//		
		try {
			Zippear("/home/tranx6/25173-2.jpg", "/home/tranx6/rifJean.pdf", "/home/tranx6/x.zip");
			
			Filedownload.save(new File("/home/tranx6/x.zip"), null);
			
//			Filedownload.save(new File("/home/tranx6/report1.pdf"), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
    public void Zippear(String pFile1, String pFile2, String pZipFile) throws Exception {
    	Integer BUFFER_SIZE = 2048;
		// objetos en memoria
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zipos = null;
	
		// buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			// fichero a comprimir
			fis = new FileInputStream(pFile1);
			// fichero contenedor del zip
			fos = new FileOutputStream(pZipFile);
			// fichero comprimido
			zipos = new ZipOutputStream(fos);
			ZipEntry zipEntry1 = new ZipEntry(pFile1.substring(pFile1.lastIndexOf("/") + 1));
			zipos.putNextEntry(zipEntry1);
			int len = 0;
			// zippear
			while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1)
				zipos.write(buffer, 0, len);
			// volcar la memoria al disco
			
			fis.close();
			
			
			// fichero a comprimir
			fis = new FileInputStream(pFile2);
			// fichero comprimido
			ZipEntry zipEntry = new ZipEntry(pFile2.substring(pFile2.lastIndexOf("/") + 1));
			zipos.putNextEntry(zipEntry);
			len = 0;
			// zippear
			while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1)
				zipos.write(buffer, 0, len);
			// volcar la memoria al disco
			zipos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			// cerramos los files
			zipos.close();
			fis.close();
			fos.close();
		} // end try
	} // end Zippear
	
//	public void UnZip(String pZipFile, String pFile) throws Exception {
//		BufferedOutputStream bos = null;
//		FileInputStream fis = null;
//		ZipInputStream zipis = null;
//		FileOutputStream fos = null;
//	
//		try {
//			fis = new FileInputStream(pZipFile);
//			zipis = new ZipInputStream(new BufferedInputStream(fis));
//			if (zipis.getNextEntry() != null) {
//				int len = 0;
//				byte[] buffer = new byte[BUFFER_SIZE];
//				fos = new FileOutputStream(pFile);
//				bos = new BufferedOutputStream(fos, BUFFER_SIZE);
//	
//				while  ((len = zipis.read(buffer, 0, BUFFER_SIZE)) != -1)
//					bos.write(buffer, 0, len);
//				bos.flush();
//			} else {
//				throw new Exception("El zip no contenia fichero alguno");
//			} // end if
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			bos.close();
//			zipis.close();
//			fos.close();
//			fis.close();
//		} // end try
//	} // end UnZip
}
