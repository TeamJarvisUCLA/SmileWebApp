package ve.smile.gestion.donativo.colaborador.viewmodels;

//import java.util.HashMap;
//import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.listfoot.enums.HowToSeeEnum;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Colaborador;
import ve.smile.enums.EstatusColaboradorEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ColaboradorIndex extends
		VM_WindowSimpleListPrincipal<Colaborador> {
	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<EstatusColaboradorEnum> estatusColaboradorEnums;
	private EstatusColaboradorEnum estatusColaboradorEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Colaborador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		if (this.getTipoPersonaEnum() != null) {
			criterios.put("fkPersona.tipoPersona",
					String.valueOf(this.tipoPersonaEnum.ordinal()));
		}
		if (this.getEstatusColaboradorEnum() != null) {
			criterios.put("estatusColaborador",
					String.valueOf(this.getEstatusColaboradorEnum().ordinal()));
		}
		PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadColaboradorResponse;
	}

	@Override
	public void doDelete() {
		PayloadColaboradorResponse payloadPatrocinadorResponse = S.ColaboradorService
				.eliminar(getSelectedObject().getIdColaborador());
		Alert.showMessage(payloadPatrocinadorResponse);
		if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/donativo/colaborador/ColaboradorFormBasic.zul";
	}

	@Command
	public void changeFilter() {
		super.updateListBox(1, HowToSeeEnum.NORMAL);
	}

	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums() {
		if (this.tipoPersonaEnums == null) {
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty()) {
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values()) {
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums) {
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

	public List<EstatusColaboradorEnum> getEstatusColaboradorEnums() {
		if (this.estatusColaboradorEnums == null) {
			this.estatusColaboradorEnums = new ArrayList<>();
		}
		if (this.estatusColaboradorEnums.isEmpty()) {
			for (EstatusColaboradorEnum estatusColaboradorEnum : EstatusColaboradorEnum
					.values()) {
				this.estatusColaboradorEnums.add(estatusColaboradorEnum);
			}
		}
		return estatusColaboradorEnums;
	}

	public void setEstatusColaboradorEnums(
			List<EstatusColaboradorEnum> estatusColaboradorEnums) {
		this.estatusColaboradorEnums = estatusColaboradorEnums;
	}

	public EstatusColaboradorEnum getEstatusColaboradorEnum() {
		return estatusColaboradorEnum;
	}

	public void setEstatusColaboradorEnum(
			EstatusColaboradorEnum estatusColaboradorEnum) {
		this.estatusColaboradorEnum = estatusColaboradorEnum;
	}

}
