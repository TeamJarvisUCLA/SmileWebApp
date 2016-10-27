package ve.smile.administracion.app_movil.buzon.viewmodels;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.dto.ContactoPortal;
import ve.smile.enums.EstatusContactoEnum;
import ve.smile.enums.ProcedenciaMensajeEnum;
import ve.smile.enums.TipoContactoPortalEnum;
import ve.smile.payload.response.PayloadContactoPortalResponse;
import ve.smile.seguridad.dto.Operacion;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.helper.OperacionHelper;

public class VM_BuzonMovilIndex extends
		VM_WindowSimpleListPrincipal<ContactoPortal> {

	@Init(superclass = true)
	public void childInit() {

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		if (operacionEnum.equals(OperacionEnum.CUSTOM1)) {
			return "views/desktop/administracion/appMovil/buzon/BuzonMovilFormBasic.zul";
		}
		return "";
	}

	@Override
	public IPayloadResponse<ContactoPortal> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		// criterios.put("tipoContactoPortal",
		// String.valueOf(TipoContactoPortalEnum.SUGERENCIA.ordinal()));
		criterios.put("estatusContacto",
				String.valueOf(EstatusContactoEnum.PENDIENTE.ordinal()));
		criterios.put("procedencia",
				String.valueOf(ProcedenciaMensajeEnum.MOVIL.ordinal()));
		PayloadContactoPortalResponse payloadContactoPortalResponse = S.ContactoPortalService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadContactoPortalResponse;
	}

	@Override
	public void executeCustom1() {
		Operacion operacion = OperacionHelper.getPorType(OperacionEnum.CUSTOM1);
		DataCenter.updateSrcPageContent(selectedObject, operacion,
				getSrcFileZulForm(OperacionEnum.CUSTOM1));
	}

	@Override
	public String isValidPreconditionsCustom1() {
		if (selectedObject == null) {
			return "I:Information Code: 107-Debe seleccionar un Registro";
		}
		return "";
	}

	@Override
	public void doDelete() {
		PayloadContactoPortalResponse payloadContactoPortalResponse = S.ContactoPortalService
				.eliminar(getSelectedObject().getIdContactoPortal());
		Alert.showMessage(payloadContactoPortalResponse);
		if (UtilPayload.isOK(payloadContactoPortalResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}
}
