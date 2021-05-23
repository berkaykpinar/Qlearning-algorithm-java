package com.company;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotEpisodeCost extends JFrame {
    private double[] stepCounter;


    public PlotEpisodeCost(double[] stepCounter) {
        this.stepCounter=stepCounter;

        XYDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Episode via Cost",
                "Episode", "Cost", dataset);


        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(153,255,153));


        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Cost");

        for(int i=0;i<stepCounter.length;i++){
            series.add(i,stepCounter[i]);
        }


        dataset.addSeries(series);


        return dataset;
    }

}