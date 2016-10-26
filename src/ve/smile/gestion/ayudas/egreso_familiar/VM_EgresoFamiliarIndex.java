package ve.smile.gestion.ayudas.egreso_familiar;

import java.util.ArrayList;
import java.util.Calendar;
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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Familiar;
import ve.smile.dto.Motivo;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusBeneficiarioEnum;
import ve.smile.enums.EstatusFamiliarEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_EgresoFamiliarIndex extends VM_WindowWizard {

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

	private Familiar familiar;

	private List<Motivo> motivos;

	private List<Beneficiario> beneficiarios;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		familiar = new Familiar();

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

	// ENUN SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getFamiliar().getFkPersona().setSexo(this.sexoEnum.ordinal());
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

	// ENUM TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getFamiliar().getFkPersona()
				.setTipoPersona(this.tipoPersonaEnum.ordinal());
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

	// CIUDADES
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

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getFamiliar().setFechaIngreso(fechaIngreso.getTime());
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	// FECHAS
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getFamiliar().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
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
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));

		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));

		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(4, listOperacionWizard4);

		List<OperacionWizard> listOperacionWizard5 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard5.add(operacionWizardCustom);

		botones.put(5, listOperacionWizard5);

		return botones;
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		// TODO Auto-generated method stub
		restartWizard();
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/egresoFamiliar/selectFamiliar.zul");
		urls.add("views/desktop/gestion/ayudas/egresoFamiliar/FamiliarFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/egresoFamiliar/BeneficiarioDeFamiliarFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/egresoFamiliar/MotivoFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/egresoFamiliar/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		executeCancelar(currentStep);
		this.setBeneficiarios(null);
		return "";

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
	public IPayloadResponse<Familiar> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusFamiliar",
				String.valueOf(EstatusFamiliarEnum.ACTIVO.ordinal()));
		PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		return payloadFamiliarResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {

		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Familiar</b>";
			}
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {

		if (currentStep == 4) {
			try {
				UtilValidate.validateNull(this.getFamiliar().getFkMotivo(),
						"Motivo");

				UtilValidate.validateString(
						this.getFamiliar().getObservacion(),
						"Detalle de Rechazo", 100);
			} catch (Exception e) {
				return e.getMessage();
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 4) {
			Familiar familiar = this.getFamiliar();
			// familiar.s(EstatusFamiliarEnum.INACTIVO.ordinal());

			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("fkFamiliar.idFamiliar",
					String.valueOf(familiar.getIdFamiliar()));

			PayloadBeneficiarioResponse payloadBeneficiario =

			S.BeneficiarioService
					.consultarCriterios(TypeQuery.EQUAL, criterios);

			List<Beneficiario> list = payloadBeneficiario.getObjetos();

			// System.out.println(list);
			for (Beneficiario elemento : list) {
				// System.out.println(elemento.getFkBeneficiario().getFkPersona().getNombre()
				// + elemento.getFkBeneficiario().getFkPersona().getApellido()
				// );
				// Persona personaBeneficiario = elemento;
				// personaBeneficiario.setEstatus('0');
				// PayloadPersonaResponse payloadPersonaResponse =
				// S.PersonaService.modificar(personaBeneficiario);
				Beneficiario beneficiario = elemento;
				beneficiario
						.setEstatusBeneficiario(EstatusBeneficiarioEnum.INACTIVO
								.ordinal());
				beneficiario.setFkMotivo(familiar.getFkMotivo());
				beneficiario.setObservacion(familiar.getObservacion());
				PayloadBeneficiarioResponse payloadBeneficiarioResponse2 = S.BeneficiarioService
						.modificar(beneficiario);
			}
			// PayloadBeneficiarioFamiliarResponse
			// payloadBeneficiarioFamiliarResponse =
			// S.BeneficiarioFamiliarService.consultarCriterios(typeQuery,
			// criterios);
			/*
			 * PayloadPersonaResponse payloadPersonaResponse =
			 * S.PersonaService.modificar(persona); Familiar familiar =
			 * this.getFamiliar(); familiar.getFkPersona().setEstatus('0');
			 */

			familiar.setEstatusFamiliar(EstatusFamiliarEnum.INACTIVO.ordinal());

			PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
					.modificar(familiar);

			if (!UtilPayload.isOK(payloadFamiliarResponse)) {
				Alert.showMessage(payloadFamiliarResponse);

			}

			if (UtilPayload.isOK(payloadFamiliarResponse)) {
				goToNextStep();
			}

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {

		if (currentStep == 2) {

			this.setFamiliar((Familiar) selectedObject);

			this.setPersona(this.getFamiliar().getFkPersona());

			this.setEstado(this.getFamiliar().getFkPersona().getFkCiudad()
					.getFkEstado());
			if (this.getFamiliar().getFkPersona().getSexo() != null) {
				this.setSexoEnum(SexoEnum.values()[this.getFamiliar()
						.getFkPersona().getSexo()]);
			}

			this.setTipoPersonaEnum(this.getFamiliar().getFkPersona()
					.getTipoPersonaEnum());

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(this.getFamiliar().getFechaIngreso());
			this.setFechaIngreso(cal.getTime());

			if (this.getFamiliar().getFkPersona().getFechaNacimiento() != null) {
				Calendar cal2 = Calendar.getInstance();
				cal2.setTimeInMillis(this.getFamiliar().getFkPersona()
						.getFechaNacimiento());
				this.setFechaNacimiento(cal2.getTime());
			}

			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(estado.getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
			BindUtils.postNotifyChange(null, null, this, "estado");
			BindUtils.postNotifyChange(null, null, this, "ciudades");
			BindUtils.postNotifyChange(null, null, this, "sexoEnum");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
		}

	}

	public List<Beneficiario> getBeneficiarios() {
		if (this.beneficiarios == null) {
			this.beneficiarios = new ArrayList<>();
		}
		// System.out.println(this.getFamiliar().getIdFamiliar());
		if (this.getFamiliar().getIdFamiliar() != null) {

			Map<String, String> criterios = new HashMap<>();

			criterios.put("fkFamiliar.idFamiliar",
					String.valueOf(this.getFamiliar().getIdFamiliar()));
			criterios.put("estatusBeneficiario",
					String.valueOf(EstatusBeneficiarioEnum.ACTIVO.ordinal()));
			PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadBeneficiarioResponse)) {
				Alert.showMessage(payloadBeneficiarioResponse);
			}
			this.beneficiarios.addAll(payloadBeneficiarioResponse.getObjetos());

		}

		return beneficiarios;
	}

	public void setBeneficiarios(List<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

}
