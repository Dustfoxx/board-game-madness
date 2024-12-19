package Controller;

import Model.*;
import Model.Game.gameStates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTests {
    Csv boardCsv;
    ArrayList<User> users;

    @BeforeEach
    void setUp() {
        String[][] boardData = new String[][] {
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
                new String[] { "BILLBOARD:BUS", "BILLBOARD:BUS", "BILLBOARD:BUS" },
        };
        boardCsv = mock(Csv.class);
        when(boardCsv.getData()).thenReturn(boardData);
        users = new ArrayList<>(Arrays.asList(
                new User(0, "User1"),
                new User(1, "User2"),
                new User(2, "User3"),
                new User(3, "User4"),
                new User(4, "User5")));
    }

    @Test
    public void testRecruiterExists() {
        // Arrange
        GameController controller;

        // Act
        controller = new GameController(boardCsv, users);

        // Assert
        assertEquals(Recruiter.class, controller.getGame().getPlayers().get(0).getClass());
    }

    @Test
    public void testOnePlayer() {
        // Arrange
        users = new ArrayList<>(Arrays.asList(new User(0, "User1")));
        // Act
        GameController controller = new GameController(boardCsv, users);
        // Assert
        assertNotNull(controller);
    }

    @Test
    public void testNoPlayers() {
        // Arrange
        users = new ArrayList<>();

        // Act
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new GameController(boardCsv, users));

        // Assert
        assertEquals("No users registered!", exception.getMessage());
    }

    @Test
    public void testTurnOrder() {
        // Arrange
        GameController controller = new GameController(boardCsv, users);
        List<Player> players = controller.getGame().getPlayers();
        controller.getGame().setGameState(gameStates.ONGOING);
        List<Player> expectedOrder = new ArrayList<>(Arrays.asList(
                players.get(0), players.get(1), players.get(2), players.get(0), players.get(3), players.get(4)));
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
        GameController controller = new GameController(boardCsv, users);
        controller.getGame().setGameState(gameStates.ONGOING);

        for (int i = 0; i < 6; i++) {
            controller.newTurn();
        }
        assertEquals(2, controller.getGame().getCurrentTime());
    }
}
