package ve.smile.gestion.trabajadores.fortalezas.viewmodels;

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
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadFortalezaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;

public class VM_FortalezaTrabajadorIndex extends VM_WindowWizard<Trabajador>
{
	
	private List<Fortaleza> fortalezas;
	private Set <Fortaleza> fortalezasSeleccionadas;
	private List<Fortaleza> trabajadorFortalezas;
	private Set <Fortaleza> trabajadorFortalezasSeleccionadas;

	@Init(superclass = true)
	public void childInit()
	{
		
		//	FORTALEZAS
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

	// M�TODOS DE LAS LISTAS
	public boolean disabledFortaleza(Fortaleza fortaleza)
	{
		return this.getTrabajadorFortalezas().contains(fortaleza);
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
	
	public List<Fortaleza> getTrabajadorFortalezas()
	{
		if (this.trabajadorFortalezas == null)
		{
			trabajadorFortalezas = new ArrayList<>();
		}
		return trabajadorFortalezas;
	}

	public void setTrabajadorFortalezas (List<Fortaleza> trabajadorFortalezas)
	{
		this.trabajadorFortalezas = trabajadorFortalezas;
	}

	public Set<Fortaleza> getTrabajadorFortalezasSeleccionadas()
	{
		if (this.trabajadorFortalezasSeleccionadas == null)
		{
			this.trabajadorFortalezasSeleccionadas = new HashSet<>();
		}
		return trabajadorFortalezasSeleccionadas;
	}

	public void setTrabajadorFortalezasSeleccionadas(Set<Fortaleza> trabajadorFortalezasSeleccionadas)
	{
		this.trabajadorFortalezasSeleccionadas = trabajadorFortalezasSeleccionadas;
	}
	
	@Command("agregarFortalezas")
	@NotifyChange({"fortalezas", "trabajadorFortalezas", "fortalezasSeleccionadas", "trabajadorFortalezasSeleccionadas" })
	public void agregarFortalezas()
	{
		if (this.getFortalezasSeleccionadas() != null && this.getFortalezasSeleccionadas().size() > 0)
		{
			this.getTrabajadorFortalezas().addAll(fortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getTrabajadorFortalezasSeleccionadas().clear();
		}
	}

	@Command("removerFortalezas")
	@NotifyChange({"fortalezas", "trabajadorFortalezas", "fortalezasSeleccionadas", "trabajadorFortalezasSeleccionadas" })
	public void removerFortalezas()
	{
		if (this.getTrabajadorFortalezasSeleccionadas() != null && this.getTrabajadorFortalezasSeleccionadas().size() > 0)
		{
			this.getTrabajadorFortalezas().removeAll(trabajadorFortalezasSeleccionadas);
			this.getFortalezasSeleccionadas().clear();
			this.getTrabajadorFortalezasSeleccionadas().clear();
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
		urls.add("views/desktop/gestion/trabajadores/fortalezas/selectTrabajador.zul");
		urls.add("views/desktop/gestion/trabajadores/fortalezas/listaFortalezas.zul");
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
		if (currentStep == 1)
		{			
			if (getSelectedObject().getFortalezas() == null
					|| getSelectedObject().getFortalezas().size() == 0) {

				PayloadFortalezaResponse payloadFortalezaResponse = S.FortalezaService
						.consultarPorTrabajador(getSelectedObject()
								.getIdTrabajador());

				if (!UtilPayload.isOK(payloadFortalezaResponse)) {
					Alert.showMessage(payloadFortalezaResponse);
				}
				
				getSelectedObject().setFortalezas(payloadFortalezaResponse.getObjetos());
			}
			
			this.getTrabajadorFortalezas().addAll(getSelectedObject().getFortalezas());
		}
		goToNextStep();
		return "";
	}

	@Override
	public String executeAtras(Integer currentStep)
	{
		this.getTrabajadorFortalezas().clear();
		goToPreviousStep();
		return "";
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador", String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}


	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar un <b>trabajador</b>";
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
			this.selectedObject.getFortalezas().addAll(this.getTrabajadorFortalezas());
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadTrabajadorResponse))
			{
				restartWizard();
				this.setSelectedObject(new Trabajador());
				this.getTrabajadorFortalezas().clear();
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "trabajador");
			}
			return (String) payloadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE);
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
