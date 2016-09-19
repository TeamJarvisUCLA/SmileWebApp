package ve.smile.datos.parametros.participacion.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

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
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.dto.Participacion;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.dto.Multimedia;

public class VM_ParticipacionFormBasic extends VM_WindowForm {

	private List<Multimedia> multimedias;
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
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

			this.multimedias
					.addAll(payloadMultimediaResponse.getObjetos());
		}
		return multimedias;
	}

	public void setMultimedias(
			List<Multimedia> multimedias) {
		this.multimedias = multimedias;
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

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
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadParticipacionResponse payloadParticipacionResponse =
					S.ParticipacionService.incluir(getParticipacion());

			if(!UtilPayload.isOK(payloadParticipacionResponse)) {
				Alert.showMessage(payloadParticipacionResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadParticipacionResponse payloadParticipacionResponse =
					S.ParticipacionService.modificar(getParticipacion());

			if(!UtilPayload.isOK(payloadParticipacionResponse)) {
				Alert.showMessage(payloadParticipacionResponse);
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

	public Participacion getParticipacion() {
		return (Participacion) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		//TODO: Validar multimedia
		try{
			UtilValidate.validateString(getParticipacion().getNombre(), "Nombre", 100);
			UtilValidate.validateString(getParticipacion().getDescripcion(), "Descripción", 250);
			return true;
		}catch(Exception e){
			Alert.showMessage(e.getMessage());
			return false;
		}		
	}
}
