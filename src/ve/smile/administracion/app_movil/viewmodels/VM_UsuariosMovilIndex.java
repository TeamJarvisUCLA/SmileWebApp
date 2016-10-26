package ve.smile.administracion.app_movil.viewmodels;

import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_UsuariosMovilIndex extends
		VM_WindowSimpleListPrincipal<Persona> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Persona> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
				.consultarPersonaConUsuarioPaginacion(cantidadRegistrosPagina,
						pagina, "");

		return payloadPersonaResponse;
	}

	@Override
	public void executeEliminar() {
		// NOTHING OK!
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/appMovil/usuarios/usuariosMovilFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsCustom2() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un usuario";
		}
		return "";
	}

	@Override
	public String isValidPreconditionsCustom1() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un usuario";
		}
		return "";
	}

}
