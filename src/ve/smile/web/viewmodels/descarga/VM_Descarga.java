package ve.smile.web.viewmodels.descarga;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zkmax.zul.Filedownload;

import ve.smile.consume.services.S;
import ve.smile.dto.Configuracion;
import ve.smile.enums.PropiedadEnum;
import ve.smile.payload.response.PayloadConfiguracionResponse;

public class VM_Descarga {

	private List<Configuracion> infoMovil;
	private List<Configuracion> apk;

	private Configuracion configuracionMovil;
	private Configuracion configuracionApk;

	@Init(superclass = true)
	public void childInit() {
		PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.INFO_MOVIL
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponse)) {

			if (!payloadConfiguracionResponse.getObjetos().isEmpty()) {
				this.setConfiguracionMovil(payloadConfiguracionResponse
						.getObjetos().get(0));
			}
		}
		
		PayloadConfiguracionResponse payloadConfiguracionResponse2 = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.APK
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponse2)) {

			if (!payloadConfiguracionResponse2.getObjetos().isEmpty()) {
				this.setConfiguracionApk(payloadConfiguracionResponse2
						.getObjetos().get(0));
			}
		}

	}

	public List<Configuracion> getInfoMovil() {
		if (this.infoMovil == null) {
			this.infoMovil = new ArrayList<>();
		}
		if (this.infoMovil.isEmpty()) {
			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.consultarConfiguracionPropiedad(PropiedadEnum.INFO_MOVIL
							.ordinal());
			this.infoMovil.addAll(payloadConfiguracionResponse.getObjetos());
		}
		return infoMovil;
	}

	public List<Configuracion> getApk() {
		if (this.apk == null) {
			this.apk = new ArrayList<>();
		}
		if (this.apk.isEmpty()) {
			PayloadConfiguracionResponse payloadConfiguracionResponse = S.ConfiguracionService
					.consultarConfiguracionPropiedad(PropiedadEnum.APK
							.ordinal());
			this.apk.addAll(payloadConfiguracionResponse.getObjetos());
		}
		return apk;
	}

	public Configuracion getConfiguracionMovil() {
		return configuracionMovil;
	}

	public void setConfiguracionMovil(Configuracion configuracionMovil) {
		this.configuracionMovil = configuracionMovil;
	}

	public Configuracion getConfiguracionApk() {
		return configuracionApk;
	}

	public void setConfiguracionApk(Configuracion configuracionApk) {
		this.configuracionApk = configuracionApk;
	}
	
	@Command
	public void descargaApk() {
		try {
			Filedownload.save(getConfiguracionApk().getFkMultimedia().getUrl(), getConfiguracionApk().getDescripcion());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cambiar en el campo de descripcion en la tabla de configuracion por: application/vnd.android.package-archive
		
	}

}
