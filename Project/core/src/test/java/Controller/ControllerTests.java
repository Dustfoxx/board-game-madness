package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTests {
    private final Controller controller = new Controller();
    private final CheckMove checkMove = new CheckMove();

    @Test
    public void testAdd() {
        assertEquals(42, controller.Add(19, 23));
    }

    @Test
    public void testMovementCheck() {
        int[][] boardLayout = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0 } };
        int[][] expectedMask = {{}};


        assertEquals(, boardLayout);
    }
}
