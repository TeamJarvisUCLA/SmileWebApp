package ve.smile.administracion.portalweb.galeria.galeria;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.dto.Multimedia;

public class VM_formMultimediaAlbum extends VM_WindowDialog
		implements UploadImageSingle{

	private Multimedia multimedia = new Multimedia();
	
	private byte[] bytes = null;
	private String extensionImage;
	private String urlImage;
	
	private String typeMedia;
	
	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
		this.multimedia = (Multimedia) this.getDialogData().get("multimedia");
		System.out.println(this.multimedia);
		System.out.println(this.multimedia.getIdMultimedia());
		
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
		BindUtils.postGlobalCommand(null,null,"refreshMultimedias",null);
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
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
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
