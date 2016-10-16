package ve.smile.gestion.voluntariado.capacitacion.inscripcion.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.text.SimpleDateFormat;

import karen.core.crux.alert.Alert;
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
import lights.core.enums.TypeQuery;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.VolCapacitacionPlanificada;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.enums.EstatusCapacitacionPlanificadaEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;

public class VM_InscripcionCapacitacionIndex extends VM_WindowWizard
{
	private VolCapacitacionPlanificada volCapacitacionPlanificada;
	
	private List<Voluntario> voluntarios;
	private Set <Voluntario> voluntariosSeleccionados;
	private List<Voluntario> voluntariosInscritos;
	private Set <Voluntario> voluntariosInscritosSeleccionados;

	@Init(superclass = true)
	public void childInit()
	{
		volCapacitacionPlanificada = new VolCapacitacionPlanificada();
		
		// VOLUNTARIOS
		if (this.getVoluntarios().isEmpty())
		{			
			Map<String, String> criterios = new HashMap<>();
			EstatusVoluntarioEnum.ACTIVO.ordinal();
			criterios.put("estatusVoluntario", String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadVoluntarioResponse))
			{
				Alert.showMessage(payloadVoluntarioResponse);
			}
			else
			{
				voluntarios.addAll(payloadVoluntarioResponse.getObjetos());
			}		
		}
	}
	
	VolCapacitacionPlanificada getVolCapacitacionPlanificada()
	{
		return volCapacitacionPlanificada;
	}
	
	public void setVolCapacitacionPlanificada (VolCapacitacionPlanificada volCapacitacionPlanificada)
	{
		this.volCapacitacionPlanificada = volCapacitacionPlanificada;
	}
	
	// MÉTODOS DE LAS LISTAS
	public boolean disabledVoluntario(Voluntario voluntario)
	{
		return this.getVoluntariosInscritos().contains(voluntario);
	}
	
	public List<Voluntario> getVoluntarios()
	{
		if (this.voluntarios == null)
		{
			this.voluntarios = new ArrayList<>();
		}
		return voluntarios;
	}

	public void setVoluntarios(List<Voluntario> voluntarios)
	{
		this.voluntarios = voluntarios;
	}

	public Set<Voluntario> getVoluntariosSeleccionados()
	{
		if (this.voluntariosSeleccionados == null)
		{
			this.voluntariosSeleccionados = new HashSet<>();
		}
		return voluntariosSeleccionados;
	}

	public void setVoluntariosSeleccionados(Set<Voluntario> voluntariosSeleccionados)
	{
		this.voluntariosSeleccionados = voluntariosSeleccionados;
	}
	
	public List<Voluntario> getVoluntariosInscritos()
	{
		if (this.voluntariosInscritos == null)
		{
			voluntariosInscritos = new ArrayList<>();
		}
		return voluntariosInscritos;
	}

	public void setVoluntariosInscritos (List<Voluntario> voluntariosInscritos)
	{
		this.voluntariosInscritos = voluntariosInscritos;
	}

	public Set<Voluntario> getVoluntariosInscritosSeleccionados()
	{
		if (this.voluntariosInscritosSeleccionados == null)
		{
			this.voluntariosInscritosSeleccionados = new HashSet<>();
		}
		return voluntariosInscritosSeleccionados;
	}

	public void setVoluntariosInscritosSeleccionados(Set<Voluntario> voluntariosInscritosSeleccionados)
	{
		this.voluntariosInscritosSeleccionados = voluntariosInscritosSeleccionados;
	}
		
	@Command("agregarVoluntario")
	@NotifyChange({"voluntarios", "voluntariosInscritos", "voluntariosSeleccionados", "voluntariosInscritosSeleccionados"})
	public void agregarVoluntario()
	{
		if (this.getVoluntariosSeleccionados() != null && this.getVoluntariosSeleccionados().size() > 0)
		{
			this.getVoluntariosInscritos().addAll(voluntariosSeleccionados);
			this.getVoluntariosSeleccionados().clear();
			this.getVoluntariosInscritosSeleccionados().clear();
		}
	}

	@Command("removerVoluntario")
	@NotifyChange({"voluntarios", "voluntariosInscritos", "voluntariosSeleccionados", "voluntariosInscritosSeleccionados"})
	public void removerVoluntario()
	{
		if (this.getVoluntariosInscritosSeleccionados() != null && this.getVoluntariosInscritosSeleccionados().size() > 0)
		{
			this.getVoluntariosInscritos().removeAll(voluntariosInscritosSeleccionados);
			this.getVoluntariosSeleccionados().clear();
			this.getVoluntariosInscritosSeleccionados().clear();
		}
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
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/selectCapacitacion.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/registroVoluntarios.zul");
		urls.add("views/desktop/gestion/voluntariado/capacitacion/inscripcion/registroCompletado.zul");
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
			PayloadVolCapacitacionPlanificadaResponse payloadVolCapacitacionPlanificadaResponse = S.VolCapacitacionPlanificadaService.modificar(volCapacitacionPlanificada);
			if (UtilPayload.isOK(payloadVolCapacitacionPlanificadaResponse))
			{
				this.setSelectedObject(new CapacitacionPlanificada());
				this.setVolCapacitacionPlanificada(new VolCapacitacionPlanificada());
				BindUtils.postNotifyChange(null, null, this, "voluntarios");
				BindUtils.postNotifyChange(null, null, this, "voluntariosSeleccionados");
				BindUtils.postNotifyChange(null, null, this, "voluntariosInscritos");
				BindUtils.postNotifyChange(null, null, this, "voluntariosInscritosSeleccionados");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
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
