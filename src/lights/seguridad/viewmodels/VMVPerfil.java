package lights.seguridad.viewmodels;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDateTime;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.enums.SexoEnum;
import lights.seguridad.payload.response.PayloadUsuarioResponse;
import lights.seguridad.consume.services.UsuarioRolService;
import lights.seguridad.dto.Perfil;
import lights.smile.consume.services.S;

public class VMVPerfil extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (DataCenter.getPerfil().getIdPerfil() == null) {
			
			OperacionForm operacionForm = OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR);
			operacionForm.setSclass("teal lighten-1");
			
			operacionesForm.add(operacionForm);

			return operacionesForm;
		}
		
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}
		
		if (DataCenter.getPerfil().getIdPerfil() == null) {
			
			PayloadUsuarioResponse payload = 
					S.UsuarioService.setNewPerfil(DataCenter.getUserSecurityData().getUsuario());
			
			if (!UtilPayload.isOK(payload)) {
				Alert.showMessage(payload);
				
				return true;
			}
			
			Integer idPerfil = 
					((Double) payload.getInformacion("idPerfil")).intValue();
			
			DataCenter.getPerfil().setIdPerfil(idPerfil);
			
			Executions.sendRedirect("index.zul");
		}
		
//		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
//			PayloadPerfilResponse payloadPerfilResponse =
//					S.PerfilService.incluir(getPerfil());
//
//			if(!UtilPayload.isOK(payloadPerfilResponse)) {
//				Alert.showMessage(payloadPerfilResponse);
//				return true;
//			}
//
//			DataCenter.reloadCurrentNodoMenu();
//
//			return true;
//		}
//
//		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
//			PayloadPerfilResponse payloadPerfilResponse =
//					S.PerfilService.modificar(getPerfil());
//
//			if(!UtilPayload.isOK(payloadPerfilResponse)) {
//				Alert.showMessage(payloadPerfilResponse);
//				return true;
//			}
//
//			DataCenter.reloadCurrentNodoMenu();
//
//			return true;
//		}

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

	public Perfil getPerfil() {
		return DataCenter.getPerfil();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getPerfil().getIdentificacion(), "Identificación", 34);
			UtilValidate.validateString(getPerfil().getNombre(), "Nombre", 149);
			UtilValidate.validateInteger(getPerfil().getEdad(), "Edad", ValidateOperator.GREATER_THAN, 0);
			UtilValidate.validateInteger(getPerfil().getEdad(), "Edad", ValidateOperator.LESS_THAN, 100);
			UtilValidate.validateNullOrEmpty(getPerfil().getSexo(), "Sexo");
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -18);
			
			UtilValidate.validateDate(getPerfil().getFechaNacimiento(), "Fecha de Nacimiento", ValidateOperator.LESS_THAN, 
					new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()), "dd/MM/yyyy");
			UtilValidate.validateString(getPerfil().getTelefono1(), "Teléfono 1", 24);
			UtilValidate.validateString(getPerfil().getTelefono2(), "Teléfono 2", 24);
			UtilValidate.validateString(getPerfil().getTelefono3(), "Teléfono 3", 24);
			UtilValidate.validateString(getPerfil().getDireccion(), "Dirección", 249);
			UtilValidate.validateString(getPerfil().getPais(), "País", 44);
			UtilValidate.validateString(getPerfil().getTwitter(), "Twitter", 99);
			UtilValidate.validateString(getPerfil().getInstagram(), "Instagram", 99);
			UtilValidate.validateString(getPerfil().getLinkedin(), "Linkedin", 99);
			
			
			getPerfil().setNombre(getPerfil().getNombre().toUpperCase());
			getPerfil().setDireccion(getPerfil().getDireccion().toUpperCase());
			getPerfil().setPais(getPerfil().getPais().toUpperCase());
			getPerfil().setIdentificacion(getPerfil().getIdentificacion().toUpperCase());
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

	public void setFechaNacimiento(Timestamp fechaNacimiento) {		
		getPerfil().setFechaNacimiento(UtilDateTime.getLongOfDate(fechaNacimiento));
	}
	
	public Timestamp getFechaNacimiento() {
		return new Timestamp(getPerfil().getFechaNacimiento());
	}
	
	public void setEnumSexo(SexoEnum sexoEnum) {
		if (sexoEnum == null) {
			getPerfil().setSexo(null);
			return;
		}
		getPerfil().setSexo(sexoEnum.ordinal());
	}
	
	public SexoEnum getEnumSexo() {
		if (getPerfil().getSexo() == null) {
			return null;
		}
		return SexoEnum.values()[getPerfil().getSexo()];
	}
	
	public List<SexoEnum> getOptionsSexo() {
		return Arrays.asList(SexoEnum.values());
	}
}
