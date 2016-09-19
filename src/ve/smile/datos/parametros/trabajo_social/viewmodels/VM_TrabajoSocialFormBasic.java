package ve.smile.datos.parametros.trabajo_social.viewmodels;

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
import ve.smile.payload.response.PayloadClasificadorTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.dto.ClasificadorTrabajoSocial;
import ve.smile.dto.TrabajoSocial;

public class VM_TrabajoSocialFormBasic extends VM_WindowForm {

	private List<ClasificadorTrabajoSocial> clasificadorTrabajoSocials;
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}
	
	public List<ClasificadorTrabajoSocial> getClasificadorTrabajoSocials() {
		if (this.clasificadorTrabajoSocials == null) {
			this.clasificadorTrabajoSocials = new ArrayList<>();
		}
		if (this.clasificadorTrabajoSocials.isEmpty()) {
			PayloadClasificadorTrabajoSocialResponse payloadClasificadorTrabajoSocialResponse = S.ClasificadorTrabajoSocialService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorTrabajoSocialResponse)) {
				Alert.showMessage(payloadClasificadorTrabajoSocialResponse);
			}

			this.clasificadorTrabajoSocials.addAll(payloadClasificadorTrabajoSocialResponse.getObjetos());
		}
		return clasificadorTrabajoSocials;
	}

	public void setClasificadorTrabajoSocials(
			List<ClasificadorTrabajoSocial> clasificadorTrabajoSocials) {
		this.clasificadorTrabajoSocials = clasificadorTrabajoSocials;
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
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse =
					S.TrabajoSocialService.incluir(getTrabajoSocial());

			if(!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
				Alert.showMessage(payloadTrabajoSocialResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadTrabajoSocialResponse payloadTrabajoSocialResponse =
					S.TrabajoSocialService.modificar(getTrabajoSocial());

			if(!UtilPayload.isOK(payloadTrabajoSocialResponse)) {
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
			UtilValidate.validateString(getTrabajoSocial().getNombre(), "Nombre",
					100);
			UtilValidate.validateNull(getTrabajoSocial().getFkClasificadorTrabajoSocial(),
					"Clasificador de Trabajo Social");
			UtilValidate.validateString(getTrabajoSocial().getDescripcion(),
					"Descripción", 250);
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
