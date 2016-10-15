package ve.smile.gestion.donativo.registro.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import karen.core.listfoot.enums.HowToSeeEnum;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.CuentaBancaria;
import ve.smile.enums.PropietarioEnum;
import ve.smile.payload.response.PayloadCuentaBancariaResponse;

public class VM_CatalogoCuentaBancaria extends
		VM_ListPaginationCatalogueDialog<CuentaBancaria> {

	private String titular;
	private String banco;
	private String cuenta;
	private String tipoCuenta;

	private PropietarioEnum propietarioEnum;

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
		this.propietarioEnum = (PropietarioEnum) this.getDialogData().get(
				"PropietarioEnum");
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<CuentaBancaria> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		if (titular != null && !titular.equalsIgnoreCase("")) {
			criterios.put("titular", titular);
		}
		if (banco != null && !banco.equalsIgnoreCase("")) {
			criterios.put("banco", banco);
		}
		if (cuenta != null && !cuenta.equalsIgnoreCase("")) {
			criterios.put("cuenta", cuenta);
		}
		if (tipoCuenta != null && !tipoCuenta.equalsIgnoreCase("")) {
			criterios.put("tipoCuenta", tipoCuenta);
		}
		if (propietarioEnum != null) {
			criterios.put("propietario",
					String.valueOf(propietarioEnum.ordinal()));
		}
		PayloadCuentaBancariaResponse payloadCuentaBancariaResponse = S.CuentaBancariaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.ILIKE, criterios);
		return payloadCuentaBancariaResponse;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	@Command
	public void changeFilter() {
		super.updateListBox(1, HowToSeeEnum.NORMAL);
	}
}
