package market;

public class Price {
    private final int x;

    public Price(double d) {
        x = (int) Math.round(10 * d);
    }

    public static Price of(double d) {
        return new Price(d);
    }

    public double value() {
        return 0.1 * x;
    }

    public Price minus(Price p) {
        return new Price(this.x - p.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        return x == price.x;
    }

    @Override
    public int hashCode() {
        return x;
    }

    @Override
    public String toString() {
        return "Price{" +
                "x=" + x +
                '}';
    }
}
