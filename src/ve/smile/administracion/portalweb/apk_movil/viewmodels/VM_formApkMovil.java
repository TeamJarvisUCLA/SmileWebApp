package ve.smile.administracion.portalweb.apk_movil.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.dto.Multimedia;

public class VM_formApkMovil extends VM_WindowDialog {

	private Multimedia multimedia = new Multimedia();

	private byte[] bytes = null;
	private String extensionImage;
	private String urlImage;

	private Media media;
	private String typeMedia;

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
		this.multimedia = (Multimedia) this.getDialogData().get("multimedia");

	}

	public Multimedia getMultimedia() {
		return multimedia;
	}

	public void setMultimedia(Multimedia multimedia) {
		this.multimedia = multimedia;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}

	public void limpiar() {
		this.setMultimedia(new Multimedia());
		BindUtils.postNotifyChange(null, null, this, "multimedia");
		BindUtils.postGlobalCommand(null, null, "refreshMultimedias", null);
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

	@Command
	public void onUploadFile(
			@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {

		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null) {
			media = upEvent.getMedia();

			this.extensionImage = media.getName().substring(
					media.getName().lastIndexOf(".") + 1);
			this.bytes = media.getByteData();
			this.typeMedia = media.getContentType();

			// try {
			// Files.copy(new File(Zki.getPath() + media.getName()),
			// media.getStreamData());
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// fileuploaded = true;
			// nombre = media.getName();
		}
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
