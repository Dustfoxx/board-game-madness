package Controller;

import org.junit.jupiter.api.Test;

import Model.Board;
import Model.Game;
import Model.Player;
import Model.Recruiter;
import Model.RougeAgent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class ControllerTests {
    private final Controller controller = new Controller();

    @Test
    public void testAdd() {
        assertEquals(42, controller.Add(19, 23));
    }

    @Test
    public void testMovementCheck() {
        // Needs rewriting with classes
        // Left for later when temple class is available
    }

    @Test
    public void testNoRecruiter() {
        List<Player> players = new ArrayList<Player>();
        Player mockedPlayer = mock(RougeAgent.class);
        players.add(mockedPlayer);
        players.add(mock(RougeAgent.class));
        Game mockedGame = mock(Game.class);

        when(mockedGame.getPlayers()).thenReturn(players);

        Throwable exception = assertThrows(IllegalStateException.class,
                () -> {
                    final GameController gameController = new GameController(mockedGame);
                });

        assertEquals("Recruiter player not found", exception.getMessage());

    }

    @Test
    public void testNoAgents() {
        List<Player> players = new ArrayList<Player>();
        Player mockedPlayer = mock(Recruiter.class);
        players.add(mockedPlayer);
        Game mockedGame = mock(Game.class);

        when(mockedGame.getPlayers()).thenReturn(players);

        Throwable exception = assertThrows(IllegalStateException.class,
                () -> {
                    final GameController gameController = new GameController(mockedGame);
                });

        assertEquals("No agents found", exception.getMessage());
    }

    @Test
    public void testTooManyRecruiters() {
        List<Player> players = new ArrayList<Player>();
        Player mockedPlayer = mock(Recruiter.class);
        players.add(mockedPlayer);
        players.add(mock(RougeAgent.class));
        players.add(mock(Recruiter.class));
        Game mockedGame = mock(Game.class);

        when(mockedGame.getPlayers()).thenReturn(players);

        Throwable exception = assertThrows(IllegalStateException.class,
                () -> {
                    final GameController gameController = new GameController(mockedGame);
                });

        assertEquals("More than one recruiter", exception.getMessage());
    }

    @Test
    public void testTurnOrder() { // Could possibly also be mocked but much more difficult than previous versions
        List<Player> players = new ArrayList<Player>();
        Player mockedPlayer = mock(RougeAgent.class);
        players.add(mockedPlayer);
        players.add(mock(RougeAgent.class));
        players.add(mock(Recruiter.class));

        List<Player> playerOrder = new ArrayList<Player>();

        playerOrder.add(players.get(2));
        playerOrder.add(players.get(0));
        playerOrder.add(players.get(1));

        Board board = mock(Board.class);

        Game game = new Game(players, board, mockedPlayer);
        GameController gameController = new GameController(game);

        for (int i = 0; i < 6; i++) {
            assertEquals(playerOrder.get(i % 3), game.getCurrentPlayer());
            gameController.newTurn();
        }
    }

    @Test
    public void testNrOfTurns() {
        List<Player> players = new ArrayList<Player>();
        Player mockedPlayer = mock(RougeAgent.class);
        players.add(mockedPlayer);
        players.add(mock(RougeAgent.class));
        players.add(mock(Recruiter.class));

        Board board = mock(Board.class);

        Game game = new Game(players, board, mockedPlayer);
        GameController gameController = new GameController(game);

        for (int i = 0; i < 6; i++) {
            gameController.newTurn();
        }
        assertEquals(3, game.getCurrentTime());
    }

}
