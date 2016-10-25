package ve.seguridad.usuarios.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.encryptor.UtilEncryptor;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtillMail;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.dto.Rol;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

public class VM_UsuariosFormBasic extends VM_WindowForm {

	private Persona persona;

	@Init(superclass = true)
	public void childInit() {
		if (this.getUsuario() != null
				&& this.getUsuario().getIdUsuario() != null) {
			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("fkUsuario.idUsuario",
					String.valueOf(this.getUsuario().getIdUsuario()));
			PayloadPersonaResponse payloadResponse = S.PersonaService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadResponse)
					&& payloadResponse.getObjetos() != null
					&& payloadResponse.getObjetos().size() > 0) {
				this.setPersona(payloadResponse.getObjetos().get(0));
			}
		}
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

			getUsuario().setClave(UtilEncryptor.encriptar("123456"));

			PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
					.incluir(getUsuario());

			if (!UtilPayload.isOK(payloadUsuarioResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
				return true;
			}

			this.getUsuario().setIdUsuario(
					((Double) payloadUsuarioResponse.getInformacion("id"))
							.intValue());
			getPersona().setFkUsuario(getUsuario());
			PayloadPersonaResponse payloadResponse = S.PersonaService
					.modificar(getPersona());
			if (!UtilPayload.isOK(payloadResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
				return true;
			}

			Alert.showMessage(payloadUsuarioResponse);
			DataCenter.reloadCurrentNodoMenu();

			try {
				new UtillMail()
						.generateAndSendEmail(
								this.persona.getCorreo(),
								"FANCA - Usuario Smile",
								"<p><span style=\"color:#3399cc;\"><strong>&iexcl;Felicidades!</strong></span></p><p><span style=\"color:#3399cc;\">Eres parte del nuestra organizaci&oacute;n, tus credenciales de ingreso son:</span></p><p><span style=\"color:#ff6699;\"><strong>Usuario</strong>: </span><span style=\"color:#3399ff;\">"
										+ this.getUsuario().getCorreo()
										+ "</span></p><p><span style=\"color: rgb(255, 102, 153);\"><b>Clave</b>:&nbsp;</span><span style=\"color:#3399ff;\"> 123456 </span></p><p><span style=\"color:#3399ff;\">Ingresa a nuestro portal por la Url:&nbsp;<a href=\"http://localhost:8080/SmileWebApp/main.zul\" target=\"_blank\">http://localhost:8080/SmileWebApp/main.zul</a>&nbsp;e inicia sesi&oacute;n</span></p><p>&nbsp;</p><p><span style=\"color:#ff6699;\"><strong>FANCA - Fundaci&oacute;n del Amigo Ni&ntilde;os con C&aacute;ncer</strong></span></p>");
			} catch (AddressException e) {
				e.printStackTrace();
				Alert.showErrorMessage("100",
						"Problemas al enviar el correo al usuario");
				return true;
			} catch (MessagingException e) {
				e.printStackTrace();
				Alert.showErrorMessage("100",
						"Problemas al enviar el correo al usuario");
				return true;
			}
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
					.modificar(getUsuario());

			if (!UtilPayload.isOK(payloadUsuarioResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
				return true;
			}

			getPersona().setFkUsuario(getUsuario());
			PayloadPersonaResponse payloadResponse = S.PersonaService
					.modificar(getPersona());
			if (!UtilPayload.isOK(payloadResponse)) {
				Alert.showMessage(payloadUsuarioResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	@Command("buscarRol")
	public void buscarRol() {
		CatalogueDialogData<Rol> catalogueDialogData = new CatalogueDialogData<Rol>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Rol>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Rol> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						getUsuario().setFkRol(
								catalogueDialogCloseEvent.getEntity());

						refreshUsuario();
					}
				});

		UtilDialog.showDialog("views/seguridad/usuarios/catalogoRol.zul",
				catalogueDialogData);
	}

	@Command("quitarRol")
	public void quitarRol() {
		getUsuario().setFkRol(null);

		refreshUsuario();
	}

	public void refreshUsuario() {
		BindUtils.postNotifyChange(null, null, this, "usuario");
	}

	@Command("buscarPersona")
	public void buscarPersona() {
		CatalogueDialogData<Persona> catalogueDialogData = new CatalogueDialogData<Persona>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Persona>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Persona> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						setPersona(catalogueDialogCloseEvent.getEntity());

						refreshPersona();
						refreshUsuario();
					}
				});

		UtilDialog.showDialog("views/seguridad/usuarios/catalogoPersonas.zul",
				catalogueDialogData);
	}

	@Command("quitarPersona")
	public void quitarPersona() {
		this.setPersona(new Persona());
		this.getUsuario().setCorreo("");
		refreshPersona();
	}

	public void refreshPersona() {
		this.getUsuario().setCorreo(this.getPersona().getCorreo());
		BindUtils.postNotifyChange(null, null, this, "persona");
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

	public Usuario getUsuario() {
		return (Usuario) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getUsuario().getCorreo(), "Correo", 59);
			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("correo",
					String.valueOf(this.getUsuario().getCorreo()));
			PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
					.consultarCriterios(TypeQuery.IEQUAL, criterios);
			if (UtilPayload.isOK(payloadUsuarioResponse)
					&& payloadUsuarioResponse.getObjetos() != null
					&& payloadUsuarioResponse.getObjetos().size() > 0) {
				Alert.showErrorMessage("100",
						"El <b>nombre</b> de usuario ingresado ya se encuentra designado");
				return false;
			}

			UtilValidate.validateNull(getPersona(), "Persona");
			UtilValidate.validateNull(getPersona().getIdPersona(), "Persona");

			UtilValidate.validateNull(getUsuario().getFkRol(), "Rol");
			UtilValidate
					.validateNull(getUsuario().getFkRol().getIdRol(), "Rol");	
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

	public boolean getEstatus() {
		return getUsuario().getEstatus().equals(Usuario.ACTIVO);
	}

	public void setEstatus(boolean estatus) {
		getUsuario().setEstatus((estatus) ? Usuario.ACTIVO : Usuario.INACTIVO);
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public void onIncluir() {
		getUsuario().setEstatus(Usuario.ACTIVO);
	}
}