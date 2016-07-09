package app;

import org.zkoss.chart.Charts;
import org.zkoss.chart.XAxis;
import org.zkoss.chart.YAxis;
import org.zkoss.json.JavaScriptValue;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;


public class BarNegativeStackComposer extends SelectorComposer<Window> {

    @Wire
    Charts chart;

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        chart.setModel(BarNegativeStackData.getCategoryModel());
    
        chart.getXAxis().setReversed(false);
        chart.getXAxis().getLabels().setStep(1);
    
        // mirror axis on right side
        XAxis minorAxis = chart.getXAxis(1);
        minorAxis.setOpposite(true);
        minorAxis.setReversed(false);
        minorAxis.setLinkedTo(0);
        minorAxis.getLabels().setStep(1);
        minorAxis.setCategories(BarNegativeStackData.getCategories());
    
        YAxis yAxis = chart.getYAxis();
        yAxis.setTitle("");
        yAxis.getLabels().setFormatter(new JavaScriptValue("function() {return Math.abs(this.value)/1000000 + 'M';}"));
        yAxis.setMin(-4000000);
        yAxis.setMax(4000000);
    
        chart.getPlotOptions().getSeries().setStacking("normal");
        
        chart.getTooltip().setFormatter(new JavaScriptValue("function() {return '<b>' + this.series.name + ', age ' + this.point.category + '</b><br/>' + 'Population: ' + Highcharts.numberFormat(Math.abs(this.point.y), 0);}"));
    }
}