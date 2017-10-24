package qLearner;

import market.Price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PriceSpace extends Indexable<Price> {
    public PriceSpace(Collection<Price> coll) {
        super(coll);
    }

    public static PriceSpace create() {
        // price space P
        List<Price> L = new ArrayList<>();
        for (int j = 0; j < Param._pSize; j++) {
            L.add(new Price(Param._tickSize * (j+1)));
        }
        return new PriceSpace(L);
    }

    @Override
    public String toString() {

        return "PriceSpace{" +
                "list=" + list +
                '}';
    }
}
