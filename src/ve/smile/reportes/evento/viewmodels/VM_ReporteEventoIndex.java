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
import lights.smile.util.UtilConverterDataList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Profesion;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_ReporteEventoIndex extends VM_WindowWizard {

	private boolean clasificadorEvento = false;

	private boolean evento = false;

	private boolean tarea = false;

	private boolean indicador = false;

	private boolean fechaPlanificada = false;

	private boolean ejecutado = false;

	private boolean cancelado = false;

	private boolean todos = false;

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private List<ClasificadorVoluntario> listClasificadorEvento = new ArrayList<>();

	private Set<ClasificadorVoluntario> clasificadorEventoSeleccionados;

	private List<Fortaleza> listEvento = new ArrayList<>();
	
	private Set<Fortaleza> eventosSeleccionados;

	private List<Profesion> listTareas = new ArrayList<>();

	private Set<Profesion> tareasSeleccionadas;

	private List<Voluntario> listIndicadores;

	private Set<Profesion> indicadoresSeleccionados;
	
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

		listClasificadorEvento = S.ClasificadorVoluntarioService
				.consultarTodos().getObjetos();

		listEvento = S.FortalezaService.consultarTodos().getObjetos();

		listTareas = S.ProfesionService.consultarTodos().getObjetos();


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

			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ";

				if (fechaPlanificada) {
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
						sql += " and v.fechaIngreso >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaIngreso <= "
								+ fechaHastaDate.getTime() + " ";
						System.out.println(sql);
					}
				}

			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ";

				if (fechaPlanificada) {
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
						sql += " and v.fechaIngreso >= "
								+ fechaDesdeDate.getTime()
								+ " and v.fechaIngreso <= "
								+ fechaHastaDate.getTime() + " ";
						System.out.println(sql);
					}
				}

				if (cancelado || ejecutado) {
					String estatusVoluntarios = "";

					if (cancelado) {
						estatusVoluntarios += EstatusVoluntarioEnum.POR_COMPLETAR
								.ordinal() + ",";
						estatusEventosP += EstatusVoluntarioEnum.POR_COMPLETAR
								.toString() + " ";
					}

					if (ejecutado) {
						estatusVoluntarios += EstatusVoluntarioEnum.POSTULADO
								.ordinal() + ",";
						estatusEventosP += EstatusVoluntarioEnum.POSTULADO
								.toString() + " ";
					}

					int tamano = estatusVoluntarios.length();

					char[] tmp = estatusVoluntarios.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusVoluntarios = new String(tmp);

					sql += "and v.estatusVoluntario in(" + estatusVoluntarios
							+ ")";
				}
				if (evento) {

					if (indicadoresSeleccionados != null) {
						String profesiones = "";
						int i = 0;

						for (Profesion profesion : indicadoresSeleccionados) {
							i++;
							profesiones += profesion.getIdProfesion();
							tareasP += profesion.getNombre() + "," + " ";

							if (i != indicadoresSeleccionados.size()) {
								profesiones += ",";
							}

						}
						sql = sql.replace("WHERE",
								", VoluntarioProfesion pv WHERE");
						sql += " and pv.fkVoluntario.idVoluntario = v.idVoluntario and pv.fkProfesion.idProfesion in ("
								+ profesiones + ")";
					}
				}
			}
			if (tarea) {

				if (eventosSeleccionados != null) {
					String fortalezas = "";
					int i = 0;

					for (Fortaleza fortaleza : eventosSeleccionados) {
						i++;
						fortalezas += fortaleza.getIdFortaleza();
						indicadoresP += fortaleza.getNombre() + "," + " ";

						if (i != eventosSeleccionados.size()) {
							fortalezas += ",";
						}
					}
					sql = sql
							.replace("WHERE", ", VoluntarioFortaleza vf WHERE");
					sql += " and vf.fkVoluntario.idVoluntario = v.idVoluntario and vf.fkFortaleza.idFortaleza in ("
							+ fortalezas + ")";
				}

			}
			if (clasificadorEvento) {

				if (clasificadorEventoSeleccionados != null) {
					String voluntarioClasificado = "";
					int i = 0;

					for (ClasificadorVoluntario clasificadorVoluntario : clasificadorEventoSeleccionados) {
						i++;
						eventosClasificadosP += clasificadorVoluntario
								.getNombre() + "," + " ";
						voluntarioClasificado += clasificadorVoluntario
								.getIdClasificadorVoluntario();

						if (i != clasificadorEventoSeleccionados.size()) {
							voluntarioClasificado += ",";
						}
					}
					sql = sql.replace("WHERE",
							", VoluntarioClasificado vc WHERE");
					sql += " and vc.fkVoluntario.idVoluntario = v.idVoluntario and vc.fkClasificadorVoluntario.idClasificadorVoluntario in ("
							+ voluntarioClasificado + ")";
				}

			}
			if (sql.equals("SELECT DISTINCT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ")
					&& !todos) {
				
				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Voluntarios</b>";

			}
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.consultaVoluntariosParametrizado(sql);
//			List<Voluntario> listVoluntarios = payloadVoluntarioResponse
//					.getObjetos();
//			this.getVoluntarios().addAll(listVoluntarios);
//
//			System.out.println(sql);
//			if (listVoluntarios.isEmpty()) {
//				return "E:Error Code 5-Los criterios seleccionados no aportan información para <b>Voluntarios</b>";
//			}
//
//			jrDataSource = new JRBeanCollectionDataSource(listVoluntarios);
		}
		if (currentStep == 2) {
			type = "pdf";
			if (!estatusEventosP.equals("")) {
				tStatus = "Estatus";
			}
			if (!indicadoresP.equals("")) {
				tIndicadores = "Fortalezas";
			}
			if (!tareasP.equals("")) {
				tTareas = "Profesiones";
			}
			if (!eventosClasificadosP.equals("")) {
				tEventosClasificados = "Clasificacion de Voluntarios";
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

			parametros.put("titulo", "VOLUNTARIOS");

			parametros.put("tStatus", tStatus);

			parametros.put("tIndicadores", tIndicadores);

			parametros.put("tTareas", tTareas);

			parametros.put("tEventosClasificados", tEventosClasificados);

			parametros.put("estatusEventosP", estatusEventosP);

			parametros.put("indicadoresP", indicadoresP);

			parametros.put("tareasP", tareasP);

			parametros.put("eventosClasificadosP", eventosClasificadosP);

			source = "reporte/reportVoluntariosParametrizados.jasper";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		System.out.println("algo paso por aqui pendiente");
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


	public List<ClasificadorVoluntario> getListClasificadorEvento() {
		return listClasificadorEvento;
	}


	public void setListClasificadorEvento(
			List<ClasificadorVoluntario> listClasificadorEvento) {
		this.listClasificadorEvento = listClasificadorEvento;
	}


	public Set<ClasificadorVoluntario> getClasificadorEventoSeleccionados() {
		return clasificadorEventoSeleccionados;
	}


	public void setClasificadorEventoSeleccionados(
			Set<ClasificadorVoluntario> clasificadorEventoSeleccionados) {
		this.clasificadorEventoSeleccionados = clasificadorEventoSeleccionados;
	}


	public List<Fortaleza> getListEvento() {
		return listEvento;
	}


	public void setListEvento(List<Fortaleza> listEvento) {
		this.listEvento = listEvento;
	}


	public Set<Fortaleza> getEventosSeleccionados() {
		return eventosSeleccionados;
	}


	public void setEventosSeleccionados(Set<Fortaleza> eventosSeleccionados) {
		this.eventosSeleccionados = eventosSeleccionados;
	}


	public List<Profesion> getListTareas() {
		return listTareas;
	}


	public void setListTareas(List<Profesion> listTareas) {
		this.listTareas = listTareas;
	}


	public Set<Profesion> getTareasSeleccionadas() {
		return tareasSeleccionadas;
	}


	public void setTareasSeleccionadas(Set<Profesion> tareasSeleccionadas) {
		this.tareasSeleccionadas = tareasSeleccionadas;
	}


	public List<Voluntario> getListIndicadores() {
		return listIndicadores;
	}


	public void setListIndicadores(List<Voluntario> listIndicadores) {
		this.listIndicadores = listIndicadores;
	}


	public Set<Profesion> getIndicadoresSeleccionados() {
		return indicadoresSeleccionados;
	}


	public void setIndicadoresSeleccionados(Set<Profesion> indicadoresSeleccionados) {
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
		return tareasP;
	}


	public void setTareasP(String tareasP) {
		this.tareasP = tareasP;
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
	
	
	
	
}
