package ve.smile.datos.configuracion;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;

import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;

import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;

import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;

import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;

public class VMVPlantillaTrabajoSocial extends VM_WindowForm {
	
	private List <Indicador> indicadores;
	private Set  <Indicador> indicadoresSel;
	private List <Indicador> trabajoSocialIndicadores;
	private Set  <Indicador> trabajoSocialIndicadoresSel;
	
	private List <Actividad> actividades;
	private Set  <Actividad> actividadesSel;
	private List <Actividad> trabajoSocialActividades;
	private Set  <Actividad> trabajoSocialActividadesSel;
	
	public VMVPlantillaTrabajoSocial () {
		indicadores = new ArrayList <Indicador>();
		indicadoresSel = new HashSet <Indicador> ();
		trabajoSocialIndicadores = new ArrayList <Indicador> ();
		trabajoSocialIndicadoresSel = new HashSet <Indicador>();
	}
	
	@Init(superclass = true)
	public void childInit()
	{
		
	}

	// PROPIEDADES DE LAS LISTAS
	// Indicadores
	public List<Indicador> getIndicadores()
	{
		if (this.indicadores == null)
		{
			this.indicadores = new ArrayList<>();
		}	
		if (this.indicadores.isEmpty())
		{
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse))
			{
				Alert.showMessage(payloadIndicadorResponse);
			}
			this.indicadores.addAll(payloadIndicadorResponse.getObjetos());
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores)
	{
		this.indicadores = indicadores;
	}
	
	public Set<Indicador> getIndicadoresSel()
	{
		return indicadoresSel;
	}

	public void setIndicadoresSel (Set<Indicador> indicadoresSel)
	{
		this.indicadoresSel = indicadoresSel;
	}

	public List <Indicador> getListaIndicadores()
	{
		return indicadores;
	}
	
	// Indicadores del evento
	public Set<Indicador> getTrabajoSocialIndicadoresSel()
	{
		return  trabajoSocialIndicadoresSel;
	}

	public void setTrabajoSocialIndicadoresSel (Set<Indicador> trabajoSocialIndicadoresSel)
	{
		this.trabajoSocialIndicadoresSel = trabajoSocialIndicadoresSel;
	}

	public List <Indicador> getListaTrabajoSocialIndicadores()
	{
		if (this.trabajoSocialIndicadores == null)
		{
			this.trabajoSocialIndicadores = new ArrayList<>();
		}
		if (this.trabajoSocialIndicadores.isEmpty())
		{
			PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse = S.PlantillaTrabajoSocialIndicadorService.consultarPorEvento(getEvento().getIdEvento());	
			if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialIndicadorResponse)) {
				Alert.showMessage(payloadPlantillaTrabajoSocialIndicadorResponse);
			}
			TrabajoSocial t = new TrabajoSocial ();				
			t.setTrabajoSocialIndicadors(payloadPlantillaTrabajoSocialIndicadorResponse.getObjetos());
			this.trabajoSocialIndicadores = t.getTrabajoSocialIndicadors(); 
		}
		return trabajoSocialIndicadores;
	}
	
	// Actividades
	
	public List<Actividad> getActividades()
	{
		if (this.actividades == null)
		{
			this.actividades = new ArrayList<>();
		}
		
		if (this.actividades.isEmpty())
		{
			PayloadActividadResponse payloadActividadResponse = S.ActividadService.consultarTodos();
			if (!UtilPayload.isOK(payloadActividadResponse))
			{
				Alert.showMessage(payloadActividadResponse);
			}
			this.actividades.addAll(payloadActividadResponse.getObjetos());
		}
		return actividades;
	}

	public void setActividades(List<Actividad> actividades)
	{
		this.actividades = actividades;
	}
	
	public Set<Actividad> getActividadesSel()
	{
		return actividadesSel;
	}

	public void setActividadesSel (Set<Actividad> actividadesSel) {
		this.actividadesSel = actividadesSel;
	}

	public List <Actividad> getListaActividades() {
		return actividades;
	}
	
	// Actividades del evento
	public Set<Actividad> getTrabajoSocialActividadesSel()
	{
		return  trabajoSocialActividadesSel;
	}

	public void setTrabajoSocialActividadesSel (Set<Actividad> trabajoSocialActividadesSel)
	{
		this.trabajoSocialActividadesSel = trabajoSocialActividadesSel;
	}

	public List <Actividad> getListaTrabajoSocialActividades()
	{
		return trabajoSocialActividades;
	}
	
	@Command
	@NotifyChange({"indicadores","trabajoSocialIndicadores","indicadoresSel","trabajoSocialIndicadoresSel"})
	public void indicadoresAPlantilla(){
				if(indicadoresSel != null && indicadoresSel.size() > 0){
					trabajoSocialIndicadores.addAll(indicadoresSel);
					indicadores.removeAll(indicadoresSel);
					trabajoSocialIndicadoresSel.addAll(indicadoresSel);
					indicadoresSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"indicadores","trabajoSocialIndicadores","indicadoresSel","trabajoSocialIndicadoresSel"})
	public void indicadoresFueraPlantilla(){
		if(trabajoSocialIndicadoresSel != null && trabajoSocialIndicadoresSel.size() > 0){
			indicadores.addAll(trabajoSocialIndicadoresSel);
			trabajoSocialIndicadores.removeAll(trabajoSocialIndicadoresSel);
			indicadoresSel.addAll(trabajoSocialIndicadoresSel);
			trabajoSocialIndicadoresSel.clear();
		}
	}
	
	@Command
	@NotifyChange({"actividades","trabajoSocialActividades","actividadesSel","trabajoSocialActividadesSel"})
	public void actividadesAPlantilla(){
				if(actividadesSel != null && actividadesSel.size() > 0){
					trabajoSocialActividades.addAll(actividadesSel);
					actividades.removeAll(actividadesSel);
					trabajoSocialActividadesSel.addAll(actividadesSel);
					actividadesSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"actividades","trabajoSocialActividades","actividadesSel","trabajoSocialActividadesSel"})
	public void tareasFueraPlantilla(){
		if(trabajoSocialActividadesSel != null && trabajoSocialActividadesSel.size() > 0){
			actividades.addAll(trabajoSocialActividadesSel);
			trabajoSocialActividades.removeAll(trabajoSocialActividadesSel);
			actividadesSel.addAll(trabajoSocialActividadesSel);
			trabajoSocialActividadesSel.clear();
		}
	}
	
	// Propiedades del formulario
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum)
	{
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if(!isFormValidated())
		{
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			/*
			PayloadPlantillaTrabajoSocialResponse payloadPlantillaTrabajoSocialResponse = S.PlantillaTrabajoSocialService.incluir(getEvento());
			if(!UtilPayload.isOK(payloadPlantillaTrabajoSocialResponse))
			{
				Alert.showMessage(payloadPlantillaTrabajoSocialResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
			*/
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			/*
			PayloadPlantillaTrabajoSocialResponse payloadPlantillaTrabajoSocialResponse = S.PlantillaTrabajoSocialService.modificar(getEvento());
			if(!UtilPayload.isOK(payloadPlantillaTrabajoSocialResponse))
			{
				Alert.showMessage(payloadPlantillaTrabajoSocialResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
			*/
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum)
	{
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum)
	{
		return actionSalir(operacionEnum);
	}

	public TrabajoSocial getTrabajoSocial()
	{
		return (TrabajoSocial) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
