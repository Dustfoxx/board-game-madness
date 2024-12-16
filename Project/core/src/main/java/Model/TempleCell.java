package Model;

/**
 * The TempleCell class represents a cell of temple type on the game board.
 * Each temple cell can hold a list of players and tokens of BrainNote type.
 */
public class TempleCell extends AbstractCell {

    /**
     * Constructor to initialize a temple cell.
     */
    public TempleCell() {
        super();
    }

    /**
     * Adds a token to the cell and ensures it is of type BrainNote.
     * 
     * @param newToken The token to add to the cell.
     * @throws IllegalArgumentException If the token is not of type BrainNote or if
     *                                  a BrainNote already exists.
     */
    @Override
    public void addToken(Token newToken) {
        if (newToken instanceof BrainNote || newToken instanceof Step) {
            super.addToken(newToken);
        } else {
            throw new IllegalArgumentException("Only BrainNote tokens can be added to a TempelCell.");
        }
    }

    @Override
    public String toString() {
        return "TempleCell{" +
                "players=" + players +
                ", tokens=" + tokens +
                '}';
    }
}
