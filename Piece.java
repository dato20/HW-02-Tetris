import java.util.*;

/**
 * An immutable representation of a Tetris piece in a particular rotation.
 * A piece is defined by its body (a set of TPoints), its width, height,
 * and its skirt.
 */
public class Piece {

    // Instance variables
    private TPoint[] body;
    private int[] skirt;
    private int width;
    private int height;
    private Piece next; // for fast rotation

    // Cached array of root pieces
    private static Piece[] pieces;

    // Constants for the standard set of Tetris pieces
    public static final int STICK = 0;
    public static final int L1 = 1;
    public static final int L2 = 2;
    public static final int S1 = 3;
    public static final int S2 = 4;
    public static final int SQUARE = 5;
    public static final int PYRAMID = 6;

    /**
     * Constructor — takes an array of TPoints describing the body.
     */
    public Piece(TPoint[] points) {
        body = points.clone();
        computeWidthHeightSkirt();
    }

    /**
     * Alternate constructor — takes a string of x y pairs (e.g. "0 0 0 1 1 0 1 1").
     */
    public Piece(String points) {
        this(parsePoints(points));
    }

    /**
     * Parses a string of x,y pairs into a TPoint[].
     */
    private static TPoint[] parsePoints(String string) {
        StringTokenizer tok = new StringTokenizer(string);
        int n = tok.countTokens() / 2;
        TPoint[] points = new TPoint[n];
        for (int i = 0; i < n; i++) {
            int x = Integer.parseInt(tok.nextToken());
            int y = Integer.parseInt(tok.nextToken());
            points[i] = new TPoint(x, y);
        }
        return points;
    }

    /**
     * Computes width, height, and skirt from the body.
     */
    private void computeWidthHeightSkirt() {
        int maxX = 0;
        int maxY = 0;
        for (TPoint p : body) {
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        }
        width = maxX + 1;
        height = maxY + 1;

        skirt = new int[width];
        Arrays.fill(skirt, Integer.MAX_VALUE);
        for (TPoint p : body) {
            if (p.y < skirt[p.x]) skirt[p.x] = p.y;
        }
    }

    /**
     * Returns the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a copy of the body.
     */
    public TPoint[] getBody() {
        return body.clone();
    }

    /**
     * Returns the skirt.
     */
    public int[] getSkirt() {
        return skirt.clone();
    }

    /**
     * Equals — true if the bodies contain the same points.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Piece)) return false;

        Piece other = (Piece) obj;
        return new HashSet<>(Arrays.asList(this.body))
                .equals(new HashSet<>(Arrays.asList(other.body)));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(body);
    }

    /**
     * Computes the next rotation (CCW 90°) of this piece.
     */
    public Piece computeNextRotation() {
        TPoint[] rotated = new TPoint[body.length];
        for (int i = 0; i < body.length; i++) {
            TPoint p = body[i];
            rotated[i] = new TPoint(height - 1 - p.y, p.x);
        }
        // move to lowest position (reset origin to (0,0))
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (TPoint p : rotated) {
            if (p.x < minX) minX = p.x;
            if (p.y < minY) minY = p.y;
        }
        for (int i = 0; i < rotated.length; i++) {
            rotated[i] = new TPoint(rotated[i].x - minX, rotated[i].y - minY);
        }
        return new Piece(rotated);
    }

    /**
     * Returns the next rotation in constant time.
     */
    public Piece fastRotation() {
        return next;
    }

    /**
     * Builds the linked list of all rotations starting from the root piece.
     * Rotations are precomputed and circularly linked.
     */
    public static Piece makeFastRotations(Piece root) {
        Piece current = root;
        Piece nextRotation = current.computeNextRotation();
        while (!nextRotation.equals(root)) {
            current.next = nextRotation;
            current = nextRotation;
            nextRotation = current.computeNextRotation();
        }
        current.next = root;
        return root;
    }

    /**
     * Lazy-loads the standard set of pieces.
     */
    public static Piece[] getPieces() {
        if (pieces == null) {
            pieces = new Piece[] {
                    makeFastRotations(new Piece("0 0 0 1 0 2 0 3")),  // Stick
                    makeFastRotations(new Piece("0 0 0 1 0 2 1 0")),  // L1
                    makeFastRotations(new Piece("0 0 1 0 1 1 1 2")),  // L2
                    makeFastRotations(new Piece("0 0 1 0 1 1 2 1")),  // S1
                    makeFastRotations(new Piece("0 1 1 1 1 0 2 0")),  // S2
                    makeFastRotations(new Piece("0 0 0 1 1 0 1 1")),  // Square
                    makeFastRotations(new Piece("0 0 1 0 1 1 2 0"))   // Pyramid
            };
        }
        return pieces;
    }

    @Override
    public String toString() {
        return Arrays.toString(body);
    }
}
