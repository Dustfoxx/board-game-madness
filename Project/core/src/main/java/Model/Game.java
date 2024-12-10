package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class is the top-level class in the game model hierarchy.
 * It oversees and manages the overall game state.
 */
public class Game {

    private boolean gameOver; // Indicates if the game is over
    private Player currentPlayer; // The player whose turn it is
    private int currentTime; // The current time in the game
    private int maxTime; // The time at which the game ends
    private int maxRecruits; // The amount recruits needed for recruiter to win
    private Board board; // The game board
    private List<int[]> recruitHistory; // Tracks history of revealed recruits as (time, amount) pairs.
    private List<Integer> mindSlipHistory; // Tracks history og when mind slips were used
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
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Players list cannot be null or empty.");
        }
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        if (startingPlayer == null) {
            throw new IllegalArgumentException("Starting player cannot be null.");
        }
        if (!players.contains(startingPlayer)) {
            throw new IllegalArgumentException("Starting player must be in the players list.");
        }
        this.gameOver = false;
        this.currentPlayer = startingPlayer;
        this.currentTime = 1;
        this.maxTime = 14;
        this.maxRecruits = 9;
        this.board = board;
        this.recruitHistory = new ArrayList<>();
        this.mindSlipHistory = new ArrayList<>();
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
        } else {
            throw new IllegalStateException("The game is already over.");
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
        if (!players.contains(nextPlayer)) {
            throw new IllegalArgumentException("Player is not part of the game.");
        }
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
     * Gets the total amount of recruits reveald so far.
     * 
     * @return The total amount of recruits reveald so far.
     */
    public int getAmountRecruited() {
        int count = 0;
        for (int[] recruits : this.recruitHistory) {
            count += recruits[1];
        }
        return count;
    }

    /**
     * Gets the history of revealed recruits.
     * 
     * @return A list of revealed recruites as (time, amount) pairs.
     */
    public List<int[]> getRecruitHistory() {
        return new ArrayList<>(this.recruitHistory); //Returns a copy for safety
    }

    /**
     * Adds the number of recruits revealed during the current round.
     * 
     * @param amount The number of recruits revealed during current round.
     */
    public void addAmountRecruited(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be greater than 0.");
        }
        this.recruitHistory.add(new int[] { currentTime, amount });
    }

    /**
     * Gets the mind slip history.
     * 
     * @return A list of times when mind slips were used.
     */
    public List<Integer> getMindSlipHistory() {
        return new ArrayList<>(this.mindSlipHistory); //Returns a copy for safety
    }

    /**
     * Adds a mind slip event to the mind slip history based on current time.
     * 
     */
    public void addMindSlipEvent() {

        if (this.mindSlipHistory.size() >= 2) {
            throw new IllegalStateException("Mind slip can only be used twice per game.");
        }
        if (this.mindSlipHistory.contains(currentTime)) {
            throw new IllegalStateException("Mind slip can only be used once per round.");
        }
        this.mindSlipHistory.add(currentTime);
    }

    /**
     * Gets the list of players in the game.
     * 
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    public Recruiter getRecruiter() {
        for (Player player : players) {
            if (player.getClass().equals(Recruiter.class)) {
                return (Recruiter) player;
            }
        }
        throw new IllegalStateException("No recruiter found in the game");
    }
}
