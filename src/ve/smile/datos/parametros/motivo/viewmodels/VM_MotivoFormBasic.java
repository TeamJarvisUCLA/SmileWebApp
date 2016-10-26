package ve.smile.datos.parametros.motivo.viewmodels;

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
import ve.smile.payload.response.PayloadClasificadorMotivoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.dto.ClasificadorMotivo;
import ve.smile.dto.Motivo;

public class VM_MotivoFormBasic extends VM_WindowForm {
	
	private List<ClasificadorMotivo> clasificadorMotivos;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	
	public List<ClasificadorMotivo> getClasificadorMotivos() {
		if (this.clasificadorMotivos == null) {
			this.clasificadorMotivos = new ArrayList<>();
		}
		if (this.clasificadorMotivos.isEmpty()) {
			PayloadClasificadorMotivoResponse payloadClasificadorMotivoResponse = S.ClasificadorMotivoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorMotivoResponse)) {
				Alert.showMessage(payloadClasificadorMotivoResponse);
			}

			this.clasificadorMotivos
					.addAll(payloadClasificadorMotivoResponse.getObjetos());
		}
		return clasificadorMotivos;
	}

	public void setClasificadorMotivos(List<ClasificadorMotivo> clasificadorMotivos) {
		this.clasificadorMotivos = clasificadorMotivos;
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
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.incluir(getMotivo());

			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.modificar(getMotivo());

			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
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

	public Motivo getMotivo() {
		return (Motivo) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getMotivo().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getMotivo().getFkClasificadorMotivo(),
					"Clasificador de Motivo");
			UtilValidate.validateString(getMotivo().getDescripcion(),
					"Descripción", 250);

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}
}
