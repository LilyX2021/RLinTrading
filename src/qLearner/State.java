package qLearner;

import market.Price;
import market.Shares;

public class State {

    public final Shares holding;
    public final Price price;

    public State(Price price, Shares holding) {
        this.price = price;
        this.holding = holding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (!holding.equals(state.holding)) return false;
        return price.equals(state.price);
    }

    @Override
    public int hashCode() {
        int result = holding.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "price=" + price +
                ", holding=" + holding +
                '}';
    }
}
