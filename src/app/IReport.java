package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class IReport {

	public static void main(String[] args) throws Exception {
		List<Dato> datos = new ArrayList<Dato>();
		
		datos.add(new Dato("Juan", "Perez"));
		datos.add(new Dato("Pedro", "Perez"));
		datos.add(new Dato("Juan", "Salas"));
		datos.add(new Dato("Pedro", "Salas"));
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("autor", "Yo");
		parametros.put("fecha", "18:21");
		
		URL urlArchivoReporte = R.class.getResource("report1.jasper");
		System.out.println(urlArchivoReporte);
		
		JasperReport reporte = (JasperReport) JRLoader.loadObject(urlArchivoReporte);
	    JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(datos);
	    
	    JasperPrint jasper = JasperFillManager.fillReport(reporte, parametros, datasource);
		
		JasperExportManager.exportReportToPdfFile(jasper, "/home/tranx6/report1.pdf");
	}
}
