package ve.smile.administracion.portalweb.comentarios.album.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.enums.EstatusComentarioAlbumEnum;
import ve.smile.payload.response.PayloadComentarioAlbumResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

public class VM_ComentariosAlbumFormBasic extends VM_WindowForm{
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
		if (publicar==true){
			getComentarioAlbum().setEstatusComentario(EstatusComentarioAlbumEnum.PUBLICADO.ordinal());
		}else {
			getComentarioAlbum().setEstatusComentario(EstatusComentarioAlbumEnum.OCULTO.ordinal());
		}
		PayloadComentarioAlbumResponse PayloadComentarioAlbumResponse =S.ComentarioAlbumService.modificar(getComentarioAlbum());
		if (!UtilPayload.isOK(PayloadComentarioAlbumResponse)) {
			Alert.showMessage(PayloadComentarioAlbumResponse);
			return true;
		}
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	public ComentarioAlbum getComentarioAlbum() {
		return (ComentarioAlbum) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getComentarioAlbum().getFkComunidad().getNombre(), "Nombre", 200);
			UtilValidate.validateString(getComentarioAlbum().getFkComunidad().getCorreo(), "Correo", 200);
			UtilValidate.validateString(getComentarioAlbum().getComentario(), "Comentario", 300);
			//UtilValidate.validateDate(getComentarioAlbum().getFecha(), "Fecha", validateOperator, date_8601, formatToShow);
			//UtilValidate.validateInteger(getComentarioAlbum().getTipoContactoPortal(), "Tipo Contacto", validateOperator, valueToCompare);
			UtilValidate.validateNull(getComentarioAlbum().getPuntuacion(), "Puntuacion");
			
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
