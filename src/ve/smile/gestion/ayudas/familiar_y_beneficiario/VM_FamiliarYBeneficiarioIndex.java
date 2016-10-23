package ve.smile.gestion.ayudas.familiar_y_beneficiario;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Familiar;
import ve.smile.enums.EstatusFamiliarEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_FamiliarYBeneficiarioIndex extends VM_WindowSimpleListPrincipal<Familiar> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Familiar> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("estatusFamiliar",
				String.valueOf(EstatusFamiliarEnum.ACTIVO.ordinal()));
		PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		/*Map<String, String> criterios = new HashMap<String, String>();
		criterios.put("estatus",
		String.valueOf(EstatusSolicitudEnum.EN_PROCESO.ordinal()));

		PayloadFamiliarResponse payloadFamiliarResponse = 

		S.FamiliarService.consultarCriterios(TypeQuery.EQUAL, criterios);*/

		/*PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
*/
		return payloadFamiliarResponse;
	}

	@Override
	public void doDelete() {
		PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.eliminar(getSelectedObject().getIdFamiliar());

		Alert.showMessage(payloadFamiliarResponse);

		if (UtilPayload.isOK(payloadFamiliarResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/ayudas/familiarYBeneficiario/FamiliarYBeneficiarioFormBasic.zul";
	}

}
