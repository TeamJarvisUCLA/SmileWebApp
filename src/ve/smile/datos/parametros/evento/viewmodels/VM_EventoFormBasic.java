package ve.smile.datos.parametros.evento.viewmodels;

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
import ve.smile.dto.Evento;
import ve.smile.dto.ClasificadorEvento;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadClasificadorEventoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EventoFormBasic extends VM_WindowForm {

	private List<ClasificadorEvento> clasificadorEventos;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public List<ClasificadorEvento> getClasificadorEventos() {
		if (this.clasificadorEventos == null) {
			this.clasificadorEventos = new ArrayList<>();
		}
		if (this.clasificadorEventos.isEmpty()) {
			PayloadClasificadorEventoResponse payloadClasificadorEventoResponse = S.ClasificadorEventoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorEventoResponse)) {
				Alert.showMessage(payloadClasificadorEventoResponse);
			}

			this.clasificadorEventos
					.addAll(payloadClasificadorEventoResponse.getObjetos());
		}
		return clasificadorEventos;
	}

	public void setClasificadorEventos(
			List<ClasificadorEvento> clasificadorEventos) {
		this.clasificadorEventos = clasificadorEventos;
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
			PayloadEventoResponse payloadEventoResponse = S.EventoService
					.incluir(getEvento());

			if (!UtilPayload.isOK(payloadEventoResponse)) {
				Alert.showMessage(payloadEventoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadEventoResponse payloadEventoResponse = S.EventoService
					.modificar(getEvento());

			if (!UtilPayload.isOK(payloadEventoResponse)) {
				Alert.showMessage(payloadEventoResponse);
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

	public Evento getEvento() {
		return (Evento) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		//TODO: Validar tipo_evento es de tipo Char
		try {
			UtilValidate.validateString(getEvento().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getEvento().getFkClasificadorEvento(),
					"Clasificador de Evento");
			
			
			UtilValidate.validateString(getEvento().getDescripcion(),
					"Descripción", 250);			
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
