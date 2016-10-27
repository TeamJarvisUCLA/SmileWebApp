package ve.smile.reportes.trabajador.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.smile.util.UtilConverterDataList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Profesion;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteTrabajadorIndex extends VM_WindowWizard {


	private boolean profesionTrabajador = false;

	private boolean fortalezaTrabajador = false;

	private boolean redesSociales = false;

	private boolean fechaIngreso = false;



	private boolean activo = false;

	private boolean egresado = false;

	private boolean todos = false;

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();



	private List<Fortaleza> listFortalezas = new ArrayList<>();

	private List<String> listRedesSociales = new ArrayList<>();

	private Set<String> redesSeleccionadas;

	private Set<Fortaleza> fortalezasSeleccionados;

	private List<Profesion> listProfesion = new ArrayList<>();

	private Set<Profesion> profesionesSeleccionadas;

	private List<Trabajador> trabajadores;


	String estatusTrabajadores = "";

	String fortalezasP = "";

	String profesionesP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";


	String tFortalezas = "";

	String tProfesiones = "";


	String fechaDesde = "";

	String fechaHasta = "";


	@Init(superclass = true)
	public void childInit() {

		parametros.clear();

		listFortalezas = S.FortalezaService.consultarTodos().getObjetos();

		listProfesion = S.ProfesionService.consultarTodos().getObjetos();


	}



	public boolean isProfesionTrabajador() {
		return profesionTrabajador;
	}

	public void setProfesionTrabajador(boolean profesionTrabajador) {
		this.profesionTrabajador = profesionTrabajador;
	}

	public boolean isFortalezaTrabajador() {
		return fortalezaTrabajador;
	}

	public void setFortalezaTrabajador(boolean fortalezaTrabajador) {
		this.fortalezaTrabajador = fortalezaTrabajador;
	}

	public boolean isRedesSociales() {
		return redesSociales;
	}

	public void setRedesSociales(boolean redesSociales) {
		this.redesSociales = redesSociales;
	}

	public boolean isFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(boolean fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	

	public List<Fortaleza> getListFortalezas() {
		return listFortalezas;
	}

	public void setListFortalezas(List<Fortaleza> listFortalezas) {
		this.listFortalezas = listFortalezas;
	}

	public Set<Fortaleza> getFortalezasSeleccionados() {
		return fortalezasSeleccionados;
	}

	public void setFortalezasSeleccionados(
			Set<Fortaleza> fortalezasSeleccionados) {
		this.fortalezasSeleccionados = fortalezasSeleccionados;
	}

	public List<Profesion> getListProfesion() {
		return listProfesion;
	}

	public void setListProfesion(List<Profesion> listProfesion) {
		this.listProfesion = listProfesion;
	}

	public Set<Profesion> getProfesionesSeleccionadas() {
		return profesionesSeleccionadas;
	}

	public void setProfesionesSeleccionadas(
			Set<Profesion> profesionesSeleccionadas) {
		this.profesionesSeleccionadas = profesionesSeleccionadas;
	}

	public List<String> getListRedesSociales() {
		return listRedesSociales;
	}

	public void setListRedesSociales(List<String> listRedesSociales) {
		this.listRedesSociales = listRedesSociales;
	}

	public Set<String> getRedesSeleccionadas() {
		return redesSeleccionadas;
	}

	public void setRedesSeleccionadas(Set<String> redesSeleccionadas) {
		this.redesSeleccionadas = redesSeleccionadas;
	}

	

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isEgresado() {
		return egresado;
	}

	public void setEgresado(boolean egresado) {
		this.egresado = egresado;
	}

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();

		OperacionWizard operacionWizard = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "GENERAR", "Custom1",
				"", "indigo", "Generar");

		listOperacionWizard1.add(operacionWizard);

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();

		OperacionWizard operacionWizard2 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "REPORTE", "Custom1",
				"", "amber", "Reporte");

		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));

		OperacionWizard operacionWizard3 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM2.ordinal(), "EXPORTAR", "Custom2",
				"", "teal accent-4", "Exportar");

		listOperacionWizard2.add(operacionWizard2);

		listOperacionWizard2.add(operacionWizard3);

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-list");
		iconos.add("fa fa-file-text");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/reportes/trabajador/selectOpcionesReporteTrabajador.zul");
		urls.add("views/desktop/reportes/trabajador/selectCompletado.zul");
		urls.add("views/desktop/reportes/trabajador/viewReportPdfTrabajador.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {

		}

		if (currentStep == 2) {

		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			parametros.clear();
			restartWizard();
			parametros.clear();
		}

		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		
			goToNextStep();
		
		return "";
	}

	@Override
	public String executeCustom2(Integer currentStep) {
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1(Integer currentStep) {

		if (currentStep == 1) {

			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM Trabajador v  WHERE  v.idTrabajador = v.idTrabajador ";

				if (fechaIngreso) {
				
					if (fechaDesdeDate == null && fechaHastaDate == null) {

						return "E:Error Code 5-No se han ingresado parametros de fechas ";

					} else if (fechaDesdeDate == null) {

						return "E:Error Code 5-No se ha ingresado una <b>Fecha Desde</b> como Parametro ";

					} else if (fechaHastaDate == null) {

						return "E:Error Code 5-No se ha ingresado ninguna <b>Fecha Hasta</b> como Parametro ";

					} else if (fechaDesdeDate.getTime() >= fechaHastaDate
							.getTime()) {

						return "E:Error Code 5-No se puede ingresar una <b>Fecha Desde</b>  mayor a la <b>Fecha Hasta</b> ";

					} else {
						sql += " and v.fechaIngreso >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaIngreso <= "
								+ fechaHastaDate.getTime() + " ";
			
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM Trabajador v  WHERE  v.idTrabajador = v.idTrabajador ";

				if (fechaIngreso) {
		
					if (fechaDesdeDate == null && fechaHastaDate == null) {

						return "E:Error Code 5-No se han ingresado parametros de fechas ";

					} else if (fechaDesdeDate == null) {

						return "E:Error Code 5-No se ha ingresado una <b>Fecha Desde</b> como Parametro ";

					} else if (fechaHastaDate == null) {

						return "E:Error Code 5-No se ha ingresado ninguna <b>Fecha Hasta</b> como Parametro ";

					} else if (fechaDesdeDate.getTime() >= fechaHastaDate
							.getTime()) {

						return "E:Error Code 5-No se puede ingresar una <b>Fecha Desde</b>  mayor a la <b>Fecha Hasta</b> ";

					} else {
					
						sql += " and v.fechaIngreso >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaIngreso <= "
								+ fechaHastaDate.getTime() + " ";
						
					}
				}

				if ( egresado || activo) {
					String estatusTrabajadores = "";

					

					if (egresado) {
						estatusTrabajadores += EstatusTrabajadorEnum.INACTIVO
								.ordinal() + ",";
						estatusTrabajadores += EstatusTrabajadorEnum.INACTIVO
								.ordinal() + " ";
					}

					if (activo) {
						estatusTrabajadores += EstatusTrabajadorEnum.ACTIVO
								.ordinal() + ",";
						estatusTrabajadores += EstatusTrabajadorEnum.ACTIVO
								.ordinal() + " ";
					}

					int tamano = estatusTrabajadores.length();

					char[] tmp = estatusTrabajadores.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusTrabajadores = new String(tmp);

					sql += "and v.estatusTrabajador in(" + estatusTrabajadores
							+ ")";
				}
				if (profesionTrabajador) {

					if (profesionesSeleccionadas != null) {
						String profesiones = "";
						int i = 0;

						for (Profesion profesion : profesionesSeleccionadas) {
							i++;
							profesiones += profesion.getIdProfesion();
							profesionesP += profesion.getNombre() + "," + " ";

							if (i != profesionesSeleccionadas.size()) {
								profesiones += ",";
							}

						}
						sql = sql.replace("WHERE",
								", TrabajadorProfesion pv WHERE");
						sql += " and pv.fkTrabajador.idTrabajador = v.idTrabajador and pv.fkProfesion.idProfesion in ("
								+ profesiones + ")";
					}
				}
			}
			if (fortalezaTrabajador) {

				if (fortalezasSeleccionados != null) {
					String fortalezas = "";
					int i = 0;

					for (Fortaleza fortaleza : fortalezasSeleccionados) {
						i++;
						fortalezas += fortaleza.getIdFortaleza();
						fortalezasP += fortaleza.getNombre() + "," + " ";

						if (i != fortalezasSeleccionados.size()) {
							fortalezas += ",";
						}
					}
					sql = sql
							.replace("WHERE", ", TrabajadorFortaleza vf WHERE");
					sql += " and vf.fkTrabajador.idTrabajador = v.idTrabajador and vf.fkFortaleza.idFortaleza in ("
							+ fortalezas + ")";
				}

			}
			
			if (sql.equals("SELECT DISTINCT v FROM Trabajador v  WHERE  v.idTrabajador = v.idTrabajador ")
					&& !todos) {
				
				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Trabajadores</b>";

			}

		
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultaTrabajadoresParametrizado(sql);
			List<Trabajador> listTrabajadores = payloadTrabajadorResponse
					.getObjetos();
			this.getTrabajadores().addAll(listTrabajadores);

			if (listTrabajadores.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>Trabajadores</b>";
			}
	

			jrDataSource = new JRBeanCollectionDataSource(listTrabajadores);
		}
		if (currentStep == 2) {
			String direccion = Reporte.class
					.getResource("Reporte.jasper")
					.getPath()
					.replace(
							"WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
							"imagenes/logo_fanca.jpg");
			direccion = direccion.replaceFirst("/", "");
			direccion = direccion.replace("/", "\\");
			parametros.put("timagen1", direccion);

			direccion = Reporte.class
					.getResource("Reporte.jasper")
					.getPath()
					.replace(
							"WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
							"imagenes/smiles_webdesktop.png");
			direccion = direccion.replaceFirst("/", "");
			direccion = direccion.replace("/", "\\");
			parametros.put("timagen2", direccion);

			
			type = "pdf";
			
			if (!estatusTrabajadores.equals("")) {
				tStatus = "Estatus";
			}
			if (!fortalezasP.equals("")) {
				tFortalezas = "Fortalezas";
			}
			if (!profesionesP.equals("")) {
				tProfesiones = "Profesiones";
			}
			
			fechaDesde = fechaDesdeDate == null ? "" : UtilConverterDataList
					.convertirLongADate(fechaDesdeDate.getTime());

			fechaHasta = fechaHastaDate == null ? "" : UtilConverterDataList
					.convertirLongADate(fechaHastaDate.getTime());

			tFechaDesde = fechaDesde.equals("") ? "" : "Fecha Desde";

			tFechaHasta = fechaHasta.equals("") ? "" : "Fecha Hasta";

			parametros.put("tFechaDesde", tFechaDesde);

			parametros.put("tfechaHasta", tFechaHasta);

			parametros.put("fechaDesde", fechaDesde);

			parametros.put("fechaHasta", fechaHasta);

			parametros.put("titulo", "TRABAJADORES");

			parametros.put("tEstatus", tStatus);

			parametros.put("tFortalezas", tFortalezas);

			parametros.put("tProfesiones", tProfesiones);


			parametros.put("estatusTrabajadores", estatusTrabajadores);

			parametros.put("fortalezasP", fortalezasP);

			parametros.put("profesionesP", profesionesP);
			
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos().getObjetos().get(0);
			
			parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
			
			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());
			
			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());


			source = "reporte/reportTrabajadoresParametrizados.jasper";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {

		return "";
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

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public List<Trabajador> getTrabajadores() {
		if (this.trabajadores == null) {
			this.trabajadores = new ArrayList<>();
		}
		return trabajadores;
	}

	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}



	public String getEstatusTrabajadores() {
		return estatusTrabajadores;
	}

	public void setEstatusTrabajadores(String estatusTrabajadores) {
		this.estatusTrabajadores = estatusTrabajadores;
	}

	public String getFortalezasP() {
		return fortalezasP;
	}

	public void setFortalezasP(String fortalezasP) {
		this.fortalezasP = fortalezasP;
	}

	public String getProfesionesP() {
		return profesionesP;
	}

	public void setProfesionesP(String profesionesP) {
		this.profesionesP = profesionesP;
	}


	
	public String gettStatus() {
		return tStatus;
	}

	public void settStatus(String tStatus) {
		this.tStatus = tStatus;
	}

	public String gettFechaDesde() {
		return tFechaDesde;
	}

	public void settFechaDesde(String tFechaDesde) {
		this.tFechaDesde = tFechaDesde;
	}

	public String gettFechaHasta() {
		return tFechaHasta;
	}

	public void settFechaHasta(String tFechaHasta) {
		this.tFechaHasta = tFechaHasta;
	}



	public String gettFortalezas() {
		return tFortalezas;
	}

	public void settFortalezas(String tFortalezas) {
		this.tFortalezas = tFortalezas;
	}

	public String gettProfesiones() {
		return tProfesiones;
	}

	public void settProfesiones(String tProfesiones) {
		this.tProfesiones = tProfesiones;
	}


	public Date getFechaDesdeDate() {
		return fechaDesdeDate;
	}

	public void setFechaDesdeDate(Date fechaDesdeDate) {
		this.fechaDesdeDate = fechaDesdeDate;
	}

	public Date getFechaHastaDate() {
		return fechaHastaDate;
	}

	public void setFechaHastaDate(Date fechaHastaDate) {
		this.fechaHastaDate = fechaHastaDate;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

}
