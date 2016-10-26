package ve.smile.gestion.ayudas.egreso_beneficiario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusBeneficiarioEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_EgresoBeneficiarioIndex extends VM_WindowWizard {

	private List<Ciudad> ciudades;

	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;

	private Date fechaNacimiento;
	private Date fechaIngreso;
	private Persona persona;

	private Estado estado;

	private List<Estado> estados;

	private Beneficiario beneficiario;

	private List<Motivo> motivos;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		beneficiario = new Beneficiario();

	}

	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}

	public List<Motivo> getMotivos() {
		if (this.motivos == null) {
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty()) {
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
			}

			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}

		return motivos;
	}

	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPersona().setTipoPersona(tipoPersonaEnum.ordinal());
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums() {
		if (this.tipoPersonaEnums == null) {
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty()) {
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values()) {
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums) {
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		if (this.estados == null) {
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty()) {
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse)) {
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		if (persona == null) {
			persona = new Persona();
		}
		this.persona = persona;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getBeneficiario().setFechaIngreso(fechaIngreso.getTime());
	}

	public List<SexoEnum> getSexoEnums() {
		if (this.sexoEnums == null) {
			this.sexoEnums = new ArrayList<>();
		}
		if (this.sexoEnums.isEmpty()) {
			for (SexoEnum sexoEnum : SexoEnum.values()) {
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums) {
		this.sexoEnums = sexoEnums;
	}

	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPersona().setSexo(sexoEnum.ordinal());
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "EGRESAR", "Custom1",
				"z-icon-times", "deep-orange", "EGRESAR");
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(operacionWizardCustom1);
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);

		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-list");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/egresoBeneficiario/selectBeneficiario.zul");
		urls.add("views/desktop/gestion/ayudas/egresoBeneficiario/BeneficiarioFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/egresoBeneficiario/MotivoFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/egresoBeneficiario/registroCompletado.zul");

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
	public IPayloadResponse<Beneficiario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusBeneficiario",
				String.valueOf(EstatusBeneficiarioEnum.ACTIVO.ordinal()));
		PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadBeneficiarioResponse;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 2) {
			goToNextStep();
		}
		if (currentStep == 4) {
			executeCancelar(currentStep);
		}

		return "";

	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {

		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Beneficiario</b>";
			}
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 3) {
			try {
				UtilValidate.validateNull(this.getBeneficiario().getFkMotivo(),
						"Motivo");

				UtilValidate.validateString(this.getBeneficiario()
						.getObservacion(), "Detalle de Rechazo", 100);
			} catch (Exception e) {
				return e.getMessage();
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			Beneficiario beneficiario = this.getBeneficiario();
			beneficiario
					.setEstatusBeneficiario(EstatusBeneficiarioEnum.INACTIVO
							.ordinal());
			PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
					.modificar(beneficiario);
			if (!UtilPayload.isOK(payloadBeneficiarioResponse)) {
				Alert.showMessage(payloadBeneficiarioResponse);

			}

			if (UtilPayload.isOK(payloadBeneficiarioResponse)) {
				goToNextStep();
			}

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {

		if (currentStep == 2) {

			this.setBeneficiario((Beneficiario) selectedObject);

			this.setPersona(this.getBeneficiario().getFkPersona());
			if (this.persona.getSexo() != null) {
				this.setSexoEnum(SexoEnum.values()[this.persona.getSexo()]);
			}
			if (this.persona.getTipoPersona() != null) {
				this.setTipoPersonaEnum(TipoPersonaEnum.values()[this.persona
						.getTipoPersona()]);
			}
			if (this.getPersona().getFechaNacimiento() != null) {
				this.fechaNacimiento = new Date(this.getPersona()
						.getFechaNacimiento());
			}

			if (this.getBeneficiario().getFechaIngreso() != null) {
				this.fechaNacimiento = new Date(this.getPersona()
						.getFechaNacimiento());
			}
		}

	}

}
