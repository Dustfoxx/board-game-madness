package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.AbstractCell;
import Model.Board;
import Model.NormalCell;
import Model.Feature;
import Model.Footstep;
import Model.RougeAgent;
import Model.TempleCell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckActionTests {
    private final CheckAction checkAction = new CheckAction();
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

        cells[0][0] = new TempleCell();

        board = new Board(cells);
    }

    // @Test
    // TODO: Update tests
    // public void testMovementRecruiter() {
    // Recruiter mockedRecruiter = mock(Recruiter.class);
    // when(mockedRecruiter.getId()).thenReturn(1);
    // when(mockedRecruiter.getWalkedPath()).thenReturn(Arrays.asList(new int[] { 4,
    // 3 }));

    // board.getCell(5, 3).addPlayer(mockedRecruiter);

    // MutableBoolean[][] mask = checkAction.getValidMoves(mockedRecruiter, board,
    // 0);

    // MutableBoolean[][] expectedMask = new MutableBoolean[][]

    // { { false, false, false, false, false, false },
    // { false, false, false, false, false, false, },
    // { false, false, false, false, false, false },
    // { false, false, false, true, false, false },
    // { false, false, false, false, false, false },
    // { false, true, true, false, true, true },
    // { false, false, false, true, false, false } };

    // assertArrayEquals(expectedMask, mask);

    // mask = checkAction.getValidMoves(mockedRecruiter, board, 1);

    // expectedMask = new boolean[][]

    // { { false, false, false, false, false, false },
    // { false, false, false, false, false, false, },
    // { false, false, false, false, false, false },
    // { false, true, false, false, false, true },
    // { false, false, false, false, false, false },
    // { false, false, true, false, true, false },
    // { false, false, false, true, false, false } };

    // assertArrayEquals(expectedMask, mask);
    // }

    // @Test
    // public void testMovementAgent() {
    // RougeAgent mockedAgent = mock(RougeAgent.class);
    // when(mockedAgent.getId()).thenReturn(1);

    // board.getCell(3, 3).addPlayer(mockedAgent);

    // MutableBoolean[][] mask = checkAction.getValidMoves(mockedAgent, board);

    // M[][] expectedMask = new boolean[][]

    // { { false, false, false, false, false, false },
    // { false, false, false, true, false, false, },
    // { false, false, true, true, true, false },
    // { false, true, true, true, true, true },
    // { false, false, true, true, true, false },
    // { false, false, false, true, false, false },
    // { false, false, false, false, false, false } };

    // assertArrayEquals(expectedMask, mask);
    // }

    @Test
    public void testAskValidation() {
        RougeAgent mockedAgent = mock(RougeAgent.class);
        when(mockedAgent.getId()).thenReturn(1);

        board.getCell(3, 3).addPlayer(mockedAgent);

        assert checkAction.checkAskAction(mockedAgent, board);

        board.getCell(0, 0).addPlayer(mockedAgent);
        board.getCell(3, 3).removePlayer(mockedAgent);

        assertEquals(false, checkAction.checkAskAction(mockedAgent, board));
    }

    @Test
    public void testRevealValidation() {
        RougeAgent mockedAgent = mock(RougeAgent.class);
        when(mockedAgent.getId()).thenReturn(1);

        Footstep mockedFootstep = mock(Footstep.class);

        board.getCell(3, 3).addPlayer(mockedAgent);

        assert !checkAction.checkRevealAction(mockedAgent, board);

        board.getCell(3, 3).addToken(mockedFootstep);

        assert checkAction.checkAskAction(mockedAgent, board);
    }
}
