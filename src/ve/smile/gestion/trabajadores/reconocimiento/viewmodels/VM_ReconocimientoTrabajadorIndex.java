package ve.smile.gestion.trabajadores.reconocimiento.viewmodels;

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

import karen.core.crux.alert.Alert;
import karen.core.simple_list.wizard.buttons.data.OperacionWizard;
import karen.core.simple_list.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.simple_list.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.simple_list.wizard.viewmodels.VM_WindowWizard;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.Trabajador;
import ve.smile.payload.response.PayloadTrabajadorResponse;
import app.UploadImageSingle;

public class VM_ReconocimientoTrabajadorIndex extends
		VM_WindowWizard<Trabajador> implements UploadImageSingle {

	
	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
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

		return botones;
	}


	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();
		
		iconos.add("fa fa-user");
		iconos.add("fa fa-star");
		
		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();
		
		urls.add("views/desktop/gestion/trabajadores/reconocimiento/selectTrabajador.zul");
		urls.add("views/desktop/gestion/trabajadores/reconocimiento/reconocimiento.zul");
		
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
	public IPayloadResponse<Trabajador> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadTrabajadorResponse payloadTrabajadorResponse = S.TrabajadorService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTrabajadorResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajador</b>";
			}
		}

		if (currentStep == 2) {
				return "E:Error Code 5-Debe responder la interrogante planteada";
		}

		return "";
	}

//	@Override
//	public String isValidPreconditionsFinalizar(Integer currentStep) {
		//TODO: No he validado porque estaba dando error segun me informaron
//	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			//TODO: Funcionalidad de la segunda vista
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