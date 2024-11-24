package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import Model.Feature;
import Model.Footstep;
import Model.Board;
import Model.BrainFact;
import Model.Cell;


public class RecruiterActionControllerTests {

    private final RecruiterActionController actionController = new RecruiterActionController();

    private Board board;

    @BeforeEach
    void setUp() {
    }

   /* @Test
    void testAnswer() {
        board = new Board(3, 3);
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[]{0, 0});
        walkedPath.add(new int[]{1, 1});

        Cell selectedCell = board.getCell(walkedPath.get(1)[0], walkedPath.get(1)[1]);
        
        assertNotNull(selectedCell);
        assert(selectedCell.getFeatures().length != 0);
        // Call the answer method
        boolean result = actionController.answer(selectedCell.getFeatures()[0], board, walkedPath);


        // Recreating to make sure references work out
        Cell verifyCell = board.getCell(1, 1);

        assertEquals(1, verifyCell.getTokens().size(), "The selected cell should contain one token.");
        assertTrue(verifyCell.getTokens().get(0) instanceof Footstep, "The token should be a Footstep.");
    }

    */

}
