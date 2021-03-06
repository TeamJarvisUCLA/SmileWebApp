package ve.smile.administracion.portalweb.comentarios.noticias.viewmodels;


import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ComentarioCartelera;
import ve.smile.enums.EstatusComentarioCarteleraEnum;
import ve.smile.payload.response.PayloadComentarioCarteleraResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;


public class VM_ComentariosCarteleraFormBasic  extends VM_WindowForm{
	public boolean publicar;
	
	@Init(superclass = true)
	public void childInit() {
				
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if(operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			OperacionForm operacionForm = new OperacionForm(OperacionEnum.CUSTOM1.ordinal(), "Procesar", "Custom1", "fa fa-cog", "indigo");
			operacionesForm.add(operacionForm);
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		return operacionesForm;
	}
	@Override
	public boolean actionCustom1(OperacionEnum operacionEnum) {
		if(publicar == true){
			getComentarioCartelera().setEstatusComentario(EstatusComentarioCarteleraEnum.PUBLICADO.ordinal());
		} else {
			getComentarioCartelera().setEstatusComentario(EstatusComentarioCarteleraEnum.OCULTO.ordinal());
		}
		PayloadComentarioCarteleraResponse payloadComentarioCarteleraResponse = S.ComentarioCarteleraService.modificar(getComentarioCartelera());
		if (!UtilPayload.isOK(payloadComentarioCarteleraResponse)) {
			Alert.showMessage(payloadComentarioCarteleraResponse);
			return true;
		}
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	public ComentarioCartelera getComentarioCartelera() {
		return (ComentarioCartelera) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getComentarioCartelera().getFkCartelera().getTitulo(), "Nombre", 200);
			UtilValidate.validateString(getComentarioCartelera().getFkComunidad().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getComentarioCartelera().getFkComunidad().getCorreo(), "Correo", 200);
			UtilValidate.validateString(getComentarioCartelera().getComentario(), "Comentario", 300);
			//UtilValidate.validateDate(getComentarioCartelera().getFecha(), "Fecha", validateOperator, date_8601, formatToShow);
			//UtilValidate.validateInteger(getComentarioCartelera().getTipoContactoPortal(), "Tipo Contacto", validateOperator, valueToCompare);
			UtilValidate.validateNull(getComentarioCartelera().getPuntuacion(), "Puntuacion");
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	public boolean isPublicar() {
		return publicar;
	}

	public void setPublicar(boolean publicar) {
		this.publicar = publicar;
	}

	
}
