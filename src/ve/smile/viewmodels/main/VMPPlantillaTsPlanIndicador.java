package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.dto.PlantillaTsPlanIndicador;
import ve.smile.payload.response.PayloadPlantillaTsPlanIndicadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

import org.zkoss.bind.annotation.Init;

public class VMPPlantillaTsPlanIndicador extends VM_WindowSimpleListPrincipal<PlantillaTsPlanIndicador> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<PlantillaTsPlanIndicador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPlantillaTsPlanIndicadorResponse payloadPlantillaTsPlanIndicadorResponse = 
				S.PlantillaTsPlanIndicadorService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadPlantillaTsPlanIndicadorResponse;
	}

	@Override
	public void doDelete() {
		PayloadPlantillaTsPlanIndicadorResponse payloadPlantillaTsPlanIndicadorResponse =
				S.PlantillaTsPlanIndicadorService.eliminar(getSelectedObject().getIdPlantillaTsPlanIndicador());

		Alert.showMessage(payloadPlantillaTsPlanIndicadorResponse);

		if (UtilPayload.isOK(payloadPlantillaTsPlanIndicadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

}
