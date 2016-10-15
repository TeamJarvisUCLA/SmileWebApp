package ve.smile.gestion.apadrinamiento.padrino.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.listfoot.enums.HowToSeeEnum;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_PadrinoIndex extends VM_WindowSimpleListPrincipal<Padrino> {

	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<EstatusPadrinoEnum> estatusPadrinoEnums;
	private EstatusPadrinoEnum estatusPadrinoEnum;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		estatusPadrinoEnum = EstatusPadrinoEnum.ACTIVO;
	}

	public void doDelete() {
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.eliminar(getSelectedObject().getIdPadrino());
		Alert.showMessage(payloadPadrinoResponse);
		if (UtilPayload.isOK(payloadPadrinoResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/apadrinamiento/padrino/PadrinoFormBasic.zul";
	}

	@Override
	public IPayloadResponse<Padrino> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		if (this.getTipoPersonaEnum() != null) {
			criterios.put("fkPersona.tipoPersona",
					String.valueOf(this.tipoPersonaEnum.ordinal()));
		}
		if (this.getEstatusPadrinoEnum() != null) {
			criterios.put("estatusPadrino",
					String.valueOf(this.estatusPadrinoEnum.ordinal()));
		}
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadPadrinoResponse;
	}

	@Override
	public String isValidSearchDataModificar() {
		if (selectedObject.getEstatusPadrinoEnum().equals(
				EstatusPadrinoEnum.POSTULADO)
				|| selectedObject.getEstatusPadrinoEnum().equals(
						EstatusPadrinoEnum.INACTIVO)
				|| selectedObject.getEstatusPadrinoEnum().equals(
						EstatusPadrinoEnum.RECHAZADO)) {
			Alert.showMessage("E: Error Code: 100-El padrino seleccionado no puede ser modicado su estatus es <b> "
					+ selectedObject.getEstatusPadrinoEnum().toString()
					+ "</b>");
		}
		return super.isValidSearchDataModificar();
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

	public List<EstatusPadrinoEnum> getEstatusPadrinoEnums() {
		if (this.estatusPadrinoEnums == null) {
			this.estatusPadrinoEnums = new ArrayList<>();
		}
		if (this.estatusPadrinoEnums.isEmpty()) {
			for (EstatusPadrinoEnum estatusPadrinoEnum : EstatusPadrinoEnum
					.values()) {
				this.estatusPadrinoEnums.add(estatusPadrinoEnum);
			}
		}
		return estatusPadrinoEnums;
	}

	public void setEstatusPadrinoEnums(
			List<EstatusPadrinoEnum> estatusPadrinoEnums) {
		this.estatusPadrinoEnums = estatusPadrinoEnums;
	}

	public EstatusPadrinoEnum getEstatusPadrinoEnum() {
		return estatusPadrinoEnum;
	}

	public void setEstatusPadrinoEnum(EstatusPadrinoEnum estatusPadrinoEnum) {
		this.estatusPadrinoEnum = estatusPadrinoEnum;
	}

}