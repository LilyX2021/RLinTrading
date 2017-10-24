package market;

/**
 * Created by xionglihua on 10/5/17.
 */

public class Shares {
    private final int shares;


    public Shares(int shares) {
        this.shares = shares;
    }

    public int value() {
        return shares;
    }

    public static Shares max(Shares a, Shares b) {
        return a.value() > b.value() ? a : b;
    }


    public static Shares min(Shares a, Shares b) {
        return a.value() < b.value() ? a : b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shares shares1 = (Shares) o;

        return shares == shares1.shares;
    }

    @Override
    public int hashCode() {
        return shares;
    }

    @Override
    public String toString() {
        return shares + "";
    }


}
