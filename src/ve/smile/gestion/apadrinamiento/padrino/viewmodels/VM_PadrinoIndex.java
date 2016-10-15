package ve.smile.gestion.apadrinamiento.padrino.viewmodels;

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
import ve.smile.dto.Padrino;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PadrinoIndex extends VM_WindowSimpleListPrincipal<Padrino> {

	private String nombre;
	private String identificacion;
	private String frecuencia;
	private List<EstatusPadrinoEnum> estatusPadrinoEnums;
	private EstatusPadrinoEnum estatusPadrinoEnum;

	@Init(superclass = true)
	public void childInit() {
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

		if (nombre != null && !nombre.equalsIgnoreCase("")) {
			criterios.put("fkPersona.nombre", nombre);
		}
		if (identificacion != null && !identificacion.equalsIgnoreCase("")) {
			criterios.put("fkPersona.identificacion", identificacion);
		}
		if (frecuencia != null && !frecuencia.equalsIgnoreCase("")) {
			criterios.put("fkFrecuenciaAporte.nombre", frecuencia);
		}
		if (this.getEstatusPadrinoEnum() != null) {
			criterios.put("estatusPadrino",
					String.valueOf(this.estatusPadrinoEnum.ordinal()));
		}
		PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.ILIKE, criterios);
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

}