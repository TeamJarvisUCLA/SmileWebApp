package ve.smile.reportes.donativo.viewmodels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.DonativoRecurso;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Profesion;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.reportes.Reporte;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilConverterDataList;

public class VM_ReporteDonativoIndex extends VM_WindowWizard {
	
	private boolean todos = false;
	private boolean fechaDonativo = false;
	private Date fechaDesdeDate;
	private Date fechaHastaDate;
	
	private boolean procedenciaDonativo;
	String procedenciaDonativoEnum;
	
	private boolean sitioRecepcion;
	String sitioRecepcionEnum;
	
	private boolean aporte;
	
	private boolean montoDonativo;
	
	private String type;
	private String source;
	private JRDataSource jrDataSource;
	private Map<String, Object> parametros = new HashMap<>();
	
	private List<DonativoRecurso> listDonativo = new ArrayList<>();
	private Set<DonativoRecurso> donativoRecursoSeleccionado;
	
	private List<DonativoRecurso> lisDonantes = new ArrayList<>();
	private Set<DonativoRecurso> donanteRecursoSeleccionado;
	private List<DonativoRecurso> donativos;
	String donativoClasificadoP= "";
	String donanteClasificadoP = "";
	
	String tAporteDesde = "";
	String tAporteHasta = "";
	String tProcedencia ="";
	String tRecepcion ="";
	Double aporteDesde = 0.00;
	Double aporteHasta = 0.00;
	


	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public boolean isFechaDonativo() {
		return fechaDonativo;
	}

