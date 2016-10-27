package ve.smile.reportes.evento.viewmodels;

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
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilConverterDataList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorEventoPlanificado;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Tarea;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.enums.TipoEventoEnum;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteEventoIndex extends VM_WindowWizard {

	private EventoPlanificado eventoPlanificado = new EventoPlanificado();

	private boolean clasificadorEvento = false;

	private boolean evento = false;

	private boolean tarea = false;

	private boolean indicador = false;

	private boolean fechaPlanificada = false;

	private boolean ejecutado = false;

	private boolean cancelado = false;

	private boolean rechazado = false;

	private boolean todos = false;

	private boolean planificado = false;

	private boolean parcial = false;

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private List<ClasificadorEvento> listClasificadorEvento = new ArrayList<>();

	private List<EventoPlanificado> listEventosPlanificados = new ArrayList<>();

	private Set<ClasificadorEvento> clasificadorEventoSeleccionados;

	private List<TipoEventoEnum> listEvento = new ArrayList<>();

	private Set<TipoEventoEnum> eventosSeleccionados;

	private List<Tarea> listTareas = new ArrayList<>();

	private Set<Tarea> tareasSeleccionadas;

	private List<Indicador> listIndicadores;

	private Set<Indicador> indicadoresSeleccionados;

	String eventosClasificadosP = "";

	String estatusEventosP = "";

	String indicadoresP = "";

	String tareasP = "";

	String eventosP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";

	String tEventosClasificados = "";

	String tIndicadores = "";

	String tTareas = "";

	String fechaDesde = "";

	String fechaHasta = "";

	@Init(superclass = true)
	public void childInit() {

		listClasificadorEvento = S.ClasificadorEventoService.consultarTodos()
				.getObjetos();

		listTareas = S.TareaService.consultarTodos().getObjetos();


		listIndicadores = S.IndicadorService.consultarTodos().getObjetos();

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

		urls.add("views/desktop/reportes/evento/selectOpcionesReporteEvento.zul");
		urls.add("views/desktop/reportes/evento/selectCompletado.zul");
		urls.add("views/desktop/reportes/evento/viewReportPdfEvento.zul");

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
			restartWizard();
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
			listEventosPlanificados = new ArrayList<>();
			String sql = "";

			if (todos) {

				sql = "SELECT DISTINCT ep FROM EventoPlanificado ep  WHERE  ep.idEventoPlanificado = ep.idEventoPlanificado ";

				if (fechaPlanificada) {
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
						sql += " and ep.fechaPlanificada >= "
								+ fechaDesdeDate.getTime()
								+ " and ep.fechaPlanificada <= "
								+ fechaHastaDate.getTime() + " ";
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT ep FROM EventoPlanificado ep  WHERE  ep.idEventoPlanificado = ep.idEventoPlanificado ";

				if (fechaPlanificada) {

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

						sql += " and ep.fechaPlanificada >= "
								+ fechaDesdeDate.getTime()
								+ " and ep.fechaPlanificada <= "
								+ fechaHastaDate.getTime() + " ";
					}
				}

				if (cancelado || ejecutado || planificado || rechazado) {
					String estatusEventos = "";

					if (cancelado) {
						estatusEventos += EstatusEventoPlanificadoEnum.CANCELADO
								.ordinal() + ",";
						estatusEventosP += EstatusEventoPlanificadoEnum.CANCELADO
								.toString() + " ";
					}

					if (ejecutado) {

						estatusEventos += EstatusEventoPlanificadoEnum.EJECUTADO
								.ordinal() + ",";
						estatusEventosP += EstatusEventoPlanificadoEnum.EJECUTADO
								.toString() + " ";
					}

					if (planificado) {

						estatusEventos += EstatusEventoPlanificadoEnum.PLANIFICADO
								.ordinal() + ",";
						estatusEventosP += EstatusEventoPlanificadoEnum.PLANIFICADO
								.toString() + " ";
					}

					if (rechazado) {

						estatusEventos += EstatusEventoPlanificadoEnum.RECHAZADO
								.ordinal() + ",";
						estatusEventosP += EstatusEventoPlanificadoEnum.RECHAZADO
								.toString() + " ";
					}

					int tamano = estatusEventos.length();

					char[] tmp = estatusEventos.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusEventos = new String(tmp);

					sql += "and ep.estatusEvento in(" + estatusEventos + ")";
				}
				if (indicador) {
					if (indicadoresSeleccionados != null) {
						String indicadores = "";
						int i = 0;

						for (Indicador indicador : indicadoresSeleccionados) {
							i++;
							indicadores += indicador.getIdIndicador();
							indicadoresP += indicador.getNombre() + "," + " ";

							if (i != indicadoresSeleccionados.size()) {
								indicadores += ",";
							}

						}
						sql = sql.replace("WHERE",
								", IndicadorEventoPlanificado epi WHERE");
						sql += " and epi.fkEventoPlanificado.idEventoPlanificado = ep.idEventoPlanificado and epi.fkIndicador.idIndicador in ("
								+ indicadores + ")";
					}
				}
			}
			if (tarea) {
				if (tareasSeleccionadas != null) {
					String tareas = "";
					int i = 0;

					for (Tarea tarea : tareasSeleccionadas) {
						i++;
						tareas += tarea.getIdTarea();
						tareasP += tarea.getNombre() + "," + " ";

						if (i != eventosSeleccionados.size()) {
							tareas += ",";
						}
					}
					sql = sql.replace("WHERE", ", EventPlanTarea ept WHERE");
					sql += " and ept.fkEventoPlanificado.idEventoPlanificado = ep.idEventoPlanificado and ept.fkTarea.idTarea in ("
							+ tareas + ")";
				}

			}
			if (clasificadorEvento) {

				if (clasificadorEventoSeleccionados != null) {
					String eventosClasificados = "";
					int i = 0;

					for (ClasificadorEvento clasificadorEvento : clasificadorEventoSeleccionados) {
						i++;
						eventosClasificadosP += clasificadorEvento.getNombre()
								+ "," + " ";
						eventosClasificados += clasificadorEvento
								.getIdClasificadorEvento();

						if (i != clasificadorEventoSeleccionados.size()) {
							eventosClasificados += ",";
						}
					}
					sql += " and  ep.fkEvento.fkClasificadorEvento.idClasificadorEvento in ("
							+ eventosClasificados + ")";
				}

			}
			if (sql.equals("SELECT DISTINCT v FROM EventoPlanificado ep  WHERE  ep.idEventoPlanificado = ep.idEventoPlanificado ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Voluntarios</b>";

			}
	
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultaEventosPlanificadosParametrizado(sql);
			List<EventoPlanificado> listEventosPlanificados = payloadEventoPlanificadoResponse
					.getObjetos();

			if (listEventosPlanificados.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>Eventos</b>";
			}

			this.listEventosPlanificados.addAll(listEventosPlanificados);

			jrDataSource = new JRBeanCollectionDataSource(
					listEventosPlanificados);
		}

		if (currentStep == 2) {
			if (parcial) {
				if (eventoPlanificado == null
						|| eventoPlanificado.getIdEventoPlanificado() == null) {

					return "E:Error Code 5-Debe Seleccionar un <b> Trabajo Social</b>";

				} else {
					reportParametrizadoParcial();
					return "";
				}
			}
			reportEventosPlanificadosParametrizados();
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		return "";
	}

	public boolean isClasificadorEvento() {
		return clasificadorEvento;
	}

	public void setClasificadorEvento(boolean clasificadorEvento) {
		this.clasificadorEvento = clasificadorEvento;
	}

	public boolean isEvento() {
		return evento;
	}

	public void setEvento(boolean evento) {
		this.evento = evento;
	}

	public boolean isTarea() {
		return tarea;
	}

	public void setTarea(boolean tarea) {
		this.tarea = tarea;
	}

	public boolean isIndicador() {
		return indicador;
	}

	public void setIndicador(boolean indicador) {
		this.indicador = indicador;
	}

	public boolean isFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(boolean fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}

	public boolean isEjecutado() {
		return ejecutado;
	}

	public void setEjecutado(boolean ejecutado) {
		this.ejecutado = ejecutado;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
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

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public List<ClasificadorEvento> getListClasificadorEvento() {
		return listClasificadorEvento;
	}

	public void setListClasificadorEvento(
			List<ClasificadorEvento> listClasificadorEvento) {
		this.listClasificadorEvento = listClasificadorEvento;
	}

	public Set<ClasificadorEvento> getClasificadorEventoSeleccionados() {
		return clasificadorEventoSeleccionados;
	}

	public void setClasificadorEventoSeleccionados(
			Set<ClasificadorEvento> clasificadorEventoSeleccionados) {
		this.clasificadorEventoSeleccionados = clasificadorEventoSeleccionados;
	}

	public List<TipoEventoEnum> getListEvento() {
		if (this.listEvento == null) {
			this.listEvento = new ArrayList<>();
		}
		if (this.listEvento.isEmpty()) {
			for (TipoEventoEnum tipoEventoEnum : TipoEventoEnum.values()) {
				this.listEvento.add(tipoEventoEnum);
			}
		}
		return listEvento;
	}

	public void setListEvento(List<TipoEventoEnum> listEvento) {
		this.listEvento = listEvento;
	}

	public Set<TipoEventoEnum> getEventosSeleccionados() {
		return eventosSeleccionados;
	}

	public void setEventosSeleccionados(Set<TipoEventoEnum> eventosSeleccionados) {
		this.eventosSeleccionados = eventosSeleccionados;
	}

	public List<Tarea> getListTareas() {
		return listTareas;
	}

	public void setListTareas(List<Tarea> listTareas) {
		this.listTareas = listTareas;
	}

	public Set<Tarea> getTareasSeleccionadas() {
		return tareasSeleccionadas;
	}

	public void setTareasSeleccionadas(Set<Tarea> tareasSeleccionadas) {
		this.tareasSeleccionadas = tareasSeleccionadas;
	}

	public List<Indicador> getListIndicadores() {
		return listIndicadores;
	}

	public void setListIndicadores(List<Indicador> listIndicadores) {
		this.listIndicadores = listIndicadores;
	}

	public Set<Indicador> getIndicadoresSeleccionados() {
		return indicadoresSeleccionados;
	}

	public void setIndicadoresSeleccionados(
			Set<Indicador> indicadoresSeleccionados) {
		this.indicadoresSeleccionados = indicadoresSeleccionados;
	}

	public String getEventosClasificadosP() {
		return eventosClasificadosP;
	}

	public void setEventosClasificadosP(String eventosClasificadosP) {
		this.eventosClasificadosP = eventosClasificadosP;
	}

	public String getEstatusEventosP() {
		return estatusEventosP;
	}

	public void setEstatusEventosP(String estatusEventosP) {
		this.estatusEventosP = estatusEventosP;
	}

	public String getIndicadoresP() {
		return indicadoresP;
	}

	public void setIndicadoresP(String indicadoresP) {
		this.indicadoresP = indicadoresP;
	}

	public String getTareasP() {
		return indicadoresP;
	}

	public void setTareasP(String tareasP) {
		this.indicadoresP = tareasP;
	}

	public String getEventosP() {
		return eventosP;
	}

	public void setEventosP(String eventosP) {
		this.eventosP = eventosP;
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

	public String gettEventosClasificados() {
		return tEventosClasificados;
	}

	public void settEventosClasificados(String tEventosClasificados) {
		this.tEventosClasificados = tEventosClasificados;
	}

	public String gettIndicadores() {
		return tIndicadores;
	}

	public void settIndicadores(String tIndicadores) {
		this.tIndicadores = tIndicadores;
	}

	public String gettTareas() {
		return tTareas;
	}

	public void settTareas(String tTareas) {
		this.tTareas = tTareas;
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

	public boolean isPlanificado() {
		return planificado;
	}

	public void setPlanificado(boolean planificado) {
		this.planificado = planificado;
	}

	public boolean isRechazado() {
		return rechazado;
	}

	public void setRechazado(boolean rechazado) {
		this.rechazado = rechazado;
	}

	public List<EventoPlanificado> getListEventosPlanificados() {
		return listEventosPlanificados;
	}

	public void setListEventosPlanificados(
			List<EventoPlanificado> listEventosPlanificados) {
		this.listEventosPlanificados = listEventosPlanificados;
	}

	public boolean isParcial() {
		return parcial;
	}

	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}

	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public void reportEventosPlanificadosParametrizados() {
		String direccion = Reporte.class
				.getResource("Reporte.jasper")
				.getPath()
				.replace("WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
						"imagenes/logo_fanca.jpg");
		direccion = direccion.replaceFirst("/", "");
		direccion = direccion.replace("/", "\\");
		parametros.put("timagen1", direccion);

		direccion = Reporte.class
				.getResource("Reporte.jasper")
				.getPath()
				.replace("WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
						"imagenes/smiles_webdesktop.png");
		direccion = direccion.replaceFirst("/", "");
		direccion = direccion.replace("/", "\\");
		parametros.put("timagen2", direccion);

		type = "pdf";

		Organizacion organizacion = S.OrganizacionService.consultarTodos()
				.getObjetos().get(0);

		if (!estatusEventosP.equals("")) {
			tStatus = "Estatus";
		}
		if (!indicadoresP.equals("")) {
			tIndicadores = "Indicadores";
		}
		if (!tareasP.equals("")) {
			tTareas = "Tareas";
		}
		if (!eventosClasificadosP.equals("")) {
			tEventosClasificados = "Clasificacion de Eventos";
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

		parametros.put("titulo", "EVENTOS");

		parametros.put("tStatus", tStatus);

		parametros.put("tIndicadores", tIndicadores);

		parametros.put("tTareas", tTareas);

		parametros.put("tEventosClasificados", tEventosClasificados);

		parametros.put("estatusEventosP", estatusEventosP);

		parametros.put("indicadoresP", indicadoresP);

		parametros.put("tareasP", indicadoresP);

		parametros.put("eventosClasificadosP", eventosClasificadosP);

		parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
		
		parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());
		
		parametros.put("tCorreoOrganizacion", organizacion.getCorreo());

		source = "reporte/reportEventosPlanificadosParametrizados.jasper";
	}

	private void reportParametrizadoParcial() {

		parametros.put("titulo",
				"Reporte Estadistico de Eventos Planificados Vs Ejecutados");
		String direccion = Reporte.class
				.getResource("Reporte.jasper")
				.getPath()
				.replace("WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
						"imagenes/logo_fanca.jpg");
		direccion = direccion.replaceFirst("/", "");
		direccion = direccion.replace("/", "\\");
		parametros.put("timagen1", direccion);
		direccion = Reporte.class
				.getResource("Reporte.jasper")
				.getPath()
				.replace("WEB-INF/classes/ve/smile/reportes/Reporte.jasper",
						"imagenes/smiles_webdesktop.png");
		direccion = direccion.replaceFirst("/", "");
		direccion = direccion.replace("/", "\\");
		parametros.put("timagen2", direccion);

		parametros.put("pNombreEvento", eventoPlanificado.getFkEvento()
				.getNombre());

		String tipoEvento = eventoPlanificado.getFkEvento().getTipoEvento() == 0 ? "ANUAL"
				: "EXTRAORDINARIO";

		parametros.put("pTipoEvento", tipoEvento);

		parametros.put("pFecha", eventoPlanificado.getFechaPlanificada());

		parametros.put("pDescripcion", eventoPlanificado.getFkEvento()
				.getDescripcion());

		parametros.put("pLugar", eventoPlanificado.getFkDirectorio()
				.getNombre());

		parametros.put("pDireccion", eventoPlanificado.getFkDirectorio()
				.getDireccion());

		parametros.put("pResponsable", eventoPlanificado.getFkPersona()
				.getNombre()
				+ " "
				+ eventoPlanificado.getFkPersona().getApellido());

		parametros.put("tDireccionOrganizacion", S.OrganizacionService
				.consultarTodos().getObjetos().get(0).getDireccion());

		type = "pdf";

		List<IndicadorEventoPlanificado> eventoIndicadores = new ArrayList<>();
		Map<String, String> criterios = new HashMap<>();

		criterios.put("fkEventoPlanificado.idEventoPlanificado",
				eventoPlanificado.getIdEventoPlanificado() + "");
		PayloadIndicadorEventoPlanificadoResponse indicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService
				.consultarCriterios(TypeQuery.EQUAL, criterios);

		if (indicadorEventoPlanificadoResponse.getObjetos() != null) {
			eventoIndicadores.addAll(indicadorEventoPlanificadoResponse
					.getObjetos());
		}

		List<EventPlanTarea> eventoTareas = new ArrayList<>();
		criterios = new HashMap<>();
		criterios.put("fkEventoPlanificado.idEventoPlanificado",
				eventoPlanificado.getIdEventoPlanificado() + "");
		PayloadEventPlanTareaResponse payloadTsPlanActividadResponse = S.EventPlanTareaService
				.consultarCriterios(TypeQuery.EQUAL, criterios);

		if (payloadTsPlanActividadResponse.getObjetos() != null) {
			eventoTareas.addAll(payloadTsPlanActividadResponse.getObjetos());
		}
		if (!eventoTareas.isEmpty()) {
			parametros.put("eventoTareas", eventoTareas);
			parametros.put("tNombre", "Nombre");
			parametros.put("tDescripcion", "Descripci�n");

		}
		else{
		
			parametros.put("tMensajeTarea", "No hay tareas Asignadas a este evento");
			
		}
		if (!eventoIndicadores.isEmpty()) {
			parametros.put("tUnidadMedida", "Unidad de Medida");
			parametros.put("tValorReal", "Valor Real");
			parametros.put("tValorEsperado", "Valor esperado");
			parametros.put("eventoIndicadores", eventoIndicadores);
		}
		else{
			parametros.put("tMensajeIndicador", "No hay indicadores asignandos a este evento");
		}

		source = "reporte/reportEventosParametrizadosParcial.jasper";
	}

}
