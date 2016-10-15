package ve.smile.gestion.evento.planificaion.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.Evento;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Tarea;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadEventoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
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

public class VM_EventoPlanificadoRegistroIndex extends VM_WindowWizard implements UploadImageSingle{

	private EventoPlanificado eventoPlanificado;
	private Voluntario voluntario = new Voluntario();
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();

	private byte[] bytes;
	private String nameImage;
	private String urlImagen;
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		eventoPlanificado = new EventoPlanificado();
		voluntario = new Voluntario();
		directorio = new Directorio();
		fechaPlanificada = new Date();
	}
	
	@Command("buscarVoluntario")
	public void buscarVoluntario() {
		CatalogueDialogData<Voluntario> catalogueDialogData = new CatalogueDialogData<Voluntario>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Voluntario>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Voluntario> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						voluntario = catalogueDialogCloseEvent.getEntity();

						refreshVoluntario();
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
						directorio = catalogueDialogCloseEvent.getEntity();
						refreshDirectorio();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}
	
	@Override
	public BufferedImage getImageContent() {
		try {
			return loadImage();
		} catch (Exception e) {
			return null;
		}
	}
	
	private BufferedImage loadImage() throws Exception {
		try {
			Path path = Paths.get(this.getUrlImagen());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImagen()));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image) {
			bytes = media.getByteData();
			this.nameImage = media.getName();
			if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage
					.lastIndexOf(".") + 1))) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(new StringBuilder()
						.append("/Smile/Patrocinador/").append(nameImage)
						.toString());
				multimedia.setExtension(UtilMultimedia
						.stringToExtensionEnum(
								nameImage.substring(this.nameImage
										.lastIndexOf(".") + 1)).ordinal());
				multimedia.setDescripcion("Imgen del patrocinador.");
				this.getEventoPlanificado().setFkMultimedia(multimedia);
			} else {
				this.getEventoPlanificado().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}

		}
		
	}
	
	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSelectedObject(new EventoPlanificado());
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
		restartWizard();
		return "";
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
		this.getEventoPlanificado().setFkMultimedia(null);
		
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
	public IPayloadResponse<Evento> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
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
				UtilValidate.validateDate(this.getFechaPlanificada().getTime(),
						"Fecha Planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				UtilValidate.validateNull(this.getVoluntario()
						.getIdVoluntario(), "Responsable");
				UtilValidate.validateNull(this.getDirectorio()
						.getIdDirectorio(), "Directorio");
				UtilValidate.validateNull(this.getEventoPlanificado().getFkMultimedia(),
						"Imagen");
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
					this.getFechaPlanificada().getTime());
			this.getEventoPlanificado().setFkDirectorio(this.getDirectorio());
			this.getEventoPlanificado().setFkPersona(this.getVoluntario().getFkPersona());
			this.getEventoPlanificado().setPublicoPortal(true);
			this.getEventoPlanificado().setFkEvento((Evento) selectedObject);

			Multimedia multimedia = this.getEventoPlanificado().getFkMultimedia();

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.incluir(multimedia);
			if (!UtilPayload.isOK(payloadMultimediaResponse)) {
				return (String) payloadMultimediaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
					.getInformacion("id")).intValue());
			this.getEventoPlanificado().setFkMultimedia(multimedia);
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.incluir(this.getEventoPlanificado());
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				
				this.setEventoPlanificado(new EventoPlanificado());
				this.setDirectorio(new Directorio());
				this.setFechaPlanificada(new Date());
				this.setSelectedObject(new Evento());
				this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "directorio");
				BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
				BindUtils
						.postNotifyChange(null, null, this, "fechaPlanificada");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
				
			}
			goToNextStep();
			return (String) payloadEventoPlanificadoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
			
		}

		return "";
	}


	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public Voluntario getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(Voluntario voluntario) {
		this.voluntario = voluntario;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public Date getFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
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

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	
	
}
