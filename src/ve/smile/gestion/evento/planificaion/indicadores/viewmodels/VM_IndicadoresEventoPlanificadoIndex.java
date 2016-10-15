package ve.smile.gestion.evento.planificaion.indicadores.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Indicador;
import ve.smile.dto.IndicadorEventoPlanificado;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_IndicadoresEventoPlanificadoIndex extends VM_WindowWizard{
	
	private List<Indicador> indicadores = new ArrayList<>();
	private Set<Indicador> indicadoresSeleccionados = new HashSet<>();
	private List<Indicador> eventoIndicadores = new ArrayList<>();
	private Set<Indicador> eventoIndicadoresSeleccionados = new HashSet<>();

	private List<IndicadorEventoPlanificado> indicadorEventoPlanificado = new ArrayList<>();

	private List<Indicador> EventoIndicadoresAux = new ArrayList<>();
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Command("agregarIndicadoresPlantilla")
	@NotifyChange({ "indicadores", "eventoIndicadores",
			"indicadoresSeleccionados", "eventoIndicadoresSeleccionados" })
	public void agregarIndicadoresPlantilla() {
		if (this.getIndicadoresSeleccionados() != null
				&& this.getIndicadoresSeleccionados().size() > 0) {
			this.getEventoIndicadores().addAll(indicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getEventoIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "indicadores", "eventoIndicadores",
			"indicadoresSeleccionados", "eventoIndicadoresSeleccionados" })
	public void removerIndicadoresPlantilla() {
		if (this.getEventoIndicadoresSeleccionados() != null
				&& this.getEventoIndicadoresSeleccionados().size() > 0) {
			this.getEventoIndicadores().removeAll(
					eventoIndicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getEventoIndicadoresSeleccionados().clear();
		}
	}

	public boolean disabledIndicador(Indicador indicador) {
		return this.getEventoIndicadores().contains(indicador);
	}
	
	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);
		botones.put(4, listOperacionWizard4);

		return botones;
	}
	
	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			/*Map<String, String> criterios = new HashMap<>();
			EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
			criterios.put("fkEventoPlanificado.idEventoPlanificado", eventoPlanificado.getIdEventoPlanificado()+"");
			PayloadIndicadorEventoPlanificadoResponse indicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService.consultarCriterios(TypeQuery.EQUAL, criterios);
			if(indicadorEventoPlanificadoResponse.getObjetos() != null & indicadorEventoPlanificadoResponse.getObjetos().size()>0){
				for(IndicadorEventoPlanificado indicadorEventoPlanificado: indicadorEventoPlanificadoResponse.getObjetos()){
					S.IndicadorEventoPlanificadoService.eliminar(indicadorEventoPlanificado.getIdIndicadorEventoPlanificado());					
				}		
			}*/
			
			for (IndicadorEventoPlanificado indicadorEventPlan : this.getIndicadorEventoPlanificado()) {
				PayloadIndicadorEventoPlanificadoResponse payloadIndicadorEventPlanResponse = S.IndicadorEventoPlanificadoService
						.incluir(indicadorEventPlan);
				if (!UtilPayload.isOK(payloadIndicadorEventPlanResponse)) {
					return (String) payloadIndicadorEventPlanResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}

			goToNextStep();
		}

		return "";
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		
		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/indicadores/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/indicadores/selectIndicadoresEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/indicadores/editIndicadoresEventoPlanificado.zul");
		urls.add("views/desktop/gestion/evento/planificacion/indicadores/registroCompletado.zul");
		return urls;
	}
	
	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}
		}

		if (currentStep == 2) {
			if (this.getEventoIndicadores().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al evento planificado.";
			}

		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if(currentStep == 1){
			this.eventoIndicadores = new ArrayList<>();
			Map<String, String> criterios = new HashMap<>();
			EventoPlanificado eventoPlanificado = (EventoPlanificado)selectedObject;
			criterios.put("fkEventoPlanificado.idEventoPlanificado", eventoPlanificado.getIdEventoPlanificado()+"");
			PayloadIndicadorEventoPlanificadoResponse indicadorEventoPlanificadoResponse = S.IndicadorEventoPlanificadoService.consultarCriterios(TypeQuery.EQUAL, criterios);
			
			if(indicadorEventoPlanificadoResponse.getObjetos() != null){				
				for(IndicadorEventoPlanificado indicadorEventoPlanificado: indicadorEventoPlanificadoResponse.getObjetos()){
					if(this.eventoIndicadores.size()==0){
					this.eventoIndicadores.add(indicadorEventoPlanificado.getFkIndicador()); 
					}
				}								
			}
			BindUtils.postNotifyChange(null, null, this, "eventoIndicadores");
		}
		
		
		if (currentStep == 2) {
			if (this.getEventoIndicadores().isEmpty()) {
				return "E:Error Code 5-Debe agregar al menos un <b>Indicador</b> al evento planificado.";
			} else {
				this.getIndicadorEventoPlanificado().clear();
				for (Indicador indicador : this.getEventoIndicadores()) {
					IndicadorEventoPlanificado indicadorEventoPlanificado = new IndicadorEventoPlanificado();
					indicadorEventoPlanificado.setFkEventoPlanificado((EventoPlanificado) selectedObject);
					indicadorEventoPlanificado.setFkIndicador(indicador);
					this.getIndicadorEventoPlanificado().add(indicadorEventoPlanificado);
				}
				BindUtils
						.postNotifyChange(null, null, this, "indicadorEventoPlanificado");
			}

		}

		return "";
	}
	
	@Override
	public String executeCustom1(Integer currentStep) {
		this.setIndicadorEventoPlanificado(new ArrayList<IndicadorEventoPlanificado>());
		this.setEventoIndicadores(new ArrayList<Indicador>());
		this.setSelectedObject(new EventoPlanificado());

		BindUtils.postNotifyChange(null, null, this, "indicadorEventoPlanificado");
		BindUtils
				.postNotifyChange(null, null, this, "eventoIndicadores");
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
		
	}

	public List<Indicador> getIndicadores() {
		if (this.indicadores == null) {
			this.indicadores = new ArrayList<>();
		}
		if (this.indicadores.isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			} else {
				indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public Set<Indicador> getIndicadoresSeleccionados() {
		return indicadoresSeleccionados;
	}

	public void setIndicadoresSeleccionados(Set<Indicador> indicadoresSeleccionados) {
		this.indicadoresSeleccionados = indicadoresSeleccionados;
	}

	public List<Indicador> getEventoIndicadores() {
		return eventoIndicadores;
	}

	public void setEventoIndicadores(List<Indicador> eventoIndicadores) {
		this.eventoIndicadores = eventoIndicadores;
	}

	public Set<Indicador> getEventoIndicadoresSeleccionados() {
		return eventoIndicadoresSeleccionados;
	}

	public void setEventoIndicadoresSeleccionados(
			Set<Indicador> eventoIndicadoresSeleccionados) {
		this.eventoIndicadoresSeleccionados = eventoIndicadoresSeleccionados;
	}

	public List<IndicadorEventoPlanificado> getIndicadorEventoPlanificado() {
		return indicadorEventoPlanificado;
	}

	public void setIndicadorEventoPlanificado(
			List<IndicadorEventoPlanificado> indicadorEventoPlanificado) {
		this.indicadorEventoPlanificado = indicadorEventoPlanificado;
	}

	public List<Indicador> getEventoIndicadoresAux() {
		return EventoIndicadoresAux;
	}

	public void setEventoIndicadoresAux(List<Indicador> eventoIndicadoresAux) {
		EventoIndicadoresAux = eventoIndicadoresAux;
	}
	
	

}
