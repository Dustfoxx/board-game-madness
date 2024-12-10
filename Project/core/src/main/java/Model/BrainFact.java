package Model;

/**
 * The BrainFact class represents a type of Token that contains a timestamp.
 * The timestamp indicates when the recruiter visited the cell containing it.
 */

public class BrainFact extends Token {

    private int timestamp;  // The timestamp of the BrainFact token.

    /**
     * Constructor to initialize a BrainFact with a specified timestamp.
     *
     * @param timestamp The timestamp of the BrainFact token.
     */
    public BrainFact(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the timestamp of this BrainFact token.
     *
     * @return The timestamp of the BrainFact token.
     */
    public int getTimestamp() {
        return this.timestamp;
    }

}
