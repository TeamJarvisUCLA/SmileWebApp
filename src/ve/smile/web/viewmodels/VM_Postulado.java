package ve.smile.web.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import ve.smile.dto.Padrino;
import ve.smile.dto.Persona;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_Postulado extends VM_WindowDialog{
	
	private Persona persona = new Persona();
	
	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;
	
	
	public List<SexoEnum> getSexoEnums() {
		if (this.sexoEnums == null) {
			sexoEnums = new ArrayList<SexoEnum>();
		}
		if (this.sexoEnums.isEmpty()){
			for (SexoEnum sexoEnum : SexoEnum.values()) {
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums) {
		this.sexoEnums = sexoEnums;
	}

	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPersona().setSexo(sexoEnum.ordinal());
	}
	

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
