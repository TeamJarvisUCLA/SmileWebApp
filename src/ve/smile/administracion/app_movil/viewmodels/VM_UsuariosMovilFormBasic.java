package ve.smile.administracion.app_movil.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import lights.core.encryptor.UtilEncryptor;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtillMail;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.dto.Persona;
import ve.smile.enums.PropiedadEnum;
import ve.smile.enums.RolMovilEnum;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_UsuariosMovilFormBasic extends VM_WindowForm {

	private List<RolMovilEnum> rolMovilEnums;
	private RolMovilEnum rolMovilEnum;
	private Configuracion confPostulacionPadrino;

	@Init(superclass = true)
	public void childInit() {
		if (this.getPersona() != null) {
			PayloadConfiguracionResponse payloadConfiguracionResponseP = S.ConfiguracionService
					.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_USUARIOS_REGISTRO
							.ordinal());
			if (UtilPayload.isOK(payloadConfiguracionResponseP)) {
				if (!payloadConfiguracionResponseP.getObjetos().isEmpty()) {
					this.confPostulacionPadrino
							.setValor(payloadConfiguracionResponseP
									.getObjetos().get(0).getValor());
				} else {
					this.confPostulacionPadrino.setValor("false");
				}

			}
			Map<String, String> criterios2 = new HashMap<>();
			criterios2.put("fkPersona.idPersona",
					String.valueOf(this.getPersona().getIdPersona()));
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.consultarCriterios(TypeQuery.EQUAL, criterios2);
			if (UtilPayload.isOK(payloadColaboradorResponse)
					&& payloadColaboradorResponse.getObjetos() != null
					&& payloadColaboradorResponse.getObjetos().size() > 0) {
				getRolMovilEnums().add(RolMovilEnum.COLABORADOR);
			}
			if (this.getPersona().getRolMovil() != null) {
				this.setRolMovilEnum(RolMovilEnum.values()[this.getPersona()
						.getRolMovil()]);
			}

			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.consultarCriterios(TypeQuery.EQUAL, criterios2);
			if (UtilPayload.isOK(payloadPadrinoResponse)
					&& payloadPadrinoResponse.getObjetos() != null
					&& payloadPadrinoResponse.getObjetos().size() > 0) {
				this.getRolMovilEnums().add(RolMovilEnum.PADRINO);
			}
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.consultarCriterios(TypeQuery.EQUAL, criterios2);
			if (UtilPayload.isOK(payloadVoluntarioResponse)
					&& payloadVoluntarioResponse.getObjetos() != null
					&& payloadVoluntarioResponse.getObjetos().size() > 0) {
				getRolMovilEnums().add(RolMovilEnum.VOLUNTARIO);
			}
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultarCriterios(TypeQuery.EQUAL, criterios2);
			if (UtilPayload.isOK(payloadTrabajadorResponse)
					&& payloadTrabajadorResponse.getObjetos() != null
					&& payloadTrabajadorResponse.getObjetos().size() > 0) {
				getRolMovilEnums().add(RolMovilEnum.COLABORADOR);
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

		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.getPersona().setRolMovil(rolMovilEnum.ordinal());
			PayloadPersonaResponse payloadResponse = S.PersonaService
					.modificar(getPersona());
			if (!UtilPayload.isOK(payloadResponse)) {
				Alert.showMessage(payloadResponse);
				return true;
			}

			if (isEmailRegistro()) {

				try {
					new UtillMail()
							.generateAndSendEmail(
									this.getPersona().getCorreo(),
									"FANCA - Usuario Smile",
									"<p><span style=\"color:#3399cc;\"><strong>&iexcl;Felicidades!</strong></span></p><p><span style=\"color:#3399cc;\">Eres parte del nuestra app m&oacute;vil, tus credenciales de ingreso son:</span></p><p><span style=\"color:#ff6699;\"><strong>Usuario</strong>: </span><span style=\"color:#3399ff;\">"
											+ getPersona().getFkUsuario()
													.getCorreo()
											+ "</span></p><p><span style=\"color: rgb(255, 102, 153);\"><b>Clave</b>:&nbsp;</span><span style=\"color:#3399ff;\"> "
											+ UtilEncryptor.desencriptar(this
													.getPersona()
													.getFkUsuario().getClave())
											+ " </span></p><p><span style=\"color: rgb(255, 102, 153);\"><b>Rol</b>:&nbsp;</span><span style=\"color:#3399ff;\"> "
											+ this.getRolMovilEnum()
											+ " </span></p><p><span style=\"color:#3399ff;\">Ingresa a nuestro portal por la Url:&nbsp;<a href=\"http://localhost:8080/SmileWebApp/main.zul\" target=\"_blank\">http://localhost:8080/SmileWebApp/main.zul</a>&nbsp;e inicia sesi&oacute;n</span></p><p>&nbsp;</p><p><span style=\"color:#ff6699;\"><strong>FANCA - Fundaci&oacute;n del Amigo Ni&ntilde;os con C&aacute;ncer</strong></span></p>");
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
			}
			Alert.showMessage(payloadResponse);
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

	public Persona getPersona() {
		return (Persona) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		return true;
	}

	public List<RolMovilEnum> getRolMovilEnums() {
		if (this.rolMovilEnums == null) {
			rolMovilEnums = new ArrayList<>();
		}
		return rolMovilEnums;
	}

	public void setRolMovilEnums(List<RolMovilEnum> rolMovilEnums) {
		this.rolMovilEnums = rolMovilEnums;
	}

	public RolMovilEnum getRolMovilEnum() {
		return rolMovilEnum;
	}

	public void setRolMovilEnum(RolMovilEnum rolMovilEnum) {
		this.rolMovilEnum = rolMovilEnum;
	}

	public boolean isEmailRegistro() {
		if (this.confPostulacionPadrino.getValor().equals("true")) {
			return true;
		}
		return false;
	}
}