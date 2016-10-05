package ve.smile.web.viewmodels.buzon;	

	import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.dto.Comunidad;
import ve.smile.dto.ContactoPortal;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Padrino;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadContactoPortalResponse;
import ve.smile.web.viewmodels.faq.VM_Pregunta;

	public class C_Sugerencia extends C_WindowDialog {
		private static final long serialVersionUID = 1L;
		
		ContactoPortal contactoPortal;
		Comunidad comunidad;

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
				VM_Sugerencia  incluirSugerenciaContacto = (VM_Sugerencia) vm();
				ContactoPortal contactoPortal=incluirSugerenciaContacto.getContactoPortal()  ;
				ClasificadorSugerencia cSugerencia=incluirSugerenciaContacto.getcSugerencia();
				contactoPortal.setFkClasificadorSugerencia(cSugerencia);
				Comunidad comunidad=new Comunidad();
                comunidad.setFechaCreacion(incluirSugerenciaContacto.getMyFecha());
				contactoPortal.setFecha(incluirSugerenciaContacto.getMyFecha());
				contactoPortal.setProcedencia(ProcedenciaEnum.TRABAJO_SOCIAL.ordinal());
				contactoPortal.setTipoContactoPortal(TipoContactoPortalEnum.SUGERENCIA.ordinal());

				contactoPortal.setFkComunidad(comunidad);			
				comunidad.setApellido(incluirSugerenciaContacto.getComunidad().getApellido());
				comunidad.setCorreo(incluirSugerenciaContacto.getComunidad().getCorreo());
				comunidad.setNombre(incluirSugerenciaContacto.getComunidad().getNombre());


				PayloadContactoPortalResponse payloadContactoPortalResponse =  S.ContactoPortalService.incluirContactoPortal(contactoPortal);

				close(dialogCloseEvent);	


			}
		
		@Override
		public void onCancel(Event event) {
			close(new MessageBoxDialogCloseEvent(event, DialogActionEnum.CANCELAR));	
		}
		
		//@Command
		//@NotifyChange({"contactoPortal"})
        //public void limpiar(){
	
	//contactoPortal= new ContactoPortal();
	//DataCenter.updateSrcPageContent(contactoPortal, null, "/web/views/...");
   	
//}

		@Override
		public void doOnCreate() {
			// TODO Auto-generated method stub		
		}
		
		
		


	}



