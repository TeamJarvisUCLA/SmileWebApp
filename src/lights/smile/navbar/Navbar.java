package lights.smile.navbar;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;

import karen.core.crux.session.DataCenter;
import ve.smile.seguridad.dto.Usuario;

public class Navbar {

	private List<String> listBreadsCrumbs = new ArrayList<String>();

	private Usuario usuario;
	
	private int size;

	public Navbar() {
		super();
		usuario = DataCenter.getUserSecurityData().getUsuario();
		size= 4;
		System.out.println(size);
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public List<String> getListBreadsCrumbs() {
		return listBreadsCrumbs;
	}

	public void setListBreadsCrumbs(List<String> listBreadsCrumbs) {
		this.listBreadsCrumbs = listBreadsCrumbs;
	}

	public boolean getEmptyList() {
		return listBreadsCrumbs.isEmpty();
	}

	public boolean getNotEmptyList() {
		return !listBreadsCrumbs.isEmpty();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
