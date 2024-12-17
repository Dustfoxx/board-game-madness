package Model;

import java.util.Arrays;

/**
 * The NormalCell class represents a cell or normal type on the game board.
 * Each normal cell can hold a list of players, tokens, and features.
 */
public class NormalCell extends AbstractCell {

    private Feature[] features; // Array of features in the cell

    /**
     * Constructor to initialize a normal cell with a specified array of features.
     * 
     * @param features The array of features to associate with this cell. The array
     *                 must contain exactly 2 features of different type.
     * @throws IllegalArgumentException If the features array does not contain
     *                                  exactly 2 elements or if the features are of
     *                                  the same type.
     */
    public NormalCell(Feature[] features) {
        super();
        if (features.length != 2) {
            throw new IllegalArgumentException(
                    "The features array must contain exactly 2 elements, but it contains " + features.length);
        }

        // Check if both features are of the same type
        if (features[0] == features[1]) {
            throw new IllegalArgumentException("The features must be of different types, but both are of type "
                    + features[0].getClass().getName());
        }

        this.features = features;
    }

    /**
     * Gets the list of features associated with this cell.
     * 
     * @return The list of features in the cell.
     */
    public Feature[] getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "NormalCell{" +
                "features=" + Arrays.toString(features) +
                ", players=" + players +
                ", tokens=" + tokens +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        NormalCell cell = (NormalCell) o;
        return Arrays.deepEquals(features, cell.features);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(features);
        return result;
    }
}
