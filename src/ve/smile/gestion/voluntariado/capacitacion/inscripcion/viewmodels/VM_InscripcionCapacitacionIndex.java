package ve.smile.gestion.voluntariado.capacitacion.inscripcion.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import lights.core.payload.response.IPayloadResponse;
import lights.core.enums.TypeQuery;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.TsPlanActividadVoluntario;
import ve.smile.dto.Voluntario;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.VolCapacitacionPlanificada;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;

public class VM_InscripcionCapacitacionIndex extends VM_WindowWizard <CapacitacionPlanificada>
{
	private List<Voluntario> voluntariosInscritos;
	
	@Init(superclass = true)
	public void childInit()
	{
		
	}
	
	// VOLUNTARIOS INSCRITOS
	public List<Voluntario> getVoluntariosInscritos()
	{
		return voluntariosInscritos;
	}
		
	public void setVoluntariosInscritos (List<Voluntario> voluntariosInscritos)
	{
		this.voluntariosInscritos = voluntariosInscritos;
	}
	
	@Command("buscarVoluntarios")
	public void buscarVoluntarios()
	{
		CatalogueDialogData<Voluntario> catalogueDialogData = new CatalogueDialogData<Voluntario>();
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Voluntario>()
			{
				@Override
				public void onClose(CatalogueDialogCloseEvent<Voluntario> catalogueDialogCloseEvent)
				{
					if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
					{
						return;
					}
					List<Voluntario> voluntariosInscritos = new ArrayList<Voluntario>();
					voluntariosInscritos = catalogueDialogCloseEvent.getEntities();
					refreshVoluntarios(voluntariosInscritos);
					}
				});
		UtilDialog.showDialog("views/desktop/gestion/voluntariado/capacitacion/inscripcion/catalogoVoluntarios.zul", catalogueDialogData);
	}
	
	public void refreshVoluntarios(List<Voluntario> listVoluntarios)
	{
		boolean validar = true;
		/* EJEMPLO
		TsPlanActividadVoluntario tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
		for (Voluntario voluntario : listVoluntarios)
		{
			for (TsPlanActividadVoluntario tsPlanActividadVoluntario2 : listTsPlanActividads.get(indexActividad).getTsPlanActividadVoluntarios())
			{
				if (tsPlanActividadVoluntario2.getFkVoluntario().getIdVoluntario().equals(voluntario.getIdVoluntario()))
				{
					validar = false;
				}
			}

			if (validar)
			{
				tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
				tsPlanActividadVoluntario.setFkTsPlanActividad(listTsPlanActividads.get(indexActividad));
				tsPlanActividadVoluntario.setFkVoluntario(voluntario);
				this.listTsPlanActividads.get(indexActividad).getTsPlanActividadVoluntarios().add(tsPlanActividadVoluntario);
			}
			validar = true;
		}
		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
		*/
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
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep()
	{
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/registroVoluntarios.zul");
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
	
	@Override
	public void comeIn(Integer currentStep)
	{
		if (currentStep == 1)
		{
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}
	
	// ATRAS
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
			if (this.getCapacitacionPlanificadaSelected() == null)
			{
				return "E:Error Code 5-Debe seleccionar una <b>capacitacion planificada</b>";
			}
		}
		return "";
	}
	
	@Override
	public String isValidSearchDataSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			// BUSCAR VOLUNTARIOS INSCRITOS
			this.setVoluntariosInscritos(new ArrayList<Voluntario>());
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkCapacitacionPlanificada.idCapacitacionPlanificada", String.valueOf(this.getCapacitacionPlanificadaSelected().getIdCapacitacionPlanificada()));
			PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (payloadVolCapacitacionPlanificadaResponse.getObjetos() != null)
			{
				for (VolCapacitacionPlanificada vC : payloadVolCapacitacionPlanificadaResponse.getObjetos())
				{
					this.getVoluntariosInscritos().add(vC.getFkVoluntario());
				}
			}
			BindUtils.postNotifyChange(null, null, this, "*");
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
				if (this.getVoluntariosInscritos() == null || this.getVoluntariosInscritos().size() <= 0)
				{
					return "E:Error Code 5-Debe inscribir <b>al menos un voluntario</b> en la capacitación planificada";
				}
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
			this.getCapacitacionPlanificadaSelected().setVoluntariosInscritos(new ArrayList<Voluntario>());
			this.getCapacitacionPlanificadaSelected().getVoluntariosInscritos().clear();
			this.getCapacitacionPlanificadaSelected().getVoluntariosInscritos().addAll(this.getVoluntariosInscritos());
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.modificar(this.getCapacitacionPlanificadaSelected());
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				restartWizard();
				this.setSelectedObject(new CapacitacionPlanificada());
				//this.getVoluntariosSeleccionados().clear();
				this.getVoluntariosInscritos().clear();
				//this.getVoluntariosInscritosSeleccionados().clear();
				BindUtils.postNotifyChange(null, null, this, "voluntariosSeleccionados");
				BindUtils.postNotifyChange(null, null, this, "voluntariosInscritos");
				BindUtils.postNotifyChange(null, null, this, "voluntariosInscritosSeleccionados");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
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