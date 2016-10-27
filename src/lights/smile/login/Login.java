package lights.smile.login;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import karen.core.crux.session.DataCenter;
import karen.core.crux.session.UserSecurityData;
import karen.core.crux.session.context.SesionContextHelper;
import karen.core.crux.session.listener_zk.MySessionListener;
import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.encryptor.UtilEncryptor;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

import com.google.gson.internal.LinkedTreeMap;

public class Login {

	private Usuario usuario;

	public Login() {
		super();
		usuario = new Usuario();
	}

	@SuppressWarnings("unchecked")
	@Command("login")
	public void login() {
		if (!isFormValidated()) {
			return;
		}

		usuario.setClave(UtilEncryptor.encriptar(usuario.getClave()));
		PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService.login(
				usuario, Executions.getCurrent().getRemoteAddr());

		if (!UtilPayload.isOK(payloadUsuarioResponse)) {
			UtilDialog.showMessage(payloadUsuarioResponse);

			return;
		}

		DataCenter.clear();

		usuario = Usuario
				.constructFromLinkedTreeMap((LinkedTreeMap<String, Object>) payloadUsuarioResponse
						.getInformacion("usuario"));

		Persona persona = new Persona();

		Integer idSesion = ((Double) payloadUsuarioResponse
				.getInformacion(IPayloadResponse.ID_SESION)).intValue();

		String accessToken = (String) payloadUsuarioResponse
				.getInformacion(IPayloadResponse.ACCESS_TOKEN);

		HttpSession httpSession = (HttpSession) Sessions.getCurrent()
				.getNativeSession();
		MySessionListener.putIdSesion(httpSession.getId(), idSesion);

		SesionContextHelper.setLogued(true);

		// DataCenter.putVentanaDefault(httpSession.getId(),
		// "/views/desktop/prueba.zul");

		DataCenter
				.setUserSecurityData(new UserSecurityData(usuario, String
						.valueOf(usuario.getFkRol().getIdRol()), idSesion,
						accessToken));
		HashMap<String, String> criterio = new HashMap<>();
		criterio.put("fkUsuario.idUsuario",
				String.valueOf(this.usuario.getIdUsuario()));
		PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
				.consultarCriterios(TypeQuery.EQUAL, criterio);
		if (UtilPayload.isOK(payloadPersonaResponse)
				&& payloadPersonaResponse.getObjetos() != null
				&& payloadPersonaResponse.getObjetos().size() > 0) {
			persona = payloadPersonaResponse.getObjetos().get(0);
		}

		usuario.setPersona(persona);
//		DataCenter.getUserSecurityData().getUsuario()
//				.setNotificacionUsuarios(new ArrayList<NotificacionUsuario>());
		
		DataCenter.getUserSecurityData().getUsuario().setPersona(persona);
		// DataCenter.setSizeNotificacions(8);
		criterio.put("estatusNotificacion",
				String.valueOf(EstatusNotificacionEnum.PENDIENTE.ordinal()));
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
				.consultarCriterios(TypeQuery.EQUAL, criterio);
		
		if (UtilPayload.isOK(payloadPersonaResponse)
				&& payloadPersonaResponse.getObjetos() != null) {
			DataCenter
					.getUserSecurityData()
					.getUsuario()
					.setNotificacionUsuariosPendientes(
							payloadNotificacionUsuarioResponse.getObjetos());
		}

		Executions.sendRedirect("index.zul");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean getLogued() {
		return DataCenter.getUserSecurityData() != null;
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(usuario.getCorreo(), "Correo", 59);
			UtilValidate.validateString(usuario.getClave(), "Clave", 100);

			usuario.setCorreo((usuario.getCorreo().toUpperCase()));

			return true;
		} catch (Exception e) {
			UtilDialog.showMessage(e.getMessage());

			return false;
		}
	}
	
	@Command
	public void recuperacion(){
		DialogData dialogData = new DialogData();
		UtilDialog.showDialog("/views/web/recuperacion.zul", dialogData);
	}
}
