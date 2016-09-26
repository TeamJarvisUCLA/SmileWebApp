package ve.smile.web.conocenos.viewmodels;

import java.util.ArrayList;
import java.util.List;
import karen.core.crux.session.DataCenter;
import org.zkoss.bind.annotation.Command;
import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadOrganizacionResponse;


public class VM_Conocenos {
	
	private List<Organizacion> organizacion;
	
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
	
	@Command
	public void conocenos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/conocenos.zul");
		
	}
	
}
