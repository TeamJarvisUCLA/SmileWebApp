package ve.smile.web.viewmodels;

import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.Command;

import ve.smile.dto.Persona;

public class VM_AbrirPostulado {

	@Command
	public void abrir(){
		DialogData dialogData = new DialogData();

		
		
		UtilDialog.showDialog("/views/web/postulacionVoluntario.zul", dialogData);
	}
}
