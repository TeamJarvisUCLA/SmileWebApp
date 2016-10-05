package ve.smile.web.conocenos.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.annotation.Command;

import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;
import ve.smile.dto.Valores;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.payload.response.PayloadValoresResponse;


public class VM_Conocenos {
	
	private List<Organizacion> organizacion;
	private List<Valores> valores;
	
	public List<Organizacion> getOrganizacion(){
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
	
	public List<Valores> getvalores(){
		if (this.valores == null) {
			this.valores = new ArrayList<>();
		}
		if (this.valores.isEmpty()) {
// uso de servicio consultaCarteleraPorParametro en CarteleraService, aqui se instancian los objetos 
// devueltos por consultaCarteleraPorParametro como una variable de tipo PayloadCarteleraResponse			
			PayloadValoresResponse payloadValoresResponse = S.ValoresService.consultarAllValores();
// agregar en lista de noticias todos los objetos contenidos en la variable payloadCarteleraResponse 			
			this.valores.addAll(payloadValoresResponse.getObjetos());
		}
// retornar lista
		return valores;
	}
	
	@Command
	public void conocenos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/conocenos.zul");
		
	}
	
}
