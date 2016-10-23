package ve.smile.gestion.voluntariado.postulacion.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.seguridad.enums.SexoEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_VoluntarioPostuladoIndex extends VM_WindowWizard {
	private Estado estado = new Estado();
	private Motivo motivo = new Motivo();
	private Date fechaNacimiento = new Date();

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;
	private EstatusVoluntarioEnum estatusVoluntarioEnum;

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<Motivo> motivos;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;
	private List<EstatusVoluntarioEnum> estatusVoluntarioEnums;

	@Init(superclass = true)
	public void childInit() {
		estado = new Estado();
		motivo = new Motivo();
		fechaNacimiento = new Date();
	}

	// ENUM SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getVoluntarioSelected().getFkPersona()
				.setSexo(this.sexoEnum.ordinal());
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
		this.getVoluntarioSelected().getFkPersona()
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

	// ESTADOS
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

	// CIUDADES POR ESTADO
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getVoluntarioSelected().getFkPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();
		criterios
				.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}

	// FECHA NACIMIENTO
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getVoluntarioSelected().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	// ESTATUS VOLUNTARIO
	public EstatusVoluntarioEnum getEstatusVoluntarioEnum() {
		return estatusVoluntarioEnum;
	}

	public void setEstatusVoluntarioEnum(
			EstatusVoluntarioEnum estatusVoluntarioEnum) {
		this.estatusVoluntarioEnum = estatusVoluntarioEnum;
		this.getVoluntarioSelected().setEstatusVoluntarioEnum(
				estatusVoluntarioEnum);
	}

	public List<EstatusVoluntarioEnum> getEstatusVoluntarioEnums() {
		if (this.estatusVoluntarioEnums == null) {
			this.estatusVoluntarioEnums = new ArrayList<>();
		}
		if (this.estatusVoluntarioEnums.isEmpty()) {
			for (EstatusVoluntarioEnum estatusVoluntarioEnum : EstatusVoluntarioEnum
					.values()) {
				this.estatusVoluntarioEnums.add(estatusVoluntarioEnum);
			}
		}
		return estatusVoluntarioEnums;
	}

	public void setEstatusVoluntarioEnums(
			List<EstatusVoluntarioEnum> estatusVoluntarioEnums) {
		this.estatusVoluntarioEnums = estatusVoluntarioEnums;
	}

	// MOTIVOS
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
		this.getVoluntarioSelected().setFkMotivo(this.motivo);
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

	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}

	// M�TODOS DEL WIZARD
	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "APROBAR", "Custom1",
				"fa fa-check-square-o", "green", "APROBAR");
		OperacionWizard operacionWizardCustom2 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM2.ordinal(), "RECHAZAR", "Custom2",
				"z-icon-times", "deep-orange", "RECHAZAR");
		listOperacionWizard2.add(operacionWizardCustom1);
		listOperacionWizard2.add(operacionWizardCustom2);
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/postulacion/selectPostulado.zul");
		urls.add("views/desktop/gestion/voluntariado/postulacion/datosPostulado.zul");
		urls.add("views/desktop/gestion/voluntariado/postulacion/registroMotivo.zul");
		return urls;
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusVoluntario",
				String.valueOf(EstatusVoluntarioEnum.POSTULADO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	// ATRAS
	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return "";
	}

	// SIGUIENTE
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>postulado</b>";
			}
		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.setEstado(this.getVoluntarioSelected().getFkPersona()
					.getFkCiudad().getFkEstado());
			this.setSexoEnum(SexoEnum.values()[this.getVoluntarioSelected()
					.getFkPersona().getSexo()]);
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this
					.getVoluntarioSelected().getFkPersona().getTipoPersona()]);
			this.setMotivo(this.getVoluntarioSelected().getFkMotivo());
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
			BindUtils.postNotifyChange(null, null, this, "tipoPersonaEnum");
			BindUtils.postNotifyChange(null, null, this, "motivo");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
		}
		if (currentStep == 2) {
			// NOTHING
		}
		goToNextStep();
		return "";
	}

	// CUSTOMS
	@Override
	public String executeCustom1(Integer currentStep) {
		// POR COMPLETAR DATOS
		this.getVoluntarioSelected().setEstatusVoluntario(
				EstatusVoluntarioEnum.POR_COMPLETAR.ordinal());
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
				.modificar(this.getVoluntarioSelected());
		if (UtilPayload.isOK(payloadVoluntarioResponse)) {
			restartWizard();
			this.setSelectedObject(new Voluntario());
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
			BindUtils.postNotifyChange(null, null, this, "voluntario");
			return (String) payloadVoluntarioResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	@Override
	public String executeCustom2(Integer currentStep) {
		// RECHAZADO
		this.getVoluntarioSelected().setEstatusVoluntario(
				EstatusVoluntarioEnum.RECHAZADO.ordinal());
		goToNextStep();
		return "";
	}

	// FINALIZAR
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				UtilValidate.validateNull(this.getVoluntarioSelected()
						.getFkMotivo(), "Motivo");
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			this.getVoluntarioSelected().setFkMotivo(this.getMotivo());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.modificar(this.getVoluntarioSelected());
			if (UtilPayload.isOK(payloadVoluntarioResponse)) {
				restartWizard();
				this.setSelectedObject(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
				return (String) payloadVoluntarioResponse
						.getInformacion(IPayloadResponse.MENSAJE);

			}
		}
		return "";
	}

	public Voluntario getVoluntarioSelected() {
		return (Voluntario) this.selectedObject;
	}
}
