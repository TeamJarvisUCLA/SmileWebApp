package ve.smile.datos.parametros.parentesco.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Parentesco;
import ve.smile.payload.response.PayloadBeneficiarioFamiliarResponse;
import ve.smile.payload.response.PayloadParentescoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ParentescoIndex extends
VM_WindowSimpleListPrincipal<Parentesco> {

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Parentesco> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadParentescoResponse payloadParentescoResponse = S.ParentescoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);

		return payloadParentescoResponse;
	}

	@Override
	public void doDelete() {
		PayloadParentescoResponse payloadParentescoResponse = S.ParentescoService
				.eliminar(getSelectedObject().getIdParentesco());

		Alert.showMessage(payloadParentescoResponse);

		if (UtilPayload.isOK(payloadParentescoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/datos/parametros/parentesco/ParentescoFormBasic.zul";
	}

	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Parentesco";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkParentesco.idParentesco", String.valueOf(getSelectedObject().getIdParentesco()));

		//Table Relation BeneficiarioFamiliar
		PayloadBeneficiarioFamiliarResponse payloadBeneficiarioFamiliarResponse =
				S.BeneficiarioFamiliarService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadBeneficiarioFamiliarResponse)) {
			return String.valueOf(payloadBeneficiarioFamiliarResponse.getInformacion(IPayloadResponse.MENSAJE));
		}

		Integer countFamiliares = 
				Double.valueOf(String.valueOf(payloadBeneficiarioFamiliarResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countFamiliares > 0) {
			return "E:Error 0:No se puede eliminar el <b>Parentesco</b> seleccionado ya que está siendo utilizado por " + countFamiliares + " Familiares de Beneficiarios";
		}

		return "";
	}

}
