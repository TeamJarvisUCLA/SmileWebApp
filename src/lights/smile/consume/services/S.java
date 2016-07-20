package lights.smile.consume.services;

import lights.seguridad.consume.services.MensajeSistemaService;
import lights.seguridad.consume.services.OperacionService;
import lights.seguridad.consume.services.SclassService;
import lights.seguridad.consume.services.VistaOperacionBasicoService;
import lights.seguridad.consume.services.UsuarioService;
import lights.seguridad.consume.services.PermisoSeguridadService;
import lights.seguridad.consume.services.IconSclassService;
import lights.seguridad.consume.services.VistaOperacionCustomService;
import lights.seguridad.consume.services.TablaService;
import lights.seguridad.consume.services.VistaService;
import lights.seguridad.consume.services.AuditoriaService;
import lights.seguridad.consume.services.SesionService;
import lights.seguridad.consume.services.NodoMenuService;
import lights.seguridad.consume.services.RolService;
import lights.seguridad.consume.services.MetodoDaoService;

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
	
	public static ModeloService ModeloService;
	
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
		ModeloService = new ModeloService();
		SclassService = new SclassService();
	}
}
