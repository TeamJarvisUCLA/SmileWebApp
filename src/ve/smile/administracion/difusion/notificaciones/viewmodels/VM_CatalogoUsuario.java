package ve.smile.administracion.difusion.notificaciones.viewmodels;

import lights.core.payload.response.IPayloadResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import org.zkoss.bind.annotation.Init;
import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.seguridad.payload.response.PayloadUsuarioResponse;

public class VM_CatalogoUsuario extends VM_ListPaginationCatalogueDialog<Usuario>
{
	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass()
	{
		// NOTHING OK!
	}

	@Override
	public void afterChildInit()
	{
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Usuario> getObjectListToLoad(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadUsuarioResponse payloadUsuarioResponse = S.UsuarioService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadUsuarioResponse;
	}
}