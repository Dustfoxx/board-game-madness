package Model;

/**
 * The BrainNote class represents a type of Token that contains a note.
 * The note allows the rouge agents to write down thoughts about the cell containing it. 
 */
public class BrainNote extends Token {

    private String note;  // The note of this BrainNote token

    /**
     * Constructor to initialize a BrainNote with a specified note.
     * 
     * @param note The note of the BrainNote token.
     */
    public BrainNote(String note) {
        this.note = note;
    }

    /**
     * Gets the note of this BrainNote token.
     * 
     * @return The note of the BrainNote token.
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets a new note for this BrainNote token.
     * 
     * @param note The new note of the BrainNote token.
     */
    public void setNote(String note) {
        this.note = note;
    }
}
