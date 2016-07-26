package ve.smile.web.viewmodels;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import ve.smile.dto.Persona;

public class VM_Postulado extends VM_WindowDialog{
	
	private Persona persona = new Persona();

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}
	
	

}
