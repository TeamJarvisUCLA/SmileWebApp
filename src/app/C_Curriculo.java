package app;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class C_Curriculo {

	private ActionListener actionListenerProcesar;
	private ActionListener actionListenerCargarCurriculo;
	private ActionListener actionListenerGuardar;
	
	private V_Curriculo v_Curriculo;
	
	public C_Curriculo(V_Curriculo v_Curriculo) {
		this.v_Curriculo = v_Curriculo;
		
		initActionListener();
		addActionListenerToView();
		configureView();
	}

	private void configureView() {
		v_Curriculo.getjButton2().setEnabled(false);
		v_Curriculo.getjButton3().setEnabled(false);
	}

	private void initActionListener() {
		actionListenerProcesar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String[] datos = new PrintTextLocations().procesar(v_Curriculo.getFile().getAbsolutePath());
	        		
					v_Curriculo.getjTextField10().setText(datos[9]);
					v_Curriculo.getjTextField11().setText(datos[10]);
					v_Curriculo.getjTextField12().setText(datos[11]);
					v_Curriculo.getjTextField13().setText(datos[12]);
					v_Curriculo.getjTextField2().setText(datos[0]);
					v_Curriculo.getjTextField3().setText(datos[1]);
					v_Curriculo.getjTextField4().setText(datos[2]);
					v_Curriculo.getjTextField5().setText(datos[3]);
					v_Curriculo.getjTextField6().setText(datos[4]);
					v_Curriculo.getjTextField7().setText(datos[5]);
					v_Curriculo.getjTextField8().setText(datos[6]);
					v_Curriculo.getjTextField9().setText(datos[8]);
					
					v_Curriculo.getjButton3().setEnabled(true);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,
	             			   e2.getMessage());
				}
			}
		};
		
		actionListenerCargarCurriculo = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final JFileChooser fc = new JFileChooser(); 
		            int returnVal = fc.showOpenDialog(null);
		            
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	v_Curriculo.setFile(fc.getSelectedFile()); 
		                
		                String nombre = v_Curriculo.getFile().getName();
		                
		                if (nombre.length() <= 5 || !nombre.substring(nombre.length() - 4).equals(".pdf")) {
		                	JOptionPane.showMessageDialog(null,
		                			   "El archivo escogido no es un archivo pdf");
		                	return;
		                }
		                
		                String urlImage = 
		                		new PageToImageSample().process(v_Curriculo.getFile().getAbsolutePath());
		        		
		                Image imagen = ImageIO.read(new File(urlImage));
		        		
		        		v_Curriculo.getPanelImagen().setImagen(imagen);
		        		
		        		v_Curriculo.getjButton2().setEnabled(true);
		            }
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,
             			   e2.getMessage());
				}
			}
		};
		
		actionListenerGuardar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Guardar en la base de datos");
				
				System.out.println(v_Curriculo.getjTextField10().getText());
				System.out.println(v_Curriculo.getjTextField11().getText());
				System.out.println(v_Curriculo.getjTextField12().getText());
				System.out.println(v_Curriculo.getjTextField13().getText());
				System.out.println(v_Curriculo.getjTextField2().getText());
				System.out.println(v_Curriculo.getjTextField3().getText());
				System.out.println(v_Curriculo.getjTextField4().getText());
				System.out.println(v_Curriculo.getjTextField5().getText());
				System.out.println(v_Curriculo.getjTextField6().getText());
				System.out.println(v_Curriculo.getjTextField7().getText());
				System.out.println(v_Curriculo.getjTextField8().getText());
				System.out.println(v_Curriculo.getjTextField9().getText());
				
				configureView();
				v_Curriculo.getjTextField10().setText("");
				v_Curriculo.getjTextField11().setText("");
				v_Curriculo.getjTextField12().setText("");
				v_Curriculo.getjTextField13().setText("");
				v_Curriculo.getjTextField2().setText("");
				v_Curriculo.getjTextField3().setText("");
				v_Curriculo.getjTextField4().setText("");
				v_Curriculo.getjTextField5().setText("");
				v_Curriculo.getjTextField6().setText("");
				v_Curriculo.getjTextField7().setText("");
				v_Curriculo.getjTextField8().setText("");
				v_Curriculo.getjTextField9().setText("");
				v_Curriculo.setFile(null);
				v_Curriculo.getPanelImagen().setImagen(null);
			}
		};
	}
	
	private void addActionListenerToView() {
		v_Curriculo.getjButton1().addActionListener(actionListenerCargarCurriculo);
		v_Curriculo.getjButton2().addActionListener(actionListenerProcesar);
		v_Curriculo.getjButton3().addActionListener(actionListenerGuardar);
	}
	
	public void setVisible(boolean visible) {
		v_Curriculo.setVisible(visible);
	}
}
