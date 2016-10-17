package ve.smile.gestion.voluntariado.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Estado;
import ve.smile.dto.Profesion;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.seguridad.enums.SexoEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import app.UploadImageSingle;

public class VM_RegistroVoluntarioIndex extends VM_WindowWizard<Voluntario> implements UploadImageSingle
{
	private Estado estado;
	private Voluntario voluntario = new Voluntario();
	
	private Date fechaNacimiento = new Date();
	private Date fechaIngreso = new Date();

	private List<Ciudad> ciudades;
	private List<Estado> estados;
	private List<SexoEnum> sexoEnums;
	private List<TipoPersonaEnum> tipoPersonaEnums;
	
	private List<Profesion> profesiones;
	private List<Profesion> voluntarioProfesiones;
	private Set <Profesion> profesionesSeleccionadas;
	private Set <Profesion> voluntarioProfesionesSeleccionadas;

	private SexoEnum sexoEnum;
	private TipoPersonaEnum tipoPersonaEnum;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String typeMedia;

	@Init(superclass = true)
	public void childInit()
	{
		voluntario = new Voluntario();
		estado = new Estado(); 
		fechaNacimiento = new Date();
		fechaIngreso = new Date();
		
		// PROFESIONES
		if (this.getProfesiones().isEmpty())
		{
			PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService.consultarTodos();
			if (!UtilPayload.isOK(payloadProfesionResponse))
			{
				Alert.showMessage(payloadProfesionResponse);
			}
			else
			{
				profesiones.addAll(payloadProfesionResponse.getObjetos());
			}		
		}
	}

	// VOLUNTARIO
	public Voluntario getVoluntario()
	{
		return voluntario;
	}

	public void setVoluntario(Voluntario voluntario)
	{
		this.voluntario = voluntario;
	}

	// ENUM SEXO
	public SexoEnum getSexoEnum()
	{
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum)
	{
		this.sexoEnum = sexoEnum;
		selectedObject.getFkPersona().setSexo(this.sexoEnum.ordinal());
	}

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

