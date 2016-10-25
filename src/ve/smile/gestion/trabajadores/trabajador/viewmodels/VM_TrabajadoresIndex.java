package ve.smile.gestion.trabajadores.trabajador.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Cargo;
import ve.smile.dto.Profesion;
import ve.smile.dto.Trabajador;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.payload.response.PayloadCargoResponse;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;
import ve.smile.payload.response.PayloadEventPlanTareaTrabajadorResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_TrabajadoresIndex extends VM_WindowSimpleListPrincipal<Trabajador>
{
	
	private List<Cargo> cargos;
	private Cargo cargo;
	private List<Trabajador> trabajadores;
	private String nombre;
	private String identificacion;	
	private List<Profesion> profesiones;
	private List<EstatusTrabajadorEnum> estatusTrabajadorEnums;
	private EstatusTrabajadorEnum estatusTrabajadorEnum;
	

	@Init(superclass = true)
	public void childInit()
	{
		estatusTrabajadorEnum = EstatusTrabajadorEnum.ACTIVO;	
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();

		if (nombre != null && !nombre.equalsIgnoreCase("")) {
			criterios.put("fkPersona.nombre", nombre);
		}
		if (identificacion != null && !identificacion.equalsIgnoreCase("")) {
			criterios.put("fkPersona.identificacion", identificacion);
		}
		if (this.getCargo() != null)
		{
			criterios.put("fkCargo.idCargo", String.valueOf(this.cargo.getIdCargo()));
		}
		if (this.getEstatusTrabajadorEnum() != null)
		{
			criterios.put("estatusTrabajador", String.valueOf(this.estatusTrabajadorEnum.ordinal()));
		}
		PayloadTrabajadorResponse payloadTrabajadorResponse = 
				S.TrabajadorService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.ILIKE, criterios);
		return payloadTrabajadorResponse;
	}

	@Override
	public void doDelete()
	{
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.eliminar(getSelectedObject().getIdTrabajador());
		
		Alert.showMessage(payloadTrabajadorResponse);
		
		if (UtilPayload.isOK(payloadTrabajadorResponse))
		{
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}
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

	public EstatusTrabajadorEnum getEstatusTrabajadorEnum() {
		return estatusTrabajadorEnum;
	}

	public void setEstatusTrabajadorEnum(EstatusTrabajadorEnum estatusTrabajadorEnum) {
		this.estatusTrabajadorEnum = estatusTrabajadorEnum;
	}
	
	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public List<Cargo> getCargos() {
		if (this.cargos == null) {
			this.cargos = new ArrayList<>();
		}
		if (this.cargos.isEmpty()) {
			PayloadCargoResponse payloadCargoResponse = S.CargoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadCargoResponse)) {
				Alert.showMessage(payloadCargoResponse);
			}
			this.cargos.addAll(payloadCargoResponse.getObjetos());
		}
		return cargos;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public List<Trabajador> getTrabajadores() {
		if (this.trabajadores == null) {
			this.trabajadores = new ArrayList<>();
		}
		if (this.trabajadores.isEmpty()) {
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
				Alert.showMessage(payloadTrabajadorResponse);
			}
			this.trabajadores.addAll(payloadTrabajadorResponse.getObjetos());
		}
		return trabajadores;
	}
	
	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public List<Profesion> getProfesiones() {
		return profesiones;
	}

	public void setProfesiones(List<Profesion> profesiones) {
		this.profesiones = profesiones;
	}

	public List<EstatusTrabajadorEnum> getEstatusTrabajadorEnums() {
		if (this.estatusTrabajadorEnums == null) {
			this.estatusTrabajadorEnums = new ArrayList<>();
		}
		if (this.estatusTrabajadorEnums.isEmpty()) {
			for (EstatusTrabajadorEnum estatusTrabajadorEnum : EstatusTrabajadorEnum.values()) {
				this.estatusTrabajadorEnums.add(estatusTrabajadorEnum);
			}
		}
		return estatusTrabajadorEnums;
	}

	public void setEstatusTrabajadorEnums(
			List<EstatusTrabajadorEnum> estatusTrabajadorEnums) {
		this.estatusTrabajadorEnums = estatusTrabajadorEnums;
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/gestion/trabajadores/trabajador/TrabajadorFormBasic.zul";
	}
	
	@Override
	public String isValidPreconditionsModificar() {
		if(getSelectedObject().getEstatusTrabajadorEnum().ordinal() == EstatusTrabajadorEnum.INACTIVO.ordinal()){
			return "I:Information Code: 905-Trabajador Inactivo, no puede modificar sus datos";
		}
		return "";
	}
	
	@Override
	public String isValidPreconditionsEliminar() {
		if (getSelectedObject() == null) {
			return "I:Information Code: 905-Debe seleccionar un Trabajador";
		}

		Map<String, String> criterios =
				new HashMap<String, String>();

		criterios.put("fkTrabajador.idTrabajador", String.valueOf(getSelectedObject().getIdTrabajador()));

		//Table Relation Estudiosocieconomico
		PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse =
				S.EstudioSocioEconomicoService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEstudioSocioEconomicoResponse)) {
			return String.valueOf(payloadEstudioSocioEconomicoResponse.getInformacion(IPayloadResponse.MENSAJE));
		}
		
		//Table Relation EventPlanTareaTrabajador
		PayloadEventPlanTareaTrabajadorResponse payloadEventPlanTareaTrabajadorResponse =
				S.EventPlanTareaTrabajadorService.contarCriterios(TypeQuery.EQUAL,
						criterios);

		if (!UtilPayload.isOK(payloadEventPlanTareaTrabajadorResponse)) {
			return String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
		}
		
		//Table Relation TsPlanActividadTrabajador
				PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse =
						S.TsPlanActividadTrabajadorService.contarCriterios(TypeQuery.EQUAL,
								criterios);

				if (!UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {
					return String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.MENSAJE));
				}
		

		Integer countEstudiosSocieconomicos = 
				Double.valueOf(String.valueOf(payloadEstudioSocioEconomicoResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
		Integer countEventPlanTareasTrabajador = 
				Double.valueOf(String.valueOf(payloadEventPlanTareaTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();
		Integer countTsPlanActividadesTrabajador = 
				Double.valueOf(String.valueOf(payloadTsPlanActividadTrabajadorResponse.getInformacion(IPayloadResponse.COUNT))).intValue();

		if (countEstudiosSocieconomicos > 0) {
			if(countEstudiosSocieconomicos == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que est� siendo utilizado en un estudio sociecon�mico";	
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countEstudiosSocieconomicos + " estudios sociecon�micos";
			}
		}
		if (countEventPlanTareasTrabajador > 0) {
			if(countEventPlanTareasTrabajador == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en una tarea de evento";
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countEventPlanTareasTrabajador + " tareas de eventos";
			}
		}
		if (countTsPlanActividadesTrabajador > 0) {
			if(countTsPlanActividadesTrabajador == 1){
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en una actividad del trabajo social";
			}else{
				return "E:Error 0:No se puede eliminar el <b>Trabajador</b> seleccionado ya que está siendo utilizado en " + countTsPlanActividadesTrabajador + " actividades del trabajo social";
			}
		}
		
		return "";
	}

	@Command
	public void changeFilter() {
		super.getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
	}
}