package ve.mensajes.mensajes_sistema.viewmodels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.crux.session.listener_zk.MySessionListener;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadMensajeSistemaResponse;
import ve.smile.seguridad.dto.MensajeSistema;

public class VM_MensajesSistemaFormBasic extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.CUSTOM5)) {

			OperacionForm operacionForm = OperacionFormHelper.getPorType(OperacionFormEnum.CUSTOM1);
			operacionForm.setIconSclass("fa fa-bullhorn");
			operacionForm.setLabel("Publicar");
			operacionForm.setSclass("blue darken-1");
			
			operacionesForm.add(operacionForm);
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionCustom1(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		MensajeSistema mensajeSistema = getMensajeSistema();
		mensajeSistema.setFecha(Calendar.getInstance().getTimeInMillis());
		mensajeSistema.setFkUsuario(DataCenter.getUserSecurityData().getUsuario());
		
		if (operacionEnum.equals(OperacionEnum.CUSTOM5)) {
			PayloadMensajeSistemaResponse payloadMensajeSistemaResponse =
					S.MensajeSistemaService.incluir(mensajeSistema);

			if(!UtilPayload.isOK(payloadMensajeSistemaResponse)) {
				Alert.showMessage(payloadMensajeSistemaResponse);
				return true;
			}
			
			DataCenter.setMessageSystem("<em><b>Mensaje del sistema:</b></em> " + mensajeSistema.getContenido());
			MySessionListener.execute();
			
			payloadMensajeSistemaResponse.setInformacion(IPayloadResponse.MENSAJE, "S:Success Code: 996-Mensaje publicado exitosamente.");
			Alert.showMessage(payloadMensajeSistemaResponse);

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

	public MensajeSistema getMensajeSistema() {
		return (MensajeSistema) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getMensajeSistema().getContenido(), "Contenido", 60);
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

}
