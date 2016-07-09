package lights.smile.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import lights.seguridad.enums.OperacionEnum;
import lights.smile.consume.services.S;
import lights.smile.payload.response.PayloadModeloResponse;
import lights.smile.dto.Modelo;

public class VMVModelo extends VM_WindowForm {

	private byte[] bytesRostro = null;
	private byte[] bytesCuerpo = null;
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadModeloResponse payloadModeloResponse =
					S.ModeloService.incluir(getModelo());

			Alert.showMessage(payloadModeloResponse);
			
			if(!UtilPayload.isOK(payloadModeloResponse)) {
				return true;
			}
			
			getModelo().setIdModelo(((Double) payloadModeloResponse.getInformacion("id")).intValue());
			
			guardarImagenes();

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadModeloResponse payloadModeloResponse =
					S.ModeloService.modificar(getModelo());

			Alert.showMessage(payloadModeloResponse);
			
			if(!UtilPayload.isOK(payloadModeloResponse)) {
				return true;
			}
			
			guardarImagenes();

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}

	public void guardarImagenes() {
		
		Integer idModelo = getModelo().getIdModelo();
		
		if (bytesRostro != null) {
			try {
				FileUtils.writeByteArrayToFile(
						new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_r_" + idModelo), bytesRostro);
			} catch (IOException e) {
				UtilDialog.showMessageBoxError("Ha ocurrido un error al guardar la imágen del rostro");
			}
		} else {
			new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_r_" + idModelo).delete();
		}
		
		if (bytesCuerpo != null) {
			try {
				FileUtils.writeByteArrayToFile(
						new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_c_" + idModelo), bytesCuerpo);
			} catch (IOException e) {
				UtilDialog.showMessageBoxError("Ha ocurrido un error al guardar la imágen del cuerpo completo");
			}
		} else {
			new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_c_" + idModelo).delete();
		}
		
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum) {
		DataCenter.reloadCurrentNodoMenu();

		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum) {
		return actionSalir(operacionEnum);
	}

	public Modelo getModelo() {
		return (Modelo) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getModelo().getNombres(), "Nombres", 195);
			
			UtilValidate.validateString(getModelo().getApellidos(), "Apellidos", 195);
			
			UtilValidate.validateString(getModelo().getEdad(), "Edad", 195);
			
			UtilValidate.validateString(getModelo().getColorPiel(), "Color de Piel", 195);
			
			UtilValidate.validateString(getModelo().getColorCabello(), "Color de Cabello", 195);
			
			UtilValidate.validateString(getModelo().getColorOjos(), "Color de Ojos", 195);
			
			UtilValidate.validateString(getModelo().getEstatura(), "Estatura", 195);
			
			UtilValidate.validateString(getModelo().getPeso(), "peso", 195);
			
			UtilValidate.validateString(getModelo().getTalla(), "Talla", 195);
			
			UtilValidate.validateString(getModelo().getActividades(), "Actividades", 195);
			
			UtilValidate.validateString(getModelo().getNumero(), "Número", 195);
			
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			
			return false;
		}
	}

	public void setFechaNacimiento(Timestamp asignacion) {
		getModelo().setFechaNacimiento(asignacion.getTime());
	}
	
	public Timestamp getFechaNacimiento() {
		if (getModelo().getFechaNacimiento() == null) {
			getModelo().setFechaNacimiento(Calendar.getInstance().getTimeInMillis());
		}
		return new Timestamp(getModelo().getFechaNacimiento());
	}
	
	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();
		
        if (media instanceof org.zkoss.image.Image) {
        	if (idUpload.equals("1")) {
        		bytesRostro = media.getByteData();
        	} else {
        		bytesCuerpo = media.getByteData();
        	}
        	
        }
	}
	
	@Override
	public void onRemoveImageSingle(String idUpload) {
		if (idUpload.equals("1")) {
    		bytesRostro = null;
    	} else {
    		bytesCuerpo = null;
    	}
	}

	//FotoRostro
	
	public BufferedImage getImageContent() {
		try {
			return loadImage();
		} catch (Exception e) {
			return null;
		}
	}
	
	private BufferedImage loadImage() throws Exception {
		try {
			Integer idModelo = getModelo().getIdModelo();
			
			if (idModelo == null) {
				return null;
			}
			
			Path pathRostro = Paths.get("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_r_" + idModelo);
			
			bytesRostro = Files.readAllBytes(pathRostro);
			
			return ImageIO.read(new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_r_" + idModelo));
		} catch (Exception e) {
			return null;
		}
	}
	
	//FotoCuerpo
	public BufferedImage getImageContent2() {
		try {
			return loadImage2();
		} catch (Exception e) {
			return null;
		}
	}
	
	private BufferedImage loadImage2() throws Exception {
		try {
			Integer idModelo = getModelo().getIdModelo();
			
			if (idModelo == null) {
				return null;
			}
			
			Path pathCuerpo = Paths.get("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_c_" + idModelo);
			
			bytesCuerpo = Files.readAllBytes(pathCuerpo);
			
			return ImageIO.read(new File("/home/tranx6/CAJAFUERTE/Promo54/Imagenes/m_c_" + idModelo));
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] getBytesRostro() {
		return bytesRostro;
	}

	public void setBytesRostro(byte[] bytesRostro) {
		this.bytesRostro = bytesRostro;
	}

	public byte[] getBytesCuerpo() {
		return bytesCuerpo;
	}

	public void setBytesCuerpo(byte[] bytesCuerpo) {
		this.bytesCuerpo = bytesCuerpo;
	}
}
