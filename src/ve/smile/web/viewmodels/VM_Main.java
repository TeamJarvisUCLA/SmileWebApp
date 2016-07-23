package ve.smile.web.viewmodels;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.Command;

public class VM_Main {
	
	@Command
	public void noticias() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/noticias.zul");
		
	}
	
	@Command
	public void conocenos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/conocenos.zul");
		
	}
	
	@Command
	public void eventos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/eventos.zul");
		
	}
	
	@Command
	public void participa() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/participa.zul");
		
	}
	
	@Command
	public void galeria() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/galeria.zul");
		
	}
	
	@Command
	public void detalleNoticia() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/detalleNoticia.zul");
		
	}
	
	@Command
	public void album() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/album.zul");
		
	}

	
	@Command
	public void calendario() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/calendario.zul");
		
	}

}
