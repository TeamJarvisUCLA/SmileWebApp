package ve.smile.reportes.ayuda.viewmodels;

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
import ve.smile.dto.Recurso;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteAyudaIndex extends VM_WindowWizard {


	private boolean recursoSolicitudAyuda = false;

	private boolean fortalezaSolicitudAyuda = false;

	private boolean redesSociales = false;

	private boolean fecha = false;



	private boolean aprobadas = false;

	private boolean enProceso = false;
	
	private boolean rechazadas = false;
	
	private boolean pendientes = false;

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

	private List<Recurso> listRecurso = new ArrayList<>();

	private Set<Recurso> recursosSeleccionadas;

	private List<SolicitudAyuda> solicitudes;


	String estatusSolicitudAyudas = "";

	String fortalezasP = "";

	String recursosP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";


	String tFortalezas = "";

	String tRecursos = "";


	String fechaDesde = "";

	String fechaHasta = "";


	@Init(superclass = true)
	public void childInit() {

		parametros.clear();

		listFortalezas = S.FortalezaService.consultarTodos().getObjetos();

		listRecurso = S.RecursoService.consultarTodos().getObjetos();


	}



	public boolean isRecursoSolicitudAyuda() {
		return recursoSolicitudAyuda;
	}

	public void setRecursoSolicitudAyuda(boolean recursoSolicitudAyuda) {
		this.recursoSolicitudAyuda = recursoSolicitudAyuda;
	}

	public boolean isFortalezaSolicitudAyuda() {
		return fortalezaSolicitudAyuda;
	}

	public void setFortalezaSolicitudAyuda(boolean fortalezaSolicitudAyuda) {
		this.fortalezaSolicitudAyuda = fortalezaSolicitudAyuda;
	}

	public boolean isRedesSociales() {
		return redesSociales;
	}

	public void setRedesSociales(boolean redesSociales) {
		this.redesSociales = redesSociales;
	}

	public boolean isFecha() {
		return fecha;
	}

	public void setFecha(boolean fecha) {
		this.fecha = fecha;
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

	public List<Recurso> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public Set<Recurso> getRecursosSeleccionadas() {
		return recursosSeleccionadas;
	}

	public void setRecursosSeleccionadas(
			Set<Recurso> recursosSeleccionadas) {
		this.recursosSeleccionadas = recursosSeleccionadas;
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

	

	public boolean isAprobadas() {
		return aprobadas;
	}

	public void setAprobadas(boolean aprobadas) {
		this.aprobadas = aprobadas;
	}

	public boolean isEnProceso() {
		return enProceso;
	}

	public void setEnProceso(boolean enProceso) {
		this.enProceso = enProceso;
	}
	
	public boolean isRechazadas() {
		return rechazadas;
	}

	public void setRechazadas(boolean rechazadas) {
		this.rechazadas = rechazadas;
	}
	
	public boolean isPendientes() {
		return pendientes;
	}

	public void setPendientes(boolean pendientes) {
		this.pendientes = pendientes;
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

		urls.add("views/desktop/reportes/ayudas/selectOpcionesReporteAyuda.zul");
		urls.add("views/desktop/reportes/ayudas/selectCompletado.zul");
		urls.add("views/desktop/reportes/ayudas/viewReportPdfAyuda.zul");

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
				sql = "SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ";

				if (fecha) {
					System.out.println("entro");
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
						sql += " and v.fecha >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fecha <= "
								+ fechaHastaDate.getTime() + " ";
						System.out.println(sql);
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ";

				if (fecha) {
					System.out.println("entro");
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
						System.out.println("no llego al final");
						sql += " and v.fecha >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fecha <= "
								+ fechaHastaDate.getTime() + " ";
						System.out.println(sql);
					}
				}

				if ( aprobadas || enProceso || rechazadas || pendientes) {
					String estatusSolicitudAyudas = "";

					

					if (aprobadas) {
						estatusSolicitudAyudas += EstatusSolicitudEnum.APROBADA
								.ordinal() + ",";
						estatusSolicitudAyudas += EstatusSolicitudEnum.APROBADA
								.ordinal() + " ";
					}

					if (enProceso) {
						estatusSolicitudAyudas += EstatusSolicitudEnum.EN_PROCESO
								.ordinal() + ",";
						estatusSolicitudAyudas += EstatusSolicitudEnum.EN_PROCESO
								.ordinal() + " ";
					}
					
					if (rechazadas) {
						estatusSolicitudAyudas += EstatusSolicitudEnum.RECHAZADA
								.ordinal() + ",";
						estatusSolicitudAyudas += EstatusSolicitudEnum.RECHAZADA
								.ordinal() + " ";
					}

					if (pendientes) {
						estatusSolicitudAyudas += EstatusSolicitudEnum.PENDIENTE
								.ordinal() + ",";
						estatusSolicitudAyudas += EstatusSolicitudEnum.PENDIENTE
								.ordinal() + " ";
					}

					int tamano = estatusSolicitudAyudas.length();

					char[] tmp = estatusSolicitudAyudas.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusSolicitudAyudas = new String(tmp);

					sql += "and v.estatusSolicitud in(" + estatusSolicitudAyudas
							+ ")";
				}
				if (recursoSolicitudAyuda) {

					if (recursosSeleccionadas != null) {
						String recursos = "";
						int i = 0;

						for (Recurso recurso : recursosSeleccionadas) {
							i++;
							recursos += recurso.getIdRecurso();
							recursosP += recurso.getNombre() + "," + " ";

							if (i != recursosSeleccionadas.size()) {
								recursos += ",";
							}

						}
						sql = sql.replace("WHERE",
								", SolicitudAyudaRecurso pv WHERE");
						sql += " and pv.fkSolicitudAyuda.idSolicitudAyuda = v.idSolicitudAyuda and pv.fkRecurso.idRecurso in ("
								+ recursos + ")";
					}
				}
			}
			if (fortalezaSolicitudAyuda) {

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
							.replace("WHERE", ", SolicitudAyudaFortaleza vf WHERE");
					sql += " and vf.fkSolicitudAyuda.idSolicitudAyuda = v.idSolicitudAyuda and vf.fkFortaleza.idFortaleza in ("
							+ fortalezas + ")";
				}

			}
			
			if (sql.equals("SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ")
					&& !todos) {
				
				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>SolicitudAyudas</b>";

			}

			System.out.println(sql);
			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.consultaSolicitudesParametrizado(sql);
			List<SolicitudAyuda> listSolicitudAyudas = payloadSolicitudAyudaResponse
					.getObjetos();
			System.out.println(listSolicitudAyudas);
			this.getSolicitudAyudas().addAll(listSolicitudAyudas);

			if (listSolicitudAyudas.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>SolicitudAyudas</b>";
			}
			for(SolicitudAyuda solicitud : listSolicitudAyudas){
				System.out.println(solicitud.getEstatusSolicitud());
			}

			jrDataSource = new JRBeanCollectionDataSource(listSolicitudAyudas);
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
			if (!estatusSolicitudAyudas.equals("")) {
				tStatus = "Estatus";
			}
			if (!fortalezasP.equals("")) {
				tFortalezas = "Fortalezas";
			}
			if (!recursosP.equals("")) {
				tRecursos = "Recursos";
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

			parametros.put("titulo", "Solicitud de Ayudas");

			parametros.put("tEstatus", tStatus);

			parametros.put("tFortalezas", tFortalezas);

			parametros.put("tRecursos", tRecursos);


			parametros.put("estatusSolicitudAyudas", estatusSolicitudAyudas);

			parametros.put("fortalezasP", fortalezasP);

			parametros.put("recursosP", recursosP);
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos().getObjetos().get(0);
			
			parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
			
			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());
			
			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());


			source = "reporte/reportAyudasParametrizados.jasper";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		System.out.println("algo paso por aqui pendiente");
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

	public List<SolicitudAyuda> getSolicitudAyudas() {
		if (this.solicitudes == null) {
			this.solicitudes = new ArrayList<>();
		}
		return solicitudes;
	}

	public void setSolicitudAyudas(List<SolicitudAyuda> solicitudes) {
		this.solicitudes = solicitudes;
	}

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}



	public String getEstatusSolicitudAyudas() {
		return estatusSolicitudAyudas;
	}

	public void setEstatusSolicitudAyudas(String estatusSolicitudAyudas) {
		this.estatusSolicitudAyudas = estatusSolicitudAyudas;
	}

	public String getFortalezasP() {
		return fortalezasP;
	}

	public void setFortalezasP(String fortalezasP) {
		this.fortalezasP = fortalezasP;
	}

	public String getRecursosP() {
		return recursosP;
	}

	public void setRecursosP(String recursosP) {
		this.recursosP = recursosP;
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

	public String gettRecursos() {
		return tRecursos;
	}

	public void settRecursos(String tRecursos) {
		this.tRecursos = tRecursos;
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
