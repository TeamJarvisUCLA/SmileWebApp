package ve.smile.administracion.portalweb.apk_movil.viewmodels;

import java.util.ArrayList;
import java.util.List;

import karen.core.dialog.generic.data.DialogData;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Multimedia;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class VM_apkMovilIndex {

	private List<Multimedia> imagenPrincipal;

	public List<Multimedia> getImagenPrincipal() {
		if (this.imagenPrincipal == null) {
			this.imagenPrincipal = new ArrayList<>();
		}
		this.imagenPrincipal.clear();
		if (this.imagenPrincipal.isEmpty()) {

			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.consultarMultimediaTipo(1,
							TipoMultimediaEnum.APK.ordinal());

			this.imagenPrincipal.addAll(payloadMultimediaResponse.getObjetos());
		}

		return imagenPrincipal;
	}

	public void setImagenPrincipal(List<Multimedia> imagenPrincipal) {
		this.imagenPrincipal = imagenPrincipal;
	}

	@Init(superclass = true)
	public void childInit() {
	}

	@Command
	public void formNewMultimedia() {
		DialogData dialogData = new DialogData();
		Multimedia multimedia = new Multimedia();
		dialogData.put("multimedia", multimedia);
		UtilDialog
				.showDialog(
						"/views/desktop/administracion/portalweb/apkMovil/apkMovil.zul",
						dialogData);
	}

	@Command
	public void formEditMultimedia(
			@BindingParam("multimedia") Multimedia multimedia) {
		DialogData dialogData = new DialogData();
		dialogData.put("multimedia", multimedia);
		UtilDialog
				.showDialog(
						"/views/desktop/administracion/portalweb/apkMovil/apkMovil.zul",
						dialogData);
	}

	@GlobalCommand
	@NotifyChange("imagenPrincipal")
	public void refreshMultimedias() {
	}
}
