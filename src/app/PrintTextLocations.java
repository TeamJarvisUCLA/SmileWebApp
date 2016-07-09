package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class PrintTextLocations extends PDFTextStripper {

	private Map<Float, DataProcessed> map;

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
        
        map = new HashMap<Float, DataProcessed>();
    }

    public String[] procesar(String url) throws Exception {
    	
    	String[] datos = new String[13];
    	
        PDDocument document = null;
        try {
            File input = new File(url);
            document = PDDocument.load(input);
            if (document.isEncrypted()) {
                try {
                    document.decrypt("");
                } catch (Exception e) {
                	throw new Exception("Document is encrypted with a password");
                }
            }
            
            PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(0);

            PDStream contents = page.getContents();

            if (contents != null) {
                this.processStream(page, page.findResources(), page.getContents().getStream());
                
                List<DataProcessed> list = new ArrayList<DataProcessed>(map.values());
                
                Collections.sort(list);
                
//                for (DataProcessed dataProcessed : list) {
//                	System.out.println(dataProcessed);
//				}
                
                datos[0] = buscarNombre(list);
                datos[1] = buscarCedula(list);
                datos[2] = buscarEdad(list);
                datos[3] = buscarFechaDeNacimiento(list);
                datos[4] = buscarDireccion(list);
                datos[5] = buscarTelefono(list);
                datos[6] = buscarCelular(list);
                datos[7] = buscarCorreo(list);
                datos[8] = buscarFormacionAcademica(list);
                datos[9] = buscarExperienciaLaboral(list);
                datos[10] = buscarCursosRealizados(list);
                datos[11] = buscarPresentaciones(list);
                datos[12] = buscarInformacionAdicional(list);

                return datos;
            }
        } catch (Exception e) {
			throw e;
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return datos;
    }

    private String buscarNombre(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Nombre") &&
					dataProcessed.getLine().contains("Completo:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado el nombre completo");
    	}
    	
    	String nombre = list.get(k + 1).getLine().trim().toUpperCase();
    	
    	for (int i = k + 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return nombre;
	}
    
    private String buscarCedula(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Cédula:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado la cédula");
    	}
    	
    	String cedula = list.get(k).getLine().replace("Cédula:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return cedula;
	}
    
    private String buscarEdad(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Edad:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado la edad");
    	}
    	
    	String edad = list.get(k).getLine().replace("Edad:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return edad;
	}
    
    private String buscarFechaDeNacimiento(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Fecha de Nacimiento:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado la fecha de nacimiento");
    	}
    	
    	String fecha = list.get(k).getLine().replace("Fecha de Nacimiento:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return fecha;
	}

    private String buscarDireccion(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (k == -1 && dataProcessed.getLine().contains("Dirección:")) {
				k = i;
				continue;
			}
			
			if (dataProcessed.getLine().contains("Teléfono:")) {
				l = i;
				break;
			}
		}
    	
    	if (k == -1 || l == -1 || k > l) {
    		throw new Exception("No se ha encontrado la dirección");
    	}
    	
    	String direccion = "";
    	
    	for(int i = k; i < l; i++) {
    		if (i == k) {
    			direccion += list.get(i).getLine().replace("Dirección:", "").trim().toUpperCase();
    		} else {
    			direccion += " " + list.get(i).getLine().trim().toUpperCase();
    		}
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return direccion;
	}
    
    private String buscarTelefono(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Teléfono:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado el teléfono");
    	}
    	
    	String telefono = list.get(k).getLine().replace("Teléfono:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return telefono;
	}
    
    private String buscarCelular(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Celular:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado el celular");
    	}
    	
    	String celular = list.get(k).getLine().replace("Celular:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return celular;
	}
    
    private String buscarCorreo(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Correo:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado el correo");
    	}
    	
    	String celular = list.get(k).getLine().replace("Correo:", "").trim().toUpperCase();
    	
    	for (int i = k; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return celular;
	}
    
    private String buscarFormacionAcademica(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (k == -1 && dataProcessed.getLine().contains("Formación Académica:")) {
				k = i;
				continue;
			}
			
			if (dataProcessed.getLine().contains("Experiencia Laboral:")) {
				l = i;
				break;
			}
		}
    	
    	if (k == -1 || l == -1 || k > l) {
    		throw new Exception("No se ha encontrado la formación académica");
    	}
    	
    	String formacion = "";
    	
    	for(int i = k + 1; i < l; i++) {
    		if (formacion.length() > 0) {
    			formacion += "\n";
    		}
    		
    		formacion += list.get(i).getLine().trim().toUpperCase();
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return formacion;
	}
    
    private String buscarExperienciaLaboral(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (k == -1 && dataProcessed.getLine().contains("Experiencia Laboral:")) {
				k = i;
				continue;
			}
			
			if (dataProcessed.getLine().contains("Cursos Realizados:")) {
				l = i;
				break;
			}
		}
    	
    	if (k == -1 || l == -1 || k > l) {
    		throw new Exception("No se ha encontrado la experiencia laboral");
    	}
    	
    	String experiencia = "";
    	
    	for(int i = k + 1; i < l; i++) {
    		if (experiencia.length() > 0) {
    			experiencia += "\n";
    		}
    		
    		experiencia += list.get(i).getLine().trim().toUpperCase();
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return experiencia;
	}
    
    private String buscarCursosRealizados(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (k == -1 && dataProcessed.getLine().contains("Cursos Realizados:")) {
				k = i;
				continue;
			}
			
			if (dataProcessed.getLine().contains("Presentaciones en conferencias:")) {
				l = i;
				break;
			}
		}
    	
    	if (k == -1 || l == -1 || k > l) {
    		throw new Exception("No se ha encontrado los cursos realizados");
    	}
    	
    	String cursos = "";
    	
    	for(int i = k + 1; i < l; i++) {
    		if (cursos.length() > 0) {
    			cursos += "\n";
    		}
    		
    		cursos += list.get(i).getLine().trim().toUpperCase();
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return cursos;
	}
    
    private String buscarPresentaciones(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = -1;
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (k == -1 && dataProcessed.getLine().contains("Presentaciones en conferencias:")) {
				k = i;
				continue;
			}
			
			if (dataProcessed.getLine().contains("Información Adicional:")) {
				l = i;
				break;
			}
		}
    	
    	if (k == -1 || l == -1 || k > l) {
    		throw new Exception("No se ha encontrado las presentaciones en conferencias");
    	}
    	
    	String presentaciones = "";
    	
    	for(int i = k + 1; i < l; i++) {
    		if (presentaciones.length() > 0) {
    			presentaciones += "\n";
    		}
    		
    		presentaciones += list.get(i).getLine().trim().toUpperCase();
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return presentaciones;
	}
    
    private String buscarInformacionAdicional(List<DataProcessed> list) throws Exception {
    	int k = -1;
    	int l = list.size();
    	
    	for(int i = 0; i < list.size(); i++) {
    		DataProcessed dataProcessed = list.get(i);
    		
			if (dataProcessed.getLine().contains("Información Adicional:")) {
				k = i;
				break;
			}
		}
    	
    	if (k == -1) {
    		throw new Exception("No se ha encontrado la informaciion adicional");
    	}
    	
    	String informacion = "";
    	
    	for(int i = k + 1; i < l; i++) {
    		if (informacion.length() > 0) {
    			informacion += "\n";
    		}
    		
    		informacion += list.get(i).getLine().trim().toUpperCase();
    	}
    	
    	for (int i = l - 1; i >= 0; i--) {
    		list.remove(i);
    	}
    	
    	return informacion;
	}
    
	@Override
    protected void processTextPosition(TextPosition text) {
//        String tChar = text.getCharacter();
//        System.out.println("String[" + text.getXDirAdj() + ","
//                + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
//                + text.getXScale() + " height=" + text.getHeightDir() + " space="
//                + text.getWidthOfSpace() + " width="
//                + text.getWidthDirAdj() + "]" + text.getCharacter());
        
        Float yDirAdj = text.getYDirAdj();
        String character = text.getCharacter();
        
        DataProcessed dataProcessed = map.get(yDirAdj);
        
        if (dataProcessed == null) {
        	dataProcessed = new DataProcessed(yDirAdj, character);
        } else {
        	dataProcessed.addCharacterToLine(character);
        }
        
        map.put(yDirAdj, dataProcessed);
    }
}