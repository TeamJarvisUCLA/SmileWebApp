package ve.smile.web.viewmodels.recuperacion;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;

import org.zkoss.bind.annotation.Init;

import ve.smile.seguridad.dto.Usuario;


public class VM_recuperacion extends VM_WindowDialog {
	
	private Usuario usuario = new Usuario();

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!

	}
	
	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}
		
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
