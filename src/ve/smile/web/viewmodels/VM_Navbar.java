package ve.smile.web.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadOrganizacionResponse;

public class VM_Navbar {
	
	private List<Organizacion> organizacion;

	
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
	
	@Command
	public void app() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/descargaAppMovil.zul");
		
	}
	
	@Command
	public void ayuda() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/faq.zul");
		
	}
	
	public List<Organizacion> getorganizacion(){
		if (this.organizacion == null) {
			this.organizacion = new ArrayList<>();
		}
		if (this.organizacion.isEmpty()) {
			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService
					.buscarOrganizacion();
			
			this.organizacion.addAll(payloadOrganizacionResponse.getObjetos());
		}

		return organizacion;
	}
}
