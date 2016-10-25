package ve.smile.web.postulacion.viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import karen.core.dialog.generic.viewmodels.VM_WindowDialog;
import karen.core.util.payload.UtilPayload;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import ve.smile.consume.services.S;
import ve.smile.dto.Ciudad;
import ve.smile.dto.Configuracion;
import ve.smile.dto.Estado;
import ve.smile.dto.FrecuenciaAporte;
import ve.smile.dto.Padrino;
import ve.smile.dto.Persona;
import ve.smile.enums.PropiedadEnum;
import ve.smile.enums.TipoPersonaEnum;
import ve.smile.payload.response.PayloadCiudadResponse;
import ve.smile.payload.response.PayloadConfiguracionResponse;
import ve.smile.payload.response.PayloadEstadoResponse;
import ve.smile.payload.response.PayloadFrecuenciaAporteResponse;
import ve.smile.seguridad.enums.SexoEnum;

public class VM_Postulado extends VM_WindowDialog {

	private Persona persona = new Persona();
	private Padrino padrino = new Padrino();
	private List<SexoEnum> sexoEnums;
	private SexoEnum sexoEnum;
	private List<TipoPersonaEnum> tPersonaEnum;
	private TipoPersonaEnum tPersonaEnums;
	private Date myFecha;
	private Estado estado;
	private List<Estado> estados;
	private List<Ciudad> ciudades;
	private List<FrecuenciaAporte> frecuenciaAporte;
	private FrecuenciaAporte faporte;
	private Configuracion confPostulacionPadrino =  new Configuracion();
	boolean emailPostulacionPadrino = false;
	private Configuracion confPostulacionVoluntario =  new Configuracion();
	boolean emailPostulacionVoluntario = false;


	@Init(superclass = true)
	public void childInit() {
		// NOTHING OK!
		PayloadConfiguracionResponse payloadConfiguracionResponseP = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_POSTULACION_PADRINO
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponseP)) {
			if (!payloadConfiguracionResponseP.getObjetos().isEmpty()) {
				this.confPostulacionPadrino.setValor(payloadConfiguracionResponseP
						.getObjetos().get(0).getValor());
			}else{
				this.confPostulacionPadrino.setValor("false");
			}
			
		}
		PayloadConfiguracionResponse payloadConfiguracionResponseV = S.ConfiguracionService
				.consultarConfiguracionPropiedad(PropiedadEnum.EMAIL_POSTULACION_VOLUNTARIO
						.ordinal());
		if (UtilPayload.isOK(payloadConfiguracionResponseV)) {
			if (!payloadConfiguracionResponseV.getObjetos().isEmpty()) {
				this.confPostulacionVoluntario.setValor(payloadConfiguracionResponseV
						.getObjetos().get(0).getValor());
			}else{
				this.confPostulacionVoluntario.setValor("false");
			}
			
		}
	}

	public FrecuenciaAporte getFaporte() {

		return faporte;
	}

	public void setFaporte(FrecuenciaAporte faporte) {
		this.faporte = faporte;
	}

	public List<FrecuenciaAporte> getFrecuenciaAporte() {
		if (this.frecuenciaAporte == null) {
			this.frecuenciaAporte = new ArrayList<>();
		}
		if (this.frecuenciaAporte.isEmpty()) {
			PayloadFrecuenciaAporteResponse payloadFrecuenciaAporteResponse = S.FrecuenciaAporteService
					.consultaFrecuenciaAporteSinSession();

			this.frecuenciaAporte.addAll(payloadFrecuenciaAporteResponse
					.getObjetos());
		}

		return frecuenciaAporte;
	}

	public void setFrecuenciaAporte(List<FrecuenciaAporte> frecuenciaAporte) {
		this.frecuenciaAporte = frecuenciaAporte;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		if (this.estados == null) {
			this.estados = new ArrayList<>();
		}
		if (this.estados.isEmpty()) {
			PayloadEstadoResponse payloadEstadoResponse = S.EstadoService
					.consultaEstadoSinSession();

			this.estados.addAll(payloadEstadoResponse.getObjetos());
		}
		return estados;

	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Ciudad> getCiudades() {
		if (this.ciudades == null) {
			this.ciudades = new ArrayList<>();
		}
		return ciudades;
	}

	public void setCiudades(List<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}

	@Command("changeEstado")
	@NotifyChange({ "ciudades", "persona" })
	public void changeEstado() {
		this.getCiudades().clear();
		this.getPersona().setFkCiudad(null);
		PayloadCiudadResponse payloadCiudadResponse = S.CiudadService
				.consultaCiudadPorEstadoSinSession(estado.getIdEstado());
		this.getCiudades().addAll(payloadCiudadResponse.getObjetos());
	}

	public Date getMyFecha() {
		if (myFecha == null) {
			myFecha = new Date();
		}
		System.out.print(myFecha);

		return myFecha;
	}

	public void setMyFecha(Date myFecha) {
		this.myFecha = myFecha;
	}

	// TipoPErsonaEnums

	public TipoPersonaEnum gettPersonaEnums() {
		return tPersonaEnums;
	}

	public void settPersonaEnums(TipoPersonaEnum tPersonaEnums) {
		this.tPersonaEnums = tPersonaEnums;
		this.getPersona().setTipoPersona(tPersonaEnums.ordinal());

	}

	public List<TipoPersonaEnum> gettPersonaEnum() {
		if (this.tPersonaEnum == null) {
			tPersonaEnum = new ArrayList<TipoPersonaEnum>();
		}
		if (this.tPersonaEnum.isEmpty()) {
			for (TipoPersonaEnum tPersonaEnums : TipoPersonaEnum.values()) {
				this.tPersonaEnum.add(tPersonaEnums);
			}
		}

		return tPersonaEnum;
	}

	public void settPersonaEnum(List<TipoPersonaEnum> tPersonaEnum) {
		this.tPersonaEnum = tPersonaEnum;
	}

	// SexoEnum

	public List<SexoEnum> getSexoEnums() {
		if (this.sexoEnums == null) {
			sexoEnums = new ArrayList<SexoEnum>();
		}
		if (this.sexoEnums.isEmpty()) {
			for (SexoEnum sexoEnum : SexoEnum.values()) {
				this.sexoEnums.add(sexoEnum);
			}
		}
		return sexoEnums;
	}

	public void setSexoEnums(List<SexoEnum> sexoEnums) {
		this.sexoEnums = sexoEnums;
	}

	public SexoEnum getSexoEnum() {
		return sexoEnum;
	}

	public void setSexoEnum(SexoEnum sexoEnum) {
		this.sexoEnum = sexoEnum;
		this.getPersona().setSexo(sexoEnum.ordinal());
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public void afterChildInit() {
		// TODO Auto-generated method stub

	}

	public Padrino getPadrino() {
		return padrino;
	}

	public void setPadrino(Padrino padrino) {
		if (padrino == null) {
			padrino = new Padrino();
		}
		this.padrino = padrino;

	}

	public boolean isEmailPostulacionPadrino() {
		if(confPostulacionPadrino.getValor().equals("true")){
			this.emailPostulacionPadrino = true;
		}
		return emailPostulacionPadrino;
	}
	
	public boolean isEmailPostulacionVoluntario() {
		if(confPostulacionVoluntario.getValor().equals("true")){
			this.emailPostulacionVoluntario = true;
		}
		return emailPostulacionVoluntario;
	}
}
