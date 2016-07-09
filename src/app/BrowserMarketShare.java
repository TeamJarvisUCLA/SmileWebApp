package app;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BrowserMarketShare {
private static Map<Browser.GROUP, Double> brands;
private static List<Browser> browsers;
static {
    
    brands = new LinkedHashMap<Browser.GROUP, Double>();
    brands.put(Browser.IE, 53.6);
    brands.put(Browser.CHROME, 18.7);
    brands.put(Browser.FIREFOX, 19.9);
    brands.put(Browser.SAFARI, 4.6);
    brands.put(Browser.OPERA, 1.5);
    brands.put(Browser.OTHER, 0.29);
    
    browsers = new LinkedList<Browser>();
    
    browsers.add(new Browser(Browser.IE, "v8.0", 26.61));
    browsers.add(new Browser(Browser.IE, "v9.0", 16.96));
    browsers.add(new Browser(Browser.IE, "v6.0", 6.4));
    browsers.add(new Browser(Browser.IE, "v7.0", 3.55));
    browsers.add(new Browser(Browser.IE, "v8.0 - Tencent Traveler Edition", 0.09));
    
    browsers.add(new Browser(Browser.CHROME, "v18.0", 8.01 ));
    browsers.add(new Browser(Browser.CHROME, "v19.0", 7.73 ));
    browsers.add(new Browser(Browser.CHROME, "v17.0", 1.13 ));
    browsers.add(new Browser(Browser.CHROME, "v16.0", 0.45 ));
    browsers.add(new Browser(Browser.CHROME, "v18.0 - Maxthon Edition", 0.26 ));
    browsers.add(new Browser(Browser.CHROME, "v14.0", 0.25 ));
    browsers.add(new Browser(Browser.CHROME, "v20.0", 0.24 ));
    browsers.add(new Browser(Browser.CHROME, "v15.0", 0.18 ));
    browsers.add(new Browser(Browser.CHROME, "v12.0", 0.16 ));
    browsers.add(new Browser(Browser.CHROME, "v13.0", 0.13 ));
    browsers.add(new Browser(Browser.CHROME, "v11.0", 0.10 ));
    browsers.add(new Browser(Browser.CHROME, "v10.0", 0.09 ));
    
    browsers.add(new Browser(Browser.FIREFOX, "v12", 6.72));
    browsers.add(new Browser(Browser.FIREFOX, "v11", 4.72));
    browsers.add(new Browser(Browser.FIREFOX, "v13", 2.16));
    browsers.add(new Browser(Browser.FIREFOX, "v3.6", 1.87));
    browsers.add(new Browser(Browser.FIREFOX, "v10", 0.90));
    browsers.add(new Browser(Browser.FIREFOX, "v9.0", 0.65));
    browsers.add(new Browser(Browser.FIREFOX, "v8.0", 0.55));
    browsers.add(new Browser(Browser.FIREFOX, "v4.0", 0.50));
    browsers.add(new Browser(Browser.FIREFOX, "v3.0", 0.36));
    browsers.add(new Browser(Browser.FIREFOX, "v3.5", 0.36));
    browsers.add(new Browser(Browser.FIREFOX, "v6.0", 0.32));
    browsers.add(new Browser(Browser.FIREFOX, "v5.0", 0.31));
    browsers.add(new Browser(Browser.FIREFOX, "v7.0", 0.29));
    browsers.add(new Browser(Browser.FIREFOX, "v9.0", 0.65));
    
    browsers.add(new Browser(Browser.SAFARI, "5.1", 3.53));
    browsers.add(new Browser(Browser.SAFARI, "5.0", 0.85));
    browsers.add(new Browser(Browser.SAFARI, "4.0", 0.14));
    browsers.add(new Browser(Browser.SAFARI, "4.1", 0.12));
    
    browsers.add(new Browser(Browser.OPERA, "11.x", 1.30));
    browsers.add(new Browser(Browser.OPERA, "12.x", 0.15));
    browsers.add(new Browser(Browser.OPERA, "10.x", 0.09));
}

static public  Map<Browser.GROUP, Double> getBrands() {
    return brands;
}

static public List<Browser> getVersions(Browser.GROUP group) {
    List<Browser> result = new LinkedList<Browser>();
    for (Browser browser: browsers) {
        if(browser.getGroup() == group) {
            result.add(browser);
        }
    }
    return result;
}
}
