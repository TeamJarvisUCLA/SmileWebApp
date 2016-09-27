package ve.smile.web.viewmodels.noticias;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import karen.core.crux.session.DataCenter;
import ve.smile.consume.services.S;
import ve.smile.dto.Cartelera;
import ve.smile.enums.TipoCarteleraEnum;
import ve.smile.payload.response.PayloadCarteleraResponse;

public class VM_Noticias {
	
	private List<Cartelera> noticias;
	
	public List<Cartelera> getnoticias(){
		if (this.noticias == null) {
			this.noticias = new ArrayList<>();
		}
		if (this.noticias.isEmpty()) {
		
			PayloadCarteleraResponse payloadCarteleraResponse = S.CarteleraService
					.consultaCarteleraPorParametro(1000, TipoCarteleraEnum.NOTICIAS.ordinal());
		
			this.noticias.addAll(payloadCarteleraResponse.getObjetos());
		}

		return noticias;
	}
	
	@Command
	public void detalleNoticia(@BindingParam("noticia") Cartelera noticia ) {

		DataCenter.updateSrcPageContent(noticia, null, "/views/web/detalleNoticia.zul");
		
	}

}
