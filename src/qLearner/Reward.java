package qLearner;

public class Reward {

	// Do I need it here?
	public Reward() { }
	
	public static double getPnL(double prePrice, double nextPrice, double totalCost, double n) {
		return n * (nextPrice - prePrice) - totalCost;
	}
	
	public static double getReward(double prePrice, double nextPrice, double totalCost, double n) {
		double pnl = getPnL(prePrice, nextPrice, totalCost, n);
		return pnl - 0.5 * Param._kappa * Math.pow(pnl, 2);
	}
	

}
