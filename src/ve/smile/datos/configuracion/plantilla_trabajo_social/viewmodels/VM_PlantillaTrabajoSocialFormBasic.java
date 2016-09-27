package ve.smile.datos.configuracion.plantilla_trabajo_social.viewmodels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.dto.TrabajoSocial;
import ve.smile.dto.Indicador;
import ve.smile.dto.Actividad;
import ve.smile.payload.response.PayloadClasificadorTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.payload.response.PayloadIndicadorResponse;
import ve.smile.payload.response.PayloadActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PlantillaTrabajoSocialFormBasic extends VM_WindowForm {

	private List<Indicador> indicadores;
	private Set<Indicador> indicadoresSeleccionados;
	private List<Indicador> trabajoSocialIndicadores;
	private Set<Indicador> trabajoSocialIndicadoresSeleccionados;

	private List<Actividad> actividades;
	private Set<Actividad> actividadesSeleccionadas;
	private List<Actividad> trabajoSocialActividades;
	private Set<Actividad> trabajoSocialActividadesSeleccionadas;

	private List<ClasificadorTrabajoSocial> clasificadorTrabajoSociales;
	
	@Init(superclass = true)
	public void childInit() {
		this.getTrabajoSocialIndicadores().addAll(
				this.getTrabajoSocial().getTrabajoSocialIndicadores());

		this.getTrabajoSocialActividades().addAll(this.getTrabajoSocial().getTrabajoSocialActividades());

		if (this.getIndicadores().isEmpty()) {
			PayloadIndicadorResponse payloadIndicadorResponse = S.IndicadorService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadIndicadorResponse)) {
				Alert.showMessage(payloadIndicadorResponse);
			} else {
				indicadores.addAll(payloadIndicadorResponse.getObjetos());
			}
		}

		if (this.getActividades().isEmpty()) {
			PayloadActividadResponse payloadActividadResponse = S.ActividadService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadActividadResponse)) {
				Alert.showMessage(payloadActividadResponse);
			} else {
				actividades.addAll(payloadActividadResponse.getObjetos());
			}
		}
	}

	@Command("agregarIndicadoresPlantilla")
	@NotifyChange({ "indicadores", "trabajoSocialIndicadores",
			"indicadoresSeleccionados", "trabajoSocialIndicadoresSeleccionados" })
	public void agregarIndicadoresPlantilla() {
		if (this.getIndicadoresSeleccionados() != null
				&& this.getIndicadoresSeleccionados().size() > 0) {
			this.getTrabajoSocialIndicadores().addAll(indicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getTrabajoSocialIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "indicadores", "trabajoSocialIndicadores",
			"indicadoresSeleccionados", "trabajoSocialIndicadoresSeleccionados" })
	public void removerIndicadoresPlantilla() {
		if (this.getTrabajoSocialIndicadoresSeleccionados() != null
				&& this.getTrabajoSocialIndicadoresSeleccionados().size() > 0) {
			this.getTrabajoSocialIndicadores().removeAll(
					trabajoSocialIndicadoresSeleccionados);
			this.getIndicadoresSeleccionados().clear();
			this.getTrabajoSocialIndicadoresSeleccionados().clear();
		}
	}

	@Command
	@NotifyChange({ "actividades", "trabajoSocialActividades", "actividadesSeleccionadas",
			"trabajoSocialActividadesSeleccionadas" })
	public void agregarActividadesPlantilla() {
		if (this.getActividadesSeleccionadas() != null
				&& this.getActividadesSeleccionadas().size() > 0) {
			this.getTrabajoSocialActividades().addAll(actividadesSeleccionadas);
			this.getTrabajoSocialActividadesSeleccionadas().clear();
			this.getActividadesSeleccionadas().clear();
		}
	}

	@Command
	@NotifyChange({ "actividades", "trabajoSocialActividades", "actividadesSeleccionadas",
			"trabajoSocialActividadesSeleccionadas" })
	public void removerActividadesPlantilla() {
		if (this.getTrabajoSocialActividadesSeleccionadas() != null
				&& this.getTrabajoSocialActividadesSeleccionadas().size() > 0) {
			this.getTrabajoSocialActividades().removeAll(trabajoSocialActividadesSeleccionadas);
			this.getActividadesSeleccionadas().clear();
			this.getTrabajoSocialActividadesSeleccionadas().clear();
		}
	}

	public boolean disabledIndicador(Indicador indicador) {
		return this.getTrabajoSocialIndicadores().contains(indicador);
	}

	public boolean disabledActividad(Actividad actividad) {
		return this.getTrabajoSocialActividades().contains(actividad);
	}

	// Propiedades del formulario
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.getTrabajoSocial().getTrabajoSocialIndicadores().clear();
			this.getTrabajoSocial().getTrabajoSocialIndicadores().addAll(this.getTrabajoSocialIndicadores());
			this.getTrabajoSocial().getTrabajoSocialActividades().clear();
			this.getTrabajoSocial().getTrabajoSocialActividades().addAll(this.getTrabajoSocialActividades());
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService.modificar(getTrabajoSocial());
			if (!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
				return true;
			}
			Alert.showMessage(payloadTrabajoSocialResponse);
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

	public List<ClasificadorTrabajoSocial> getClasificadorTrabajoSociales() {
		if (this.clasificadorTrabajoSociales == null) {
			this.clasificadorTrabajoSociales = new ArrayList<>();
		}
		if (this.clasificadorTrabajoSociales.isEmpty()) {
			PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
				Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
			}

			this.clasificadorTrabajoSociales
					.addAll(payloadClasificadorTrabajoSocialResponse.getObjetos());
		}
		return clasificadorTrabajoSociales;
	}

	public void setClasificadorTrabajoSociales(
			List<ClasificadorTrabajoSocial> clasificadorTrabajoSociales) {
		this.clasificadorTrabajoSociales = clasificadorTrabajoSociales;
	}

	public List<Indicador> getIndicadores() {
		if (this.indicadores == null) {
			this.indicadores = new ArrayList<>();
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public Set<Indicador> getIndicadoresSeleccionados() {
		if (this.indicadoresSeleccionados == null) {
			this.indicadoresSeleccionados = new HashSet<>();
		}
		return indicadoresSeleccionados;
	}

	public void setIndicadoresSeleccionados(
			Set<Indicador> indicadoresSeleccionados) {
		this.indicadoresSeleccionados = indicadoresSeleccionados;
	}

	public List<Indicador> getTrabajoSocialIndicadores() {
		if (this.trabajoSocialIndicadores == null) {
			trabajoSocialIndicadores = new ArrayList<>();
		}
		return trabajoSocialIndicadores;
	}

	public void setTrabajoSocialIndicadores(List<Indicador> trabajoSocialIndicadores) {
		this.trabajoSocialIndicadores = trabajoSocialIndicadores;
	}

	public Set<Indicador> getTrabajoSocialIndicadoresSeleccionados() {
		if (this.trabajoSocialIndicadoresSeleccionados == null) {
			this.trabajoSocialIndicadoresSeleccionados = new HashSet<>();
		}
		return trabajoSocialIndicadoresSeleccionados;
	}

	public void setTrabajoSocialIndicadoresSeleccionados(
			Set<Indicador> trabajoSocialIndicadoresSeleccionados) {
		this.trabajoSocialIndicadoresSeleccionados = trabajoSocialIndicadoresSeleccionados;
	}

	public List<Actividad> getActividades() {
		if (this.actividades == null) {
			this.actividades = new ArrayList<>();
		}
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	public Set<Actividad> getActividadesSeleccionadas() {
		if (this.actividadesSeleccionadas == null) {
			this.actividadesSeleccionadas = new HashSet<>();
		}
		return actividadesSeleccionadas;
	}

	public void setActividadesSeleccionadas(Set<Actividad> actividadesSeleccionadas) {
		this.actividadesSeleccionadas = actividadesSeleccionadas;
	}

	public List<Actividad> getTrabajoSocialActividades() {
		if (this.trabajoSocialActividades == null) {
			this.trabajoSocialActividades = new ArrayList<>();
		}
		return trabajoSocialActividades;
	}

	public void setTrabajoSocialActividades(List<Actividad> trabajoSocialActividades) {
		this.trabajoSocialActividades = trabajoSocialActividades;
	}

	public Set<Actividad> getTrabajoSocialActividadesSeleccionadas() {
		if (this.trabajoSocialActividadesSeleccionadas == null) {
			this.trabajoSocialActividadesSeleccionadas = new HashSet<>();
		}
		return trabajoSocialActividadesSeleccionadas;
	}

	public void setTrabajoSocialActividadesSeleccionadas(
			Set<Actividad> trabajoSocialActividadesSeleccionadas) {
		this.trabajoSocialActividadesSeleccionadas = trabajoSocialActividadesSeleccionadas;
	}

}
