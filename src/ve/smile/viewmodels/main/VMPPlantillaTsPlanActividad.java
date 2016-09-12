package ve.smile.viewmodels.main;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S ;
import ve.smile.dto.PlantillaTsPlanActividad;
import ve.smile.payload.response.PayloadPlantillaTsPlanActividadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

import org.zkoss.bind.annotation.Init;

public class VMPPlantillaTsPlanActividad extends VM_WindowSimpleListPrincipal<PlantillaTsPlanActividad> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<PlantillaTsPlanActividad> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadPlantillaTsPlanActividadResponse payloadPlantillaTsPlanActividadResponse = 
				S.PlantillaTsPlanActividadService.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadPlantillaTsPlanActividadResponse;
	}

	@Override
	public void doDelete() {
		PayloadPlantillaTsPlanActividadResponse payloadPlantillaTsPlanActividadResponse =
				S.PlantillaTsPlanActividadService.eliminar(getSelectedObject().getIdPlantillaTsPlanActividad());

		Alert.showMessage(payloadPlantillaTsPlanActividadResponse);

		if (UtilPayload.isOK(payloadPlantillaTsPlanActividadResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}




}
