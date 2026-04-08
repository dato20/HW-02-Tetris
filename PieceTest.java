import org.junit.Test;
import static org.junit.Assert.*;

public class PieceTest {

    @Test
    public void testSquare() {
        Piece square = new Piece("0 0 0 1 1 0 1 1");
        assertEquals(2, square.getWidth());
        assertEquals(2, square.getHeight());
        assertArrayEquals(new int[]{0, 0}, square.getSkirt());
    }

    @Test
    public void testRotationEquality() {
        Piece stick = new Piece("0 0 0 1 0 2 0 3");
        Piece rot = stick.computeNextRotation();
        Piece twice = rot.computeNextRotation().computeNextRotation().computeNextRotation();
        assertTrue(twice.equals(stick));
    }

    @Test
    public void testFastRotations() {
        Piece[] pieces = Piece.getPieces();
        Piece stick = pieces[Piece.STICK];
        assertEquals(stick, stick.fastRotation().fastRotation().fastRotation().fastRotation());
    }
}
