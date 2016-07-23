package ve.seguridad.permisos.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import lights.seguridad.dto.Rol;
import lights.seguridad.enums.OperacionEnum;

public class VM_PermisosFormBasic extends VM_WindowForm {
	
	@Init(superclass = true)
	public void childInit_VM_PermisosFormBasic() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}

		return operacionesForm;

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
}
