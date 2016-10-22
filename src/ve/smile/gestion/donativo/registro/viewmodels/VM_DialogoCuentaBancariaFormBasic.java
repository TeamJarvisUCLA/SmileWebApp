package ve.smile.gestion.donativo.registro.viewmodels;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import karen.core.crux.alert.Alert;
import karen.core.dialog.form.data.ToCloseWindowFormDialog;
import karen.core.dialog.form.viewmodels.VM_WindowFormDialog;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Banco;
import ve.smile.dto.CuentaBancaria;
import ve.smile.enums.PropietarioEnum;
import ve.smile.payload.response.PayloadBancoResponse;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_DialogoCuentaBancariaFormBasic extends
		VM_WindowFormDialog<CuentaBancaria> {

	private CuentaBancaria cuentaBancaria = new CuentaBancaria();
	private List<Banco> bancos;
	
	@Init(superclass = true)
	public void childInit() {

	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;
	}
	
	

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public ToCloseWindowFormDialog<CuentaBancaria> actionGuardar(OperacionEnum operacionEnum) {
		

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			 cuentaBancaria.setPropietario(PropietarioEnum.ORGANIZACION.ordinal());
			PayloadCuentaBancariaResponse cuentaBancariaResponse = S.CuentaBancariaService.incluir(cuentaBancaria);

			return new ToCloseWindowFormDialog<CuentaBancaria>(true, cuentaBancaria, operacionEnum);
		}

		return new ToCloseWindowFormDialog<CuentaBancaria>(false, operacionEnum);
	}
	
	@Override
	public ToCloseWindowFormDialog<CuentaBancaria> actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}
	
	@Override
	public ToCloseWindowFormDialog<CuentaBancaria> actionSalir(OperacionEnum operacionEnum) {
		return new ToCloseWindowFormDialog<CuentaBancaria>(true, operacionEnum);
	}

	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public List<Banco> getBancos() {
		if (this.bancos == null) {
			this.bancos = new ArrayList<>();
		}
		if (this.bancos.isEmpty()) {
			PayloadBancoResponse payloadBancoResponse = S.BancoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadBancoResponse)) {
				Alert.showMessage(payloadBancoResponse);
			}
			this.bancos.addAll(payloadBancoResponse.getObjetos());
		}
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}


	
	

}
