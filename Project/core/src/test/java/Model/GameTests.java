package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTests {

    private Game game;
    private Feature[] featuresOfInterest;
    private Player player1;
    private Player player2;
    private Board board;
    private int rows;
    private int columns;
    private AbstractCell[][] cells;

    @BeforeEach
    void setUp() {
        // Mocking Player and Board objects
        featuresOfInterest = new Feature[3];
        player1 = new Recruiter(0, null, featuresOfInterest);
        player2 = new RougeAgent(1, null);
        rows = 3;
        columns = 3;
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
        List<Player> players = Arrays.asList(player1, player2);

        // Initializing the Game object
        game = new Game(players, board, player1);
    }

    @Test
    void testGameConstructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Game(null, board, player1),
                "Constructor should throw an exception if players list is null.");
        assertThrows(IllegalArgumentException.class, () -> new Game(Collections.emptyList(), board, player1),
                "Constructor should throw an exception if players list is empty.");
        assertThrows(IllegalArgumentException.class, () -> new Game(Arrays.asList(player1, player2), null, player1),
                "Constructor should throw an exception if board is null.");
        assertThrows(IllegalArgumentException.class, () -> new Game(Arrays.asList(player1, player2), board, null),
                "Constructor should throw an exception if startingPlayer is null.");
        Player player3 = new RougeAgent(2, "Player 3");
        assertThrows(IllegalArgumentException.class, () -> new Game(Arrays.asList(player1, player2), board, player3),
                "Constructor should throw an exception if startingPlayer is not in the players list.");
    }

    @Test
    void testGameInitialization() {
        assertFalse(game.isGameOver(), "Game should not be over initially.");
        assertEquals(player1, game.getCurrentPlayer(), "Starting player should be Player 1.");
        assertEquals(1, game.getCurrentTime(), "Initial game time should be 1.");
        assertEquals(0, game.getAmountRecruited(), "Amount recruited should start at 0.");
        assertEquals(0, game.getMindSlipCount(), "Mind slip count should start at 0.");
        assertEquals(board, game.getBoard(), "Board should match the one passed in.");
        assertEquals(2, game.getPlayers().size(), "Game should have 2 players.");
    }

    @Test
    void testSetGameOver() {
        game.setGameOver();
        assertTrue(game.isGameOver(), "Game should be marked as over.");
        assertThrows(IllegalStateException.class, () -> game.setGameOver(),
                "Constructor should throw an exception if game is already over");
    }

    @Test
    void testSetCurrentPlayer() {
        game.setCurrentPlayer(player2);
        assertEquals(player2, game.getCurrentPlayer(), "Current player should be Player 2.");
        game.setCurrentPlayer(player1);
        assertEquals(player1, game.getCurrentPlayer(), "Current player should be Player 1.");
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentPlayer(new RougeAgent(2, null)),
                "Setting the next player to one not in the game should throw IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentPlayer(null),
                "Setting the next player to null should throw an IllegalArgumentException.");
    }

    @Test
    void testIncrementTime() {
        game.incrementTime();
        assertEquals(2, game.getCurrentTime(), "Game time should increment to 2.");
        game.incrementTime();
        assertEquals(3, game.getCurrentTime(), "Game time should increment to 3.");
    }

    @Test
    void testAddAmountRecruited() {
        game.addAmountRecruited(1);
        assertEquals(1, game.getAmountRecruited(), "Amount recruited should increase to 1.");
        game.addAmountRecruited(2);
        assertEquals(3, game.getAmountRecruited(), "Amount recruited should increase to 3.");
        assertThrows(IllegalArgumentException.class, () -> game.addAmountRecruited(-1),
                "Adding a negative number to the amount recruited should throw an IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> game.addAmountRecruited(0),
                "Adding zero to the amount recruited should throw an IllegalArgumentException.");
    }

    @Test
    void testIncrementMindSlipCount() {
        game.incrementMindSlipCount();
        assertEquals(1, game.getMindSlipCount(), "Mind slip count should increase to 1.");
        game.incrementMindSlipCount();
        assertEquals(2, game.getMindSlipCount(), "Mind slip count should increase to 2.");
        assertThrows(IllegalStateException.class, () -> game.incrementMindSlipCount(),
                "Increaseing the mind slip count above 2 should throw an IllegalStateException.");
    }

    @Test
    void testGetPlayers() {
        List<Player> players = game.getPlayers();
        assertEquals(2, players.size(), "There should be 2 players in the game.");
        assertTrue(players.contains(player1), "Player list should include Player 1.");
        assertTrue(players.contains(player2), "Player list should include Player 2.");
    }

    @Test
    void testBoardAccess() {
        assertEquals(board, game.getBoard(), "Game board should be accessible and match the one provided.");
    }
}
