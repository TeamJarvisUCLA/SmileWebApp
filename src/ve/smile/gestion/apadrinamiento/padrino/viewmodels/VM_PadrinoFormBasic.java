package ve.smile.gestion.apadrinamiento.padrino.viewmodels;

import java.util.ArrayList;
import java.util.List;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Padrino;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;


public class VM_PadrinoFormBasic extends VM_WindowForm {
	
	private List<Ciudad> ciudades;
	
	public void childInit()
	{
		
	}
	
	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty()) {
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
		}
		return ciudades;
	}
	
	public void setCiudades (List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.incluir(getPadrino());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				Alert.showMessage(payloadPadrinoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService.modificar(getPadrino());
			if (!UtilPayload.isOK(payloadPadrinoResponse)) {
				Alert.showMessage(payloadPadrinoResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}
	
	private boolean isFormValidated() {
		try
		{
			UtilValidate.validateString(getPadrino().getFkPersona().getIdentificacion(), "C�dula", 35);
			UtilValidate.validateString(getPadrino().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getPadrino().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateNull(getPadrino().getFkPersona().getFkCiudad().getIdCiudad(), "Ciudad");
			UtilValidate.validateNull(getPadrino().getFkPersona().getFkMultimedia().getIdMultimedia(), "Imagen");
			// UtilValidate.validateInteger(getPadrino().getFkPersona().getSexo(), "Sexo", OPERATOR , 3);
			// UtilValidate.validateDate(getPadrino().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", validateOperator, date_8601, formatToShow);
			UtilValidate.validateString(getPadrino().getFkPersona().getTelefono1(), "Tel�fono 1", 25);
			UtilValidate.validateString(getPadrino().getFkPersona().getTelefono2(), "Tel�fono 2", 25);
			UtilValidate.validateString(getPadrino().getFkPersona().getDireccion(), "Direccion", 250);
			UtilValidate.validateString(getPadrino().getFkPersona().getTwitter(), "Twitter", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getInstagram(), "Instagram", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getLinkedin(), "LinkedIn", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getSitioWeb(), "Sitio web", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getCorreo(), "Correo", 100);
			UtilValidate.validateString(getPadrino().getFkPersona().getFacebook(), "Facebook", 100);
			// UtilValidate.validateInteger(getPadrino().getFkPersona().getTipoPersona(), "Tipo de persona", 100);
			// UtilValidate.validateDate(getPadrino().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
			// UtilValidate.validateDate(getPadrino().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

	public Padrino getPadrino() {
		
		return (Padrino) DataCenter.getEntity();
	}

}
