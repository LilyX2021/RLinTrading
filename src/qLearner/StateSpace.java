package qLearner;

import market.Price;
import market.Shares;

import java.util.ArrayList;
import java.util.Collection;

public class StateSpace extends Indexable<State> {
    public StateSpace(Collection<State> coll) {
        super(coll);
    }

    public static StateSpace create(){
        ArrayList<State> states = new ArrayList<>();
        PriceSpace priceSpace = PriceSpace.create();
        HoldingSpace holdingSpace = HoldingSpace.create();

        for (Price p : priceSpace){
            for (Shares h : holdingSpace){
                states.add(new State(p,h));
            }
        }

        return new StateSpace(states);
    }

    @Override
    public String toString() {
        return "StateSpace{" +
                "list=" + list +
                '}';
    }
}
