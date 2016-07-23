package lights.smile.consume.services;

import ve.smile.consume.services.ClasificadorEventoService;
import ve.smile.seguridad.consume.services.MensajeSistemaService;
import ve.smile.seguridad.consume.services.OperacionService;
import ve.smile.seguridad.consume.services.SclassService;
import ve.smile.seguridad.consume.services.VistaOperacionBasicoService;
import ve.smile.seguridad.consume.services.UsuarioService;
import ve.smile.seguridad.consume.services.PermisoSeguridadService;
import ve.smile.seguridad.consume.services.IconSclassService;
import ve.smile.seguridad.consume.services.VistaOperacionCustomService;
import ve.smile.seguridad.consume.services.TablaService;
import ve.smile.seguridad.consume.services.VistaService;
import ve.smile.seguridad.consume.services.AuditoriaService;
import ve.smile.seguridad.consume.services.SesionService;
import ve.smile.seguridad.consume.services.NodoMenuService;
import ve.smile.seguridad.consume.services.RolService;
import ve.smile.seguridad.consume.services.MetodoDaoService;

public class S {

	public static VistaOperacionBasicoService VistaOperacionBasicoService;
	public static UsuarioService UsuarioService;
	public static PermisoSeguridadService PermisoSeguridadService;
	public static IconSclassService IconSclassService;
	public static VistaOperacionCustomService VistaOperacionCustomService;
	public static TablaService TablaService;
	public static OperacionService OperacionService;
	public static VistaService VistaService;
	public static AuditoriaService AuditoriaService;
	public static SesionService SesionService;
	public static NodoMenuService NodoMenuService;
	public static RolService RolService;
	public static MetodoDaoService MetodoDaoService;
	public static MensajeSistemaService MensajeSistemaService;
	public static SclassService SclassService;
	public static ClasificadorEventoService ClasificadorEventoService;
	

	
	static {
		VistaOperacionBasicoService = new VistaOperacionBasicoService();
		UsuarioService = new UsuarioService();
		PermisoSeguridadService = new PermisoSeguridadService();
		IconSclassService = new IconSclassService();
		VistaOperacionCustomService = new VistaOperacionCustomService();
		TablaService = new TablaService();
		OperacionService = new OperacionService();
		VistaService = new VistaService();
		AuditoriaService = new AuditoriaService();
		SesionService = new SesionService();
		NodoMenuService = new NodoMenuService();
		RolService = new RolService();
		MetodoDaoService = new MetodoDaoService();
		MensajeSistemaService = new MensajeSistemaService();
		
		SclassService = new SclassService();
		ClasificadorEventoService = new ClasificadorEventoService();
	}
}
