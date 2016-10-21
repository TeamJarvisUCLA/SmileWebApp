package ve.smile.administracion.organizacion.viewmodels;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Organizacion;

import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_OrganizacionIndex extends VM_WindowSimpleListPrincipal<Organizacion>{
	
	
	
	
	@Init(superclass = true)
	public void childInit() {
		PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService.consultarTodos();
		System.out.println(selectedObject);
		setSelectedObject(payloadOrganizacionResponse.getObjetos().get(0));
		
	}



	@Override
	public IPayloadResponse<Organizacion> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		System.out.println(payloadOrganizacionResponse.getObjetos().get(0));
		return payloadOrganizacionResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/administracion/organizacion/OrganizacionFormBasic.zul";
	}

	@Override
	public void doDelete() {
		PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService.eliminar(getSelectedObject().getIdOrganizacion());
		Alert.showMessage(payloadOrganizacionResponse);
		if (UtilPayload.isOK(payloadOrganizacionResponse)){
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
	

	@Override
	public void setSelectedObject(Organizacion selectedObject) {
		// TODO Auto-generated method stub
		super.setSelectedObject(selectedObject);
	}

	@Override
	public Organizacion getSelectedObject() {
		// TODO Auto-generated method stub
		return super.getSelectedObject();
	}
	
}
