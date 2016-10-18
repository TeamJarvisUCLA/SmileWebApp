package ve.smile.gestion.voluntariado.capacitacion.inscripcion.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.payload.UtilPayload;
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
import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadVolCapacitacionPlanificadaResponse;

public class VM_InscripcionCapacitacionIndex extends VM_WindowWizard<CapacitacionPlanificada>
{
	private CapacitacionPlanificada capacitacionPlanificada;
	
	private List<Voluntario> voluntarios;
	private Set <Voluntario> voluntariosSeleccionados;
	private List<Voluntario> voluntariosInscritos;
	private Set <Voluntario> voluntariosInscritosSeleccionados;

	@Init(superclass = true)
	public void childInit()
	{
		capacitacionPlanificada = new CapacitacionPlanificada();
		
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
	
	CapacitacionPlanificada getCapacitacionPlanificada()
	{
		return capacitacionPlanificada;
	}
	
	public void setCapacitacionPlanificada (CapacitacionPlanificada capacitacionPlanificada)
	{
		this.capacitacionPlanificada = capacitacionPlanificada;
	}
	
	// M�TODOS DE LAS LISTAS
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
	
	// ATR�S
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
			this.selectedObject.setVoluntariosInscritos(new ArrayList<Voluntario>());
			this.selectedObject.getVoluntariosInscritos().clear();
			this.selectedObject.getVoluntariosInscritos().addAll(this.getVoluntariosInscritos());
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				this.setSelectedObject(new CapacitacionPlanificada());
				this.setCapacitacionPlanificada(new CapacitacionPlanificada());
				this.getVoluntariosSeleccionados().clear();
				this.getVoluntariosInscritos().clear();
				this.getVoluntariosInscritosSeleccionados().clear();
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
				return "E:Error Code 5-Debe seleccionar una <b>capacitaci�n planificada</b>";
			}
		}

		if (currentStep == 2)
		{
			try
			{
				if (this.getVoluntariosInscritos() == null || this.getVoluntariosInscritos().size() <= 0)
				{
					return "E:Error Code 5-Debe inscribir <b>al menos un voluntario</b> en la capacitaci�n planificada";
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
	public String isValidSearchDataSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			// BUSCAR VOLUNTARIOS INSCRITOS
			this.setVoluntariosInscritos(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkCapacitacionPlanificada.idCapacitacionPlanificada", String.valueOf(this.getSelectedObject().getIdCapacitacionPlanificada()));
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