	public void setFechaDonativo(boolean fechaDonativo) {
		this.fechaDonativo = fechaDonativo;
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

	public boolean isProcedenciaDonativo() {
		return procedenciaDonativo;
	}

	public void setProcedenciaDonativo(boolean procedenciaDonativo) {
		this.procedenciaDonativo = procedenciaDonativo;
	}

	public String getProcedenciaDonativoEnum() {
		return procedenciaDonativoEnum;
	}

	public void setProcedenciaDonativoEnum(String procedenciaDonativoEnum) {
		this.procedenciaDonativoEnum = procedenciaDonativoEnum;
	}

	public boolean isSitioRecepcion() {
		return sitioRecepcion;
	}

	public void setSitioRecepcion(boolean sitioRecepcion) {
		this.sitioRecepcion = sitioRecepcion;
	}

	public String getSitioRecepcionEnum() {
		return sitioRecepcionEnum;
	}

	public void setSitioRecepcionEnum(String sitioRecepcionEnum) {
		this.sitioRecepcionEnum = sitioRecepcionEnum;
	}

	public boolean isAporte() {
		return aporte;
	}

	public void setAporte(boolean aporte) {
		this.aporte = aporte;
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

	public List<DonativoRecurso> getListDonativo() {
		if (listDonativo == null) {
			listDonativo = new ArrayList<>();
		}
		if (listDonativo.isEmpty()) {
			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService.consultarTodos();
			if (UtilPayload.isOK(payloadDonativoRecursoResponse)){
				listDonativo = payloadDonativoRecursoResponse.getObjetos();
			}
		}
		return listDonativo;
	}

	public void setListDonativo(List<DonativoRecurso> listDonativo) {
		this.listDonativo = listDonativo;
	}

	public Set<DonativoRecurso> getDonativoRecursoSeleccionado() {
		return donativoRecursoSeleccionado;
	}

	public void setDonativoRecursoSeleccionado(
			Set<DonativoRecurso> donativoRecursoSeleccionado) {
		this.donativoRecursoSeleccionado = donativoRecursoSeleccionado;
	}

	public String getDonativoClasificadoP() {
		return donativoClasificadoP;
	}

	public void setDonativoClasificadoP(String donativoClasificadoP) {
		this.donativoClasificadoP = donativoClasificadoP;
	}

	public String gettAporteDesde() {
		return tAporteDesde;
	}

	public void settAporteDesde(String tAporteDesde) {
		this.tAporteDesde = tAporteDesde;
	}

	public String gettAporteHasta() {
		return tAporteHasta;
	}

	public void settAporteHasta(String tAporteHasta) {
		this.tAporteHasta = tAporteHasta;
	}

	public String gettProcedencia() {
		return tProcedencia;
	}

	public void settProcedencia(String tProcedencia) {
		this.tProcedencia = tProcedencia;
	}

	public String gettRecepcion() {
		return tRecepcion;
	}

	public void settRecepcion(String tRecepcion) {
		this.tRecepcion = tRecepcion;
	}

	public Double getAporteDesde() {
		return aporteDesde;
	}

	public void setAporteDesde(Double aporteDesde) {
		this.aporteDesde = aporteDesde;
	}

	public Double getAporteHasta() {
		return aporteHasta;
	}

	public void setAporteHasta(Double aporteHasta) {
		this.aporteHasta = aporteHasta;
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
		if (currentStep == 3) {
			parametros.clear();
			clearElements();
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		return "";
	}

	public void clearElements() {
		// TODO Auto-generated method stub
		
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
				HSSFSheet workSheet = workBook.createSheet("DONATIVOS");
				Row excelRow = null;
				Cell excelCell = null;
				int rowNumber = 0;
				excelRow = workSheet.createRow(rowNumber++);

				excelCell = excelRow.createCell(4);
				excelCell.setCellValue("DONATIVOS");
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
				excelCell.setCellValue("DIRECCIÓN");
				excelCell = excelRow.createCell(5);
				excelCell.setCellValue("TELÉFONO");
				excelCell = excelRow.createCell(6);
				excelCell.setCellValue("ESTATUS");

				for (DonativoRecurso donativo : this.getDonativos()) {

					excelRow = workSheet.createRow(rowNumber++);
					excelCell = excelRow.createCell(0);
					excelCell.setCellValue(donativo.getFkPersona()
							.getIdentificacion());

					excelCell = excelRow.createCell(1);
					excelCell.setCellValue(donativo.getFkPersona()
							.getNombre());

					excelCell = excelRow.createCell(2);
					excelCell.setCellValue(donativo.getFkPersona()
							.getApellido());

					excelCell = excelRow.createCell(3);
					excelCell.setCellValue(donativo.getFkPersona()
							.getCorreo());

					excelCell = excelRow.createCell(4);
					excelCell.setCellValue(donativo.getFkPersona()
							.getDireccion());

					excelCell = excelRow.createCell(5);
					excelCell.setCellValue(donativo.getFkPersona()
							.getTelefono1());

					if (donativo.getProcedencia() != null) {
						excelCell = excelRow.createCell(6);
						excelCell
								.setCellValue(donativo
										.getProcedenciaEnum().toString());
					}
					
				}
				File tempFile = new File("C:\\Smile\\Donativos.xls");
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
			this.setDonativos(new ArrayList<DonativoRecurso>());
			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ";

				if (fechaDonativo) {
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
				sql = "SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ";

				if (fechaDonativo) {

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
/*
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
			
			*/
			}
			/*
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
			*/
			if (sql.equals("SELECT DISTINCT v FROM DonativoRecurso v  WHERE  v.idDonativoRecurso = v.idDonativoRecurso ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Voluntarios</b>";

			}
			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
					.consultaDonativosParametrizado(sql);
			List<DonativoRecurso> lisDonativoRecursos = payloadDonativoRecursoResponse
					.getObjetos();
			this.getDonativos().addAll(listDonativo);

			System.out.println(sql);
			if (lisDonativoRecursos.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaciï¿½n para <b>Voluntarios</b>";
			}

			jrDataSource = new JRBeanCollectionDataSource(lisDonativoRecursos);
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
			
			/*
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
*/
			parametros.put("tAporteDesde", tAporteDesde);

			parametros.put("tAporteHasta", tAporteHasta);

			parametros.put("fechaDesde", fechaDesdeDate);

			parametros.put("fechaHasta", fechaHastaDate);

			parametros.put("titulo", "Donativos");

	//		parametros.put("tEstatus", tStatus);

	//		parametros.put("tFortalezas", tFortalezas);

	//		parametros.put("tProfesiones", tProfesiones);

	//		parametros.put("tVoluntarioClasificado", tVoluntarioClasificado);

	//		parametros.put("estatusVoluntariosS", estatusVoluntariosS);

	//		parametros.put("fortalezasP", fortalezasP);

	//		parametros.put("profesionesP", profesionesP);

	//		parametros.put("voluntarioClasificadoP", voluntarioClasificadoP);

			source = "reporte/reportVoluntariosParametrizados.jasper";
		}

		return "";
	}
	

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		return "";
	}

	public boolean isMontoDonativo() {
		return montoDonativo;
	}

	public void setMontoDonativo(boolean montoDonativo) {
		this.montoDonativo = montoDonativo;
	}

	public List<DonativoRecurso> getLisDonantes() {
		if (lisDonantes == null) {
			lisDonantes = new ArrayList<>();
		}
		if (lisDonantes.isEmpty()) {
			Map<String, String> criterios = new HashMap<>();
			ProcedenciaEnum.ANONIMO.ordinal();
		//	ProcedenciaEnum.COLABORADOR.ordinal();
		//	PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService.consultarTodos();
			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService.consultarCriterios(TypeQuery.EQUAL, criterios);
			
			if (UtilPayload.isOK(payloadDonativoRecursoResponse)){
				lisDonantes = payloadDonativoRecursoResponse.getObjetos();
			}
		}
		return lisDonantes;
	}

	public void setLisDonantes(List<DonativoRecurso> lisDonantes) {
		this.lisDonantes = lisDonantes;
	}

	public Set<DonativoRecurso> getDonanteRecursoSeleccionado() {
		return donanteRecursoSeleccionado;
	}

	public void setDonanteRecursoSeleccionado(
			Set<DonativoRecurso> donanteRecursoSeleccionado) {
		this.donanteRecursoSeleccionado = donanteRecursoSeleccionado;
	}

	public String getDonanteClasificadoP() {
		return donanteClasificadoP;
	}

	public void setDonanteClasificadoP(String donanteClasificadoP) {
		this.donanteClasificadoP = donanteClasificadoP;
	}

	public List<DonativoRecurso> getDonativos() {
		return donativos;
	}

	public void setDonativos(List<DonativoRecurso> donativos) {
		this.donativos = donativos;
	}

}
