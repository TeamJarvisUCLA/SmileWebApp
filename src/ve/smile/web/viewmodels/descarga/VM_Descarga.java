package ve.smile.web.viewmodels.descarga;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import lights.smile.util.Zki;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zkmax.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.Multimedia;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;

public class VM_Descarga {

	private List<Multimedia> imagenPrincipal;

	@Init(superclass = true)
	public void childInit() {

	}

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

	@Command
	public void descargaApk() {
		try {

			Filedownload.save(Zki.getPath()
					+ getImagenPrincipal().get(0).getUrl(),
					getImagenPrincipal().get(0).getUrl());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// cambiar en el campo de descripcion en la tabla de configuracion por:
		// application/vnd.android.package-archive

	}

}
