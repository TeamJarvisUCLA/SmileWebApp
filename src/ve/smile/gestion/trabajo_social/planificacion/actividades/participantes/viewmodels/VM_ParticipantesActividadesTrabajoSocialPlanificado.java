package ve.smile.gestion.trabajo_social.planificacion.actividades.participantes.viewmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Trabajador;
import ve.smile.dto.TsPlan;
import ve.smile.dto.TsPlanActividad;
import ve.smile.dto.TsPlanActividadTrabajador;
import ve.smile.dto.TsPlanActividadVoluntario;
import ve.smile.dto.Voluntario;
import ve.smile.payload.response.PayloadTsPlanActividadResponse;
import ve.smile.payload.response.PayloadTsPlanActividadTrabajadorResponse;
import ve.smile.payload.response.PayloadTsPlanActividadVoluntarioResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_ParticipantesActividadesTrabajoSocialPlanificado extends
		VM_WindowWizard {

	private List<TsPlanActividad> listTsPlanActividads;
	private int indexActividad;

	private List<TsPlanActividadTrabajador> listTsPlanActividadTrabajadors;

	private List<TsPlanActividadVoluntario> listTsPlanActividadVoluntarios;

	public List<TsPlanActividad> getListTsPlanActividads() {
		if (this.listTsPlanActividads == null) {
			listTsPlanActividads = new ArrayList<>();
		}
		return listTsPlanActividads;
	}

	public void setListTsPlanActividads(
			List<TsPlanActividad> listTsPlanActividads) {

		this.listTsPlanActividads = listTsPlanActividads;
	}

	public int getIndexActividad() {
		return indexActividad;
	}

	public void setIndexActividad(int indexActividad) {
		this.indexActividad = indexActividad;
	}

	public List<TsPlanActividadTrabajador> getListTsPlanActividadTrabajadors() {
		if (this.listTsPlanActividadTrabajadors == null) {
			listTsPlanActividadTrabajadors = new ArrayList<>();
		}
		return listTsPlanActividadTrabajadors;
	}

	public void setListTsPlanActividadTrabajadors(
			List<TsPlanActividadTrabajador> listTsPlanActividadTrabajadors) {
		this.listTsPlanActividadTrabajadors = listTsPlanActividadTrabajadors;
	}

	public List<TsPlanActividadVoluntario> getListTsPlanActividadVoluntarios() {
		if (this.listTsPlanActividadVoluntarios == null) {
			listTsPlanActividadVoluntarios = new ArrayList<>();
		}
		return listTsPlanActividadVoluntarios;
	}

	public void setListTsPlanActividadVoluntarios(
			List<TsPlanActividadVoluntario> listTsPlanActividadVoluntarios) {
		this.listTsPlanActividadVoluntarios = listTsPlanActividadVoluntarios;
	}

	@Init(superclass = true)
	public void childInit() {

	}

	@Command("buscarVoluntarios")
	public void buscarVoluntarios(@BindingParam("index") int index) {
		this.setIndexActividad(index);
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
						List<Voluntario> listVoluntarios = new ArrayList<Voluntario>();
						listVoluntarios = catalogueDialogCloseEvent
								.getEntities();

						refreshVoluntarios(listVoluntarios);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/catalogoVoluntarios.zul",
						catalogueDialogData);
	}

	public void refreshVoluntarios(List<Voluntario> listVoluntarios) {

		boolean validar = true;
		TsPlanActividadVoluntario tsPlanActividadVoluntario = new TsPlanActividadVoluntario();

		for (Voluntario voluntario : listVoluntarios) {

			for (TsPlanActividadVoluntario tsPlanActividadVoluntario2 : listTsPlanActividads
					.get(indexActividad).getTsPlanActividadVoluntarios()) {
				if (tsPlanActividadVoluntario2.getFkVoluntario()
						.getIdVoluntario().equals(voluntario.getIdVoluntario())) {
					validar = false;
				}
			}

			if (validar) {
				tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
				tsPlanActividadVoluntario
						.setFkTsPlanActividad(listTsPlanActividads
								.get(indexActividad));

				tsPlanActividadVoluntario.setFkVoluntario(voluntario);
				this.listTsPlanActividads.get(indexActividad)
						.getTsPlanActividadVoluntarios()
						.add(tsPlanActividadVoluntario);
			}

			validar = true;
		}

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarVoluntario")
	public void eliminarVoluntario(
			@BindingParam("tsPlanActividadVoluntario") TsPlanActividadVoluntario tsPlanActividadVoluntario,
			@BindingParam("index") int index) {
		this.getListTsPlanActividadVoluntarios().add(tsPlanActividadVoluntario);
		this.getListTsPlanActividads().get(index)
				.getTsPlanActividadVoluntarios()
				.remove(tsPlanActividadVoluntario);
		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	// Trabajadores
	@Command("buscarTrabajadores")
	public void buscarTrabajadores(@BindingParam("index") int index) {
		this.setIndexActividad(index);
		CatalogueDialogData<Trabajador> catalogueDialogData = new CatalogueDialogData<Trabajador>();

		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Trabajador>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<Trabajador> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						List<Trabajador> listTrabajadors = new ArrayList<>();
						listTrabajadors = catalogueDialogCloseEvent
								.getEntities();

						refreshTrabajadores(listTrabajadors);
					}
				});

		UtilDialog
				.showDialog(
						"views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/catalogoTrabajadores.zul",
						catalogueDialogData);
	}

	public void refreshTrabajadores(List<Trabajador> lisTrabajadors) {

		boolean validar = true;
		TsPlanActividadTrabajador tsPlanActividadTrabajador = new TsPlanActividadTrabajador();

		for (Trabajador trabajador : lisTrabajadors) {
			for (TsPlanActividadTrabajador tsPlanActividadTrabajador2 : listTsPlanActividads
					.get(indexActividad).getTsPlanActividadTrabajadors()) {
				if (tsPlanActividadTrabajador2.getFkTrabajador()
						.getIdTrabajador().equals(trabajador.getIdTrabajador())) {
					validar = false;
					break;
				}
			}

			if (validar) {
				tsPlanActividadTrabajador = new TsPlanActividadTrabajador();
				tsPlanActividadTrabajador.setFkTsPlanActividad(this
						.getListTsPlanActividads().get(indexActividad));
				tsPlanActividadTrabajador.setFkTrabajador(trabajador);

				this.getListTsPlanActividads().get(indexActividad)
						.getTsPlanActividadTrabajadors()
						.add(tsPlanActividadTrabajador);
			}
			validar = true;
		}

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarTrabajador")
	public void eliminarTrabajador(
			@BindingParam("tsPlanActividadTrabajador") TsPlanActividadTrabajador tsPlanActividadTrabajador,
			@BindingParam("index") int index) {
		this.getListTsPlanActividadTrabajadors().add(tsPlanActividadTrabajador);
		this.getListTsPlanActividads().get(index)
				.getTsPlanActividadTrabajadors()
				.remove(tsPlanActividadTrabajador);
		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
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

		List<OperacionWizard> listOperacionWizard4 = new ArrayList<OperacionWizard>();
		OperacionWizard operacionWizardCustom = new OperacionWizard(
				OperacionWizardEnum.CUSTOM1.ordinal(), "Aceptar", "Custom1",
				"fa fa-check", "indigo", "Aceptar");
		listOperacionWizard4.add(operacionWizardCustom);

		botones.put(3, listOperacionWizard4);

		return botones;
	}

	@Override
	public IPayloadResponse<TsPlan> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {
		PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadTsPlanResponse;
	}

	@Override
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-list-alt");
		iconos.add("fa fa-pencil-square-o");
		iconos.add("fa fa-check-square-o");

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/selectTrabajoSocialPlanificado.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/actividadesParticipantes.zul");
		urls.add("views/desktop/gestion/trabajoSocial/planificacion/actividades/participantes/registroCompletado.zul");

		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			Map<String, String> parametro = new HashMap<String, String>();
			parametro.put("fkTsPlan.idTsPlan",
					String.valueOf(getTsPlanSelected().getIdTsPlan()));

			this.setListTsPlanActividads(null);
			PayloadTsPlanActividadResponse payloadTsPlanActividadResponse = S.TsPlanActividadService
					.consultarCriterios(TypeQuery.EQUAL, parametro);
			if (UtilPayload.isOK(payloadTsPlanActividadResponse)) {
				this.getListTsPlanActividads().addAll(
						payloadTsPlanActividadResponse.getObjetos());
			}

			for (TsPlanActividad tsPlanActividad : this
					.getListTsPlanActividads()) {

				Map<String, String> criterios = new HashMap<String, String>();
				criterios.put("fkTsPlanActividad.idTsPlanActividad",
						String.valueOf(tsPlanActividad.getIdTsPlanActividad()));
				PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadVoluntarioResponse)) {
					tsPlanActividad
							.setTsPlanActividadVoluntarios(payloadTsPlanActividadVoluntarioResponse
									.getObjetos());
				}

				PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				if (UtilPayload.isOK(payloadTsPlanActividadTrabajadorResponse)) {

					tsPlanActividad
							.setTsPlanActividadTrabajadors(payloadTsPlanActividadTrabajadorResponse
									.getObjetos());
				}

			}

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
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (selectedObject == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificado</b>";
			}
		}

		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		goToNextStep();
		return "";
	}

	@Override
	public String isValidSearchDataFinalizar(Integer currentStep) {
		if (currentStep == 2) {
			PayloadTsPlanActividadTrabajadorResponse payloadTsPlanActividadTrabajadorResponse = new PayloadTsPlanActividadTrabajadorResponse();
			for (TsPlanActividad obj : this.getListTsPlanActividads()) {

				for (TsPlanActividadTrabajador tsPlanActividadTrabajador : obj
						.getTsPlanActividadTrabajadors()) {
					TsPlanActividadTrabajador tsPlanActividadTrabajador2 = new TsPlanActividadTrabajador(
							tsPlanActividadTrabajador.getFkTrabajador(),
							new TsPlanActividad(obj.getIdTsPlanActividad()),
							tsPlanActividadTrabajador.getEjecucion(), null,
							tsPlanActividadTrabajador.getParticipacion(),
							tsPlanActividadTrabajador.getObservacion());
					tsPlanActividadTrabajador2
							.setIdTsPlanActividadTrabajador(tsPlanActividadTrabajador
									.getIdTsPlanActividadTrabajador());
					if (tsPlanActividadTrabajador2
							.getIdTsPlanActividadTrabajador() == null) {

						payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
								.incluir(tsPlanActividadTrabajador2);
					} else {
						payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
								.modificar(tsPlanActividadTrabajador2);
					}

					if (!UtilPayload
							.isOK(payloadTsPlanActividadTrabajadorResponse)) {
						return (String) payloadTsPlanActividadTrabajadorResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}

			}
			for (TsPlanActividadTrabajador tsPlanActividadTrabajador : this
					.getListTsPlanActividadTrabajadors()) {
				if (tsPlanActividadTrabajador.getIdTsPlanActividadTrabajador() != null) {
					payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
							.eliminar(tsPlanActividadTrabajador
									.getIdTsPlanActividadTrabajador());
				}

			}

			PayloadTsPlanActividadVoluntarioResponse payloadTsPlanActividadVoluntarioResponse = new PayloadTsPlanActividadVoluntarioResponse();
			for (TsPlanActividad obj : this.getListTsPlanActividads()) {

				for (TsPlanActividadVoluntario tsPlanActividadVoluntario : obj
						.getTsPlanActividadVoluntarios()) {
					TsPlanActividadVoluntario tsPlanActividadVoluntario2 = new TsPlanActividadVoluntario(
							new TsPlanActividad(obj.getIdTsPlanActividad()),
							tsPlanActividadVoluntario.getFkVoluntario(),
							tsPlanActividadVoluntario.getEjecucion(), null,
							tsPlanActividadVoluntario.getParticipacion(),
							tsPlanActividadVoluntario.getObservacion());
					tsPlanActividadVoluntario2
							.setIdTsPlanActividadVoluntario(tsPlanActividadVoluntario
									.getIdTsPlanActividadVoluntario());
					if (tsPlanActividadVoluntario2
							.getIdTsPlanActividadVoluntario() == null) {

						payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
								.incluir(tsPlanActividadVoluntario2);
					} else {
						payloadTsPlanActividadVoluntarioResponse = S.TsPlanActividadVoluntarioService
								.modificar(tsPlanActividadVoluntario2);
					}

					if (!UtilPayload
							.isOK(payloadTsPlanActividadVoluntarioResponse)) {
						return (String) payloadTsPlanActividadVoluntarioResponse
								.getInformacion(IPayloadResponse.MENSAJE);
					}
				}

			}
			for (TsPlanActividadVoluntario tsPlanActividadVoluntario : this
					.getListTsPlanActividadVoluntarios()) {
				if (tsPlanActividadVoluntario.getIdTsPlanActividadVoluntario() != null) {
					payloadTsPlanActividadTrabajadorResponse = S.TsPlanActividadTrabajadorService
							.eliminar(tsPlanActividadVoluntario
									.getIdTsPlanActividadVoluntario());
				}

			}

			goToNextStep();
		}
		return "";
	}

	@Override
	public String executeCustom1(Integer currentStep) {
		executeCancelar(currentStep);
		return "";

	}

	@Override
	public String executeCancelar(Integer currentStep) {
		this.setListTsPlanActividads(null);
		this.setListTsPlanActividadTrabajadors(null);
		this.setListTsPlanActividadVoluntarios(null);
		BindUtils.postNotifyChange(null, null, this, "*");

		restartWizard();
		return "";
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
