package ve.smile.reportes.apadrinamiento.viewmodels;

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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.reportes.Reporte;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;

public class VM_ReporteApadrinamientoIndex extends VM_WindowWizard {

	private boolean frecuenciaAporte =false;
	
	private boolean montoAporte = false;
	
	private double aporteDesdeMonto;
	private double aporteHastaMonto;
	
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
	
	private List<FrecuenciaAporte> listFrecuenciaAporte = new ArrayList<>();
	
	private Set<FrecuenciaAporte> frecuenciaAporteSeleccionados;
	
	private List<Padrino> padrinos;
	
	String aporteSeleccionadoP;
	
	String estatusPadrinosS ="";
	
	String tStatus="";
	String tAporteSeleccionado= "";
	
	String tAporteDesde = "";

	String tAporteHasta = "";
	
	String tFrecuenciaAportes;
	
	Double aporteDesde = 0.00;

	Double aporteHasta = 0.00;
	
	@Init(superclass = true)
	public void childInit(){
		parametros.clear();
	}
	
		
	public boolean isFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(boolean fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public boolean isFrecuenciaAporte() {
		return frecuenciaAporte;
	}

	public void setFrecuenciaAporte(boolean frecuenciaAporte) {
		this.frecuenciaAporte = frecuenciaAporte;
	}

	public boolean isPostulado() {
		return postulado;
	}

	public void setPostulado(boolean postulado) {
		this.postulado = postulado;
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

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	
	public boolean isMontoAporte() {
		return montoAporte;
	}


	public void setMontoAporte(boolean montoAporte) {
		this.montoAporte = montoAporte;
	}


	public List<FrecuenciaAporte> getListFrecuenciaAporte() {
		if (listFrecuenciaAporte == null){
			listFrecuenciaAporte = new ArrayList<>();
		}
		if (listFrecuenciaAporte.isEmpty()) {
			PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService.consultarTodos();
			if (UtilPayload.isOK(payloadFrecuenciaAporteResponse)) {
				listFrecuenciaAporte = payloadFrecuenciaAporteResponse.getObjetos();
			}
		}
		return listFrecuenciaAporte;
	}


	public void setListFrecuenciaAporte(List<FrecuenciaAporte> listFrecuenciaAporte) {
		this.listFrecuenciaAporte = listFrecuenciaAporte;
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

		urls.add("views/desktop/reportes/apadrinamiento/selectOpcionesReportePadrino.zul");
		urls.add("views/desktop/reportes/apadrinamiento/selectCompletado.zul");
		urls.add("views/desktop/reportes/apadrinamiento/viewReportPdfPadrino.zul");

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
		frecuenciaAporte =false;
		montoAporte = false;
		aporteDesdeMonto= 0.00;
		aporteHastaMonto = 0.00;
		fechaIngreso = false;
		postulado = false;
		porCompletar = false;
		activo = false;
		egresado = false;		
		todos = false;
		fechaDesdeDate = null;
		fechaHastaDate= null;		
		type = new String();
		source = new String();
		frecuenciaAporteSeleccionados = new HashSet<>();
		listFrecuenciaAporte = new ArrayList<>();
		padrinos = new ArrayList<>();
		tStatus="";
		tAporteDesde = "";
	    tAporteHasta = "";
		tFrecuenciaAportes= "";
		aporteDesde = 0.00;
		aporteHasta = 0.00;
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
				HSSFSheet workSheet = workBook.createSheet("PADRINOS");
				Row excelRow = null;
				Cell excelCell = null;
				int rowNumber = 0;
				excelRow = workSheet.createRow(rowNumber++);

				excelCell = excelRow.createCell(4);
				excelCell.setCellValue("PADRINOS");
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
				excelCell.setCellValue("FRECUENCIA APORTE");
				excelCell = excelRow.createCell(7);
				excelCell.setCellValue("MONTO");
				excelCell = excelRow.createCell(8);
				excelCell.setCellValue("ESTATUS");

				for (Padrino padrino : this.getPadrinos()) {

					excelRow = workSheet.createRow(rowNumber++);
					excelCell = excelRow.createCell(0);
					excelCell.setCellValue(padrino.getFkPersona()
							.getIdentificacion());

					excelCell = excelRow.createCell(1);
					excelCell.setCellValue(padrino.getFkPersona()
							.getNombre());

					excelCell = excelRow.createCell(2);
					excelCell.setCellValue(padrino.getFkPersona()
							.getApellido());

					excelCell = excelRow.createCell(3);
					excelCell.setCellValue(padrino.getFkPersona()
							.getCorreo());

					excelCell = excelRow.createCell(4);
					excelCell.setCellValue(padrino.getFkPersona()
							.getDireccion());

					excelCell = excelRow.createCell(5);
					excelCell.setCellValue(padrino.getFkPersona()
							.getTelefono1());
					
					excelCell = excelRow.createCell(6);
					excelCell.setCellValue(padrino.getFkFrecuenciaAporte()
							.getNombre());
					
					excelCell = excelRow.createCell(7);
					excelCell.setCellValue(padrino.getMonto());


					if (padrino.getEstatusPadrino() != null) {
						excelCell = excelRow.createCell(8);
						excelCell
								.setCellValue(padrino
										.getEstatusPadrinoEnum().toString());
					}
					
				}
				File tempFile = new File("C:\\Smile\\Padrinos.xls");
						
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
			this.setPadrinos(new ArrayList<Padrino>());
			String sql = "";

			if (todos) {
				sql = "SELECT DISTINCT v FROM Padrino v  WHERE  v.idPadrino = v.idPadrino ";

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
				System.out.println(sql);
			}

			if (!todos) {
				sql = "SELECT DISTINCT v FROM Padrino v  WHERE  v.idPadrino = v.idPadrino ";

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
						System.out.println(sql);
					}
				}

				if (porCompletar || postulado || egresado || activo) {
					String estatusPadrinos = "";

					if (porCompletar) {
						estatusPadrinos += EstatusPadrinoEnum.POR_COMPLETAR
								.ordinal() + ",";
						estatusPadrinosS += EstatusPadrinoEnum.POR_COMPLETAR
								.toString() + " ";
					}

					if (postulado) {
						estatusPadrinos += EstatusPadrinoEnum.POSTULADO
								.ordinal() + ",";
						estatusPadrinosS += EstatusPadrinoEnum.POSTULADO
								.toString() + " ";
					}

					if (egresado) {
						estatusPadrinos += EstatusPadrinoEnum.INACTIVO
								.ordinal() + ",";
						estatusPadrinosS += EstatusPadrinoEnum.INACTIVO
								.toString() + " ";
					}

					if (activo) {
						estatusPadrinos += EstatusPadrinoEnum.ACTIVO
								.ordinal() + ",";
						estatusPadrinosS += EstatusPadrinoEnum.ACTIVO
								.toString() + " ";
					}
					

					int tamano = estatusPadrinos.length();

					char[] tmp = estatusPadrinos.toCharArray();

					tmp[tamano - 1] = ' ';

					estatusPadrinos = new String(tmp);

					sql += "and v.estatusPadrino in(" + estatusPadrinos
							+ ")";
				}

			}
			
			if (frecuenciaAporte) {

				if (frecuenciaAporteSeleccionados != null) {
					String aporteSeleccionado = "";
					int i = 0;

					for (FrecuenciaAporte frecuenciaAporte : frecuenciaAporteSeleccionados) {
						i++;
						aporteSeleccionadoP += frecuenciaAporte
								.getNombre() + "," + " ";
						aporteSeleccionado += frecuenciaAporte
								.getIdFrecuenciaAporte();

						if (i != frecuenciaAporteSeleccionados.size()) {
							aporteSeleccionado += ",";
						}
					}
					sql = sql.replace("WHERE",
							", FrecuenciaAporte vc WHERE");
					sql += " and vc.idFrecuenciaAporte = v.fkFrecuenciaAporte and vc.idFrecuenciaAporte in ("
							+ aporteSeleccionado + ")";
				}

			}
			
			if (montoAporte){
				
				if (aporteDesde == 0 && aporteHasta == 0) {
					return "E:Error Code 5-No se han ingresado montos para la b�squeda";
				} else if (aporteDesde == 0) {
					return "E:Error Code 5-No se ha ingresado un <b>Monto Desde</b> como Parametro ";
				} else if (aporteHasta == 0) {
					return "E:Error Code 5-No se ha ingresado un <b>Monto Hasta</b> como Parametro ";
				} else if (aporteDesde >= aporteHasta){
					return "E:Error Code 5-No se puede ingresar un <b>Monto en Desde</b>  mayor al <b>Monto en Hasta</b> ";
				}else {
					sql += " and v.monto >= "
							+ aporteDesde
							+ " and v.monto <= "
							+ aporteHasta + " ";
					System.out.println(sql);
				}
				
				
			}
			if (sql.equals("SELECT DISTINCT v FROM Padrino v  WHERE  v.idPadrino = v.idPadrino ")
					&& !todos) {

				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Padrinos</b>";

			}
			System.out.println(sql);
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.consultaPadrinoParametrizado(sql);
			List<Padrino> listPadrinos = payloadPadrinoResponse
					.getObjetos();
			
			this.getPadrinos().addAll(listPadrinos);

			
			if (listPadrinos.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan informaci�n para <b>Padrinos</b>";
			}

			jrDataSource = new JRBeanCollectionDataSource(listPadrinos);
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
			if (!estatusPadrinosS.equals("")) {
				tStatus = "Estatus";
			}
			/*
			if (!fortalezasP.equals("")) {
				tFortalezas = "Fortalezas";
			}
			if (!profesionesP.equals("")) {
				tProfesiones = "Profesiones";
			}
			*/
			
			/*
			if (!aporteSeleccionadoP.equals("")) {
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

			parametros.put("titulo", "PADRINOS");

			parametros.put("tEstatus", tStatus);

	//		parametros.put("tFortalezas", tFortalezas);

	//		parametros.put("tProfesiones", tProfesiones);

			parametros.put("tAporteSeleccionado", tAporteSeleccionado);

			parametros.put("estatusPadrinosS", estatusPadrinosS);

		//	parametros.put("fortalezasP", fortalezasP);

		//	parametros.put("profesionesP", profesionesP);

			parametros.put("aporteSeleccionadoP", aporteSeleccionadoP);

			source = "reporte/reportPadrinosParametrizados.jasper";
			
		
		}

		return "";
	}
	
	public double getAporteDesdeMonto() {
		return aporteDesdeMonto;
	}


	public void setAporteDesdeMonto(double aporteDesdeMonto) {
		this.aporteDesdeMonto = aporteDesdeMonto;
	}


	public double getAporteHastaMonto() {
		return aporteHastaMonto;
	}


	public void setAporteHastaMonto(double aporteHastaMonto) {
		this.aporteHastaMonto = aporteHastaMonto;
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


	public Set<FrecuenciaAporte> getFrecuenciaAporteSeleccionados() {
		return frecuenciaAporteSeleccionados;
	}


	public void setFrecuenciaAporteSeleccionados(
			Set<FrecuenciaAporte> frecuenciaAporteSeleccionados) {
		this.frecuenciaAporteSeleccionados = frecuenciaAporteSeleccionados;
	}


	public List<Padrino> getPadrinos() {
		if(this.padrinos == null) {
			this.padrinos = new ArrayList<>();
		}
		return padrinos;
	}


	public void setPadrinos(List<Padrino> padrinos) {
		this.padrinos = padrinos;
	}


	public String gettStatus() {
		return tStatus;
	}


	public void settStatus(String tStatus) {
		this.tStatus = tStatus;
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


	public String gettFrecuenciaAportes() {
		return tFrecuenciaAportes;
	}


	public void settFrecuenciaAportes(String tFrecuenciaAportes) {
		this.tFrecuenciaAportes = tFrecuenciaAportes;
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


	public String getEstatusPadrinosS() {
		return estatusPadrinosS;
	}


	public void setEstatusPadrinosS(String estatusPadrinosS) {
		this.estatusPadrinosS = estatusPadrinosS;
	}


	public String getAporteSeleccionadoP() {
		return aporteSeleccionadoP;
	}


	public void setAporteSeleccionadoP(String aporteSeleccionadoP) {
		this.aporteSeleccionadoP = aporteSeleccionadoP;
	}


	public String gettAporteSeleccionado() {
		return tAporteSeleccionado;
	}


	public void settAporteSeleccionado(String tAporteSeleccionado) {
		this.tAporteSeleccionado = tAporteSeleccionado;
	}	

}