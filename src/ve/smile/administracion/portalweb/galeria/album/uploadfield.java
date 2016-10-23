package ve.smile.administracion.portalweb.galeria.album;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import lights.smile.util.UtilMultimedia;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zk.ui.ext.*;
import org.zkoss.zk.au.*;
import org.zkoss.zk.au.out.*;
import org.zkoss.zul.*;
import org.zkoss.util.media.*;

public class uploadfield extends GenericForwardComposer{
	
	private List<Media> fotos = new ArrayList<Media>();

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

	}
  
  	public void onUpload$btn(UploadEvent e)// throws InterruptedException
        {
  		org.zkoss.util.media.Media media = e.getMedia();
  		
  		
  		
		if (media instanceof org.zkoss.image.Image) {

			if (UtilMultimedia.validateImage(media.getName().substring(
					media.getName().lastIndexOf(".") + 1))) {

			} else {
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		} else {
			Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
		}
  		
  		
		if (e.getMedias() != null)
                {
			StringBuilder sb = new StringBuilder("tu carga: \n");
			

                  	for (Media m : e.getMedias())
                        {
	                  		this.fotos.add(m);
	          				sb.append(m.getName());
	                      	sb.append(" (");
	                        sb.append(m.getContentType());
	                    	sb.append(")\n");
                        }
                  
			Messagebox.show(sb.toString());
                }
          	else
                {
			Messagebox.show("You uploaded no files!");
                }
        }

	public List<Media> getFotos() {
		return fotos;
	}
}
