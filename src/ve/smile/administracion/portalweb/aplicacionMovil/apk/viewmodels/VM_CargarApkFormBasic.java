package ve.smile.administracion.portalweb.aplicacionMovil.apk.viewmodels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import lights.smile.util.Zki;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.dto.Multimedia;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.seguridad.enums.OperacionEnum;


public class VM_CargarApkFormBasic extends VM_WindowForm {

	private Configuracion configuracion;
	public Multimedia getMultimedia() {
		return multimedia;
	}

	public void setMultimedia(Multimedia multimedia) {
		this.multimedia = multimedia;
	}

	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}

	private Multimedia multimedia;
	private byte[] bytes = null;
	private String urlApk;

	public String getUrlApk() {
		return urlApk;
	}

	public void setUrlApk(String urlApk) {
		this.urlApk = urlApk;
	}


	@Init(superclass = true)
	public void childInit_VM_ConfiguracionFormBasic() {
		// NOTHING OK!
	}

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

	}

	@Command
	public void onUploadFile(
			@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {

		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null) {
			Media media = upEvent.getMedia();
			try {
				System.out.println(media.getName());
				Files.copy(new File(Zki.getPath() + media.getName()),
						media.getStreamData());
				urlApk = Zki.getPath() + media.getName();
				String url = media.getName();
				// ImportExcel(media);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			System.out.println(media.getByteData());
			// fileuploaded = true;
			// nombre = media.getName();
		}
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
		/*if (!isFormValidated()) {
			return true;
		}*/

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {

			if (bytes != null) {
				Multimedia multimedia = new Multimedia();
				multimedia.setIdMultimedia(16);
				multimedia.setNombre("Apk");
				multimedia.setTipoMultimedia(3);
				multimedia.setUrl(this.getUrlApk());
				multimedia.setExtension(1);
				multimedia.setDescripcion(this.multimedia.getDescripcion());

				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);

				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());
				this.getConfiguracion().setFkMultimedia(multimedia);
				this.getConfiguracion().setDescripcion("application/vnd.android.package-archive");
				this.getConfiguracion().setValor(this.multimedia.getDescripcion());
				this.getConfiguracion().setPropiedad(3);
				
			}
			
			
			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.incluir(getConfiguracion());
			this.getConfiguracion().setIdConfiguracion(
					((Double) payloadConfiguracionResponse.getInformacion("id"))
							.intValue());
			
			if (bytes != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

			Alert.showMessage(payloadConfiguracionResponse);
			if (!UtilPayload.isOK(payloadConfiguracionResponse)) {
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			if (this.getBytes() != null) {

				if (this.getConfiguracion().getFkMultimedia() == null
						|| this.getConfiguracion().getFkMultimedia()
								.getIdMultimedia() == null) {

					Multimedia multimedia = new Multimedia();
					multimedia.setIdMultimedia(16);
					multimedia.setNombre("Apk");
					multimedia.setTipoMultimedia(3);
					multimedia.setUrl(this.getUrlApk());
					multimedia.setExtension(1);
					multimedia.setDescripcion(this.multimedia.getDescripcion());

					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.incluir(multimedia);

					multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
							.getInformacion("id")).intValue());
					this.getConfiguracion().setFkMultimedia(multimedia);
					this.getConfiguracion().setDescripcion("application/vnd.android.package-archive");
					this.getConfiguracion().setValor(this.multimedia.getDescripcion());
					this.getConfiguracion().setPropiedad(3);

				} else {
					Multimedia multimedia = this.getConfiguracion().getFkMultimedia();
					multimedia.setNombre("Apk");
					multimedia.setTipoMultimedia(3);
					multimedia.setUrl(this.getUrlApk());
					multimedia.setExtension(1);
					multimedia.setDescripcion(this.multimedia.getDescripcion());

					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.incluir(multimedia);

					multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
							.getInformacion("id")).intValue());
					this.getConfiguracion().setFkMultimedia(multimedia);
					this.getConfiguracion().setDescripcion("application/vnd.android.package-archive");
					this.getConfiguracion().setValor(this.multimedia.getDescripcion());
					this.getConfiguracion().setPropiedad(3);
					
				}

			}
			Multimedia multimedia = this.getConfiguracion().getFkMultimedia();

			if (bytes == null && this.getConfiguracion().getFkMultimedia() != null) {
				Zki.remove(this.getConfiguracion().getFkMultimedia().getUrl());
				getConfiguracion().setFkMultimedia(null);
			}

			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.modificar(getConfiguracion());

			if (bytes == null && multimedia != null) {
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.eliminar(multimedia.getIdMultimedia());
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					return true;
				}
			}

			if (!UtilPayload.isOK(payloadConfiguracionResponse)) {
				Alert.showMessage(payloadConfiguracionResponse);
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
		return actionSalir(operacionEnum);
	}

	public Configuracion getConfiguracion() {
		return (Configuracion) DataCenter.getEntity();
	}

	/*public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getConfiguracion().getDireccion(),
					"Dirección", 250);
			UtilValidate.validateString(getConfiguracion().getNombre(), "Nombre",
					100);
			UtilValidate.validateString(getConfiguracion().getTelefono(),
					"Teléfono", 80);
			UtilValidate.validateString(getConfiguracion().getUrl(), "URL", 200);
			UtilValidate.validateNull(getConfiguracion().getFkCiudad(), "Ciudad");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}*/

	

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}


}
