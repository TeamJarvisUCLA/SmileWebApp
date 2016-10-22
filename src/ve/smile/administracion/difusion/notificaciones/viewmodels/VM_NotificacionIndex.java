package ve.smile.administracion.difusion.notificaciones.viewmodels;

import java.util.ArrayList;
import java.util.Date;
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
import ve.smile.dto.NotificacionUsuario;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_NotificacionIndex extends VM_WindowSimpleListPrincipal<NotificacionUsuario>
{
	private EstatusNotificacionEnum estatusNotificacionEnum;
	private TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum;
	
	private List<EstatusNotificacionEnum> estatusNotificacionEnums;
	private List<TipoReferenciaNotificacionEnum> tipoReferenciaNotificacionEnums;
	
	@Init(superclass = true)
	public void childInit()
	{
		estatusNotificacionEnum = EstatusNotificacionEnum.PENDIENTE;
	}
	
	// CARGAR OBJETOS
	@Override
	public IPayloadResponse<NotificacionUsuario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<String, String>();
		if (this.getEstatusNotificacionEnum() != null)
		{
			criterios.put("estatusNotificacion", String.valueOf(this.estatusNotificacionEnum.ordinal()));
		}
		if (this.getTipoReferenciaNotificacionEnum() != null)
		{
			criterios.put("tipoReferenciaNotificacion", String.valueOf(this.tipoReferenciaNotificacionEnum.ordinal()));
		}
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina, TypeQuery.ILIKE, criterios);
		return payloadNotificacionUsuarioResponse;
	}

	// VALIDACION
	@Override
	public String isValidSearchDataModificar()
	{
		if (selectedObject.getEstatusNotificacion().equals(EstatusNotificacionEnum.PENDIENTE.ordinal()))
		{
			Alert.showMessage("E: Error Code: 100-La notificación no puede ser modicada, su estatus es <b> " + selectedObject.getEstatusNotificacion().toString() + "</b>");
		}
		return super.isValidSearchDataModificar();
	}
	
	// ELIMINAR
	@Override
	public void doDelete()
	{
		PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService.eliminar(getSelectedObject().getIdNotificacionUsuario());
		Alert.showMessage(payloadNotificacionUsuarioResponse);
		if (UtilPayload.isOK(payloadNotificacionUsuarioResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
	}

	// FORMULARIO
	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum)
	{
		return "views/desktop/administracion/difusion/notificaciones/NotificacionFormBasic.zul";
	}

	// FILTRO
	@Command
	public void changeFilter()
	{
		super.getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
	}
	
	// ESTATUS NOTIFICACION
	public List<EstatusNotificacionEnum> getEstatusNotificacionEnums()
	{
		if (this.estatusNotificacionEnums == null)
		{
			this.estatusNotificacionEnums = new ArrayList<>();
		}
		if (this.estatusNotificacionEnums.isEmpty())
		{
			for (EstatusNotificacionEnum estatusNotificacionEnum : EstatusNotificacionEnum.values())
			{
				this.estatusNotificacionEnums.add(estatusNotificacionEnum);
			}
		}
		return estatusNotificacionEnums;
	}

	public void setEstatusNotificacionEnums(List<EstatusNotificacionEnum> estatusNotificacionEnums)
	{
		this.estatusNotificacionEnums = estatusNotificacionEnums;
	}

	public EstatusNotificacionEnum getEstatusNotificacionEnum()
	{
		return estatusNotificacionEnum;
	}

	public void setEstatusNotificacionEnum(EstatusNotificacionEnum estatusNotificacionEnum)
	{
		this.estatusNotificacionEnum = estatusNotificacionEnum;
	}
	
	// TIPO REFERENCIA
	public List<TipoReferenciaNotificacionEnum> getTipoReferenciaNotificacionEnums()
	{
		if (this.tipoReferenciaNotificacionEnums == null)
		{
			this.tipoReferenciaNotificacionEnums = new ArrayList<>();
		}
		if (this.tipoReferenciaNotificacionEnums.isEmpty())
		{
			for (TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum : TipoReferenciaNotificacionEnum.values())
			{
				this.tipoReferenciaNotificacionEnums.add(tipoReferenciaNotificacionEnum);
			}
		}
		return tipoReferenciaNotificacionEnums;
	}

	public void setTipoReferenciaNotificacionEnums(List<TipoReferenciaNotificacionEnum> tipoReferenciaNotificacionEnums)
	{
		this.tipoReferenciaNotificacionEnums = tipoReferenciaNotificacionEnums;
	}

	public TipoReferenciaNotificacionEnum getTipoReferenciaNotificacionEnum()
	{
		return tipoReferenciaNotificacionEnum;
	}

	public void setTipoReferenciaNotificacionEnum(TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum)
	{
		this.tipoReferenciaNotificacionEnum = tipoReferenciaNotificacionEnum;
	}
}
