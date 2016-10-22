package ve.smile.gestion.donativo.registro.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.util.UtilDialog;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;
import karen.core.util.validate.UtilValidate.ValidateOperator;
import karen.core.wizard.buttons.data.OperacionWizard;
import karen.core.wizard.buttons.enums.OperacionWizardEnum;
import karen.core.wizard.buttons.helpers.OperacionWizardHelper;
import karen.core.wizard.viewmodels.VM_WindowWizard;
import lights.core.enums.TypeQuery;
import lights.core.payload.response.IPayloadResponse;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.Colaborador;
import ve.smile.dto.CuentaBancaria;
import ve.smile.dto.Directorio;
import ve.smile.dto.DonativoCuentaBancaria;
import ve.smile.dto.DonativoRecurso;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Padrino;
import ve.smile.dto.Patrocinador;
import ve.smile.dto.Recurso;
import ve.smile.dto.TsPlan;
import ve.smile.enums.ProcedenciaEnum;
import ve.smile.enums.PropietarioEnum;
import ve.smile.enums.RecepcionEnum;
import ve.smile.enums.TipoDonativoCuentaBancariaEnum;
import ve.smile.enums.UnidadFrecuenciaAporteEnum;
import ve.smile.payload.response.PayloadColaboradorResponse;
import ve.smile.payload.response.PayloadDonativoCuentaBancariaResponse;
import ve.smile.payload.response.PayloadDonativoRecursoResponse;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;
import ve.smile.payload.response.PayloadPadrinoResponse;
import ve.smile.payload.response.PayloadPatrocinadorResponse;
import ve.smile.payload.response.PayloadRecursoResponse;
import ve.smile.payload.response.PayloadTsPlanResponse;

public class VM_RegistroDonativoIndex extends VM_WindowWizard {

	private DonativoRecurso donativoRecurso;
	private DonativoCuentaBancaria donativoCuentaBancaria;

	private List<ProcedenciaEnum> tipoProcedenciaEnums;
	private ProcedenciaEnum procedenciaEnums;

	private List<RecepcionEnum> recepcionEnums;
	private RecepcionEnum recepcionEnum;

	private Long nroReferencia;

	private EventoPlanificado eventoPlanificado;
	private TsPlan tsPlan;
	private Padrino padrino;
	private Colaborador colaborador;
	private Patrocinador patrocinador;
	private Directorio directorio;

	private List<EventoPlanificado> eventoPlanificados;

	private List<TsPlan> tsPlans;
	private List<Padrino> padrinos;
	private List<Colaborador> colaboradores;
	private List<Patrocinador> patrocinadores;

	private String srcList;
	private List<Recurso> recursos;

	private CuentaBancaria cuentaBancariaDestinataria;
	private CuentaBancaria cuentaBancariaRemitente;

	private Date fechaDonativo;

	private Date fechaAporte;

