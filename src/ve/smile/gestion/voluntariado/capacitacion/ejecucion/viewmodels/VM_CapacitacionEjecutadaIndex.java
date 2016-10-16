package ve.smile.gestion.voluntariado.capacitacion.ejecucion.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.Motivo;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadMotivoResponse;

public class VM_CapacitacionEjecutadaIndex extends VM_WindowWizard
{
	private List<Motivo> motivos;
	private Motivo motivo = new Motivo();
	private Date fechaEjecutada = new Date();
	private String observacion = new String();
	private CapacitacionPlanificada capacitacionPlanificada;
	private List<EstatusCapacitacionPlanificadaEnum> estatusCapacitacionPlanificadaEnums;
	private EstatusCapacitacionPlanificadaEnum estatusCapacitacionPlanificadaEnum;

	@Init(superclass = true)
	public void childInit()
	{
		capacitacionPlanificada = new CapacitacionPlanificada();
		fechaEjecutada = new Date();
		motivo = new Motivo();
	}
	
	// CAPACITACION PLANIFICADA
	public CapacitacionPlanificada getCapacitacionPlanificada()
	{
		return capacitacionPlanificada;
	}

	public void setCapacitacionPlanificada(CapacitacionPlanificada capacitacionPlanificada)
	{
		this.capacitacionPlanificada = capacitacionPlanificada;
	}

	// FECHA EJECUTADA
	public Date getFechaEjecutada()
	{
		return fechaEjecutada;
	}

	public void setFechaEjecutada(Date fechaEjecutada)
	{
		this.fechaEjecutada = fechaEjecutada;
	}
	
	// ESTATUS CAPACITACION PLANIFICADA
	public EstatusCapacitacionPlanificadaEnum getEstatusCapacitacionPlanificadaEnum()
	{
		return estatusCapacitacionPlanificadaEnum;
	}

	public void setEstatusCapacitacionPlanificadaEnum(EstatusCapacitacionPlanificadaEnum estatusCapacitacionPlanificadaEnum)
	{
		this.estatusCapacitacionPlanificadaEnum = estatusCapacitacionPlanificadaEnum;
		this.getCapacitacionPlanificada().setEstatusCapacitacionPlanificada(estatusCapacitacionPlanificadaEnum);
	}

	public List<EstatusCapacitacionPlanificadaEnum> getEstatusCapacitacionPlanificadaEnums()
	{
		if (this.estatusCapacitacionPlanificadaEnums == null)
		{
			this.estatusCapacitacionPlanificadaEnums = new ArrayList<>();
		}
		if (this.estatusCapacitacionPlanificadaEnums.isEmpty())
		{
			for (EstatusCapacitacionPlanificadaEnum estatusCapacitacionPlanificadaEnum : EstatusCapacitacionPlanificadaEnum.values())
			{
				this.estatusCapacitacionPlanificadaEnums.add(estatusCapacitacionPlanificadaEnum);
			}
		}
		return estatusCapacitacionPlanificadaEnums;
	}
			
	public void setEstatusCapacitacionPlanificadaEnums(List<EstatusCapacitacionPlanificadaEnum> estatusCapacitacionPlanificadaEnums)
	{
		this.estatusCapacitacionPlanificadaEnums = estatusCapacitacionPlanificadaEnums;
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
	
	// OBSERVACIONES
	public String getObservacion()
	{
		return this.observacion;
	}

	public void setObservacion (String observacion)
	{
		this.observacion = observacion;
	}
		
	// METODOS DEL WIZARD
	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep()
	{
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.FINALIZAR));
		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public List<String> getIconsToStep()
	{
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep()
	{
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/registroCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/registroCompletado.zul");
		return urls;
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
	public String executeSiguiente(Integer currentStep)
	{
		if (currentStep == 2)
		{
			this.getCapacitacionPlanificada().setFkCapacitacion((Capacitacion) selectedObject);
			this.getMotivo().setIdMotivo(this.getMotivo().getIdMotivo());
			this.getCapacitacionPlanificada().setFechaEjecutada(this.getFechaEjecutada().getTime());
			this.getCapacitacionPlanificada().setObservacion(this.getObservacion());
			this.getCapacitacionPlanificada().setEstatusCapacitacionPlanificada(this.getEstatusCapacitacionPlanificadaEnum());
			//this.getCapacitacionPlanificada().setFkPersona(this.getVoluntario().getFkPersona());
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.incluir(this.capacitacionPlanificada);
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				//restartWizard();
				this.setCapacitacionPlanificada(new CapacitacionPlanificada());
				this.setFechaEjecutada(new Date());
				this.setSelectedObject(new CapacitacionPlanificada());
				//this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "capacitacionPlanificada");
				BindUtils.postNotifyChange(null, null, this, "fechaEjecutada");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				//BindUtils.postNotifyChange(null, null, this, "voluntario");
			}
		}
		goToNextStep();
		return "";
	}
	
	// FINALIZAR
	@Override
	public String executeFinalizar(Integer currentStep)
	{
		if (currentStep == 3)
		{
			restartWizard();
		}
		return "";
	}

	// CAPACITACIONES PLANIFICADAS
	@Override
	public IPayloadResponse<CapacitacionPlanificada> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		EstatusCapacitacionPlanificadaEnum.PLANIFICADA.ordinal();
		criterios.put("estatusCapacitacionPlanificada", String.valueOf(EstatusCapacitacionPlanificadaEnum.PLANIFICADA.ordinal()));
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadCapacitacionPlanificadaResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar una <b>capacitación planificada</b>";
			}
		}

		if (currentStep == 2)
		{
			try
			{
				UtilValidate.validateDate(this.getFechaEjecutada().getTime(), "Fecha planificada", ValidateOperator.GREATER_THAN, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
				//UtilValidate.validateNull(this.getVoluntario().getIdVoluntario(), "Responsable");
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep)
	{
		if (currentStep == 3)
		{
			// NOTHING
		}
		return "";
	}

	@Override
	public void comeIn(Integer currentStep)
	{
		/*
		if (currentStep == 1)
		{
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
		*/
	}
}
