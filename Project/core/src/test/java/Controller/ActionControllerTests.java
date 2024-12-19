package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Recruiter;
import Model.Step;
import Model.AbstractCell;
import Model.Board;
import Model.BrainFact;

public class ActionControllerTests {
    private final ActionController actionController = new ActionController();

    private Board board;

    @BeforeEach
    void setUp() {
        NormalCell[][] cells = new NormalCell[3][3];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new NormalCell(new Feature[] { Feature.BILLBOARD, Feature.BOOKSTORE });
            }
        }
        board = new Board(cells);
    }

    @Test
    void testAsk() {
        // Prepare
        Feature feature = Feature.BILLBOARD;
        Recruiter recruiter = new Recruiter(0, "", new Feature[] { Feature.BILLBOARD, Feature.BUS, Feature.BOOKSTORE });
        recruiter.addToWalkedPath(0, 0);
        recruiter.addToWalkedPath(0, 1);

        // Call function
        actionController.ask(feature, recruiter, board);

        // Verify that a Footstep token was added to the correct cell (0, 0).
        NormalCell firstStep = (NormalCell) board.getCell(0, 0);
        assertEquals(1, firstStep.getTokens().size(), "Expected one token in cell (0, 0).");
        assertTrue(firstStep.getTokens().get(0) instanceof Footstep, "Expected a Footstep token in cell (0, 0).");

        // Verify that no Footstep token was added to cell (0, 1).
        NormalCell secondStep = (NormalCell) board.getCell(0, 1);
        assertEquals(0, secondStep.getTokens().size(), "Expected no tokens in cell (0, 1).");
    }

    @Test
    void testRevealSingle() {
        int[] position = { 0, 0 };
        AbstractCell cell = board.getCell(position[0], position[1]);
        cell.addToken(new Step(1));
        cell.addToken(new Footstep());
        actionController.reveal(cell);
        assert (cell.getTokens().size() == 2);
        assert (!cell.containsFootstep());
        assertEquals(1, ((BrainFact) cell.getTokens().get(1)).getTimestamp(),
                "Expected timestamp to be 1 but was: " + ((BrainFact) cell.getTokens().get(1)).getTimestamp());
    }

    @Test
    void testRevealEmpty() {
        int[] position = { 1, 1 };
        AbstractCell cell = board.getCell(position[0], position[1]);
        assertThrows(IllegalStateException.class, () -> actionController.reveal(cell));
    }
}
