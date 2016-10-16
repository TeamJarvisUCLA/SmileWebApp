package ve.smile.gestion.voluntariado.capacitacion.planificacion.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;

public class VM_CapacitacionPlanificadaIndex extends VM_WindowWizard
{
	private CapacitacionPlanificada capacitacionPlanificada;
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
		capacitacionPlanificada = new CapacitacionPlanificada();
		directorio = new Directorio();
		fechaPlanificada = new Date();
	}
	
	public CapacitacionPlanificada getCapacitacionPlanificada()
	{
		return capacitacionPlanificada;
	}

	public void setCapacitacionPlanificada(CapacitacionPlanificada capacitacionPlanificada)
	{
		this.capacitacionPlanificada = capacitacionPlanificada;
	}

	public Directorio getDirectorio()
	{
		return directorio;
	}

	public void setDirectorio(Directorio directorio)
	{
		this.directorio = directorio;
	}

	public Date getFechaPlanificada()
	{
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada)
	{
		this.fechaPlanificada = fechaPlanificada;
	}

	@Command("buscarDirectorio")
	public void buscarDirectorio()
	{
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>()
			{
				@Override
				public void onClose(CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent)
				{
					if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
					{
						return;
					}
					directorio = catalogueDialogCloseEvent.getEntity();
					refreshDirectorio();
					}
				});
		UtilDialog.showDialog("views/desktop/gestion/voluntariado/capacitacion/planificacion/catalogoDirectorio.zul", catalogueDialogData);
	}

	public void refreshDirectorio()
	{
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}

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
		urls.add("views/desktop/gestion/voluntariado/capacitacion/planificacion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/planificacion/registroCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/planificacion/registroCompletado.zul");
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
			this.getCapacitacionPlanificada().setEstatusCapacitacionPlanificada(EstatusCapacitacionPlanificadaEnum.PLANIFICADA);
			this.getCapacitacionPlanificada().setFechaPlanificada(this.getFechaPlanificada().getTime());
			this.getCapacitacionPlanificada().setFkDirectorio(this.getDirectorio());
			//this.getCapacitacionPlanificada().setFkPersona(this.getVoluntario().getFkPersona());
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.incluir(this.capacitacionPlanificada);
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				//restartWizard();
				this.setCapacitacionPlanificada(new CapacitacionPlanificada());
				this.setDirectorio(new Directorio());
				this.setFechaPlanificada(new Date());
				this.setSelectedObject(new Capacitacion());
				//this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "directorio");
				BindUtils.postNotifyChange(null, null, this, "capacitacionPlanificada");
				BindUtils.postNotifyChange(null, null, this, "fechaPlanificada");
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

	// CAPACITACIONES
	@Override
	public IPayloadResponse<Capacitacion> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadCapacitacionResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar una <b>capacitación</b>";
			}
		}

		if (currentStep == 2)
		{
			try
			{
				UtilValidate.validateDate(this.getFechaPlanificada().getTime(), "Fecha planificada", ValidateOperator.GREATER_THAN, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
				UtilValidate.validateNull(this.getDirectorio().getIdDirectorio(), "Directorio");
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
