package ve.smile.web.viewmodels.buzon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.dto.Comunidad;
import ve.smile.dto.ContactoPortal;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadClasificadorSugerenciaResponse;
import ve.smile.payload.response.PayloadComunidadResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;

public class VM_Sugerencia extends VM_WindowDialog{
	
	private Comunidad comunidad = new Comunidad();	
	private ContactoPortal contactoPortal = new ContactoPortal();
	private List<ClasificadorSugerencia> cSugerencias;
	private ClasificadorSugerencia cSugerencia;	
	
	public List<ClasificadorSugerencia> getcSugerencias() {
		
		if (this.cSugerencias == null) {
			this.cSugerencias = new ArrayList<>();
		}
		if (this.cSugerencias.isEmpty()) {
			PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService.consultaClasificadorSugerenciaSinSession();
		
			this.cSugerencias.addAll(payloadClasificadorSugerenciaResponse.getObjetos());
		}
		
		return cSugerencias;
	}

	public void setcSugerencias(List<ClasificadorSugerencia> cSugerencias) {
		this.cSugerencias = cSugerencias;
	}

	public ClasificadorSugerencia getcSugerencia() {
		return cSugerencia;
	}

	public void setcSugerencia(ClasificadorSugerencia cSugerencia) {
		this.cSugerencia = cSugerencia;
	}


	private long myFecha;	
		
	
	
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
