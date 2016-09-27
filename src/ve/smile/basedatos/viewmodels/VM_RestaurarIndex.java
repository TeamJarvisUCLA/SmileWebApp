package ve.smile.basedatos.viewmodels;

import java.util.Date;
import java.util.HashMap;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

import karen.core.crux.alert.Alert;
import karen.core.listfoot.enums.HowToSeeEnum;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Respaldo;
import ve.smile.payload.response.PayloadRespaldoResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_RestaurarIndex extends VM_WindowSimpleListPrincipal<Respaldo> {
	private String nombre;
	private Date fecha;

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
		System.out.println("escuchar bro" + this.nombre);
		this.updateListBox(1, HowToSeeEnum.NORMAL);
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

}
