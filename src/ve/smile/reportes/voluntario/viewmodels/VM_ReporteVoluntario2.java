package ve.smile.reportes.voluntario.viewmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.Init;

import ve.smile.consume.services.S;
import ve.smile.dto.ClasificadorVoluntario;
import ve.smile.dto.Fortaleza;
import ve.smile.dto.Profesion;


public class VM_ReporteVoluntario2 {
	
	private boolean clasificadorVoluntario = false;
	
	private boolean profesionVoluntario  = false; 
	
	private boolean	fortalezaVoluntario  = false;
	
	private boolean redesSociales  = false;
	
	private boolean fechaIngreso  = false; 
	
	private boolean postulado  = false;
	
	private boolean porCompletar  = false;
	
	private boolean activo  = false;
	
	private boolean egresado  = false;

	private List<ClasificadorVoluntario> listClasificadorVoluntario = new ArrayList<>();

	private Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados;

	private List<Fortaleza>  listFortalezas = new ArrayList<>();
	
	private List<String> listRedesSociales = new ArrayList<>();
	
	private Set<String> redesSeleccionadas ;
	
	private Set<Fortaleza> fortalezasSeleccionados;
	
	private List<Profesion> listProfesion = new ArrayList<>();
	
	private Set<Profesion> profesionesSeleccionadas;
	
	public Set<ClasificadorVoluntario> getClasificadorVoluntariosSeleccionados() {
		return clasificadorVoluntariosSeleccionados;
	}

	public void setClasificadorVoluntariosSeleccionados(
			Set<ClasificadorVoluntario> clasificadorVoluntariosSeleccionados) {
		
		this.clasificadorVoluntariosSeleccionados = clasificadorVoluntariosSeleccionados;
	
	}

	@Init(superclass = true)
	public void childInit() {
	
		listClasificadorVoluntario = S.ClasificadorVoluntarioService.consultarTodos().getObjetos();
		
		listFortalezas = S.FortalezaService.consultarTodos().getObjetos();
		
		listProfesion = S.ProfesionService.consultarTodos().getObjetos();
		
		llenarListRedes();
		
	}
	
	public void llenarListRedes(){
		listRedesSociales.add("Facebook");
		listRedesSociales.add("Instagram");
		listRedesSociales.add("Twitter");
		listRedesSociales.add("Linkedin");
	}



	public boolean isClasificadorVoluntario() {
		return clasificadorVoluntario;
	}

	public void setClasificadorVoluntario(boolean clasificadorVoluntario) {
		this.clasificadorVoluntario = clasificadorVoluntario;
	}

	public boolean isProfesionVoluntario() {
		return profesionVoluntario;
	}

	public void setProfesionVoluntario(boolean profesionVoluntario) {
		this.profesionVoluntario = profesionVoluntario;
	}

	public boolean isFortalezaVoluntario() {
		return fortalezaVoluntario;
	}

	public void setFortalezaVoluntario(boolean fortalezaVoluntario) {
		this.fortalezaVoluntario = fortalezaVoluntario;
	}

	public boolean isRedesSociales() {
		return redesSociales;
	}

	public void setRedesSociales(boolean redesSociales) {
		this.redesSociales = redesSociales;
	}

	public boolean isFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(boolean fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public boolean isPostulado() {
		return postulado;
	}

	public void setPostulado(boolean postulado) {
		this.postulado = postulado;
	}

	public List<ClasificadorVoluntario> getListClasificadorVoluntario() {
		return listClasificadorVoluntario;
	}

	public void setListClasificadorVoluntario(
			List<ClasificadorVoluntario> listClasificadorVoluntario) {
		this.listClasificadorVoluntario = listClasificadorVoluntario;
	}

	public List<Fortaleza> getListFortalezas() {
		return listFortalezas;
	}

	public void setListFortalezas(
			List<Fortaleza> listFortalezas) {
		this.listFortalezas = listFortalezas;
	}

	public Set<Fortaleza> getFortalezasSeleccionados() {
		return fortalezasSeleccionados;
	}

	public void setFortalezasSeleccionados(
			Set<Fortaleza> fortalezasSeleccionados) {
		this.fortalezasSeleccionados = fortalezasSeleccionados;
	}

	public List<Profesion> getListProfesion() {
		return listProfesion;
	}

	public void setListProfesion(List<Profesion> listProfesion) {
		this.listProfesion = listProfesion;
	}

	public Set<Profesion> getProfesionesSeleccionadas() {
		return profesionesSeleccionadas;
	}

	public void setProfesionesSeleccionadas(Set<Profesion> profesionesSeleccionadas) {
		this.profesionesSeleccionadas = profesionesSeleccionadas;
	}

	public List<String> getListRedesSociales() {
		return listRedesSociales;
	}

	public void setListRedesSociales(List<String> listRedesSociales) {
		this.listRedesSociales = listRedesSociales;
	}

	public Set<String> getRedesSeleccionadas() {
		return redesSeleccionadas;
	}

	public void setRedesSeleccionadas(Set<String> redesSeleccionadas) {
		this.redesSeleccionadas = redesSeleccionadas;
	}

	public boolean isPorCompletar() {
		return porCompletar;
	}

	public void setPorCompletar(boolean porCompletar) {
		this.porCompletar = porCompletar;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isEgresado() {
		return egresado;
	}

	public void setEgresado(boolean egresado) {
		this.egresado = egresado;
	}
	
	

}
