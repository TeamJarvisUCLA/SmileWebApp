package lights.seguridad.viewmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.core.payload.response.IPayloadResponse;
import lights.seguridad.enums.OperacionEnum;
import lights.seguridad.enums.helper.OperacionHelper;
import lights.seguridad.payload.response.PayloadRolResponse;
import lights.seguridad.payload.response.PayloadUsuarioResponse;
import lights.seguridad.dto.Operacion;
import lights.seguridad.dto.Rol;
import lights.seguridad.dto.Usuario;
import lights.smile.consume.services.S;

public class VMVRol extends VM_WindowForm {
	
	private Set<Usuario> usuariosDeRolSeleccionado;
	
	@Init(superclass = true)
	public void childInit() {
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			List<Usuario> usuarios = new ArrayList<Usuario>(getRol().getUsuarios());
			
			String idUsuarios = "";
			
			for (Usuario usuario : usuarios) {
				if (idUsuarios.length() > 0) {
					idUsuarios += ",";
				}
				idUsuarios += usuario.getIdUsuario();
			}
			
			getRol().getUsuarios().clear();
			
			PayloadRolResponse payloadRolResponse =
							S.RolService.incluirConUsuarios(getRol(), idUsuarios);

			if(!UtilPayload.isOK(payloadRolResponse)) {
				Alert.showMessage(payloadRolResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			List<Usuario> usuarios = new ArrayList<Usuario>(getRol().getUsuarios());
			
			String idUsuarios = "";
			
			for (Usuario usuario : usuarios) {
				if (idUsuarios.length() > 0) {
					idUsuarios += ",";
				}
				idUsuarios += usuario.getIdUsuario();
			}
			
			getRol().getUsuarios().clear();
			
			PayloadRolResponse payloadRolResponse =
							S.RolService.modificarConUsuarios(getRol(), idUsuarios);

			if(!UtilPayload.isOK(payloadRolResponse)) {
				Alert.showMessage(payloadRolResponse);
				return true;
			}

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

	public Rol getRol() {
		return (Rol) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getRol().getNombre(), "Nombre", 99);
			
			//toUpper
			getRol().setNombre(getRol().getNombre().toUpperCase());
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

	//Lista Derecha (Roles Disponibles)
	@Override
	public IPayloadResponse<?> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
		
		PayloadUsuarioResponse payloadUsuarioResponse =
				S.UsuarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		
		if (!UtilPayload.isOK(payloadUsuarioResponse)) {
			Alert.showMessage(payloadUsuarioResponse);
		}
		
		payloadUsuarioResponse.getObjetos().removeAll(getRol().getUsuarios());
		
		return payloadUsuarioResponse;
	}

	@Override
	public List<Operacion> getOperationsListBox(OperacionEnum operacionEnum) {
		List<Operacion> operaciones = new ArrayList<Operacion>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);
			operacion.setIconSclass("fa fa-arrow-left");
			operacion.setTooltiptext("Delegar rol a este usuario");
			operacion.setSclass("light-green darken-3");
			
			operaciones.add(operacion);

			return operaciones;
		}

		return operaciones;
	}
	
	@Command("removerUsuarioDeRol")
	public void removerUsuarioDeRol() {
		if (usuariosDeRolSeleccionado == null) {
			return;
		}
		
		for (Usuario usuario : usuariosDeRolSeleccionado) {
			objectsList.add(usuario);
			getRol().getUsuarios().remove(usuario);
		}

		usuariosDeRolSeleccionado = null;
		
		BindUtils.postGlobalCommand(null, null, "actualizarListas", null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void executeCustom1() {
		if (selectedObject == null) {
			return;
		}
		Set<Usuario> usuarios = (Set<Usuario>) selectedObject;
		
		for (Usuario usuario : usuarios) {
			objectsList.remove(usuario);
			getRol().getUsuarios().add(usuario);
		}
		
		
		selectedObject = null;
		
		BindUtils.postGlobalCommand(null, null, "actualizarListas", null);
	}
	
	@GlobalCommand
	@NotifyChange({"selectedObject", "rol", "objectsList"})
	public void actualizarListas() {
		//NOTHING OK!
	}
	
	public Set<Usuario> getUsuariosDeRolSeleccionado() {
		return usuariosDeRolSeleccionado;
	}

	public void setUsuariosDeRolSeleccionado(Set<Usuario> usuariosDeRolSeleccionado) {
		this.usuariosDeRolSeleccionado = usuariosDeRolSeleccionado;
	}

	@Command("onDropListUsusariosDisponibles")
	public void onDropListUsusariosDisponibles(@BindingParam("id") String pId) {
		Integer id = Integer.parseInt(pId);
		
		for (Usuario usuario : getRol().getUsuarios()) {
			if (usuario.getIdUsuario().equals(id)) {
				
				objectsList.add(usuario);
				getRol().getUsuarios().remove(usuario);
				
				BindUtils.postGlobalCommand(null, null, "actualizarListas", null);
				
				break;
			}
		}
	}
	
	@Command("onDropListUsusariosSeleccionados")
	public void onDropListUsusariosSeleccionados(@BindingParam("id") String pId) {
		Integer id = Integer.parseInt(pId);
		
		for (Object object : objectsList) {
			Usuario usuario = (Usuario) object;
			
			if (usuario.getIdUsuario().equals(id)) {
				
				objectsList.remove(usuario);
				getRol().getUsuarios().add(usuario);
				
				BindUtils.postGlobalCommand(null, null, "actualizarListas", null);
				
				break;
			}
		}
	}
}
