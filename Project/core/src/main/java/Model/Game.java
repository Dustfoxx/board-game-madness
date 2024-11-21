package Model;

import java.util.List;

/**
 * The Game class is the top-level class in the game model hierarchy.
 * It oversees and manages the overall game state.
 */
public class Game {

    private boolean gameOver; // Indicates if the game is over
    private Player currentPlayer; // The player whose turn it is
    private int currentTime; // The current time in the game
    private Board board; // The game board
    private int amountRecruited; // The number of recruited players revealed
    private int mindSlipCount; // The number of mindslips used
    private List<Player> players; // The list of players in the game

    /**
     * Constructor to initialize a new game with a list of players, a game board,
     * and the starting player.
     * 
     * @param players        The list of players participating in the game.
     * @param board          The game board.
     * @param startingPlayer The player who will start the game.
     */
    public Game(List<Player> players, Board board, Player startingPlayer) {
        this.gameOver = false;
        this.currentPlayer = startingPlayer;
        this.currentTime = 1;
        this.board = board;
        this.amountRecruited = 0;
        this.mindSlipCount = 0;
        this.players = players;
    }

    /**
     * Gets the current game over status.
     * 
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Sets the game as over. If the game is not already over, it will be marked as
     * over.
     */
    public void setGameOver() {
        if (!gameOver) {
            this.gameOver = true;
        }
    }

    /**
     * Gets the player whose turn it is currently.
     * 
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Sets the next player as the current player.
     * 
     * @param nextPlayer The player to become the current player.
     */
    public void setCurrentPlayer(Player nextPlayer) {
        this.currentPlayer = nextPlayer;
    }

    /**
     * Gets the current time in the game.
     * 
     * @return The current time.
     */
    public int getCurrentTime() {
        return this.currentTime;
    }

    /**
     * Increments the current time by 1.
     */
    public void incrementTime() {
        this.currentTime += 1;
    }

    /**
     * Gets the game board.
     * 
     * @return The game board.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets the current number of recruited players revealed.
     * 
     * @return The amount of recruited players revealed.
     */
    public int getAmountRecruited() {
        return this.amountRecruited;
    }

    /**
     * Adds a specified amount to the number of recruited players.
     * 
     * @param newAmountRecruited The number of recruited players to add.
     */
    public void addAmountRecruited(int newAmountRecruited) {
        this.amountRecruited += newAmountRecruited;
    }

    /**
     * Gets the current number of mindslips used in the game.
     * 
     * @return The number of mindslips used.
     */
    public int getMindSlipCount() {
        return this.mindSlipCount;
    }

    /**
     * Increments the mind slip count by 1.
     */
    public void incrementMindSlipCount() {
        this.mindSlipCount += 1;
    }

    /**
     * Gets the list of players in the game.
     * 
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return this.players;
    }
}
