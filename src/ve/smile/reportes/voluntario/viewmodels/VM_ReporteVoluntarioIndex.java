package ve.smile.reportes.voluntario.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Profesion;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.request.PayloadVoluntarioRequest;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.reporte.Reporte;

public class VM_ReporteVoluntarioIndex extends VM_WindowWizard {

	private boolean clasificadorVoluntario = false;

	private boolean profesionVoluntario = false;

	private boolean fortalezaVoluntario = false;

	private boolean redesSociales = false;

	private boolean fechaIngreso = false;

	private boolean postulado = false;

	private boolean porCompletar = false;

	private boolean activo = false;

	private boolean egresado = false;
	
	private String type;
	
	private String source;
	
	private Map<String, Object> parametros =  new HashMap<>();

	private List<ClasificadorVoluntario> listClasificadorVoluntario = new ArrayList<>();

	private Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados;

	private List<Fortaleza> listFortalezas = new ArrayList<>();

	private List<String> listRedesSociales = new ArrayList<>();

	private Set<String> redesSeleccionadas;

	private Set<Fortaleza> fortalezasSeleccionados;

	private List<Profesion> listProfesion = new ArrayList<>();

	private Set<Profesion> profesionesSeleccionadas;

	public Set<ClasificadorVoluntario> getClasificadorVoluntariosSeleccionados() {
		return clasificadorVoluntariosSeleccionados;
	}

	public void setClasificadorVoluntariosSeleccionados(
			Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados) {

		this.clasificadorVoluntariosSeleccionados = clasificadorVoluntariosSeleccionados;

	}

	@Init(superclass = true)
	public void childInit() {

		listClasificadorVoluntario = S.ClasificadorVoluntarioService
				.consultarTodos().getObjetos();

		listFortalezas = S.FortalezaService.consultarTodos().getObjetos();

		listProfesion = S.ProfesionService.consultarTodos().getObjetos();

		llenarListRedes();

	}

	public void llenarListRedes() {
		listRedesSociales.add("Facebook");
		listRedesSociales.add("Instagram");
		listRedesSociales.add("Twitter");
		listRedesSociales.add("Linkedin");
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

	public boolean isRedesSociales() {
		return redesSociales;
	}

	public void setRedesSociales(boolean redesSociales) {
		this.redesSociales = redesSociales;
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
		return listClasificadorVoluntario;
	}

	public void setListClasificadorVoluntario(
			List<ClasificadorVoluntario> listClasificadorVoluntario) {
		this.listClasificadorVoluntario = listClasificadorVoluntario;
	}

	public List<Fortaleza> getListFortalezas() {
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

	public List<String> getListRedesSociales() {
		return listRedesSociales;
	}

	public void setListRedesSociales(List<String> listRedesSociales) {
		this.listRedesSociales = listRedesSociales;
	}

	public Set<String> getRedesSeleccionadas() {
		return redesSeleccionadas;
	}

	public void setRedesSeleccionadas(Set<String> redesSeleccionadas) {
		this.redesSeleccionadas = redesSeleccionadas;
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

		urls.add("views/desktop/reporte/voluntario/selectOpcionesReporteVoluntario.zul");
		urls.add("views/desktop/reporte/voluntario/selectCompletado.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroTrabajoSocialPlanificado.zul");

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
		if (currentStep == 2) {
		}

		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 1) {
			goToNextStep();
		}
		return "";
	}

	@Override
	public String executeCustom2(Integer currentStep) {
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1(Integer currentStep) {
		if (currentStep == 1) {
			String sql = "SELECT v FROM Voluntario v  WHERE  v.idVoluntario = v.idVoluntario ";

			if (porCompletar || postulado || egresado || activo) {
				String estatusVoluntarios = "";
				if(porCompletar){
					estatusVoluntarios += EstatusVoluntarioEnum.POR_COMPLETAR.ordinal()+",";
				}
				if(postulado){
					estatusVoluntarios += EstatusVoluntarioEnum.POSTULADO.ordinal()+",";
				}
				if(egresado){
					estatusVoluntarios += EstatusVoluntarioEnum.INACTIVO.ordinal()+",";
				}
				if(activo){
					estatusVoluntarios += EstatusVoluntarioEnum.ACTIVO.ordinal()+",";
				}
				int tamano = estatusVoluntarios.length();
				
				char[] tmp = estatusVoluntarios.toCharArray();
				
				tmp[tamano -1] = ' ';
				
				estatusVoluntarios = new String(tmp);

				sql += "and v.estatusVoluntario in("+estatusVoluntarios+")";
			}
			if (profesionVoluntario) {
				if (profesionesSeleccionadas != null) {
					String profesiones = "";
					int i = 0;

					for (Profesion profesion : profesionesSeleccionadas) {
						i++;
						profesiones += profesion.getIdProfesion();

						if (i != profesionesSeleccionadas.size()) {
							profesiones += ",";
						}
					}
					sql = sql.replace("WHERE", ", VoluntarioProfesion pv WHERE");
					sql += " and pv.fkVoluntario.idVoluntario = v.idVoluntario and pv.fkProfesion.idProfesion in ("
							+ profesiones + ")";
				}
			}
			if (fortalezaVoluntario) {
				if (fortalezasSeleccionados != null) {
					String fortalezas = "";
					int i = 0;

					for (Fortaleza fortaleza : fortalezasSeleccionados) {
						i++;
						fortalezas += fortaleza.getIdFortaleza();

						if (i != fortalezasSeleccionados.size()) {
							fortalezas += ",";
						}
					}
					sql = sql.replace("WHERE", ", VoluntarioFortaleza vf WHERE");
					sql += " and vf.fkVoluntario.idVoluntario = v.idVoluntario and vf.fkFortaleza.idFortaleza in ("
							+ fortalezas + ")";
				}

			}
			if (clasificadorVoluntario) {
				if (clasificadorVoluntariosSeleccionados != null) {
					String voluntarioClasificado = "";
					int i = 0;

					for ( ClasificadorVoluntario clasificadorVoluntario : clasificadorVoluntariosSeleccionados) {
						i++;
						voluntarioClasificado += clasificadorVoluntario.getIdClasificadorVoluntario();

						if (i != clasificadorVoluntariosSeleccionados.size()) {
							voluntarioClasificado += ",";
						}
					}
					sql = sql.replace("WHERE", ", VoluntarioClasificado vc WHERE");
					sql += " and vc.fkVoluntario.idVoluntario = v.idVoluntario and vc.fkClasificadorVoluntario.idClasificadorVoluntario in ("
							+ voluntarioClasificado + ")";
				}

			}
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultaVoluntariosParametrizado(sql);
			List<Voluntario> listVoluntarios = payloadVoluntarioResponse.getObjetos();
			System.out.println(sql);
		}
		System.out.println(currentStep);
		if(currentStep==2){
			parametros.put("SUBREPORT_DIR", Reporte.class.getResource("reportDetalleVoluntarioParametrizado.jasper").getPath().replace("reportDetalleVoluntarioParametrizado.jasper", ""));
			source = "reporte/reportVoluntariosParametrizados.jasper";
			Executions.getCurrent().sendRedirect("reporteVoluntariosParametrizados.zul", "_blank");
		}

		return "";
	}

	@Override
	public String isValidPreconditionsCustom2(Integer currentStep) {
		System.out.println("algo paso por aqui pendiente");
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
	
}
