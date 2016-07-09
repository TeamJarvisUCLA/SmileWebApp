package lights.seguridad.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import karen.core.listfoot.enums.HowToSeeEnum;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.seguridad.controllers.C_UltimasSesionesActivas;
import lights.seguridad.dto.Sesion;
import lights.seguridad.payload.response.PayloadSesionResponse;
import lights.smile.consume.services.S;

public class VMUltimasSesionesActivas extends VM_WindowDialog {

	private List<Sesion> listaSesiones;
	private Sesion sesionSeleccionada = null;
	
	@Init(superclass=true)
    public void childInit_VMUltimasSesionesActivas() {
		//NOTHING OK!
    }

	@Override
	public void afterChildInit() {
		//NOTHING OK!
	}
	
	public PayloadSesionResponse getDataToTable(Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		
		criterios.put("fkUsuario.correo", ((Sesion)getDialogData().get("sesion")).getFkUsuario().getCorreo());
		
		return S.SesionService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios, "idSesion=true");
		
	}
	
	public PayloadSesionResponse updateListBox(Integer page, HowToSeeEnum howToSeeEnum) {
		PayloadSesionResponse iPayload = getDataToTable(C_UltimasSesionesActivas.CANTIDAD_REGISTROS_PAGINA_DEFECTO, Math.abs(page));
		
		if (!UtilPayload.isOK(iPayload)) {
			return iPayload;
		}
		
		if (howToSeeEnum.equals(HowToSeeEnum.NORMAL)) {
			listaSesiones = iPayload.getObjetos();
			
			sesionSeleccionada = null;
		} else {//EXTENDED
			if (listaSesiones == null) {
				listaSesiones = new ArrayList<Sesion>();
			}
			
			if (page == 1) {
				listaSesiones = iPayload.getObjetos();
				
				sesionSeleccionada = null;
			} else {
				listaSesiones.addAll(iPayload.getObjetos());
			}
		}
		
		BindUtils.postNotifyChange(null, null, this, "listaSesiones");
		
		return iPayload;
	}
	
	public String getCorreo() {
		return ((Sesion)getDialogData().get("sesion")).getFkUsuario().getCorreo();
	}

	public List<Sesion> getListaSesiones() {
		return listaSesiones;
	}

	public void setListaSesiones(List<Sesion> listaSesiones) {
		this.listaSesiones = listaSesiones;
	}

	public Sesion getSesionSeleccionada() {
		return sesionSeleccionada;
	}

	public void setSesionSeleccionada(Sesion sesionSeleccionada) {
		this.sesionSeleccionada = sesionSeleccionada;
	}
}
