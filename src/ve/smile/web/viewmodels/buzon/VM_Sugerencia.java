package ve.smile.web.viewmodels.buzon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.smile.util.UtillMail;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.dto.Comunidad;
import ve.smile.dto.Configuracion;
import ve.smile.dto.ContactoPortal;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.ProcedenciaMensajeEnum;
import ve.smile.enums.PropiedadEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadClasificadorSugerenciaResponse;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.payload.response.PayloadContactoPortalResponse;

public class VM_Sugerencia {

	private Comunidad comunidad = new Comunidad();
	private ContactoPortal contactoPortal = new ContactoPortal();
	private List<ClasificadorSugerencia> cSugerencias;
	private ClasificadorSugerencia cSugerencia;
	private Configuracion confSugerencia =  new Configuracion();
	boolean emailSugerencia = false;
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_BUZON
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			if (!payloadConfiguracionResponse.getObjetos().isEmpty()) {
				this.confSugerencia.setValor(payloadConfiguracionResponse
						.getObjetos().get(0).getValor());
			}else{
				this.confSugerencia.setValor("false");
			}
			
		}
	}

	public List<ClasificadorSugerencia> getcSugerencias() {

		if (this.cSugerencias == null) {
			this.cSugerencias = new ArrayList<>();
		}
		if (this.cSugerencias.isEmpty()) {
			PayloadClasificadorSugerenciaResponse payloadClasificadorSugerenciaResponse = S.ClasificadorSugerenciaService
					.consultaClasificadorSugerenciaSinSession();

			this.cSugerencias.addAll(payloadClasificadorSugerenciaResponse
					.getObjetos());
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
		myFecha = new Date().getTime();
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

	@Command("incluir")
	public void incluir() {
		if (!isFormValidate()) {
			return;
		}
		ContactoPortal contactoPortal = this.getContactoPortal();
		ClasificadorSugerencia cSugerencia = this.getcSugerencia();
		contactoPortal.setFkClasificadorSugerencia(cSugerencia);
		Comunidad comunidad = new Comunidad();
		comunidad.setFechaCreacion(this.getMyFecha());
		contactoPortal.setFecha(this.getMyFecha());
		contactoPortal.setProcedencia(ProcedenciaMensajeEnum.PORTAL.ordinal());
		contactoPortal.setTipoContactoPortal(TipoContactoPortalEnum.SUGERENCIA
				.ordinal());
		contactoPortal.setEstatusContactoEnum(EstatusContactoEnum.PENDIENTE);

		contactoPortal.setFkComunidad(comunidad);
		comunidad.setApellido(this.getComunidad().getApellido());
		comunidad.setCorreo(this.getComunidad().getCorreo());
		comunidad.setNombre(this.getComunidad().getNombre());

		PayloadContactoPortalResponse payloadContactoPortalResponse = S.ContactoPortalService
				.incluirContactoPortal(contactoPortal);
		if(this.isEmailContactanos()){
			CreatesendEmail(cSugerencia.getNombre(), comunidad.getCorreo(),
					comunidad.getNombre(),
					comunidad.getApellido());
		}
		this.limpiar();
		UtilDialog
				.showMessageBoxSuccess("Gracias por contactarnos. Su informacion sera procesada.");
	}

	public boolean isFormValidate() {

		try {
			UtilValidate.validateString(contactoPortal.getContenido(),
					"mensaje");

		} catch (Exception e) {

			UtilDialog.showMessageBoxError(e.getMessage());
			return false;
		}

		return true;
	}

	public void limpiar() {
		this.setComunidad(new Comunidad());
		this.setContactoPortal(new ContactoPortal());
		this.setcSugerencia(new ClasificadorSugerencia());
		this.setcSugerencias(null);
		BindUtils.postNotifyChange(null, null, this, "contactoPortal");
		BindUtils.postNotifyChange(null, null, this, "comunidad");
		BindUtils.postNotifyChange(null, null, this, "cSugerencia");
		BindUtils.postNotifyChange(null, null, this, "cSugerencias");
	}

	public boolean isEmailContactanos() {
		if(confSugerencia.getValor().equals("true")){
			this.emailSugerencia = true;
		}
		return emailSugerencia;
	}
	
	public void CreatesendEmail(String tipoContacto, String correo, String nombre, String apellido){
		String asunto = "Recibimos tu " + tipoContacto ;
		String contenido = "Recibe un cordial saludo " + nombre + " " + apellido + ", Gracias por tu " + tipoContacto;
		System.out.println(asunto);
		System.out.println(contenido);
		
		try {
			new UtillMail().generateAndSendEmail(correo,asunto,contenido);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
