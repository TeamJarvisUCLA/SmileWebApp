package ve.smile.gestion.donativo.registro.viewmodels;

import java.util.HashMap;
import java.util.Map;

import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Recurso;
import ve.smile.payload.response.PayloadRecursoResponse;

public class VM_CatalogoRecurso extends
		VM_ListPaginationCatalogueDialog<Recurso> {

	private String nombre;
	private String clasificadorRecurso;
	private String unidadMedida;

	@Init(superclass = true)
	public void childInit_VM_CatalogoIconSclass() {
		// NOTHING OK!
	}

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Recurso> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<String, String>();
		if (nombre != null && !nombre.equalsIgnoreCase("")) {
			criterios.put("nombre", nombre);
		}
		if (clasificadorRecurso != null) {
			criterios.put("fkClasificadorRecurso.nombre",
					String.valueOf(clasificadorRecurso));
		}
		if (unidadMedida != null) {
			criterios
					.put("fkUnidadMedida.nombre", String.valueOf(unidadMedida));
		}
		PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.ILIKE, criterios);
		return payloadRecursoResponse;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClasificadorRecurso() {
		return clasificadorRecurso;
	}

	public void setClasificadorRecurso(String clasificadorRecurso) {
		this.clasificadorRecurso = clasificadorRecurso;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	@Command
	public void changeFilter() {
		super.getControllerListPaginationCatalogueDialog()
				.updateListBoxAndFooter();
	}
}
