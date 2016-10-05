package ve.smile.gestion.voluntariado.voluntario.viewmodels;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Persona;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import ve.smile.seguridad.enums.SexoEnum;
import ve.smile.seguridad.enums.OperacionEnum;
import lights.core.enums.TypeQuery;
import lights.smile.util.UtilMultimedia;

public class VM_VoluntarioFormBasic extends VM_WindowForm implements UploadImageSingle 
{
	// Objetos locales
	private Estado estado;
	private Persona persona;
	
	// Datos
	private Date fechaEgreso;
	private Date fechaIngreso;
	private Date fechaNacimiento;

	// Manejo de multimedia
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImagen;
	
	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;
	
	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;
	
	@Init(superclass = true)
	public void childInit()
	{
		this.setPersona(this.getVoluntario().getFkPersona());
		if (this.persona.getSexo() != null)
		{
			this.setSexoEnum(SexoEnum.values()[this.persona.getSexo()]);
		}
		if (this.persona.getTipoPersona() != null)
		{
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[this.persona.getTipoPersona()]);
		}
		if (this.getVoluntario().getFkPersona() != null && this.getVoluntario().getFkPersona().getFkMultimedia() != null)
		{
			this.setUrlImagen(this.getVoluntario().getFkPersona().getFkMultimedia().getUrl());
		}
		else
		{
			this.getPersona().setFkMultimedia(new Multimedia());
			this.setUrlImagen("/Smile/default-image.jpg");
		}
	}
	
	// ENUM TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum()
	{
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum)
	{
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPersona().setTipoPersona(tipoPersonaEnum.ordinal());
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
	
	// ENUM SEXO
	public List<SexoEnum> getSexoEnums()
	{ 	
		if (this.sexoEnums == null)
		{
			this.sexoEnums = new ArrayList<>();
		}
		if (this.sexoEnums.isEmpty())
		{
			for (SexoEnum sexoEnum : SexoEnum.values())
			{
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}
	
	public void setSexoEnums(List<SexoEnum> sexoEnums)
	{
		this.sexoEnums = sexoEnums;
	}
	
	public SexoEnum getSexoEnum()
	{
		return sexoEnum;
	}
	
	public void setSexoEnum(SexoEnum sexoEnum)
	{
		this.sexoEnum = sexoEnum;
		getVoluntario().getFkPersona().setSexo(sexoEnum.ordinal()); 
	}

	// CIUDADES
	public List<Ciudad> getCiudades()
	{
		if (this.ciudades == null)
		{
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}
	
	// ESTADOS
	public Estado getEstado()
	{
		return estado;
	}

	public void setEstado(Estado estado)
	{
		this.estado = estado;
	}

	public List<Estado> getEstados()
	{
		if (this.estados == null)
		{
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty())
		{
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse))
			{
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}

	public void setEstados(List<Estado> estados)
	{
		this.estados = estados;
	}
	
	// Filtra las ciudades al seleccionar el estado
	@Command("changeEstado")
	@NotifyChange({ "ciudades", "persona" })
	public void changeEstado()
	{
		this.getCiudades().clear();
		this.getPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();
		criterios.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse))
		{
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}
	
	// PERSONA
	public Persona getPersona()
	{
		return persona;
	}

	public void setPersona(Persona persona)
	{
		if (persona == null)
		{
			persona = new Persona();
		}
		this.persona = persona;
	}
	
	// FECHAS
	public Date getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
		this.getPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso()
	{
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso)
	{
		this.fechaIngreso = fechaIngreso;
		this.getVoluntario().setFechaIngreso(fechaIngreso.getTime());
	}
	
	public Date getFechaEgreso()
	{
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso)
	{
		this.fechaEgreso = fechaEgreso;
		this.getVoluntario().setFechaEgreso(fechaEgreso.getTime());
	}
	
	// MULTIMEDIA
	public String getNameImage()
	{
		return nameImage;
	}

	public void setNameImage(String nameImage)
	{
		this.nameImage = nameImage;
	}

	public String getExtensionImage()
	{
		return extensionImage;
	}

	public void setExtensionImage(String extensionImage)
	{
		this.extensionImage = extensionImage;
	}
	
	public String getUrlImagen()
	{
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen)
	{
		this.urlImagen = urlImagen;
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	@Override
	public void onRemoveImageSingle(String idUpload)
	{
		bytes = null;
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
			if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage.lastIndexOf(".") + 1)))
			{
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN.ordinal());
				multimedia.setUrl(new StringBuilder().append("/Smile/Patrocinador/").append(nameImage).toString());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(nameImage.substring(this.nameImage.lastIndexOf(".") + 1)).ordinal());
				multimedia.setDescripcion("Imagen del voluntario");
				System.out.println(multimedia.getDescripcion());
				System.out.println(multimedia.getExtension());
				this.getPersona().setFkMultimedia(multimedia);
			}
			else
			{
				this.getPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
			}
		}
	}

	private BufferedImage loadImage() throws Exception
	{
		try
		{
			Path path = Paths.get(this.getUrlImagen());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImagen()));
		}
		catch (Exception e)
		{
			Path path = Paths.get("/Smile/default-image.jpg");
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File("/Smile/default-image.jpg"));
		}
	}
	
	// OPERACIONES DEL FORMULARIO

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
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.incluir(getVoluntario());
			if (!UtilPayload.isOK(payloadVoluntarioResponse))
			{
				Alert.showMessage(payloadVoluntarioResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(getVoluntario());
			if (!UtilPayload.isOK(payloadVoluntarioResponse))
			{
				Alert.showMessage(payloadVoluntarioResponse);
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

	public Voluntario getVoluntario()
	{
		return (Voluntario) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateString(getVoluntario().getFkPersona().getIdentificacion(), "Cédula", 35);
			UtilValidate.validateString(getVoluntario().getFkPersona().getNombre(), "Nombre", 150);
			UtilValidate.validateString(getVoluntario().getFkPersona().getApellido(), "Apellido", 150);
			UtilValidate.validateNull(getVoluntario().getFkPersona().getFkCiudad().getIdCiudad(), "Ciudad");
			UtilValidate.validateNull(getVoluntario().getFkPersona().getFkMultimedia().getIdMultimedia(), "Imagen");
			// UtilValidate.validateInteger(getVoluntario().getFkPersona().getSexo(), "Sexo", OPERATOR , 3);
			// UtilValidate.validateDate(getVoluntario().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", validateOperator, date_8601, formatToShow);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTelefono1(), "Teléfono 1", 25);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTelefono2(), "Teléfono 2", 25);
			UtilValidate.validateString(getVoluntario().getFkPersona().getDireccion(), "Direccion", 250);
			UtilValidate.validateString(getVoluntario().getFkPersona().getTwitter(), "Twitter", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getInstagram(), "Instagram", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getLinkedin(), "LinkedIn", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getSitioWeb(), "Sitio web", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getFax(), "Fax", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getCorreo(), "Correo", 100);
			UtilValidate.validateString(getVoluntario().getFkPersona().getFacebook(), "Facebook", 100);
			// UtilValidate.validateInteger(getVoluntario().getFkPersona().getTipoPersona(), "Tipo de persona", 100);
			// UtilValidate.validateDate(getVoluntario().getFechaIngreso(), "Fecha de ingreso", validateOperator, date_8601, formatToShow);
			// UtilValidate.validateDate(getVoluntario().getFechaEgreso(), "Fecha de egreso", validateOperator, date_8601, formatToShow);			
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
