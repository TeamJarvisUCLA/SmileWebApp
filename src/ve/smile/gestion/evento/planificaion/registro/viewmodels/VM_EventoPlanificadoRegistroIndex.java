package ve.smile.gestion.evento.planificaion.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Evento;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import app.UploadImageSingle;

public class VM_EventoPlanificadoRegistroIndex extends VM_WindowWizard
		implements UploadImageSingle {

	private EventoPlanificado eventoPlanificado;	
	private Date fechaInicio = new Date();
	private Date fechaFin = new Date();
	private Persona persona = new Persona();

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		eventoPlanificado = new EventoPlanificado();		
		
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
						"views/desktop/gestion/evento/planificacion/registro/catalogoVoluntario.zul",
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
						"views/desktop/gestion/evento/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshEventoPlanificado() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSelectedObject(new EventoPlanificado());
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
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
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));


		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard3.add(operacionWizardCustom);

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<Evento> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadEventoResponse payloadEventoResponse = S.EventoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadEventoResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}
	
	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	@Override
	public List<String> getUrlPageToStep() {

		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/evento/planificacion/registro/selectEvento.zul");
		urls.add("views/desktop/gestion/evento/planificacion/registro/eventoPlanificadoFormBasic.zul");
		urls.add("views/desktop/gestion/evento/planificacion/registro/registroCompletado.zul");

		return urls;

	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento</b>";
			}
		}

		if (currentStep == 2) {
			return "E:Error Code 5-No hay otro paso";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			try {
				Date fechainicio = new Date(fechaInicio.getYear(), fechaInicio.getMonth(), fechaInicio.getDate(), 23, 59,59);			
				if(fechainicio.getTime()<new Date().getTime()){
					return "E:Error Code 5-La fecha inicio no puede ser menor que la fecha de actual";
					 
				}
				Date fechafin = new Date(fechaFin.getYear(), fechaFin.getMonth(), fechaInicio.getDate(), 23, 59,59);
				if(fechafin.getTime()<new Date().getTime()){
					return "E:Error Code 5-La fecha fin no puede ser menor que la fecha de actual";
					
				}
				if(fechaInicio.getTime()>fechaFin.getTime()){
					return "E:Error Code 5-La fecha fin no puede ser menor que la fecha de inicio";
					
				}
				UtilValidate.validateNull(this.getEventoPlanificado().getFkPersona()
						, "Responsable");
				UtilValidate.validateNull(this.getEventoPlanificado().getFkDirectorio()
						, "Directorio");
				UtilValidate.validateNull(this.getBytes(), "Imagen");
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getEventoPlanificado().setFechaPlanificada(
					new Date().getTime());
			this.eventoPlanificado.setFechaFin(this.fechaFin.getTime());
			this.eventoPlanificado.setFechaInicio(this.fechaInicio.getTime());
			this.eventoPlanificado.setPublicoPortal(true);
			this.eventoPlanificado.setEstatusEvento(
					EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal());
			this.getEventoPlanificado().setFkEvento((Evento) selectedObject);

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
					return (String) payloadMultimediaResponse
							.getInformacion(IPayloadResponse.MENSAJE);
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
					return (String) payloadMultimediaResponse
							.getInformacion(IPayloadResponse.MENSAJE);

				}
			}
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				
			}
			
			if (this.getEventoPlanificado().getFkPersona().getFkUsuario() != null
					&& this.getEventoPlanificado().getFkPersona().getFkUsuario()
							.getIdUsuario() != null) {
			String contenido = "Ha sido asignado responsable del Evento "
					+ this.getEventoPlanificado().getFkEvento().getNombre()
					+ " a realizar el " + this.getFechaInicio();
			NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
					this.getEventoPlanificado().getFkPersona().getFkUsuario(),
					new Date().getTime(),
					((Double) payloadEventoPlanificadoResponse
							.getInformacion("id")).intValue(),
					EstatusNotificacionEnum.PENDIENTE.ordinal(),
					TipoReferenciaNotificacionEnum.EVENTO.ordinal(), contenido);
				PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
						.incluir(notificacionUsuario);
				if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
					return (String) payloadNotificacionUsuarioResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}
				this.setEventoPlanificado(new EventoPlanificado());			
				this.setFechaInicio(new Date());
				this.setFechaFin(new Date());
				this.setSelectedObject(new Evento());								
				BindUtils.postNotifyChange(null, null, this,
						"eventoPlanificado");
				BindUtils
						.postNotifyChange(null, null, this, "fechaPlanificada");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");				

			
			goToNextStep();			
		}

		return "";
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
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	

}
