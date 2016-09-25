package ve.smile.web.viewmodels.footer;

import java.util.ArrayList;
import java.util.List;

import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadOrganizacionResponse;

public class VM_Footer {
	
	private List<Organizacion> organizacion;
	
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
