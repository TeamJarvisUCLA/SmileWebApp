package ve.smile.gestion.trabajo_social.planificacion.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import lights.smile.util.UtilConverterDataList;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Multimedia;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.Persona;
import ve.smile.dto.TrabajoSocial;
import ve.smile.dto.TsPlan;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.EstatusTrabajoSocialPlanificadoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import app.UploadImageSingle;

public class VM_TrabajoSocialPlanificadoRegistroIndex extends VM_WindowWizard
		implements UploadImageSingle {

	private TsPlan tsPlan;
	private Persona persona = new Persona();
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();

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
		tsPlan = new TsPlan();
		persona = new Persona();
		directorio = new Directorio();
		fechaPlanificada = new Date();
	}

	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
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

						persona = catalogueDialogCloseEvent.getEntity();

						refreshPersona();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/registro/catalogoTrabajadorVoluntario.zul",
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
						"views/desktop/gestion/trabajoSocial/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshPersona() {
		BindUtils.postNotifyChange(null, null, this, "persona");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
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
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/selectTrabajoSocial.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/trabajoSocialPlanificadoFormBasic.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/registroCompletado.zul");

		return urls;
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
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadTrabajoSocialResponse;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		if (currentStep == 3) {

			restartWizard();
			this.setTsPlan(new TsPlan());
			this.bytes = null;
			this.setDirectorio(new Directorio());
			this.setFechaPlanificada(new Date());
			this.setSelectedObject(new TrabajoSocial());
			this.setPersona(new Persona());
			BindUtils.postNotifyChange(null, null, this, "directorio");
			BindUtils.postNotifyChange(null, null, this, "tsPlan");
			BindUtils.postNotifyChange(null, null, this, "fechaPlanificada");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
			BindUtils.postNotifyChange(null, null, this, "voluntario");

		}
		return super.executeCustom1(currentStep);
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social</b>";
			}
			this.getTsPlan()
					.setFkTrabajoSocial(this.getTrabajoSocialSelected());
		}
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			try {
				UtilValidate.validateDate(this.getFechaInicio().getTime(),
						"Fecha Inicio", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				UtilValidate.validateDate(this.getFechaFin().getTime(),
						"Fecha Fin", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(this
								.getFechaFin()), "dd/MM/yyyy");
				UtilValidate.validateNull(this.getPersona().getIdPersona(),
						"Responsable");
				UtilValidate.validateNull(this.getDirectorio()
						.getIdDirectorio(), "Directorio");
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
			this.getTsPlan().setFechaInicio(this.getFechaInicio().getTime());
			this.getTsPlan().setEstatusTsPlan(
					EstatusTrabajoSocialPlanificadoEnum.PLANIFICADO.ordinal());
			this.getTsPlan().setFechaFin(this.getFechaFin().getTime());
			this.getTsPlan().setFechaPlanificada(
					this.getFechaPlanificada().getTime());
			this.getTsPlan().setFkDirectorio(this.getDirectorio());
			this.getTsPlan().setFkPersona(this.getPersona());
			this.getTsPlan().setPublicoPortal(true);

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
				this.getTsPlan().setFkMultimedia(multimedia);
			}

			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.incluir(this.tsPlan);

			this.getTsPlan().setIdTsPlan(
					((Double) payloadTsPlanResponse.getInformacion("id"))
							.intValue());
			if (bytes != null) {
				Zki.save(Zki.TRABAJO_SOCIAL, this.getTsPlan().getIdTsPlan(),
						extensionImage, bytes);
				Multimedia multimedia = this.getTsPlan().getFkMultimedia();
				multimedia.setNombre(Zki.TRABAJO_SOCIAL
						+ this.getTsPlan().getIdTsPlan() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.TRABAJO_SOCIAL
						+ this.getTsPlan().getIdTsPlan() + "."
						+ this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					return (String) payloadMultimediaResponse
							.getInformacion(IPayloadResponse.MENSAJE);

				}
			}
			if (this.getTsPlan().getFkPersona().getFkUsuario() != null
					&& this.getTsPlan().getFkPersona().getFkUsuario()
							.getIdUsuario() != null) {
				String contenido = "Ha sido asignado responsable del Evento "
						+ this.getTsPlan().getFkTrabajoSocial().getNombre()
						+ " a realizar el "
						+ UtilConverterDataList.convertirLongADate(this
								.getFechaPlanificada().getTime());
				NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
						this.getTsPlan().getFkPersona().getFkUsuario(),
						new Date().getTime(),
						this.getTsPlan().getIdTsPlan(),
						EstatusNotificacionEnum.PENDIENTE.ordinal(),
						TipoReferenciaNotificacionEnum.TRABAJO_SOCIAL.ordinal(),
						contenido);
				PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
						.incluir(notificacionUsuario);
				if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
					return (String) payloadNotificacionUsuarioResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}

			goToNextStep();

		}

		return "";
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
				this.getDirectorio().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			this.getDirectorio().setFkMultimedia(null);
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

	public TrabajoSocial getTrabajoSocialSelected() {
		return (TrabajoSocial) selectedObject;
	}
}
