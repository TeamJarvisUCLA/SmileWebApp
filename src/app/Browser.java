package app;

public class Browser {
    
    public static GROUP IE = Browser.GROUP.IE;
    public static GROUP CHROME = Browser.GROUP.CHROME;
    public static GROUP FIREFOX = Browser.GROUP.FIREFOX;
    public static GROUP SAFARI = Browser.GROUP.SAFARI;
    public static GROUP OPERA = Browser.GROUP.OPERA;
    public static GROUP OTHER = Browser.GROUP.OTHER;
    
    public static enum GROUP {
        IE("Microsoft Internet Explorer"),
        CHROME("Chrome"),
        FIREFOX("Firefox"),
        SAFARI("Safari"),
        OPERA("Opera"),
        OTHER("Proprietary or Undetectable");
        
        private String label;
        
        GROUP(String label) {
        	this.label = label;
        }
        
        public String getLabel() {
        	return label;
        }
    }
    
    private GROUP group;
    private String version;
    private Number percentage; 
    
    public Browser(GROUP group, String version, Number percentage) {
        this.group = group;
        this.version = version;
        this.percentage = percentage;
    }

    public GROUP getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public Number getPercentage() {
        return percentage;
    }
}