package ve.smile.web.viewmodels;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class VM_Navbar {
	
	@Init
	public void init(){
		DataCenter.updateSrcPageContent(null, null, "/views/web/main.zul");
	}
	
	@Command
	public void inicio() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/main.zul");
		
	}
	
	@Command
	public void galeria() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/galeria.zul");
		
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
	public void contactanos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/contactanos.zul");
		
	}
	
	@Command
	public void buzon() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/buzon.zul");
		
	}
	
	@Command
	public void noticias() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/noticias.zul");
		
	}
	
	@Command
	public void faq() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/faq.zul");
		
	}
}
