package ve.smile.administracion.difusion.notificaciones.viewmodels;

import java.util.Date;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

public class VM_NotificacionDifusionIndex extends
		VM_WindowSimpleListPrincipal<Usuario> {

	private NotificacionUsuario notificacionUsuario = new NotificacionUsuario();
	private List<Usuario> usuarios;

	private Date fecha = new Date();
	private String contenido = new String();

	@Init(superclass = true)
	public void childInit() {
	}

	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<Usuario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadUsuarioResponse;
	}

	// VALIDACION
	@Command("aceptar")
	public void aceptar() {
		try {
			UtilValidate.validateNull(this.getUsuarios(), "Usuarios");

			UtilValidate.validateString(this.getContenido(), "Contenido");

			this.getNotificacionUsuario().setFecha(this.getFecha().getTime());
			this.getNotificacionUsuario().setContenido(this.getContenido());
			this.getNotificacionUsuario().setTipoReferenciaNotificacionEnum(
					TipoReferenciaNotificacionEnum.DIFUSION);
			this.getNotificacionUsuario().setEstatusNotificacionEnum(
					EstatusNotificacionEnum.PENDIENTE);

			for (int k = 0; k < this.getUsuarios().size(); k++) {
				this.getNotificacionUsuario().setFkUsuario(
						this.getUsuarios().get(k));
				PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
						.incluir(this.getNotificacionUsuario());
				if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
					Alert.showMessage(payloadNotificacionUsuarioResponse);

				}

			}
			notificacionUsuario = new NotificacionUsuario();
			usuarios = null;

			fecha = new Date();
			contenido = new String();
			BindUtils.postNotifyChange(null, null, this, "*");

			UtilDialog.showMessageBoxSuccess("Difusión Generada Exitosamente");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			UtilDialog.showMessageBoxSuccess(e.getMessage());
		}

	}

	// USUARIOS
	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	@Command("cancelar")
	public void cancelar() {
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	// BUSCAR USUARIO
	@Command("buscarUsuarios")
	public void buscarUsuarios() {
		CatalogueDialogData<Usuario> catalogueDialogData = new CatalogueDialogData<Usuario>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Usuario>() {
					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Usuario> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						usuarios = catalogueDialogCloseEvent.getEntities();
						refreshUsuarios();
					}
				});
		UtilDialog
				.showDialog(
						"views/desktop/administracion/difusion/notificaciones/catalogoUsuario.zul",
						catalogueDialogData);
	}

	public void refreshUsuarios() {
		BindUtils.postNotifyChange(null, null, this, "usuarios");
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public NotificacionUsuario getNotificacionUsuario() {
		return notificacionUsuario;
	}

	public void setNotificacionUsuario(NotificacionUsuario notificacionUsuario) {
		this.notificacionUsuario = notificacionUsuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
