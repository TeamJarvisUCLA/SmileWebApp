package ve.smile.datos.parametros.recurso.viewmodels;

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
import ve.smile.dto.ClasificadorRecurso;
import ve.smile.dto.Recurso;
import ve.smile.dto.UnidadMedida;
import ve.smile.payload.response.PayloadClasificadorRecursoResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.payload.response.PayloadUnidadMedidaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RecursoFormBasic extends VM_WindowForm {

	private List<ClasificadorRecurso> clasificadorRecursos;
	private List<UnidadMedida> unidadMedidas;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<ClasificadorRecurso> getClasificadorRecursos() {
		if (this.clasificadorRecursos == null) {
			this.clasificadorRecursos = new ArrayList<>();
		}
		if (this.clasificadorRecursos.isEmpty()) {
			PayloadClasificadorRecursoResponse payloadClasificadorRecursoResponse = S.ClasificadorRecursoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorRecursoResponse)) {
				Alert.showMessage(payloadClasificadorRecursoResponse);
			}

			this.clasificadorRecursos.addAll(payloadClasificadorRecursoResponse
					.getObjetos());
		}
		return clasificadorRecursos;
	}

	public void setClasificadorRecursos(
			List<ClasificadorRecurso> clasificadorRecursos) {
		this.clasificadorRecursos = clasificadorRecursos;
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
			PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
					.incluir(getRecurso());

			if (!UtilPayload.isOK(payloadRecursoResponse)) {
				Alert.showMessage(payloadRecursoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
					.modificar(getRecurso());

			if (!UtilPayload.isOK(payloadRecursoResponse)) {
				Alert.showMessage(payloadRecursoResponse);
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

	public Recurso getRecurso() {
		return (Recurso) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate
					.validateString(getRecurso().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getRecurso().getFkUnidadMedida(),
					"Unidad de Medida");
			UtilValidate.validateNull(getRecurso().getFkClasificadorRecurso(),
					"Clasificador de Recurso");			
			UtilValidate.validateString(getRecurso().getDescripcion(),
					"Descripción", 250);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
