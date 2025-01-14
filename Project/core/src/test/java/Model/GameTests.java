package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Game.gameStates;

import java.util.ArrayList;
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
        game = new Game(players, new ArrayList<User>(), board, 0);
        game.setGameState(gameStates.ONGOING);
    }

    @Test
    void testGameConstructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Game(null, new ArrayList<User>(), board, 0),
                "Constructor should throw an exception if players list is null.");
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Collections.emptyList(), new ArrayList<User>(), board, 0),
                "Constructor should throw an exception if players list is empty.");
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(player1, player2), new ArrayList<User>(), null, 0),
                "Constructor should throw an exception if board is null.");
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(player1, player2), new ArrayList<User>(), board, -1),
                "Constructor should throw an exception if startingPlayer is incorrect.");
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(player1, player2), new ArrayList<>(), board, 2),
                "Constructor should throw an exception if startingPlayer is not in the players list.");
    }

    @Test
    void testGameInitialization() {
        assertFalse(game.isGameOver(), "Game should not be over initially.");
        assertEquals(player1, game.getCurrentPlayer(), "Starting player should be Player 1.");
        assertEquals(0, game.getCurrentTime(), "Initial game time should be 0.");
        assertEquals(0, game.getRecruitHistory().size(), "Recruit history should be empty.");
        assertEquals(0, game.getMindSlipHistory().size(), "Mind slip history should be empty.");
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
        game.setCurrentPlayer(1);
        assertEquals(player2, game.getCurrentPlayer(), "Current player should be Player 2.");
        game.setCurrentPlayer(0);
        assertEquals(player1, game.getCurrentPlayer(), "Current player should be Player 1.");
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentPlayer(2),
                "Setting the next player to one not in the game should throw IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentPlayer(-1),
                "Setting the next player to null should throw an IllegalArgumentException.");
    }

    @Test
    void testIncrementTime() {
        game.incrementTime();
        assertEquals(1, game.getCurrentTime(), "Game time should increment to 1.");
        game.incrementTime();
        assertEquals(2, game.getCurrentTime(), "Game time should increment to 3.");
    }

    @Test
    void testAddAmountRecruited() {
        game.addAmountRecruited(1);
        assertEquals(1, game.getAmountRecruited(), "Total amount recruited should increase to 1.");
        game.addAmountRecruited(2);
        assertEquals(3, game.getAmountRecruited(), "Total amount recruited should increase to 3.");
        assertThrows(IllegalArgumentException.class, () -> game.addAmountRecruited(-1),
                "Adding a negative number to the amount recruited should throw an IllegalArgumentException.");
    }

    @Test
    void testAddMindSlipEvent() {
        // TODO: Update tests
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
