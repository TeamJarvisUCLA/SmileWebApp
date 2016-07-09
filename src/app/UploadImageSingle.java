package app;

import java.awt.image.BufferedImage;

import org.zkoss.zk.ui.event.UploadEvent;

public interface UploadImageSingle {
	
	public abstract BufferedImage getImageContent();
	
	public abstract void onUploadImageSingle(UploadEvent event, String idUpload);
	
	public abstract void onRemoveImageSingle(String idUpload);
}
