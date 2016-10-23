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
import ve.smile.dto.Directorio;
import ve.smile.dto.EstudioSocioEconomico;
import ve.smile.dto.Multimedia;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.dto.SolicitudAyudaRecurso;
import ve.smile.dto.Recurso;
import ve.smile.enums.EstatusPadrinoEnum;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.enums.UrgenciaEnum;
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

						recurso = catalogueDialogCloseEvent.getEntity();

						refreshRecurso();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/ayudas/asignacionAyuda/catalogoRecurso.zul",
						catalogueDialogData);
	}



	public void refreshRecurso() {
		BindUtils.postNotifyChange(null, null, this, "recurso");
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
				.getPorType(OperacionWizardEnum.FINALIZAR));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

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
		// iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/selectSolicitudAyuda.zul");
		urls.add("views/desktop/gestion/ayudas/asignacionAyuda/AsignacionAyudaFormBasic.zul");
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
			return "E:Error Code 5-No hay otro paso";
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

		if (currentStep == 2) {
			try {
				
				
				UtilValidate.validateDate(this.getFechaAsignacion().getTime(),
						"Fecha Planificada", ValidateOperator.GREATER_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
						"dd/MM/yyyy");
				UtilValidate.validateInteger(this.getSolicitudAyudaRecurso().getCantidad(),
						"Cantidad", ValidateOperator.GREATER_THAN, 0);
				UtilValidate.validateNull(this.getRecurso()
						.getIdRecurso(), "Recurso");
				//UtilValidate.validateInteger(getSolicitudAyudaRecurso().getCantidad(), "cantidad", null, null);
				
			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			this.getSolicitudAyudaRecurso().setFechaAsignacion(
					this.getFechaAsignacion().getTime());
			this.getSolicitudAyudaRecurso().setFkSolicitudAyuda(selectedObject);
			this.getSolicitudAyudaRecurso().setFkRecurso(this.getRecurso());

	
			PayloadSolicitudAyudaRecursoResponse payloadSolicitudAyudaRecursoResponse = S.SolicitudAyudaRecursoService
					.incluir(this.solicitudAyudaRecurso);
			if (UtilPayload.isOK(payloadSolicitudAyudaRecursoResponse)) {
				restartWizard();
				this.setSolicitudAyudaRecurso(new SolicitudAyudaRecurso());
				this.setFechaAsignacion(new Date());
				this.setSelectedObject(new SolicitudAyuda());
				this.setRecurso(new Recurso());
				BindUtils.postNotifyChange(null, null, this, "directorio");
				BindUtils.postNotifyChange(null, null, this, "solicitudAyudaRecurso");
				BindUtils
						.postNotifyChange(null, null, this, "fechaAsignacion");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "recurso");
			}
			return (String) payloadSolicitudAyudaRecursoResponse
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

	
}
