package ve.smile.gestion.trabajo_social.planificacion.actualizacion.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

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
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_TrabajoSocialPlanificadoFormBasic extends VM_WindowForm
		implements UploadImageSingle {
	private Persona persona = new Persona();
	private Directorio directorio = new Directorio();

	private Date fechaInicio;
	private Date fechaFin;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;

	private TrabajoSocial trabajoSocial;

	@Init(superclass = true)
	public void childInit() {
		this.setDirectorio(this.getTsPlan().getFkDirectorio());
		this.setFechaFin(new Date(this.getTsPlan().getFechaFin()));
		this.setFechaInicio(new Date(this.getTsPlan().getFechaInicio()));
		this.setPersona(this.getTsPlan().getFkPersona());
		if (this.getTsPlan() != null
				&& this.getTsPlan().getFkMultimedia() != null
				&& this.getTsPlan().getFkMultimedia().getIdMultimedia() != null) {

			this.setUrlImage(this.getTsPlan().getFkMultimedia().getUrl());

			this.nameImage = this.getTsPlan().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getTsPlan().getFkMultimedia()
					.getDescripcion();
		}
	}

	public TrabajoSocial getTrabajoSocial() {
		return trabajoSocial;
	}

	public void setTrabajoSocial(TrabajoSocial trabajoSocial) {
		this.trabajoSocial = trabajoSocial;
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

	@Command("buscarTrabajoSocial")
	public void buscarTrabajoSocial() {
		CatalogueDialogData<TrabajoSocial> catalogueDialogData = new CatalogueDialogData<TrabajoSocial>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<TrabajoSocial>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<TrabajoSocial> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						trabajoSocial = catalogueDialogCloseEvent.getEntity();

						refreshTrabajoSocial();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actualizacion/catalogoTrabajoSocial.zul",
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

						persona = catalogueDialogCloseEvent.getEntity();

						refreshPersona();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actualizacion/catalogoTrabajadorVoluntario.zul",
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
						"views/desktop/gestion/trabajoSocial/planificacion/actualizacion/catalogoDirectorio.zul",
						catalogueDialogData);
	}

	public void refreshTrabajoSocial() {
		this.getTsPlan().setFkTrabajoSocial(trabajoSocial);

		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public void refreshPersona() {
		BindUtils.postNotifyChange(null, null, this, "persona");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
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

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			this.getTsPlan().setFechaInicio(this.getFechaInicio().getTime());
			this.getTsPlan().setFechaFin(this.getFechaFin().getTime());
			this.getTsPlan().setFkDirectorio(this.getDirectorio());
			if (!this.getTsPlan().getFkPersona().equals(this.getPersona())) {

				if (this.getTsPlan().getFkPersona().getFkUsuario() != null
						&& this.getTsPlan().getFkPersona().getFkUsuario()
								.getIdUsuario() != null) {
					String contenido = "Ya no es responsable del Trabajo Socual "
							+ this.getTsPlan().getFkTrabajoSocial().getNombre()
							+ " con fecha de inicio el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaInicio().getTime())
							+ " y con fecha de finalización el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaFin().getTime());
					NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
							this.getTsPlan().getFkPersona().getFkUsuario(),
							new Date().getTime(), this.getTsPlan()
									.getIdTsPlan(),
							EstatusNotificacionEnum.PENDIENTE.ordinal(),
							TipoReferenciaNotificacionEnum.TRABAJO_SOCIAL
									.ordinal(), contenido);
					PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
							.incluir(notificacionUsuario);
					if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
						Alert.showMessage(payloadNotificacionUsuarioResponse);
					}
				}

				this.getTsPlan().setFkPersona(this.getPersona());

				if (this.getTsPlan().getFkPersona().getFkUsuario() != null
						&& this.getTsPlan().getFkPersona().getFkUsuario()
								.getIdUsuario() != null) {

					String contenido = "Ha sido asignado responsable del Trabajo Socual "
							+ this.getTsPlan().getFkTrabajoSocial().getNombre()
							+ " con fecha de inicio el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaInicio().getTime())
							+ " y con fecha de finalización el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaFin().getTime());
					NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
							this.getTsPlan().getFkPersona().getFkUsuario(),
							new Date().getTime(), this.getTsPlan()
									.getIdTsPlan(),
							EstatusNotificacionEnum.PENDIENTE.ordinal(),
							TipoReferenciaNotificacionEnum.TRABAJO_SOCIAL
									.ordinal(), contenido);
					PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
							.incluir(notificacionUsuario);
					if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
						Alert.showMessage(payloadNotificacionUsuarioResponse);
					}
				}
			}

			if (bytes != null) {
				Multimedia multimedia = this.getTsPlan().getFkMultimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
						extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
				}
				Zki.save(Zki.TRABAJO_SOCIAL, this.getTsPlan().getIdTsPlan(),
						extensionImage, bytes);
			}

			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.modificar(this.getTsPlan());
			Alert.showMessage(payloadTsPlanResponse);
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
		return actionSalir(operacionEnum);
	}

	public TsPlan getTsPlan() {
		return (TsPlan) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
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
				this.urlImage = new StringBuilder().append(Zki.TRABAJO_SOCIAL)
						.append(this.getTsPlan().getIdTsPlan()).append(".")
						.append(extensionImage).toString();
				this.nameImage = new StringBuilder().append(Zki.TRABAJO_SOCIAL)
						.append(this.getTsPlan().getIdTsPlan()).append(".")
						.append(extensionImage).toString();

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

}
