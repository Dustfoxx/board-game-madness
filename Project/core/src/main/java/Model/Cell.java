package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cell class represents a single cell on the game board.
 * Each cell can hold a list of players, tokens, and features that affect the game.
 * Players and tokens can be added or removed from the cell.
 * Features are set from the start. 
 */
public class Cell {
    private List<Player> players;  // List of players currently in the cell
    private List<Token> tokens;  // List of tokens currently in the cell
    private List<Feature> features;  // List of features in the cell

    /**
     * Constructor to initialize a cell with a specified list of features.
     * 
     * @param features The list of features to associate with this cell.
     */
    public Cell(List<Feature> features) {
        this.players = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.features = features;
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
            } else {
                tokens.add(newToken);
            }
        }
    }

    /**
     * Removes a token from the cell.
     * 
     * @param token The token to remove from the cell.
     */
    public void removeToken(Token token) {
        tokens.remove(token);
    }

    /**
     * Gets the list of features associated with this cell.
     * 
     * @return The list of features in the cell.
     */
    public List<Feature> getFeatures() {
        return features;
    }
}
