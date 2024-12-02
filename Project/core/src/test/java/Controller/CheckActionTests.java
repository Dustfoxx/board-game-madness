package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.AbstractCell;
import Model.Board;
import Model.NormalCell;
import Model.Feature;
import Model.Recruiter;
import Model.RougeAgent;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

public class CheckActionTests {
    private final CheckAction checkMove = new CheckAction();
    private int rows;
    private int columns;
    private AbstractCell[][] cells;
    private Board board;

    @BeforeEach
    void setUp() {
        rows = 7;
        columns = 6;
        cells = new AbstractCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Feature[] features = new Feature[2];
                features[0] = Feature.BILLBOARD;
                features[1] = Feature.BOOKSTORE;
                cells[row][column] = new NormalCell(features);
            }
        }
        board = new Board(cells);
    }

    @Test
    public void testMovementRecruiter() {
        Recruiter mockedRecruiter = mock(Recruiter.class);
        when(mockedRecruiter.getId()).thenReturn(1);
        when(mockedRecruiter.getWalkedPath()).thenReturn(Arrays.asList(new int[] { 4, 3 }));

        board.getCell(5, 3).addPlayer(mockedRecruiter);

        int[][] mask = checkMove.getValidMoves(mockedRecruiter, board, 0);

        int[][] expectedMask = new int[][]

        { { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 1, 1 },
                { 0, 0, 0, 1, 0, 0 } };

        assertArrayEquals(expectedMask, mask);

        mask = checkMove.getValidMoves(mockedRecruiter, board, 1);

        expectedMask = new int[][]

        { { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 0 } };

        assertArrayEquals(expectedMask, mask);
    }

    @Test
    public void testMovementAgent() {
        RougeAgent mockedAgent = mock(RougeAgent.class);
        when(mockedAgent.getId()).thenReturn(1);

        board.getCell(3, 3).addPlayer(mockedAgent);

        int[][] mask = checkMove.getValidMoves(mockedAgent, board);

        int[][] expectedMask = new int[][]

        { { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, },
                { 0, 0, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1 },
                { 0, 0, 1, 1, 1, 0 },
                { 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0 } };

        assertArrayEquals(expectedMask, mask);
    }
}
