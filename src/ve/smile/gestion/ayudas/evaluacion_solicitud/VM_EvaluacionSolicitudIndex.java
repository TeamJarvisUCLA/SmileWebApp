package ve.smile.gestion.ayudas.evaluacion_solicitud;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import karen.core.dialog.generic.enums.DialogActionEnum;
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

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Directorio;
import ve.smile.dto.Motivo;
import ve.smile.dto.Multimedia;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.Persona;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.TsPlan;
import ve.smile.dto.Voluntario;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.UrgenciaEnum;
import ve.smile.payload.response.PayloadMotivoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import app.UploadImageSingle;

public class VM_EvaluacionSolicitudIndex extends
		VM_WindowWizard<SolicitudAyuda>  {
	
	
	private List<EstatusSolicitudEnum> estatusSolicitudEnums;
	private EstatusSolicitudEnum estatusSolicitudEnum;

	private List<UrgenciaEnum> urgenciaEnums;
	private UrgenciaEnum urgenciaEnum;

	private SolicitudAyuda solicitudAyuda;

	private Date fecha;

	private Beneficiario beneficiario = new Beneficiario();

	private Ayuda ayuda = new Ayuda();
	
	private List<Persona> personas ;
	
	private List<Motivo> motivos ;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		solicitudAyuda = new SolicitudAyuda();
		fecha = new Date();
		
		
	}
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Ayuda getAyuda() {
		return ayuda;
	}

	public void setAyuda(Ayuda ayuda) {
		this.ayuda = ayuda;
	}
	
	public EstatusSolicitudEnum getEstatusSolicitudEnum() {
		return estatusSolicitudEnum;
	}

	public void setEstatusSolicitudEnum(
			EstatusSolicitudEnum estatusSolicitudEnum) {
		this.estatusSolicitudEnum = estatusSolicitudEnum;
		this.getSolicitudAyuda().setEstatusSolicitud(
				estatusSolicitudEnum.ordinal());
	}

	public List<EstatusSolicitudEnum> getEstatusSolicitudEnums() {
		if (this.estatusSolicitudEnums == null) {
			this.estatusSolicitudEnums = new ArrayList<>();
		}
		if (this.estatusSolicitudEnums.isEmpty()) {
			for (EstatusSolicitudEnum estatusSolicitudEnum : EstatusSolicitudEnum
					.values()) {
				this.estatusSolicitudEnums.add(estatusSolicitudEnum);
			}
		}
		return estatusSolicitudEnums;
	}

	public void setEstatusSolicitudEnums(
			List<EstatusSolicitudEnum> estatusSolicitudEnums) {
		this.estatusSolicitudEnums = estatusSolicitudEnums;
	}

	public UrgenciaEnum getUrgenciaEnum() {
		return urgenciaEnum;
	}

	public void setUrgenciaEnum(UrgenciaEnum urgenciaEnum) {
		this.urgenciaEnum = urgenciaEnum;
		this.getSolicitudAyuda().setUrgencia(urgenciaEnum.ordinal());
	}

	public List<UrgenciaEnum> getUrgenciaEnums() {
		if (this.urgenciaEnums == null) {
			this.urgenciaEnums = new ArrayList<>();
		}
		if (this.urgenciaEnums.isEmpty()) {
			for (UrgenciaEnum urgenciaEnum : UrgenciaEnum.values()) {
				this.urgenciaEnums.add(urgenciaEnum);
			}
		}
		return urgenciaEnums;
	}

	public void setUrgenciaEnums(List<UrgenciaEnum> urgenciaEnums) {
		this.urgenciaEnums = urgenciaEnums;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.getSolicitudAyuda().setFecha(fecha.getTime());
	}

	
	public List<Persona> getPersonas() {
		if (this.personas == null) {
			this.personas = new ArrayList<>();
		}
		if (this.personas.isEmpty()) {
			PayloadPersonaResponse payloadPersonaResponse = S.PersonaService
					.consultarTodos();

			if (!UtilPayload.isOK(payloadPersonaResponse)) {
				Alert.showMessage(payloadPersonaResponse);
			}

			this.personas.addAll(payloadPersonaResponse.getObjetos());
		}

		return personas;
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

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}
	
	public SolicitudAyuda getSolicitudAyuda() {
		return solicitudAyuda;
	}

	public void setSolicitudAyuda(SolicitudAyuda solicitudAyuda) {
		this.solicitudAyuda = solicitudAyuda;
	}



	

	@Override
	public Map<Integer, List<OperacionWizard>> getButtonsToStep() {
		Map<Integer, List<OperacionWizard>> botones = new HashMap<Integer, List<OperacionWizard>>();
		
		OperacionWizard operacionWizardCustom1 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "APROBAR", "Custom1", "fa fa-check-square-o", "green", "APROBAR");
		OperacionWizard operacionWizardCustom2 = new OperacionWizard(
				OperacionWizardEnum.CUSTOM2.ordinal(), "RECHAZAR", "Custom2", "z-icon-times", "deep-orange", "RECHAZAR");

		List<OperacionWizard> listOperacionWizard1 = new ArrayList<OperacionWizard>();
		listOperacionWizard1.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(1, listOperacionWizard1);
		
		List<OperacionWizard> listOperacionWizard2 = new ArrayList<OperacionWizard>();
		listOperacionWizard2.add(operacionWizardCustom1);
		listOperacionWizard2.add(operacionWizardCustom2);
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		

		botones.put(2, listOperacionWizard2);

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

		botones.put(4, listOperacionWizard4);

		return botones;
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
		iconos.add("fa fa-pencil-square-o");
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/evaluacionSolicitud/selectSolicitudAyuda.zul");
		urls.add("views/desktop/gestion/ayudas/evaluacionSolicitud/SolicitudFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/evaluacionSolicitud/MotivoFormBasic.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroSolicitudAyudaPlanificado.zul");

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
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadSolicitudAyudaResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar una <b>Solicitud de Ayuda</b>";
			}
		}
		
		

		
		
		return "";
	}

	/*@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		
		if (currentStep == 3) {
			try {
				UtilValidate.validateDate(this.getFecha().getTime(),
						"Fecha Planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				UtilValidate.validateString(this.getSolicitudAyuda().getResultado(), "Resutlado", 100);
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		
		return "";
	}*/

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			SolicitudAyuda solicitudAyuda = this.getSolicitudAyuda();
			solicitudAyuda.setEstatusSolicitud(EstatusSolicitudEnum.RECHAZADA.ordinal());

			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.modificar(solicitudAyuda);
			
			if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				Alert.showMessage(payloadSolicitudAyudaResponse);
				
			}
			
			if (UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				restartWizard();
				return "Solicitud Rechazada Con Exito";
			}

		}

		return "";
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
		
		if(currentStep==2){
			
			this.setSolicitudAyuda(selectedObject);
			
			if (this.getSolicitudAyuda().getEstatusSolicitud() != null) {
				this.setEstatusSolicitudEnum(estatusSolicitudEnum.values()[this
						.getSolicitudAyuda().getEstatusSolicitud()]);
			} else {
				this.getSolicitudAyuda().setEstatus('A');// TODO borrar cuando will
															// arregle BD
				this.getSolicitudAyuda().setEstatusSolicitud(
						EstatusSolicitudEnum.PENDIENTE.ordinal());
				this.setEstatusSolicitudEnum(EstatusSolicitudEnum.PENDIENTE);
			}
			if (this.getSolicitudAyuda().getFkBeneficiario() != null) {
				this.setBeneficiario(this.getSolicitudAyuda().getFkBeneficiario());
			}

			if (this.getSolicitudAyuda().getFkAyuda() != null) {
				this.setAyuda(this.getSolicitudAyuda().getFkAyuda());
			}

			if (this.getSolicitudAyuda().getUrgencia() != null) {
				this.setUrgenciaEnum(urgenciaEnum.values()[this.getSolicitudAyuda()
						.getUrgencia()]);
			}

			if (this.getSolicitudAyuda().getFecha() != null) {
				this.setFecha(new Date(this.getSolicitudAyuda().getFecha()));
			} else {
				this.setFecha(new Date());
			}
			
			
		}
		
		
	}
	
	@Override
	public String executeCustom1(Integer currentStep) {
		
		
		SolicitudAyuda solicitudAyuda = this.getSolicitudAyuda();
		solicitudAyuda.setEstatusSolicitud(EstatusSolicitudEnum.APROBADA.ordinal());

		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.modificar(solicitudAyuda);
		
		if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			Alert.showMessage(payloadSolicitudAyudaResponse);
			
		}
		
		if (UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
			restartWizard();
			return "Solicitud Aprobada Con Exito";
		}
		

		return "";
	}
	
	@Override
	public String executeCustom2(Integer currentStep) {
		goToNextStep();

		return "";
	}




}
