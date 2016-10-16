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
		List<TsPlanActividadVoluntario> listAux2 = new ArrayList<>();
		TsPlanActividadVoluntario tsPlanActividadVoluntario = new TsPlanActividadVoluntario();

		if (listTsPlanActividads.get(indexActividad)
				.getTsPlanActividadVoluntarios() != null) {
			for (TsPlanActividadVoluntario tsPlanAVoluntario : listTsPlanActividads
					.get(indexActividad).getTsPlanActividadVoluntarios()) {
				tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
				tsPlanActividadVoluntario
						.setFkTsPlanActividad(listTsPlanActividads
								.get(indexActividad));
				tsPlanActividadVoluntario.setFkVoluntario(tsPlanAVoluntario
						.getFkVoluntario());
				listAux2.add(tsPlanActividadVoluntario);
			}
		}

		for (Voluntario voluntario : listVoluntarios) {
			if (listTsPlanActividads.get(indexActividad)
					.getTsPlanActividadVoluntarios() != null) {

				for (TsPlanActividadVoluntario iet : listTsPlanActividads.get(
						indexActividad).getTsPlanActividadVoluntarios()) {
					if (iet.getFkVoluntario().getIdVoluntario()
							.equals(voluntario.getIdVoluntario())) {

						validar = false;

					}
				}

				if (validar) {
					tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
					tsPlanActividadVoluntario
							.setFkTsPlanActividad(listTsPlanActividads
									.get(indexActividad));

					tsPlanActividadVoluntario.setFkVoluntario(voluntario);
					listAux2.add(tsPlanActividadVoluntario);

				}

			} else {
				tsPlanActividadVoluntario = new TsPlanActividadVoluntario();
				tsPlanActividadVoluntario
						.setFkTsPlanActividad(listTsPlanActividads
								.get(indexActividad));

				tsPlanActividadVoluntario.setFkVoluntario(voluntario);
				listAux2.add(tsPlanActividadVoluntario);

			}

		}

		this.listTsPlanActividads.get(indexActividad)
				.setTsPlanActividadVoluntarios(listAux2);

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarVoluntario")
	public void eliminarVoluntario(
			@BindingParam("tsPlanActividadVoluntario") TsPlanActividadVoluntario tsPlanActividadVoluntario,
			@BindingParam("index") int index) {
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
		List<TsPlanActividadTrabajador> listAux2 = new ArrayList<>();
		TsPlanActividadTrabajador tsPlanActividadTrabajador = new TsPlanActividadTrabajador();

		if (listTsPlanActividads.get(indexActividad)
				.getTsPlanActividadVoluntarios() != null) {
			for (TsPlanActividadTrabajador tsPlanActividadTrabajador2 : listTsPlanActividads
					.get(indexActividad).getTsPlanActividadTrabajadors()) {
				tsPlanActividadTrabajador = new TsPlanActividadTrabajador();
				tsPlanActividadTrabajador
						.setFkTsPlanActividad(listTsPlanActividads
								.get(indexActividad));
				tsPlanActividadTrabajador
						.setFkTrabajador(tsPlanActividadTrabajador2
								.getFkTrabajador());
				listAux2.add(tsPlanActividadTrabajador);
			}
		}

		for (Trabajador trabajador : lisTrabajadors) {
			if (listTsPlanActividads.get(indexActividad)
					.getTsPlanActividadVoluntarios() != null) {

				for (TsPlanActividadTrabajador iet : listTsPlanActividads.get(
						indexActividad).getTsPlanActividadTrabajadors()) {
					if (iet.getFkTrabajador().getIdTrabajador()
							.equals(trabajador.getIdTrabajador())) {

						validar = false;

					}
				}

				if (validar) {
					tsPlanActividadTrabajador = new TsPlanActividadTrabajador();
					tsPlanActividadTrabajador
							.setFkTsPlanActividad(listTsPlanActividads
									.get(indexActividad));

					tsPlanActividadTrabajador.setFkTrabajador(trabajador);
					listAux2.add(tsPlanActividadTrabajador);

				}

			} else {
				tsPlanActividadTrabajador = new TsPlanActividadTrabajador();
				tsPlanActividadTrabajador
						.setFkTsPlanActividad(listTsPlanActividads
								.get(indexActividad));

				tsPlanActividadTrabajador.setFkTrabajador(trabajador);
				listAux2.add(tsPlanActividadTrabajador);

			}

		}

		this.listTsPlanActividads.get(indexActividad)
				.setTsPlanActividadTrabajadors(listAux2);

		BindUtils.postNotifyChange(null, null, this, "listTsPlanActividads");
	}

	@Command("eliminarTrabajador")
	public void eliminarTrabajador(
			@BindingParam("tsPlanActividadTrabajador") TsPlanActividadTrabajador tsPlanActividadTrabajador,
			@BindingParam("index") int index) {
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

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.CUSTOM1));

		botones.put(3, listOperacionWizard3);

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

		}
		return "";
	}

	@Override
	public String executeCancelar(Integer currentStep) {
		restartWizard();
		return super.executeCancelar(currentStep);
	}

	public TsPlan getTsPlanSelected() {
		return (TsPlan) this.getSelectedObject();
	}

}
