package ve.smile.gestion.evento.actualizacionEventPs.viewmodels;

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
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;

public class VM_EventoPlanificado extends VM_WindowForm implements UploadImageSingle{

	private Date fechaInicio;
	private Date fechaFin;
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private Persona persona = new Persona()	;
	private String typeMedia;


	@Init(superclass = true)
	public void childInit() {
		if (this.getEventoPlanificado() != null
				&& this.getEventoPlanificado().getFkMultimedia() != null
				&& this.getEventoPlanificado().getFkMultimedia().getIdMultimedia() != null) {

			this.setUrlImage(this.getEventoPlanificado().getFkMultimedia().getUrl());

			this.nameImage = this.getEventoPlanificado().getFkMultimedia().getNombre();
			this.extensionImage = nameImage.substring(nameImage
					.lastIndexOf(".") + 1);
			this.typeMedia = this.getEventoPlanificado().getFkMultimedia()
					.getDescripcion();
		}
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;	

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.persona = getEventoPlanificado().getFkPersona();
			this.getEventoPlanificado().setFechaInicio(this.getFechaInicio().getTime());
			this.getEventoPlanificado().setFechaFin(this.getFechaFin().getTime());
			if (!this.getEventoPlanificado().getFkPersona().equals(this.persona)) {

				if (this.getEventoPlanificado().getFkPersona().getFkUsuario() != null
						&& this.getEventoPlanificado().getFkPersona().getFkUsuario()
								.getIdUsuario() != null) {
					String contenido = "Ya no es responsable del Evento "
							+ this.getEventoPlanificado().getFkEvento().getNombre()
							+ " con fecha de inicio el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaInicio().getTime())
							+ " y con fecha de finalizaci�n el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaFin().getTime());
					NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
							this.getEventoPlanificado().getFkPersona().getFkUsuario(),
							new Date().getTime(), this.getEventoPlanificado()
									.getIdEventoPlanificado(),
							EstatusNotificacionEnum.PENDIENTE.ordinal(),
							TipoReferenciaNotificacionEnum.EVENTO
									.ordinal(), contenido);
					PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
							.incluir(notificacionUsuario);
					if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
						Alert.showMessage(payloadNotificacionUsuarioResponse);
					}
				}

				this.getEventoPlanificado().setFkPersona(this.getPersona());

				if (this.getEventoPlanificado().getFkPersona().getFkUsuario() != null
						&& this.getEventoPlanificado().getFkPersona().getFkUsuario()
								.getIdUsuario() != null) {

					String contenido = "Ha sido asignado responsable del Evento "
							+ this.getEventoPlanificado().getFkEvento().getNombre()
							+ " con fecha de inicio el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaInicio().getTime())
							+ " y con fecha de finalización el "
							+ UtilConverterDataList.convertirLongADate(this
									.getFechaFin().getTime());
					NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
							this.getEventoPlanificado().getFkPersona().getFkUsuario(),
							new Date().getTime(), this.getEventoPlanificado()
									.getIdEventoPlanificado(),
							EstatusNotificacionEnum.PENDIENTE.ordinal(),
							TipoReferenciaNotificacionEnum.EVENTO
									.ordinal(), contenido);
					PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
							.incluir(notificacionUsuario);
					if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
						Alert.showMessage(payloadNotificacionUsuarioResponse);
					}
				}
			}

			if (bytes != null) {
				Multimedia multimedia = this.getEventoPlanificado().getFkMultimedia();
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
				Zki.save(Zki.EVENTO, this.getEventoPlanificado().getIdEventoPlanificado(),
						extensionImage, bytes);
			}

			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.modificar(this.getEventoPlanificado());
			Alert.showMessage(payloadEventoPlanificadoResponse);
			DataCenter.reloadCurrentNodoMenu();


			return true;
		}

		return false;
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

						getEventoPlanificado().setFkPersona(catalogueDialogCloseEvent.getEntity());

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
						getEventoPlanificado().setFkDirectorio(catalogueDialogCloseEvent.getEntity());
						refreshDirectorio();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}
	
	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
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

	public EventoPlanificado getEventoPlanificado() {
		return (EventoPlanificado) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		//TODO
		return true;
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

	public Date getFechaInicio() {
		return fechaInicio = new Date(getEventoPlanificado().getFechaInicio());
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin = new Date(getEventoPlanificado().getFechaFin());
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}



	
	
	

}
