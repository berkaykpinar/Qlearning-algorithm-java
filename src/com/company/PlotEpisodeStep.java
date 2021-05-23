package com.company;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotEpisodeStep extends JFrame {
    private int[] stepCounter;


    public PlotEpisodeStep(int[] stepCounter) {
        this.stepCounter=stepCounter;

        XYDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Episode via Steps",
                "Episode", "Step", dataset);


        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));


        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Step");

        for(int i=0;i<stepCounter.length;i++){
            series.add(i,stepCounter[i]);
        }


        dataset.addSeries(series);


        return dataset;
    }

}