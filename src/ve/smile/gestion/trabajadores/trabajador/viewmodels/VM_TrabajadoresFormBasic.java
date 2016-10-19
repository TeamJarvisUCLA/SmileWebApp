package ve.smile.gestion.trabajadores.trabajador.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Cargo;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Estado;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Profesion;
import ve.smile.dto.Trabajador;
import ve.smile.dto.Persona;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadCargoResponse;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadProfesionResponse;
import ve.smile.payload.response.PayloadTareaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_TrabajadoresFormBasic extends VM_WindowForm implements
		UploadImageSingle {

	private List<Ciudad> ciudades;

	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;

	private Date fechaNacimiento;
	private Date fechaIngreso;
	
	private Persona persona;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImagen;

	private Estado estado;
	private Cargo cargo;

	private List<Estado> estados;
	private List<Cargo> cargos;
	
	private List<Profesion> profesiones;
	private Set <Profesion> profesionesSeleccionadas;
	private List<Profesion> trabajadorProfesiones;
	private Set <Profesion> trabajadorProfesionesSeleccionadas;
	
	@Init(superclass = true)
	public void childInit() {		
		
		this.getCargos();
		
		if(this.getTrabajador().getIdTrabajador() != null ){
			if (getTrabajador().getFortalezas() == null
					|| getTrabajador().getFortalezas().size() == 0) {

				PayloadProfesionResponse payloadProfesionResponse = S.ProfesionService
						.consultarPorTrabajador(getTrabajador()
								.getIdTrabajador());

				if (!UtilPayload.isOK(payloadProfesionResponse)) {
					Alert.showMessage(payloadProfesionResponse);
				}
				
				getTrabajador().setProfesiones(payloadProfesionResponse.getObjetos());
			}
			
			
			this.getTrabajadorProfesiones().addAll(getTrabajador().getProfesiones());			
		}		
		
		
		if (this.getProfesiones().isEmpty()){
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
		
		this.setPersona(this.getTrabajador().getFkPersona());
		
		if (this.persona.getSexo() != null) {
			this.setSexoEnum(SexoEnum.values()[this.persona.getSexo()]);
			
			//De long a date
			Date fechaIng = new Date(this.getTrabajador().getFechaIngreso());
			Date fechaNac = new Date(this.getPersona().getFechaNacimiento());
			
			this.setFechaIngreso(fechaIng);
			this.setFechaNacimiento(fechaNac);
			this.getCiudades().add(this.getPersona().getFkCiudad());		
		}
		if (this.getPersona().getFkCiudad() != null) {
			this.setEstado(this.getPersona().getFkCiudad().getFkEstado());
			this.setCiudades(null);
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(this.getEstado().getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
		}
		if (this.getTrabajador().getFkPersona() != null
				&& this.getTrabajador().getFkPersona().getFkMultimedia() != null) {
			this.setUrlImagen(this.getTrabajador().getFkPersona()
					.getFkMultimedia().getUrl());
		} else {
			this.getPersona().setFkMultimedia(new Multimedia());
		}
				
}
	
	public boolean disabledProfesion(Profesion profesion)
	{
		return this.getTrabajadorProfesiones().contains(profesion);
	}
		
		//TODO: Sets and gets de profesiones
					
	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		if (this.estados == null) {
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty()) {
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadEstadoResponse)) {
				Alert.showMessage(payloadEstadoResponse);
			}
			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;
	}
	
	public Cargo getCargo() {
		return cargo;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public List<Cargo> getCargos() {
		if (this.cargos == null) {
			this.cargos = new ArrayList<>();
		}
		if (this.cargos.isEmpty()) {
			PayloadCargoResponse payloadCargoResponse = S.CargoService
					.consultarTodos();
			if (!UtilPayload.isOK(payloadCargoResponse)) {
				Alert.showMessage(payloadCargoResponse);
			}
			this.cargos.addAll(payloadCargoResponse.getObjetos());
		}
		return cargos;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		if (persona == null) {
			persona = new Persona();
		}
		this.persona = persona;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.getPersona().setFechaNacimiento(fechaNacimiento.getTime());
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
		this.getTrabajador().setFechaIngreso(fechaIngreso.getTime());
	}

	public String getNameImage() {
		return nameImage;
	}

	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	public String getExtensionImage() {
		return extensionImage;
	}

	public void setExtensionImage(String extensionImage) {
		this.extensionImage = extensionImage;
	}

	public List<SexoEnum> getSexoEnums() {
		if (this.sexoEnums == null) {
			this.sexoEnums = new ArrayList<>();
		}
		if (this.sexoEnums.isEmpty()) {
			for (SexoEnum sexoEnum : SexoEnum.values()) {
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums) {
		this.sexoEnums = sexoEnums;
	}

	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPersona().setSexo(sexoEnum.ordinal());
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	@Command("changeEstado")
	@NotifyChange({ "ciudades", "persona" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getPersona().setFkCiudad(null);
		Map<String, String> criterios = new HashMap<>();

		criterios
				.put("fkEstado.idEstado", String.valueOf(estado.getIdEstado()));
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultarCriterios(TypeQuery.EQUAL, criterios);
		if (!UtilPayload.isOK(payloadCiudadResponse)) {
			Alert.showMessage(payloadCiudadResponse);
		}
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}

	@Override
	public BufferedImage getImageContent() {
		try {
			return loadImage();
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		org.zkoss.util.media.Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image) {
			bytes = media.getByteData();
			this.nameImage = media.getName();
			this.extensionImage = media.getFormat();
			if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage
					.lastIndexOf(".") + 1))) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(new StringBuilder()
						.append("/Smile/Trabajador/").append(nameImage)
						.toString());
				multimedia.setExtension(UtilMultimedia
						.stringToExtensionEnum(
								nameImage.substring(this.nameImage
										.lastIndexOf(".") + 1)).ordinal());
				multimedia.setDescripcion("Imagen del trabajador.");
				System.out.println(multimedia.getDescripcion());
				System.out.println(multimedia.getExtension());
				this.getPersona().setFkMultimedia(multimedia);
			} else {
				this.getPersona().setFkMultimedia(null);
				Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

			}

		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload) {
		bytes = null;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	private BufferedImage loadImage() throws Exception {
		try {
			Path path = Paths.get(this.getUrlImagen());
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File(this.getUrlImagen()));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		this.getTrabajador().setFkPersona(persona);
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			
			Trabajador trabajador = this.getTrabajador();
			
			Multimedia multimedia = this.getPersona().getFkMultimedia();
			
			PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
					.incluir(multimedia);

			if (!UtilPayload.isOK(payloadMultimediaResponse)) {
				Alert.showMessage(payloadMultimediaResponse);
				return true;
			}

			multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
					.getInformacion("id")).intValue());
			this.getPersona().setFkMultimedia(multimedia);
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.incluir(this.getPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				return true;
			}
			this.getPersona().setIdPersona(
					((Double) payloadPersonaResponse.getInformacion("id"))
							.intValue());
			trabajador.setFkPersona(this.getPersona());
			
			trabajador.setFechaIngreso(this.getFechaIngreso().getTime());
			trabajador.setEstatusTrabajador(EstatusTrabajadorEnum.ACTIVO.ordinal());
			trabajador.setProfesiones(this.getTrabajadorProfesiones());
			
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.incluir(trabajador);
			if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
				Alert.showMessage(payloadTrabajadorResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			this.getTrabajador().getProfesiones().clear();
			this.getTrabajador().getProfesiones().addAll(this.getTrabajadorProfesiones());
			
			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.modificar(getTrabajador());
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.modificar(getPersona());
			
		if (!UtilPayload.isOK(payloadPersonaResponse)) {
			if (!UtilPayload.isOK(payloadTrabajadorResponse)) {
				Alert.showMessage(payloadTrabajadorResponse);
				return true;
			}
		}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
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

	public Trabajador getTrabajador() {
		return (Trabajador) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateNull(this.getTrabajador().getFkCargo(), "Cargo");
			UtilValidate.validateString(this.getPersona().getIdentificacion(),
					"C�dula/RIF", 35);
			UtilValidate.validateString(this.getPersona().getNombre(),
					"Nombre", 150);
			UtilValidate.validateString(this.getPersona().getApellido(),
					"Apellido", 150);
			UtilValidate.validateInteger(this.getPersona().getSexo(), "Sexo",
					ValidateOperator.LESS_THAN, 2);
			UtilValidate.validateDate(this.getPersona().getFechaNacimiento(),
					"Fecha de nacimiento", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
					"DD/MM/YYYY");

			UtilValidate.validateNull(this.getPersona().getFkCiudad(), "Ciudad");
			UtilValidate.validateDate(this.getTrabajador().getFechaIngreso(),
					"Fecha de ingreso", ValidateOperator.LESS_THAN,
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
					"DD/MM/YYYY");
			UtilValidate.validateString(this.getPersona().getDireccion(),
					"Direcci�n", 250);
			UtilValidate.validateNull(this.getPersona().getFkMultimedia(),
					"Imagen");
			UtilValidate.validateString(this.getPersona().getTelefono1(),
					"Tel�fono 1", 25);
			UtilValidate.validateString(this.getPersona().getCorreo(),
					"Correo", 100);

			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());
			return false;
		}
	}
	
		public List<Profesion> getProfesiones() {
			if(this.profesiones == null){
				this.profesiones = new ArrayList<>();
			}
			return profesiones;
		}

		public void setProfesiones(List<Profesion> profesiones) {
			this.profesiones = profesiones;
		}

		public Set<Profesion> getProfesionesSeleccionadas() {
			if(this.profesionesSeleccionadas == null){
				this.profesionesSeleccionadas = new HashSet<>();
			}
			return profesionesSeleccionadas;
		}

		public void setProfesionesSeleccionadas(Set<Profesion> profesionesSeleccionadas) {
			this.profesionesSeleccionadas = profesionesSeleccionadas;
		}

		public List<Profesion> getTrabajadorProfesiones() {
			if(this.trabajadorProfesiones == null){
				this.trabajadorProfesiones = new ArrayList<>();
			}			
			return trabajadorProfesiones;
		}

		public void setTrabajadorProfesiones(List<Profesion> trabajadorProfesiones) {
			this.trabajadorProfesiones = trabajadorProfesiones;
		}

		public Set<Profesion> getTrabajadorProfesionesSeleccionadas() {
			if(this.trabajadorProfesionesSeleccionadas == null){
				this.trabajadorProfesionesSeleccionadas = new HashSet<>();
			}			
			return trabajadorProfesionesSeleccionadas;
		}

		public void setTrabajadorProfesionesSeleccionadas(
				Set<Profesion> trabajadorProfesionesSeleccionadas) {
			this.trabajadorProfesionesSeleccionadas = trabajadorProfesionesSeleccionadas;
		}
		
		
		@Command("agregarProfesiones")
		@NotifyChange({"profesiones", "trabajadorProfesiones", "profesionesSeleccionadas", "trabajadorProfesionesSeleccionadas" })
		public void agregarProfesiones()
		{
			if (this.getProfesionesSeleccionadas() != null && this.getProfesionesSeleccionadas().size() > 0)
			{
				this.getTrabajadorProfesiones().addAll(profesionesSeleccionadas);
				this.getProfesionesSeleccionadas().clear();
				this.getTrabajadorProfesionesSeleccionadas().clear();
			}
		}

		@Command("removerProfesiones")
		@NotifyChange({"profesiones", "trabajadorProfesiones", "profesionesSeleccionadas", "trabajadorProfesionesSeleccionadas" })
		public void removerProfesiones()
		{
			if (this.getTrabajadorProfesionesSeleccionadas() != null && this.getTrabajadorProfesionesSeleccionadas().size() > 0)
			{
				this.getTrabajadorProfesiones().removeAll(trabajadorProfesionesSeleccionadas);
				this.getProfesionesSeleccionadas().clear();
				this.getTrabajadorProfesionesSeleccionadas().clear();
			}
		}
}
