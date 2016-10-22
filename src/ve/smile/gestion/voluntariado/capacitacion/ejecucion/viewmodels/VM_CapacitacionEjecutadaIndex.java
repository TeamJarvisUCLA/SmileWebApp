package ve.smile.gestion.voluntariado.capacitacion.ejecucion.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;
import lights.core.enums.TypeQuery;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Voluntario;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.VolCapacitacionPlanificada;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEventPlanTareaResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;

public class VM_CapacitacionEjecutadaIndex extends VM_WindowWizard<CapacitacionPlanificada>
{
	private Motivo motivo = new Motivo();
	private Date fechaEjecutada = new Date();
	private String observacion = new String();	
	private EstatusCapacitacionPlanificadaEnum estatusCapacitacionPlanificadaEnum;
	
	private List<Motivo> motivos;
	private List<VolCapacitacionPlanificada> voluntariosInscritos;
	private List<EstatusCapacitacionPlanificadaEnum> estatusCapacitacionPlanificadaEnums;

	@Init(superclass = true)
	public void childInit()
	{
		motivo = new Motivo();
		fechaEjecutada = new Date();
	}
	
	// VOLUNTARIOS INSCRITOS
	public List<VolCapacitacionPlanificada> getVoluntariosInscritos()
	{
		return voluntariosInscritos;
	}
	
	public void setVoluntariosInscritos (List<VolCapacitacionPlanificada> voluntariosInscritos)
	{
		this.voluntariosInscritos = voluntariosInscritos;
	}

	// FECHA EJECUTADA
	public Date getFechaEjecutada()
	{
		return fechaEjecutada;
	}

	public void setFechaEjecutada(Date fechaEjecutada)
	{
		this.fechaEjecutada = fechaEjecutada;
		this.getCapacitacionPlanificadaSelected().setFechaEjecutada(this.fechaEjecutada.getTime());
	}
	
	// ESTATUS CAPACITACION PLANIFICADA
	public EstatusCapacitacionPlanificadaEnum getEstatusCapacitacionPlanificadaEnum()
	{
		return estatusCapacitacionPlanificadaEnum;
	}

	public void setEstatusCapacitacionPlanificadaEnum(EstatusCapacitacionPlanificadaEnum estatusCapacitacionPlanificadaEnum)
	{
		this.estatusCapacitacionPlanificadaEnum = estatusCapacitacionPlanificadaEnum;
		this.getCapacitacionPlanificadaSelected().setEstatusCapacitacionPlanificada(estatusCapacitacionPlanificadaEnum.ordinal());
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
		
	// MOTIVO DE LA CAPACITACION EJECUTADA
	public Motivo getMotivo()
	{
		return motivo;
	}

	public void setMotivo (Motivo motivo)
	{
		this.motivo = motivo;
		this.getCapacitacionPlanificadaSelected().setFkMotivo(this.motivo);
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
	
	// OBSERVACIONES DE LA CAPACITACION EJECUTADA
	public String getObservacion()
	{
		return this.observacion;
	}

	public void setObservacion (String observacion)
	{
		this.observacion = observacion;
		this.getCapacitacionPlanificadaSelected().setObservacion(this.observacion);
	}
	
	// CATALOGO DE MOTIVOS
	@Command("buscarMotivo")
	public void buscarMotivo()
	{
		CatalogueDialogData<Motivo> catalogueDialogData = new CatalogueDialogData<Motivo>();
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Motivo>()
			{
				@Override
				public void onClose(CatalogueDialogCloseEvent<Motivo> catalogueDialogCloseEvent)
				{
					if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
					{
						return;
					}
					motivo = catalogueDialogCloseEvent.getEntity();
					refreshMotivo();
				}
			});
		UtilDialog.showDialog("views/desktop/gestion/voluntariado/capacitacion/ejecucion/catalogoMotivo.zul", catalogueDialogData);
	}

	public void refreshMotivo()
	{
		BindUtils.postNotifyChange(null, null, this, "motivo");
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
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		return botones;
	}

	@Override
	public List<String> getIconsToStep()
	{
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep()
	{
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/ejecucion/registroCapacitacion.zul");
		return urls;
	}
	
	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<CapacitacionPlanificada> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusCapacitacionPlanificada", String.valueOf(EstatusCapacitacionPlanificadaEnum.PLANIFICADA.ordinal()));
		PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadCapacitacionPlanificadaResponse;
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
				return "E:Error Code 5-Debe seleccionar una <b>capacitación planificada</b>";
			}
		}
		return "";
	}
		
	@Override
	public String executeSiguiente(Integer currentStep)
	{	
		goToNextStep();
		return "";
	}
	
	// FINALIZAR
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep)
	{
		if (currentStep == 2)
		{
			try
			{
				this.voluntariosInscritos = new ArrayList<>();
				Map<String, String> criterios = new HashMap<>();
				criterios.put("fkCapacitacionPlanificada.idCapacitacionPlanificada", String.valueOf(this.getSelectedObject().getIdCapacitacionPlanificada()));
				PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService.consultarCriterios(TypeQuery.EQUAL, criterios);
				if(payloadVolCapacitacionPlanificadaResponse.getObjetos() != null & payloadVolCapacitacionPlanificadaResponse.getObjetos().size() > 0)
				{				
					this.voluntariosInscritos.addAll(payloadVolCapacitacionPlanificadaResponse.getObjetos());
				}
				BindUtils.postNotifyChange(null, null, this, "voluntariosInscritos");
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
		if (currentStep == 2)
		{
			// VOLUNTARIOS CAPACITACION PLANIFICADA
			for (int k = 0; k < this.getVoluntariosInscritos().size(); k++)
			{
				VolCapacitacionPlanificada vcp;
				vcp = this.getVoluntariosInscritos().get(k);
				// SETEAR VALORES DE TABLA
				PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService.modificar(vcp);
				if (!UtilPayload.isOK(payloadVolCapacitacionPlanificadaResponse))
				{
					Alert.showMessage(payloadVolCapacitacionPlanificadaResponse);
				}
				Alert.showMessage(payloadVolCapacitacionPlanificadaResponse);
			}
			
			// CAPACITACION PLANIFICADA
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.modificar(this.getCapacitacionPlanificadaSelected());
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				restartWizard();
				this.setMotivo(new Motivo());
				this.setObservacion(new String());
				this.setFechaEjecutada(new Date());
				this.setSelectedObject(new CapacitacionPlanificada());
				BindUtils.postNotifyChange(null, null, this, "motivo");
				BindUtils.postNotifyChange(null, null, this, "observacion");
				BindUtils.postNotifyChange(null, null, this, "fechaEjecutada");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "capacitacionPlanificada");
				BindUtils.postNotifyChange(null, null, this, "estatusCapacitacionPlanificada");
				return (String) payloadCapacitacionPlanificadaResponse.getInformacion(IPayloadResponse.MENSAJE);
			}
		}
		return "";
	}

	// CAPACITACION PLANIFICADA SELECTED
	public CapacitacionPlanificada getCapacitacionPlanificadaSelected()
	{
		return (CapacitacionPlanificada) this.selectedObject;
	}
}
