package ve.smile.reportes.reconocimiento.viewmodels;

// import para Excel
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



/*import org.apache.poi.hssf.usermodel.HSSFSheet;
 import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 import org.apache.poi.ss.usermodel.Cell;
 import org.apache.poi.ss.usermodel.Row;*/
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.dto.Organizacion;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.dto.TsPlan;
import ve.smile.enums.TipoReconocimientoEnum;
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.reportes.Reporte;

public class VM_ReporteReconocimientoIndex extends VM_WindowWizard {

	private boolean clasificadorReconocimiento = false;

	private boolean tipoReconocimientoEnum = false;

	private boolean todos = false;

	private String type;

	private String source;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private List<ClasificadorReconocimiento> listClasificadorReconocimiento = new ArrayList<>();

	private Set<ClasificadorReconocimiento> clasificadorReconocimientosSeleccionados;

	private List<ReconocimientoPersona> reconocimientos;

	private List<TipoReconocimientoEnum> listTipoReconocimientoEnums = new ArrayList<>();

	private Set<TipoReconocimientoEnum> tipoReconocimientoEnumsSeleccionados;

	String tReconocimientoClasificado = "";
	String reconocimientoClasificadoP = "";
	String tipoReconocimientoEnumP = "";
	String tTipoReconocimientoEnumP = "";

	public List<TipoReconocimientoEnum> getListTipoReconocimientoEnums() {
		if (this.listTipoReconocimientoEnums == null) {
			this.listTipoReconocimientoEnums = new ArrayList<TipoReconocimientoEnum>();
		}
		if (this.listTipoReconocimientoEnums.isEmpty()) {
			for (TipoReconocimientoEnum tipoReconocimiento : TipoReconocimientoEnum
					.values()) {
				this.listTipoReconocimientoEnums.add(tipoReconocimiento);
			}
		}
		return listTipoReconocimientoEnums;
	}

	public void setListTipoReconocimientoEnums(
			List<TipoReconocimientoEnum> listTipoReconocimientoEnums) {
		this.listTipoReconocimientoEnums = listTipoReconocimientoEnums;
	}

	public Set<TipoReconocimientoEnum> getTipoReconocimientoEnumsSeleccionados() {
		return tipoReconocimientoEnumsSeleccionados;
	}

	public void setTipoReconocimientoEnumsSeleccionados(
			Set<TipoReconocimientoEnum> tipoReconocimientoEnumsSeleccionados) {
		this.tipoReconocimientoEnumsSeleccionados = tipoReconocimientoEnumsSeleccionados;
	}

	public String getTipoReconocimientoEnumP() {
		return tipoReconocimientoEnumP;
	}

	public void setTipoReconocimientoEnumP(String tipoReconocimientoEnumP) {
		this.tipoReconocimientoEnumP = tipoReconocimientoEnumP;
	}

	public String gettTipoReconocimientoEnumP() {
		return tTipoReconocimientoEnumP;
	}

	public void settTipoReconocimientoEnumP(String tTipoReconocimientoEnumP) {
		this.tTipoReconocimientoEnumP = tTipoReconocimientoEnumP;
	}

	public String gettReconocimientoClasificado() {
		return tReconocimientoClasificado;
	}

	public void settReconocimientoClasificado(String tReconocimientoClasificado) {
		this.tReconocimientoClasificado = tReconocimientoClasificado;
	}

	@Init(superclass = true)
	public void childInit() {
		parametros.clear();
	}

	public Set<ClasificadorReconocimiento> getClasificadorReconocimientosSeleccionados() {
		return clasificadorReconocimientosSeleccionados;
	}

	public void setClasificadorReconocimientosSeleccionados(
			Set<ClasificadorReconocimiento> clasificadorReconocimientosSeleccionados) {

		this.clasificadorReconocimientosSeleccionados = clasificadorReconocimientosSeleccionados;

	}

	public boolean isClasificadorReconocimiento() {
		return clasificadorReconocimiento;
	}

	public boolean isTipoReconocimientoEnum() {
		return tipoReconocimientoEnum;
	}

	public void setClasificadorReconocimiento(boolean clasificadorReconocimiento) {
		this.clasificadorReconocimiento = clasificadorReconocimiento;
	}

	public void setTipoReconocimientoEnum(boolean tipoReconocimientoEnum) {
		this.tipoReconocimientoEnum = tipoReconocimientoEnum;
	}

	public String getReconocimientoClasificadoP() {
		return reconocimientoClasificadoP;
	}

	public void setReconocimientoClasificadoP(String reconocimientoClasificadoP) {
		this.reconocimientoClasificadoP = reconocimientoClasificadoP;
	}

