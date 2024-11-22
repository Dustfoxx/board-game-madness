package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cell class is an abstract class represents a single cell on the game board.
 * Each cell keeps track of current players visiting the cell and tokens placed on the cell.
 * NormalCell and TempelCell inherit from this class and define additional behaviors.
 */

public abstract class Cell {

    protected List<Player> players;  // List of players currently at the cell
    protected List<Token> tokens;  // List of tokens currently at the cell

    /**
     * Constructor to initialize a cell with empty lists of players and tokens.
     */
    public Cell() {
        this.players = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    /**
     * Gets the list of players currently in the cell.
     * 
     * @return The list of players in the cell.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Adds a player to the cell.
     * 
     * @param player The player to add to the cell.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from the cell.
     * 
     * @param player The player to remove from the cell.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Gets the list of tokens currently in the cell.
     * 
     * @return The list of tokens in the cell.
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Adds a token to the cell. 
     * 
     * @param newToken The token to add to the cell.
     * @throws IllegalArgumentException If a token of the same class is already in the cell.
     */
    public void addToken(Token newToken) {
        for (Token token : tokens) {
            if (token.getClass().equals(newToken.getClass())) {
                throw new IllegalArgumentException("There is already a " + newToken.getClass() + " in this cell");
            }
        }
        tokens.add(newToken);
    }

    /**
     * Removes a token from the cell.
     * 
     * @param token The token to remove from the cell.
     */
    public void removeToken(Token token) {
        tokens.remove(token);
    }
}
