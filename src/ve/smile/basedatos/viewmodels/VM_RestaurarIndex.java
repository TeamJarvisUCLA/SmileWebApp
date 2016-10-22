package ve.smile.basedatos.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Respaldo;
import ve.smile.payload.response.PayloadRespaldoResponse;
import ve.smile.seguridad.dto.Tabla;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_RestaurarIndex extends VM_WindowSimpleListPrincipal<Respaldo> {
	private String nombre;
	private Date fecha;
	private List<Tabla> listsTabla = new ArrayList<>();

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Respaldo> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		HashMap<String, String> criterios = new HashMap<>();
		if (!(this.nombre == null || this.nombre.isEmpty())) {
			criterios.put("fkMultimedia.nombre", this.nombre);
		}

		PayloadRespaldoResponse payloadRespaldoResponse = S.RespaldoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.LIKE, criterios);
		return payloadRespaldoResponse;
	}

	@Command("findByRespaldo")
	@NotifyChange({ "listsTabla" })
	public void findByRespaldo() {
		getSelectedObject().getFechaRespaldo();
		listsTabla = S.TablaService.consultarPorRespaldo(
				getSelectedObject().getIdRespaldo()).getObjetos();
	}

	@Override
	public void doDelete() {
		PayloadRespaldoResponse payloadRespaldoTablaResponse = S.RespaldoService
				.eliminar(getSelectedObject().getIdRespaldo());

		Alert.showMessage(payloadRespaldoTablaResponse);

		if (UtilPayload.isOK(payloadRespaldoTablaResponse)) {
			getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
		}

	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	public void changeFilter() {
		this.getControllerWindowSimpleListPrincipal().updateListBoxAndFooter();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Tabla> getListsTabla() {
		return listsTabla;
	}

	public void setListsTabla(List<Tabla> listsTabla) {
		this.listsTabla = listsTabla;
	}

}
