package ve.smile.administracion.perfil.viewmodels;

import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

public class VM_PerfilIndex extends VM_WindowSimpleListPrincipal<Usuario> {

	@Init(superclass = true)
	public void childInit() {
		setSelectedObject(DataCenter.getUserSecurityData().getUsuario());
	}

	@Override
	public IPayloadResponse<Usuario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadUsuarioResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/perfil/PerfilFormBasic.zul";
	}

}
