package Controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Board;
import Model.BrainFact;
import Model.Feature;
import Model.Footstep;
import Model.NormalCell;

public class RecruiterActionControllerTests {

    private final RecruiterActionController actionController = new RecruiterActionController();

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

    // Note need
    @Test
    void testAnswerTrue() {
        // Arrange
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[] { 0, 0 });
        walkedPath.add(new int[] { 1, 1 });

        NormalCell selectedCell = (NormalCell) board.getCell(walkedPath.get(1)[0], walkedPath.get(1)[1]);

        // Act
        // Call the answer method
        boolean result = actionController.answer(Feature.BILLBOARD, board, walkedPath);

        // Recreating to make sure references work out
        NormalCell verifyCell = (NormalCell) board.getCell(walkedPath.get(0)[0], walkedPath.get(0)[1]);

        // Assert
        assertTrue(result);
        assertNotNull(selectedCell);
        assertEquals(1, verifyCell.getTokens().size(), "The selected cell should contain one token");
        assertTrue(verifyCell.getTokens().get(0) instanceof Footstep, "The token should be a Footstep");
    }

    @Test
    void testAnswerFalse() {
        // Arrange
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[] { 0, 0 });
        walkedPath.add(new int[] { 1, 1 });

        NormalCell selectedCell = (NormalCell) board.getCell(walkedPath.get(1)[0], walkedPath.get(1)[1]);

        // Act
        // Call the answer method
        boolean result = actionController.answer(Feature.COURIER, board, walkedPath);

        // Recreating to make sure references work out
        NormalCell verifyCell = (NormalCell) board.getCell(walkedPath.get(0)[0], walkedPath.get(0)[1]);

        // Assert
        assertFalse(result);
        assertNotNull(selectedCell);
        assertEquals(0, verifyCell.getTokens().size(), "The selected cell should contain one token");
    }

    // Note need
    @Test
    void testAnswerFullCell() { // test for checking that you cant set brainfact ontop of anoter brainfact
        // Arrange
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[] { 2, 1 });

        NormalCell selectedCell = (NormalCell) board.getCell(walkedPath.get(0)[0], walkedPath.get(0)[1]);

        selectedCell.addToken(new BrainFact(22));

        // Call the answer method
        boolean result = actionController.answer(Feature.BILLBOARD, board, walkedPath);

        // Recreating to make sure references work out
        NormalCell verifyCell = (NormalCell) board.getCell(walkedPath.get(0)[0], walkedPath.get(0)[1]);

        // Assert
        assertFalse(result);
        assertEquals(1, verifyCell.getTokens().size(), "The selected cell should contain 1 token");
        assertTrue(verifyCell.getTokens().get(0) instanceof BrainFact, "The token should be a BrainFact");

    }

}
