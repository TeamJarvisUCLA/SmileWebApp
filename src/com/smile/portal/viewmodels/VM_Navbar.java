package com.smile.portal.viewmodels;

import karen.core.crux.session.DataCenter;
import karen.core.util.UtilDialog;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import bsh.util.Util;

public class VM_Navbar {
	
	@Init
	public void init(){
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/main.zul");
		
	}
	
	@Command
	public void inicio() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/main.zul");
		
	}
	
	@Command
	public void galeria() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/galeria.zul");
		
	}
	
	@Command
	public void conocenos() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/conocenosWeb.zul");
		
	}
	
	@Command
	public void eventos() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/eventos.zul");
		
	}
	
	@Command
	public void participa() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/participa.zul");
		
	}	
	
	@Command
	public void contactanos() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/contactanos.zul");
		
	}
	
	@Command
	public void noticias() {
		DataCenter.updateSrcPageContent(null, null, "/vista/portal/noticias.zul");
		
	}

}
