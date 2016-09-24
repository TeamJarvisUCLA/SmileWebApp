package ve.smile.gestion.voluntariado.maestro.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Voluntario;
import ve.smile.dto.Ciudad;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_VoluntarioFormBasic extends VM_WindowForm {

	private List<Ciudad> ciudades;

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	public List<Ciudad> getCiudades()
	{
		if (this.ciudades == null)
		{
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty())
		{
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum)
	{
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		if (operacionEnum.equals(OperacionEnum.CONSULTAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if (!isFormValidated())
		{
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.incluir(getVoluntario());
			if (!UtilPayload.isOK(payloadVoluntarioResponse))
			{
				Alert.showMessage(payloadVoluntarioResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(getVoluntario());
			if (!UtilPayload.isOK(payloadVoluntarioResponse))
			{
				Alert.showMessage(payloadVoluntarioResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum)
	{
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum)
	{
		return actionSalir(operacionEnum);
	}

	public Voluntario getVoluntario()
	{
		return (Voluntario) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateString(getVoluntario().getFkPersona().getIdentificacion(), "Cédula", 35);
			UtilValidate.validateString(getVoluntario().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getVoluntario().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateNull(getVoluntario().getFkPersona().getFkCiudad().getIdCiudad(), "Ciudad");
			UtilValidate.validateNull(getVoluntario().getFkPersona().getFkMultimedia().getIdMultimedia(), "Imagen");
			// UtilValidate.validateInteger(getVoluntario().getFkPersona().getSexo(), "Sexo", OPERATOR , 3);
			// UtilValidate.validateDate(getVoluntario().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", validateOperator, date_8601, formatToShow);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTelefono1(), "Teléfono 1", 25);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTelefono2(), "Teléfono 2", 25);
			UtilValidate.validateString(getVoluntario().getFkPersona().getDireccion(), "Direccion", 250);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTwitter(), "Twitter", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getInstagram(), "Instagram", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getLinkedin(), "LinkedIn", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getSitioWeb(), "Sitio web", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getCorreo(), "Correo", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getFacebook(), "Facebook", 100);
			// UtilValidate.validateInteger(getVoluntario().getFkPersona().getTipoPersona(), "Tipo de persona", 100);
			// UtilValidate.validateDate(getVoluntario().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
			// UtilValidate.validateDate(getVoluntario().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
