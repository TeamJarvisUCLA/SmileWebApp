package ve.smile.gestion.evento.planificaion.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import app.UploadImageSingle;

public class VMCargarImagen extends VM_WindowDialog implements
		UploadImageSingle {
	
	private byte[] bytes = null;

	@Init(superclass=true)
    public void childInit_VMCargarImagen() {
		//NOTHING OK!
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
		}

	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;

	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	private BufferedImage loadImage() throws Exception {
		try {
			Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
			
			Path path = Paths.get("/home/conamerica97/eclipseKepler/imagen/u_" + idUser);
			bytes = Files.readAllBytes(path);
			
			return ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagene/u_" + idUser));
		} catch (Exception e) {
			Path path = Paths.get("/home/conamerica97/eclipseKepler/imagene/default");
			bytes = Files.readAllBytes(path);
			
			return ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagen/default"));
		}
	}
	

}
