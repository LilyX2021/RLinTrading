package qLearner;

import diagnostics.Utils;
import market.Price;
import market.Shares;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearner2 {

    // min & max holding
    private static final Shares maxHold = HoldingSpace.getMax();
    private static final Shares minHold = HoldingSpace.getMin();
    // action space
    private static final ActionSpace actionSpace = ActionSpace.create();
    // state space
    private static final StateSpace stateSpace = StateSpace.create();
    // Q matrix
    private static final int nStates = stateSpace.size();
    private static final int nActions = actionSpace.size();
    private static final double[][] qMatrix = new double[nStates][nActions];


    public static void main(String[] args) {

        // Q-Learning iteration
        qLearning();

        //calculate Out-of-Sample Sharpe, plot cumulative pnl
        run(5000);

        // Diagnostics:
        Utils.plot_action_price(new Shares(0), qMatrix);
        Utils.plot_action_price(new Shares(100), qMatrix);
        Utils.plot_action_price(new Shares(-100), qMatrix);
        Utils.plot_action_price(new Shares(200), qMatrix);
        Utils.plot_action_price(new Shares(-200), qMatrix);
        Utils.plot_action_price(new Shares(300), qMatrix);
        Utils.plot_action_price(new Shares(-300), qMatrix);
        Utils.plot_action_price(new Shares(400), qMatrix);
        Utils.plot_action_price(new Shares(-400), qMatrix);
        Utils.plot_action_price(new Shares(500), qMatrix);
        Utils.plot_action_price(new Shares(-500), qMatrix);

    }


    // Q-Learning iteration
    private static void qLearning() {

        //qMatrix = new double[nStates][nActions];

        ArrayList<Price> pricePath = PriceSampler.genPrice(Param._nTrain);

        Shares currHolding = new Shares(0);

        for (int i = 1; i < Param._nTrain; i++) {

            // see the price update but haven't taken action yet
            final Price currPrice = pricePath.get(i);
            final State currState = new State(currPrice, currHolding);
            final int state_index = stateSpace.indexOf(currState);

            // choose action
            final Shares shares_traded = chooseAction(currState);
            final int action_index = actionSpace.indexOf(shares_traded);

            // take action and yield to the next state
            final Shares nextHolding =
                    Shares.max(Shares.min(
                    new Shares(currHolding.value() + shares_traded.value()),
            maxHold), minHold);

            final Price nextPrice = pricePath.get(i + 1);
            final State nextState = new State(nextPrice, nextHolding);

            // observe reward
            final Result result = Result.getReward(currState, nextState);

            // update Q Matrix
            final double q_sa = qMatrix[state_index][action_index];
            final double increment = Param._alpha * (result.reward + Param._gamma * getMaxQ(nextState) - q_sa);
            qMatrix[state_index][action_index] += increment;
            //double after = qMatrix[state_index][action_index];
            //System.out.println("q_sa = " + q_sa + ", increment = " + increment + ", after = " + after);

            currHolding = new Shares(nextHolding.value());
            if (i % Math.pow(10,6) == 0){
                System.out.println(i);
            }

        }

        // output the qMatrix
        Path path = Paths.get("/Users/xionglihua/Desktop/qMatrix_"+Double.toString(Param._epsilon)+".csv");
        Utils.write(qMatrix, path);


    }


    private static void run(int nsteps) {

        ArrayList<Price> pricePath = PriceSampler.genPrice(nsteps+1);
        Shares currHolding = new Shares(0);
        List<Double> pnl = new ArrayList<>();

        for (int i = 0; i < nsteps; i++) {

            // see the price update but haven't taken action yet
            Price currPrice = pricePath.get(i);
            State currState = new State(currPrice, currHolding);
            //int state_index = stateSpace.indexOf(currState);

            // choose action
            Shares shares_traded = chooseAction(currState);


            //int action_index = actionSpace.indexOf(shares_traded);

            // take action and yield to the next state
            Shares nextHolding = new Shares(currHolding.value() + shares_traded.value());
            nextHolding = Shares.max(Shares.min(nextHolding, maxHold), minHold);

            Price nextPrice = pricePath.get(i + 1);
            State nextState = new State(nextPrice, nextHolding);

            // observe reward
            final Result result = Result.getReward(currState, nextState);
            pnl.add(result.pnl);

            currHolding = new Shares(nextHolding.value());

        }

        final XYSeries series = new XYSeries("Out-of-Sample PnL");
        double sum = 0;
        double sum_x = 0;
        double sum_squares = 0;

        for (int i = 0; i < pnl.size(); i++) {
            double x = pnl.get(i);
            sum += x;
            series.add((double)i, sum);

            sum_x += x;
            sum_squares += x*x;
        }

        int n = pnl.size();
        double mean = sum_x / n;
        double variance = (1.0/(double)n) * sum_squares - mean * mean;
        double std = Math.sqrt(variance);
        double sharpe = Math.sqrt(260) * mean / std;
        System.out.println("sharpe = " + sharpe);
        System.out.println("std = " + std);
        System.out.println("mean = " + mean);

        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Out-of-Sample PnL",
                "Time",
                "PnL",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFrame frame = new JFrame("Out-of-Sample PnL");
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }




    private static final Random r = new Random();

    // choose action given the current state
    private static Shares chooseAction(State state) {
        Shares dn = new Shares(0);

        // apply epsilon-greedy policy to currActions
        double temp = Math.random();
        if (temp < Param._epsilon) {
            //select random;
            dn = actionSpace.get(r.nextInt(actionSpace.size()));
        } else {
            //select a s.t. q(st, a) is maximized
            dn = argmaxQ(state);
        }
        return dn;
    }

    // choose the action (from available ones) that maximizes the Q score with a given state
    public static Shares argmaxQ(State state) {
        int a = 0;

        double max = Double.MIN_VALUE;
        final int state_index = stateSpace.indexOf(state);
        final double[] temp = qMatrix[state_index];
        // for available actions

        /*
        int start = Math.max(0, actionSpace.indexOf(new Shares(minHold.value()-state.holding.value())));
        int end = Math.min(temp.length, actionSpace.indexOf(new Shares(maxHold.value()-state.holding.value())));

        for (int i = start; i < end+1; i++) {
            if (temp[i] > max) {
                max = temp[i];
                a = i;
            }
        }
        */


        for (int i = 0; i < temp.length; i++) {
            if (temp[i] > max) {
                max = temp[i];
                a = i;
            }
        }
        return actionSpace.get(a);


    }


    // calculate the max q score given a state
    private static double getMaxQ(State state) {
        //int a = 0;

        final int state_index = stateSpace.indexOf(state);
        double[] temp = qMatrix[state_index];
        double max = Double.MIN_VALUE;
        // for available actions

        /*
        int start = Math.max(0, actionSpace.indexOf(new Shares(minHold.value()-state.holding.value())));
        int end = Math.min(temp.length, actionSpace.indexOf(new Shares(maxHold.value()-state.holding.value())));

        for (int i = start; i < end+1; i++) {
            if (temp[i] > max) {
                max = temp[i];
                a = i;
            }
        }
        */

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] > max) {
                max = temp[i];
            }
        }

        return max;
    }



}