	// ENUM TIPO PERSONA
	public TipoPersonaEnum getTipoPersonaEnum()
	{
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum)
	{
		this.tipoPersonaEnum = tipoPersonaEnum;
		selectedObject.getFkPersona().setTipoPersona(this.tipoPersonaEnum.ordinal());
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

	// CIUDADES
	public List<Ciudad> getCiudades()
	{
		if (this.ciudades == null)
		{
			this.ciudades = new ArrayList<>();
		}
		if (this.ciudades.isEmpty())
		{
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarTodos();
			if (!UtilPayload.isOK(payloadCiudadResponse))
			{
				Alert.showMessage(payloadCiudadResponse);
			}
			this.ciudades.addAll(payloadCiudadResponse.getObjetos());
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades)
	{
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
	@NotifyChange({"ciudades"})
	public void changeEstado()
	{
		this.getCiudades().clear();
		this.getSelectedObject().getFkPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();
		criterios.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse))
		{
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}
	
	// FECHAS
	public Date getFechaNacimiento()
	{
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento (Date fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
		this.getSelectedObject().getFkPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}
	
	public Date getFechaIngreso()
	{
		return fechaIngreso;
	}

	public void setFechaIngreso (Date fechaIngreso)
	{
		this.fechaIngreso = fechaIngreso;
		this.getSelectedObject().setFechaIngreso(fechaIngreso.getTime()); 
	}
	
	// MÉTODOS DE LAS LISTAS
	public boolean disabledProfesion(Profesion profesion)
	{
		return this.getVoluntarioProfesiones().contains(profesion);
	}
		
	public List<Profesion> getProfesiones()
	{
		if (this.profesiones == null)
		{
			this.profesiones = new ArrayList<>();
		}
		return profesiones;
	}

	public void setProfesiones(List<Profesion> profesiones)
	{
		this.profesiones = profesiones;
	}

	public Set<Profesion> getProfesionesSeleccionadas()
	{
		if (this.profesionesSeleccionadas == null)
		{
			this.profesionesSeleccionadas = new HashSet<>();
		}
		return profesionesSeleccionadas;
	}

	public void setProfesionesSeleccionadas(Set<Profesion> profesionesSeleccionadas)
	{
		this.profesionesSeleccionadas = profesionesSeleccionadas;
	}
		
	public List<Profesion> getVoluntarioProfesiones()
	{
		if (this.voluntarioProfesiones == null)
		{
			voluntarioProfesiones = new ArrayList<>();
		}
		return voluntarioProfesiones;
	}

	public void setVoluntarioProfesiones (List<Profesion> voluntarioProfesiones)
	{
		this.voluntarioProfesiones = voluntarioProfesiones;
	}

	public Set<Profesion> getVoluntarioProfesionesSeleccionadas()
	{
		if (this.voluntarioProfesionesSeleccionadas == null)
		{
			this.voluntarioProfesionesSeleccionadas = new HashSet<>();
		}
		return voluntarioProfesionesSeleccionadas;
	}

	public void setVoluntarioProfesionesSeleccionadas(Set<Profesion> voluntarioProfesionesSeleccionadas)
	{
		this.voluntarioProfesionesSeleccionadas = voluntarioProfesionesSeleccionadas;
	}
		
	@Command("agregarProfesiones")
	@NotifyChange({"profesiones", "voluntarioProfesiones", "profesionesSeleccionadas", "voluntarioProfesionesSeleccionadas"})
	public void agregarProfesiones()
	{
		if (this.getProfesionesSeleccionadas() != null && this.getProfesionesSeleccionadas().size() > 0)
		{
			this.getVoluntarioProfesiones().addAll(profesionesSeleccionadas);
			this.getProfesionesSeleccionadas().clear();
			this.getVoluntarioProfesionesSeleccionadas().clear();
		}
	}

	@Command("removerProfesiones")
	@NotifyChange({"profesiones", "voluntarioProfesiones", "profesionesSeleccionadas", "voluntarioProfesionesSeleccionadas"})
	public void removerProfesiones()
	{
		if (this.getVoluntarioProfesionesSeleccionadas() != null && this.getVoluntarioProfesionesSeleccionadas().size() > 0)
		{
			this.getVoluntarioProfesiones().removeAll(voluntarioProfesionesSeleccionadas);
			this.getProfesionesSeleccionadas().clear();
			this.getVoluntarioProfesionesSeleccionadas().clear();
		}
	}
		
	// MÉTODOS DEL WIZARD
	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep()
	{
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard2.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard3.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(3, listOperacionWizard3);
		
		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard4.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard4.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(4, listOperacionWizard4);
		
		List<OperacionWizard> listOperacionWizard5 = new ArrayList<OperacionWizard>();
		listOperacionWizard5.add(OperacionWizardHelper.getPorType(OperacionWizardEnum.FINALIZAR));
		botones.put(5, listOperacionWizard5);
		
		return botones;
	}

	@Override
	public List<String> getIconsToStep()
	{
		List<String> iconos = new ArrayList<String>();
		iconos.add("fa fa-user");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep()
	{
		List<String> urls = new ArrayList<String>();
		urls.add("views/desktop/gestion/voluntariado/registro/selectVoluntario.zul");
		urls.add("views/desktop/gestion/voluntariado/registro/datosPersonales.zul");
		urls.add("views/desktop/gestion/voluntariado/registro/datosContacto.zul");
		urls.add("views/desktop/gestion/voluntariado/registro/listaProfesiones.zul");
		urls.add("views/desktop/gestion/voluntariado/registro/registroCompletado.zul");
		return urls;
	}
	
	// ATRAS
	@Override
	public String executeAtras(Integer currentStep)
	{
		goToPreviousStep();
		return "";
	}
	
	// CANCELAR
	@Override
	public String executeCancelar(Integer currentStep)
	{
		restartWizard();
		return "";
	}

	// SIGUIENTE
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep)
	{
		if (currentStep == 1)
		{
			if (selectedObject == null)
			{
				return "E:Error Code 5-Debe seleccionar un <b>voluntario</b>";
			}
		}
		if (currentStep == 2)
		{
			try
			{
				UtilValidate.validateInteger(this.getVoluntarioSelected().getFkPersona().getTipoPersona(), "Tipo de persona", ValidateOperator.LESS_THAN, 1);
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getIdentificacion(), "Cédula", 35);
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getNombre(), "Nombre", 150);
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getApellido(), "Apellido", 150);
				UtilValidate.validateInteger(this.getVoluntarioSelected().getFkPersona().getSexo(), "Sexo", ValidateOperator.LESS_THAN, 2);
				UtilValidate.validateDate(this.getVoluntarioSelected().getFkPersona().getFechaNacimiento(), "Fecha de nacimiento", ValidateOperator.LESS_THAN, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "dd/MM/yyyy");
				UtilValidate.validateNull(this.getVoluntarioSelected().getFkPersona().getFkCiudad(), "Ciudad");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				UtilValidate.validateDate(this.getVoluntarioSelected().getFechaIngreso(), "Fecha de ingreso", ValidateOperator.LESS_THAN, new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()),"dd/MM/yyyy");
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getDireccion(), "Dirección", 250);
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}
		if (currentStep == 3)
		{
			try
			{
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getTelefono1(), "Teléfono 1", 25);
				UtilValidate.validateString(this.getVoluntarioSelected().getFkPersona().getCorreo(), "Correo", 100);
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}
		return "";
	}
	
	@Override
	public String executeSiguiente(Integer currentStep)
	{
		if (currentStep == 2)
		{
			this.setSexoEnum(SexoEnum.values()[selectedObject.getFkPersona().getSexo()]);
			this.setTipoPersonaEnum(TipoPersonaEnum.values()[selectedObject.getFkPersona().getTipoPersona()]);
		}
		
		if (currentStep == 3)
		{
			
		}
		if (currentStep == 4)
		{
			// MULTIMEDIA
			if (bytes != null)
			{
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService.incluir(multimedia);
				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse.getInformacion("id")).intValue());
				Zki.save(Zki.PERSONAS, this.getVoluntarioSelected().getFkPersona().getIdPersona(), extensionImage, bytes);
				this.getVoluntarioSelected().getFkPersona().setFkMultimedia(multimedia);
			}

			// PERSONA
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService.modificar(this.getVoluntario().getFkPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse))
			{
				Alert.showMessage(payloadPersonaResponse);
			}
									
			// PROFESIONES
			this.selectedObject.setProfesiones(new ArrayList<Profesion>());
			this.selectedObject.getProfesiones().clear();
			this.selectedObject.getProfesiones().addAll(this.getVoluntarioProfesiones());
			
			// VOLUNTARIO
			this.selectedObject.setEstatusVoluntario(EstatusVoluntarioEnum.ACTIVO.ordinal());
			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.modificar(this.selectedObject);
			if (UtilPayload.isOK(payloadVoluntarioResponse))
			{
				this.setSelectedObject(new Voluntario());
				this.setVoluntario(new Voluntario());
				//this.setProfesiones(new ArrayList<Profesiones>());
				this.getProfesionesSeleccionadas().clear();
				this.setVoluntarioProfesiones(new ArrayList<Profesion>());
				this.getVoluntarioProfesionesSeleccionadas().clear();
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "voluntario");
				BindUtils.postNotifyChange(null, null, this, "profesiones");
				BindUtils.postNotifyChange(null, null, this, "profesionesSeleccionadas");
				BindUtils.postNotifyChange(null, null, this, "voluntarioProfesiones");
				BindUtils.postNotifyChange(null, null, this, "voluntarioProfesionesSeleccionadas");
			}
		}
		goToNextStep();
		return "";
	}
	
	// FINALIZAR
	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep)
	{
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep)
	{
		restartWizard();
		return "";
	}

	// CARGA DE OBJETOS
	@Override
	public IPayloadResponse<Voluntario> getDataToTable(Integer cantidadRegistrosPagina, Integer pagina)
	{
		Map<String, String> criterios = new HashMap<>();
		EstatusVoluntarioEnum.POSTULADO.ordinal();
		criterios.put("estatusVoluntario", String.valueOf(EstatusVoluntarioEnum.POR_COMPLETAR.ordinal()));
		PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,	TypeQuery.EQUAL, criterios);
		return payloadVoluntarioResponse;
	}

	@Override
	public void comeIn(Integer currentStep)
	{
		if (currentStep == 1)
		{
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}
	
	// VOLUNTARIO SELECTED
	public Voluntario getVoluntarioSelected()
	{
		return (Voluntario) this.selectedObject;
	}
	
	// PROPIEDADES DEL MULTIMEDIA
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

	public String getUrlImage()
	{
		return urlImage;
	}

	public void setUrlImage(String urlImage)
	{
		this.urlImage = urlImage;
	}

	public String getTypeMedia()
	{
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia)
	{
		this.typeMedia = typeMedia;
	}

	@Override
	public BufferedImage getImageContent()
	{
		if (bytes != null)
		{
			try
			{
				return ImageIO.read(new ByteArrayInputStream(bytes));
			}
			catch (IOException e)
			{
				return null;
			}
		}

		if (urlImage != null)
		{
			bytes = Zki.getBytes(urlImage);
			return Zki.getBufferedImage(urlImage);
		}
		return null;
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload)
	{
		org.zkoss.util.media.Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image)
		{
			if (UtilMultimedia.validateImage(media.getName().substring(media.getName().lastIndexOf(".") + 1)))
			{
				this.extensionImage = media.getName().substring(media.getName().lastIndexOf(".") + 1);
				this.nameImage = new StringBuilder().append(Zki.PERSONAS).append(this.getVoluntarioSelected().getFkPersona().getIdPersona()).append(".").append(this.extensionImage).toString();
				this.bytes = media.getByteData();
				this.urlImage = new StringBuilder().append(Zki.PERSONAS).append(this.getVoluntarioSelected().getFkPersona().getIdPersona()).append(".").append(extensionImage).toString();
				this.typeMedia = media.getContentType();
			}
			else
			{
				this.getVoluntarioSelected().getFkPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}
		}
		else
		{
			this.getVoluntarioSelected().getFkPersona().setFkMultimedia(null);
			Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
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

}