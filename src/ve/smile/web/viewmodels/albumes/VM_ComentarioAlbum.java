package ve.smile.web.viewmodels.albumes;

import java.util.Date;

import karen.core.crux.session.DataCenter;
import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.ComentarioAlbum;
import ve.smile.dto.Comunidad;
import ve.smile.dto.Configuracion;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadConfiguracionResponse;

public class VM_ComentarioAlbum extends VM_WindowDialog {

	private Comunidad comunidad = new Comunidad();
	private ComentarioAlbum comentarioAlbum = new ComentarioAlbum();
	private Integer calificacion;
	private Configuracion confComentarAlbum =  new Configuracion();
	boolean emailComentarAlbum = false;
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_COMENTARIO_ALBUM
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponse)) {
			if (!payloadConfiguracionResponse.getObjetos().isEmpty()) {
				this.confComentarAlbum.setValor(payloadConfiguracionResponse
						.getObjetos().get(0).getValor());
			}else{
				this.confComentarAlbum.setValor("false");
			}
			
		}
	}
	
	
	public Integer getCalificacion() {
		return calificacion;
	}
	@NotifyChange
	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	private long myFecha;

	public Comunidad getComunidad() {
		return comunidad;
	}
	
	public Album getalbum() {
		return (Album) DataCenter.getEntity();
	}

	public void setComunidad(Comunidad comunidad) {
		if (comunidad == null) {
			comunidad = new Comunidad();
		}
		this.comunidad = comunidad;
	}

	public long getMyFecha() {
		myFecha = new Date().getTime();
		return myFecha;
	}

	public void setMyFecha(long myFecha) {
		this.myFecha = myFecha;
	}

	public ComentarioAlbum getComentarioAlbum() {
		return comentarioAlbum;
	}

	public void setComentarioAlbum(ComentarioAlbum comentarioAlbum) {
		if (comentarioAlbum == null) {
			comentarioAlbum = new ComentarioAlbum();
		}
		this.comentarioAlbum = comentarioAlbum;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}
	
	@Command
	public void changeValor(@BindingParam("valor") int valor){
		this.comentarioAlbum.setPuntuacion(valor);
	}

	public void limpiar() {
		this.setComunidad(new Comunidad());
		this.setComentarioAlbum(new ComentarioAlbum());
		BindUtils.postNotifyChange(null, null, this, "comentarioAlbum");
		BindUtils.postNotifyChange(null, null, this, "comunidad");
		Clients.evalJavaScript("f_star();");
	}

	public boolean isEmailComentarAlbum() {
		if(confComentarAlbum.getValor().equals("true")){
			this.emailComentarAlbum = true;
		}
		return emailComentarAlbum;
	}
}
