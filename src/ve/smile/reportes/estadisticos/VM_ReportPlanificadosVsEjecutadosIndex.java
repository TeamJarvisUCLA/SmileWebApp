package ve.smile.reportes.estadisticos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.bind.BindUtils;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.IndicadorEventoPlanificado;
import ve.smile.dto.Organizacion;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.reportes.Reporte;

public class VM_ReportPlanificadosVsEjecutadosIndex extends VM_WindowWizard {

	private List<IndicadorEventoPlanificado> indicadorEventoPlanificado;

	private JRDataSource jrDataSource;

	private Map<String, Object> parametros = new HashMap<>();

	private String source;

	private String type;

	EventoPlanificado eventoPlanificado = new EventoPlanificado();

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
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/reportes/estadisticos/planificadosVsEjecutados/selectOpcionesPlanificadosVsEjecutados.zul");
		urls.add("views/desktop/reportes/estadisticos/planificadosVsEjecutados/listIndicadoresEventoPlanificado.zul");
		urls.add("views/desktop/reportes/estadisticos/planificadosVsEjecutados/viewReportPlanificadosVsEjecutados.zul");

		return urls;
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
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}

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
			if (currentStep == 1) {
				this.indicadorEventoPlanificado = new ArrayList<IndicadorEventoPlanificado>();
				Map<String, String> criterios = new HashMap<>();
				eventoPlanificado = (EventoPlanificado) selectedObject;
				criterios.put("fkEventoPlanificado.idEventoPlanificado",
						eventoPlanificado.getIdEventoPlanificado() + "");
				PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (payloadIndicadorEventoPlanificadoResponse.getObjetos()
						.size() > 0
						& payloadIndicadorEventoPlanificadoResponse
								.getObjetos() != null) {
					this.indicadorEventoPlanificado
							.addAll(payloadIndicadorEventoPlanificadoResponse
									.getObjetos());
					BindUtils.postNotifyChange(null, null, this,
							"indicadorEventoPlanificado");
				}

			}
			if (!indicadorEventoPlanificado.isEmpty()) {
				jrDataSource = new JRBeanCollectionDataSource(
						indicadorEventoPlanificado);
			}
		}
		if (currentStep == 2) {
			parametros
					.put("titulo",
							"Reporte Estadistico de Eventos Planificados Vs Ejecutados");
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

			parametros.put("pIndicador", "Indicador");

			parametros.put("pUnidadDeMedida", "Unidad de Medida");

			parametros.put("pValorEsperado", "Valor Esperado");

			parametros.put("pValorReal", "Valor Real");

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

			Organizacion organizacion = S.OrganizacionService.consultarTodos()
					.getObjetos().get(0);

			parametros.put("tDireccionOrganizacion",
					organizacion.getDireccion());

			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono()
					+ " " + "/" + " " + organizacion.getTelefono2());

			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());

			type = "pdf";

			source = "reporte/estadisticoPlanificadosVsEjecutados.jasper";
		}
		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {

		return "";

	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusEvento", String
				.valueOf(EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal()));
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadEventoPlanificadoResponse;
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {

		return "";
	}

	public List<IndicadorEventoPlanificado> getIndicadorEventoPlanificado() {
		return indicadorEventoPlanificado;
	}

	public void setIndicadorEventoPlanificado(
			List<IndicadorEventoPlanificado> indicadorEventoPlanificado) {
		this.indicadorEventoPlanificado = indicadorEventoPlanificado;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
