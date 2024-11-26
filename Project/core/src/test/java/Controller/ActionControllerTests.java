package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import Model.Feature;
import Model.Footstep;
import Model.NormalCell;
import Model.Board;
import Model.BrainFact;

public class ActionControllerTests {
    private final ActionController actionController = new ActionController();

    private Board board;
    private Footstep footstep;

    @BeforeEach
    void setUp() {
        NormalCell[][] cells = new NormalCell[3][3];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new NormalCell(new Feature[]{Feature.BILLBOARD, Feature.BOOKSTORE});
            }
        }
        board = new Board(cells);
        footstep = new Footstep();
    }

    @Test
    void testAsk() {
        NormalCell testCell = new NormalCell(new Feature[]{Feature.BILLBOARD, Feature.BOOKSTORE});
        Feature selectedFeature = actionController.ask(testCell);

        assertNotNull(selectedFeature, "The selected feature should not be null");
        assertEquals(Feature.BILLBOARD, selectedFeature, "The feature should be BILLBOARD.");
    }

    @Test
    void testRevealSingle() {
        int[] position = {0, 0};
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[]{position[0], position[1]});
        walkedPath.add(new int[]{0, 1});

        actionController.reveal(footstep, board, position, walkedPath);
        NormalCell boardCell = (NormalCell) board.getCell(position[0], position[1]);
        assert(boardCell.getTokens().size() == 1);
        assertEquals(1, ((BrainFact) boardCell.getTokens().get(0)).getTimestamp(), 
        "Expected timestamp to be 1 but was: " + ((BrainFact) boardCell.getTokens().get(0)).getTimestamp());
    }

    @Test
    void testRevealEmpty() {
        int[] position = {1, 1};
        List<int[]> walkedPath = new ArrayList<>();
        walkedPath.add(new int[]{2, 2});
        actionController.reveal(footstep, board, position, walkedPath);

        NormalCell boardCell = (NormalCell) board.getCell(position[0], position[1]);
        assert(boardCell.getTokens().size() == 0);
    }
}
