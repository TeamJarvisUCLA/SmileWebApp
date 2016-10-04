package ve.smile.gestion.donativo.colaborador.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.dto.Colaborador;

public class VM_ColaboradorFormBasic extends VM_WindowForm {

	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

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

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadColaboradorResponse payloadColaboradorResponse =
					S.ColaboradorService.incluir(getColaborador());

			if(!UtilPayload.isOK(payloadColaboradorResponse)) {
				Alert.showMessage(payloadColaboradorResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadColaboradorResponse payloadColaboradorResponse =
					S.ColaboradorService.modificar(getColaborador());

			if(!UtilPayload.isOK(payloadColaboradorResponse)) {
				Alert.showMessage(payloadColaboradorResponse);
				return true;
			}

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

	public Colaborador getColaborador() {
		return (Colaborador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try
		{
			UtilValidate.validateString(getColaborador().getFkPersona().getIdentificacion(), "Cédula", 35);
			UtilValidate.validateString(getColaborador().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getColaborador().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateNull(getColaborador().getFkPersona().getFkCiudad().getIdCiudad(), "Ciudad");
			UtilValidate.validateNull(getColaborador().getFkPersona().getFkMultimedia().getIdMultimedia(), "Imagen");
			// UtilValidate.validateInteger(getColaborador().getFkPersona().getSexo(), "Sexo", OPERATOR , 3);
			// UtilValidate.validateDate(getColaborador().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", validateOperator, date_8601, formatToShow);
			UtilValidate.validateString(getColaborador().getFkPersona().getTelefono1(), "Teléfono 1", 25);
			UtilValidate.validateString(getColaborador().getFkPersona().getTelefono2(), "Teléfono 2", 25);
			UtilValidate.validateString(getColaborador().getFkPersona().getDireccion(), "Direccion", 250);
			UtilValidate.validateString(getColaborador().getFkPersona().getTwitter(), "Twitter", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getInstagram(), "Instagram", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getLinkedin(), "LinkedIn", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getSitioWeb(), "Sitio web", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getCorreo(), "Correo", 100);
			UtilValidate.validateString(getColaborador().getFkPersona().getFacebook(), "Facebook", 100);
			// UtilValidate.validateInteger(getColaborador().getFkPersona().getTipoPersona(), "Tipo de persona", 100);
			// UtilValidate.validateDate(getColaborador().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
			// UtilValidate.validateDate(getColaborador().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
}
