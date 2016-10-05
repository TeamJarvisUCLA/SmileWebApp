package ve.smile.gestion.ayudas.beneficiarios_y_familiares;

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
import ve.smile.dto.BeneficiarioFamiliar;
import ve.smile.dto.BeneficiarioFamiliar;
import ve.smile.dto.Persona;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.Voluntario;
import ve.smile.enums.TipoMultimediaEnum;
import ve.smile.payload.response.PayloadMultimediaResponse;
import ve.smile.payload.response.PayloadBeneficiarioFamiliarResponse;
import ve.smile.payload.response.PayloadBeneficiarioFamiliarResponse;
import ve.smile.payload.response.PayloadPersonaResponse;
import ve.smile.payload.response.PayloadBeneficiarioResponse;
import app.UploadImageSingle;

public class VM_BeneficiarioYFamiliarIndex extends
		VM_WindowWizard<Beneficiario> implements
		UploadImageSingle {

	private BeneficiarioFamiliar beneficiarioFamiliar;
	
	private Date fecha = new Date();
	

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		beneficiarioFamiliar = new BeneficiarioFamiliar();
		fecha = new Date();
	}

	

	public BeneficiarioFamiliar getBeneficiarioFamiliar() {
		return beneficiarioFamiliar;
	}

	public void setBeneficiarioFamiliar(BeneficiarioFamiliar beneficiarioFamiliar) {
		this.beneficiarioFamiliar = beneficiarioFamiliar;
	}


	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

		return botones;
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

		urls.add("views/desktop/gestion/ayudas/beneficiarioYFamiliares/selectBeneficiario.zul");
		urls.add("views/desktop/gestion/ayudas/beneficiarioYFamiliares/BeneficiarioYFamiliarFormBasic.zul");
		// urls.add("views/desktop/gestion/trabajoSocial/planificacion/registro/successRegistroBeneficiarioFamiliarPlanificado.zul");

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
	public IPayloadResponse<Beneficiario> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadBeneficiarioResponse payloadBeneficiarioResponse = S.BeneficiarioService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadBeneficiarioResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		
		
		
		
		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		
		
		
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			//this.getBeneficiarioFamiliar().setFecha(this.getFecha().getTime());
			
			this.getBeneficiarioFamiliar().setFkBeneficiario(selectedObject);
			
			
			PayloadBeneficiarioFamiliarResponse payloadBeneficiarioFamiliarResponse = S.BeneficiarioFamiliarService
					.incluir(this.beneficiarioFamiliar);
			if (UtilPayload.isOK(payloadBeneficiarioFamiliarResponse)) {
				restartWizard();
				this.setBeneficiarioFamiliar(new BeneficiarioFamiliar());
				this.setFecha(new Date());
				this.setSelectedObject(new Beneficiario());
				BindUtils.postNotifyChange(null, null, this, "beneficiarioFamiliar");
				BindUtils
						.postNotifyChange(null, null, this, "fecha");
				BindUtils.postNotifyChange(null, null, this, "selectedObject");
				BindUtils.postNotifyChange(null, null, this, "solicitudAyuda");
			}
			return (String) payloadBeneficiarioFamiliarResponse
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
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onRemoveImageSingle(String idUpload) {
		// TODO Auto-generated method stub
		
	}


}
