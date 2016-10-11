package ve.smile.gestion.apadrinamiento.postulacion.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_PostulacionIndex extends VM_WindowWizard<Padrino> {

	private List<EstatusPadrinoEnum> estatusPadrinoEnums;
	private EstatusPadrinoEnum estatusPadrinoEnum;

	private Estado estado;
	private Padrino padrino = new Padrino();

	private Date fechaNacimiento = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;
	private List<Motivo> motivos;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;
	private Ciudad ciudad;
	private Motivo motivo;

	@Init(superclass = true)
	public void childInit() {
		padrino = new Padrino();
		estado = new Estado();
		fechaNacimiento = new Date();
	}

	//

	public Padrino getPadrino() {
		return padrino;
	}

	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
	}

	// ENUN SEXO
	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		selectedObject.getFkPersona().setSexo(this.sexoEnum.ordinal());
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
		selectedObject.getFkPersona().setTipoPersona(
				this.tipoPersonaEnum.ordinal());
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

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
		this.selectedObject.getFkPersona().setFkCiudad(ciudad);
	}

	public List<Motivo> getMotivos() {
		if (this.motivo == null) {
			this.motivos = new ArrayList<>();
			this.motivos = S.MotivoService.consultarTodos().getObjetos();
		}
		return motivos;
	}

	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
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

	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({ "ciudades" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getSelectedObject().getFkPersona().setFkCiudad(null);
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
		this.getSelectedObject().getFkPersona()
				.setFechaNacimiento(fechaNacimiento.getTime());
	}

	// ESTATUS POSTULADO
	public EstatusPadrinoEnum getEstatusPadrinoEnum() {
		return estatusPadrinoEnum;
	}

	public void setEstatusPadrinoEnum(EstatusPadrinoEnum estatusPadrinoEnum) {
		this.estatusPadrinoEnum = estatusPadrinoEnum;
		this.getPadrino().setEstatusPadrinoEnum(estatusPadrinoEnum);
	}

	public List<EstatusPadrinoEnum> getEstatusPadrinoEnums() {
		if (this.estatusPadrinoEnums == null) {
			this.estatusPadrinoEnums = new ArrayList<>();
		}
		if (this.estatusPadrinoEnums.isEmpty()) {
			for (EstatusPadrinoEnum estatusPadrinoEnum : EstatusPadrinoEnum
					.values()) {
				this.estatusPadrinoEnums.add(estatusPadrinoEnum);
			}
		}
		return estatusPadrinoEnums;
	}

	public void setEstatusPadrinoEnums(
			List<EstatusPadrinoEnum> estatusPadrinoEnums) {
		this.estatusPadrinoEnums = estatusPadrinoEnums;
	}

	// Wizard

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		/*
		 * List<OperacionWizard> listOperacionWizard1 = new
		 * ArrayList<OperacionWizard>();
		 * listOperacionWizard1.add(OperacionWizardHelper
		 * .getPorType(OperacionWizardEnum.SIGUIENTE)); botones.put(1,
		 * listOperacionWizard1);
		 * 
		 * List<OperacionWizard> listOperacionWizard2 = new
		 * ArrayList<OperacionWizard>();
		 * listOperacionWizard2.add(OperacionWizardHelper
		 * .getPorType(OperacionWizardEnum.ATRAS));
		 * listOperacionWizard2.add(OperacionWizardHelper
		 * .getPorType(OperacionWizardEnum.FINALIZAR)); botones.put(2,
		 * listOperacionWizard2);
		 */
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "APROBAR", "Custom1",
				"fa fa-check-square-o", "green", "APROBAR");
		OperacionWizard operacionWizardCustom2 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM2.ordinal(), "RECHAZAR", "Custom2",
				"z-icon-times", "deep-orange", "RECHAZAR");
		OperacionWizard operacionWizardCustom3 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM3.ordinal(), "CANCELAR", "Custom3",
				"z-icon-times", "red", "CANCELAR");

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(operacionWizardCustom1);
		listOperacionWizard2.add(operacionWizardCustom2);
		listOperacionWizard2.add(operacionWizardCustom3);

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard3.add(operacionWizardCustom3);

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
		urls.add("views/desktop/gestion/apadrinamiento/postulacion/selectPostulado.zul");
		urls.add("views/desktop/gestion/apadrinamiento/postulacion/datosPostulado.zul");
		urls.add("views/desktop/gestion/apadrinamiento/postulacion/MotivoRechazo.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			this.setEstado(selectedObject.getFkPersona().getFkCiudad()
					.getFkEstado());
			this.setSexoEnum(SexoEnum.values()[selectedObject.getFkPersona()
					.getSexo()]);
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[selectedObject
					.getFkPersona().getTipoPersona()]);
			this.setMotivo(selectedObject.getFkMotivo());
			changeEstado();
			this.setCiudad(selectedObject.getFkPersona().getFkCiudad());
			BindUtils.postNotifyChange(null, null, this, "estado");
			BindUtils.postNotifyChange(null, null, this, "ciudades");
			BindUtils.postNotifyChange(null, null, this, "sexoEnum");
			BindUtils.postNotifyChange(null, null, this, "ciudad");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
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
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusPadrino",
				String.valueOf(EstatusPadrinoEnum.POSTULADO.ordinal()));
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPadrinoResponse;
	}

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
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			try {
				// NOTHING
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeCustom3(Integer currentStep) {
		restartWizard();
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {

		selectedObject.setEstatusPadrino(EstatusPadrinoEnum.POR_COMPLETAR
				.ordinal());
		selectedObject.setFkMotivo(motivo);
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.modificar(selectedObject);
		if (UtilPayload.isOK(payloadPadrinoResponse)) {
			restartWizard();
			this.setSelectedObject(new Padrino());
			this.setPadrino(new Padrino());
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
			BindUtils.postNotifyChange(null, null, this, "Padrino");
		}
		return (String) payloadPadrinoResponse
				.getInformacion(IPayloadResponse.MENSAJE);
	}

	@Override
	public String executeCustom2(Integer currentStep) {
		if (currentStep == 2) {
			// selectedObject.setEstatusPadrino(EstatusPadrinoEnum.RECHAZADO.ordinal());
			goToNextStep();
		}

		// PayloadPadrinoResponse payloadPadrinoResponse =
		// S.PadrinoService.modificar(selectedObject);
		// if (UtilPayload.isOK(payloadPadrinoResponse))
		// {
		// restartWizard();
		// this.setSelectedObject(new Padrino());
		// this.setPadrino(new Padrino());
		// BindUtils.postNotifyChange(null, null, this, "selectedObject");
		// BindUtils.postNotifyChange(null, null, this, "Padrino");
		// }
		// return (String)
		// payloadPadrinoResponse.getInformacion(IPayloadResponse.MENSAJE);
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			selectedObject.setEstatusPadrino(EstatusPadrinoEnum.RECHAZADO
					.ordinal());
			//selectedObject.setFkMotivo(fkMotivo);
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.modificar(this.selectedObject);
			
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				restartWizard();
				this.setSelectedObject(new Padrino());
				this.setPadrino(new Padrino());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "Padrino");
			}
			return (String) payloadPadrinoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}

}