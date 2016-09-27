package ve.smile.gestion.voluntariado.capacitacion.ejecucion.viewmodels;

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

import ve.smile.dto.CapacitacionPlanificada;
import ve.smile.dto.Capacitacion;
import ve.smile.dto.Directorio;
import ve.smile.dto.Motivo;

import ve.smile.payload.response.PayloadCapacitacionPlanificadaResponse;
import ve.smile.payload.response.PayloadCapacitacionResponse;
import ve.smile.payload.response.PayloadDirectorioResponse;
import ve.smile.payload.response.PayloadMotivoResponse;

import ve.smile.seguridad.enums.OperacionEnum;

public class VM_CapacitacionEjecutadaFormBasic extends VM_WindowForm {

	private List<Capacitacion> capacitaciones;
	private List<Directorio> lugares;
	private List<Motivo> motivos;

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	public List<Capacitacion> getCapacitaciones()
	{
		if (this.capacitaciones == null)
		{
			this.capacitaciones = new ArrayList<>();
		}
		if (this.capacitaciones.isEmpty())
		{
			PayloadCapacitacionResponse payloadCapacitacionResponse = S.CapacitacionService.consultarTodos();
			if (!UtilPayload.isOK(payloadCapacitacionResponse)) {
				Alert.showMessage(payloadCapacitacionResponse);
			}
			this.capacitaciones.addAll(payloadCapacitacionResponse.getObjetos());
		}
		return capacitaciones;
	}

	public void setCapacitaciones(List<Capacitacion> capacitaciones)
	{
		this.capacitaciones = capacitaciones;
	}
	
	public List<Directorio> getLugares()
	{
		if (this.lugares == null)
		{
			this.lugares = new ArrayList<>();
		}
		if (this.lugares.isEmpty())
		{
			PayloadDirectorioResponse payloadDirectorioResponse = S.DirectorioService.consultarTodos();
			if (!UtilPayload.isOK(payloadDirectorioResponse)) {
				Alert.showMessage(payloadDirectorioResponse);
			}
			this.lugares.addAll(payloadDirectorioResponse.getObjetos());
		}
		return lugares;
	}

	public void setLugares(List<Directorio> lugares)
	{
		this.lugares = lugares;
	}
	
	public List<Motivo> getMotivos()
	{
		if (this.motivos == null)
		{
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty())
		{
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService.consultarTodos();
			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
			}
			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}
		return motivos;
	}

	public void setMotivos(List<Motivo> motivos)
	{
		this.motivos = motivos;
	}

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

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if (!isFormValidated())
		{
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.incluir(getCapacitacionPlanificada());
			if (!UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				Alert.showMessage(payloadCapacitacionPlanificadaResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			PayloadCapacitacionPlanificadaResponse payloadCapacitacionPlanificadaResponse = S.CapacitacionPlanificadaService.modificar(getCapacitacionPlanificada());
			if (!UtilPayload.isOK(payloadCapacitacionPlanificadaResponse))
			{
				Alert.showMessage(payloadCapacitacionPlanificadaResponse);
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

	public CapacitacionPlanificada getCapacitacionPlanificada()
	{
		return (CapacitacionPlanificada) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateNull(getCapacitacionPlanificada().getFkCapacitacion(), "Capacitación");
			UtilValidate.validateNull(getCapacitacionPlanificada().getFkDirectorio(), "Lugar");
			UtilValidate.validateNull(getCapacitacionPlanificada().getFechaPlanificada(), "Fecha planificada");
			UtilValidate.validateNull(getCapacitacionPlanificada().getFechaEjecutada(), "Fecha ejecutada");
			UtilValidate.validateNull(getCapacitacionPlanificada().getFkMotivo(), "Motivo");
			UtilValidate.validateString(getCapacitacionPlanificada().getObservacion(), "Observación", 250);
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
