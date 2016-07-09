package app;

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

public class VMChangeImage extends VM_WindowDialog implements UploadImageSingle {
	
	private byte[] bytes = null;

	@Init(superclass=true)
    public void childInit_VMChangeImage() {
		//NOTHING OK!
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
		
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
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
			Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
			
			Path path = Paths.get("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/u_" + idUser);
			bytes = Files.readAllBytes(path);
			
			return ImageIO.read(new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/u_" + idUser));
		} catch (Exception e) {
			Path path = Paths.get("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/default");
			bytes = Files.readAllBytes(path);
			
			return ImageIO.read(new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/default"));
		}
	}
}
