package qLearner;

import market.Shares;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class ActionSpace extends Indexable<Shares> {

    public ActionSpace(Collection<Shares> coll) {
        super(coll);
    }

    // the space of possible trades: dn
    // boundary condition: -M <=  dn(t) + n(t-1) <= M, where _M = 10

    public static ActionSpace create(){
        ArrayList<Shares> actions = new ArrayList<>();

        actions.add(new Shares(0));
        for (int i = 1; i < Param._K+1; i++){
            actions.add(new Shares(i * Param._lotSize));
        }
        for (int i = 1; i < Param._K+1; i++){
            actions.add(new Shares(-i * Param._lotSize));
        }

        /*
        int size = 2*Param._K + 1;
        int curr = (-1) * Param._K;

        for (int i = 0; i < size; i++) {
            actions.add(new Shares(Param._lotSize * (curr + i)));
        }*/

        return new ActionSpace(actions);
    }



    @Override
    public String toString() {
        return "ActionSpace{" +
                "list=" + list +
                '}';
    }


}
