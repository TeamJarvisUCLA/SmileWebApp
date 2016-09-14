package ve.smile.datos.configuracion;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.dto.Indicador;
import ve.smile.payload.response.PayloadIndicadorResponse;
//import ve.smile.dto.PlantillaTrabajoSocialIndicador;
//import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadActividadResponse;
//import ve.smile.dto.PlantillaTrabajoSocialActividad;
//import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VMVPlantillaTrabajoSocial extends VM_WindowForm {

	private List <Indicador> indicadores;
	private Set  <Indicador> indicadoresSel;
	private List <PlantillaTrabajoSocialIndicador> plantillatrabajosocialindicadores;
	private Set  <PlantillaTrabajoSocialIndicador> plantillatrabajosocialindicadoresSel;
	
	private List <Actividad> actividades;
	private Set  <Actividad> actividadesSel;
	private List <PlantillaTrabajoSocialActividad> plantillatrabajosocialactividades;
	private Set  <PlantillaTrabajoSocialActividad> plantillatrabajosocialactividadesSel;
	
	public VMVPlantillaTrabajoSocial () {
		indicadores = new ArrayList <Indicador>();
		indicadoresSel = new HashSet <Indicador> ();
		plantillatrabajosocialindicadores = new ArrayList <PlantillaTrabajoSocialIndicador>();
		plantillatrabajosocialindicadoresSel = new HashSet <PlantillaTrabajoSocialIndicador>();
		
		actividades = new ArrayList <Actividad>();
		actividadesSel = new HashSet <Actividad> ();
		plantillatrabajosocialactividades = new ArrayList <PlantillaTrabajoSocialActividad>();
		plantillatrabajosocialactividadesSel = new HashSet <PlantillaTrabajoSocialActividad>();
	}
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	// INDICADORES
		public List<Indicador> getIndicadores() {
			if (this.indicadores == null) {
				this.indicadores = new ArrayList<>();
			}
			if (this.indicadores.isEmpty()) {
				PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService.consultarTodos();

				if (!UtilPayload.isOK(payloadIndicadorResponse)) {
					Alert.showMessage(payloadIndicadorResponse);
				}

				this.indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
			return indicadores;
		}

		public void setIndicadores(List<Indicador> indicadores) {
			this.indicadores = indicadores;
		}
	
	// INDICADORES DEL TRABAJO SOCIAL
		public List<PlantillaTrabajoSocialIndicador> getPlantillaTrabajoSocialIndicadores() {
			if (this.plantillatrabajosocialindicadores == null) {
				this.plantillatrabajosocialindicadores = new ArrayList<>();
			}
			if (this.plantillatrabajosocialindicadores.isEmpty()) {
				PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse = S.PlantillaTrabajoSocialIndicadorService.consultarPorTrabajoSocial(getTrabajoSocial().getIdTrabajoSocial());	
				if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialIndicadorResponse)) {
					Alert.showMessage(payloadPlantillaTrabajoSocialIndicadorResponse);
				}
				TrabajoSocial t = new TrabajoSocial ();				
				t.setTrabajoSocialIndicadors(payloadPlantillaTrabajoSocialIndicadorResponse.getObjetos());
				this.plantillatrabajosocialindicadores = t.getTrabajoSocialIndicadors();
			}
			return plantillatrabajosocialindicadores;
		}

		public void setPlantillaTrabajoSocialIndicadores(List<PlantillaTrabajoSocialIndicador> plantillatrabajosocialindicadores) {
			this.plantillatrabajosocialindicadores = plantillatrabajosocialindicadores;
		}
	
	// ACTIVIDADES
		public List<Actividad> getActividades() {
			if (this.actividades == null) {
				this.actividades = new ArrayList<>();
			}
			if (this.actividades.isEmpty()) {
				PayloadActividadResponse payloadActividadResponse = S.ActividadService.consultarTodos();

				if (!UtilPayload.isOK(payloadActividadResponse)) {
					Alert.showMessage(payloadActividadResponse);
				}
				this.actividades.addAll(payloadActividadResponse.getObjetos());
			}
			return actividades;
		}

		public void setActividades(List<Actividad> actividades) {
			this.actividades = actividades;
		}
	
	// ACTIVIDADES DE TRABAJO SOCIAL
		public List<PlantillaTrabajoSocialActividad> getPlantillaTrabajoSocialActividades() {
			if (this.plantillatrabajosocialactividades == null) {
				this.plantillatrabajosocialactividades = new ArrayList<>();
			}
			if (this.plantillatrabajosocialactividades.isEmpty()) {
				PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse = S.PlantillaTrabajoSocialActividadService.consultarPorTrabajoSocial(getTrabajoSocial().getIdTrabajoSocial());	
				if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialActividadResponse)) {
					Alert.showMessage(payloadPlantillaTrabajoSocialActividadResponse);
				}
				TrabajoSocial t = new TrabajoSocial ();				
				t.setTrabajoSocialActividades(payloadPlantillaTrabajoSocialActividadResponse.getObjetos());
				this.plantillatrabajosocialactividades = t.getTrabajoSocialActividades();
			}
			return plantillatrabajosocialactividades;
		}

		public void setPlantillaTrabajoSocialActividades(List<PlantillaTrabajoSocialActividad> plantillatrabajosocialactividades) {
			this.plantillatrabajosocialactividades = plantillatrabajosocialactividades;
		}
	
	// MÉTODOS DE LAS LISTAS
	
	public Set<Indicador> getIndicadoresSel() {
		return indicadoresSel;
	}

	public void setIndicadoresSel (Set<Indicador> indicadoresSel) {
		this.indicadoresSel = indicadoresSel;
	}

	public List <Indicador> getListaIndicadores() {
		return indicadores;
	}
	
	public Set<PlantillaTrabajoSocialIndicador> getPlantillaTrabajoSocialIndicadoresSel() {
		return plantillatrabajosocialindicadoresSel;
	}

	public void setPlantillaTrabajoSocialIndicadoresSel (Set<PlantillaTrabajoSocialIndicador> plantillatrabajosocialindicadoresSel) {
		this.plantillatrabajosocialindicadoresSel = plantillatrabajosocialindicadoresSel;
	}

	public List <PlantillaTrabajoSocialIndicador> getListaPlantillaTrabajoSocialIndicadores() {
		return plantillatrabajosocialindicadores;
	}
	
	public Set <Actividad> getActividadesSel() {
		return actividadesSel;
	}

	public void setActividadesSel (Set<Actividad> actividadesSel) {
		this.actividadesSel = actividadesSel;
	}

	public List <Actividad> getListaActividades() {
		return actividades;
	}
	
	public Set <PlantillaTrabajoSocialActividad> getPlantillaTrabajoSocialActividadesSel() {
		return plantillatrabajosocialactividadesSel;
	}

	public void setPlantillaTrabajoSocialActividadesSel (Set<PlantillaTrabajoSocialActividad> plantillatrabajosocialactividadesSel) {
		this.plantillatrabajosocialactividadesSel = plantillatrabajosocialactividadesSel;
	}

	public List <PlantillaTrabajoSocialActividad> getListaPlantillaTrabajoSocialActividades() {
		return plantillatrabajosocialactividades;
	}
			
	@Command
	@NotifyChange({"indicadores","plantillatrabajosocialindicadores","indicadoresSel","plantillatrabajosocialindicadoresSel"})
	public void indicadoresAPlantilla(){
				if(indicadoresSel != null && indicadoresSel.size() > 0){
					plantillatrabajosocialindicadores.addAll(indicadoresSel);
					indicadores.removeAll(indicadoresSel);
					plantillatrabajosocialindicadoresSel.addAll(indicadoresSel);
					indicadoresSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"indicadores","plantillatrabajosocialindicadores","indicadoresSel","plantillatrabajosocialindicadoresSel"})
	public void indicadoresFueraPlantilla(){
		if(plantillatrabajosocialindicadoresSel != null && plantillatrabajosocialindicadoresSel.size() > 0){
			indicadores.addAll(plantillatrabajosocialindicadoresSel);
			plantillatrabajosocialindicadores.removeAll(plantillatrabajosocialindicadoresSel);
			indicadoresSel.addAll(plantillatrabajosocialindicadoresSel);
			plantillatrabajosocialindicadoresSel.clear();
		}
	}
	
	@Command
	@NotifyChange({"actividades","plantillatrabajosocialactividades","actividadesSel","plantillatrabajosocialactividadesSel"})
	public void actividadesAPlantilla(){
				if(actividadesSel != null && actividadesSel.size() > 0){
					plantillatrabajosocialactividades.addAll(actividadesSel);
					actividades.removeAll(actividadesSel);
					plantillatrabajosocialactividadesSel.addAll(actividadesSel);
					actividadesSel.clear();
				}
			}
	
	@Command
	@NotifyChange({"actividades","plantillatrabajosocialactividades","actividadesSel","plantillatrabajosocialactividadesSel"})
	public void actividadesFueraPlantilla(){
		if(plantillatrabajosocialactividadesSel != null && plantillatrabajosocialactividadesSel.size() > 0){
			actividades.addAll(plantillatrabajosocialactividadesSel);
			plantillatrabajosocialactividades.removeAll(plantillatrabajosocialactividadesSel);
			actividadesSel.addAll(plantillatrabajosocialactividadesSel);
			plantillatrabajosocialactividadesSel.clear();
		}
	}
	
	// MÉTODOS DEL FORMULARIO

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService.incluir(getTrabajoSocial());

			if (!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService.modificar(getTrabajoSocial());

			if (!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	public TrabajoSocial getTrabajoSocial() {
		return (TrabajoSocial) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
