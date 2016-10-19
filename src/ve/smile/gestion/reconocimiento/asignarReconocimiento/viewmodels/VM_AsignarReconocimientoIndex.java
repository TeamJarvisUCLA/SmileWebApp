package ve.smile.gestion.reconocimiento.asignarReconocimiento.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.dto.Colaborador;
import ve.smile.dto.Padrino;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.dto.Trabajador;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusColaboradorEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.enums.TipoReconocimientoEnum; 
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_AsignarReconocimientoIndex extends VM_WindowWizard{
	
	private List<TipoReconocimientoEnum> tipoReconocimientoEnums;
	private List<ClasificadorReconocimiento> clasificadorReconocimientos;
	TipoReconocimientoEnum tipoReconocimientoEnum;
	private ReconocimientoPersona reconocimientoPersona;
	
	private Padrino padrino;
	private Voluntario voluntario;
	private Colaborador colaborador;
	private Patrocinador patrocinador;
	private Trabajador trabajador;
	
	private List<Padrino> padrinos;
	private List<Voluntario> voluntarios;
	private List<Colaborador> colaboradores;
	private List<Patrocinador> patrocinadores;
	private List<Trabajador> trabajadores;
	
	private String srcList;
	
	@Init(superclass = true)
	public void childInit() {
		padrino = new Padrino();
		voluntario = new Voluntario();
		colaborador = new Colaborador();
		patrocinador = new Patrocinador();
		trabajador = new Trabajador();
	}
	
	
	public TipoReconocimientoEnum getTipoReconocimientoEnum() {
		return tipoReconocimientoEnum;
	}

	public void setTipoReconocimientoEnum(
			TipoReconocimientoEnum tipoReconocimientoEnum) {
		this.tipoReconocimientoEnum = tipoReconocimientoEnum;
	}


	public List<TipoReconocimientoEnum> getTipoReconocimientoEnums() {
		if (this.tipoReconocimientoEnums == null) {
			this.tipoReconocimientoEnums = new ArrayList<>();
		}
		if (this.tipoReconocimientoEnums.isEmpty()) {
			for (TipoReconocimientoEnum tipoReconocimiento : TipoReconocimientoEnum.values()) {
				this.tipoReconocimientoEnums.add(tipoReconocimiento);
			}
		}
		return tipoReconocimientoEnums;
	}
	
	
	public void setTipoReconocimientoEnums(
			List<TipoReconocimientoEnum> tipoReconocimientoEnums) {
		this.tipoReconocimientoEnums = tipoReconocimientoEnums;
	}



	public Padrino getPadrino() {
		return padrino;
	}



	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
	}



	public Voluntario getVoluntario() {
		return voluntario;
	}



	public void setVoluntario(Voluntario voluntario) {
		this.voluntario = voluntario;
	}



	public Colaborador getColaborador() {
		return colaborador;
	}



	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}



	public Patrocinador getPatrocinador() {
		return patrocinador;
	}



	public void setPatrocinador(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}



	public Trabajador getTrabajador() {
		return trabajador;
	}



	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
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
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(4, listOperacionWizard4);

		return botones;
	}
	
	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart"); // seleccionar tipo reconocimiento
		iconos.add("fa fa-pencil-square-o"); // seleccionar persona a reconocer
		iconos.add("fa fa-pencil-square-o"); // formulario de reconocimiento
		iconos.add("fa fa-check-square-o"); // registro finalizado

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectTipoReconocimiento.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectList.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/AsignarReconocimientoFormBasic.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/registroCompletado.zul");
		return urls;
	}
	
	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PADRINO)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectPadrino.zul");
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.VOLUNTARIO)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectVoluntario.zul");
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.COLABORADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectColaborador.zul");
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PATROCINADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectPatrocinador.zul");
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.TRABAJADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectTrabajador.zul");
			}
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		
		if (currentStep == 3) {
			
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PADRINO)) {
				this.getReconocimientoPersona().setFkPersona(this.getPadrino().getFkPersona());
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.VOLUNTARIO)) {
				this.getReconocimientoPersona().setFkPersona(this.getVoluntario().getFkPersona());
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.COLABORADOR)) {
				this.getReconocimientoPersona().setFkPersona(this.getColaborador().getFkPersona());
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PATROCINADOR)) {
				this.getReconocimientoPersona().setFkPersona(this.getPatrocinador().getFkPersona());
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.TRABAJADOR)) {
				this.getReconocimientoPersona().setFkPersona(this.getTrabajador().getFkPersona());
			}
			
			PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService
					.incluir(getReconocimientoPersona());
			if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
				Alert.showMessage(payloadReconocimientoPersonaResponse);
			}			
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
	public IPayloadResponse<Trabajador> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador", String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}
	
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (tipoReconocimientoEnum == null) {
				return "E:Error Code 5-Debe seleccionar un <b> Rol</b>";
			}
		}

		if (currentStep == 2) {
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PADRINO)
					&& padrino.getIdPadrino() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.VOLUNTARIO)
					&& voluntario.getIdVoluntario() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Voluntario</b>";
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.COLABORADOR)
					&& colaborador.getIdColaborador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Colaborador</b>";
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PATROCINADOR)
					&& patrocinador.getIdPatrocinador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Patrocinador</b>";
			}
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.TRABAJADOR)
					&& trabajador.getIdTrabajador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajador</b>";
			}
		}

		if (currentStep == 3) {
			if (getReconocimientoPersona().getFkClasificadorReconocimiento() == null || getReconocimientoPersona().getContenido() == null){
				return "E:Error Code 5-Debe llenar todos los campos";
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		reconocimientoPersona = new ReconocimientoPersona();
		restartWizard();
		return "";
	}

	public String getSrcList() {
		return srcList;
	}

	public void setSrcList(String srcList) {
		this.srcList = srcList;
	}
	
	public List<Patrocinador> getPatrocinadores() {
		if (this.patrocinadores == null) {
			this.patrocinadores = new ArrayList<>();
		}
		if (this.patrocinadores.isEmpty()) {
			
			//TODO: Todo patrocinador que está en tabla es ACTIVO, por tanto no se valida por estatus.
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
	
	public List<Padrino> getPadrinos() {
		if (this.padrinos == null) {
			this.padrinos = new ArrayList<>();
		}
		if (this.padrinos.isEmpty()) {
			
			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusPadrino", String.valueOf(EstatusPadrinoEnum.ACTIVO.ordinal()));
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				this.padrinos.addAll(payloadPadrinoResponse
						.getObjetos());
			}
		}

		return padrinos;
	}

	public void setPadrinos(List<Padrino> padrinos) {
		this.padrinos = padrinos;
	}
	
	public List<Trabajador> getTrabajadores() {
		if (this.trabajadores == null) {
			this.trabajadores = new ArrayList<>();
		}
		if (this.trabajadores.isEmpty()) {
			
			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusTrabajador", String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
			
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadTrabajadorResponse)) {
				this.trabajadores.addAll(payloadTrabajadorResponse
						.getObjetos());
			}
		}

		return trabajadores;
	}
	
	

	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}
	
	public List<Voluntario> getVoluntarios() {
		if (this.voluntarios == null) {
			this.voluntarios = new ArrayList<>();
		}
		if (this.voluntarios.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusVoluntario", String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
			
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadVoluntarioResponse)) {
				this.voluntarios.addAll(payloadVoluntarioResponse
						.getObjetos());
			}
		}

		return voluntarios;
	}

	public void setVoluntarios(List<Voluntario> voluntarios) {
		this.voluntarios = voluntarios;
	}
	
	public List<Colaborador> getColaboradores() {
		if (this.colaboradores == null) {
			this.colaboradores = new ArrayList<>();
		}
		if (this.colaboradores.isEmpty()) {
			

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusColaborador", String.valueOf(EstatusColaboradorEnum.ACTIVO.ordinal()));
			
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService.consultarCriterios(TypeQuery.EQUAL, criterios);
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
	
	public ReconocimientoPersona getReconocimientoPersona() {
		if(reconocimientoPersona == null){
			reconocimientoPersona = new ReconocimientoPersona();
		}
		return reconocimientoPersona;
	}


	public void setReconocimientoPersona(ReconocimientoPersona reconocimientoPersona) {
		this.reconocimientoPersona = reconocimientoPersona;
	}
	
	public List<ClasificadorReconocimiento> getClasificadorReconocimientos() {
		if (this.clasificadorReconocimientos == null) {
			this.clasificadorReconocimientos = new ArrayList<>();
		}
		if (this.clasificadorReconocimientos.isEmpty()) {
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
				Alert.showMessage(payloadClasificadorReconocimientoResponse);
			}

			this.clasificadorReconocimientos
					.addAll(payloadClasificadorReconocimientoResponse.getObjetos());
		}
		return clasificadorReconocimientos;
	}

	public void setClasificadorReconocimientos(
			List<ClasificadorReconocimiento> clasificadorReconocimientos) {
		this.clasificadorReconocimientos = clasificadorReconocimientos;
	}
}
