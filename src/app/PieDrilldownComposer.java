package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zkoss.chart.Charts;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.plotOptions.ColumnPlotOptions;
import org.zkoss.chart.plotOptions.DataLabels;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;


public class PieDrilldownComposer extends SelectorComposer<Window> {

    @Wire
    Charts chart;

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        DataLabels dataLabels = chart.getPlotOptions().getSeries().getDataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setFormat("{point.name}: {point.y:.1f}%");
        
        chart.getTooltip().setHeaderFormat("<span style=\"font-size:11px\">{series.name}</span><br>");
        chart.getTooltip().setPointFormat("<span style=\"color:{point.color}\">{point.name}" +
            "</span>: <b>{point.y:.2f}%</b> of total<br/>");
        
        initSeries();
    }
    
    private void initSeries() {
        Series series = chart.getSeries();
        List<Series> drilldowns = new ArrayList<Series>();
        series.setName("Brands");
        ColumnPlotOptions plotOptions = new ColumnPlotOptions();
        plotOptions.setColorByPoint(true);
        series.setPlotOptions(plotOptions);
        Iterator<Entry<Browser.GROUP, Double>> iterator =
                BrowserMarketShare.getBrands().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Browser.GROUP, Double> entry = iterator.next();
            String label = entry.getKey().getLabel();
            Point point = new Point(label, entry.getValue());
            if (entry.getValue() > 1) {
                point.setDrilldown(label);
                List<Browser> browsers = BrowserMarketShare.getVersions(entry.getKey());
                if (!browsers.isEmpty()) {
                    Series s = new Series();
                    s.setId(label);
                    for (Browser browser: browsers) {
                        Point p = new Point(browser.getVersion(), browser.getPercentage());
                        s.addPoint(p);
                    }
                    drilldowns.add(s);
                }
            }
            series.addPoint(point);
        }
        chart.getDrilldown().setSeries(drilldowns);
    }
}
