package ve.smile.gestion.ayudas.familiar_y_beneficiario;

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
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.form.data.WindowFormDialogData;
import karen.core.dialog.form.events.WindowFormDialogCloseEvent;
import karen.core.dialog.form.events.listeners.WindowFormDialogCloseListener;
import karen.core.dialog.generic.data.DialogData;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.generic.events.listeners.DialogCloseListener;
import karen.core.form.buttons.enums.OperacionFormEnum;
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
import ve.smile.dto.Ayuda;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Familiar;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Directorio;
import ve.smile.dto.Estado;
import ve.smile.dto.Motivo;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Familiar;
import ve.smile.dto.Familiar;
import ve.smile.dto.Persona;
import ve.smile.dto.TsPlan;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusBeneficiarioEnum;
import ve.smile.enums.EstatusFamiliarEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.enums.UrgenciaEnum;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.payload.response.PayloadFamiliarResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.seguridad.enums.SexoEnum;
import app.UploadImageSingle;

public class VM_FamiliarBeneficiarioIndex extends
		VM_WindowWizard<Familiar> implements
		UploadImageSingle  {
	
	private List<Ciudad> ciudades;

	private List<TipoPersonaEnum> tipoPersonaEnums;
	private TipoPersonaEnum tipoPersonaEnum;

	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;

	private Date fechaNacimiento;
	private Date fechaIngreso;
	private Persona persona;

	private Estado estado;

	private List<Estado> estados;
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;

	private String typeMedia;
	

	private Familiar familiar;

	private List<Motivo> motivos ;

	private List<Beneficiario> beneficiarios;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		familiar = new Familiar();
		persona = new  Persona();
		
		
	}
	
	public TipoPersonaEnum getTipoPersonaEnum() {
		return tipoPersonaEnum;
	}

	public void setTipoPersonaEnum(TipoPersonaEnum tipoPersonaEnum) {
		this.tipoPersonaEnum = tipoPersonaEnum;
		this.getPersona().setTipoPersona(tipoPersonaEnum.ordinal());
	}

	public List<TipoPersonaEnum> getTipoPersonaEnums() {
		if (this.tipoPersonaEnums == null) {
			this.tipoPersonaEnums = new ArrayList<>();
		}
		if (this.tipoPersonaEnums.isEmpty()) {
			for (TipoPersonaEnum tipoPersonaEnum : TipoPersonaEnum.values()) {
				this.tipoPersonaEnums.add(tipoPersonaEnum);
			}
		}
		return tipoPersonaEnums;
	}

	public void setTipoPersonaEnums(List<TipoPersonaEnum> tipoPersonaEnums) {
		this.tipoPersonaEnums = tipoPersonaEnums;
	}

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
		this.getFamiliar().setFechaIngreso(fechaIngreso.getTime());
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

	
	public void setMotivos(List<Motivo> motivos) {
		this.motivos = motivos;
	}
	
	public List<Motivo> getMotivos() {
		if (this.motivos == null) {
			this.motivos = new ArrayList<>();
		}
		if (this.motivos.isEmpty()) {
			PayloadMotivoResponse payloadMotivoResponse = S.MotivoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadMotivoResponse)) {
				Alert.showMessage(payloadMotivoResponse);
			}

			this.motivos.addAll(payloadMotivoResponse.getObjetos());
		}

		return motivos;
	}
	
	
	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	
	

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		
		/*OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "APROBAR", "Custom1", "fa fa-check-square-o", "green", "APROBAR");
		OperacionWizard operacionWizardCustom2 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM2.ordinal(), "RECHAZAR", "Custom2", "z-icon-times", "deep-orange", "RECHAZAR");
*/
		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);
		
		
		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(2, listOperacionWizard2);
		
		/*
		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(4, listOperacionWizard4);*/

		return botones;
	}
	
	@Command("dialogoBeneficiario")
	public void buscarBeneficiario() {
WindowFormDialogData<Beneficiario> windowFormDialogData = new WindowFormDialogData<Beneficiario>();
		
		windowFormDialogData.addWindowFormDialogCloseListeners(new WindowFormDialogCloseListener<Beneficiario>() {

			@Override
			public void onClose(WindowFormDialogCloseEvent<Beneficiario> windowFormDialogCloseEvent) {
				if (windowFormDialogCloseEvent.getOperacionFormEnum().equals(OperacionFormEnum.CANCELAR)) {
					return;
				}
				
				//incidenciasEventos.add(windowFormDialogCloseEvent.getEntity());
				
				//beneficiarios.add(windowFormDialogCloseEvent.getEntity());
				getBeneficiarios().add(windowFormDialogCloseEvent.getEntity());
				System.out.println(windowFormDialogCloseEvent.getEntity().getFkPersona().getNombre());
				refreshBeneficiario();
			}
		} );
		
		windowFormDialogData.setOperacionEnum(OperacionEnum.INCLUIR);


			UtilDialog.showDialog("views/desktop/gestion/ayudas/familiarYBeneficiario/dialogoBeneficiario.zul", windowFormDialogData);
			
		System.out.println("entro dialogo");
	}
	
	public void refreshBeneficiario() {
		
		BindUtils.postNotifyChange(null, null, this, "beneficiarios");
		BindUtils.postNotifyChange(null, null, this, "solicitudAyuda");
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		// TODO Auto-generated method stub
		restartWizard();
		return "";
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-pencil-square-o");
		//iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		
		urls.add("views/desktop/gestion/ayudas/familiarYBeneficiario/FamiliarFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/familiarYBeneficiario/agregarBeneficiario.zul");
		
		//urls.add("views/desktop/gestion/ayudas/familiarYBeneficiario/MotivoFormBasic.zul");
		
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroFamiliarPlanificado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		
		
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<Familiar> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusFamiliar",
				String.valueOf(EstatusFamiliarEnum.ACTIVO.ordinal()));
		PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		/*PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);*/
		return payloadFamiliarResponse;
	}

	/*@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Familiar</b>";
			}
		}
		
		

		
		
		return "";
	}*/

	/*@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		
		if(currentStep == 3){
			try {
				UtilValidate.validateNull(this.getFamiliar().getFkMotivo(), "Motivo");
				
				UtilValidate.validateString(this.getFamiliar().getObservacion(),
						"Detalle de Rechazo", 100);
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		
		return "";
	}*/

	@Override
	public String executeFinalizar(Integer currentStep) {
		
		if(currentStep==1){
			

			Familiar familiar = this.getFamiliar();
			familiar.setFechaIngreso(this.getFechaIngreso().getTime());
			familiar.setFechaSalida(new Date().getTime());

			if (bytes != null) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(this.getUrlImage());
				multimedia.setExtension(UtilMultimedia.stringToExtensionEnum(
						extensionImage).ordinal());
				multimedia.setDescripcion(typeMedia);

				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.incluir(multimedia);
				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					
				}
				multimedia.setIdMultimedia(((Double) payloadMultimediaResponse
						.getInformacion("id")).intValue());
				this.getPersona().setFkMultimedia(multimedia);
			}
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.incluir(this.getPersona());
			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
				
			}
			this.getPersona().setIdPersona(
					((Double) payloadPersonaResponse.getInformacion("id"))
							.intValue());
			familiar.setFkPersona(this.getPersona());
			PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
					.incluir(getFamiliar());
			if (!UtilPayload.isOK(payloadFamiliarResponse)) {
				
			}
			if (bytes != null) {
				Zki.save(Zki.PERSONAS, getPersona().getIdPersona(),
						extensionImage, bytes);
				Multimedia multimedia = this.getPersona().getFkMultimedia();
				multimedia.setNombre(Zki.PERSONAS
						+ this.getPersona().getIdPersona() + "."
						+ this.extensionImage);
				multimedia.setUrl(Zki.PERSONAS + getPersona().getIdPersona()
						+ "." + this.extensionImage);
				PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
						.modificar(multimedia);

				if (!UtilPayload.isOK(payloadMultimediaResponse)) {
					Alert.showMessage(payloadMultimediaResponse);
					
				}
			}
			Alert.showMessage(payloadFamiliarResponse);
			
			
		}
		
		/*if (currentStep == 3) {
			Familiar familiar = this.getFamiliar();
			//familiar.s(EstatusFamiliarEnum.INACTIVO.ordinal());
			
			Map<String, String> criterios = new HashMap<String, String>();
			criterios.put("fkFamiliar.idFamiliar",
			String.valueOf(familiar.getIdFamiliar()));

			PayloadBeneficiarioResponse payloadBeneficiario = 

			S.BeneficiarioService.consultarCriterios(TypeQuery.EQUAL, criterios);
			
			List<Beneficiario> list = payloadBeneficiario.getObjetos();
			
			//System.out.println(list);
			for(Beneficiario elemento:list){
				//System.out.println(elemento.getFkBeneficiario().getFkPersona().getNombre() + elemento.getFkBeneficiario().getFkPersona().getApellido() );
				//Persona personaBeneficiario = elemento;
				//personaBeneficiario.setEstatus('0');
				//PayloadPersonaResponse payloadPersonaResponse = S.PersonaService.modificar(personaBeneficiario);
				Beneficiario beneficiario = elemento;
				beneficiario.setEstatusBeneficiario(EstatusBeneficiarioEnum.INACTIVO.ordinal());
				PayloadBeneficiarioResponse payloadBeneficiarioResponse2 = S.BeneficiarioService.modificar(beneficiario); 
			}
			//PayloadBeneficiarioFamiliarResponse payloadBeneficiarioFamiliarResponse = S.BeneficiarioFamiliarService.consultarCriterios(typeQuery, criterios);
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService.modificar(persona);
			Familiar familiar = this.getFamiliar();
			familiar.getFkPersona().setEstatus('0');
			
			familiar.setEstatusFamiliar(EstatusFamiliarEnum.INACTIVO.ordinal());

			PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
					.modificar(familiar);
			
			if (!UtilPayload.isOK(payloadFamiliarResponse)) {
				Alert.showMessage(payloadFamiliarResponse);
				
			}
			
			if (UtilPayload.isOK(payloadFamiliarResponse)) {
				restartWizard();
				return "Familiar Egresado con Exito";
			}

		}*/

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
		
		
		/*
		if (currentStep == 2) {
			
			this.setFamiliar(selectedObject);
			
			this.setPersona(this.getFamiliar().getFkPersona());
			
			this.setEstado(this.getFamiliar().getFkPersona()
					.getFkCiudad().getFkEstado());
			this.setSexoEnum(SexoEnum.values()[this.getFamiliar()
					.getFkPersona().getSexo()]);
			this.setTipoPersonaEnum(this.getFamiliar().getFkPersona().getTipoPersonaEnum());
			
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(this.getFamiliar().getFechaIngreso());
			this.setFechaIngreso(cal.getTime());
			
			Calendar cal2 = Calendar.getInstance();
			cal2.setTimeInMillis(this.getFamiliar().getFkPersona().getFechaNacimiento());
			this.setFechaNacimiento(cal2.getTime());
			
			Map<String, String> criterios = new HashMap<>();
			criterios.put("fkEstado.idEstado",
					String.valueOf(estado.getIdEstado()));
			PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadCiudadResponse)) {
				Alert.showMessage(payloadCiudadResponse);
			}
			this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
			BindUtils.postNotifyChange(null, null, this, "estado");
			BindUtils.postNotifyChange(null, null, this, "ciudades");
			BindUtils.postNotifyChange(null, null, this, "sexoEnum");
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
		}
		*/
		
	}
	
	
