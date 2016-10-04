package ve.smile.web.viewmodels.faq;

import java.util.Date;
import java.util.List;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import ve.smile.consume.services.S;
import ve.smile.dto.Comunidad;
import ve.smile.dto.ContactoPortal;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadComunidadResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;

public class VM_Pregunta extends VM_WindowDialog{
	
	private Comunidad comunidad = new Comunidad();	
	private ContactoPortal contactoPortal = new ContactoPortal();
	//private List<TipoContactoPortalEnum> tContactoPortalEnums;
	//private TipoContactoPortalEnum tContactoPortalEnum;
	//private List<EstatusContactoEnum> eContactoPortalEnums;
	//rivate EstatusContactoEnum eContactoEnum; 
	private long myFecha;	
	//private ProcedenciaEnum procedenciaEnum;
	//private List<ProcedenciaEnum> procedenciaEnums;	
	
	
	public Comunidad getComunidad() {
		return comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		if (comunidad == null) {
			comunidad = new Comunidad();
		}
		this.comunidad = comunidad;
	}

	public long getMyFecha() {
		myFecha= new Date().getTime();
		return myFecha;
	}

	public void setMyFecha(long myFecha) {
		this.myFecha = myFecha;
	}

	public ContactoPortal getContactoPortal() {
		return contactoPortal;
	}

	public void setContactoPortal(ContactoPortal contactoPortal) {
		if (contactoPortal == null) {
			contactoPortal = new ContactoPortal();
		}
		this.contactoPortal = contactoPortal;

	}

	
	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub
		
	}


	
	
}
