package ve.smile.datos.parametros.directorio.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.enums.TypeQuery;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Directorio;
import ve.smile.dto.Estado;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_DirectorioFormBasic extends VM_WindowForm {

	private List<Ciudad> ciudads;
	private Estado estado;

	private List<Estado> estados;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		this.getDirectorio().setLatitud(10.066560);
		this.getDirectorio().setLongitud(-69.312565);
	}

	@Command("changeEstado")
	@NotifyChange({ "ciudads" })
	public void changeEstado() {
		this.getCiudads().clear();
		this.getDirectorio().setFkCiudad(new Ciudad());
		Map<String, String> criterios = new HashMap<>();
		criterios
				.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudads().addAll(payloadCiudadResponse.getObjetos());
	}

	@SuppressWarnings("deprecation")
	@Command
	@NotifyChange({ "directorio" })
	public void changeMarker(@BindingParam("event") MapMouseEvent event) {
		Gmarker gmarker = event.getGmarker();
		if (gmarker != null) {
			gmarker.setOpen(true);
		} else {
			this.getDirectorio().setLatitud(event.getLat());
			this.getDirectorio().setLongitud(event.getLng());
		}
	}

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

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

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
			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.incluir(getDirectorio());

			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				Alert.showMessage(payloadDirectorioResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService
					.modificar(getDirectorio());

			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				Alert.showMessage(payloadDirectorioResponse);
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

	public Directorio getDirectorio() {
		return (Directorio) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getDirectorio().getDireccion(),
					"Dirección", 250);
			UtilValidate.validateString(getDirectorio().getNombre(), "Nombre",
					100);
			UtilValidate.validateString(getDirectorio().getTelefono(),
					"Teléfono", 80);
			UtilValidate.validateString(getDirectorio().getUrl(), "URL", 200);
			UtilValidate.validateNull(getDirectorio().getFkCiudad(), "Ciudad");
			UtilValidate.validateNull(getDirectorio().getFkMultimedia(),
					"Imagen");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

	public List<Ciudad> getCiudads() {
		if (this.ciudads == null) {
			this.ciudads = new ArrayList<>();
		}
		return ciudads;
	}

	public void setCiudads(List<Ciudad> ciudads) {
		this.ciudads = ciudads;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		if (this.estados == null) {
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty()) {
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse)) {
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

}
