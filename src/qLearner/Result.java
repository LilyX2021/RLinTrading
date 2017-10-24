package qLearner;

import market.Price;
import market.Shares;

/**
 * Created by xionglihua on 10/5/17.
 */

public class Result {

    public final double pnl, cost, reward, trade;

    public Result(double pnl, double cost, double reward, double trade) {
        this.pnl = pnl;
        this.cost = cost;
        this.reward = reward;
        this.trade = trade;
    }

    public static Result getReward(State currState, State nextState) {

        final Price currPrice = currState.price;
        final Price nextPrice = nextState.price;

        final Shares currHolding = currState.holding;
        final Shares nextHolding = nextState.holding;

        final double dn = nextHolding.value() - currHolding.value();
        final double cost = Cost.getTotalCost(dn);

        final double pricedif = nextPrice.value() - currPrice.value();

        //final double pnl = currHolding.value() * pricedif - cost;
        final double pnl = nextHolding.value() * pricedif - cost;
        final double reward = pnl - 0.5 * Param._kappa * Math.pow(pnl, 2);

        return new Result(pnl, cost, reward, dn);
    }

}
