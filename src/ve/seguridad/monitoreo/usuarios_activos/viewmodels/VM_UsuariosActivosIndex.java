package ve.seguridad.monitoreo.usuarios_activos.viewmodels;

import java.util.HashMap;

import karen.core.dialog.generic.data.DialogData;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.UtilDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.consume.services.S ;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.dto.Sesion;
import ve.smile.seguridad.payload.response.PayloadSesionResponse;

import org.zkoss.bind.annotation.Init;

public class VM_UsuariosActivosIndex extends VM_WindowSimpleListPrincipal<Sesion> {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<Sesion> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		HashMap<String, String> criterios = new HashMap<String, String>();
		
		criterios.put("estado", Sesion.ACTIVO);

		PayloadSesionResponse payloadSesion = 
				S.SesionService.consultarPaginacionCriteriosWithLastAuditoria(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);

		return payloadSesion;
	}

	@Override
	public void executeEliminar() {
		//NOTHING OK!
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return null;
	}

	@Override
	public void executeCustom1() {
		System.out.println("Custom 1");
	}
	
	@Override
	public void executeCustom2() {
		DialogData dialogData = new DialogData();
		dialogData.put("sesion", selectedObject);

		
		
//		dialogData.addDialogCloseListeners(new DialogCloseListener() {
//			
//			@Override
//			public void onClose(DialogCloseEvent arg0) {
//				
//			}
//		});
		
		UtilDialog.showDialog("views/seguridad/monitoreo/usuariosActivos/ultimasSesionesActivas.zul", dialogData);
	}
	
	@Override
	public void executeCustom3() {
		System.out.println("Custom 3");
	}

	@Override
	public String isValidPreconditionsCustom2() {
		if (selectedObject == null) {
			return "W:Warning Code: 105-Debe seleccionar un registro";
		}
		return "";
	}
	
	
}
