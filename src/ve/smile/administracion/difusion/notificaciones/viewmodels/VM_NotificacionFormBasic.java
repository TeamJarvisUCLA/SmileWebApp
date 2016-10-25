package ve.smile.administracion.difusion.notificaciones.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.seguridad.dto.Usuario;
import ve.smile.dto.TsPlanActividad;
import ve.smile.dto.EventPlanTarea;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.TsPlan;
import ve.smile.dto.Difusion;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_NotificacionFormBasic extends VM_WindowForm
{
	private Date fecha = new Date();
	private String contenido = new String();
	private List<Usuario> usuarios;
	private Integer referencia;
	
	private EstatusNotificacionEnum estatusNotificacionEnum;
	private TipoReferenciaNotificacionEnum tipoReferenciaNotificacionEnum;	
	private List<EstatusNotificacionEnum> estatusNotificacionEnums;
	private List<TipoReferenciaNotificacionEnum> tipoReferenciaNotificacionEnums;

	@Init
	public void childInit()
	{
		// NOTHING
	}
	
	// USUARIOS
	public List<Usuario> getUsuarios()
	{
		return this.usuarios;
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
	
	// REFERENCIA
	public Integer getReferencia()
	{
		return this.referencia;
	}
	
	public void setReferencia(Integer referencia)
	{
		this.referencia = referencia;
		this.getNotificacionUsuario().setReferenciaGenericoId(this.getReferencia());
	}

	// FECHA
	public Date getFecha()
	{
		return fecha;
	}

	public void setFecha(Date fecha)
	{
		this.fecha = fecha;
		this.getNotificacionUsuario().setFecha(this.getFecha().getTime());
	}
	
	// CONTENIDO
	public String getContenido()
	{
		return contenido;
	}
	
	public void setContenido(String contenido)
	{
		this.contenido = contenido;
		this.getNotificacionUsuario().setContenido(this.getContenido());
	}
	
	// BUSCAR USUARIO
	@Command("buscarUsuarios")
	public void buscarUsuarios()
	{
		CatalogueDialogData<Usuario> catalogueDialogData = new CatalogueDialogData<Usuario>();
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Usuario>()
			{
				@Override
				public void onClose(CatalogueDialogCloseEvent<Usuario> catalogueDialogCloseEvent)
				{
					if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
					{
						return;
					}
					usuarios = catalogueDialogCloseEvent.getEntities();
					refreshUsuarios();
					}
				});
		UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoUsuario.zul", catalogueDialogData);
	}

	public void refreshUsuarios()
	{
		BindUtils.postNotifyChange(null, null, this, "usuarios");
	}
	
	// BUSCAR REFERENCIA
	@Command("buscarReferencia")
	public void buscarReferencia()
	{
		if (this.getTipoReferenciaNotificacionEnum() == null)
		{
			// VALIDAR
		}
		else
		{
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.ACTIVIDAD)
			{
				CatalogueDialogData<TsPlanActividad> catalogueDialogData = new CatalogueDialogData<TsPlanActividad>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<TsPlanActividad>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<TsPlanActividad> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdTsPlanActividad();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoActividad.zul", catalogueDialogData);
			}
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.TAREA)
			{
				CatalogueDialogData<EventPlanTarea> catalogueDialogData = new CatalogueDialogData<EventPlanTarea>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<EventPlanTarea>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<EventPlanTarea> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdEventPlanTarea();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoTarea.zul", catalogueDialogData);
			}
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.EVENTO)
			{
				CatalogueDialogData<EventoPlanificado> catalogueDialogData = new CatalogueDialogData<EventoPlanificado>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<EventoPlanificado>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<EventoPlanificado> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdEventoPlanificado();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoEvento.zul", catalogueDialogData);
			}
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.TRABAJO_SOCIAL)
			{
				CatalogueDialogData<TsPlan> catalogueDialogData = new CatalogueDialogData<TsPlan>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<TsPlan>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<TsPlan> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdTsPlan();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoTrabajoSocial.zul", catalogueDialogData);
			}
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.DIFUSION)
			{
				CatalogueDialogData<Difusion> catalogueDialogData = new CatalogueDialogData<Difusion>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Difusion>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<Difusion> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdDifusion();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoDifusion.zul", catalogueDialogData);
			}
			if (this.getTipoReferenciaNotificacionEnum() == TipoReferenciaNotificacionEnum.RECONOCIMIENTO)
			{
				CatalogueDialogData<ReconocimientoPersona> catalogueDialogData = new CatalogueDialogData<ReconocimientoPersona>();
				catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<ReconocimientoPersona>()
					{
						@Override
						public void onClose(CatalogueDialogCloseEvent<ReconocimientoPersona> catalogueDialogCloseEvent)
						{
							if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR))
							{
								return;
							}
							referencia = catalogueDialogCloseEvent.getEntity().getIdReconocimientoPersona();
							refreshReferencia();
							}
						});
				UtilDialog.showDialog("views/desktop/administracion/difusion/notificaciones/catalogoReconocimiento.zul", catalogueDialogData);
			}
		}
	}

	public void refreshReferencia()
	{
		BindUtils.postNotifyChange(null, null, this, "referencia");
	}
	
	// MÉTODOS DEL FORMULARIO
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
	
	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateNull(this.getContenido(), "Contenido");
			UtilValidate.validateNull(this.getTipoReferenciaNotificacionEnum(), "Tipo de referencia");
			UtilValidate.validateNull(this.getReferencia(), "Referencia");
			UtilValidate.validateDate(this.getFecha().getTime(), "Fecha", ValidateOperator.GREATER_THAN, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
			UtilValidate.validateNull(this.getUsuarios(), "Usuarios");
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if (!isFormValidated())
		{
			return true;
		}
		// NOTIFICACION USUARIO
		this.getNotificacionUsuario().setFecha(this.getFecha().getTime());
		this.getNotificacionUsuario().setContenido(this.getContenido());
		this.getNotificacionUsuario().setTipoReferenciaNotificacionEnum(this.getTipoReferenciaNotificacionEnum());
		this.getNotificacionUsuario().setReferenciaGenericoId(this.getReferencia());
		this.getNotificacionUsuario().setEstatusNotificacionEnum(EstatusNotificacionEnum.PENDIENTE);
		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{						
			for (int k = 0; k < this.getUsuarios().size(); k++)
			{
				this.getNotificacionUsuario().setFkUsuario(this.getUsuarios().get(k));
				PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService.incluir(this.getNotificacionUsuario());
				if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse))
				{
					Alert.showMessage(payloadNotificacionUsuarioResponse);
					return true;
				}
				Alert.showMessage(payloadNotificacionUsuarioResponse);
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{			
			for (int k = 0; k < this.getUsuarios().size(); k++)
			{
				this.getNotificacionUsuario().setFkUsuario(this.getUsuarios().get(k));
				PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService.modificar(this.getNotificacionUsuario());
				if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse))
				{
					Alert.showMessage(payloadNotificacionUsuarioResponse);
					return true;
				}
				Alert.showMessage(payloadNotificacionUsuarioResponse);
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
	
	public NotificacionUsuario getNotificacionUsuario()
	{
		return (NotificacionUsuario) DataCenter.getEntity();
	}
}