package ve.smile.reportes.donativo.viewmodels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.zkoss.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.DonativoRecurso;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Recurso;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.RecepcionEnum;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteDonativoIndex extends VM_WindowWizard {

	private boolean tipoProcedenciaEnum = false;

	private boolean tipoRecepcionEnum = false;

	private boolean recursoDonativoRecurso = false;

	private boolean fortalezaDonativoRecurso = false;

	private boolean redesSociales = false;

	private boolean fecha = false;

	private boolean aporte = false;

	private boolean aprobadas = false;

	private boolean enProceso = false;

	private boolean rechazadas = false;

	private boolean pendientes = false;

	private boolean procesada = false;

	private boolean todos = false;

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private List<Fortaleza> listProcedencias = new ArrayList<>();

	private List<String> listRedesSociales = new ArrayList<>();

	private Set<String> redesSeleccionadas;

	private Set<Fortaleza> fortalezasSeleccionados;

	private List<Recurso> listRecurso = new ArrayList<>();

	private Set<Recurso> recursosSeleccionadas;

	private List<DonativoRecurso> donativos;

	private Set<ProcedenciaEnum> tipoProcedenciaEnumsSeleccionados;

	private Set<RecepcionEnum> tipoRecepcionEnumsSeleccionados;

	private List<ProcedenciaEnum> listTipoProcedenciaEnums = new ArrayList<>();

	private List<RecepcionEnum> listTipoRecepcionEnums = new ArrayList<>();

	String tipoProcedenciaEnumP = "";

	String tTipoProcedenciaEnumP = "";

	String tipoRecepcionEnumP = "";

	String tTipoRecepcionEnumP = "";

	String estatusDonativoRecursos = "";

	String fortalezasP = "";

	String recursosP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";

	String tProcedencias = "";

	String tRecepciones = "";

	String fechaDesde = "";

	String fechaHasta = "";

	@Init(superclass = true)
	public void childInit() {

		parametros.clear();

		listProcedencias = S.FortalezaService.consultarTodos().getObjetos();

		listRecurso = S.RecursoService.consultarTodos().getObjetos();

		tipoProcedenciaEnumsSeleccionados = new HashSet<>();

	}

	public List<ProcedenciaEnum> getListTipoProcedenciaEnums() {
		if (this.listTipoProcedenciaEnums == null) {
			this.listTipoProcedenciaEnums = new ArrayList<ProcedenciaEnum>();
		}
		if (this.listTipoProcedenciaEnums.isEmpty()) {
			for (ProcedenciaEnum procedencia : ProcedenciaEnum.values()) {
				this.listTipoProcedenciaEnums.add(procedencia);
			}
		}
		return listTipoProcedenciaEnums;
	}

	public void setListTipoProcedenciaEnums(
			List<ProcedenciaEnum> listTipoProcedenciaEnums) {
		this.listTipoProcedenciaEnums = listTipoProcedenciaEnums;
	}

	public List<RecepcionEnum> getListTipoRecepcionEnums() {
		if (this.listTipoRecepcionEnums == null) {
			this.listTipoRecepcionEnums = new ArrayList<RecepcionEnum>();
		}
		if (this.listTipoRecepcionEnums.isEmpty()) {
			for (RecepcionEnum recepcion : RecepcionEnum.values()) {
				this.listTipoRecepcionEnums.add(recepcion);
			}
		}
		return listTipoRecepcionEnums;
	}

	public void setListTipoRecepcionEnums(
			List<ProcedenciaEnum> listTipoProcedenciaEnums) {
		this.listTipoProcedenciaEnums = listTipoProcedenciaEnums;
	}

	public boolean isAporte() {
		return aporte;
	}

	public void setAporte(boolean aporte) {
		this.aporte = aporte;
	}

	public boolean isTipoRecepcionEnum() {
		return tipoRecepcionEnum;
	}

	public void setTipoRecepcionEnum(boolean tipoRecepcionEnum) {
		this.tipoRecepcionEnum = tipoRecepcionEnum;
	}

	public Set<RecepcionEnum> getTipoRecepcionEnumsSeleccionados() {
		return tipoRecepcionEnumsSeleccionados;
	}

	public void setTipoRecepcionEnumsSeleccionados(
			Set<RecepcionEnum> tipoRecepcionEnumsSeleccionados) {
		this.tipoRecepcionEnumsSeleccionados = tipoRecepcionEnumsSeleccionados;
	}

	public String getTipoRecepcionEnumP() {
		return tipoRecepcionEnumP;
	}

	public void setTipoRecepcionEnumP(String tipoRecepcionEnumP) {
		this.tipoRecepcionEnumP = tipoRecepcionEnumP;
	}

	public String gettTipoRecepcionEnumP() {
		return tTipoRecepcionEnumP;
	}

	public void settTipoRecepcionEnumP(String tTipoRecepcionEnumP) {
		this.tTipoRecepcionEnumP = tTipoRecepcionEnumP;
	}

	public boolean isTipoProcedenciaEnum() {
		return tipoProcedenciaEnum;
	}

	public void setTipoProcedenciaEnum(boolean tipoProcedenciaEnum) {
		this.tipoProcedenciaEnum = tipoProcedenciaEnum;
	}

	public Set<ProcedenciaEnum> getTipoProcedenciaEnumsSeleccionados() {
		return tipoProcedenciaEnumsSeleccionados;
	}

	public void setTipoProcedenciaEnumsSeleccionados(
			Set<ProcedenciaEnum> tipoProcedenciaEnumsSeleccionados) {
		this.tipoProcedenciaEnumsSeleccionados = tipoProcedenciaEnumsSeleccionados;
	}

	public String getTipoProcedenciaEnumP() {
		return tipoProcedenciaEnumP;
	}

	public void setTipoProcedenciaEnumP(String tipoProcedenciaEnumP) {
		this.tipoProcedenciaEnumP = tipoProcedenciaEnumP;
	}

	public String gettTipoProcedenciaEnumP() {
		return tTipoProcedenciaEnumP;
	}

	public void settTipoProcedenciaEnumP(String tTipoProcedenciaEnumP) {
		this.tTipoProcedenciaEnumP = tTipoProcedenciaEnumP;
	}

	public boolean isRecursoDonativoRecurso() {
		return recursoDonativoRecurso;
	}

	public void setRecursoDonativoRecurso(boolean recursoDonativoRecurso) {
		this.recursoDonativoRecurso = recursoDonativoRecurso;
	}

	public boolean isFortalezaDonativoRecurso() {
		return fortalezaDonativoRecurso;
	}

	public void setFortalezaDonativoRecurso(boolean fortalezaDonativoRecurso) {
		this.fortalezaDonativoRecurso = fortalezaDonativoRecurso;
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

	public List<Fortaleza> getListProcedencias() {
		return listProcedencias;
	}

	public void setListProcedencias(List<Fortaleza> listProcedencias) {
		this.listProcedencias = listProcedencias;
	}

	public Set<Fortaleza> getProcedenciasSeleccionados() {
		return fortalezasSeleccionados;
	}

	public void setProcedenciasSeleccionados(
			Set<Fortaleza> fortalezasSeleccionados) {
		this.fortalezasSeleccionados = fortalezasSeleccionados;
	}

	public List<Recurso> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public Set<Recurso> getRecepcionesSeleccionadas() {
		return recursosSeleccionadas;
	}

	public void setRecepcionesSeleccionadas(Set<Recurso> recursosSeleccionadas) {
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

	public boolean isProcesada() {
		return procesada;
	}

	public void setProcesada(boolean procesada) {
		this.procesada = procesada;
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

		urls.add("views/desktop/reportes/donativo/selectOpcionesReporteDonativo.zul");
		urls.add("views/desktop/reportes/donativo/selectCompletado.zul");
		urls.add("views/desktop/reportes/donativo/viewReportPdfDonativo.zul");

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
		if (currentStep == 2) {
			File crear_archivo = new File("C:\\Smile\\donativos.csv");
			try {
				crear_archivo.createNewFile();
				FileWriter w = new FileWriter(crear_archivo);
				BufferedWriter bw = new BufferedWriter(w);
				PrintWriter wr = new PrintWriter(bw);

				for (DonativoRecurso obj : this.donativos) {

					wr.println(obj.getFkPersona().getIdentificacion()+";"+obj.getFkPersona().getNombre()
							+ ";" + obj.getFkPersona().getApellido() +";"
							+ obj.getFkRecurso().getNombre() + ";"
							+ obj.getFkRecurso().getDescripcion() + ";"
							+ obj.getCantidad());
				}
				wr.close();
				bw.close();
				Filedownload.save(crear_archivo, "application/file");			
			} catch (IOException e) {
				return "E:Error Code 5-No se pudo generar el archivo";
			}
		}
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1(Integer currentStep) {

		if (currentStep == 1) {

			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ";

				if (fecha) {
		
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
						sql += " and v.fechaDonativo >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaDonativo <= "
								+ fechaHastaDate.getTime() + " ";
				
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ";

				if (fecha) {
			
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
					
						sql += " and v.fechaDonativo >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaDonativo <= "
								+ fechaHastaDate.getTime() + " ";
				
					}
				}

				if (tipoProcedenciaEnum) {

					if (tipoProcedenciaEnumsSeleccionados != null) {
						String tipoProcedenciaEnumClasificado = "";
						int i = 0;

						for (ProcedenciaEnum tipoProcedenciaEnum : tipoProcedenciaEnumsSeleccionados) {
							i++;
							tipoProcedenciaEnumP += tipoProcedenciaEnum.name()
									+ "," + " ";
							tipoProcedenciaEnumClasificado += tipoProcedenciaEnum
									.ordinal();

							if (i != tipoProcedenciaEnumsSeleccionados.size()) {
								tipoProcedenciaEnumClasificado += ",";
							}
						}

						sql += " and v.procedencia in ("
								+ tipoProcedenciaEnumClasificado + ")";
					}
				}

				if (tipoRecepcionEnum) {

					if (tipoRecepcionEnumsSeleccionados != null) {
						String tipoProcedenciaEnumClasificado = "";
						int i = 0;

						for (RecepcionEnum tipoRecepcionEnum : tipoRecepcionEnumsSeleccionados) {
							i++;
							tipoRecepcionEnumP += tipoRecepcionEnum.name()
									+ "," + " ";
							tipoProcedenciaEnumClasificado += tipoRecepcionEnum
									.ordinal();

							if (i != tipoRecepcionEnumsSeleccionados.size()) {
								tipoProcedenciaEnumClasificado += ",";
							}
						}

						sql += " and v.recepcion in ("
								+ tipoProcedenciaEnumClasificado + ")";
					}
				}

				if (aporte) {

					sql += "and v.aporte =(" + aporte + ")";
				}

			}

			if (sql.equals("SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>DonativoRecursos</b>";

			}


			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
					.consultaDonativosParametrizado(sql);
			List<DonativoRecurso> listDonativoRecursos = payloadDonativoRecursoResponse
					.getObjetos();
	
			this.getDonativoRecursos().addAll(listDonativoRecursos);

			if (listDonativoRecursos.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>DonativoRecursos</b>";
			}
		

			jrDataSource = new JRBeanCollectionDataSource(listDonativoRecursos);

		}
		if (currentStep == 2) {
			type = "pdf";
			if (!estatusDonativoRecursos.equals("")) {
				tStatus = "Estatus";
			}
			if (!tipoProcedenciaEnumP.equals("")) {
				tProcedencias = "Procedencia";
			}
			if (!tipoRecepcionEnumP.equals("")) {
				tRecepciones = "Recepcion";
			}
			
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

			parametros.put("titulo", " DONATIVOS");

			parametros.put("tipoProcedenciaEnumP", tipoProcedenciaEnumP);

			parametros.put("tipoRecepcionEnumP", tipoRecepcionEnumP);

			parametros.put("tProcedencias", tProcedencias);

			parametros.put("tRecepciones", tRecepciones);
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos().getObjetos().get(0);
			
			parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
			
			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());

			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());
			
			source = "reporte/reportDonativosParametrizados.jasper";
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

	public List<DonativoRecurso> getDonativoRecursos() {
		if (this.donativos == null) {
			this.donativos = new ArrayList<>();
		}
		return donativos;
	}

	public void setDonativoRecursos(List<DonativoRecurso> donativos) {
		this.donativos = donativos;
	}

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	public String getEstatusDonativoRecursos() {
		return estatusDonativoRecursos;
	}

	public void setEstatusDonativoRecursos(String estatusDonativoRecursos) {
		this.estatusDonativoRecursos = estatusDonativoRecursos;
	}

	public String getProcedenciasP() {
		return fortalezasP;
	}

	public void setProcedenciasP(String fortalezasP) {
		this.fortalezasP = fortalezasP;
	}

	public String getRecepcionesP() {
		return recursosP;
	}

	public void setRecepcionesP(String recursosP) {
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

	public String gettProcedencias() {
		return tProcedencias;
	}

	public void settProcedencias(String tProcedencias) {
		this.tProcedencias = tProcedencias;
	}

	public String gettRecepciones() {
		return tRecepciones;
	}

	public void settRecepciones(String tRecepciones) {
		this.tRecepciones = tRecepciones;
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
