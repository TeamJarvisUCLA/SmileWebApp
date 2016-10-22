package ve.smile.gestion.evento.actualizacionEventPs.viewmodels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import app.UploadImageSingle;
import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import ve.smile.consume.services.S;
import ve.smile.seguridad.enums.OperacionEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.dto.Directorio;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Voluntario;

public class VM_EventoPlanificado extends VM_WindowForm implements UploadImageSingle{

	private Date fechaPlanificada;
	
	@Init(superclass = true)
	public void childInit() {
		//NOTHING OK!
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR) ||
				operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if(!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse =
					S.EventoPlanificadoService.incluir(getEventoPlanificado());

			if(!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				Alert.showMessage(payloadEventoPlanificadoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse =
					S.EventoPlanificadoService.modificar(getEventoPlanificado());

			if(!UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				Alert.showMessage(payloadEventoPlanificadoResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
	}
	
	@Command("buscarVoluntario")
	public void buscarVoluntario() {
		CatalogueDialogData<Voluntario> catalogueDialogData = new CatalogueDialogData<Voluntario>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Voluntario>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Voluntario> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}

						getEventoPlanificado().setFkPersona(catalogueDialogCloseEvent.getEntity().getFkPersona());

						refreshVoluntario();
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/registro/catalogoVoluntario.zul",
						catalogueDialogData);
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
						getEventoPlanificado().setFkDirectorio(catalogueDialogCloseEvent.getEntity());
						refreshDirectorio();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/evento/planificacion/registro/catalogoDirectorio.zul",
						catalogueDialogData);
	}
	
	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
	}

	public void refreshDirectorio() {
		BindUtils.postNotifyChange(null, null, this, "eventoPlanificado");
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

	public EventoPlanificado getEventoPlanificado() {
		return (EventoPlanificado) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		//TODO
		return true;
	}

	@Override
	public BufferedImage getImageContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getFechaPlanificada() {
		return new Date(this.getEventoPlanificado().getFechaPlanificada());
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}

	
	
	

}
