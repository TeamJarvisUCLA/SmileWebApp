package app;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.chart.Charts;
import org.zkoss.chart.LinearGradient;
import org.zkoss.chart.PaneBackground;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.DefaultDialModel;
import org.zkoss.chart.model.DialModel;
import org.zkoss.chart.model.DialModelScale;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;


public class GaugeSpeedometerComposer extends SelectorComposer<Window> {
    @Wire
    Charts chart;

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        DialModel dialmodel = new DefaultDialModel();
        dialmodel.setFrameBgColor(null);
        dialmodel.setFrameBgColor1(null);
        dialmodel.setFrameBgColor2(null);
        dialmodel.setFrameFgColor(null);
        DialModelScale scale = dialmodel.newScale(0, 200, -150, -300, 10, 4);
        scale.setText("km/h");
        scale.setTickColor("#666666");
        scale.setValue(80);
        scale.newRange(0, 120, "#55BF3B", 0.9, 1); // green
        scale.newRange(120, 160, "#DDDF0D", 0.9, 1); // yellow
        scale.newRange(160, 200, "#DF5353", 0.9, 1); // red
        chart.setModel(dialmodel);

        List<PaneBackground> backgrounds = new LinkedList<PaneBackground>();

        PaneBackground background1 = new PaneBackground();
        LinearGradient linearGradient1 = new LinearGradient(0, 0, 0, 1);
        linearGradient1.setStops("#FFF", "#333");
        background1.setBackgroundColor(linearGradient1);
        background1.setBorderWidth(0);
        background1.setOuterRadius("109%");
        backgrounds.add(background1);

        PaneBackground background2 = new PaneBackground();
        LinearGradient linearGradient2 = new LinearGradient(0, 0, 0, 1);
        linearGradient2.setStops("#333", "#FFF");
        background2.setBackgroundColor(linearGradient2);
        background2.setBorderWidth(1);
        background2.setOuterRadius("107%");
        backgrounds.add(background2);

        // default background
        backgrounds.add(new PaneBackground());

        PaneBackground background3 = new PaneBackground();
        background3.setBackgroundColor("#DDD");
        background3.setBorderWidth(0);
        background3.setOuterRadius("105%");
        background3.setInnerRadius("103%");
        backgrounds.add(background3);

        chart.getPane().setBackground(backgrounds);

        // the value axis
        YAxis yAxis = chart.getYAxis();
        // yAxis.setMinorTickInterval("auto");
        yAxis.setMinorTickWidth(1);
        yAxis.setMinorTickPosition("inside");
        yAxis.setMinorTickColor("#666");

        yAxis.setTickPixelInterval(30);
        yAxis.setTickWidth(2);
        yAxis.setTickPosition("inside");
        yAxis.setTickLength(10);

        yAxis.getLabels().setStep(2);
        yAxis.getLabels().setRotation("auto");

        chart.getPlotOptions().getGauge().getTooltip().setValueSuffix(" km/h");
        
        chart.getSeries().setName("Speed");

    }

    // Add some life
    @Listen("onTimer = #timer")
    public void updateData() {
        int inc = (int) Math.round((Math.random() - 0.5) * 20);
        int oldVal = chart.getSeries().getPoint(0).getY().intValue();
        int newVal = oldVal + inc;
        if (newVal < 0 || newVal > 200) {
            newVal = oldVal - inc;
        }
        chart.getSeries().getPoint(0).update(newVal);
    }
}