	private Recurso recurso;

	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		donativoRecurso = new DonativoRecurso();
		donativoCuentaBancaria = new DonativoCuentaBancaria();
		tsPlan = new TsPlan();
		eventoPlanificado = new EventoPlanificado();
		padrino = new Padrino();
		colaborador = new Colaborador();
		patrocinador = new Patrocinador();
		directorio = new Directorio();
		this.setFechaDonativo(new Date());
	}

	public void setTsPlan(TsPlan tsPlan) {
		this.tsPlan = tsPlan;
	}

	public TsPlan getTsPlan() {
		return tsPlan;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setPatrocindor(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}

	public Patrocinador getPatrocinador() {
		return patrocinador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setPadrino(Padrino padrino) {
		this.padrino = padrino;
	}

	public Padrino getPadrino() {
		return padrino;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public EventoPlanificado getEventoPlanificado() {
		return eventoPlanificado;
	}

	public ProcedenciaEnum getProcedenciaEnums() {
		return procedenciaEnums;
	}

	public void setProcedenciaEnums(ProcedenciaEnum procedenciaEnums) {
		this.procedenciaEnums = procedenciaEnums;
		if (procedenciaEnums != null) {
			this.getDonativoRecurso()
					.setProcedencia(procedenciaEnums.ordinal());
		}
	}

	public DonativoRecurso getDonativoRecurso() {
		return donativoRecurso;
	}

	public void setDonativoRecurso(DonativoRecurso donativoRecurso) {
		this.donativoRecurso = donativoRecurso;
	}

	public DonativoCuentaBancaria getDonativoCuentaBancaria() {
		return donativoCuentaBancaria;
	}

	public void setDonativoCuentaBancaria(
			DonativoCuentaBancaria donativoCuentaBancaria) {
		this.donativoCuentaBancaria = donativoCuentaBancaria;
	}

	public List<ProcedenciaEnum> getTipoProcedenciaEnums() {
		if (this.tipoProcedenciaEnums == null) {
			this.tipoProcedenciaEnums = new ArrayList<>();
		}
		if (this.tipoProcedenciaEnums.isEmpty()) {
			for (ProcedenciaEnum procedenciaEnums : ProcedenciaEnum.values()) {
				this.tipoProcedenciaEnums.add(procedenciaEnums);
			}
		}
		return tipoProcedenciaEnums;
	}

	public void setTipoProcedenciaEnums(
			List<ProcedenciaEnum> tipoProcedenciaEnums) {
		this.tipoProcedenciaEnums = tipoProcedenciaEnums;
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

		UtilDialog.showDialog(
				"views/desktop/gestion/donativo/registro/catalogoRecurso.zul",
				catalogueDialogData);
	}

	public void refreshRecurso() {
		donativoRecurso.setFkRecurso(recurso);

		BindUtils.postNotifyChange(null, null, this, "donativoRecurso");
	}

	@Command("buscarCuentaBancariaRemitente")
	public void buscarCuentaBancariaRemitente() {

		CatalogueDialogData<CuentaBancaria> catalogueDialogData = new CatalogueDialogData<CuentaBancaria>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<CuentaBancaria>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<CuentaBancaria> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						cuentaBancariaRemitente = catalogueDialogCloseEvent
								.getEntity();
						refresCuentaBancariaRemitente();

					}
				});
		catalogueDialogData.put("PropietarioEnum", PropietarioEnum.DONATIVO);
		UtilDialog
				.showDialog(
						"views/desktop/gestion/donativo/registro/catalogoCuentaBancaria.zul",
						catalogueDialogData);
	}

	public void refresCuentaBancariaRemitente() {
		BindUtils.postNotifyChange(null, null, this, "cuentaBancariaRemitente");
	}

	@Command("buscarCuentaBancariaDestinataria")
	public void buscarCuentaBancariaDestinataria() {

		CatalogueDialogData<CuentaBancaria> catalogueDialogData = new CatalogueDialogData<CuentaBancaria>();
		catalogueDialogData
				.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<CuentaBancaria>() {

					@Override
					public void onClose(
							CatalogueDialogCloseEvent<CuentaBancaria> catalogueDialogCloseEvent) {
						if (catalogueDialogCloseEvent.getDialogAction().equals(
								DialogActionEnum.CANCELAR)) {
							return;
						}
						cuentaBancariaDestinataria = catalogueDialogCloseEvent
								.getEntity();
						refreshCuentaBancariaDestinataria();

					}
				});
		catalogueDialogData
				.put("PropietarioEnum", PropietarioEnum.ORGANIZACION);
		UtilDialog
				.showDialog(
						"views/desktop/gestion/donativo/registro/catalogoCuentaBancaria.zul",
						catalogueDialogData);
	}

	public void refreshCuentaBancariaDestinataria() {
		BindUtils.postNotifyChange(null, null, this,
				"cuentaBancariaDestinataria");
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

		botones.put(2, listOperacionWizard2);

		List<OperacionWizard> listOperacionWizard3 = new ArrayList<OperacionWizard>();
		listOperacionWizard3.add(OperacionWizardHelper
				.getPorType(OperacionWizardEnum.ATRAS));
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
	public List<String> getIconsToStep() {
		List<String> iconos = new ArrayList<String>();

		iconos.add("fa fa-heart"); // seleccionar Procedencia
		iconos.add("fa fa-pencil-square-o"); // seleccionar objeto de la
												// Procedencia
		iconos.add("fa fa-pencil-square-o"); // formulario de registro
		iconos.add("fa fa-check-square-o"); // registro finalizado

		return iconos;
	}

	@Override
	public List<String> getUrlPageToStep() {
		List<String> urls = new ArrayList<String>();

		urls.add("views/desktop/gestion/donativo/registro/selectProcedencia.zul");
		urls.add("views/desktop/gestion/donativo/registro/selectList.zul");
		urls.add("views/desktop/gestion/donativo/registro/registroDonativoFormBasicf.zul");
		urls.add("views/desktop/gestion/donativo/registro/registroCompletado.zul");
		return urls;
	}

	@Override
	public String executeSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (procedenciaEnums.equals(ProcedenciaEnum.COLABORADOR)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectColaborador.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.EVENTO)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectEventoPlanificado.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectPadrino.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PATROCINADOR)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectPatrocinador.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.TRABAJO_SOCIAL)) {
				this.setSrcList("views/desktop/gestion/donativo/registro/selectTrabajoSocialPlanificado.zul");
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.ANONIMO)) {
				goToNextStep();
			}
			BindUtils.postNotifyChange(null, null, this, "*");
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
	public IPayloadResponse<DonativoRecurso> getDataToTable(
			Integer cantidadRegistrosPagina, Integer pagina) {

		PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
				.consultarPaginacion(cantidadRegistrosPagina, pagina);
		return payloadDonativoRecursoResponse;
	}

	@Override
	public String isValidPreconditionsSiguiente(Integer currentStep) {
		if (currentStep == 1) {
			if (procedenciaEnums == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Tipo de Procedencia</b>";
			}
		}

		if (currentStep == 2) {
			if (procedenciaEnums.equals(ProcedenciaEnum.COLABORADOR)
					&& colaborador.getIdColaborador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Colaborador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.EVENTO)
					&& eventoPlanificado.getIdEventoPlanificado() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Evento Planificador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)
					&& padrino.getIdPadrino() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Padrino</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PATROCINADOR)
					&& patrocinador.getIdPatrocinador() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Patrocinador</b>";
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.TRABAJO_SOCIAL)
					&& tsPlan.getIdTsPlan() == null) {
				return "E:Error Code 5-Debe seleccionar un <b>Trabajo Social Planificador</b>";
			}

		}

		if (currentStep == 3) {
			return "E:Error Code 5-Debe llenar todos los campos";
		}

		return "";
	}

	@Override
	public String isValidPreconditionsFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			try {
				Calendar calendar = Calendar.getInstance();

				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				UtilValidate.validateDate(this.getFechaDonativo().getTime(),
						"Fecha de Donativo", ValidateOperator.LESS_THAN,
						new SimpleDateFormat("yyyy-MM-dd").format(calendar
								.getTime()), "dd/MM/yyyy");
				UtilValidate.validateNull(donativoRecurso.getFkRecurso(),
						"Recurso");

				UtilValidate.validateInteger(
						new Integer((int) donativoRecurso.getCantidad()),
						"Cantidad", ValidateOperator.GREATER_THAN, new Integer(
								0));

				UtilValidate.validateInteger(donativoRecurso.getRecepcion(),
						"Lugar de Recepción", ValidateOperator.LESS_THAN,
						RecepcionEnum.OTRO.ordinal() + 1);

				if (this.getRecepcionEnum().equals(
						RecepcionEnum.TRANSACCION_BANCARIA)) {
					UtilValidate.validateNull(cuentaBancariaRemitente,
							"Cuenta Bancaria Remitente");
					UtilValidate.validateNull(cuentaBancariaDestinataria,
							"Cuenta Bancaria Destinataria");
				}

			} catch (Exception e) {
				return e.getMessage();
			}

		}
		return "";
	}

	@Override
	public String executeFinalizar(Integer currentStep) {
		if (currentStep == 3) {
			if (procedenciaEnums.equals(ProcedenciaEnum.COLABORADOR)) {
				donativoRecurso.setFkPersona(this.getColaborador()
						.getFkPersona());
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.EVENTO)) {
				donativoRecurso.setFkEventoPlanificado(eventoPlanificado);
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)) {
				donativoRecurso.setFkPersona(this.getPadrino().getFkPersona());
				if (this.getDonativoRecurso().getAporte()) {
					donativoRecurso
							.setFechaArpoteCorrespondiente(this.fechaAporte
									.getTime());
				}

			}
			if (procedenciaEnums.equals(ProcedenciaEnum.PATROCINADOR)) {
				donativoRecurso.setFkPersona(this.getPatrocinador()
						.getFkPersona());
			}
			if (procedenciaEnums.equals(ProcedenciaEnum.TRABAJO_SOCIAL)) {
				donativoRecurso.setFkTsPlan(tsPlan);
			}

			PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
					.incluir(getDonativoRecurso());
			this.getDonativoRecurso().setIdDonativoRecurso(
					((Double) payloadDonativoRecursoResponse
							.getInformacion("id")).intValue());

			if (!UtilPayload.isOK(payloadDonativoRecursoResponse)) {
				return (String) payloadDonativoRecursoResponse
						.getInformacion(IPayloadResponse.MENSAJE);
			}

			if (recepcionEnum.equals(recepcionEnum.TRANSACCION_BANCARIA)) {
				DonativoCuentaBancaria donativoCuentaBancaria = new DonativoCuentaBancaria();
				donativoCuentaBancaria.setFechaTransaccion(fechaDonativo
						.getTime());
				donativoCuentaBancaria
						.setFkCuentaBancaria(cuentaBancariaDestinataria);
				donativoCuentaBancaria.setFkDonativoRecurso(donativoRecurso);
				donativoCuentaBancaria
						.setTipoDonativoCuentaBancaria(TipoDonativoCuentaBancariaEnum.DESTINATARIO
								.ordinal());
				donativoCuentaBancaria.setNroReferencia(nroReferencia);

				PayloadDonativoCuentaBancariaResponse payloadDonativoCuentaBancariaResponse = S.DonativoCuentaBancariaService
						.incluir(donativoCuentaBancaria);

				if (!UtilPayload.isOK(payloadDonativoCuentaBancariaResponse)) {
					return (String) payloadDonativoCuentaBancariaResponse
							.getInformacion(IPayloadResponse.MENSAJE);
				}
				DonativoCuentaBancaria donativoCuentaBancaria2 = new DonativoCuentaBancaria();
				donativoCuentaBancaria2.setFechaTransaccion(fechaDonativo
						.getTime());
				donativoCuentaBancaria2
						.setFkCuentaBancaria(cuentaBancariaRemitente);
				donativoCuentaBancaria2.setFkDonativoRecurso(donativoRecurso);
				donativoCuentaBancaria2
						.setTipoDonativoCuentaBancaria(TipoDonativoCuentaBancariaEnum.ORIGEN
								.ordinal());
				donativoCuentaBancaria2.setNroReferencia(nroReferencia);
				PayloadDonativoCuentaBancariaResponse payloadDonativoCuentaBancariaResponse2 = S.DonativoCuentaBancariaService
						.incluir(donativoCuentaBancaria2);

				if (!UtilPayload.isOK(payloadDonativoCuentaBancariaResponse2)) {
					return (String) payloadDonativoCuentaBancariaResponse2
							.getInformacion(IPayloadResponse.MENSAJE);
				}
			}

			donativoRecurso = new DonativoRecurso();

			this.setTipoProcedenciaEnums(null);
			this.setProcedenciaEnums(null);

			this.setRecepcionEnums(null);
			this.setRecepcionEnum(null);

			setNroReferencia((long) 0);

			this.setEventoPlanificado(new EventoPlanificado());
			this.setTsPlan(new TsPlan());
			this.setPadrino(new Padrino());
			this.setColaborador(new Colaborador());
			this.setPatrocinador(new Patrocinador());
			this.setDirectorio(new Directorio());

			this.setEventoPlanificados(null);

			this.setTsPlans(null);
			this.setPadrinos(null);
			this.setColaboradores(null);
			this.setPatrocinadores(null);

			this.setSrcList(new String());
			this.setRecursos(null);

			this.setCuentaBancariaDestinataria(new CuentaBancaria());
			this.setCuentaBancariaRemitente(new CuentaBancaria());

			fechaDonativo = new Date();

			fechaAporte = new Date();

			recurso = new Recurso();
			BindUtils.postNotifyChange(null, null, this, "*");
			restartWizard();
			return (String) payloadDonativoRecursoResponse
					.getInformacion(IPayloadResponse.MENSAJE);
		}

		return "";
	}

	@Override
	public String isValidSearchDataSiguiente(Integer currentStep) {
		if (currentStep == 2) {
			if (procedenciaEnums.equals(ProcedenciaEnum.PADRINO)) {
				Map<String, String> criterios = new HashMap<>();
				System.err.println(padrino.getFkPersona().getIdPersona());
				criterios.put("fkPersona.idPersona",
						String.valueOf(padrino.getFkPersona().getIdPersona()));
				PayloadDonativoRecursoResponse payloadDonativoRecursoResponse = S.DonativoRecursoService
						.consultarCriterios(TypeQuery.EQUAL, criterios);
				fechaAporte = null;
				if (UtilPayload.isOK(payloadDonativoRecursoResponse)) {
					System.out.println("ok");
					if (payloadDonativoRecursoResponse.getObjetos() != null
							&& !payloadDonativoRecursoResponse.getObjetos()
									.isEmpty()) {
						System.out.println("if");
						for (DonativoRecurso donativoRecurso : payloadDonativoRecursoResponse
								.getObjetos()) {
							System.out.println("for");
							if (donativoRecurso.getAporte()) {
								fechaAporte = new Date(
										donativoRecurso
												.getFechaArpoteCorrespondiente());
								if (fechaAporte.after(new Date(donativoRecurso
										.getFechaArpoteCorrespondiente()))) {
									fechaAporte = new Date(
											donativoRecurso
													.getFechaArpoteCorrespondiente());
								}
							}

						}

					}
				}
				if (fechaAporte == null) {
					fechaAporte = new Date(padrino.getFechaIngreso());
				} else {
					UnidadFrecuenciaAporteEnum unidadFrecuenciaAporteEnum = padrino
							.getFkFrecuenciaAporte()
							.getUnidadFrecuenciaAporteEnum();
					Integer cantidad = padrino.getFkFrecuenciaAporte()
							.getFrecuencia();

					if (unidadFrecuenciaAporteEnum
							.equals(UnidadFrecuenciaAporteEnum.DIA)) {
						Calendar calendar = Calendar.getInstance();

						calendar.setTime(new Date());
						calendar.add(Calendar.DAY_OF_YEAR, cantidad);
						fechaAporte = calendar.getTime();
					} else if (unidadFrecuenciaAporteEnum
							.equals(UnidadFrecuenciaAporteEnum.ANNO)) {
						Calendar calendar = Calendar.getInstance();

						calendar.setTime(new Date());
						calendar.add(Calendar.YEAR, cantidad);
						fechaAporte = calendar.getTime();
					} else {
						Calendar calendar = Calendar.getInstance();

						calendar.setTime(new Date());
						calendar.add(Calendar.MONTH, cantidad);
						fechaAporte = calendar.getTime();
					}
				}
			}
		}
		return super.isValidSearchDataSiguiente(currentStep);
	}

	public String getSrcList() {
		return srcList;
	}

	public void setSrcList(String srcList) {
		this.srcList = srcList;
	}

	public List<EventoPlanificado> getEventoPlanificados() {
		if (this.eventoPlanificados == null) {
			this.eventoPlanificados = new ArrayList<>();
		}
		if (this.eventoPlanificados.isEmpty()) {
			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				this.eventoPlanificados.addAll(payloadEventoPlanificadoResponse
						.getObjetos());
			}

		}
		return eventoPlanificados;
	}

	public void setEventoPlanificados(List<EventoPlanificado> eventoPlanificados) {
		this.eventoPlanificados = eventoPlanificados;
	}

	public List<TsPlan> getTsPlans() {
		if (this.tsPlans == null) {
			this.tsPlans = new ArrayList<>();
		}
		if (this.tsPlans.isEmpty()) {
			PayloadTsPlanResponse payloadTsPlanResponse = S.TsPlanService
					.consultarTodos();
			this.tsPlans.addAll(payloadTsPlanResponse.getObjetos());
		}
		return tsPlans;
	}

	public void setTsPlans(List<TsPlan> tsPlans) {
		this.tsPlans = tsPlans;
	}

	public List<Padrino> getPadrinos() {
		if (this.padrinos == null) {
			this.padrinos = new ArrayList<>();
		}
		if (this.padrinos.isEmpty()) {
			PayloadPadrinoResponse payloadPadrinoResponse = S.PadrinoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadPadrinoResponse)) {
				this.padrinos.addAll(payloadPadrinoResponse.getObjetos());
			}
		}

		return padrinos;
	}

	public void setPadrinos(List<Padrino> padrinos) {
		this.padrinos = padrinos;
	}

	public List<Colaborador> getColaboradores() {
		if (this.colaboradores == null) {
			this.colaboradores = new ArrayList<>();
		}
		if (this.colaboradores.isEmpty()) {
			PayloadColaboradorResponse payloadColaboradorResponse = S.ColaboradorService
					.consultarTodos();
			if (UtilPayload.isOK(payloadColaboradorResponse)) {
				this.colaboradores.addAll(payloadColaboradorResponse
						.getObjetos());
			}
		}
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public List<Patrocinador> getPatrocinadores() {
		if (this.patrocinadores == null) {
			this.patrocinadores = new ArrayList<>();
		}
		if (this.patrocinadores.isEmpty()) {

			PayloadPatrocinadorResponse payloadPatrocinadorResponse = S.PatrocinadorService
					.consultarTodos();
			if (UtilPayload.isOK(payloadPatrocinadorResponse)) {
				this.patrocinadores.addAll(payloadPatrocinadorResponse
						.getObjetos());
			}
		}

		return patrocinadores;
	}

	public void setPatrocinadores(List<Patrocinador> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public void setPatrocinador(Patrocinador patrocinador) {
		this.patrocinador = patrocinador;
	}

	public List<RecepcionEnum> getRecepcionEnums() {
		if (this.recepcionEnums == null) {
			this.recepcionEnums = new ArrayList<>();
		}
		if (this.recepcionEnums.isEmpty()) {
			for (RecepcionEnum recepcionEnum : RecepcionEnum.values()) {
				this.recepcionEnums.add(recepcionEnum);
			}
		}
		return recepcionEnums;
	}

	public void setRecepcionEnums(List<RecepcionEnum> recepcionEnums) {
		this.recepcionEnums = recepcionEnums;
	}

	public RecepcionEnum getRecepcionEnum() {
		return recepcionEnum;
	}

	public void setRecepcionEnum(RecepcionEnum recepcionEnum) {
		this.recepcionEnum = recepcionEnum;
		if (recepcionEnum != null) {
			this.donativoRecurso.setRecepcion(recepcionEnum.ordinal());
		} else {
			this.donativoRecurso.setRecepcion(null);
		}
	}

	public CuentaBancaria getCuentaBancariaDestinataria() {
		return cuentaBancariaDestinataria;
	}

	public void setCuentaBancariaDestinataria(
			CuentaBancaria cuentaBancariaDestinataria) {
		this.cuentaBancariaDestinataria = cuentaBancariaDestinataria;
	}

	public CuentaBancaria getCuentaBancariaRemitente() {
		return cuentaBancariaRemitente;
	}

	public void setCuentaBancariaRemitente(
			CuentaBancaria cuentaBancariaRemitente) {
		this.cuentaBancariaRemitente = cuentaBancariaRemitente;
	}

	public List<Recurso> getRecursos() {
		if (this.recursos == null) {
			recursos = new ArrayList<>();
		}
		if (this.recursos.isEmpty()) {
			PayloadRecursoResponse payloadRecursoResponse = S.RecursoService
					.consultarTodos();
			if (UtilPayload.isOK(payloadRecursoResponse)) {
				recursos.addAll(payloadRecursoResponse.getObjetos());
			}

		}
		return recursos;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}

	public Date getFechaDonativo() {
		return fechaDonativo;
	}

	public void setFechaDonativo(Date fechaDonativo) {
		this.fechaDonativo = fechaDonativo;
		if (fechaDonativo != null) {
			this.getDonativoRecurso().setFechaDonativo(fechaDonativo.getTime());
		}
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Date getFechaAporte() {
		return fechaAporte;
	}

	public void setFechaAporte(Date fechaAporte) {
		this.fechaAporte = fechaAporte;
		if (fechaAporte != null) {
			this.getDonativoRecurso().setFechaArpoteCorrespondiente(
					fechaAporte.getTime());
		}
	}

	public Long getNroReferencia() {
		return nroReferencia;
	}

	public void setNroReferencia(Long nroReferencia) {
		this.nroReferencia = nroReferencia;
	}

}
