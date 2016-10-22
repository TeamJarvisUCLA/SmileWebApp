package ve.smile.gestion.evento.planificaion.registro.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Persona;
import ve.smile.dto.Trabajador;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import karen.core.dialog.catalogue.list_pagination.viewmodels.VM_ListPaginationCatalogueDialog;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

public class VM_CatalogoVoluntario extends VM_ListPaginationCatalogueDialog<Persona>{
	
	@Init(superclass=true)
    public void childInit_VM_CatalogoIconSclass() {
		tipo = this.getTipoPersona().get(0);
    }
	
	private List<String> tipoPersona;
	private String tipo;

	@Override
	public void afterChildInit() {
		// NOTHING OK!
	}

	@Override
	public IPayloadResponse<Persona> getObjectListToLoad(
			Integer cantidadRegistrosPagina, Integer pagina) {
		List<Persona> personas = new ArrayList<>();
		PayloadPersonaResponse payloadPersonaResponse = new PayloadPersonaResponse();

		if (tipo.equalsIgnoreCase("TRABAJADORES")) {

			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("estatusTrabajador",
					String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultarPaginacionCriterios(cantidadRegistrosPagina,
							pagina, TypeQuery.EQUAL, criterios);

			List<Trabajador> trabajadores = payloadTrabajadorResponse
					.getObjetos();
			for (Trabajador trabajador : trabajadores) {
				personas.add(trabajador.getFkPersona());
			}
			payloadPersonaResponse.setObjetos(personas);
			payloadPersonaResponse.setInformacion(payloadTrabajadorResponse
					.getInformacion());

		} else {
			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("estatusVoluntario",
					String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.consultarPaginacionCriterios(cantidadRegistrosPagina,
							pagina, TypeQuery.EQUAL, criterios);
			List<Voluntario> voluntarios = payloadVoluntarioResponse
					.getObjetos();
			for (Voluntario voluntario : voluntarios) {
				personas.add(voluntario.getFkPersona());
			}
			payloadPersonaResponse.setObjetos(personas);
			payloadPersonaResponse.setInformacion(payloadVoluntarioResponse
					.getInformacion());
		}

		return payloadPersonaResponse;
	}
	@Command("changeFilter")
	public void changeFilter() {
		getControllerListPaginationCatalogueDialog().updateListBoxAndFooter();
	}

	public List<String> getTipoPersona() {
		if (tipoPersona == null) {
			tipoPersona = new ArrayList<>();
		}
		if (tipoPersona.isEmpty()) {
			tipoPersona.add("VOLUNTARIOS");
			tipoPersona.add("TRABAJADORES");
		}
		return tipoPersona;
	}

	public void setTipoPersona(List<String> tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
