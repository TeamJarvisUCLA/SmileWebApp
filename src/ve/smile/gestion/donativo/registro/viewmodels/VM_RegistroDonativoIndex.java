package ve.smile.gestion.donativo.registro.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Colaborador;
import ve.smile.dto.Directorio;
import ve.smile.dto.DonativoCuentaBancaria;
import ve.smile.dto.DonativoRecurso;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Padrino;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Recurso;
import ve.smile.dto.TsPlan;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.RecepcionEnum;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_RegistroDonativoIndex extends VM_WindowWizard {

	private DonativoRecurso donativoRecurso;
	private DonativoCuentaBancaria donativoCuentaBancaria;

	private List<ProcedenciaEnum> tipoProcedenciaEnums;
	private ProcedenciaEnum procedenciaEnums;

	private List<RecepcionEnum> recepcionEnums;
	private RecepcionEnum recepcionEnum;

	private EventoPlanificado eventoPlanificado;
	private TsPlan tsPlan;
	private Padrino padrino;
	private Colaborador colaborador;
	private Patrocinador patrocinador;
	private Directorio directorio;

	private boolean cuentaBancaria;

	private List<EventoPlanificado> eventoPlanificados;

	private List<TsPlan> tsPlans;
	private List<Padrino> padrinos;
	private List<Colaborador> colaboradores;
	private List<Patrocinador> patrocinadores;

	private String srcList;
	private List<Recurso> recursos;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		donativoRecurso = new DonativoRecurso();
		donativoCuentaBancaria = new DonativoCuentaBancaria();
		tsPlan = new TsPlan();
		eventoPlanificado = new EventoPlanificado();
		padrino = new Padrino();
		colaborador = new Colaborador();
		patrocinador = new Patrocinador();
		directorio = new Directorio();
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
	}

	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setPatrocindor(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}

	public Patrocinador getPatrocinador() {
		return patrocinador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
	}

	public Padrino getPadrino() {
		return padrino;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public ProcedenciaEnum getProcedenciaEnums() {
		return procedenciaEnums;
	}

	public void setProcedenciaEnums(ProcedenciaEnum procedenciaEnums) {
		this.procedenciaEnums = procedenciaEnums;
		this.getDonativoRecurso().setProcedencia(procedenciaEnums.ordinal());
	}

	public DonativoRecurso getDonativoRecurso() {
		return donativoRecurso;
	}

	public void setDonativoRecurso(DonativoRecurso donativoRecurso) {
		this.donativoRecurso = donativoRecurso;
	}

	public DonativoCuentaBancaria getDonativoCuentaBancaria() {
		return donativoCuentaBancaria;
	}

	public void setDonativoCuentaBancaria(
			DonativoCuentaBancaria donativoCuentaBancaria) {
		this.donativoCuentaBancaria = donativoCuentaBancaria;
	}

	public List<ProcedenciaEnum> getTipoProcedenciaEnums() {
		if (this.tipoProcedenciaEnums == null) {
			this.tipoProcedenciaEnums = new ArrayList<>();
		}
		if (this.tipoProcedenciaEnums.isEmpty()) {
			for (ProcedenciaEnum procedenciaEnums : ProcedenciaEnum.values()) {
				this.tipoProcedenciaEnums.add(procedenciaEnums);
			}
		}
		return tipoProcedenciaEnums;
	}

	public void setTipoProcedenciaEnums(
			List<ProcedenciaEnum> tipoProcedenciaEnums) {
		this.tipoProcedenciaEnums = tipoProcedenciaEnums;
	}

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart"); // seleccionar Procedencia
		iconos.add("fa fa-pencil-square-o"); // seleccionar objeto de la
												// Procedencia
		iconos.add("fa fa-pencil-square-o"); // formulario de registro
		iconos.add("fa fa-check-square-o"); // registro finalizado

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/donativo/registro/selectProcedencia.zul");
		urls.add("views/desktop/gestion/donativo/registro/selectList.zul");
		urls.add("views/desktop/gestion/donativo/registro/RegistroDonativoFormBasic.zul");
		urls.add("views/desktop/gestion/donativo/registro/registroCompletado.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (procedenciaEnums.equals(ProcedenciaEnum.COLABORADOR)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectColaborador.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.EVENTO)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectEventoPlanificado.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectPadrino.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PATROCINADOR)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectPatrocinador.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.TRABAJO_SOCIAL)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectTrabajoSocialPlanificado.zul");
			}
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<DonativoRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDonativoRecursoResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (procedenciaEnums == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Tipo de Procedencia</b>";
			}
		}

		if (currentStep == 2) {
			if (procedenciaEnums.equals(ProcedenciaEnum.COLABORADOR)
					&& colaborador.getIdColaborador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Colaborador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.EVENTO)
					&& eventoPlanificado.getIdEventoPlanificado() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)
					&& padrino.getIdPadrino() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PATROCINADOR)
					&& patrocinador.getIdPatrocinador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Patrocinador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.TRABAJO_SOCIAL)
					&& tsPlan.getIdTsPlan() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificador</b>";
			}

		}

		if (currentStep == 3) {
			return "E:Error Code 5-Debe llenar todos los campos";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
		}

		return "";
	}

	public String getSrcList() {
		return srcList;
	}

	public void setSrcList(String srcList) {
		this.srcList = srcList;
	}

	public List<EventoPlanificado> getEventoPlanificados() {
		if (this.eventoPlanificados == null) {
			this.eventoPlanificados = new ArrayList<>();
		}
		if (this.eventoPlanificados.isEmpty()) {
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				this.eventoPlanificados.addAll(payloadEventoPlanificadoResponse
						.getObjetos());
			}

		}
		return eventoPlanificados;
	}

	public void setEventoPlanificados(List<EventoPlanificado> eventoPlanificados) {
		this.eventoPlanificados = eventoPlanificados;
	}

	public List<TsPlan> getTsPlans() {
		if (this.tsPlans == null) {
			this.tsPlans = new ArrayList<>();
		}
		if (this.tsPlans.isEmpty()) {
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultarTodos();
			this.tsPlans.addAll(payloadTsPlanResponse.getObjetos());
		}
		return tsPlans;
	}

	public void setTsPlans(List<TsPlan> tsPlans) {
		this.tsPlans = tsPlans;
	}

	public List<Padrino> getPadrinos() {
		if (this.padrinos == null) {
			this.padrinos = new ArrayList<>();
		}
		if (this.padrinos.isEmpty()) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				this.padrinos.addAll(payloadPadrinoResponse.getObjetos());
			}
		}

		return padrinos;
	}

	public void setPadrinos(List<Padrino> padrinos) {
		this.padrinos = padrinos;
	}

	public List<Colaborador> getColaboradores() {
		if (this.colaboradores == null) {
			this.colaboradores = new ArrayList<>();
		}
		if (this.colaboradores.isEmpty()) {
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.consultarTodos();
			if (UtilPayload.isOK(payloadColaboradorResponse)) {
				this.colaboradores.addAll(payloadColaboradorResponse
						.getObjetos());
			}
		}
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public List<Patrocinador> getPatrocinadores() {
		if (this.patrocinadores == null) {
			this.patrocinadores = new ArrayList<>();
		}
		if (this.patrocinadores.isEmpty()) {

			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.consultarTodos();
			if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
				this.patrocinadores.addAll(payloadPatrocinadorResponse
						.getObjetos());
			}
		}

		return patrocinadores;
	}

	public void setPatrocinadores(List<Patrocinador> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public void setPatrocinador(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}

	public List<RecepcionEnum> getRecepcionEnums() {
		if (this.recepcionEnums == null) {
			this.recepcionEnums = new ArrayList<>();
		}
		if (this.recepcionEnums.isEmpty()) {
			for (RecepcionEnum recepcionEnum : RecepcionEnum.values()) {
				this.recepcionEnums.add(recepcionEnum);
			}
		}
		return recepcionEnums;
	}

	public void setRecepcionEnums(List<RecepcionEnum> recepcionEnums) {
		this.recepcionEnums = recepcionEnums;
	}

	public RecepcionEnum getRecepcionEnum() {
		return recepcionEnum;
	}

	public void setRecepcionEnum(RecepcionEnum recepcionEnum) {
		this.recepcionEnum = recepcionEnum;
		if (recepcionEnum.equals(RecepcionEnum.TRANSACCION_BANCARIA)) {
			setCuentaBancaria(true);
		}
		if (recepcionEnum.equals(RecepcionEnum.OTRO)) {
			setCuentaBancaria(true);
		}
	}

	public boolean isCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(boolean cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public List<Recurso> getRecursos() {
		if (this.recursos == null) {
			recursos = new ArrayList<>();
		}
		if (this.recursos.isEmpty()) {
			PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadRecursoResponse)) {
				recursos.addAll(payloadRecursoResponse.getObjetos());
			}

		}
		return recursos;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}

}
