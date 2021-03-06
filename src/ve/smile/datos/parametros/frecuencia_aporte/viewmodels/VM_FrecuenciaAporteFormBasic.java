package ve.smile.datos.parametros.frecuencia_aporte.viewmodels;

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
import karen.core.util.validate.UtilValidate.ValidateOperator;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.enums.UnidadFrecuenciaAporteEnum;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FrecuenciaAporteFormBasic extends VM_WindowForm {

	private List<UnidadFrecuenciaAporteEnum> unidadFrecuenciaAporteEnums;
	private UnidadFrecuenciaAporteEnum unidadFrecuenciaAporteEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!

		if (getFrecuenciaAporte().getUnidadFrecuenciaAporte() != null) {
			this.setUnidadFrecuenciaAporteEnum(UnidadFrecuenciaAporteEnum
					.values()[getFrecuenciaAporte().getUnidadFrecuenciaAporte()]);
		}
	}

	public List<UnidadFrecuenciaAporteEnum> getUnidadFrecuenciaAporteEnums() {
		if (this.unidadFrecuenciaAporteEnums == null) {
			this.unidadFrecuenciaAporteEnums = new ArrayList<>();
		}
		if (this.unidadFrecuenciaAporteEnums.isEmpty()) {
			for (UnidadFrecuenciaAporteEnum unidadFrecuenciaAporteEnum : UnidadFrecuenciaAporteEnum
					.values()) {
				this.unidadFrecuenciaAporteEnums
						.add(unidadFrecuenciaAporteEnum);
			}
		}
		return unidadFrecuenciaAporteEnums;
	}

	public void setUnidadFrecuenciaAporteEnums(
			List<UnidadFrecuenciaAporteEnum> unidadFrecuenciaAporteEnums) {
		this.unidadFrecuenciaAporteEnums = unidadFrecuenciaAporteEnums;
	}

	public UnidadFrecuenciaAporteEnum getUnidadFrecuenciaAporteEnum() {
		return unidadFrecuenciaAporteEnum;
	}

	public void setUnidadFrecuenciaAporteEnum(
			UnidadFrecuenciaAporteEnum unidadFrecuenciaAporteEnum) {
		this.unidadFrecuenciaAporteEnum = unidadFrecuenciaAporteEnum;
		this.getFrecuenciaAporte().setUnidadFrecuenciaAporte(
				unidadFrecuenciaAporteEnum.ordinal());
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
			UtilValidate.validateNull(getFrecuenciaAporte()
					.getUnidadFrecuenciaAporte(), "Unidad de Medida");
			UtilValidate.validateInteger(getFrecuenciaAporte().getFrecuencia(),
					"Frecuencia", ValidateOperator.GREATER_THAN, 0);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
