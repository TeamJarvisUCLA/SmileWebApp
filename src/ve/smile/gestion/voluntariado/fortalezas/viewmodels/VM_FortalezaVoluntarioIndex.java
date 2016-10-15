package ve.smile.gestion.voluntariado.fortalezas.viewmodels;

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
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;

public class VM_FortalezaVoluntarioIndex extends VM_WindowWizard<Voluntario>
{
	private Voluntario voluntario;
	
	private List<Fortaleza> fortalezas;
	private Set <Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> voluntarioFortalezas;
	private Set <Fortaleza> voluntarioFortalezasSeleccionadas;

	@Init(superclass = true)
	public void childInit()
	{
		// FORTALEZAS
		if (this.getFortalezas().isEmpty())
		{
			PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService.consultarTodos();
			if (!UtilPayload.isOK(payloadFortalezaResponse))
			{
				Alert.showMessage(payloadFortalezaResponse);
			}
			else
			{
				fortalezas.addAll(payloadFortalezaResponse.getObjetos());
			}		
		}
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
	
	// M�TODOS DE LAS LISTAS
	public boolean disabledFortaleza(Fortaleza fortaleza)
	{
		return this.getVoluntarioFortalezas().contains(fortaleza);
	}
	
	public List<Fortaleza> getFortalezas()
	{
		if (this.fortalezas == null)
		{
			this.fortalezas = new ArrayList<>();
		}

		return fortalezas;
	}

	public void setFortalezas(List<Fortaleza> fortalezas)
	{
		this.fortalezas = fortalezas;
	}

	public Set<Fortaleza> getFortalezasSeleccionadas()
	{
		if (this.fortalezasSeleccionadas == null)
		{
			this.fortalezasSeleccionadas = new HashSet<>();
		}
		return fortalezasSeleccionadas;
	}

	public void setFortalezasSeleccionadas(Set<Fortaleza> fortalezasSeleccionadas)
	{
		this.fortalezasSeleccionadas = fortalezasSeleccionadas;
	}
	
	public List<Fortaleza> getVoluntarioFortalezas()
	{
		if (this.voluntarioFortalezas == null)
		{
			voluntarioFortalezas = new ArrayList<>();
		}
		return voluntarioFortalezas;
	}

	public void setVoluntarioFortalezas (List<Fortaleza> voluntarioFortalezas)
	{
		this.voluntarioFortalezas = voluntarioFortalezas;
	}

	public Set<Fortaleza> getVoluntarioFortalezasSeleccionadas()
	{
		if (this.voluntarioFortalezasSeleccionadas == null)
		{
			this.voluntarioFortalezasSeleccionadas = new HashSet<>();
		}
		return voluntarioFortalezasSeleccionadas;
	}

	public void setVoluntarioFortalezasSeleccionadas(Set<Fortaleza> voluntarioFortalezasSeleccionadas)
	{
		this.voluntarioFortalezasSeleccionadas = voluntarioFortalezasSeleccionadas;
	}
	
	@Command("agregarFortalezas")
	@NotifyChange({"fortalezas", "voluntarioFortalezas", "fortalezasSeleccionadas", "voluntarioFortalezasSeleccionadas" })
	public void agregarFortalezas()
	{
		if (this.getFortalezasSeleccionadas() != null && this.getFortalezasSeleccionadas().size() > 0)
		{
			this.getVoluntarioFortalezas().addAll(fortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getVoluntarioFortalezasSeleccionadas().clear();
		}
	}

	@Command("removerFortalezas")
	@NotifyChange({"fortalezas", "voluntarioFortalezas", "fortalezasSeleccionadas", "voluntarioFortalezasSeleccionadas" })
	public void removerFortalezas()
	{
		if (this.getVoluntarioFortalezasSeleccionadas() != null && this.getVoluntarioFortalezasSeleccionadas().size() > 0)
		{
			this.getVoluntarioFortalezas().removeAll(voluntarioFortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getVoluntarioFortalezasSeleccionadas().clear();
		}
	}
	
	// M�TODOS DEL WIZARD
	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep()
	{
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.FINALIZAR));
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
		urls.add("views/desktop/gestion/voluntariado/fortalezas/selectVoluntario.zul");
		urls.add("views/desktop/gestion/voluntariado/fortalezas/listaFortalezas.zul");
		return urls;
	}
	
	@Override
	public String executeCancelar(Integer currentStep)
	{
		restartWizard();
		return "";
	}

	@Override
	public String executeSiguiente(Integer currentStep)
	{
		if (currentStep == 2)
		{
			// NOTHING
		}
		goToNextStep();
		return "";
	}

	@Override
	public String executeAtras(Integer currentStep)
	{
		goToPreviousStep();
		return "";
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		EstatusVoluntarioEnum.ACTIVO.ordinal();
		criterios.put("estatusVoluntario", String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar un <b>voluntario</b>";
			}
		}
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep)
	{
		if (currentStep == 2)
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
		if (currentStep == 2)
		{
			this.selectedObject.setFortalezas(new ArrayList<Fortaleza>());
			this.selectedObject.getFortalezas().clear();
			this.selectedObject.getFortalezas().addAll(this.getVoluntarioFortalezas());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadVoluntarioResponse))
			{
				restartWizard();
				this.setSelectedObject(new Voluntario());
				this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
			}
			return (String) payloadVoluntarioResponse.getInformacion(IPayloadResponse.MENSAJE);
		}
		return "";
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
}
