zk.afterMount(function() {
	jQuery("#f11").click(function() {
		fullScreenApi.toggleFullScreen(document.body);
	});
	
	shortcut.add("F11",function() {
		fullScreenApi.toggleFullScreen(document.body);
	});
	
	if (fullScreenApi.supportsFullScreen) {
		document.addEventListener(fullScreenApi.fullScreenEventName, function(e) {
			
		    if (fullScreenApi.isFullScreen()) {
		    	setClassIconF11("fa-expand", "fa-compress", "Terminar pantalla completa (F11)");
		    } else {
		    	setClassIconF11("fa-compress", "fa-expand", "Iniciar pantalla completa (F11)");
		    }
		    
		}, true);
	}
	
	
});
	
function setClassIconF11(remove, add, title) {
    jQuery("#f11").children("i").removeClass(remove);
    jQuery("#f11").children("i").addClass(add);
    jQuery("#f11").attr("title", title);
}