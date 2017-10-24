package diagnostics;

import market.Price;
import market.Shares;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import qLearner.PriceSpace;
import qLearner.QLearner2;
import qLearner.State;
import qLearner.StateSpace;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xionglihua on 10/5/17.
 */

public class Utils {

    public static void write(double[][] data, Path file) {
        List<String> list = new ArrayList<>(data.length);
        StringBuilder a = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            a.setLength(0);
            for (int j = 0; j < data[0].length; j++) {
                if(j > 0) a.append(",");
                a.append(data[i][j]);
            }

            list.add(a.toString());
        }
        try {
            Files.write(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void plot(ArrayList<Double> data, String title, String xlabel, String ylabel) {
        final XYSeries series = new XYSeries(title);

        for (int i = 0; i < data.size(); i++) {
            double x = data.get(i);
            series.add((double)i, x);
        }

        final XYSeriesCollection dataCollection = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                ylabel,
                xlabel,
                title,
                dataCollection,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFrame frame = new JFrame(title);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void plot_action_price(Shares holding, double[][] qMatrix){
        ArrayList<Double> data = new ArrayList<Double>();
        PriceSpace priceSpace = PriceSpace.create();

        int n = priceSpace.size();

        for (int i = 0; i < n; i++){
            Price p = priceSpace.get(i);
            State s = new State(p, holding);
            Shares action = QLearner2.argmaxQ(s);

            data.add((double) action.value());
        }

        plot(data, "Action(price) plot at holdings =" + Integer.toString(holding.value()), "price", "action");
    }


}
