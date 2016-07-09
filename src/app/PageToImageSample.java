package app;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

public class PageToImageSample {
	
    public String process(String url) throws Exception {
    	
    	PDDocument document = null;
    	try {
    	    File input = new File(url);
    	    document = PDDocument.load(input);
    	    if (document.isEncrypted()) {
    	        try {
    	            document.decrypt("");
    	        } catch (Exception e) {
    	        	throw new Exception("Document is encrypted with a password");
    	        }
    	    }
    	    
    	    PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(0);

    	    BufferedImage bim = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
    	    
    	    ImageIOUtil.writeImage(bim, url.replace(".pdf", ".png"), 300);

    	    return url.replace(".pdf", ".png");
    	} catch (Exception e) {
    		return null;
    	} finally {
    	    if (document != null) {
    	        document.close();
    	    }
    	}
    }
}