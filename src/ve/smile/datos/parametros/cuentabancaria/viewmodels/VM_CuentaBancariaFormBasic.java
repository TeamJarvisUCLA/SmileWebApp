package ve.smile.datos.parametros.cuentabancaria.viewmodels;

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
import ve.smile.dto.Banco;
import ve.smile.dto.CuentaBancaria;
import ve.smile.enums.PropietarioEnum;
import ve.smile.payload.response.PayloadBancoResponse;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CuentaBancariaFormBasic extends VM_WindowForm {

	private List<Banco> bancos;
	private List<PropietarioEnum> tipoCuentaEnums;
	private PropietarioEnum tipoCuentaEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	public PropietarioEnum getTipoCuentaEnum()
	{
		return tipoCuentaEnum;
	}

	public void setTipoCuentaEnum(PropietarioEnum tipoCuentaEnum)
	{
		this.tipoCuentaEnum = tipoCuentaEnum;
		this.getCuentaBancaria().setPropietario(tipoCuentaEnum.ordinal());
	}

	public List<PropietarioEnum> getTipoCuentaEnums()
	{
		if (this.tipoCuentaEnums == null)
		{
			this.tipoCuentaEnums = new ArrayList<>();
		}
		if (this.tipoCuentaEnums.isEmpty())
		{
			for (PropietarioEnum tipoCuentaEnum : PropietarioEnum.values())
			{
				this.tipoCuentaEnums.add(tipoCuentaEnum);
			}
		}
		return tipoCuentaEnums;
	}

	public void setTipoCuentaEnums(List<PropietarioEnum> tipoCuentaEnums)
	{
		this.tipoCuentaEnums = tipoCuentaEnums;
	}
	
	public List<Banco> getBancos()
	{
		if (this.bancos == null)
		{
			this.bancos = new ArrayList<>();
		}
		if (this.bancos.isEmpty())
		{
			PayloadBancoResponse payloadBancoResponse = S.BancoService.consultarTodos();
			if (!UtilPayload.isOK(payloadBancoResponse))
			{
				Alert.showMessage(payloadBancoResponse);
			}
			this.bancos.addAll(payloadBancoResponse.getObjetos());
		}
		return bancos;
	}

	public void setBancos(List<Banco> bancos)
	{
		this.bancos = bancos;
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
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
			PayloadCuentaBancariaResponse payloadCuentaBancariaResponse = S.CuentaBancariaService.incluir(getCuentaBancaria());
			if (!UtilPayload.isOK(payloadCuentaBancariaResponse))
			{
				Alert.showMessage(payloadCuentaBancariaResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadCuentaBancariaResponse payloadCuentaBancariaResponse = S.CuentaBancariaService.modificar(getCuentaBancaria());
			if (!UtilPayload.isOK(payloadCuentaBancariaResponse))
			{
				Alert.showMessage(payloadCuentaBancariaResponse);
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

	public CuentaBancaria getCuentaBancaria()
	{
		return (CuentaBancaria) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateNull(getCuentaBancaria().getFkBanco(), "Banco");
			UtilValidate.validateString(getCuentaBancaria().getCuentaBancaria(), "Cuenta bancaria", 40);
			UtilValidate.validateString(getCuentaBancaria().getTitular(), "Titular", 150);
			UtilValidate.validateString(getCuentaBancaria().getIdentificacionTitular(), "Cédula de identidad o RIF", 35);
			UtilValidate.validateString(getCuentaBancaria().getCorreoTitular(), "Correo electrónico", 35);
			UtilValidate.validateNull(getCuentaBancaria().getTipoCuenta(), "Tipo de cuenta");
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
