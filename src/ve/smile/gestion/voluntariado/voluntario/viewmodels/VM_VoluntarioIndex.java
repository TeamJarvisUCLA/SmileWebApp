package ve.smile.gestion.voluntariado.voluntario.viewmodels;

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
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_VoluntarioIndex extends VM_WindowSimpleListPrincipal<Voluntario>
{
	private TipoPersonaEnum tipoPersonaEnum;
	private List<TipoPersonaEnum> tipoPersonaEnums;
	private EstatusVoluntarioEnum estatusVoluntarioEnum;
	private List<EstatusVoluntarioEnum> estatusVoluntarioEnums;
	
	@Init(superclass = true)
	public void childInit()
	{
		estatusVoluntarioEnum = EstatusVoluntarioEnum.ACTIVO;
	}

	@Override
	public IPayloadResponse<Voluntario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<String, String>();
		if (this.getTipoPersonaEnum() != null)
		{
			criterios.put("fkPersona.tipoPersona", String.valueOf(this.tipoPersonaEnum.ordinal()));
		}
		if (this.getEstatusVoluntarioEnum() != null)
		{
			criterios.put("estatusVoluntario", String.valueOf(this.estatusVoluntarioEnum.ordinal()));
		}
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.eliminar(getSelectedObject().getIdVoluntario());
		Alert.showMessage(payloadVoluntarioResponse);
		if (UtilPayload.isOK(payloadVoluntarioResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum)
	{
		return "views/desktop/gestion/voluntariado/voluntario/VoluntarioFormBasic.zul";
	}
	
	@Override
	public String isValidSearchDataModificar()
	{
		if (selectedObject.getEstatusVoluntarioEnum().equals(EstatusVoluntarioEnum.POSTULADO) ||
			selectedObject.getEstatusVoluntarioEnum().equals(EstatusVoluntarioEnum.INACTIVO) ||
			selectedObject.getEstatusVoluntarioEnum().equals(EstatusVoluntarioEnum.RECHAZADO))
		{
			Alert.showMessage("E: Error Code: 100-El voluntario seleccionado no puede ser modicado su estatus es <b> " + selectedObject.getEstatusVoluntarioEnum().toString() + "</b>");
		}
		return super.isValidSearchDataModificar();
	}

	@Command
	public void changeFilter() {
		super.updateListBox(1, HowToSeeEnum.NORMAL);
	}
	
	// TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum()
	{
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum)
	{
		this.tipoPersonaEnum = tipoPersonaEnum;
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums()
	{
		if (this.tipoPersonaEnums == null)
		{
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty())
		{
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values())
			{
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums)
	{
		this.tipoPersonaEnums = tipoPersonaEnums;
	}
	
	// ESTATUS VOLUNTARIO
	public List<EstatusVoluntarioEnum> getEstatusVoluntarioEnums()
	{
		if (this.estatusVoluntarioEnums == null)
		{
			this.estatusVoluntarioEnums = new ArrayList<>();
		}
		if (this.estatusVoluntarioEnums.isEmpty())
		{
			for (EstatusVoluntarioEnum estatusVoluntarioEnum : EstatusVoluntarioEnum.values())
			{
				this.estatusVoluntarioEnums.add(estatusVoluntarioEnum);
			}
		}
		return estatusVoluntarioEnums;
	}

	public void setEstatusVoluntarioEnums(List<EstatusVoluntarioEnum> estatusVoluntarioEnums)
	{
		this.estatusVoluntarioEnums = estatusVoluntarioEnums;
	}

	public EstatusVoluntarioEnum getEstatusVoluntarioEnum()
	{
		return estatusVoluntarioEnum;
	}

	public void setEstatusVoluntarioEnum(EstatusVoluntarioEnum estatusVoluntarioEnum)
	{
		this.estatusVoluntarioEnum = estatusVoluntarioEnum;
	}

}
