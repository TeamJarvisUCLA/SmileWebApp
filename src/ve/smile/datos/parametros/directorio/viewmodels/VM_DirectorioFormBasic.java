package ve.smile.datos.parametros.directorio.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Directorio;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_DirectorioFormBasic extends VM_WindowForm {

	private List<Ciudad> ciudads;
	private List<Multimedia> multimedias;
	private Estado estado;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<Ciudad> getCiudads() {
		if (this.ciudads == null) {
			this.ciudads = new ArrayList<>();
		}
		if (this.ciudads.isEmpty()) {
			PayloadCiudadResponse payloadCiudadsResponse = S.CiudadService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadCiudadsResponse)) {
				Alert.showMessage(payloadCiudadsResponse);
			}

			this.ciudads.addAll(payloadCiudadsResponse.getObjetos());
		}
		return ciudads;
	}

	public void setCiudads(List<Ciudad> ciudads) {
		this.ciudads = ciudads;
	}

	public List<Multimedia> getMultimedias() {
		if (this.multimedias == null) {
			this.multimedias = new ArrayList<>();
		}
		if (this.multimedias.isEmpty()) {
			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadMultimediaResponse)) {
				Alert.showMessage(payloadMultimediaResponse);
			}

			this.multimedias.addAll(payloadMultimediaResponse.getObjetos());
		}
		return multimedias;
	}

	public void setMultimedias(List<Multimedia> multimedias) {
		this.multimedias = multimedias;
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

}
