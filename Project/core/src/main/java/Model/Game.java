package Model;

import java.util.List;

public class Game {

    private boolean gameOver;
    private Player currentPlayer;
    private int currentTime;
    private Board board;
    private int amountRecruited; // Revealed number of recruited people
    private int mindSlipCount; // Number of mindslips used
    private List<Player> players;

    public Game(List<Player> players, Board board, Player startingPlayer) {
        this.gameOver = false;
        this.currentPlayer = startingPlayer;
        this.currentTime = 1;
        this.board = board;
        this.amountRecruited = 0;
        this.mindSlipCount = 0;
        this.players = players;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void setGameOver() {
        if (!gameOver) {
            this.gameOver = true;
        }
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player nextPlayer) {
        this.currentPlayer = nextPlayer;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public void incrementTime() {
        this.currentTime += 1;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getAmountRecruited() {
        return this.amountRecruited;
    }

    public void addAmountRecruited(int newAmountRecruited) {
        this.amountRecruited += newAmountRecruited;
    }

    public int getMindSlipCount() {
        return this.mindSlipCount;
    }

    public void incrementMindSlipCount() {
        this.mindSlipCount += 1;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
