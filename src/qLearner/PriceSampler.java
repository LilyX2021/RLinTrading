package qLearner;

import com.google.common.collect.Lists;
import market.Price;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class PriceSampler {

    private static final Random RNG = new Random();

    public PriceSampler () { }

    public static ArrayList<Price> genPrice(int size) {
        ArrayList<Price> price = new ArrayList<Price>();

        final double pe = Param._pe;
        final double sigma = Param._sigma;
        final  double lambda = Param._lambda;
        double p = pe;

        //generate the price path
        price.add(Price.of(p));
        for (int i = 0; i < size; i++) {

            double y = Math.log(p / pe);
            y += - lambda * y + sigma * RNG.nextGaussian();
            double pnew = pe * Math.exp(y);
            // bound it by 100
            pnew = Math.min(pnew, 100.0);
            price.add(Price.of(pnew));


        }

        return price;
    }

    public static void main(String[] args) {
        ArrayList<Double> samples = Lists.newArrayList();

        genPrice(1000).forEach(p -> samples.add(p.value()));

        final XYSeries series = new XYSeries("Price Path");

        for (int i = 0; i < samples.size(); i++) {
            double x = samples.get(i);
            series.add((double)i, x);
        }

        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Price Path",
                "Time",
                "Price Path",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFrame frame = new JFrame("Price Path");
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);

    }

}
