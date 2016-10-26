package ve.smile.administracion.portalweb.correo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.enums.EstatusAlbumEnum;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;

public class VM_ValidarCorreoFormBasic extends VM_WindowForm {

	
	Configuracion configuracion=new Configuracion();
	private List<PropiedadEnum> PropiedadesEnums;
	private PropiedadEnum PropiedadEnum;
	private boolean valor = true;


	
	
	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	public boolean isValor() {
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}
	
	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			

				
			this.getConfiguracion().setPropiedad(PropiedadEnum.ordinal());
			if(this.configuracion.getValor().equals("true")){
				this.configuracion.setValor("true");
			}else if(this.configuracion.getValor().equals("false")){
				this.configuracion.setValor("false");
			}
			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.incluir(getConfiguracion());

			if (!UtilPayload.isOK(payloadConfiguracionResponse)) {
				Alert.showMessage(payloadConfiguracionResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.modificar(getConfiguracion());

			if (!UtilPayload.isOK(payloadConfiguracionResponse)) {
				Alert.showMessage(payloadConfiguracionResponse);
				return true;
			}

			Alert.hideMessage();
			DataCenter.reloadCurrentNodoMenu();

			return true;

		}
	
		return false;
	}
	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}
	
	
	
	
	
	
	public Configuracion getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}

	public List<PropiedadEnum> getPropiedadesEnums() {
		
		if (this.PropiedadesEnums == null) {
			PropiedadesEnums = new ArrayList<PropiedadEnum>();
		}
		if (this.PropiedadesEnums.isEmpty()) {
			for (PropiedadEnum propiedadEnum : PropiedadEnum.values()) {
				this.PropiedadesEnums.add(propiedadEnum);
			}
		}
		//Para que me muestre la lista de los enum pero los mayores a 4.
		
		for(Iterator<PropiedadEnum> i = this.PropiedadesEnums.iterator(); i.hasNext(); ) {
			PropiedadEnum item = i.next();
		    if  (item.ordinal() <4 ) {
		    	i.remove();
		    	
		    }
		}
			    
		return PropiedadesEnums;
	}

	public void setPropiedadesEnums(List<PropiedadEnum> PropiedadesEnums) {
		this.PropiedadesEnums = PropiedadesEnums;
	}

	public PropiedadEnum getPropiedadEnum() {
		return PropiedadEnum;
	}

	public void setTipoCarteleraEnum(PropiedadEnum PropiedadEnum) {
		this.PropiedadEnum = PropiedadEnum;
		this.getConfiguracion().setPropiedad(PropiedadEnum.ordinal());

	}


}