	public List<ClasificadorReconocimiento> getListClasificadorReconocimiento() {
		if (listClasificadorReconocimiento == null) {
			listClasificadorReconocimiento = new ArrayList<>();
		}
		if (listClasificadorReconocimiento.isEmpty()) {
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
				listClasificadorReconocimiento = payloadClasificadorReconocimientoResponse
						.getObjetos();
			}
		}
		return listClasificadorReconocimiento;

	}

	public void setListClasificadorReconocimiento(
			List<ClasificadorReconocimiento> listClasificadorReconocimiento) {
		this.listClasificadorReconocimiento = listClasificadorReconocimiento;
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

		urls.add("views/desktop/reportes/reconocimiento/selectOpcionesReporteReconocimiento.zul");
		urls.add("views/desktop/reportes/reconocimiento/selectCompletado.zul");
		urls.add("views/desktop/reportes/reconocimiento/viewReportPdfReconocimiento.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		if (currentStep == 2) {
			this.setReconocimientos(new ArrayList<ReconocimientoPersona>());
		}
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
		clasificadorReconocimiento = false;
		tipoReconocimientoEnum = false;
		todos = false;
		type = new String();
		source = new String();
		listClasificadorReconocimiento = new ArrayList<>();
		clasificadorReconocimientosSeleccionados = new HashSet<>();
		tipoReconocimientoEnumsSeleccionados = new HashSet<>();
		reconocimientos = new ArrayList<>();
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
			File crear_archivo = new File("C:\\Smile\\reconocimientos.csv");
			try {
				crear_archivo.createNewFile();
				FileWriter w = new FileWriter(crear_archivo);
				BufferedWriter bw = new BufferedWriter(w);
				PrintWriter wr = new PrintWriter(bw);

				for (ReconocimientoPersona obj : this.reconocimientos) {

					wr.println(obj.getFkPersona().getIdentificacion()+";"+obj.getFkPersona().getNombre()
							+ ";" + obj.getFkPersona().getApellido() +";"
							+ obj.getContenido() + ";"
							+ obj.getFkClasificadorReconocimiento().getNombre()+ ";"
							+ obj.getFkClasificadorReconocimiento().getDescripcion());
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

			this.setReconocimientos(new ArrayList<ReconocimientoPersona>());
			String sql = "SELECT DISTINCT rp FROM ReconocimientoPersona rp  WHERE rp.idReconocimientoPersona = rp.idReconocimientoPersona ";

			if (!todos) {
				sql = sql.replace("WHERE",
						", ClasificadorReconocimiento cr WHERE");
				sql += " and rp.fkClasificadorReconocimiento = cr.idClasificadorReconocimiento";

				if (clasificadorReconocimiento) {

					if (clasificadorReconocimientosSeleccionados != null) {
						String reconocimientoClasificado = "";
						int i = 0;

						for (ClasificadorReconocimiento clasificadorReconocimiento : clasificadorReconocimientosSeleccionados) {
							i++;
							reconocimientoClasificadoP += clasificadorReconocimiento
									.getNombre() + "," + " ";
							reconocimientoClasificado += clasificadorReconocimiento
									.getIdClasificadorReconocimiento();

							if (i != clasificadorReconocimientosSeleccionados
									.size()) {
								reconocimientoClasificado += ",";
							}
						}

						sql += " and cr.idClasificadorReconocimiento in ("
								+ reconocimientoClasificado + ")";
					}
				}
				if (tipoReconocimientoEnum) {

					if (tipoReconocimientoEnumsSeleccionados != null) {
						String tipoReconocimientoEnumClasificado = "";
						int i = 0;

						for (TipoReconocimientoEnum tipoReconocimientoEnum : tipoReconocimientoEnumsSeleccionados) {
							i++;
							tipoReconocimientoEnumP += tipoReconocimientoEnum
									.name() + "," + " ";
							tipoReconocimientoEnumClasificado += tipoReconocimientoEnum
									.ordinal();

							if (i != tipoReconocimientoEnumsSeleccionados
									.size()) {
								tipoReconocimientoEnumClasificado += ",";
							}
						}

						sql += " and cr.tipoReconocimiento in ("
								+ tipoReconocimientoEnumClasificado + ")";
					}
				}
			}

			if (sql.equals("SELECT DISTINCT rp FROM ReconocimientoPersona rp  WHERE  rp.idReconocimientoPersona = rp.idReconocimientoPersona")
					&& !todos) {
				return "E:Error Code 5-No se han seleccionados criterios para la consulta <b>Reconocimientos</b>";

			}
			PayloadReconocimientoPersonaResponse payloadReconocimientoResponse = S.ReconocimientoPersonaService
					.consultaReconocimientoPersonasParametrizado(sql);
			List<ReconocimientoPersona> listReconocimientos = payloadReconocimientoResponse
					.getObjetos();
			
			if (listReconocimientos.isEmpty()) {
				return "E:Error Code 5-Los criterios seleccionados no aportan información para <b>Reconocimientos</b>";
			}
			this.getReconocimientos().addAll(listReconocimientos);

			jrDataSource = new JRBeanCollectionDataSource(listReconocimientos);
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
			if (!reconocimientoClasificadoP.equals("")) {
				tReconocimientoClasificado = "Clasificacion de Reconocimientos";
			}

			if (!tipoReconocimientoEnumP.equals("")) {
				tTipoReconocimientoEnumP = "Tipo de Reconocimiento";
			}

			parametros.put("titulo", "RECONOCIMIENTOS");

			parametros.put("tipoReconocimientoEnumP", tipoReconocimientoEnumP);

			parametros
					.put("tTipoReconocimientoEnumP", tTipoReconocimientoEnumP);

			parametros.put("tReconocimientoClasificado",
					tReconocimientoClasificado);

			parametros.put("reconocimientoClasificadoP",
					reconocimientoClasificadoP);
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos().getObjetos().get(0);
			
			parametros.put("tDireccionOrganizacion", organizacion.getDireccion());
			
			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono() + " " + "/" + " " + organizacion.getTelefono2());

			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());
			
			source = "reporte/reportReconocimientosParametrizados.jasper";
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

	public List<ReconocimientoPersona> getReconocimientos() {
		if (this.reconocimientos == null) {
			this.reconocimientos = new ArrayList<>();
		}
		return reconocimientos;
	}

	public void setReconocimientos(List<ReconocimientoPersona> reconocimientos) {
		this.reconocimientos = reconocimientos;
	}

	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

}