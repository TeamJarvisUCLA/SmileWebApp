package app;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.chart.Charts;
import org.zkoss.chart.ChartsEvents;
import org.zkoss.chart.Color;
import org.zkoss.chart.Point;
import org.zkoss.chart.RadialGradient;
import org.zkoss.chart.Series;
import org.zkoss.chart.XAxis;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.ZAxis;
import org.zkoss.chart.options3D.BackPanel;
import org.zkoss.chart.options3D.BottomPanel;
import org.zkoss.chart.options3D.Frame3D;
import org.zkoss.chart.options3D.Options3D;
import org.zkoss.chart.options3D.SidePanel;
import org.zkoss.chart.plotOptions.ScatterPlotOptions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;


public class Scatter3DComposer extends SelectorComposer<Window> {
    
    @Wire
    Charts chart;
    
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        
        chart.setMargin(100);
        Options3D opt3d = chart.getOptions3D();
        Frame3D frame = opt3d.getFrame();
        BottomPanel bottom = frame.getBottom();
        BackPanel back = frame.getBack();
        SidePanel side = frame.getSide();
        
        opt3d.setEnabled(true);
        opt3d.setAlpha(10);
        opt3d.setBeta(30);
        opt3d.setDepth(250);
        opt3d.setViewDistance(5);
        
        bottom.setSize(1);
        bottom.setColor("rgba(0,0,0,0.02)");
        back.setSize(1);
        back.setColor("rgba(0,0,0,0.04)");
        side.setSize(1);
        side.setColor("rgba(0,0,0,0.06)");
        
        ScatterPlotOptions sot = chart.getPlotOptions().getScatter();
        sot.setWidth(10);
        sot.setHeight(10);
        sot.setDepth(10);
        
        YAxis y0 = new YAxis();
        XAxis x0 = new XAxis();
        ZAxis z0 = new ZAxis();
        
        y0.setMin(0);
        y0.setMax(10);
        
        x0.setMin(0);
        x0.setMax(10);
        x0.setGridLineWidth(1);
        
        z0.setMin(0);
        z0.setMax(10);
        
        chart.setYAxis(y0);
        chart.setXAxis(x0);
        chart.setZAxis(z0);
        
        chart.getLegend().setEnabled(false);
        initGradientColors();
        initSeries();
    }
    
    
    private void initGradientColors() {
        List<Color> gradientColors = new LinkedList<Color>();
        String[][] colors = {
                {"#f1c40f", "#f39c12"},
                {"#3498db", "#2980b9"},
                {"#e67e22", "#d35400"},
                {"#1abc9c", "#16a085"},
                {"#2ecc71", "#27ae60"},
                {"#e74c3c", "#c0392b"}
        };
        for (String[] color: colors) {
            RadialGradient radialGradient = new RadialGradient(0.4, 0.3, 0.5);
            radialGradient.setStops(color[0], color[1]);
            gradientColors.add(new Color(radialGradient));
        }
        chart.setColors(gradientColors);
    }
    
    private void initSeries() {
        int pointsCount = 50;
        Series series = chart.getSeries();
        series.setName("Reading");
        series.setColorByPoint(true);
        
        Point[] points = new Point[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            Integer num1 = new Double(Math.floor((Math.random() * 10))).intValue();
            Integer num2 = new Double(Math.floor((Math.random() * 10))).intValue();
            Integer num3 = new Double(Math.floor((Math.random() * 10))).intValue();
            points[i] = new Point(num1, num2, num3);
        }
        series.setData(points);
    }
}