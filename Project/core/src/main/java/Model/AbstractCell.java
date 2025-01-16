package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The AbstractCell class is an abstract class represents a single cell on the
 * game board.
 * Each cell keeps track of current players visiting the cell and tokens placed
 * on the cell.
 * NormalCell and TempleCell inherit from this class and define additional
 * behaviors.
 */

public abstract class AbstractCell {

    protected List<Player> players; // List of players currently at the cell
    protected List<Token> tokens; // List of tokens currently at the cell

    /**
     * Constructor to initialize a cell with empty lists of players and tokens.
     */
    public AbstractCell() {
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

    public void updateDeeply(AbstractCell newCell) {
        this.players = newCell.players;
        this.tokens = newCell.tokens;
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

        // A way of removing a player without relying on references.
        List<Player> newPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getId() != player.getId()) {
                newPlayers.add(p);
            }
        }
        this.players = newPlayers;
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
     * Gets all brain tokens
     *
     * @return new list of token references
     */
    public List<Token> getBrains() {
        List<Token> brains = new ArrayList<Token>();

        for (Token token : this.tokens) {
            if (token instanceof BrainFact || token instanceof BrainNote) {
                brains.add(token);
            }
        }
        return brains;
    }

    /**
     * Adds a token to the cell.
     * *
     *
     * * @throws IllegalArgumentException If a token of the same class is already in
     *
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

    public Footstep getFootstep() {
        for (Token token : this.tokens) {
            if (token instanceof Footstep) {
                return (Footstep) token;
            }
        }
        return null;
    }

    public boolean containsFootstep() {
        for (Token token : tokens) {
            if (token.getClass().equals(Footstep.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsBrainFact() {
        for (Token token : tokens) {
            if (token.getClass().equals(BrainFact.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "AbstractCell{" +
                "players=" + players +
                ", tokens=" + tokens +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AbstractCell cell = (AbstractCell) o;

        return Objects.equals(players, cell.players) &&
                Objects.equals(tokens, cell.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players, tokens);
    }
}
