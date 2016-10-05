package ve.smile.gestion.voluntariado.fortalezas.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Voluntario;
import ve.smile.dto.VoluntarioFortaleza;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.payload.response.PayloadVoluntarioFortalezaResponse;

public class VM_FortalezaVoluntarioIndex extends VM_WindowWizard<Voluntario>
{
	private Voluntario voluntario;
	
	private List<Fortaleza> fortalezas;
	private Set<Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> fortalezasVoluntario;
	private Set<Fortaleza> fortalezasVoluntarioSeleccionadas;

	@Init(superclass = true)
	public void childInit()
	{
		voluntario = new Voluntario();
		
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
		
		//FORTALEZAS DEL VOLUNTARIO
		if (this.getFortalezasVoluntario().isEmpty())
		{
			Map<String, String> criteriosV = new HashMap<>();
			criteriosV.put("fkVoluntario", String.valueOf(this.getSelectedObject().getIdVoluntario()));
			PayloadVoluntarioFortalezaResponse payloadVoluntarioFortalezaResponse = S.VoluntarioFortalezaService.consultarCriterios(TypeQuery.EQUAL, criteriosV);
			if (!UtilPayload.isOK(payloadVoluntarioFortalezaResponse))
			{
				Alert.showMessage(payloadVoluntarioFortalezaResponse);
			}
			else
			{
				fortalezasVoluntario.addAll(payloadVoluntarioFortalezaResponse.getObjetos());
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
	
	// MÉTODOS DE LAS LISTAS
	
	public boolean disabledFortaleza(Fortaleza fortaleza)
	{
		return this.getFortalezasVoluntario().contains(fortaleza);
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
	
	public List<Fortaleza> getFortalezasVoluntario()
	{
		if (this.fortalezasVoluntario == null)
		{
			fortalezasVoluntario = new ArrayList<>();
		}
		return fortalezasVoluntario;
	}

	public void setFortalezasVoluntario (List<Fortaleza> fortalezasVoluntario)
	{
		this.fortalezasVoluntario = fortalezasVoluntario;
	}

	public Set<Fortaleza> getFortalezasVoluntarioSeleccionadas()
	{
		if (this.fortalezasVoluntarioSeleccionadas == null)
		{
			this.fortalezasVoluntarioSeleccionadas = new HashSet<>();
		}
		return fortalezasVoluntarioSeleccionadas;
	}

	public void setFortalezasVoluntarioSeleccionadas(Set<Fortaleza> fortalezasVoluntarioSeleccionadas)
	{
		this.fortalezasVoluntarioSeleccionadas = fortalezasVoluntarioSeleccionadas;
	}
	
	@Command("agregarFortalezas")
	@NotifyChange({"fortalezas", "fortalezasVoluntario", "fortalezasSeleccionadas", "fortalezasVoluntarioSeleccionadas" })
	public void agregarFortalezas()
	{
		if (this.getFortalezasSeleccionadas() != null && this.getFortalezasSeleccionadas().size() > 0)
		{
			this.getFortalezasVoluntario().addAll(fortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getFortalezasVoluntarioSeleccionadas().clear();
		}
	}

	@Command("removerFortalezas")
	@NotifyChange({"fortalezas", "fortalezasVoluntario", "fortalezasSeleccionadas", "fortalezasVoluntarioSeleccionadas" })
	public void removerIndicadoresPlantilla()
	{
		if (this.getFortalezasVoluntarioSeleccionadas() != null && this.getFortalezasVoluntarioSeleccionadas().size() > 0)
		{
			this.getFortalezasVoluntario().removeAll(fortalezasVoluntarioSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getFortalezasVoluntarioSeleccionadas().clear();
		}
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
			PayloadVoluntarioFortalezaResponse payloadVoluntarioFortalezaResponse = S.VoluntarioFortalezaService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadVoluntarioFortalezaResponse))
			{
				restartWizard();
				this.setSelectedObject(new Voluntario());
				this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
			}
			return (String) payloadVoluntarioFortalezaResponse.getInformacion(IPayloadResponse.MENSAJE);
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
