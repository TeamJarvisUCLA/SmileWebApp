package ve.smile.datos.parametros.valores.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Estado;
import ve.smile.dto.Valores;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadValoresResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ValoresFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private List<Estado> estados;
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String urlImagenAnterior;

	private String typeMedia;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
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
			PayloadValoresResponse payloadValoresResponse = S.ValoresService
					.incluir(getValores());
			if (!UtilPayload.isOK(payloadValoresResponse)) {
				Alert.showMessage(payloadValoresResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadValoresResponse payloadValoresResponse = S.ValoresService
					.modificar(getValores());
			if (!UtilPayload.isOK(payloadValoresResponse)) {
				Alert.showMessage(payloadValoresResponse);
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

	public Valores getValores() {
		return (Valores) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate
					.validateString(getValores().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getValores().getFkOrganizacion(),
					"Organizaci�n");
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

	public String getUrlImagenAnterior() {
		return urlImagenAnterior;
	}

	public void setUrlImagenAnterior(String urlImagenAnterior) {
		this.urlImagenAnterior = urlImagenAnterior;
	}

	public String getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
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

			if (UtilMultimedia.validateImage(media.getName().substring(
					media.getName().lastIndexOf(".") + 1))) {
				this.nameImage = media.getName();
				this.extensionImage = nameImage.substring(this.nameImage
						.lastIndexOf(".") + 1);
				System.out.println(extensionImage);
				this.bytes = media.getByteData();

				this.urlImage = new StringBuilder()
						.append("/Smile/Directorio/d_").append(nameImage)
						.toString();
				this.typeMedia = media.getContentType();

			} else {
				this.getValores().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		} else {
			this.getValores().setFkMultimedia(null);
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

	private BufferedImage loadImage() throws Exception {
		try {
			Path path = Paths.get(this.getUrlImage());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImage()));
		} catch (Exception e) {
			return null;
		}
	}

}
