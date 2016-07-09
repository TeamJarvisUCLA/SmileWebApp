package app;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class JPanelImagen extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image imagen;

	public JPanelImagen() {
		super();
		imagen = null;
	}

	/**
	 * Construye un JPanelImagen con la imágen dada por parámetro.
	 * 
	 * @param imagen la imágen.
	 */
	public JPanelImagen(Image imagen) {
		if (imagen != null) {
			this.imagen = imagen;
		} else {
			imagen = null;
		}
		repaint();
	}

	/**
	 * Asigna al JPanelImagen, la imágen dada por parámetro.
	 * 
	 * @param imagen la imagen.
	 */
	public void setImagen(Image imagen) {
		this.imagen = imagen;
		
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		if (imagen != null) {
			g.drawImage(imagen, 0, 0, this.getWidth(), this.getHeight(), this); //Dibuja la imagen
			this.setOpaque(false); 					//Elimina el fondo por defecto del panel.
		} else {
			this.setOpaque(true);					
		}
		super.paint(g);								//Dibuja el resto de los componentes.
	}
}
