package ve.smile.gestion.evento.planificaion.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.Listen;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.dialog.catalogue.generic.data.CatalogueDialogData;
import karen.core.dialog.catalogue.generic.events.CatalogueDialogCloseEvent;
import karen.core.dialog.catalogue.generic.events.listeners.CatalogueDialogCloseListener;
import karen.core.dialog.generic.data.DialogData;
import karen.core.dialog.generic.enums.DialogActionEnum;
import karen.core.dialog.generic.events.DialogCloseEvent;
import karen.core.dialog.generic.events.listeners.DialogCloseListener;
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
import ve.smile.dto.Evento;
import ve.smile.dto.EventoPlanificado;
import ve.smile.dto.Voluntario;

public class VMVEventoPlanificado extends VM_WindowForm {
	
	private List<Evento> listEventos = new ArrayList<>();
	private EventoPlanificado eventoPlanificado = new EventoPlanificado();
	private Evento evento = new Evento();
	private Voluntario voluntario = new Voluntario();
	private Directorio directorio = new Directorio();
	
	@Init(superclass = true)
	public void childInit() {
		listEventos  = S.EventoService
				.consultarPaginacion(10, 1).getObjetos();//NOTHING OK!
		
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum) {
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();

		if (OperacionEnum.INCLUIR.equals(OperacionEnum.INCLUIR) ||
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
		
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Voluntario>() {
			
			@Override
			public void onClose(CatalogueDialogCloseEvent<Voluntario> catalogueDialogCloseEvent) {
				if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR)) {
					return;
				}
				
				voluntario = catalogueDialogCloseEvent.getEntity();
				
				refreshVoluntario();
			}
		});
		
		UtilDialog.showDialog("views/desktop/gestion/evento/planificacion/catalogoVoluntario.zul", catalogueDialogData);
	}
	
	@Command("buscarDirectorio")
	public void buscarDirectorio() {
		CatalogueDialogData<Directorio> catalogueDialogData = new CatalogueDialogData<Directorio>();
		catalogueDialogData.addCatalogueDialogCloseListeners(new CatalogueDialogCloseListener<Directorio>() {
			
			@Override
			public void onClose(CatalogueDialogCloseEvent<Directorio> catalogueDialogCloseEvent) {
				if (catalogueDialogCloseEvent.getDialogAction().equals(DialogActionEnum.CANCELAR)) {					
					return;
				}		
				directorio = catalogueDialogCloseEvent.getEntity();				
				refreshDirectorio();
				
			}
		});
		
		UtilDialog.showDialog("views/desktop/gestion/evento/planificacion/catalogoDirectorio.zul", catalogueDialogData);
	}
	
	@Command("cargarImagenEvento")
	public void cargarImagenEvento() {
		DialogData dialogData = new DialogData();

		dialogData.addDialogCloseListeners(new DialogCloseListener() {
			
			@Override
			public void onClose(DialogCloseEvent dialogCloseEvent) {
				if (dialogCloseEvent.getDialogAction().equals(DialogActionEnum.ACEPTAR)) {
					byte[] bytes = (byte[]) dialogCloseEvent.get("bytes");
					
					Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
					
					if (bytes != null) {
						
						try {
							FileUtils.writeByteArrayToFile(
									new File("/home/conamerica97/eclipseKepler/imagen/u_" + idUser), bytes);
						} catch (IOException e) {
							UtilDialog.showMessageBoxError("Ha ocurrido un error al guardar la imagen");
						}
					} else {
						new File("/home/conamerica97/eclipseKepler/imagen/u_" + idUser).delete();
					}
					
					try {
						loadImage();
					} catch (Exception e) {
						UtilDialog.showMessageBoxError("Ha ocurrido un error al cargar la imagen");
					}
				}
			}
		});
		
		UtilDialog.showDialog("views/desktop/gestion/evento/planificacion/cargarImagen.zul", dialogData);
	}
	
	private void loadImage() throws Exception {
		try {
			Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
			
			BufferedImage b = 
					ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagen/u_" + idUser));
			
			//menuImagen.setImageContent(b);
		} catch (Exception e) {
			BufferedImage b = ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagen/default"));
			
			//menuImagen.setImageContent(b);
		}
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
	
	public void refreshVoluntario() {
		BindUtils.postNotifyChange(null, null, this, "voluntario");
	}
	
	public void refreshDirectorio() {
		System.out.println("entro");
		BindUtils.postNotifyChange(null, null, this, "directorio");
	}

	public EventoPlanificado getEventoPlanificado() {
		return (EventoPlanificado) DataCenter.getEntity();
	}

	public boolean isFormValidated() {
		//TODO
		return true;
	}

	public List<Evento> getListEventos() {
		return listEventos;
	}

	public void setListEventos(List<Evento> listEventos) {
		this.listEventos = listEventos;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public void setEventoPlanificado(EventoPlanificado eventoPlanificado) {
		this.eventoPlanificado = eventoPlanificado;
	}

	public Voluntario getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(Voluntario voluntario) {
		this.voluntario = voluntario;
	}

	public Directorio getDirectorio() {
		return directorio;
	}

	public void setDirectorio(Directorio directorio) {
		this.directorio = directorio;
	}

	

}
