package qLearner;


public class Cost {
	
	// constructor
	private Cost() { }
	
	public static double getSpreadCost(double dn) {
		return Param._tickSize * Math.abs(dn);
	}
	
	public static double getImpactCost(double dn) {
		return Math.pow(dn, 2) * Param._tickSize / Param._lotSize;
	}
	
	public static double getTotalCost(double dn) {
		return getSpreadCost(dn) + getImpactCost(dn);
	}
}
