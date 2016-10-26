package ve.smile.administracion.portalweb.correo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
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
import karen.core.util.validate.UtilValidate;

public class VM_ValidarCorreoFormBasic extends VM_WindowForm {

	private List<Configuracion> Configuraciones = new ArrayList<Configuracion>();
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
			
			if(this.isValor())
			{
				this.configuracion.setValor("True");
			}else{
				this.configuracion.setValor("False");
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
			if(this.isValor())
			{
				this.configuracion.setValor("true");
			}else{
				this.configuracion.setValor("false");
			}
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
	
	public boolean isFormValidated() {
		try {
			UtilValidate.validateNull(getConfiguracion().getPropiedad(), "Formulario");
			UtilValidate.validateString(getConfiguracion().getDescripcion(), "Descripcion", 100);
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
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
		this.configuracion = (Configuracion) DataCenter.getEntity();
		if(this.configuracion.getIdConfiguracion() != null){
			if(this.configuracion.getValor().equals("true")){
				this.valor = true;
			}else{
				this.valor = false;
			}
		}
			
			
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
		
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarTodos();

		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			this.Configuraciones.addAll(payloadConfiguracionResponse
					.getObjetos());
		}
		
		for(Iterator<Configuracion> c = this.Configuraciones.iterator(); c.hasNext(); ) {
			Configuracion conf = c.next();
			Integer propiedad = conf.getPropiedad();
			for(Iterator<PropiedadEnum> i = this.PropiedadesEnums.iterator(); i.hasNext(); ) {
				PropiedadEnum item = i.next();
				if(getConfiguracion().getIdConfiguracion() != null){
					if(item.ordinal() != getConfiguracion().getPropiedad()){
						i.remove();
					}
				}else{
				    if  (item.ordinal() <4 || item.ordinal() == propiedad) {
				    	i.remove();
				    }
				}
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

	public void setPropiedadEnum(PropiedadEnum PropiedadEnum) {
		this.PropiedadEnum = PropiedadEnum;
		this.getConfiguracion().setPropiedad(PropiedadEnum.ordinal());
	}

}
