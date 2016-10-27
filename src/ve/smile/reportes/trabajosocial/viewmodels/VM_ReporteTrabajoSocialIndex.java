package ve.smile.reportes.trabajosocial.viewmodels;
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
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.dto.Actividad;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.Organizacion;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteTrabajoSocialIndex extends VM_WindowWizard {

	private TsPlan trabajoSocialPlanificado = new TsPlan();

	private boolean clasificadorTrabajoSocialPlanificado = false;

	private boolean tipoTrabajoSocial = false;

	private boolean actividad = false;

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

	private List<ClasificadorTrabajoSocial> listClasificadorTrabajoSocialPlanificado = new ArrayList<>();

	private List<TsPlan> listTrabajoSocialPlanificados = new ArrayList<>();

	private Set<ClasificadorTrabajoSocial> clasificadorTsPlanSeleccionados;

	private List<Actividad> listAtividades = new ArrayList<>();

	private Set<Actividad> actividadesSeleccionadas;

	private List<Indicador> listIndicadores;

	private Set<Indicador> indicadoresSeleccionados;

	String tsClasificadosP = "";

	String estatusTsP = "";

	String indicadoresP = "";

	String actividadesP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";

	String tTsClasificados = "";

	String tIndicadores = "";

	String tActividades = "";

	String fechaDesde = "";

	String fechaHasta = "";

	@Init(superclass = true)
	public void childInit() {

		listClasificadorTrabajoSocialPlanificado = S.ClasificadorTrabajoSocialService
				.consultarTodos().getObjetos();

		listAtividades = S.ActividadService.consultarTodos().getObjetos();
	

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

		urls.add("views/desktop/reportes/trabajoSocial/selectOpcionesReporteTrabajoSocial.zul");
		urls.add("views/desktop/reportes/trabajoSocial/selectCompletado.zul");
		urls.add("views/desktop/reportes/trabajoSocial/viewReportPdfTrabajoSocial.zul");

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
		if (currentStep == 3) {
			parametros.clear();
			clearElements();
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		return "";
	}

	public void clearElements() {
		trabajoSocialPlanificado = new TsPlan();

		clasificadorTrabajoSocialPlanificado = false;

		tipoTrabajoSocial = false;

		actividad = false;

		indicador = false;

		fechaPlanificada = false;

		ejecutado = false;

		cancelado = false;

		rechazado = false;

		todos = false;

		planificado = false;

		parcial = false;

		type = new String();

		source = new String();

		jrDataSource = new JRBeanCollectionDataSource(new ArrayList<>());

		parametros = new HashMap<>();

		clasificadorTsPlanSeleccionados = null;

		actividadesSeleccionadas = null;

		indicadoresSeleccionados = null;

		tsClasificadosP = "";

		estatusTsP = "";

		indicadoresP = "";

		actividadesP = "";

		tStatus = "";

		tFechaDesde = "";

		tFechaHasta = "";

		tTsClasificados = "";

		tIndicadores = "";

		tActividades = "";

		fechaDesde = "";

		fechaHasta = "";
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
			listTrabajoSocialPlanificados = new ArrayList<>();
			String sql = "";
		
			if (todos) {

				sql = "SELECT DISTINCT tsp FROM TsPlan tsp  WHERE  tsp.idTsPlan = tsp.idTsPlan ";

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
						sql += " and tsp.fechaPlanificada >= "
								+ fechaDesdeDate.getTime()
								+ " and tsp.fechaPlanificada <= "
								+ fechaHastaDate.getTime() + " ";
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT tsp FROM TsPlan tsp  WHERE  tsp.idTsPlan = tsp.idTsPlan ";

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

				if (cancelado || ejecutado || planificado) {
					String estatusEventos = "";

					if (cancelado) {
						
						estatusEventos += EstatusTrabajoSocialPlanificadoEnum.CANCELADO.ordinal() + ",";
						estatusTsP += EstatusTrabajoSocialPlanificadoEnum.CANCELADO.toString() + " ";
					}

					if (ejecutado) {

						estatusEventos += EstatusTrabajoSocialPlanificadoEnum.EJECUTADO.ordinal() + ",";
						estatusTsP += EstatusTrabajoSocialPlanificadoEnum.EJECUTADO
								.toString() + " ";
					}

					if (planificado) {

						estatusEventos += EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO
								.ordinal() + ",";
						estatusTsP += EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO
								.toString() + " ";
					}

					int tamano = estatusEventos.length();

					char[] tmp = estatusEventos.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusEventos = new String(tmp);

					sql += "and tsp.estatusTsPlan in(" + estatusEventos + ")";
				}

				if (indicador) {
				
					if (indicadoresSeleccionados != null) {
			
						String indicadores = "";
						int i = 0;
			
						for (Indicador indicador : indicadoresSeleccionados) {
							i++;
							indicadores += indicador.getIdIndicador();
							indicadoresP += indicador.getNombre();

							if (i != indicadoresSeleccionados.size()) {
								indicadores += ",";
								indicadoresP +=",";
							}

						}
						if (!indicadores.equals("")) {
							sql = sql.replace("WHERE",
									", IndicadorTsPlan itsp WHERE");
							sql += " and itsp.fkTsPlan.idTsPlan = tsp.idTsPlan and itsp.fkIndicador.idIndicador in ("
									+ indicadores + ")";

						}
					}
				}

				if (actividad) {
					if (actividadesSeleccionadas != null) {
						String actividades = "";
						int i = 0;

						for (Actividad actividad : actividadesSeleccionadas) {
							i++;
							actividades += actividad.getIdActividad();
							actividadesP += actividad.getNombre();

							if (i != actividadesSeleccionadas.size()) {
								actividades += ",";
								actividadesP +=",";
							}
						}
						if (!actividades.equals("")) {
							sql = sql.replace("WHERE",
									", TsPlanActividad pspa WHERE");
							sql += " and pspa.fkTsPlan.idTsPlan = tsp.idTsPlan and pspa.fkActividad.idActividad in ("
									+ actividades + ")";
						}

					}

				}
				if (clasificadorTrabajoSocialPlanificado) {
					if (clasificadorTsPlanSeleccionados != null) {
						String tsPlanSeleccionados = "";
						int i = 0;

						for (ClasificadorTrabajoSocial clasificadorTrabajoSocial : clasificadorTsPlanSeleccionados) {
							i++;
							tsClasificadosP += clasificadorTrabajoSocial
									.getNombre();
							tsPlanSeleccionados += clasificadorTrabajoSocial
									.getIdClasificadorTrabajoSocial();

							if (i != clasificadorTsPlanSeleccionados.size()) {
								tsPlanSeleccionados += ",";
								tsClasificadosP += ",";
							}
						}
						if (!tsPlanSeleccionados.equals("")) {
							sql += " and  tsp.fkTrabajoSocial.fkClasificadorTrabajoSocial.idClasificadorTrabajoSocial in ("
									+ tsPlanSeleccionados + ")";
						}
						
				
					}

				}

			}
			if (sql.equals("SELECT DISTINCT tsp FROM TsPlan tsp  WHERE  tsp.idTsPlan = tsp.idTsPlan ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Voluntarios</b>";

			}
		
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultaTrabajoSocialPlanificadosParametrizado(sql);
			List<TsPlan> listTsPlans = payloadTsPlanResponse.getObjetos();

			if (listTsPlans.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan información para <b>Trabajo Social</b>";
			}

			this.listTrabajoSocialPlanificados.addAll(listTsPlans);

			jrDataSource = new JRBeanCollectionDataSource(listTsPlans);
		}

		if (currentStep == 2) {
			if (parcial) {
				if (trabajoSocialPlanificado == null
						|| trabajoSocialPlanificado.getIdTsPlan() == null) {

					return "E:Error Code 5-Debe Seleccionar un <b>Evento</b>";

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

		if (!estatusTsP.equals("")) {
			tStatus = "Estatus";
		}
		if (!indicadoresP.equals("")) {
			tIndicadores = "Indicadores";
		}
		if (!actividadesP.equals("")) {
			tActividades = "Tareas";
		}
		if (!tsClasificadosP.equals("")) {
			tTsClasificados = "Clasificacion de Eventos";
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

		parametros.put("tActividades", tActividades);
		
		parametros.put("tIndicadores", tIndicadores);

		parametros.put("tTsClasificados", tTsClasificados);

		parametros.put("estatusTsP", estatusTsP);

		parametros.put("indicadoresP", indicadoresP);


		parametros.put("tsClasificadosP", tsClasificadosP);

		parametros.put("tDireccionOrganizacion", organizacion.getDireccion());

parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());
		
		parametros.put("tCorreoOrganizacion", organizacion.getCorreo());

		source = "reporte/reportTsPlanificadosParametrizados.jasper";
	}

	private void reportParametrizadoParcial() {

		parametros.put("titulo",
				"Reporte Parcial de Trabajo Social ");
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

		parametros.put("pNombreTs", trabajoSocialPlanificado
				.getFkTrabajoSocial().getNombre());

		parametros
				.put("pFecha", trabajoSocialPlanificado.getFechaPlanificada());

		parametros.put("pDescripcion", trabajoSocialPlanificado
				.getFkTrabajoSocial().getDescripcion());

		parametros.put("pLugar", trabajoSocialPlanificado.getFkDirectorio()
				.getNombre());

		parametros.put("pDireccion", trabajoSocialPlanificado.getFkDirectorio()
				.getDireccion());

		parametros.put("pResponsable", trabajoSocialPlanificado.getFkPersona()
				.getNombre()
				+ " "
				+ trabajoSocialPlanificado.getFkPersona().getApellido());

		parametros.put("tDireccionOrganizacion", S.OrganizacionService
				.consultarTodos().getObjetos().get(0).getDireccion());

		type = "pdf";

		List<IndicadorTsPlan> tsIndicadores = new ArrayList<>();
		Map<String, String> criterios = new HashMap<>();

		criterios.put("fkTsPlan.idTsPlan",
				trabajoSocialPlanificado.getIdTsPlan() + "");
		PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
				.consultarCriterios(TypeQuery.EQUAL, criterios);

		if (payloadIndicadorTsPlanResponse.getObjetos() != null) {
			tsIndicadores.addAll(payloadIndicadorTsPlanResponse
					.getObjetos());
		}

		List<TsPlanActividad> tsPlanActividads = new ArrayList<>();
		criterios = new HashMap<>();
		criterios.put("fkTsPlan.idTsPlan",
				trabajoSocialPlanificado.getIdTsPlan() + "");
		PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);

		if (payloadTsPlanActividadResponse.getObjetos() != null) {
			tsPlanActividads.addAll(payloadTsPlanActividadResponse.getObjetos());
		}
		if (!tsPlanActividads.isEmpty()) {
			parametros.put("tsActividades", tsPlanActividads);
			parametros.put("tNombreTsA", "Nombre");
			parametros.put("tDescripcion", "Descripción");

		} else {
			parametros.put("tMensajeActividad",
					"No hay  Actividades  Asignadas a este Trabajo Social");

		}
		if (!tsIndicadores.isEmpty()) {
			parametros.put("tNombreTsI", "Nombre");
			parametros.put("tUnidadMedida", "Unidad de Medida");
			parametros.put("tValorReal", "Valor Real");
			parametros.put("tValorEsperado", "Valor esperado");
			parametros.put("tsIndicadores", tsIndicadores);
		} else {
			parametros.put("tMensajeIndicador",
					"No hay indicadores asignandos a este Trabajo Social");
		}

		source = "reporte/reportTsParametrizadosParcial.jasper";
	}

	public TsPlan getTrabajoSocialPlanificado() {
		return trabajoSocialPlanificado;
	}

	public void setTrabajoSocialPlanificado(TsPlan trabajoSocialPlanificado) {
		this.trabajoSocialPlanificado = trabajoSocialPlanificado;
	}

	public boolean isClasificadorTrabajoSocialPlanificado() {
		return clasificadorTrabajoSocialPlanificado;
	}

	public void setClasificadorTrabajoSocialPlanificado(
			boolean clasificadorTrabajoSocialPlanificado) {
		this.clasificadorTrabajoSocialPlanificado = clasificadorTrabajoSocialPlanificado;
	}

	public boolean isTipoTrabajoSocial() {
		return tipoTrabajoSocial;
	}

	public void setTipoTrabajoSocial(boolean tipoTrabajoSocial) {
		this.tipoTrabajoSocial = tipoTrabajoSocial;
	}

	public boolean isActividad() {
		return actividad;
	}

	public void setActividad(boolean actividad) {
		this.actividad = actividad;
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

	public boolean isRechazado() {
		return rechazado;
	}

	public void setRechazado(boolean rechazado) {
		this.rechazado = rechazado;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public boolean isPlanificado() {
		return planificado;
	}

	public void setPlanificado(boolean planificado) {
		this.planificado = planificado;
	}

	public boolean isParcial() {
		return parcial;
	}

	public void setParcial(boolean parcial) {
		this.parcial = parcial;
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

	public List<ClasificadorTrabajoSocial> getListClasificadorTrabajoSocialPlanificado() {
		return listClasificadorTrabajoSocialPlanificado;
	}

	public void setListClasificadorTrabajoSocialPlanificado(
			List<ClasificadorTrabajoSocial> listClasificadorTrabajoSocialPlanificado) {
		this.listClasificadorTrabajoSocialPlanificado = listClasificadorTrabajoSocialPlanificado;
	}

	public List<TsPlan> getListTrabajoSocialPlanificados() {
		return listTrabajoSocialPlanificados;
	}

	public void setListTrabajoSocialPlanificados(
			List<TsPlan> listTrabajoSocialPlanificados) {
		this.listTrabajoSocialPlanificados = listTrabajoSocialPlanificados;
	}

	public Set<ClasificadorTrabajoSocial> getClasificadorTsPlanSeleccionados() {
		return clasificadorTsPlanSeleccionados;
	}

	public void setClasificadorTsPlanSeleccionados(
			Set<ClasificadorTrabajoSocial> clasificadorTsPlanSeleccionados) {
		this.clasificadorTsPlanSeleccionados = clasificadorTsPlanSeleccionados;
	}

	public List<Actividad> getListAtividades() {
		return listAtividades;
	}

	public void setListAtividades(List<Actividad> listAtividades) {
		this.listAtividades = listAtividades;
	}

	public Set<Actividad> getActividadesSeleccionadas() {
		return actividadesSeleccionadas;
	}

	public void setActividadesSeleccionadas(
			Set<Actividad> actividadesSeleccionadas) {
		this.actividadesSeleccionadas = actividadesSeleccionadas;
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

	public String getTsPlanClasificadosP() {
		return tsClasificadosP;
	}

	public void setTsPlanClasificadosP(String tsPlanClasificadosP) {
		this.tsClasificadosP = tsPlanClasificadosP;
	}

	public String getEstatusTsPlanP() {
		return estatusTsP;
	}

	public void setEstatusTsPlanP(String estatusTsPlanP) {
		this.estatusTsP = estatusTsPlanP;
	}

	public String getIndicadoresP() {
		return indicadoresP;
	}

	public void setIndicadoresP(String indicadoresP) {
		this.indicadoresP = indicadoresP;
	}

	public String getActividadesP() {
		return actividadesP;
	}

	public void setActividadesP(String actividadesP) {
		this.actividadesP = actividadesP;
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

	public String gettTsPlanClasificados() {
		return tTsClasificados;
	}

	public void settTsPlanClasificados(String tTsPlanClasificados) {
		this.tTsClasificados = tTsPlanClasificados;
	}

	public String gettIndicadores() {
		return tIndicadores;
	}

	public void settIndicadores(String tIndicadores) {
		this.tIndicadores = tIndicadores;
	}

	public String gettActividades() {
		return tActividades;
	}

	public void settActividades(String tActividades) {
		this.tActividades = tActividades;
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
