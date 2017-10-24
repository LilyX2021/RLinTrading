package market;

import market.Shares;
import org.junit.Test;
import qLearner.*;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

/**
 * Created by xionglihua on 10/19/17.
 */

public class Test_Loop {

    // price path
    private static ArrayList<Price> pricePath;
    // holding space
    private static HoldingSpace holdingSpace;
    private static Shares maxHold;
    private static Shares minHold;
    // price space
    private static PriceSpace priceSpace;
    // action space
    private static ActionSpace actionSpace;
    // state matrix
    //static double[][] stateMatrix;
    private static StateSpace stateSpace;
    // Q matrix
    private static double[][] qMatrix;

    @Test
    public void testSwap() throws Exception {
        Shares currHolding = new Shares(0);
        Shares nextHolding;

        for (int i = 1; i < 5; i++){

            nextHolding = new Shares(currHolding.value()+1);
            currHolding = nextHolding;

            assertTrue(currHolding.value() == nextHolding.value());
            assertTrue(nextHolding.value() == i);

        }
    }

    @Test
    public void testInit() throws Exception {

        // generate the price path
        pricePath = PriceSampler.genPrice(Param._nTrain);
        assertTrue(pricePath.size() == Param._nTrain + 1);
        System.out.println(pricePath.get(0).value());
        //assertTrue(pricePath.get(0).value() == Param._pe);

        // generate the Action Space
        actionSpace = ActionSpace.create();
        assertTrue(actionSpace.size() == Param._K * 2 + 1);
        assertTrue(actionSpace.indexOf(new Shares(0)) == Param._K);
        System.out.println("actionSpace[0] is " + actionSpace.get(0));

        // generate State Space
        stateSpace = StateSpace.create();
        priceSpace = PriceSpace.create();
        holdingSpace = HoldingSpace.create();

        assertTrue(priceSpace.size() == Param._pSize);
        assertTrue(holdingSpace.size() == Param._M * 2 + 1);
        assertTrue(stateSpace.size() == priceSpace.size() * holdingSpace.size());

        assertTrue(priceSpace.indexOf(new Price(0.1)) == 0);
        assertTrue(priceSpace.indexOf(new Price(100)) == Param._pSize - 1);

        assertTrue(holdingSpace.indexOf(new Shares(0)) == Param._M);

        assertTrue(stateSpace.indexOf(new State(new Price(0.1), new Shares(-900))) == 1);

        System.out.println("Index of State(60.0, 0) is " + stateSpace.indexOf(new State(new Price(60.0), new Shares(0))));



        System.out.println("stateSpace[20] is " + stateSpace.get(20));
        System.out.println("priceSpace[20] is " + priceSpace.get(20));
        System.out.println("holdingSpace[5] is " + holdingSpace.get(5));


        // get the min & max holding
        maxHold = HoldingSpace.getMax();
        minHold = HoldingSpace.getMin();

        assertTrue(maxHold.value() == Param._M * Param._lotSize);
        assertTrue(minHold.value() == -1 * Param._M * Param._lotSize);

        // initialize the Q Matrix as zero matrix
        int nStates = stateSpace.size();
        int nActions = actionSpace.size();
        qMatrix = new double[nStates][nActions];

        //System.out.println(qMatrix[0][0]);
        for (int i = 0; i < nActions; i++){
            System.out.println(qMatrix[2][i]);
        }
    }

    @Test
    public void testArgMaxQ() throws Exception {

        // generate the price path
        pricePath = PriceSampler.genPrice(Param._nTrain);

        // generate the Action Space
        actionSpace = ActionSpace.create();

        // generate State Space
        stateSpace = StateSpace.create();
        priceSpace = PriceSpace.create();
        holdingSpace = HoldingSpace.create();

        // get the min & max holding
        maxHold = HoldingSpace.getMax();
        minHold = HoldingSpace.getMin();

        // initialize the Q Matrix as zero matrix
        int nStates = stateSpace.size();
        int nActions = actionSpace.size();
        qMatrix = new double[nStates][nActions];

        State state0 = new State(new Price(Param._pe), new Shares(0));

        System.out.println(QLearner2.argmaxQ(state0));
    }
}
