package ve.smile.web.viewmodels.faq;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Comunidad;
import ve.smile.dto.Configuracion;
import ve.smile.dto.ContactoPortal;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.PropiedadEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadComunidadResponse;
import ve.smile.payload.response.PayloadConfiguracionResponse;
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
	private Configuracion confFaq =  new Configuracion();
	boolean emailFaq = false;


	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_FAQ
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			if (!payloadConfiguracionResponse.getObjetos().isEmpty()) {
				this.confFaq.setValor(payloadConfiguracionResponse
						.getObjetos().get(0).getValor());
			}else{
				this.confFaq.setValor("false");
			}
			
		}
	}
	
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


	public boolean isEmailComentarAlbum() {
		if(confFaq.getValor().equals("true")){
			this.emailFaq = true;
		}
		return emailFaq;
	}
	
	
}
