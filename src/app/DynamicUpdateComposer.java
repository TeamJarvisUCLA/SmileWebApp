package app;

import java.util.Calendar;

import org.zkoss.chart.Charts;
import org.zkoss.chart.Options;
import org.zkoss.chart.PlotLine;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class DynamicUpdateComposer extends SelectorComposer<Window> {
    @Wire
    Charts chart;

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        Options options = new Options();
        options.getGlobal().setUseUTC(false);
        chart.setOptions(options);
        chart.setAnimation(true);
        
        chart.getXAxis().setType("datetime");
        chart.getXAxis().setTickPixelInterval(150);
        
        chart.getYAxis().setTitle("Value");
        PlotLine plotLine = new PlotLine();
        plotLine.setValue(0);
        plotLine.setWidth(1);
        plotLine.setColor("#808080");
        chart.getYAxis().addPlotLine(plotLine);
        
        chart.getTooltip().setHeaderFormat("<b>{series.name}</b><br/>");
        chart.getTooltip().setPointFormat("{point.x:%Y-%m-%d %H:%M:%S}<br>{point.y}");
        
        chart.getLegend().setEnabled(false);
    
        chart.getExporting().setEnabled(false);

        Series series = chart.getSeries();
        series.setName("Random data");
        for (int i = -19; i <= 0; i++) {
            Point point = new Point(Calendar.getInstance().getTimeInMillis() + i*1000, Math.random());
            series.addPoint(point);
        }
    }

    @Listen("onTimer = #timer")
    public void updateData() {
        chart.getSeries().addPoint(new Point(Calendar.getInstance().getTimeInMillis(), Math.random()),
                true, true, true);
    }
}