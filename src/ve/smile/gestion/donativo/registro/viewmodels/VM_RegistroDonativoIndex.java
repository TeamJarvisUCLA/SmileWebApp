package ve.smile.gestion.donativo.registro.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.PropietarioEnum;
import ve.smile.enums.TipoMultimediaEnum;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.DonativoCuentaBancaria;
import ve.smile.dto.DonativoRecurso;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.TsPlan;
import ve.smile.dto.Padrino;
import ve.smile.dto.Colaborador;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Multimedia;
import ve.smile.dto.Directorio;
import ve.smile.payload.response.PayloadDonativoCuentaBancariaResponse;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.seguridad.enums.OperacionEnum;
import app.UploadImageSingle;

public class VM_RegistroDonativoIndex extends
VM_WindowWizard<DonativoRecurso> implements UploadImageSingle{
	
	private DonativoRecurso donativoRecurso;
	private DonativoCuentaBancaria donativoCuentaBancaria;
	
	private byte[] bytes;
	private String nameImage;
	private String urlImagen;
	
	private List<ProcedenciaEnum> tipoProcedenciaEnums;
	private ProcedenciaEnum procedenciaEnums;
	private EventoPlanificado eventoPlanificado;
	private TsPlan tsPlan;
	private Padrino padrino;
	private Colaborador colaborador;
	private Patrocinador patrocinador;
	private Directorio directorio;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		donativoRecurso = new DonativoRecurso();
		donativoCuentaBancaria = new DonativoCuentaBancaria();
		tsPlan = new TsPlan();
		eventoPlanificado = new EventoPlanificado();
		padrino = new Padrino();
		colaborador = new Colaborador();
		patrocinador = new Patrocinador();
		directorio = new Directorio();
	}
	
	public void setTsPlan(TsPlan tsPlan){
		this.tsPlan = tsPlan;
	}
	
	public TsPlan getTsPlan(){
		return tsPlan;
	}
	
	public void setDirectorio(Directorio directorio){
		this.directorio = directorio;
	}
	
	public Directorio getDirectorio(){
		return directorio;
	}
	
	public void setPatrocindor(Patrocinador patrocinador){
		this.patrocinador = patrocinador;
	}
	
	public Patrocinador getPatrocinador(){
		return patrocinador;
	}
	
	public void setColaborador(Colaborador colaborador){
		this.colaborador = colaborador;
	}
	
	public Colaborador getColaborador(){
		return colaborador;
	}
	
	public void setPadrino(Padrino padrino){
		this.padrino = padrino;
	}
	
	public Padrino getPadrino(){
		return padrino;
	}
	
	public void setEventoPlanificado(EventoPlanificado eventoPlanificado){
		this.eventoPlanificado = eventoPlanificado;
	}
	
	public EventoPlanificado getEventoPlanificado(){
	   return eventoPlanificado;
	}
	
	public ProcedenciaEnum getProcedenciaEnums()
	{
		return procedenciaEnums;
	}

	public void setProcedenciaEnums(ProcedenciaEnum procedenciaEnums)
	{
		this.procedenciaEnums = procedenciaEnums;
		this.getDonativoRecurso().setProcedencia(procedenciaEnums.ordinal());
	}

	public DonativoRecurso getDonativoRecurso() {
		return donativoRecurso;
	}

	public void setDonativoRecurso(DonativoRecurso donativoRecurso) {
		this.donativoRecurso = donativoRecurso;
	}

	public DonativoCuentaBancaria getDonativoCuentaBancaria() {
		return donativoCuentaBancaria;
	}
	
	public void setDonativoCuentaBancaria(DonativoCuentaBancaria donativoCuentaBancaria) {
		this.donativoCuentaBancaria = donativoCuentaBancaria;
	}

	@Command("buscarDirectorio")
	public void buscarDirectorio() {
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						Directorio directorio = catalogueDialogCloseEvent.getEntity();
						refreshDirectorio();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}
	
	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}
	
	public List<ProcedenciaEnum> getTipoProcedenciaEnums()
	{
		if (this.tipoProcedenciaEnums == null)
		{
			this.tipoProcedenciaEnums = new ArrayList<>();
		}
		if (this.tipoProcedenciaEnums.isEmpty())
		{
			for (ProcedenciaEnum procedenciaEnums : ProcedenciaEnum.values())
			{
				this.tipoProcedenciaEnums.add(procedenciaEnums);
			}
		}
		return tipoProcedenciaEnums;
	}
	
	public void setTipoProcedenciaEnums(List<ProcedenciaEnum> tipoProcedenciaEnums)
	{
		this.tipoProcedenciaEnums = tipoProcedenciaEnums;
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(3, listOperacionWizard3);
		
		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(4, listOperacionWizard4);

		return botones;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart"); //seleccionar Procedencia
		iconos.add("fa fa-pencil-square-o"); //seleccionar objeto de la Procedencia
		iconos.add("fa fa-pencil-square-o"); //formulario de registro
		iconos.add("fa fa-check-square-o");  //registro finalizado

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/donativo/registro/selectProcedencia.zul"); //seleccionar Procedencia
		
		//if(urls){ //seleccionar objeto de la procedencia
		urls.add("views/desktop/gestion/donativo/registro/selectEventoPlanificado.zul");
		//urls.add("views/desktop/gestion/donativo/registro/selectTrabajoSocialPlanificado.zul");
		//urls.add("views/desktop/gestion/donativo/registro/selectColaborador.zul");
		//urls.add("views/desktop/gestion/donativo/registro/selectPatrocinador.zul");
		//urls.add("views/desktop/gestion/donativo/registro/selectPadrino.zul");
		//}
		
		urls.add("views/desktop/gestion/donativo/registro/RegistroDonativoFormBasic.zul"); //formulario de registro
		urls.add("views/desktop/gestion/donativo/registro/registroCompletado.zul"); //registro completado
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
	public IPayloadResponse<DonativoRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDonativoRecursoResponse;
	}

	
	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (procedenciaEnums == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Tipo de Procedencia</b>";
			}
		}

		if (currentStep == 2) {
			return "E:Error Code 5-Debe seleccionar un <b>Item</b>";
		}
		
		if (currentStep == 3) {
			return "E:Error Code 5-Debe llenar todos los campos";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				UtilValidate.validateNull(donativoRecurso.getRecepcion(), "Recepcion");
							} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getDonativoRecurso().getDescripcion();
			this.getDonativoRecurso().getFechaDonativo();
			this.getDonativoRecurso().getFkEventoPlanificado();
			this.getDonativoRecurso().getFkPersona();
			this.getDonativoRecurso().getFkTsPlan();
			this.getDonativoRecurso().getProcedencia();
			this.getDonativoRecurso().getProcedenciaEnum();
			this.getDonativoRecurso().getRecepcion();
			this.getDonativoRecurso().getRecepcionEnum();
			this.getDonativoCuentaBancaria().getFkDonativoRecurso();
			
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.incluir(this.eventoPlanificado);
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				restartWizard();
				this.setEventoPlanificado(new EventoPlanificado());
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
			
			return (String) payloadEventoPlanificadoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}

		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.incluir(this.tsPlan);
		if (UtilPayload.isOK(payloadTsPlanResponse)) {
			restartWizard();
			BindUtils.postNotifyChange(null, null, this, "selectedObject");
		
		return (String) payloadTsPlanResponse
				.getInformacion(IPayloadResponse.MENSAJE);
	}
	
	PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
			.incluir(this.padrino);
	if (UtilPayload.isOK(payloadPadrinoResponse)) {
		restartWizard();
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
	
	return (String) payloadPadrinoResponse
			.getInformacion(IPayloadResponse.MENSAJE);
}

	PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
			.incluir(this.colaborador);
	if (UtilPayload.isOK(payloadColaboradorResponse)) {
		restartWizard();
		BindUtils.postNotifyChange(null, null, this, "selectedObject");
	
	return (String) payloadColaboradorResponse
			.getInformacion(IPayloadResponse.MENSAJE);
}

	PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
	.incluir(this.patrocinador);
	if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
	restartWizard();
	BindUtils.postNotifyChange(null, null, this, "selectedObject");
	}
	return (String) payloadPatrocinadorResponse
	.getInformacion(IPayloadResponse.MENSAJE);
	}


return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
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
			if (UtilMultimedia.validateFile(nameImage.substring(this.nameImage
					.lastIndexOf(".") + 1))) {
				Multimedia multimedia = new Multimedia();
				multimedia.setNombre(nameImage);
				multimedia.setTipoMultimedia(TipoMultimediaEnum.IMAGEN
						.ordinal());
				multimedia.setUrl(new StringBuilder()
						.append("/Smile/Patrocinador/").append(nameImage)
						.toString());
				multimedia.setExtension(UtilMultimedia
						.stringToExtensionEnum(
								nameImage.substring(this.nameImage
										.lastIndexOf(".") + 1)).ordinal());
				
			} else {
				//...

			}

		}
	}
	
	@Override
	public void onRemoveImageSingle(String idUpload) {
     //...
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

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

}
