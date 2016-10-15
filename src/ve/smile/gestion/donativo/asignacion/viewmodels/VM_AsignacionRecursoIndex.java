package ve.smile.gestion.donativo.asignacion.viewmodels;

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
import lights.core.payload.response.IPayloadResponse;
import lights.smile.util.UtilMultimedia;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Directorio;
import ve.smile.dto.Multimedia;
import ve.smile.dto.DonativoRecurso;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import app.UploadImageSingle;

public class VM_AsignacionRecursoIndex extends
		VM_WindowWizard<DonativoRecurso>  {

	private DonativoRecurso donativoRecurso;
	private List<String> tipoDestinoRecurso;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		donativoRecurso = new DonativoRecurso();
	}

	public DonativoRecurso getDonativoRecurso() {
		return donativoRecurso;
	}

	public void setDonativoRecurso(DonativoRecurso donativoRecurso) {
		this.donativoRecurso = donativoRecurso;
	}
	
	public List<String> getTipoDestinoRecurso()
	{
		if (this.tipoDestinoRecurso == null)
		{
			this.tipoDestinoRecurso = new ArrayList<>();
		}
		if (this.tipoDestinoRecurso.isEmpty())
		{
		
				this.tipoDestinoRecurso.add("Evento");
				this.tipoDestinoRecurso.add("Trabajo Social");
		}
		return tipoDestinoRecurso;
	}
	
	public void setTipoDestinoRecurso(List<String> tipoDestinoRecurso)
	{
		this.tipoDestinoRecurso = tipoDestinoRecurso;
	}

	@Command("buscarDonativoRecurso")
	public void buscarDonativoRecurso() {
		CatalogueDialogData<DonativoRecurso> catalogueDialogData = new CatalogueDialogData<DonativoRecurso>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<DonativoRecurso>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<DonativoRecurso> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						donativoRecurso = catalogueDialogCloseEvent.getEntity();

						refreshDonativoRecurso();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/donativo/asignacion/catalogoDonativoRecurso.zul",
						catalogueDialogData);
	}



	public void refreshDonativoRecurso() {
		BindUtils.postNotifyChange(null, null, this, "dontivoRecurso");
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
				.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard2.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(2, listOperacionWizard2);
		
		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CANCELAR));
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.SIGUIENTE));

		botones.put(3, listOperacionWizard3);

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		listOperacionWizard4.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.FINALIZAR));

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
		iconos.add("fa fa-check-square-o");
		iconos.add("fa fa-pencil-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/donativo/asignacion/AsignacionRecursoFormBasic.zul");
		urls.add("views/desktop/gestion/donativo/asignacion/selectDestinoRecurso.zul");
		urls.add("views/desktop/gestion/donativo/asignacion/selectEventoPlanificado.zul");
		urls.add("views/desktop/gestion/donativo/asignacion/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/donativo/asignacion/registroCompletado.zul");

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
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Recurso</b>";
			}
		}

		if (currentStep == 2) {
			return "E:Error Code 5-Debe seleccionar el destino del Recurso";
		}
		
		if (currentStep == 2) {
			return "E:Error Code 5-Debe seleccionar un Item";
		}
		
		if (currentStep == 4) {
			return "E:Error Code 5-No hay otro paso";
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
