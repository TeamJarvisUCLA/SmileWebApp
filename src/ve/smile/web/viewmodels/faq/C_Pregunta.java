package ve.smile.web.viewmodels.faq;


	

	import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;

	import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import com.sun.research.ws.wadl.Request;

	import ve.smile.consume.services.S;
import ve.smile.dao.ComunidadDAO;
import ve.smile.dto.Comunidad;
import ve.smile.dto.ContactoPortal;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Persona;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadComunidadResponse;
import ve.smile.payload.response.PayloadContactoPortalResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.web.viewmodels.faq.VM_Pregunta;

	public class C_Pregunta extends C_WindowDialog {
		private static final long serialVersionUID = 1L;

		@Init(superclass = true)
		public void childInit() {
			// NOTHING OK!
		}

		@Override
		public void onAccept(Event event) {
			//if (!isFormValidated()) {
				//return;
			//}

				DialogCloseEvent dialogCloseEvent = new DialogCloseEvent(event, DialogActionEnum.ACEPTAR);
				VM_Pregunta  incluirPreguntaContacto = (VM_Pregunta) vm();
				ContactoPortal contactoPortal=incluirPreguntaContacto.getContactoPortal()  ;
				//comunidad.setFechaCreacion();	
				Comunidad comunidad=new Comunidad();
                comunidad.setFechaCreacion(incluirPreguntaContacto.getMyFecha());
				contactoPortal.setFecha(incluirPreguntaContacto.getMyFecha());
				contactoPortal.setProcedencia(ProcedenciaEnum.TRABAJO_SOCIAL.ordinal());
				contactoPortal.setTipoContactoPortal(TipoContactoPortalEnum.PREGUNTA.ordinal());

				contactoPortal.setFkComunidad(comunidad);
				
			
				comunidad.setApellido(incluirPreguntaContacto.getComunidad().getApellido());
				comunidad.setCorreo(incluirPreguntaContacto.getComunidad().getCorreo());
				comunidad.setNombre(incluirPreguntaContacto.getComunidad().getNombre());

				

				PayloadContactoPortalResponse payloadContactoPortalResponse =  S.ContactoPortalService.incluirContactoPortal(contactoPortal);
				close(dialogCloseEvent);		

			}
		
		
		@Override
		public void onCancel(Event event) {
			close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));	
		}


		@Override
		public void doOnCreate() {
			// TODO Auto-generated method stub		
		}
		
		


	}



