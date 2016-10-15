package ve.smile.web.viewmodels.faq;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.ClasificadorPregunta;
import ve.smile.dto.Pregunta;
import ve.smile.dto.PreguntaClasificada;
import ve.smile.payload.response.PayloadClasificadorPreguntaResponse;
import ve.smile.payload.response.PayloadPreguntaClasificadaResponse;

public class VM_Faq {
	private List<ClasificadorPregunta> cPreguntas;
	private List<PreguntaClasificada> pClasificadas;
	private ClasificadorPregunta cPregunta = new ClasificadorPregunta();
	private PreguntaClasificada pClasificada = new PreguntaClasificada();
	private Pregunta pregunta;

	@Init(superclass = true)
	public void childInit() {

		PayloadClasificadorPreguntaResponse payloadClasificadorPreguntaResponse = S.ClasificadorPreguntaService
				.consultarClasificadorPregunta();
		this.cPreguntas = payloadClasificadorPreguntaResponse.getObjetos();

	}

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public ClasificadorPregunta getcPregunta() {
		return this.cPregunta;
	}

	public List<PreguntaClasificada> getpClasificadas() {
		return pClasificadas;
	}

	public void setpClasificadas(List<PreguntaClasificada> pClasificadas) {
		this.pClasificadas = pClasificadas;
	}

	public void setcPregunta(ClasificadorPregunta cPregunta) {
		this.cPregunta = cPregunta;
	}

	public PreguntaClasificada getpClasificada() {
		return pClasificada;
	}

	public void setpClasificada(PreguntaClasificada pClasificada) {
		this.pClasificada = pClasificada;
	}

	public void setcPreguntas(List<ClasificadorPregunta> cPreguntas) {
		this.cPreguntas = cPreguntas;
	}

	public List<ClasificadorPregunta> getcPreguntas() {

		return cPreguntas;
	}

	//
	@Command
	public void seleccionar() {

		PayloadPreguntaClasificadaResponse payloadPreguntaClasificadaResponse = S.PreguntaClasificadaService
				.consultarPreguntaClasificador(cPregunta
						.getIdClasificadorPregunta());

		this.pClasificadas = payloadPreguntaClasificadaResponse
				.getObjetos();

		BindUtils.postNotifyChange(null, null, this, "pClasificadas");

	}

	@Command
	public void abrirFormPregunta(){
		DialogData dialogData = new DialogData();
		UtilDialog.showDialog("/views/web/formPregunta.zul", dialogData);
	}
}

// if(item.equales(cd.getItem){
// list.setModel(reminders1)//ok
// selectedItem(toModel.reminder)
// }else{
// list.setModel(reminders2)
// selectedItem(toModel.reminder)

// }

// public String cant(Integer idClasificadorPregunta) {
// return String.valueOf(getpClasificadas(idClasificadorPregunta).size());

// }

