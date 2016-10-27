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
import ve.smile.dto.IndicadorTsPlan;
import ve.smile.dto.Organizacion;
import ve.smile.dto.TsPlan;
import ve.smile.payload.response.PayloadIndicadorTsPlanResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ReportTrabajoSocialPlanificadosVsEjecutadosIndex extends VM_WindowWizard {

	private List<IndicadorTsPlan> indicadorTrabajoSocialPlanificado;
	
	private JRDataSource jrDataSource;
	
	private Map<String, Object> parametros = new HashMap<>();
	
	private String source;
	
	private String type;
	
	TsPlan tsPlan = new TsPlan();

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

		urls.add("views/desktop/reportes/estadisticos/trabajoSocialPlanificadosVsEjecutados/selectOpcionesPlanificadosVsEjecutados.zul");
		urls.add("views/desktop/reportes/estadisticos/trabajoSocialPlanificadosVsEjecutados/listIndicadoresTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/reportes/estadisticos/trabajoSocialPlanificadosVsEjecutados/viewReportPlanificadosVsEjecutados.zul");

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
				return "E:Error Code 5-Debe seleccionar un <b>TrabajoSocial Planificado</b>";
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
			parametros.clear();
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
				this.indicadorTrabajoSocialPlanificado = new ArrayList<IndicadorTsPlan>();
				Map<String, String> criterios = new HashMap<>();
				tsPlan = (TsPlan) selectedObject;

				criterios.put("fkTsPlan.idTsPlan",
						tsPlan.getIdTsPlan() + "");
				PayloadIndicadorTsPlanResponse payloadIndicadorTsPlanResponse = S.IndicadorTsPlanService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (payloadIndicadorTsPlanResponse.getObjetos().size() > 0 & payloadIndicadorTsPlanResponse.getObjetos() != null) {
					for (IndicadorTsPlan indicaTrbPlanificado : payloadIndicadorTsPlanResponse
							.getObjetos()) {
						IndicadorTsPlan indTrbPla = new IndicadorTsPlan();
						indTrbPla
								.setFkTsPlan(indicaTrbPlanificado
										.getFkTsPlan());
						indTrbPla.setFkIndicador(indicaTrbPlanificado
								.getFkIndicador());
						indTrbPla
								.setIdIndicadorTsPlan(indicaTrbPlanificado
										.getIdIndicadorTsPlan());
						indTrbPla.setValorEsperado(indicaTrbPlanificado
								.getValorEsperado());
						indTrbPla.setValorReal(indicaTrbPlanificado
								.getValorEsperado());
						this.indicadorTrabajoSocialPlanificado.add(indTrbPla);
					}
					BindUtils.postNotifyChange(null, null, this,
							"indicadorTrabajoSocialPlanificado");
				}

			}
			if(!indicadorTrabajoSocialPlanificado.isEmpty()){
				jrDataSource = new JRBeanCollectionDataSource(indicadorTrabajoSocialPlanificado);
			}
		}
		if (currentStep == 2) {
			parametros.put("titulo", "Reporte Estadistico de TrabajoSociales Planificados Vs Ejecutados");
			
			parametros.put("pIndicador", "Indicador");
			parametros.put("pUnidadDeMedida", "Unidad de Medida");
			parametros.put("pValorEsperado", "Valor Esperado");
			parametros.put("pValorReal", "Valor Real");
			parametros.put("pFecha", tsPlan.getFechaPlanificada());	
			parametros.put("pNombreTrabajoSocial", tsPlan.getFkTrabajoSocial()
					.getNombre());

			parametros.put("pDescripcion", tsPlan.getFkTrabajoSocial()
					.getDescripcion());

			parametros.put("pLugar", tsPlan.getFkDirectorio()
					.getNombre());

			parametros.put("pDireccion", tsPlan.getFkDirectorio()
					.getDireccion());

			parametros.put("pResponsable", tsPlan.getFkPersona()
					.getNombre()
					+ " "
					+ tsPlan.getFkPersona().getApellido());
			
			
			Organizacion organizacion = S.OrganizacionService.consultarTodos()
					.getObjetos().get(0);
			parametros.put("tDireccionOrganizacion",
					organizacion.getDireccion());

			parametros.put("tTelefonoOrganizacion", organizacion.getTelefono()
					+ " " + "/" + " " + organizacion.getTelefono2());

			parametros.put("tCorreoOrganizacion", organizacion.getCorreo());

			type = "pdf";
			source = "reporte/estadisticoTrabajoSocialPlanificadosVsEjecutados.jasper";
			
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		System.out.println("algo paso por aqui pendiente");
		return "";
	}

	@Override
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTsPlanResponse;
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {

		return "";
	}



	public List<IndicadorTsPlan> getIndicadorTrabajoSocialPlanificado() {
		return indicadorTrabajoSocialPlanificado;
	}

	public void setIndicadorTrabajoSocialPlanificado(
			List<IndicadorTsPlan> indicadorTrabajoSocialPlanificado) {
		this.indicadorTrabajoSocialPlanificado = indicadorTrabajoSocialPlanificado;
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
