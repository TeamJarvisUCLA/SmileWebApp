package ve.smile.web.viewmodels.participa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.crux.session.DataCenter;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.dto.Cartelera;
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.dto.Participacion;
import ve.smile.dto.Requisito;
import ve.smile.dto.RequisitoParticipacion;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.payload.response.PayloadAyudaResponse;
import ve.smile.payload.response.PayloadCarteleraResponse;
import ve.smile.payload.response.PayloadParticipacionResponse;
import ve.smile.payload.response.PayloadPreguntaClasificadaResponse;
import ve.smile.payload.response.PayloadRequisitoParticipacionResponse;
import ve.smile.payload.response.PayloadRequisitoResponse;

public class VM_Participa {
	private List<Participacion> participa;
	private List<RequisitoParticipacion> rParticipacion;
	private RequisitoParticipacion rparticipacion;
	private Participacion participacion;
	private Requisito requisito;
//	private List<Requisito> requisitos;
	private List<Ayuda> ayudas;
	private Ayuda ayuda;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}




	public List<Ayuda> getAyudas() {
		if (this.ayudas == null) {
			this.ayudas = new ArrayList<>();
		}
		if (this.ayudas.isEmpty()) {

			PayloadAyudaResponse payloadAyudaResponse = S.AyudaService.consultarAllAyuda();

			this.ayudas.addAll(payloadAyudaResponse.getObjetos());
		}

		return ayudas;	
		}

	
	public Ayuda getAyuda() {
		return ayuda;
	}



	public void setAyuda(Ayuda ayuda) {
		this.ayuda = ayuda;
	}
	
//	public List<Requisito> getRequisitos(Integer idParticipacion) {
//
//		PayloadRequisitoResponse payloadRequisitoResponse = S.RequisitoService
//				.consultarParticipacionRequisitos(idParticipacion);
//		return (payloadRequisitoResponse.getObjetos());
//
//	}

	public Requisito getRequisito() {
		return (Requisito) DataCenter.getEntity();
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public RequisitoParticipacion getRparticipacion() {
		return rparticipacion;
	}

	public void setRparticipacion(RequisitoParticipacion rparticipacion) {
		this.rparticipacion = rparticipacion;
	}

	public Participacion getParticipacion() {
		return (Participacion) DataCenter.getEntity();
	}

	public void setrParticipacion(List<RequisitoParticipacion> rParticipacion) {
		this.rParticipacion = rParticipacion;
	}

	public void setParticipacion(Participacion participacion) {
		this.participacion = participacion;
	}

	public List<Participacion> getparticipa() {
		if (this.participa == null) {
			this.participa = new ArrayList<>();
		}
		if (this.participa.isEmpty()) {

			PayloadParticipacionResponse payloadParticipacionResponse = S.ParticipacionService
					.consultaAllParticipacion();

			this.participa.addAll(payloadParticipacionResponse.getObjetos());
			this.participa.add(new Participacion(null, "Ayuda", ""));
		}

		return participa;
	}
	@Command
	public void contactanos() {
		DataCenter.updateSrcPageContent(null, null, "/views/web/contactanos.zul");
		
	}

}
