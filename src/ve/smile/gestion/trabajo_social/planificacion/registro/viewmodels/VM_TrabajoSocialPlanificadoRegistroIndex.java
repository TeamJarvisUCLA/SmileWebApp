package ve.smile.gestion.trabajo_social.planificacion.registro.viewmodels;

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

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Multimedia;
import ve.smile.dto.TrabajoSocial;
import ve.smile.dto.TsPlan;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import app.UploadImageSingle;

public class VM_TrabajoSocialPlanificadoRegistroIndex extends
		VM_WindowWizard<TrabajoSocial> implements UploadImageSingle {

	private TsPlan tsPlan;
	private Voluntario voluntario = new Voluntario();
	private Directorio directorio = new Directorio();
	private Date fechaPlanificada = new Date();

	private byte[] bytes;
	private String nameImage;
	private String urlImagen;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		tsPlan = new TsPlan();
		voluntario = new Voluntario();
		directorio = new Directorio();
		fechaPlanificada = new Date();
	}

	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
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
						"views/desktop/gestion/trabajoSocial/planificacion/registro/catalogoVoluntario.zul",
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

	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart");
		iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/selectTrabajoSocial.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/trabajoSocialPlanificadoFormBasic.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroTrabajoSocialPlanificado.zul");

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
	public IPayloadResponse<TrabajoSocial> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajoSocialResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social</b>";
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
				UtilValidate.validateNull(this.getTsPlan().getFkMultimedia(),
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
			this.getTsPlan().setFechaPlanificada(
					this.getFechaPlanificada().getTime());
			this.getTsPlan().setFkDirectorio(this.getDirectorio());
			this.getTsPlan().setFkPersona(this.getVoluntario().getFkPersona());
			this.getTsPlan().setPublicoPortal(true);

			Multimedia multimedia = this.getTsPlan().getFkMultimedia();

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.incluir(multimedia);
			if (!UtilPayload.isOK(payloadMultimediaResponse)) {
				return (String) payloadMultimediaResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}
			multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
					.getInformacion("id")).intValue());
			this.getTsPlan().setFkMultimedia(multimedia);
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.incluir(this.tsPlan);
			if (UtilPayload.isOK(payloadTsPlanResponse)) {
				restartWizard();
				this.setTsPlan(new TsPlan());
				this.setDirectorio(new Directorio());
				this.setFechaPlanificada(new Date());
				this.setSelectedObject(new TrabajoSocial());
				this.setVoluntario(new Voluntario());
				BindUtils.postNotifyChange(null, null, this, "directorio");
				BindUtils.postNotifyChange(null, null, this, "tsPlan");
				BindUtils
						.postNotifyChange(null, null, this, "fechaPlanificada");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
			}
			return (String) payloadTsPlanResponse
					.getInformacion(IPayloadResponse.MENSAJE);

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}

	@Override
	public BufferedImage getImageContent() {
		try {
			return loadImage();
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
				this.getTsPlan().setFkMultimedia(multimedia);
			} else {
				this.getTsPlan().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}

		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
		this.getTsPlan().setFkMultimedia(null);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
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

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
}
