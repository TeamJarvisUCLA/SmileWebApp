package ve.smile.gestion.trabajadores.egreso.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Cargo;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Persona;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadCargoResponse;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_EgresoTrabajadorIndex extends VM_WindowWizard {
	private List<EstatusTrabajadorEnum> estatusTrabajadorEnums;
	private EstatusTrabajadorEnum estatusTrabajadorEnum;

	private Estado estado;
	private Motivo motivo;

	private Trabajador trabajador = new Trabajador();
	Persona persona;

	private Cargo cargo;
	private List<Cargo> cargos;

	private Date fechaNacimiento = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<Motivo> motivos;

	private List<SexoEnum> sexoEnums;

	private SexoEnum sexoEnum;

	@Init(superclass = true)
	public void childInit() {
		estado = new Estado();
		motivo = new Motivo();
		fechaNacimiento = new Date();
		trabajador = new Trabajador();

	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public List<Cargo> getCargos() {
		if (this.cargos == null) {
			this.cargos = new ArrayList<>();
		}
		if (this.cargos.isEmpty()) {
			PayloadCargoResponse payloadCargoResponse = S.CargoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadCargoResponse)) {
				Alert.showMessage(payloadCargoResponse);
			}
			this.cargos.addAll(payloadCargoResponse.getObjetos());
		}
		return cargos;
	}

	// ENUM SEXO
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

	// CIUDADES
	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty()) {
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
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

	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getTrabajadorSelected().getFkPersona().setFkCiudad(null);
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

	// FECHAS
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getTrabajadorSelected().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	// ESTATUS
	public EstatusTrabajadorEnum getEstatusTrabajadorEnum() {
		return estatusTrabajadorEnum;
	}

	public void setEstatusTrabajadorEnum(
			EstatusTrabajadorEnum estatusTrabajadorEnum) {
		this.estatusTrabajadorEnum = estatusTrabajadorEnum;
		this.getTrabajador().setEstatusTrabajadorEnum(estatusTrabajadorEnum);
	}

	public List<EstatusTrabajadorEnum> getEstatusTrabajadorEnums() {
		if (this.estatusTrabajadorEnums == null) {
			this.estatusTrabajadorEnums = new ArrayList<>();
		}
		if (this.estatusTrabajadorEnums.isEmpty()) {
			for (EstatusTrabajadorEnum estatusTrabajadorEnum : EstatusTrabajadorEnum
					.values()) {
				this.estatusTrabajadorEnums.add(estatusTrabajadorEnum);
			}
		}
		return estatusTrabajadorEnums;
	}

	public void setEstatusTrabajadorEnums(
			List<EstatusTrabajadorEnum> estatusTrabajadorEnums) {
		this.estatusTrabajadorEnums = estatusTrabajadorEnums;
	}

	// MOTIVOS
	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
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

	// METODOS DEL WIZARD

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
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "EGRESAR", "Custom1",
				"z-icon-times", "deep-orange", "EGRESAR");
		listOperacionWizard2.add(operacionWizardCustom1);
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

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);
		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/trabajadores/egreso/selectTrabajador.zul");
		urls.add("views/desktop/gestion/trabajadores/egreso/datosTrabajador.zul");
		urls.add("views/desktop/gestion/trabajadores/egreso/registroMotivo.zul");
		urls.add("views/desktop/gestion/trabajadores/egreso/registroCompletado.zul");
		return urls;
	}

	// CARGAR OBJETOS

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador",
				String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}

	// ATRAS

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();
		return "";
	}

	// SIGUIENTE

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajador</b>";
			} else {
				persona = getTrabajadorSelected().getFkPersona();
				if (this.persona.getSexo() != null) {
					this.setSexoEnum(SexoEnum.values()[this.persona.getSexo()]);
				}
			}
		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 2) {
			// NOTHING
		}
		goToNextStep();
		return "";
	}

	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		this.setSelectedObject(new Trabajador());
		this.setTrabajador(new Trabajador());
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		BindUtils.postNotifyChange(null, null, this, "trabajador");
		return "";
	}

	// CUSTOMS

	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 2) {
			this.getTrabajadorSelected().setEstatusTrabajador(
					EstatusTrabajadorEnum.INACTIVO.ordinal());
			this.getTrabajadorSelected().setFechaEgreso(new Date().getTime());
			goToNextStep();
		}
		if (currentStep == 4) {
			restartWizard();
		}
		return "";
	}

	// FINALIZAR

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.modificar(getTrabajadorSelected());
			if (UtilPayload.isOK(payloadTrabajadorResponse)) {
				restartWizard();
				this.setSelectedObject(new Trabajador());
				this.setTrabajador(new Trabajador());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "trabajador");
			}
			goToNextStep();
		}
		return "";
	}

	public Trabajador getTrabajadorSelected() {
		return (Trabajador) selectedObject;
	}
}
