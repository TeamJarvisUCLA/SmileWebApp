package ve.smile.gestion.ayudas.solicitudes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Ayuda;
import ve.smile.dto.Beneficiario;
import ve.smile.dto.BeneficiarioFamiliar;
import ve.smile.dto.SolicitudAyuda;
import ve.smile.enums.EstatusSolicitudEnum;
import ve.smile.enums.UrgenciaEnum;
import ve.smile.payload.response.PayloadSolicitudAyudaResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_SolicitudesFormBasic extends VM_WindowForm {

	private List<EstatusSolicitudEnum> estatusSolicitudEnums;
	private EstatusSolicitudEnum estatusSolicitudEnum;

	private List<UrgenciaEnum> urgenciaEnums;
	private UrgenciaEnum urgenciaEnum;

	private SolicitudAyuda solicitudAyuda;
	
	private Beneficiario beneficiario = new Beneficiario();

	private Date fecha;

	private BeneficiarioFamiliar beneficiarioFamiliar = new BeneficiarioFamiliar();

	private Ayuda ayuda = new Ayuda();

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!

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

	public void refreshBeneficiarioFamiliar() {
		this.getSolicitudAyuda().setFkBeneficiario(this.getBeneficiario());
		BindUtils.postNotifyChange(null, null, this, "beneficiarioFamiliar");
		BindUtils.postNotifyChange(null, null, this, "solicitudAyuda");
	}

	@Command("buscarBeneficiarioFamiliar")
	public void buscarBeneficiarioFamiliar() {
		CatalogueDialogData<BeneficiarioFamiliar> catalogueDialogData = new CatalogueDialogData<BeneficiarioFamiliar>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<BeneficiarioFamiliar>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<BeneficiarioFamiliar> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						beneficiarioFamiliar = catalogueDialogCloseEvent.getEntity();
						refreshBeneficiarioFamiliar();

					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/ayudas/solicitudes/catalogoBeneficiarioFamiliar.zul",
						catalogueDialogData);
	}

	public void refreshAyuda() {
		this.getSolicitudAyuda().setFkAyuda(this.getAyuda());
		BindUtils.postNotifyChange(null, null, this, "ayuda");
		BindUtils.postNotifyChange(null, null, this, "solicitudAyuda");
	}

	@Command("buscarAyuda")
	public void buscarAyuda() {
		CatalogueDialogData<Ayuda> catalogueDialogData = new CatalogueDialogData<Ayuda>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Ayuda>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Ayuda> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						ayuda = catalogueDialogCloseEvent.getEntity();
						refreshAyuda();

					}
				});

		UtilDialog.showDialog(
				"views/desktop/gestion/ayudas/solicitudes/catalogoAyuda.zul",
				catalogueDialogData);
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (operacionEnum.equals(OperacionEnum.INCLUIR)
				|| operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.CANCELAR));

			return operacionesForm;
		}

		if (operacionEnum.equals(OperacionEnum.CONSULTAR)) {
			operacionesForm.add(OperacionFormHelper
					.getPorType(OperacionFormEnum.SALIR));

			return operacionesForm;
		}

		return operacionesForm;

	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum) {
		if (!isFormValidated()) {
			return true;
		}

		if (operacionEnum.equals(OperacionEnum.INCLUIR)) {

			SolicitudAyuda solicitudAyuda = this.getSolicitudAyuda();
			solicitudAyuda.setFkBeneficiario(this.getBeneficiarioFamiliar().getFkBeneficiario());
			solicitudAyuda.setFecha(new Date().getTime());

			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.incluir(getSolicitudAyuda());

			if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				Alert.showMessage(payloadSolicitudAyudaResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {

			SolicitudAyuda solicitudAyuda = this.getSolicitudAyuda();
			solicitudAyuda.setFkBeneficiario(this.getBeneficiarioFamiliar().getFkBeneficiario());
			solicitudAyuda.setFecha(new Date().getTime());

			PayloadSolicitudAyudaResponse payloadSolicitudAyudaResponse = S.SolicitudAyudaService
					.modificar(solicitudAyuda);

			if (!UtilPayload.isOK(payloadSolicitudAyudaResponse)) {
				Alert.showMessage(payloadSolicitudAyudaResponse);
				return true;
			}

			DataCenter.reloadCurrentNodoMenu();

			return true;
		}

		return false;
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

	public SolicitudAyuda getSolicitudAyuda() {
		return (SolicitudAyuda) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		try {
			UtilValidate.validateString(getSolicitudAyuda().getObservacion(),
					"Observación", 100);
			UtilValidate.validateString(getSolicitudAyuda().getObservacion(),
					"Observación", 100);

			UtilValidate.validateNull(getAyuda(), "Ayuda");
			return true;
		} catch (Exception e) {
			Alert.showMessage(e.getMessage());

			return false;
		}
	}

	public BeneficiarioFamiliar getBeneficiarioFamiliar() {
		return beneficiarioFamiliar;
	}

	public void setBeneficiarioFamiliar(BeneficiarioFamiliar beneficiarioFamiliar) {
		this.beneficiarioFamiliar = beneficiarioFamiliar;
	}

	public Ayuda getAyuda() {
		return ayuda;
	}

	public void setAyuda(Ayuda ayuda) {
		this.ayuda = ayuda;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

}
