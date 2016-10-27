package ve.smile.reportes.voluntario.viewmodels;

// import para Excel
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.smile.util.UtilConverterDataList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Profesion;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadClasificadorVoluntarioResponse;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteVoluntarioIndex extends VM_WindowWizard {

	private boolean clasificadorVoluntario = false;

	private boolean profesionVoluntario = false;

	private boolean fortalezaVoluntario = false;

	private boolean fechaIngreso = false;

	private boolean postulado = false;

	private boolean porCompletar = false;

	private boolean activo = false;

	private boolean egresado = false;

	private boolean todos = false;

	private Date fechaDesdeDate;

	private Date fechaHastaDate;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private List<ClasificadorVoluntario> listClasificadorVoluntario = new ArrayList<>();

	private Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados;

	private List<Fortaleza> listFortalezas = new ArrayList<>();

	private Set<Fortaleza> fortalezasSeleccionados;

	private List<Profesion> listProfesion = new ArrayList<>();

	private Set<Profesion> profesionesSeleccionadas;

	private List<Voluntario> voluntarios;

	String voluntarioClasificadoP = "";

	String estatusVoluntariosS = "";

	String fortalezasP = "";

	String profesionesP = "";

	String tStatus = "";

	String tFechaDesde = "";

	String tFechaHasta = "";

	String tVoluntarioClasificado = "";

	String tFortalezas = "";

	String tProfesiones = "";

	String fechaDesde = "";

	String fechaHasta = "";

	@Init(superclass = true)
	public void childInit() {
		parametros.clear();
	}

	public Set<ClasificadorVoluntario> getClasificadorVoluntariosSeleccionados() {
		return clasificadorVoluntariosSeleccionados;
	}

	public void setClasificadorVoluntariosSeleccionados(
			Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados) {

		this.clasificadorVoluntariosSeleccionados = clasificadorVoluntariosSeleccionados;

	}

	public boolean isClasificadorVoluntario() {
		return clasificadorVoluntario;
	}

	public void setClasificadorVoluntario(boolean clasificadorVoluntario) {
		this.clasificadorVoluntario = clasificadorVoluntario;
	}

	public boolean isProfesionVoluntario() {
		return profesionVoluntario;
	}

	public void setProfesionVoluntario(boolean profesionVoluntario) {
		this.profesionVoluntario = profesionVoluntario;
	}

	public boolean isFortalezaVoluntario() {
		return fortalezaVoluntario;
	}

	public void setFortalezaVoluntario(boolean fortalezaVoluntario) {
		this.fortalezaVoluntario = fortalezaVoluntario;
	}

	public boolean isFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(boolean fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public boolean isPostulado() {
		return postulado;
	}

	public void setPostulado(boolean postulado) {
		this.postulado = postulado;
	}

	public List<ClasificadorVoluntario> getListClasificadorVoluntario() {
		if (listClasificadorVoluntario == null) {
			listClasificadorVoluntario = new ArrayList<>();
		}
		if (listClasificadorVoluntario.isEmpty()) {
			PayloadClasificadorVoluntarioResponse payloadClasificadorVoluntarioResponse = S.ClasificadorVoluntarioService
					.consultarTodos();
			if (UtilPayload.isOK(payloadClasificadorVoluntarioResponse)) {
				listClasificadorVoluntario = payloadClasificadorVoluntarioResponse
						.getObjetos();
			}
		}
		return listClasificadorVoluntario;

	}

	public void setListClasificadorVoluntario(
			List<ClasificadorVoluntario> listClasificadorVoluntario) {
		this.listClasificadorVoluntario = listClasificadorVoluntario;
	}

	public List<Fortaleza> getListFortalezas() {
		if (listFortalezas == null) {
			listFortalezas = new ArrayList<>();
		}
		if (listFortalezas.isEmpty()) {
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
					.consultarTodos();
			if (UtilPayload.isOK(payloadFortalezaResponse)) {
				listFortalezas = payloadFortalezaResponse.getObjetos();
			}
		}
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
		if (listProfesion == null) {

		}
		if (listProfesion.isEmpty()) {
			PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
					.consultarTodos();
			if (UtilPayload.isOK(payloadProfesionResponse)) {
				listProfesion = payloadProfesionResponse.getObjetos();
			}

		}
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

	public boolean isPorCompletar() {
		return porCompletar;
	}

	public void setPorCompletar(boolean porCompletar) {
		this.porCompletar = porCompletar;
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

		urls.add("views/desktop/reportes/voluntario/selectOpcionesReporteVoluntario.zul");
		urls.add("views/desktop/reportes/voluntario/selectCompletado.zul");
		urls.add("views/desktop/reportes/voluntario/viewReportPdfVoluntario.zul");

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
		clasificadorVoluntario = false;
		profesionVoluntario = false;
		fortalezaVoluntario = false;
		fechaIngreso = false;
		postulado = false;
		porCompletar = false;
		activo = false;
		egresado = false;
		todos = false;
		fechaDesdeDate = null;
		fechaHastaDate = null;
		type = new String();
		source = new String();

		clasificadorVoluntariosSeleccionados = new HashSet<>();
		listFortalezas = new ArrayList<>();
		fortalezasSeleccionados = new HashSet<>();
	
		profesionesSeleccionadas = new HashSet<>();
		
		voluntarioClasificadoP = "";
		estatusVoluntariosS = "";
		fortalezasP = "";
		profesionesP = "";
		tStatus = "";
		tFechaDesde = "";
		tFechaHasta = "";
		tVoluntarioClasificado = "";
		tFortalezas = "";
		tProfesiones = "";
		fechaDesde = "";
		fechaHasta = "";
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
			try {
				HSSFWorkbook workBook = new HSSFWorkbook();
				HSSFSheet workSheet = workBook.createSheet("VOLUNTARIOS");
				Row excelRow = null;
				Cell excelCell = null;
				int rowNumber = 0;
				excelRow = workSheet.createRow(rowNumber++);

				excelCell = excelRow.createCell(4);
				excelCell.setCellValue("VOLUNTARIOS");
				excelRow = workSheet.createRow(rowNumber++);
//				if (fechaDesdeDate != null) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("Fecha desde");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(UtilConverterDataList
//							.convertirLongADate(fechaDesdeDate.getTime()));
//					excelRow = workSheet.createRow(rowNumber++);
//				}
//				if (fechaHastaDate != null) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("Fecha Hasta");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(UtilConverterDataList
//							.convertirLongADate(fechaDesdeDate.getTime()));
//					excelRow = workSheet.createRow(rowNumber++);
//				}
//				if (!fortalezasP.trim().isEmpty()) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("Fortalezas");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(fortalezasP);
//					excelRow = workSheet.createRow(rowNumber++);
//				}
//				if (!profesionesP.trim().isEmpty()) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("PROFESIONES");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(profesionesP);
//					excelRow = workSheet.createRow(rowNumber++);
//				}
//				if (!estatusVoluntariosS.trim().isEmpty()) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("Estatus");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(estatusVoluntariosS);
//					excelRow = workSheet.createRow(rowNumber++);
//				}
//				if (!voluntarioClasificadoP.trim().isEmpty()) {
//					excelRow = workSheet.createRow(rowNumber++);
//
//					excelCell = excelRow.createCell(1);
//					excelCell.setCellValue("Clasificadores de Voluntario");
//					excelCell = excelRow.createCell(2);
//					excelCell.setCellValue(voluntarioClasificadoP);
//					excelRow = workSheet.createRow(rowNumber++);
//				}

				excelRow = workSheet.createRow(rowNumber++);
				excelCell = excelRow.createCell(0);
				excelCell.setCellValue("CEDULA / RIF");
				excelCell = excelRow.createCell(1);
				excelCell.setCellValue("NOMBRES");
				excelCell = excelRow.createCell(2);
				excelCell.setCellValue("APELLIDOS");
				excelCell = excelRow.createCell(3);
				excelCell.setCellValue("CORREO");
				excelCell = excelRow.createCell(4);
				excelCell.setCellValue("DIRECCI�N");
				excelCell = excelRow.createCell(5);
				excelCell.setCellValue("TEL�FONO");
				excelCell = excelRow.createCell(6);
				excelCell.setCellValue("ESTATUS");

				for (Voluntario voluntario : this.getVoluntarios()) {

					excelRow = workSheet.createRow(rowNumber++);
					excelCell = excelRow.createCell(0);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getIdentificacion());

					excelCell = excelRow.createCell(1);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getNombre());

					excelCell = excelRow.createCell(2);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getApellido());

					excelCell = excelRow.createCell(3);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getCorreo());

					excelCell = excelRow.createCell(4);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getDireccion());

					excelCell = excelRow.createCell(5);
					excelCell.setCellValue(voluntario.getFkPersona()
							.getTelefono1());

					if (voluntario.getEstatusVoluntario() != null) {
						excelCell = excelRow.createCell(6);
						excelCell
								.setCellValue(voluntario
										.getEstatusVoluntarioEnum().toString());
					}
					
				}
				File tempFile = new File("C:\\Smile\\Voluntarios.xls");
				FileOutputStream outputStream = new FileOutputStream(tempFile);
				workBook.write(outputStream);

				outputStream.close();

				Filedownload.save(tempFile, "application/file");
			} catch (IOException e) {
				return "E:Error Code 5-No se pudo generar el archivo";
			}
		}
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1(Integer currentStep) {

		if (currentStep == 1) {
			this.setVoluntarios(new ArrayList<Voluntario>());
			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ";

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
				sql = "SELECT DISTINCT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ";

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

				if (porCompletar || postulado || egresado || activo) {
					String estatusVoluntarios = "";

					if (porCompletar) {
						estatusVoluntarios += EstatusVoluntarioEnum.POR_COMPLETAR
								.ordinal() + ",";
						estatusVoluntariosS += EstatusVoluntarioEnum.POR_COMPLETAR
								.toString() + " ";
					}

					if (postulado) {
						estatusVoluntarios += EstatusVoluntarioEnum.POSTULADO
								.ordinal() + ",";
						estatusVoluntariosS += EstatusVoluntarioEnum.POSTULADO
								.toString() + " ";
					}

					if (egresado) {
						estatusVoluntarios += EstatusVoluntarioEnum.INACTIVO
								.ordinal() + ",";
						estatusVoluntariosS += EstatusVoluntarioEnum.INACTIVO
								.toString() + " ";
					}

					if (activo) {
						estatusVoluntarios += EstatusVoluntarioEnum.ACTIVO
								.ordinal() + ",";
						estatusVoluntariosS += EstatusVoluntarioEnum.ACTIVO
								.toString() + " ";
					}

					int tamano = estatusVoluntarios.length();

					char[] tmp = estatusVoluntarios.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusVoluntarios = new String(tmp);

					sql += "and v.estatusVoluntario in(" + estatusVoluntarios
							+ ")";
				}
				if (profesionVoluntario) {

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
								", VoluntarioProfesion pv WHERE");
						sql += " and pv.fkVoluntario.idVoluntario = v.idVoluntario and pv.fkProfesion.idProfesion in ("
								+ profesiones + ")";
					}
				}
			}
			if (fortalezaVoluntario) {

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
							.replace("WHERE", ", VoluntarioFortaleza vf WHERE");
					sql += " and vf.fkVoluntario.idVoluntario = v.idVoluntario and vf.fkFortaleza.idFortaleza in ("
							+ fortalezas + ")";
				}

			}
			if (clasificadorVoluntario) {

				if (clasificadorVoluntariosSeleccionados != null) {
					String voluntarioClasificado = "";
					int i = 0;

					for (ClasificadorVoluntario clasificadorVoluntario : clasificadorVoluntariosSeleccionados) {
						i++;
						voluntarioClasificadoP += clasificadorVoluntario
								.getNombre() + "," + " ";
						voluntarioClasificado += clasificadorVoluntario
								.getIdClasificadorVoluntario();

						if (i != clasificadorVoluntariosSeleccionados.size()) {
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
			List<Voluntario> listVoluntarios = payloadVoluntarioResponse
					.getObjetos();
	

		
			if (listVoluntarios.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>Voluntarios</b>";
			}
			this.getVoluntarios().addAll(listVoluntarios);
			jrDataSource = new JRBeanCollectionDataSource(listVoluntarios);
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
			if (!estatusVoluntariosS.equals("")) {
				tStatus = "Estatus";
			}
			if (!fortalezasP.equals("")) {
				tFortalezas = "Fortalezas";
			}
			if (!profesionesP.equals("")) {
				tProfesiones = "Profesiones";
			}
			if (!voluntarioClasificadoP.equals("")) {
				tVoluntarioClasificado = "Clasificacion de Voluntarios";
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

			parametros.put("tEstatus", tStatus);

			parametros.put("tFortalezas", tFortalezas);

			parametros.put("tProfesiones", tProfesiones);

			parametros.put("tVoluntarioClasificado", tVoluntarioClasificado);

			parametros.put("estatusVoluntariosS", estatusVoluntariosS);

			parametros.put("fortalezasP", fortalezasP);

			parametros.put("profesionesP", profesionesP);

			parametros.put("voluntarioClasificadoP", voluntarioClasificadoP);
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos().getObjetos().get(0);
			
			parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
			
			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());
			
			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());
			
			source = "reporte/reportVoluntariosParametrizados.jasper";
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

	public List<Voluntario> getVoluntarios() {
		if (this.voluntarios == null) {
			this.voluntarios = new ArrayList<>();
		}
		return voluntarios;
	}

	public void setVoluntarios(List<Voluntario> voluntarios) {
		this.voluntarios = voluntarios;
	}

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	public String getVoluntarioClasificadoP() {
		return voluntarioClasificadoP;
	}

	public void setVoluntarioClasificadoP(String voluntarioClasificadoP) {
		this.voluntarioClasificadoP = voluntarioClasificadoP;
	}

	public String getEstatusVoluntariosS() {
		return estatusVoluntariosS;
	}

	public void setEstatusVoluntariosS(String estatusVoluntariosS) {
		this.estatusVoluntariosS = estatusVoluntariosS;
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

	public String gettVoluntarioClasificado() {
		return tVoluntarioClasificado;
	}

	public void settVoluntarioClasificado(String tVoluntarioClasificado) {
		this.tVoluntarioClasificado = tVoluntarioClasificado;
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
