package lights.smile.login;

import javax.servlet.http.HttpSession;

import karen.core.crux.session.DataCenter;
import karen.core.crux.session.UserSecurityData;
import karen.core.crux.session.context.SesionContextHelper;
import karen.core.crux.session.listener_zk.MySessionListener;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.encryptor.UtilEncryptor;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

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
		//DataCenter.updateSrcPageContent(null, null, "/views/desktop/prueba.zul");

		usuario = Usuario
				.constructFromLinkedTreeMap((LinkedTreeMap<String, Object>) payloadUsuarioResponse
						.getInformacion("usuario"));

		Integer idSesion = ((Double) payloadUsuarioResponse
				.getInformacion(IPayloadResponse.ID_SESION)).intValue();

		String accessToken = (String) payloadUsuarioResponse
				.getInformacion(IPayloadResponse.ACCESS_TOKEN);
		
		DataCenter
				.setUserSecurityData(new UserSecurityData(usuario, String
						.valueOf(usuario.getFkRol().getIdRol()), idSesion,
						accessToken));

		HttpSession httpSession = (HttpSession) Sessions.getCurrent()
				.getNativeSession();
		MySessionListener.putIdSesion(httpSession.getId(), idSesion);

		SesionContextHelper.setLogued(true);
		
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
}
