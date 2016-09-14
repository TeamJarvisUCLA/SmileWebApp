package ve.smile.datos.configuracion;

import java.util.Map;
import java.util.HashMap;
import karen.core.crux.alert.Alert;
import ve.smile.consume.services.S ;
import karen.core.util.payload.UtilPayload;
import ve.smile.seguridad.enums.OperacionEnum;
import lights.core.payload.response.IPayloadResponse;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;

import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;

import org.zkoss.bind.annotation.Init;

public class VMPPlantillaTrabajoSocial extends VM_WindowSimpleListPrincipal<TrabajoSocial> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajoSocialResponse;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/configuracion/plantillaTrabajoSocial/PlantillaTrabajoSocialFormBasic.zul";
	}

}
