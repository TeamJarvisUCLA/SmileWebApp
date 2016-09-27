package ve.smile.datos.parametros.valores.viewmodels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import karen.core.crux.alert.Alert;
import karen.core.crux.session.DataCenter;
import karen.core.form.buttons.data.OperacionForm;
import karen.core.form.buttons.enums.OperacionFormEnum;
import karen.core.form.buttons.helpers.OperacionFormHelper;
import karen.core.form.viewmodels.VM_WindowForm;
import karen.core.util.payload.UtilPayload;
import karen.core.util.validate.UtilValidate;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.UploadEvent;

import app.UploadImageSingle;
import ve.smile.consume.services.S;
import ve.smile.dto.Valores;
import ve.smile.dto.Organizacion;
import ve.smile.payload.response.PayloadValoresResponse;
import ve.smile.payload.response.PayloadOrganizacionResponse;
import ve.smile.seguridad.enums.OperacionEnum;

public class VM_ValoresFormBasic extends VM_WindowForm implements UploadImageSingle
{

	private List<Organizacion> organizaciones;
	
	private byte[] bytes = null;
	private String nameImage;
	private String extensionImage;

	@Init(superclass = true)
	public void childInit()
	{
		// NOTHING OK!
	}

	public List<Organizacion> getOrganizaciones()
	{
		if (this.organizaciones == null)
		{
			this.organizaciones = new ArrayList<>();
		}
		if (this.organizaciones.isEmpty())
		{
			PayloadOrganizacionResponse payloadOrganizacionResponse = S.OrganizacionService.consultarTodos();
			if (!UtilPayload.isOK(payloadOrganizacionResponse))
			{
				Alert.showMessage(payloadOrganizacionResponse);
			}
			this.organizaciones.addAll(payloadOrganizacionResponse.getObjetos());
		}
		return organizaciones;
	}

	public void setOrganizaciones(List<Organizacion> organizaciones)
	{
		this.organizaciones = organizaciones;
	}
	
	@Override
	public BufferedImage getImageContent()
	{
		try
		{
			return loadImage();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public void onUploadImageSingle(UploadEvent event, String idUpload)
	{
		org.zkoss.util.media.Media media = event.getMedia();
		if (media instanceof org.zkoss.image.Image)
		{
			bytes = media.getByteData();
			this.nameImage = media.getName();
			this.extensionImage = media.getFormat();
		}
	}

	@Override
	public void onRemoveImageSingle(String idUpload)
	{
		bytes = null;
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	private BufferedImage loadImage() throws Exception
	{
		try
		{
			Integer idUser = DataCenter.getUserSecurityData().getUsuario().getIdUsuario();
			Path path = Paths.get("C:/imagenes/u_" + idUser);
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File("C:/imagenes/u_" + idUser));
		}
		catch (Exception e)
		{
			Path path = Paths.get("/home/conamerica97/eclipseKepler/imagene/default");
			bytes = Files.readAllBytes(path);
			return ImageIO.read(new File("/home/conamerica97/eclipseKepler/imagen/default"));
		}
	}

	@Override
	public List<OperacionForm> getOperationsForm(OperacionEnum operacionEnum)
	{
		List<OperacionForm> operacionesForm = new ArrayList<OperacionForm>();
		if (operacionEnum.equals(OperacionEnum.INCLUIR) || operacionEnum.equals(OperacionEnum.MODIFICAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.GUARDAR));
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.CANCELAR));
			return operacionesForm;
		}
		if (operacionEnum.equals(OperacionEnum.CONSULTAR))
		{
			operacionesForm.add(OperacionFormHelper.getPorType(OperacionFormEnum.SALIR));
			return operacionesForm;
		}
		return operacionesForm;
	}

	@Override
	public boolean actionGuardar(OperacionEnum operacionEnum)
	{
		if (!isFormValidated())
		{
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.INCLUIR))
		{
			PayloadValoresResponse payloadValoresResponse = S.ValoresService.incluir(getValores());
			if (!UtilPayload.isOK(payloadValoresResponse))
			{
				Alert.showMessage(payloadValoresResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		if (operacionEnum.equals(OperacionEnum.MODIFICAR)) {
			PayloadValoresResponse payloadValoresResponse = S.ValoresService.modificar(getValores());
			if (!UtilPayload.isOK(payloadValoresResponse))
			{
				Alert.showMessage(payloadValoresResponse);
				return true;
			}
			DataCenter.reloadCurrentNodoMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean actionSalir(OperacionEnum operacionEnum)
	{
		DataCenter.reloadCurrentNodoMenu();
		return true;
	}

	@Override
	public boolean actionCancelar(OperacionEnum operacionEnum)
	{
		return actionSalir(operacionEnum);
	}

	public Valores getValores()
	{
		return (Valores) DataCenter.getEntity();
	}

	public boolean isFormValidated()
	{
		try
		{
			UtilValidate.validateString(getValores().getNombre(), "Nombre", 100);
			UtilValidate.validateNull(getValores().getFkOrganizacion(), "Organización");
			return true;
		}
		catch (Exception e)
		{
			Alert.showMessage(e.getMessage());
			return false;
		}
	}

}
