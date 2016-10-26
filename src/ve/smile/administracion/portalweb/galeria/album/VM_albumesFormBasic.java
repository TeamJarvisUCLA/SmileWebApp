package ve.smile.administracion.portalweb.galeria.album;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadAlbumResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaAlbumResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.dto.Album;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.MultimediaAlbum;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusAlbumEnum;
import ve.smile.enums.TipoMultimediaEnum;

public class VM_albumesFormBasic extends VM_WindowForm {
	
	private Date fechaPublicacion = new Date();
	private Date fechaExpiracion = new Date();
	private boolean estatus = true;
	private boolean tipoEvento = true;

	private EventoPlanificado eventoPlanificado;
	private TsPlan tsPlan;

	private Album album;
	private List<MultimediaAlbum> multimediasAlbum;
	private List<Multimedia> multimediasNuevas;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		this.multimediasAlbum = new ArrayList<MultimediaAlbum>();
		this.multimediasNuevas = new ArrayList<Multimedia>();
	}

	@Command("buscarEventoPlan")
	public void buscarEventoPlan() {
		CatalogueDialogData<EventoPlanificado> catalogueDialogData = new CatalogueDialogData<EventoPlanificado>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<EventoPlanificado>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<EventoPlanificado> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						eventoPlanificado = catalogueDialogCloseEvent.getEntity();
						if(eventoPlanificado.getFkAlbum() != null){
							album = eventoPlanificado.getFkAlbum();
							fechaPublicacion = new Date(album.getFechaPublicacion());
							fechaExpiracion = new Date(album.getFechaExpiracion());
							if(album.getEstatusAlbum() == EstatusAlbumEnum.OCULTO.ordinal()){
								estatus = false;
							}else{
								estatus = true;
							}
							vaciarListMultimedias();
							PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
									.consultarMultimediaAlbum(1000, album.getIdAlbum());
							if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
								multimediasAlbum.addAll(payloadMultimediaAlbumResponse
										.getObjetos());
							}
							UtilDialog
							.showMessageBoxSuccess("Se han cargado los datos del album asociado al evento seleccionado");
						}else{
							album = new Album();
							fechaPublicacion = new Date();
							fechaExpiracion = new Date();
							estatus = true;
							multimediasAlbum = new ArrayList<MultimediaAlbum>();
							multimediasNuevas = new ArrayList<Multimedia>();
						}
						refreshAlbum();
						refreshEventoPlanificado();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/administracion/portalweb/galeria/album/catalogoEventoPlan.zul",
						catalogueDialogData);
	}
	
	@Command("buscarTsPlan")
	public void buscarTsPlan() {
		CatalogueDialogData<TsPlan> catalogueDialogData = new CatalogueDialogData<TsPlan>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<TsPlan>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<TsPlan> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						tsPlan = catalogueDialogCloseEvent.getEntity();
						if(tsPlan.getFkAlbum() != null){
							album = tsPlan.getFkAlbum();
							fechaPublicacion = new Date(album.getFechaPublicacion());
							fechaExpiracion = new Date(album.getFechaExpiracion());
							if(album.getEstatusAlbum() == EstatusAlbumEnum.OCULTO.ordinal()){
								estatus = false;
							}else{
								estatus = true;
							}
							vaciarListMultimedias();
							PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
									.consultarMultimediaAlbum(1000, album.getIdAlbum());
							if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
								multimediasAlbum.addAll(payloadMultimediaAlbumResponse
										.getObjetos());
							}
							UtilDialog
							.showMessageBoxSuccess("Se han cargado los datos del album asociado al trabajo social seleccionado");
						}else{
							album = new Album();
							fechaPublicacion = new Date();
							fechaExpiracion = new Date();
							estatus = true;
							multimediasAlbum = new ArrayList<MultimediaAlbum>();
							multimediasNuevas = new ArrayList<Multimedia>();
						}
						refreshAlbum();
						refreshTsPlan();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/administracion/portalweb/galeria/album/catalogoTsPlan.zul",
						catalogueDialogData);
	}
	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			this.getAlbum().setFechaPublicacion(getFechaPublicacion().getTime());
			this.getAlbum().setFechaExpiracion(getFechaExpiracion().getTime());
			if(this.estatus){
				this.getAlbum().setEstatusAlbumEnum(EstatusAlbumEnum.PUBLICADO);
			}else{
				this.getAlbum().setEstatusAlbumEnum(EstatusAlbumEnum.OCULTO);
			}
			PayloadAlbumResponse payloadAlbumResponse = S.AlbumService
					.incluir(getAlbum());
			this.album.setIdAlbum(((Double) payloadAlbumResponse.getInformacion("id")).intValue());
			
			if(tipoEvento){
				eventoPlanificado.setFkAlbum(this.album);
				PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
						.modificar(eventoPlanificado);
			}else{
				tsPlan.setFkAlbum(this.album);
				PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
						.modificar(tsPlan);
			}

			
			for(Iterator<MultimediaAlbum> i = this.multimediasAlbum.iterator(); i.hasNext(); ) {
				MultimediaAlbum item = i.next();
				
				item.setFkAlbum(getAlbum());
				
				PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
						.incluir(item);
			}

			if (!UtilPayload.isOK(payloadAlbumResponse)) {
				Alert.showMessage(payloadAlbumResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.getAlbum().setFechaPublicacion(getFechaPublicacion().getTime());
			this.getAlbum().setFechaExpiracion(getFechaExpiracion().getTime());
			if(this.estatus){
				this.getAlbum().setEstatusAlbumEnum(EstatusAlbumEnum.PUBLICADO);
			}else{
				this.getAlbum().setEstatusAlbumEnum(EstatusAlbumEnum.OCULTO);
			}
			PayloadAlbumResponse payloadAlbumResponse = S.AlbumService
					.modificar(getAlbum());
			
			for(Iterator<MultimediaAlbum> i = this.multimediasAlbum.iterator(); i.hasNext(); ) {
				MultimediaAlbum item = i.next();
				
				item.setFkAlbum(getAlbum());
				
				PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
						.incluir(item);
			}
			if (!UtilPayload.isOK(payloadAlbumResponse)) {
				Alert.showMessage(payloadAlbumResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {

		vaciarListMultimedias();
		return actionSalir(operacionEnum);
	}

	public Album getAlbum() {
		if(this.album == null){
			this.album = (Album) DataCenter.getEntity();
			if(this.album.getIdAlbum() != null)
				buscarInfoAlbum(this.album);
		}
		return album;
	}
	
	private void buscarInfoAlbum(Album album) {
		// TODO Auto-generated method stub
		fechaPublicacion = new Date(album.getFechaPublicacion());
		fechaExpiracion = new Date(album.getFechaExpiracion());
		if(album.getEstatusAlbum() == EstatusAlbumEnum.OCULTO.ordinal()){
			estatus = false;
		}else{
			estatus = true;
		}
		PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
				.consultarMultimediaAlbum(1000, album.getIdAlbum());
		if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
			multimediasAlbum.addAll(payloadMultimediaAlbumResponse
					.getObjetos());
		}
		if(tipoEvento){
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultaEventoPlanificadoAlbum(album.getIdAlbum());
			
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				if (!payloadEventoPlanificadoResponse.getObjetos().isEmpty()) {
					this.eventoPlanificado = payloadEventoPlanificadoResponse
							.getObjetos().get(0);
				}
				
			}
		}else{
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultaTSPlanificadoAlbum(album.getIdAlbum());
			
			if (UtilPayload.isOK(payloadTsPlanResponse)) {
				if (!payloadTsPlanResponse.getObjetos().isEmpty()) {
					this.tsPlan = payloadTsPlanResponse
							.getObjetos().get(0);
				}
				
			}
		}
		
		
		refreshEventoPlanificado();
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public boolean isFormValidated() {
		try {
			if(tipoEvento){
				UtilValidate.validateNull(eventoPlanificado, "Evento");				
			}else{
				UtilValidate.validateNull(tsPlan, "Trabajo social");
			}

			UtilValidate.validateString(getAlbum().getTitulo(), "Titulo", 100);
			UtilValidate.validateString(getAlbum().getDescripcion(), "Descripcion", 100);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}
	
	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(Date fechaInicio) {
		this.fechaPublicacion = fechaInicio;
	}

	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaFin) {
		this.fechaExpiracion = fechaFin;
	}
	
	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	
	public boolean isTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(boolean tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}
	
	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
	}

	public void refreshEventoPlanificado() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
	}
	public void refreshTsPlan() {
		BindUtils.postNotifyChange(null, null, this, "tsPlan");
	}

	public void refreshAlbum() {
		BindUtils.postNotifyChange(null, null, this, "album");
		BindUtils.postNotifyChange(null, null, this, "fechaPublicacion");
		BindUtils.postNotifyChange(null, null, this, "fechaExpiracion");
		BindUtils.postNotifyChange(null, null, this, "estatus");
		BindUtils.postNotifyChange(null, null, this, "multimediasAlbum");
	}
	
	public List<MultimediaAlbum> getMultimediasAlbum() {
		return multimediasAlbum;
	}

	public void setMultimediasAlbum(List<MultimediaAlbum> multimediasAlbum) {
		this.multimediasAlbum = multimediasAlbum;
	}
	
	@NotifyChange("multimediasAlbum")
	@Command("deleteMultimedia")
	public void deteleMultimedia(@BindingParam("multimedia") MultimediaAlbum multimedia){
		
		if(multimedia.getIdMultimediaAlbum() != null){
			PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
					.eliminar(multimedia.getIdMultimediaAlbum());
		}

		PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
				.eliminar(multimedia.getFkMultimedia().getIdMultimedia());
		
		for(Iterator<MultimediaAlbum> i = this.multimediasAlbum.iterator(); i.hasNext(); ) {
			MultimediaAlbum item = i.next();
		    if  (item.getFkMultimedia().equals(multimedia.getFkMultimedia())) {
		    	i.remove();
		    	break;
		    }
			
		}

	}
	
	@NotifyChange("multimediasAlbum")
  	@Command("btnUpload")
  	public void upload(BindContext ctx)
    {
  		boolean fileValid = true;
        UploadEvent e = (UploadEvent)ctx.getTriggerEvent();			
			
		if (e.getMedias() != null)
	            {			
	              	for (Media m : e.getMedias())
	                    {              			
		            		if (m instanceof org.zkoss.image.Image) {
		            			
		            			if (UtilMultimedia.validateImage(m.getName().substring(
		            					m.getName().lastIndexOf(".") + 1))) {
		            			} else {
		            				fileValid = false;
		            			}
		            		} else {
		            			fileValid = false;
		            		}
	                    }
	              	
	              	
	        		if (!fileValid) {
	        			Alert.showMessage("E: Error Code: 100-El formato de al menos una <b>imagen</b> es inválido");
	        		} else {
	        			
		              	for (Media m : e.getMedias())
	                    {	                    	
	                    	String extensionImage = m.getName().substring(
	                    			m.getName().lastIndexOf(".") + 1);
	                    	byte[] bytes = m.getByteData();
	                    	
	                    	Multimedia multimedia = new Multimedia();
	                    	
	                    	multimedia.setNombre(m.getName());
	                    	multimedia.setDescripcion(m.getName());
	                    	multimedia.setTipoMultimedia(TipoMultimediaEnum.ALBUM.ordinal());
	                    	multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
	                    			extensionImage).ordinal());
	                    	
	                		PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
	                				.incluir(multimedia);
	                		
	                		multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
	                				.getInformacion("id")).intValue());
	                		
	                		Zki.save(Zki.ALBUM, multimedia.getIdMultimedia(), extensionImage, bytes);

	                		multimedia.setUrl(Zki.ALBUM
	                				+ multimedia.getIdMultimedia() + "."
	                				+ extensionImage);
	                		payloadMultimediaResponse = S.MultimediaService
	                				.modificar(multimedia);
	                		
	                		MultimediaAlbum ma = new MultimediaAlbum();
	                		ma.setFkMultimedia(multimedia);
	                		this.multimediasAlbum.add(ma);
	                		this.multimediasNuevas.add(ma.getFkMultimedia());
	                    	
	                    }
	        		}
	            }
	      	else
	            {
			Messagebox.show("You uploaded no files!");
	            }
    }
	
	public void vaciarListMultimedias(){
		
		for(Iterator<Multimedia> i = this.multimediasNuevas.iterator(); i.hasNext(); ) {
			Multimedia item = i.next();
			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.eliminar(item.getIdMultimedia());
		}
		this.multimediasAlbum.clear();
		this.multimediasNuevas.clear();
		
	}

}
