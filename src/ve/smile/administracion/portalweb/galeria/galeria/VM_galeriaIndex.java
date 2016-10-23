package ve.smile.administracion.portalweb.galeria.galeria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Album;
import ve.smile.dto.Multimedia;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class VM_galeriaIndex{
	
	private List<Multimedia> multimediaGaleria;
	
	public List<Multimedia> getMultimediaGaleria() {
		if (this.multimediaGaleria == null) {
			this.multimediaGaleria = new ArrayList<>();
		}
		this.multimediaGaleria.clear();
		if (this.multimediaGaleria.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(100, TipoMultimediaEnum.GALERIA.ordinal());

			this.multimediaGaleria.addAll(payloadMultimediaResponse.getObjetos());
			Collections.reverse(this.multimediaGaleria);
		}

		return multimediaGaleria;
	}

	public void setMultimediaGaleria(List<Multimedia> multiemdiaGaleria) {
		this.multimediaGaleria = multiemdiaGaleria;
	}

	@Init(superclass = true)
	public void childInit() {
	}
		
	@Command
	public void formNewMultimedia() {
			DialogData dialogData = new DialogData();
			Multimedia multimedia = new Multimedia();
			dialogData.put("multimedia", multimedia);
			UtilDialog.showDialog("/views/desktop/administracion/portalweb/galeria/galeria/formMultimedia.zul", dialogData);
	}
	
	@Command
	public void formEditMultimedia(@BindingParam("multimedia") Multimedia multimedia) {
			DialogData dialogData = new DialogData();
			dialogData.put("multimedia", multimedia);
			UtilDialog.showDialog("/views/desktop/administracion/portalweb/galeria/galeria/formMultimedia.zul", dialogData);
	}

	@GlobalCommand
	@NotifyChange("multimediaGaleria")
	public void refreshMultimedias(){}
}
