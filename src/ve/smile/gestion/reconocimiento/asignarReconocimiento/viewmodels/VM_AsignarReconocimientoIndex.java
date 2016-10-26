package ve.smile.gestion.reconocimiento.asignarReconocimiento.viewmodels;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;
import lights.smile.util.Zki;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorReconocimiento;
import ve.smile.dto.Colaborador;
import ve.smile.dto.Multimedia;
import ve.smile.dto.NotificacionUsuario;
import ve.smile.dto.Padrino;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Persona;
import ve.smile.dto.ReconocimientoPersona;
import ve.smile.dto.Trabajador;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusColaboradorEnum;
import ve.smile.enums.EstatusNotificacionEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusTrabajadorEnum;
import ve.smile.enums.EstatusVoluntarioEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.TipoReconocimientoEnum;
import ve.smile.enums.TipoReferenciaNotificacionEnum;
import ve.smile.payload.response.PayloadClasificadorReconocimientoResponse;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadNotificacionUsuarioResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadReconocimientoPersonaResponse;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import ve.smile.payload.response.PayloadVoluntarioResponse;
import app.UploadImageSingle;

public class VM_AsignarReconocimientoIndex extends VM_WindowWizard implements
		UploadImageSingle {

	private List<TipoReconocimientoEnum> tipoReconocimientoEnums;
	private List<ClasificadorReconocimiento> clasificadorReconocimientos;
	private List<ClasificadorReconocimiento> clasificadorPorTipoReconocimientos;
	TipoReconocimientoEnum tipoReconocimientoEnum;
	private ReconocimientoPersona reconocimientoPersona;

	private Padrino padrino;
	private Voluntario voluntario;
	private Colaborador colaborador;
	private Patrocinador patrocinador;
	private Trabajador trabajador;
	private Persona persona;

	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;
	private String urlImage;
	private String typeMedia;

	private List<Padrino> padrinos;
	private List<Voluntario> voluntarios;
	private List<Colaborador> colaboradores;
	private List<Patrocinador> patrocinadores;
	private List<Trabajador> trabajadores;

	private String srcList;

	@Init(superclass = true)
	public void childInit() {
		padrino = new Padrino();
		voluntario = new Voluntario();
		colaborador = new Colaborador();
		patrocinador = new Patrocinador();
		trabajador = new Trabajador();
	}

	public TipoReconocimientoEnum getTipoReconocimientoEnum() {
		return tipoReconocimientoEnum;
	}

	public void setTipoReconocimientoEnum(
			TipoReconocimientoEnum tipoReconocimientoEnum) {
		this.tipoReconocimientoEnum = tipoReconocimientoEnum;
	}

	public List<TipoReconocimientoEnum> getTipoReconocimientoEnums() {
		if (this.tipoReconocimientoEnums == null) {
			this.tipoReconocimientoEnums = new ArrayList<>();
		}
		if (this.tipoReconocimientoEnums.isEmpty()) {
			for (TipoReconocimientoEnum tipoReconocimiento : TipoReconocimientoEnum
					.values()) {
				this.tipoReconocimientoEnums.add(tipoReconocimiento);
			}
		}
		return tipoReconocimientoEnums;
	}

	public void setTipoReconocimientoEnums(
			List<TipoReconocimientoEnum> tipoReconocimientoEnums) {
		this.tipoReconocimientoEnums = tipoReconocimientoEnums;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Padrino getPadrino() {
		return padrino;
	}

	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
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

	public Voluntario getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(Voluntario voluntario) {
		this.voluntario = voluntario;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Patrocinador getPatrocinador() {
		return patrocinador;
	}

	public void setPatrocinador(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}

	public Trabajador getTrabajador() {
		return trabajador;
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

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
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

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);

		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");

		listOperacionWizard4.add(operacionWizardCustom);

		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart"); // seleccionar tipo reconocimiento
		iconos.add("fa fa-pencil-square-o"); // seleccionar persona a reconocer
		iconos.add("fa fa-pencil-square-o"); // formulario de reconocimiento
		iconos.add("fa fa-check-square-o"); // registro finalizado

		return iconos;
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		this.setSelectedObject(null);
		restartWizard();
		return "";
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectTipoReconocimiento.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectList.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/AsignarReconocimientoFormBasic.zul");
		urls.add("views/desktop/gestion/reconocimiento/asignarReconocimiento/registroCompletado.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PADRINO)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectPadrino.zul");
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.VOLUNTARIO)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectVoluntario.zul");
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.COLABORADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectColaborador.zul");
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.PATROCINADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectPatrocinador.zul");
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.TRABAJADOR)) {
				this.setSrcList("views/desktop/gestion/reconocimiento/asignarReconocimiento/selectTrabajador.zul");
			}
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		if (currentStep == 2) {
			Map<String, String> criterios = new HashMap<>();
			criterios.put("tipoReconocimiento",
					String.valueOf(tipoReconocimientoEnum.ordinal()));
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			clasificadorPorTipoReconocimientos = payloadClasificadorReconocimientoResponse
					.getObjetos();
		}
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		Map<String, String> criterios = new HashMap<>();
		criterios.put("estatusTrabajador",
				String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));
		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);
		return payloadTrabajadorResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (tipoReconocimientoEnum == null) {
				return "E:Error Code 5-Debe seleccionar un <b> Tipo de Reconocimiento</b>";
			}
		}

		if (currentStep == 2) {
			if (tipoReconocimientoEnum.equals(tipoReconocimientoEnum.PADRINO)
					&& padrino.getIdPadrino() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			} else if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.PADRINO)) {
				persona = this.getPadrino().getFkPersona();
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.VOLUNTARIO)
					&& voluntario.getIdVoluntario() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Voluntario</b>";
			} else if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.VOLUNTARIO)) {
				persona = this.getVoluntario().getFkPersona();
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.COLABORADOR)
					&& colaborador.getIdColaborador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Colaborador</b>";
			} else if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.COLABORADOR)) {
				persona = this.getColaborador().getFkPersona();
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.PATROCINADOR)
					&& patrocinador.getIdPatrocinador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Patrocinador</b>";
			} else if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.PATROCINADOR)) {
				persona = this.getPatrocinador().getFkPersona();
			}
			if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.TRABAJADOR)
					&& trabajador.getIdTrabajador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajador</b>";
			} else if (tipoReconocimientoEnum
					.equals(tipoReconocimientoEnum.TRABAJADOR)) {
				persona = this.getTrabajador().getFkPersona();
			}
		}

		if (currentStep == 3) {
			if (getReconocimientoPersona().getFkClasificadorReconocimiento() == null
					|| getReconocimientoPersona().getContenido() == null) {
				return "E:Error Code 5-Debe llenar todos los campos";
			}
			if (extensionImage == null) {
				return "Error Code 099-Debe cargar una Imagen para el reconocimiento";
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {

			this.getReconocimientoPersona().setFkPersona(this.persona);

			// TODO: Para imagen
			if (extensionImage != null) {

				if (bytes != null) {
					Multimedia multimedia = new Multimedia();
					multimedia.setNombre(nameImage);
					multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
							.ordinal());
					multimedia.setUrl(this.getUrlImage());
					multimedia.setExtension(UtilMultimedia
							.stringToExtensionEnum(extensionImage).ordinal());
					multimedia.setDescripcion(typeMedia);

					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.incluir(multimedia);
					if (!UtilPayload.isOK(payloadMultimediaResponse)) {
						Alert.showMessage(payloadMultimediaResponse);
					}
					multimedia
							.setIdMultimedia(((Double) payloadMultimediaResponse
									.getInformacion("id")).intValue());
					this.getReconocimientoPersona().setFkMultimedia(multimedia);
				}

				// TODO: Incluir Reconocimiento
				PayloadReconocimientoPersonaResponse payloadReconocimientoPersonaResponse = S.ReconocimientoPersonaService
						.incluir(getReconocimientoPersona());
				if (!UtilPayload.isOK(payloadReconocimientoPersonaResponse)) {
					Alert.showMessage(payloadReconocimientoPersonaResponse);
				}
				getReconocimientoPersona().setIdReconocimientoPersona(
						((Double) payloadReconocimientoPersonaResponse
								.getInformacion("id")).intValue());
				if (bytes != null) {
					Zki.save(Zki.RECONOCIMIENTOS, getReconocimientoPersona()
							.getIdReconocimientoPersona(), extensionImage,
							bytes);
					Multimedia multimedia = this.getPersona().getFkMultimedia();
					multimedia.setNombre(Zki.RECONOCIMIENTOS
							+ getReconocimientoPersona()
									.getIdReconocimientoPersona() + "."
							+ this.extensionImage);
					multimedia.setUrl(Zki.RECONOCIMIENTOS
							+ getReconocimientoPersona()
									.getIdReconocimientoPersona() + "."
							+ this.extensionImage);
					PayloadMultimediaResponse payloadMultimediaResponse = S.MultimediaService
							.modificar(multimedia);

					if (!UtilPayload.isOK(payloadMultimediaResponse)) {
						Alert.showMessage(payloadMultimediaResponse);
					}
				}

				if (this.getReconocimientoPersona().getFkPersona()
						.getFkUsuario() != null
						&& this.getReconocimientoPersona().getFkPersona()
								.getFkUsuario() != null) {
					String contenido = "¡Felicitaciones!, se le ha asignado un reconocimiento de "
							+ this.getReconocimientoPersona()
									.getFkClasificadorReconocimiento()
									.getNombre();
					NotificacionUsuario notificacionUsuario = new NotificacionUsuario(
							this.getReconocimientoPersona().getFkPersona()
									.getFkUsuario(), new Date().getTime(),
							((Double) payloadReconocimientoPersonaResponse
									.getInformacion("id")).intValue(),
							EstatusNotificacionEnum.PENDIENTE.ordinal(),
							TipoReferenciaNotificacionEnum.RECONOCIMIENTO
									.ordinal(), contenido);
					PayloadNotificacionUsuarioResponse payloadNotificacionUsuarioResponse = S.NotificacionUsuarioService
							.incluir(notificacionUsuario);
					if (!UtilPayload.isOK(payloadNotificacionUsuarioResponse)) {
						return (String) payloadNotificacionUsuarioResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}
			} else {
				return "Error Code: 099-Debe cargar una Imagen del Reconocimiento";
			}

			reconocimientoPersona = new ReconocimientoPersona();
			bytes = null;
			nameImage = null;
			extensionImage = null;
			urlImage = null;
			typeMedia = null;
			goToNextStep();

		}
		return "";
	}

	public String getSrcList() {
		return srcList;
	}

	public void setSrcList(String srcList) {
		this.srcList = srcList;
	}

	public List<ClasificadorReconocimiento> getClasificadorPorTipoReconocimientos() {
		return clasificadorPorTipoReconocimientos;
	}

	public void setClasificadorPorTipoReconocimientos(
			List<ClasificadorReconocimiento> clasificadorPorTipoReconocimientos) {
		this.clasificadorPorTipoReconocimientos = clasificadorPorTipoReconocimientos;
	}

	public List<Patrocinador> getPatrocinadores() {
		if (this.patrocinadores == null) {
			this.patrocinadores = new ArrayList<>();
		}
		if (this.patrocinadores.isEmpty()) {

			// Todo patrocinador que está en tabla es ACTIVO, por tanto no se
			// valida por estatus.
			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.consultarTodos();
			if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
				this.patrocinadores.addAll(payloadPatrocinadorResponse
						.getObjetos());
			}
		}

		return patrocinadores;
	}

	public void setPatrocinadores(List<Patrocinador> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public List<Padrino> getPadrinos() {
		if (this.padrinos == null) {
			this.padrinos = new ArrayList<>();
		}
		if (this.padrinos.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusPadrino",
					String.valueOf(EstatusPadrinoEnum.ACTIVO.ordinal()));
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				this.padrinos.addAll(payloadPadrinoResponse.getObjetos());
			}
		}

		return padrinos;
	}

	public void setPadrinos(List<Padrino> padrinos) {
		this.padrinos = padrinos;
	}

	public List<Trabajador> getTrabajadores() {
		if (this.trabajadores == null) {
			this.trabajadores = new ArrayList<>();
		}
		if (this.trabajadores.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusTrabajador",
					String.valueOf(EstatusTrabajadorEnum.ACTIVO.ordinal()));

			PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadTrabajadorResponse)) {
				this.trabajadores
						.addAll(payloadTrabajadorResponse.getObjetos());
			}
		}

		return trabajadores;
	}

	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public List<Voluntario> getVoluntarios() {
		if (this.voluntarios == null) {
			this.voluntarios = new ArrayList<>();
		}
		if (this.voluntarios.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusVoluntario",
					String.valueOf(EstatusVoluntarioEnum.ACTIVO.ordinal()));

			PayloadVoluntarioResponse payloadVoluntarioResponse = S.VoluntarioService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadVoluntarioResponse)) {
				this.voluntarios.addAll(payloadVoluntarioResponse.getObjetos());
			}
		}

		return voluntarios;
	}

	public void setVoluntarios(List<Voluntario> voluntarios) {
		this.voluntarios = voluntarios;
	}

	public List<Colaborador> getColaboradores() {
		if (this.colaboradores == null) {
			this.colaboradores = new ArrayList<>();
		}
		if (this.colaboradores.isEmpty()) {

			Map<String, String> criterios = new HashMap<>();
			criterios.put("estatusColaborador",
					String.valueOf(EstatusColaboradorEnum.ACTIVO.ordinal()));

			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (UtilPayload.isOK(payloadColaboradorResponse)) {
				this.colaboradores.addAll(payloadColaboradorResponse
						.getObjetos());
			}
		}

		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public ReconocimientoPersona getReconocimientoPersona() {
		if (reconocimientoPersona == null) {
			reconocimientoPersona = new ReconocimientoPersona();
		}
		return reconocimientoPersona;
	}

	public void setReconocimientoPersona(
			ReconocimientoPersona reconocimientoPersona) {
		this.reconocimientoPersona = reconocimientoPersona;
	}

	public List<ClasificadorReconocimiento> getClasificadorReconocimientos() {
		if (this.clasificadorReconocimientos == null) {
			this.clasificadorReconocimientos = new ArrayList<>();
		}
		if (this.clasificadorReconocimientos.isEmpty()) {
			PayloadClasificadorReconocimientoResponse payloadClasificadorReconocimientoResponse = S.ClasificadorReconocimientoService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadClasificadorReconocimientoResponse)) {
				Alert.showMessage(payloadClasificadorReconocimientoResponse);
			}

			this.clasificadorReconocimientos
					.addAll(payloadClasificadorReconocimientoResponse
							.getObjetos());
		}
		return clasificadorReconocimientos;
	}

	public void setClasificadorReconocimientos(
			List<ClasificadorReconocimiento> clasificadorReconocimientos) {
		this.clasificadorReconocimientos = clasificadorReconocimientos;
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setSelectedObject(null);
		restartWizard();
		return "";
	}

}
