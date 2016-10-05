package ve.smile.gestion.ayudas.egreso_beneficiario;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_EgresoBeneficiarioIndex extends VM_WindowSimpleListPrincipal<Beneficiario> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Beneficiario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadBeneficiarioResponse;
	}

	@Override
	public void doDelete() {
		PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
				.eliminar(getSelectedObject().getIdBeneficiario());

		Alert.showMessage(payloadBeneficiarioResponse);

		if (UtilPayload.isOK(payloadBeneficiarioResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop//gestion/ayudas/egresoBeneficiario/EgresoBeneficiarioFormBasic.zul";
	}

}
