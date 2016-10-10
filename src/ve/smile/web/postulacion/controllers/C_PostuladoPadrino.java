package ve.smile.web.postulacion.controllers;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;
import karen.core.util.UtilDialog;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Padrino;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.web.postulacion.viewmodels.VM_Postulado;

public class C_PostuladoPadrino extends C_WindowDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public void onAccept(Event event) {
		if (!isFormValidated()) {
			return;
		}
		DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event,
				DialogActionEnum.ACEPTAR);
		VM_Postulado postuladoPadrino = (VM_Postulado) vm();
		Persona persona = postuladoPadrino.getPersona();
		Padrino padrino = postuladoPadrino.getPadrino();
		FrecuenciaAporte faporte = postuladoPadrino.getFaporte();
		padrino.setFkFrecuenciaAporte(faporte);
		padrino.setFkPersona(persona);
		persona.setFechaNacimiento(postuladoPadrino.getMyFecha().getTime());
		persona.setFacebook("face.com");
		persona.setEstatus('P');
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.incluirPostuladoPadrino(padrino);
		close(dialogCloseEvent);
		UtilDialog
				.showMessageBoxSuccess("Gracias por preferirnos. Su información será procesada y nos contactaremos con usted.");
	}

	public Persona getPersona() {
		return (Persona) DataCenter.getEntity();
	}

	public Padrino getPadrino() {
		return (Padrino) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		// TODO
		try {
			UtilValidate
					.validateString(getPersona().getNombre(), "Nombre", 100);

			UtilValidate.validateNull(getPersona().getNombre(), "Nombre");
			// UtilValidate.validateNull(getPersona().getIdentificacion(),
			// "Identificacion");
			UtilValidate.validateNull(getPersona().getCorreo(), "Correo");
			UtilValidate.validateNull(getPersona().getDireccion(), "Direccion");
			UtilValidate.validateNull(getPadrino().getMonto(), "Monto");
			return true;
		} catch (Exception e) {
			UtilDialog.showMessageBoxSuccess(e.getMessage());
			return false;
		}
	}

	@Override
	public void onCancel(Event event) {
		close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));
	}

	@Override
	public void doOnCreate() {
		// TODO Auto-generated method stub
	}

}
