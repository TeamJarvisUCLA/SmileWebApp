package ve.smile.web.postulacion.viewmodels;

import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.Command;

import ve.smile.dto.Persona;

public class VM_AbrirPostulado {

	@Command
	public void abrirPostularVoluntario(){
		DialogData dialogData = new DialogData();

		
		
		UtilDialog.showDialog("/views/web/postulacionVoluntario.zul", dialogData);
	}
	
	@Command
	public void abrirComentar(){
		DialogData dialogData = new DialogData();

		
		
		UtilDialog.showDialog("/views/web/comentarAlbum.zul", dialogData);
	}
	
	@Command
	public void abrirPostularPadrino(){
		DialogData dialogData = new DialogData();

		
		
		UtilDialog.showDialog("/views/web/postulacionPadrino.zul", dialogData);
	}
	
	@Command
	public void abrirFormPregunta(){
		DialogData dialogData = new DialogData();

		
		
		UtilDialog.showDialog("/views/web/formPregunta.zul", dialogData);
	}
}
