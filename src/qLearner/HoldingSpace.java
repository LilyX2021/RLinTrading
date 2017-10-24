package qLearner;

import market.Shares;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HoldingSpace extends Indexable<Shares>{

    public HoldingSpace(Collection<Shares> coll) {
        super(coll);
    }

    @Override
    public String toString() {
        return "HoldingSpace{" +
                "list=" + list +
                '}';
    }

    public static HoldingSpace create() {

        List<Shares> L = new ArrayList<>();

        int size = 2*Param._M + 1;
        int curr = (-1) * Param._M;

        for (int i = 0; i < size; i++) {
            L.add(new Shares(Param._lotSize * (curr + i)));
        }

        return new HoldingSpace(L);
    }

    public static Shares getMax(){
        return new Shares(Param._lotSize * Param._M);
    }

    public static Shares getMin(){
        return new Shares((-1) * Param._lotSize * Param._M);

    }

}
