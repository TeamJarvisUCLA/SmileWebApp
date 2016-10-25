package ve.smile.administracion.portalweb.galeria.album;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import app.UploadImageSingle;
import ve.smile.administracion.portalweb.galeria.galeria.VM_formMultimediaAlbum;
import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.MultimediaAlbum;
import ve.smile.enums.EstatusAlbumEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadAlbumResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaAlbumResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

public class VM_albumIndex extends VM_WindowWizard
		implements UploadImageSingle{
	
	private Album album;
	private Date fechaPublicacion = new Date();
	private Date fechaExpiracion = new Date();
	private boolean estatus = true;
	private byte[] bytes = null;
	private String urlImage;
	uploadfield uploadfield;
	private List<MultimediaAlbum> multimediasAlbum;

	@Init(superclass = true)
	public void childInit() {
		album = new Album();
		this.multimediasAlbum = new ArrayList<MultimediaAlbum>();
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	// EVENTO PLANIFICADO SELECTED
	public EventoPlanificado getEventoPlanSelected()
	{
		return (EventoPlanificado) this.selectedObject;
	}

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);
		
		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);
		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public IPayloadResponse<EventoPlanificado> getDataToTable(Integer cantidadRegistrosPagina,
			Integer pagina) {
		PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoPlanificadoResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		
		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/administracion/portalweb/galeria/album/selectEventoPlanificado.zul");
		urls.add("views/desktop/administracion/portalweb/galeria/album/registroAlbum.zul");
		urls.add("views/desktop/administracion/portalweb/galeria/album/addMultimedia.zul");
		urls.add("views/desktop/administracion/portalweb/galeria/album/registroCompletado.zul");
		return urls;
	}
	
	// SIGUIENTE
	@Override
	public String executeSiguiente(Integer currentStep) {
		if(currentStep == 1){
			if(getEventoPlanSelected().getFkAlbum() != null){
				this.album = this.getEventoPlanSelected().getFkAlbum();
				this.fechaPublicacion = new Date(album.getFechaPublicacion());
				this.fechaExpiracion = new Date(album.getFechaExpiracion());
				if(this.album.getEstatusAlbum() == EstatusAlbumEnum.OCULTO.ordinal()){
					this.estatus = false;
				}else{
					this.estatus = true;
				}
			}else{
				album = new Album();
				this.fechaPublicacion = new Date();
				this.fechaExpiracion = new Date();
			}
		}
		if (currentStep == 2)
		{
			if(this.estatus){
				this.album.setEstatusAlbumEnum(EstatusAlbumEnum.PUBLICADO);
			}else{
				this.album.setEstatusAlbumEnum(EstatusAlbumEnum.OCULTO);
			}
			this.album.setFechaPublicacion(getFechaPublicacion().getTime());
			this.album.setFechaExpiracion(getFechaExpiracion().getTime());
			PayloadAlbumResponse payloadAlbumResponse = S.AlbumService.incluir(this.album);
			this.album.setIdAlbum(((Double) payloadAlbumResponse.getInformacion("id")).intValue());
			this.getEventoPlanSelected().setFkAlbum(this.album);
			
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.modificar(this.getEventoPlanSelected());
			
			if (!UtilPayload.isOK(payloadEventoPlanificadoResponse))
			{
				Alert.showMessage(payloadEventoPlanificadoResponse);
			}
			
			if(this.multimediasAlbum.size() == 0){
				PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
						.consultarMultimediaAlbum(1000, this.album.getIdAlbum());
				if (UtilPayload.isOK(payloadMultimediaAlbumResponse)) {
					this.multimediasAlbum.addAll(payloadMultimediaAlbumResponse
							.getObjetos());
				}
			}
		}
		goToNextStep();

		return "";
	}
	
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificado</b>";
			}
		}
		if (currentStep == 2){
			try
			{
				UtilValidate.validateDate(this.getFechaExpiracion().getTime(), "Fecha expiracion", ValidateOperator.GREATER_THAN, new SimpleDateFormat("yyyy-MM-dd").format(this.getFechaPublicacion()), "dd/MM/yyyy");				
				UtilValidate.validateDate(this.getFechaPublicacion().getTime(), "Fecha publicacion", ValidateOperator.LESS_THAN, new SimpleDateFormat("yyyy-MM-dd").format(this.getFechaExpiracion()), "dd/MM/yyyy");

			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}

		return "";
	}
	
	// ATRAS
	@Override
	public String executeAtras(Integer currentStep) {
		
		if (currentStep == 2) {
			
			for(Iterator<MultimediaAlbum> i = this.multimediasAlbum.iterator(); i.hasNext(); ) {
				MultimediaAlbum item = i.next();
				vaciarMultiemdiaAlbum(item);
			}
			this.multimediasAlbum.clear();
		}

		goToPreviousStep();

		return "";
	}
	
	// FINALIZAR
	@Override
	public String executeFinalizar(Integer currentStep){
		if (currentStep == 3){
			
			for(Iterator<MultimediaAlbum> i = this.multimediasAlbum.iterator(); i.hasNext(); ) {
				MultimediaAlbum item = i.next();
				
				item.setFkAlbum(getAlbum());
				
				PayloadMultimediaAlbumResponse payloadMultimediaAlbumResponse = S.MultimediaAlbumService
						.incluir(item);
			}
			
			goToNextStep();
		}
		return "";
	}
	
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			if (this.multimediasAlbum.size() == 0) {
				return "E:Error Code 5-Debe agregar al menos una <b>Imagen</b> al album";
			}
		}
		return "";
	}
	
	// ACEPTAR
	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 4)
		{
			this.multimediasAlbum.clear();
			restartWizard();
		}
		return "";
	}
	
	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	
	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	
	@Override
	public BufferedImage getImageContent() {
		if (bytes != null) {
			try {
				return ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {
				return null;
			}
		}

		if (urlImage != null) {
			bytes = Zki.getBytes(urlImage);
			return Zki.getBufferedImage(urlImage);
		}

		return null;
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

		Alert.showMessage(payloadMultimediaResponse);
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
	                		ma.setFkAlbum(getAlbum());
	                		ma.setFkMultimedia(multimedia);
	                		this.multimediasAlbum.add(ma);

	                    	
	                    }
	        		}
	            }
	      	else
	            {
			Messagebox.show("You uploaded no files!");
	            }
    }
	
	public List<MultimediaAlbum> getMultimediasAlbum() {
		return multimediasAlbum;
	}

	public void setMultimediasAlbum(List<MultimediaAlbum> multimediasAlbum) {
		this.multimediasAlbum = multimediasAlbum;
	}
	
	public void vaciarMultiemdiaAlbum(MultimediaAlbum multimedia){

		PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
				.eliminar(multimedia.getFkMultimedia().getIdMultimedia());
			
		}

	
	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		// TODO Auto-generated method stub
		
	}
}
