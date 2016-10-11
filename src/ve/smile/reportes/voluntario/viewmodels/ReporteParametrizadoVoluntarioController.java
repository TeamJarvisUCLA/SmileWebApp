package ve.smile.reportes.voluntario.viewmodels;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import unexpo.modelo.Normacolombiana;
import unexpo.modelo.Normavenezolana;
import unexpo.modelo.Proyecto;
import unexpo.util.controlador.Configuracion;
import unexpo.util.controlador.ControladorCambioPagina;
import unexpo.util.controlador.Util;

public class ReporteParametrizadoVoluntarioController {
	private Map<String, Object> parametros =  new HashMap<>();
	private String type;
	private String source;
	
	@Init(superclass = true)
	public void childInit() {
		source = "reporte/unexpoColombiana.jasper";
		parametros.put("nombre",proyecto.getNombre());
		parametros.put("proyectista", proyecto.getProyectista());
		parametros.put("ciudad", proyecto.getCiudad());
		parametros.put("municipio", proyecto.getMunicipio());		
		parametros.put("direccion", proyecto.getDireccion());
		parametros.put("estado", proyecto.getEstado());
		parametros.put("edificacion", proyecto.getEdificacion());
		parametros.put("fecha", new SimpleDateFormat("dd-MM-yyyy").format(proyecto.getFecha()));		
		parametros.put("r1",  Util.darForma(normacolombiana.getR1()));
		parametros.put("r2",  Util.darForma(normacolombiana.getR2()));
		parametros.put("r3",  Util.darForma(normacolombiana.getR3()));
		parametros.put("r4",  Util.darForma(normacolombiana.getR4()));
		parametros.put("recomendacion", normacolombiana.getMensaje());
		parametros.put("nrr", Util.darForma(normacolombiana.getNrr()));
		parametros.put("recomendacionnrr", normacolombiana.getMensajenrr());
	

}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}