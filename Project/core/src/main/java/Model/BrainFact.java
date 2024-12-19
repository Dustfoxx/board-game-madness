package Model;

import java.util.Objects;

/**
 * The BrainFact class represents a type of Token that contains a timestamp.
 * The timestamp indicates when the recruiter visited the cell containing it.
 */

public class BrainFact extends Token {

    private int timestamp; // The timestamp of the BrainFact token.

    /**
     * Constructor to initialize a BrainFact with a specified timestamp.
     * 
     * @param timestamp The timestamp of the BrainFact token.
     */
    public BrainFact(int timestamp) {
        super();
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

    @Override
    public String toString() {
        return "BrainFact{timestamp=" + timestamp + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        BrainFact brain = (BrainFact) o;
        return timestamp == brain.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timestamp);
    }

}
