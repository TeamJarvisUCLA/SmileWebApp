package ve.smile.web.viewmodels.contactanos;	

	import karen.core.dialog.generic.controllers.C_WindowDialog;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.message_box.events.MessageBoxDialogCloseEvent;

import org.zkoss.bind.annotation.Init;
import org.zkoss.chart.rt.Refresh;
import org.zkoss.zk.ui.event.Event;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorSugerencia;
import ve.smile.dto.Comunidad;
import ve.smile.dto.ContactoPortal;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadContactoPortalResponse;

	public class C_Contactanos extends C_WindowDialog {
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
				VM_Contactanos  incluirContactanosContacto = (VM_Contactanos) vm();
				ContactoPortal contactoPortal=incluirContactanosContacto.getContactoPortal()  ;
				ClasificadorSugerencia cSugerencia=incluirContactanosContacto.getcSugerencia();
				contactoPortal.setFkClasificadorSugerencia(cSugerencia);
				Comunidad comunidad=new Comunidad();
                comunidad.setFechaCreacion(incluirContactanosContacto.getMyFecha());
				contactoPortal.setFecha(incluirContactanosContacto.getMyFecha());
				contactoPortal.setProcedencia(ProcedenciaEnum.TRABAJO_SOCIAL.ordinal());
				contactoPortal.setTipoContactoPortal(TipoContactoPortalEnum.CONTACTO.ordinal());

				contactoPortal.setFkComunidad(comunidad);			
				comunidad.setApellido(incluirContactanosContacto.getComunidad().getApellido());
				comunidad.setCorreo(incluirContactanosContacto.getComunidad().getCorreo());
				comunidad.setNombre(incluirContactanosContacto.getComunidad().getNombre());


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



