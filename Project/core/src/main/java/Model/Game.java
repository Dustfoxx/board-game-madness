package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Controller.CheckAction;

/**
 * The Game class is the top-level class in the game model hierarchy.
 * It oversees and manages the overall game state.
 */
public class Game {

    public enum gameStates {
        PREGAME,
        ONGOING,
        ENDGAME
    } // Enum defining different gameStates

    private String winner;

    private gameStates gameState; // Indicates which state the game is in
    private boolean gameOver; // Indicates if the game is over
    private Player currentPlayer; // The player whose turn it is
    private int currentTime; // The current time in the game
    private int maxTime; // The time at which the game ends
    private int maxRecruits; // The amount recruits needed for recruiter to win
    private Board board; // The game board
    private List<int[]> recruitHistory; // Tracks history of revealed recruits as (time, amount) pairs.
    private List<Integer> mindSlipHistory; // Tracks history og when mind slips were used
    private List<Player> players; // The list of players in the game
    private List<User> users; // The list of users connected to the game
    private boolean isMovementAvailable;
    private boolean isActionAvailable;
    private MutableBoolean[][] validityMask;
    private CheckAction checkAction;

    /**
     * Constructor to initialize a new game with a list of players, a game board,
     * and the starting player.
     *
     * @param players        The list of players participating in the game.
     * @param board          The game board.
     * @param startingPlayer The player who will start the game.
     */
    public Game(List<Player> players, List<User> users, Board board, Player startingPlayer) {
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
        this.currentTime = 0; // Starts at zero so it can increment as recruiter chooses start
        this.maxTime = 14;
        this.maxRecruits = 9;
        this.board = board;
        this.recruitHistory = new ArrayList<>();
        this.mindSlipHistory = new ArrayList<>();
        this.players = players;
        this.users = users;
        this.gameState = gameStates.PREGAME;
        this.isActionAvailable = false;
        this.isMovementAvailable = true;
        this.checkAction = new CheckAction();
        this.validityMask = checkAction.createUniformMask(board, true);
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
            if (this.currentTime >= this.maxTime || this.getAmountRecruited() >= this.maxRecruits) {
                winner = "Recruiter";
            } else {
                winner = "Rogue Agents";
            }
            setGameState(gameStates.ENDGAME);
        } else {
            throw new IllegalStateException("The game is already over.");
        }
    }

    /**
     * Gets the winning team
     * 
     * @return string name of the winning team
     */
    public String getWinner() {
        return this.winner;
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
     * getter for max time, limit of rounds
     *
     * @return max round count
     */
    public int getMaxTime() {
        return this.maxTime;
    }

    /**
     * getter for max recruits, the limit where recruiter wins
     *
     * @return max recruits as an int
     */
    public int getMaxRecruits() {
        return this.maxRecruits;
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
        return new ArrayList<>(this.recruitHistory); // Returns a copy for safety
    }

    /**
     * Gets the amount recruited at a specific timestamp as a (time, amount) tuple.
     *
     * @param time the timestamp we wish to get
     * @return the tuple at given time, null if not found
     */
    public int[] getRecruitAtTime(int time) {
        for (int[] tuple : this.recruitHistory) {
            if (tuple[0] == time) {
                return tuple;
            }
        }
        return null;
    }

    /**
     * Adds the number of recruits revealed during the current round.
     *
     * @param amount The number of recruits revealed during current round.
     */
    public void addAmountRecruited(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Recruits added cannot be below 0.");
        }
        this.recruitHistory.add(new int[] { currentTime, amount });
    }

    /**
     * Gets the mind slip history.
     *
     * @return A list of times when mind slips were used.
     */
    public List<Integer> getMindSlipHistory() {
        return new ArrayList<>(this.mindSlipHistory); // Returns a copy for safety
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

    /**
     * Gets the list of users in the game.
     *
     * @return The list of users.
     */
    public List<User> getUsers() {
        return this.users;
    }

    /**
     * Gets the current gamestate
     *
     * @return The corresponding enum for the current state of the game
     */
    public gameStates getGameState() {
        return this.gameState;
    }

    /**
     * Sets the gameState
     *
     * @param newState the new state of the game
     */
    public void setGameState(gameStates newState) {
        switch (newState) {
            case PREGAME:
                if (this.gameState == gameStates.ONGOING) {
                    throw new IllegalStateException("Cannot go from ONGOING to PREGAME states of game");
                }
                break;
            case ONGOING:
                if (this.gameState == gameStates.ENDGAME) {
                    throw new IllegalStateException("Cannot go from ENDGAME to ONGOING states of game");
                }
                break;
            case ENDGAME:
                if (this.gameState == gameStates.PREGAME) {
                    throw new IllegalStateException("Cannot go from PREGAME to ENDGAME states of game");
                }
                break;
        }
        this.gameState = newState;
    }

    /**
     * Getter for movement variable
     *
     * @return if player can move or not
     */
    public boolean isMovementAvailable() {
        return this.isMovementAvailable;
    }

    /**
     * Getter for action variable
     *
     * @return if player can use an action still or not
     */
    public boolean isActionAvailable() {
        return this.isActionAvailable;
    }

    /**
     * sets the movement variable
     *
     * @param value true if player should be able to move and false if they have
     *              just moved
     */
    public void setMovementAvailability(boolean value) {
        this.isMovementAvailable = value;
    }

    /**
     * Sets the action variable. Different from what they are allowed to do, this is
     * meant to indicate if they have used an action this turn or not
     *
     * @param value true if player can make an action, false if they have used one.
     */
    public void setActionAvailability(boolean value) {
        this.isActionAvailable = value;
    }

    /**
     * Sets the validitymask for the board
     */
    public void setValidityMask(MutableBoolean[][] mask) {
        int[] boardDims = board.getDims();
        if (boardDims[0] != mask.length || boardDims[1] != mask[0].length) {
            throw new IllegalArgumentException("Size mismatch between mask and board.");
        } else {
            for (int i = 0; i < boardDims[0]; i++) {
                for (int j = 0; j < boardDims[1]; j++) {
                    validityMask[i][j].setBoolean(mask[i][j].getBoolean());
                }
            }
        }
    }

    /**
     * Returns the validitymask for the board
     * 
     * @return matrix of booleans. True where player can move and false where they
     *         cannc
     */
    public MutableBoolean[][] getValidityMask() {
        return this.validityMask;
    }

    /**
     * Gets the recruiter in the game.
     *
     * @return The recruiter
     */
    public Recruiter getRecruiter() {
        for (Player player : players) {
            if (player.getClass().equals(Recruiter.class)) {
                return (Recruiter) player;
            }
        }
        throw new IllegalStateException("No recruiter found in the game");
    }

    /**
     * Gets the cell which the current player is at.
     * 
     * @return The cell which the current player is at, otherwise null.
     */
    public AbstractCell getCurrentPlayerCell() {
        int[] position = board.getPlayerCoord(currentPlayer);
        if (position != null) {
            return board.getCell(position[0], position[1]);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameState=" + gameState +
                ", gameOver=" + gameOver +
                ", currentPlayer=" + currentPlayer +
                ", currentTime=" + currentTime +
                ", maxTime=" + maxTime +
                ", maxRecruits=" + maxRecruits +
                ", board=" + board +
                ", recruitHistory=" + recruitHistory +
                ", mindSlipHistory=" + mindSlipHistory +
                ", players=" + players +
                ", users=" + users +
                ", isMovementAvailable=" + isMovementAvailable +
                ", isActionAvailable=" + isActionAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Game game = (Game) o;
        return gameOver == game.gameOver &&
                currentTime == game.currentTime &&
                maxTime == game.maxTime &&
                maxRecruits == game.maxRecruits &&
                isMovementAvailable == game.isMovementAvailable &&
                isActionAvailable == game.isActionAvailable &&
                gameState == game.gameState &&
                Objects.equals(currentPlayer, game.currentPlayer) &&
                Objects.equals(board, game.board) &&
                Objects.equals(recruitHistory, game.recruitHistory) &&
                Objects.equals(mindSlipHistory, game.mindSlipHistory) &&
                Objects.equals(players, game.players) &&
                Objects.equals(users, game.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState, gameOver, currentPlayer, currentTime, maxTime, maxRecruits,
                board, recruitHistory, mindSlipHistory, players,
                users, isMovementAvailable, isActionAvailable);
    }

}
