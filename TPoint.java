/**
 * Just a simple struct to represent an (x,y) point.
 */
public class TPoint {
    public final int x;
    public final int y;

    public TPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TPoint(TPoint p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TPoint)) return false;
        TPoint p = (TPoint) obj;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
