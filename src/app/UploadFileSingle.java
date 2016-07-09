package app;

import org.zkoss.zk.ui.event.UploadEvent;

public interface UploadFileSingle {
	
	public abstract void onUploadFileSingle(UploadEvent event, String idUpload);
	
	public abstract void onRemoveFileSingle(String idUpload);
}
