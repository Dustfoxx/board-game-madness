package Controller;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTests {
    Csv boardCsv;

    @BeforeEach
    void setUp() {
        String[][] boardData = new String[][] {
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
        };
        boardCsv = mock(Csv.class);
        when(boardCsv.getData()).thenReturn(boardData);
    }

    @Test
    public void testRecruiterExists() {
        // Arrange
        int nrOfPlayers = 3;
        GameController controller;

        // Act
        controller = new GameController(nrOfPlayers, boardCsv);

        // Assert
        assertEquals(Recruiter.class, controller.getGame().getPlayers().get(0).getClass());
    }

    @Test
    public void testOnePlayer() {
        // Arrange
        int nrOfPlayers = 1;

        // Act
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    final GameController controller = new GameController(nrOfPlayers, boardCsv);
                });

        // Assert
        assertEquals("Must be more than 1 player", exception.getMessage());
    }

    @Test
    public void testNoPlayers() {
        // Arrange
        int nrOfPlayers = 0;

        // Act
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    final GameController controller = new GameController(nrOfPlayers, boardCsv);
                });

        // Assert
        assertEquals("Must be more than 1 player", exception.getMessage());
    }

    @Test
    public void testTurnOrder() {
        // Arrange
        int nrOfPlayers = 3;
        GameController controller = new GameController(nrOfPlayers, boardCsv);
        List<Player> players = controller.getGame().getPlayers();
        List<Player> expectedOrder = new ArrayList<>(Arrays.asList(
                players.get(0), players.get(1), players.get(2), players.get(0), players.get(1), players.get(2)));
        List<Player> actualOrder = new ArrayList<>();

        // Act
        for (int i = 0; i < 6; i++) {
            actualOrder.add(controller.getGame().getCurrentPlayer());
            controller.newTurn();
        }

        // Assert
        for (int i = 0; i < 6; i++) {
            assertArrayEquals(expectedOrder.toArray(), actualOrder.toArray());
        }
    }

    @Test
    public void testNrOfTurns() {
        // Arrange
        int nrOfPlayers = 3;
        GameController controller = new GameController(nrOfPlayers, boardCsv);

        for (int i = 0; i < 6; i++) {
            controller.newTurn();
        }
        assertEquals(3, controller.getGame().getCurrentTime());
    }
}
