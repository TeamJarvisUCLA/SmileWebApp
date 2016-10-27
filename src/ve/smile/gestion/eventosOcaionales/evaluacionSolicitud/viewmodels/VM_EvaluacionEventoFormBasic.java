package ve.smile.gestion.eventosOcaionales.evaluacionSolicitud.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;

public class VM_EvaluacionEventoFormBasic extends VM_WindowForm{
	
	private Date fechaInicio;
	private Date fechaFin;
	
	@Init(superclass = true)
	public void childInit() {
		this.fechaInicio = new Date(getEventoPlanificado().getFechaInicio());
		this.fechaFin = new Date(getEventoPlanificado().getFechaFin());
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		 List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		 operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.APROBAR));
		 operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.RECHAZAR));
		 if(operacionEnum.equals(OperacionEnum.CUSTOM1)){
			 
		 }
		 
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
	public boolean actionAprobar(OperacionEnum operacionEnum) {
		getEventoPlanificado().setEstatusEvento(EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal());
		PayloadEventoPlanificadoResponse eventoPlanificadoResponse = S.EventoPlanificadoService.modificar(getEventoPlanificado());
		if(UtilPayload.isOK(eventoPlanificadoResponse)) {
			 Alert.showMessage("S:Success Code: 005-Registro aprobado con exito.");
			 DataCenter.reloadCurrentNodoMenu();
			 return true;
		}
		return false;
	}

	@Override
	public boolean actionRechazar(OperacionEnum operacionEnum) {
		getEventoPlanificado().setEstatusEvento(EstatusEventoPlanificadoEnum.RECHAZADO.ordinal());
		PayloadEventoPlanificadoResponse eventoPlanificadoResponse = S.EventoPlanificadoService.modificar(getEventoPlanificado());
		if(UtilPayload.isOK(eventoPlanificadoResponse)) {
			 Alert.showMessage("S:Success Code: 005-Registro rechazado con exito.");
			 DataCenter.reloadCurrentNodoMenu();
			 return true;
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}
	
	public EventoPlanificado getEventoPlanificado() {
		return (EventoPlanificado) DataCenter.getEntity();
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	

}
