package ve.smile.datos.parametros.patrocinador.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Ciudad;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_PatrocinadorFormBasic extends VM_WindowForm implements UploadImageSingle
{

	private List<Ciudad> ciudades;
	
	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;
	
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
	}
	
	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum)
	{
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPatrocinador().getFkPersona().setTipoPersona(tipoPersonaEnum.ordinal());
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums()
	{
		if (this.tipoPersonaEnums == null)
		{
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty())
		{
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values())
			{
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums)
	{
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

	public List<Ciudad> getCiudades()
	{
		if (this.ciudades == null)
		{
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty())
		{
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarTodos();

			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}
	
	@Override
	public BufferedImage getImageContent()
	{
		try
		{
			return loadImage();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload)
	{
		org.zkoss.util.media.Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image)
		{
			bytes = media.getByteData();
			this.nameImage = media.getName();
			this.extensionImage = media.getFormat();
		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload)
	{
		bytes = null;
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	private BufferedImage loadImage() throws Exception
	{
		try
		{
			Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
			Path path = Paths.get("C:/imagenes/u_" + idUser);
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File("C:/imagenes/u_" + idUser));
		}
		catch (Exception e)
		{
			Path path = Paths.get("/home/conamerica97/eclipseKepler/imagene/default");
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagen/default"));
		}
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum)
	{
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if (!isFormValidated())
		{
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService.incluir(getPatrocinador());
			if (!UtilPayload.isOK(payloadPatrocinadorResponse))
			{
				Alert.showMessage(payloadPatrocinadorResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService.modificar(getPatrocinador());
			if (!UtilPayload.isOK(payloadPatrocinadorResponse))
			{
				Alert.showMessage(payloadPatrocinadorResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum)
	{
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum)
	{
		return actionSalir(operacionEnum);
	}

	public Patrocinador getPatrocinador()
	{
		return (Patrocinador) DataCenter.getEntity();
	}

	public boolean isFormValidated()
 	{
 		try
 		{
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getIdentificacion(), "Cedula", 35);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getNombre(), "Nombre", 150);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getApellido(), "Apellido", 150);
 			UtilValidate.validateNull(getPatrocinador().getFkPersona().getFkCiudad().getIdCiudad(), "Ciudad");
 			UtilValidate.validateNull(getPatrocinador().getFkPersona().getFkMultimedia().getIdMultimedia(), "Imagen");
 			// UtilValidate.validateInteger(getPatrocinador().getFkPersona().getSexo(), "Sexo", OPERATOR , 3);
 			// UtilValidate.validateDate(getPatrocinador().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", validateOperator, date_8601, formatToShow);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getTelefono1(), "Telfono 1", 25);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getTelefono2(), "Telfono 2", 25);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getDireccion(), "Direccion", 250);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getTwitter(), "Twitter", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getInstagram(), "Instagram", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getLinkedin(), "LinkedIn", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getSitioWeb(), "Sitio web", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getFax(), "Fax", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getCorreo(), "Correo", 100);
 			UtilValidate.validateString(getPatrocinador().getFkPersona().getFacebook(), "Facebook", 100);
 			// UtilValidate.validateInteger(getPatrocinador().getFkPersona().getTipoPersona(), "Tipo de persona", 100);
 			// UtilValidate.validateDate(getPatrocinador().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
 			// UtilValidate.validateDate(getPatrocinador().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
 			return true;
 		}
 		catch (Exception e)
 		{
 			Alert.showMessage(e.getMessage());
 			return false;
 		}
	}

}
