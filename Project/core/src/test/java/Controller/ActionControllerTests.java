package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionControllerTests {
    private final ActionController  actionController = new ActionController();


    @Test
    public void testAdd() {
        assertEquals(42, controller.Add(19, 23));
    }

    @Test
    public void testAsk() {
       actionController.ask();
    }
}
