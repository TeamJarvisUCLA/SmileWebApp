package ve.smile.datos.configuracion;

import java.util.List;
import java.util.ArrayList;

import karen.core.crux.alert.Alert;
import karen.core.simple_list_principal.viewmodels.VM_WindowSimpleListPrincipal;
import karen.core.util.payload.UtilPayload;
import lights.core.payload.response.IPayloadResponse;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;

import ve.smile.dto.TrabajoSocial;
import ve.smile.payload.response.PayloadTrabajoSocialResponse;

import ve.smile.dto.PlantillaTrabajoSocialIndicador;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialIndicadorResponse;

import ve.smile.dto.PlantillaTrabajoSocialActividad;
import ve.smile.payload.response.PayloadPlantillaTrabajoSocialActividadResponse;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;


public class VMPPlantillaTrabajoSocial extends VM_WindowSimpleListPrincipal<TrabajoSocial> {

	private List <PlantillaTrabajoSocialActividad> trabajoSocialActividades;
	private List <PlantillaTrabajoSocialIndicador> trabajoSocialIndicadores;
	
	@Init(superclass = true)
	public void childInit()
	{
		//NOTHING OK!
	}

	@Override
	public IPayloadResponse<TrabajoSocial> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		PayloadTrabajoSocialResponse payloadTrabajoSocialResponse = S.TrabajoSocialService.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajoSocialResponse;
	}

	@Override
	public void doDelete()
	{
		/*
		CONSULTAR LA ELIMINACIÓN O NO DE PLANTILLAS
		*/
	}

	@Override
	public String getSrcFileZulForm(OperacionEnum operacionEnum) {
		return "views/desktop/configuracion/plantillaTrabajoSocial/PlantillaTrabajoSocialFormBasic.zul";
	}

	@Command("onSelectTrabajoSocial")
	public void onSelectTrabajoSocial() {
		TrabajoSocial trabajo = getSelectedObject();
		if (trabajo.getTrabajoSocialIndicadors() == null || trabajo.getTrabajoSocialActividades() == null)
		{
			if (this.trabajoSocialIndicadores == null)
			{
				this.trabajoSocialIndicadores = new ArrayList<>();
			}
			if (this.trabajoSocialIndicadores.isEmpty())
			{
				PayloadPlantillaTrabajoSocialIndicadorResponse payloadPlantillaTrabajoSocialIndicadorResponse = S.PlantillaTrabajoSocialIndicadorService.consultarPorEvento(evento.getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialIndicadorResponse))
				{
					Alert.showMessage(payloadPlantillaTrabajoSocialIndicadorResponse);
				}
				trabajo.setTrabajoSocialIndicadors(payloadPlantillaTrabajoSocialIndicadorResponse.getObjetos());
				this.trabajoSocialIndicadores = trabajo.getTrabajoSocialIndicadors();				
			}
			if (this.trabajoSocialActividades == null)
			{
				this.trabajoSocialActividades = new ArrayList<>();
			}
			if (this.trabajoSocialActividades.isEmpty())
			{
				PayloadPlantillaTrabajoSocialActividadResponse payloadPlantillaTrabajoSocialActividadResponse = S.PlantillaTrabajoSocialActividadService.consultarPorEvento(evento.getIdEvento());	
				if (!UtilPayload.isOK(payloadPlantillaTrabajoSocialActividadResponse))
				{
					Alert.showMessage(payloadPlantillaTrabajoSocialActividadResponse);
				}
				trabajo.setTrabajoSocialActividades(payloadPlantillaTrabajoSocialActividadResponse.getObjetos());
				this.trabajoSocialActividades = trabajo.getTrabajoSocialActividades();				
			}
		}
	}
}