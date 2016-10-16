package ve.smile.gestion.voluntariado.postulacion.viewmodels;

import java.text.SimpleDateFormat;
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
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
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

public class VM_VoluntarioPostuladoIndex extends VM_WindowWizard<Voluntario>
{
	private List<EstatusVoluntarioEnum> estatusVoluntarioEnums;
	private EstatusVoluntarioEnum estatusVoluntarioEnum;
		
	private Estado estado;
	private Motivo motivo;
	
	private Voluntario voluntario = new Voluntario();
	
	private Date fechaNacimiento = new Date();
	
	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<Motivo> motivos;
	
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	@Init(superclass = true)
	public void childInit()
	{
		estado = new Estado(); 
		motivo = new Motivo();
		fechaNacimiento = new Date();
		voluntario = new Voluntario();
	}

	// VOLUNTARIO
	public Voluntario getVoluntario()
	{
		return voluntario;
	}

	public void setVoluntario(Voluntario voluntario)
	{
		this.voluntario = voluntario;
	}

	// ENUM SEXO
	public SexoEnum getSexoEnum()
	{
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum)
	{
		this.sexoEnum = sexoEnum;
		selectedObject.getFkPersona().setSexo(this.sexoEnum.ordinal());
	}

	public List<SexoEnum> getSexoEnums()
	{
		if (this.sexoEnums == null)
		{
			this.sexoEnums = new ArrayList<>();
		}
		if (this.sexoEnums.isEmpty())
		{
			for (SexoEnum sexoEnum : SexoEnum.values())
			{
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums)
	{
		this.sexoEnums = sexoEnums;
	}

	// ENUM TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum()
	{
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum)
	{
		this.tipoPersonaEnum = tipoPersonaEnum;
		selectedObject.getFkPersona().setTipoPersona(this.tipoPersonaEnum.ordinal());
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums()
	{
		if (this.tipoPersonaEnums == null)
		{
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty())
		{
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values())
			{
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums)
	{
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

	// CIUDADES
	public List<Ciudad> getCiudades()
	{
		if (this.ciudades == null)
		{
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty())
		{
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse))
			{
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades)
	{
		this.ciudades = ciudades;
	}

	// ESTADOS
	public Estado getEstado()
	{
		return estado;
	}

	public void setEstado(Estado estado)
	{
		this.estado = estado;
	}

	public List<Estado> getEstados()
	{
		if (this.estados == null)
		{
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty())
		{
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse))
			{
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}

	public void setEstados(List<Estado> estados)
	{
		this.estados = estados;
	}

	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({"ciudades"})
	public void changeEstado()
	{
		this.getCiudades().clear();
		this.getSelectedObject().getFkPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();
		criterios.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse))
		{
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}
	
	// FECHAS
	public Date getFechaNacimiento()
	{
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento (Date fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
		this.getSelectedObject().getFkPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}
	
	// ESTATUS POSTULADO
	public EstatusVoluntarioEnum getEstatusVoluntarioEnum()
	{
		return estatusVoluntarioEnum;
	}

	public void setEstatusVoluntarioEnum(EstatusVoluntarioEnum estatusVoluntarioEnum)
	{
		this.estatusVoluntarioEnum = estatusVoluntarioEnum;
		this.getVoluntario().setEstatusVoluntarioEnum(estatusVoluntarioEnum);
	}

	public List<EstatusVoluntarioEnum> getEstatusVoluntarioEnums()
	{
		if (this.estatusVoluntarioEnums == null)
		{
			this.estatusVoluntarioEnums = new ArrayList<>();
		}
		if (this.estatusVoluntarioEnums.isEmpty())
		{
			for (EstatusVoluntarioEnum estatusVoluntarioEnum : EstatusVoluntarioEnum.values())
			{
				this.estatusVoluntarioEnums.add(estatusVoluntarioEnum);
			}
		}
		return estatusVoluntarioEnums;
	}
		
	public void setEstatusVoluntarioEnums(List<EstatusVoluntarioEnum> estatusVoluntarioEnums)
	{
		this.estatusVoluntarioEnums = estatusVoluntarioEnums;
	}
	
	// MOTIVOS
	public Motivo getMotivo()
	{
		return motivo;
	}

	public void setMotivo (Motivo motivo)
	{
		this.motivo = motivo;
	}

	public List<Motivo> getMotivos()
	{
		if (this.motivos == null)
		{
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty())
		{
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService.consultarTodos();
			if (!UtilPayload.isOK(payloadMotivoResponse))
			{
				Alert.showMessage(payloadMotivoResponse);
			}
			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}
		return motivos;
	}

	public void setMotivos(List<Motivo> motivos)
	{
		this.motivos = motivos;
	}
	
	// MÉTODOS DEL WIZARD

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep()
	{
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);
		
		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(OperacionWizardEnum.CUSTOM1.ordinal(), "RECHAZAR",  "Custom1", "z-icon-times", "deep-orange", "RECHAZAR");
		OperacionWizard operacionWizardCustom2 = new OperacionWizard(OperacionWizardEnum.CUSTOM2.ordinal(), "APROBAR",  "Custom2", "fa fa-check-square-o", "green", "APROBAR");
		listOperacionWizard2.add(operacionWizardCustom1);
		listOperacionWizard2.add(operacionWizardCustom2);
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);
		
		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(3, listOperacionWizard3);
		
		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.FINALIZAR));
		botones.put(4, listOperacionWizard4);
		
		return botones;
	}

	@Override
	public List<String> getIconsToStep()
	{
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep()
	{
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/postulacion/selectPostulado.zul");
		urls.add("views/desktop/gestion/voluntariado/postulacion/datosPostulado.zul");
		urls.add("views/desktop/gestion/voluntariado/postulacion/registroMotivo.zul");
		urls.add("views/desktop/gestion/voluntariado/postulacion/registroCompletado.zul");
		return urls;
	}
	
	// CARGAR OBJETOS
	@Override
	public void comeIn(Integer currentStep)
	{
		if (currentStep == 1)
		{
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}
	
	@Override
	public IPayloadResponse<Voluntario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		EstatusVoluntarioEnum.POSTULADO.ordinal();
		criterios.put("estatusVoluntario", String.valueOf(EstatusVoluntarioEnum.POSTULADO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}
	
	// ATRÁS
	@Override
	public String executeAtras(Integer currentStep)
	{
		goToPreviousStep();
		return "";
	}
	
	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep)
	{
		restartWizard();
		return "";
	}
	
	// SIGUIENTE
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar un <b>postulado</b>";
			}
		}
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep)
	{
		if (currentStep == 2)
		{
			// NOTHING
		}
		if (currentStep == 3)
		{
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadVoluntarioResponse))
			{
				this.setSelectedObject(new Voluntario());
				this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
			}
		}
		goToNextStep();
		return "";
	}

	// CUSTOMS
	@Override
	public String executeCustom1(Integer currentStep)
	{
		// RECHAZADO
		this.selectedObject.setEstatusVoluntario(EstatusVoluntarioEnum.RECHAZADO.ordinal());
		goToNextStep();
		return "";
	}
	
	@Override
	public String executeCustom2(Integer currentStep)
	{
		// POR COMPLETAR DATOS
		this.selectedObject.setEstatusVoluntario(EstatusVoluntarioEnum.POR_COMPLETAR.ordinal());
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(this.selectedObject);
		if (UtilPayload.isOK(payloadVoluntarioResponse))
		{
			this.setSelectedObject(new Voluntario());
			this.setVoluntario(new Voluntario());
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
			BindUtils.postNotifyChange(null, null, this, "voluntario");
		}
		goToNextStep();
		goToNextStep();
		return "";
	}
	
	// FINALIZAR
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep)
	{
		if (currentStep == 3)
		{
			try
			{
				// NOTHING
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep)
	{
		restartWizard();
		return "";
	}
}
