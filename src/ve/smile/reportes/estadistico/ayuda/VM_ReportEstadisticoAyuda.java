package ve.smile.reportes.estadistico.ayuda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import lights.smile.util.UtilConverterDataList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Recurso;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.TipoEventoEnum;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.reportes.Reporte;
import karen.core.wizard.viewmodels.VM_WindowWizard;

public class VM_ReportEstadisticoAyuda extends VM_WindowWizard {

	private Set<Ayuda> ayudasSeleccionados;

	private List<Ayuda> listAyudas = new ArrayList<>();

	private boolean fecha = false;

	private boolean todos = false;

	private boolean ayuda = false;

	private Ayuda ayudaObjeto = new Ayuda();

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String ayudas = "";

	private String ayudasP = "";

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	String estatusSolicitudAyudas = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";

	String fechaDesde = "";

	String fechaHasta = "";

	@Init(superclass = true)
	public void childInit() {

		parametros.clear();

		listAyudas = S.AyudaService.consultarAllAyuda().getObjetos();

	}

	public boolean isFecha() {
		return fecha;
	}

	public void setFecha(boolean fecha) {
		this.fecha = fecha;
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

		urls.add("views/desktop/reportes/estadisticos/ayudas/selectOpcionesReporteAyuda.zul");
		urls.add("views/desktop/reportes/estadisticos/ayudas/selectCompletado.zul");
		urls.add("views/desktop/reportes/estadisticos/ayudas/viewReportPdfAyuda.zul");

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

			String sql = "SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ";
			Integer cantidadTotal = S.SolicitudAyudaService
					.consultaSolicitudesParametrizado(sql).getObjetos().size();
			if (todos) {
				sql = "SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ";

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
						sql += " and v.fecha >= " + fechaDesdeDate.getTime()
								+ " and v.fecha <= " + fechaHastaDate.getTime()
								+ " ";

					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ";

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

						sql += " and v.fecha >= " + fechaDesdeDate.getTime()
								+ " and v.fecha <= " + fechaHastaDate.getTime()
								+ " ";

					}
				}

				if (ayuda) {

					if (ayudasSeleccionados != null) {
						String ayudas = "";
						int i = 0;

						for (Ayuda ayuda : ayudasSeleccionados) {
							i++;
							ayudas += ayuda.getIdAyuda();
							ayudasP += ayuda.getNombre() + "," + " ";

							if (i != ayudasSeleccionados.size()) {
								ayudas += ",";
							}
						}
						sql += " and v.fkAyuda.idAyuda in (" + ayudas + ")";
					}

				}

			}

			if (sql.equals("SELECT DISTINCT v FROM SolicitudAyuda v  WHERE  v.idSolicitudAyuda = v.idSolicitudAyuda ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>SolicitudAyudas</b>";

			}
			List<IndicadorTsPlan> listParametros = new ArrayList<>();
			if (cantidadTotal > 0) {
			

				String sqlAprobado = sql + "and v.estatusSolicitud = "
						+ EstatusSolicitudEnum.APROBADA.ordinal() + "";

				Integer sqlAprobadoInt = S.SolicitudAyudaService
						.consultaSolicitudesParametrizado(sqlAprobado)
						.getObjetos().size();

				Double d = (double) (sqlAprobadoInt * 100 / cantidadTotal);

				listParametros.add(new IndicadorTsPlan(null, null, null,
						EstatusSolicitudEnum.APROBADA.toString(), d, null));

				String sqlEnProceso = sql + "and v.estatusSolicitud = "
						+ EstatusSolicitudEnum.EN_PROCESO.ordinal() + "";

				Integer sqlEnProcesoInt = S.SolicitudAyudaService
						.consultaSolicitudesParametrizado(sqlEnProceso)
						.getObjetos().size();

				d = (double) (sqlEnProcesoInt * 100 / cantidadTotal);

				listParametros.add(new IndicadorTsPlan(null, null, null,
						EstatusSolicitudEnum.EN_PROCESO.toString(), d, null));

				String sqlPendiente = sql + "and v.estatusSolicitud = "
						+ EstatusSolicitudEnum.PENDIENTE.ordinal() + "";

				Integer sqlPendienteInt = S.SolicitudAyudaService
						.consultaSolicitudesParametrizado(sqlPendiente)
						.getObjetos().size();

				d = (double) (sqlPendienteInt * 100 / cantidadTotal);

				listParametros.add(new IndicadorTsPlan(null, null, null,
						EstatusSolicitudEnum.PENDIENTE.toString(), d, null));

				String sqlProcesa = sql + "and v.estatusSolicitud = "
						+ EstatusSolicitudEnum.PROCESADA.ordinal() + "";

				Integer sqlProcesaInt = S.SolicitudAyudaService
						.consultaSolicitudesParametrizado(sqlProcesa)
						.getObjetos().size();

				d = (double) (sqlProcesaInt * 100 / cantidadTotal);

				listParametros.add(new IndicadorTsPlan(null, null, null,
						EstatusSolicitudEnum.PROCESADA.toString(), d, null));

				String sqlRechazada = sql + "and v.estatusSolicitud = "
						+ EstatusSolicitudEnum.RECHAZADA.ordinal() + "";

				Integer sqlRechazadaInt = S.SolicitudAyudaService
						.consultaSolicitudesParametrizado(sqlRechazada)
						.getObjetos().size();

				System.out.println(sqlRechazadaInt);

				d = (double) (sqlRechazadaInt * 100 / cantidadTotal);

				listParametros.add(new IndicadorTsPlan(null, null, null,
						EstatusSolicitudEnum.RECHAZADA.toString(), d, null));
			}

			jrDataSource = new JRBeanCollectionDataSource(listParametros);
		}
		if (currentStep == 2) {
			type = "pdf";
			if (!estatusSolicitudAyudas.equals("")) {
				tStatus = "Estatus";
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

			parametros.put("titulo", "Solicitud de Ayudas");

			parametros.put("tEstatus", tStatus);

			parametros.put("estatusSolicitudAyudas", estatusSolicitudAyudas);

			Organizacion organizacion = S.OrganizacionService.consultarTodos()
					.getObjetos().get(0);

			parametros.put("tDireccionOrganizacion",
					organizacion.getDireccion());

			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono()
					+ " " + "/" + " " + organizacion.getTelefono2());

			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());

			source = "reporte/estadisticoAyuda.jasper";
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

	public Set<Ayuda> getAyudasSeleccionados() {
		return ayudasSeleccionados;
	}

	public void setAyudasSeleccionados(Set<Ayuda> ayudasSeleccionados) {
		this.ayudasSeleccionados = ayudasSeleccionados;
	}

	public List<Ayuda> getListAyudas() {
		return listAyudas;
	}

	public void setListAyudas(List<Ayuda> listAyudas) {
		this.listAyudas = listAyudas;
	}

	public boolean isAyuda() {
		return ayuda;
	}

	public void setAyuda(boolean ayuda) {
		this.ayuda = ayuda;
	}

	public Ayuda getAyudaObjeto() {
		return ayudaObjeto;
	}

	public void setAyudaObjeto(Ayuda ayudaObjeto) {
		this.ayudaObjeto = ayudaObjeto;
	}

}
