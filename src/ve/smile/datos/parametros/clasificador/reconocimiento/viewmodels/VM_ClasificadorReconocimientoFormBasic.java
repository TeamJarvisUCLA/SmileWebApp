package ve.smile.datos.parametros.clasificador.reconocimiento.viewmodels;

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
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.enums.TipoReconocimientoEnum;
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ClasificadorReconocimientoFormBasic extends VM_WindowForm {

	private List<TipoReconocimientoEnum> tipoReconocimientoEnums;

	private TipoReconocimientoEnum tipoReconocimientoEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public TipoReconocimientoEnum getTipoReconocimientoEnum() {
		return tipoReconocimientoEnum;
	}

	public void setTipoReconocimientoEnum(
			TipoReconocimientoEnum tipoReconocimientoEnum) {
		this.tipoReconocimientoEnum = tipoReconocimientoEnum;
		this.getClasificadorReconocimiento().setTipoReconocimiento(
				tipoReconocimientoEnum.ordinal());
	}

	public List<TipoReconocimientoEnum> getTipoReconocimientoEnums() {
		if (this.tipoReconocimientoEnums == null) {
			this.tipoReconocimientoEnums = new ArrayList<>();
		}

		if (this.tipoReconocimientoEnums.isEmpty()) {
			for (TipoReconocimientoEnum tipoReconocimientoEnum : TipoReconocimientoEnum
					.values()) {
				this.tipoReconocimientoEnums.add(tipoReconocimientoEnum);
			}
		}

		return tipoReconocimientoEnums;
	}

	public void setTipoReconocimientoEnums(
			List<TipoReconocimientoEnum> tipoReconocimientoEnums) {
		this.tipoReconocimientoEnums = tipoReconocimientoEnums;
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
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.incluir(getClasificadorReconocimiento());

			if (!UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
				Alert.showMessage(payloadClasificadorReconocimientoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.modificar(getClasificadorReconocimiento());

			if (!UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
				Alert.showMessage(payloadClasificadorReconocimientoResponse);
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

	public ClasificadorReconocimiento getClasificadorReconocimiento() {
		return (ClasificadorReconocimiento) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getClasificadorReconocimiento()
					.getNombre(), "Nombre", 100);
			UtilValidate.validateString(getClasificadorReconocimiento()
					.getDescripcion(), "Descripción", 250);
			UtilValidate.validateNull(getClasificadorReconocimiento()
					.getTipoReconocimiento(), "Tipo de Reconocimiento");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

}
