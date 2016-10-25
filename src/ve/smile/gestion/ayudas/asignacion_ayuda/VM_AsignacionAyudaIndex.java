package ve.smile.gestion.ayudas.asignacion_ayuda;

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
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Directorio;
import ve.smile.dto.EstudioSocioEconomico;
import ve.smile.dto.Multimedia;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.SolicitudAyudaRecurso;
import ve.smile.dto.Recurso;
import ve.smile.enums.EstatusBeneficiarioEnum;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.UrgenciaEnum;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import ve.smile.payload.response.PayloadEstudioSocioEconomicoResponse;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.payload.response.PayloadSolicitudAyudaRecursoResponse;
import app.UploadImageSingle;

public class VM_AsignacionAyudaIndex extends
		VM_WindowWizard<SolicitudAyuda>  {

	private SolicitudAyudaRecurso solicitudAyudaRecurso;
	private Recurso recurso = new Recurso();
	private Date fechaAsignacion = new Date();

	private List<EstatusSolicitudEnum> estatusSolicitudEnums;
	private EstatusSolicitudEnum estatusSolicitudEnum;

	private List<UrgenciaEnum> urgenciaEnums;
	private UrgenciaEnum urgenciaEnum;

	//contains de java para evitar duplicados
	private List<Recurso> recursos;
	private List<SolicitudAyudaRecurso> solicitudAyudaRecursos;
	
	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		solicitudAyudaRecurso = new SolicitudAyudaRecurso();
		recurso = new Recurso();
		fechaAsignacion = new Date();
	}
	
	public EstatusSolicitudEnum getEstatusSolicitudEnum() {
		return estatusSolicitudEnum;
	}

	public void setEstatusSolicitudEnum(
			EstatusSolicitudEnum estatusSolicitudEnum) {
		this.estatusSolicitudEnum = estatusSolicitudEnum;
		this.selectedObject.setEstatusSolicitud(
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
		this.selectedObject.setUrgencia(urgenciaEnum.ordinal());
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

	public SolicitudAyudaRecurso getSolicitudAyudaRecurso() {
		return solicitudAyudaRecurso;
	}

	public void setSolicitudAyudaRecurso(SolicitudAyudaRecurso solicitudAyudaRecurso) {
		this.solicitudAyudaRecurso = solicitudAyudaRecurso;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}



	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}
	
	public List<Recurso> getRecursos() {
		if (this.recursos == null) {
			this.recursos = new ArrayList<>();
		}
	
		return recursos;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}
	
	public List<SolicitudAyudaRecurso> getSolicitudAyudaRecursos() {
		
		if (this.solicitudAyudaRecursos == null) {
			
			/*Map<String, String> criterios = new HashMap<>();

			criterios
					.put("fkSolicitudAyuda.idFSolicitudAyuda", String.valueOf(selectedObject.getIdSolicitudAyuda()));
			
			PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse = S.SolicitudAyudaRecursoService
					.consultarCriterios(TypeQuery.EQUAL, criterios);
			if (!UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
				Alert.showMessage(payloadSolicitudAyudaRecursoResponse);
			}
			this.solicitudAyudaRecursos.addAll(payloadSolicitudAyudaRecursoResponse.getObjetos());*/
			
				this.solicitudAyudaRecursos =  new ArrayList<>();
		}
		
		return solicitudAyudaRecursos;
	}

	public void setSolicitudAyudaRecursos(List<SolicitudAyudaRecurso> solicitudAyudaRecursos) {
		this.solicitudAyudaRecursos = solicitudAyudaRecursos;
	}

	@Command("buscarRecurso")
	public void buscarRecurso() {
		CatalogueDialogData<Recurso> catalogueDialogData = new CatalogueDialogData<Recurso>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Recurso>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Recurso> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						if(!recursos.contains(catalogueDialogCloseEvent.getEntity()) ){
							recursos.add(catalogueDialogCloseEvent.getEntity());
						}

						refreshRecursos();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/ayudas/asignacionAyuda/catalogoRecurso.zul",
						catalogueDialogData);
	}



	public void refreshRecursos() {
		BindUtils.postNotifyChange(null, null, this, "recursos");
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

		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/selectSolicitudAyuda.zul");
		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/AsignacionAyudaFormBasic.zul");
		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/AsignacionAyudaFinalizarFormBasic.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroSolicitudAyudaPlanificado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if(currentStep==2){
			
			for(Recurso elemento:this.getRecursos()){
				
				SolicitudAyudaRecurso ayudaRecurso = new SolicitudAyudaRecurso();
				ayudaRecurso.setFkRecurso(elemento);
				ayudaRecurso.setFkSolicitudAyuda(selectedObject);
				this.getSolicitudAyudaRecursos().add(ayudaRecurso);
				/*PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse = S.SolicitudAyudaRecursoService
						.incluir(ayudaRecurso);
				
				if (!UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
					
				}*/
			}
			
			
		}
		
		goToNextStep();

		return "";
	}

	@Override
	public String executeAtras(Integer currentStep) {
		if(currentStep==3){
			this.getSolicitudAyudaRecursos().removeAll(solicitudAyudaRecursos);
		}
		goToPreviousStep();

		return "";
	}

	@Override
	public IPayloadResponse<SolicitudAyuda> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		
		Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		
		//usar nuevo enum recurso asignado 
		criterios.put("estatusSolicitud",
				String.valueOf(EstatusSolicitudEnum.APROBADA.ordinal()));
		PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacionCriterios(cantidadRegistrosPagina, pagina,
						TypeQuery.EQUAL, criterios);

		/*PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);*/
		return payloadSolicitudAyudaResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar una <b>Solicitud de Ayuda</b>";
			}
		}

		if (currentStep == 2) {
			if(this.getRecursos().isEmpty()){
				return "E:Error Code 5-Seleccione Un Recurso";
			}
			
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		/*Map<String, String> criterios = new HashMap<>();
		EstatusPadrinoEnum.POSTULADO.ordinal();
		criterios.put("fkSolicitudAyuda",
				String.valueOf(selectedObject.getIdSolicitudAyuda()));
		PayloadEstudioSocioEconomicoResponse payloadEstudioSocioEconomicoResponse = S.EstudioSocioEconomicoService
				.consultarCriterios(
						TypeQuery.EQUAL, criterios);
		EstudioSocioEconomico economico =  payloadEstudioSocioEconomicoResponse.getObjetos().get(0);*/

		if (currentStep == 3) {
			try {
				
				
				UtilValidate.validateDate(this.getFechaAsignacion().getTime(),
						"Fecha Planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				
				//UtilValidate.validateInteger(getSolicitudAyudaRecurso().getCantidad(), "cantidad", null, null);
				
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			
			for(SolicitudAyudaRecurso elemento:this.getSolicitudAyudaRecursos()){
				
				elemento.setFechaAsignacion(this.getFechaAsignacion().getTime());
				
				PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse = S.SolicitudAyudaRecursoService
						.incluir(elemento);
				
				if (!UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
					
				}
				

				
			}
			selectedObject.setEstatusSolicitud(EstatusSolicitudEnum.PROCESADA.ordinal());
			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService.modificar(selectedObject);
			
			restartWizard();

		}

		return "Ayudas Asignadas con Exito";
		
		
	}

	@Override
	public void comeIn(Integer currentStep) {
		if (currentStep == 1) {
			this.getControllerWindowWizard().updateListBoxAndFooter();
			BindUtils.postNotifyChange(null, null, this, "objectsList");
		}
	}

	

	
}
