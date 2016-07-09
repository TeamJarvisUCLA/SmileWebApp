package app;

public class Principal {

	public static void main(String[] args) {
		V_Curriculo v_Curriculo = new V_Curriculo();
		
		final C_Curriculo c_Curriculo = new C_Curriculo(v_Curriculo);
		
	    c_Curriculo.setVisible(true);
	        
	}

}
