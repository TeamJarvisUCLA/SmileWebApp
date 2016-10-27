package ve.smile.gestion.eventosOcaionales.registroSolicitud.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;
import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Evento;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
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
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

public class VM_RegistroSolicitudEvento extends VM_WindowForm implements UploadImageSingle{
	
	private EventoPlanificado eventoPlanificado = new EventoPlanificado();
	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();	
		
				
					
				
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));


			return operacionesForm;
		
		
	}
	
	@Command("buscarEvento")
	public void buscarEvento() {
		CatalogueDialogData<Evento> catalogueDialogData = new CatalogueDialogData<Evento>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Evento>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Evento> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						
						eventoPlanificado.setFkEvento(catalogueDialogCloseEvent.getEntity());

						refreshEventoPlanificado();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/eventosOcacionales/registroSolicitudEvento/catalogoEvento.zul",
						catalogueDialogData);
	}
	
	@Command("buscarVoluntario")
	public void buscarVoluntario() {
		CatalogueDialogData<Persona> catalogueDialogData = new CatalogueDialogData<Persona>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Persona>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Persona> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						
						eventoPlanificado.setFkPersona(catalogueDialogCloseEvent.getEntity());

						refreshEventoPlanificado();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/eventosOcacionales/registroSolicitudEvento/catalogoVoluntario.zul",
						catalogueDialogData);
	}

	@Command("buscarDirectorio")
	public void buscarDirectorio() {
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						eventoPlanificado.setFkDirectorio(catalogueDialogCloseEvent.getEntity());
						refreshEventoPlanificado();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/eventosOcacionales/registroSolicitudEvento/catalogoDirectorio.zul",
						catalogueDialogData);
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

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();

		if (media instanceof org.zkoss.image.Image) {

			if (UtilMultimedia.validateImage(media.getName().substring(
					media.getName().lastIndexOf(".") + 1))) {

				this.extensionImage = media.getName().substring(
						media.getName().lastIndexOf(".") + 1);

				this.bytes = media.getByteData();
				this.typeMedia = media.getContentType();

			} else {
				this.getEventoPlanificado().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getEventoPlanificado().setFkMultimedia(null);
			Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
		}
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}						
			this.getEventoPlanificado().setFechaPlanificada(
					new Date().getTime());
			this.eventoPlanificado.setFechaFin(this.fechaFin.getTime());
			this.eventoPlanificado.setFechaInicio(this.fechaInicio.getTime());
			this.eventoPlanificado.setPublicoPortal(false);
			this.eventoPlanificado.setEstatusEvento(
					EstatusEventoPlanificadoEnum.POSTULADO.ordinal());			
			if (bytes != null) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
						extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);

				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse
							.getInformacion(IPayloadResponse.MENSAJE));
				 return true;
				}
				this.getEventoPlanificado().setFkMultimedia(multimedia);
			}
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.incluir(this.getEventoPlanificado());
											
			this.getEventoPlanificado().setIdEventoPlanificado(
					((Double) payloadEventoPlanificadoResponse
							.getInformacion("id")).intValue());			
			if (bytes != null) {
				Zki.save(Zki.EVENTO, this.getEventoPlanificado()
						.getIdEventoPlanificado(), extensionImage, bytes);
				Multimedia multimedia = this.getEventoPlanificado()
						.getFkMultimedia();
				multimedia.setNombre(Zki.EVENTO
						+ this.getEventoPlanificado()
								.getIdEventoPlanificado() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.EVENTO
						+ this.getEventoPlanificado()
								.getIdEventoPlanificado() + "."
						+ this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					 Alert.showMessage(payloadMultimediaResponse
							.getInformacion(IPayloadResponse.MENSAJE));
					 return true;
				}
			}
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				 Alert.showMessage(payloadEventoPlanificadoResponse
							.getInformacion(IPayloadResponse.MENSAJE));						
				this.setEventoPlanificado(new EventoPlanificado());			
				this.setFechaInicio(new Date());
				this.setFechaFin(new Date());
this.bytes = null;
				BindUtils.postNotifyChange(null, null, this,
						"*");
				BindUtils
						.postNotifyChange(null, null, this, "fechaInicio");
				BindUtils
				.postNotifyChange(null, null, this, "fechaFin");								
			DataCenter.reloadCurrentNodoMenu();

			return true;
		
			}
return false;
	
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
	}

	
	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}
	
	public void refreshEventoPlanificado() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
	}
	
	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getNameImage() {
		return nameImage;
	}

	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	public String getExtensionImage() {
		return extensionImage;
	}

	public void setExtensionImage(String extensionImage) {
		this.extensionImage = extensionImage;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}
	
	public boolean isFormValidated() {
		try {
			Date fechainicio = new Date(fechaInicio.getYear(), fechaInicio.getMonth(), fechaInicio.getDate(), 23, 59,59);			
			if(fechainicio.getTime()<new Date().getTime()){
				Alert.showMessage("E:Error Code 5-La fecha inicio no puede ser menor que la fecha de actual");
				return false;
			}
			Date fechafin = new Date(fechaFin.getYear(), fechaFin.getMonth(), fechaInicio.getDate(), 23, 59,59);
			if(fechafin.getTime()<new Date().getTime()){
				Alert.showMessage("E:Error Code 5-La fecha fin no puede ser menor que la fecha de actual");
				return false;
			}
			if(fechaInicio.getTime()>fechaFin.getTime()){
				Alert.showMessage("E:Error Code 5-La fecha fin no puede ser menor que la fecha de inicio");
				return false;
			}
			UtilValidate.validateNull(getEventoPlanificado().getFkEvento(), "Evento");
			UtilValidate.validateNull(getEventoPlanificado().getFkPersona(), "Responsable");
			UtilValidate.validateNull(getEventoPlanificado().getFkDirectorio(), "Directorio");
			UtilValidate.validateNull(this.bytes, "Imagen");		
			
					
		return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
	

}
