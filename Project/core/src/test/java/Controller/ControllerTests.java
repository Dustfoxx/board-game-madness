package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTests {
    private final Controller  controller = new Controller();

    @Test
    public void testAdd() {
        assertEquals(42, controller.Add(19, 25));
    }
}
