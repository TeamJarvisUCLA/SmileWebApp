package ve.seguridad.menu.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.catalogue.simple_list.viewmodels.VM_SimpleListCatalogueDialog;
import ve.smile.seguridad.enums.TipoNodoMenuEnum;

public class VM_CatalogoTipoNodoMenu extends VM_SimpleListCatalogueDialog<TipoNodoMenuEnum> {

	@Init(superclass=true)
    public void childInit_VM_CatalogoTipoNodoMenu() {
		//NOTHING OK!
    }
	
	@Override
	public List<TipoNodoMenuEnum> getObjectListToLoad() {
		List<TipoNodoMenuEnum> lista = new ArrayList<TipoNodoMenuEnum>();
		
		boolean first = true;
		
		for (TipoNodoMenuEnum tipoNodoMenuEnum : TipoNodoMenuEnum.values()) {
			if (first) {
				first = false;
				continue;
			}
			
			lista.add(tipoNodoMenuEnum);
		}
		
		return lista;
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}
	
}