/*	@Override
	public String executeCustom1(Integer currentStep) {
		
		
		Familiar familiar = this.getFamiliar();

		PayloadFamiliarResponse payloadFamiliarResponse = S.FamiliarService
				.modificar(familiar);
		
		if (!UtilPayload.isOK(payloadFamiliarResponse)) {
			Alert.showMessage(payloadFamiliarResponse);
			
		}
		
		if (UtilPayload.isOK(payloadFamiliarResponse)) {
			restartWizard();
			return "Solicitud Aprobada Con Exito";
		}
		

		return "";
	}
	
	@Override
	public String executeCustom2(Integer currentStep) {
		goToNextStep();

		return "";
	}*/

	// Propiedades de la Imagen
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

			public String getUrlImage() {
				return urlImage;
			}

			public void setUrlImage(String urlImage) {
				this.urlImage = urlImage;
			}

			public String getTypeMedia() {
				return typeMedia;
			}

			public void setTypeMedia(String typeMedia) {
				this.typeMedia = typeMedia;
			}

			@Override
			public BufferedImage getImageContent() {
				if (bytes != null) {
					try {
						return ImageIO.read(new ByteArrayInputStream(bytes));
					} catch (IOException e) {
						return null;
					}
				}

				if (urlImage != null) {
					bytes = Zki.getBytes(urlImage);
					return Zki.getBufferedImage(urlImage);
				}

				return null;
			}

			@Override
			public void onUploadImageSingle(UploadEvent event, String idUpload) {
				org.zkoss.util.media.Media media = event.getMedia();

				if (media instanceof org.zkoss.image.Image) {

					if (UtilMultimedia.validateImage(media.getName().substring(
							media.getName().lastIndexOf(".") + 1))) {
						this.bytes = media.getByteData();
						this.extensionImage = media.getName().substring(
								media.getName().lastIndexOf(".") + 1);
						this.typeMedia = media.getContentType();

						if (this.getPersona().getIdPersona() != null) {
							this.nameImage = new StringBuilder().append(Zki.PERSONAS)
									.append(this.getPersona().getIdPersona())
									.append(".").append(extensionImage).toString();

							this.urlImage = new StringBuilder().append(Zki.PERSONAS)
									.append(this.getPersona().getIdPersona())
									.append(".").append(extensionImage).toString();

						}
					} else {
						this.getPersona().setFkMultimedia(null);
						Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");

					}
				} else {
					this.getPersona().setFkMultimedia(null);
					Alert.showMessage("E: Error Code: 100-El formato de la <b>imagen</b> es inválido");
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

			public List<Beneficiario> getBeneficiarios() {
				if (this.beneficiarios == null) {
					this.beneficiarios = new ArrayList<>();
				}
				
				return beneficiarios;
			}

			public void setBeneficiarios(List<Beneficiario> beneficiarios) {
				this.beneficiarios = beneficiarios;
			}


}
