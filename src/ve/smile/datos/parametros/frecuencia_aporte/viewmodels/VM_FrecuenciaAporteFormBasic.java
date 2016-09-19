package ve.smile.datos.parametros.frecuencia_aporte.viewmodels;

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
import karen.core.util.validate.UtilValidate.ValidateOperator;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.UnidadMedida;
import ve.smile.payload.response.PayloadUnidadMedidaResponse;

public class VM_FrecuenciaAporteFormBasic extends VM_WindowForm {

	private List<UnidadMedida> unidadMedidas;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<UnidadMedida> getUnidadMedidas() {
		if (this.unidadMedidas == null) {
			this.unidadMedidas = new ArrayList<>();
		}
		if (this.unidadMedidas.isEmpty()) {
			PayloadUnidadMedidaResponse payloadUnidadMedidaResponse = S.UnidadMedidaService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadUnidadMedidaResponse)) {
				Alert.showMessage(payloadUnidadMedidaResponse);
			}

			this.unidadMedidas.addAll(payloadUnidadMedidaResponse.getObjetos());
		}
		return unidadMedidas;
	}

	public void setUnidadMedidas(List<UnidadMedida> unidadMedidas) {
		this.unidadMedidas = unidadMedidas;
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
			PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
					.incluir(getFrecuenciaAporte());

			if (!UtilPayload.isOK(payloadFrecuenciaAporteResponse)) {
				Alert.showMessage(payloadFrecuenciaAporteResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
					.modificar(getFrecuenciaAporte());

			if (!UtilPayload.isOK(payloadFrecuenciaAporteResponse)) {
				Alert.showMessage(payloadFrecuenciaAporteResponse);
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

	public FrecuenciaAporte getFrecuenciaAporte() {
		return (FrecuenciaAporte) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getFrecuenciaAporte().getNombre(),
					"Nombre", 100);
			UtilValidate.validateNull(
					getFrecuenciaAporte().getFkUnidadMedida(),
					"Unidad de Medida");
			UtilValidate.validateInteger(getFrecuenciaAporte().getFrecuencia(),
					"Frecuencia", ValidateOperator.GREATER_THAN, 0);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
