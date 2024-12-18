package Model;

import java.util.Objects;

/**
 * The abstract Token class represents various types of tokens that can be
 * placed on the game board.
 * Footsteps, BrainNotes, or BrainFacts are tokens that inherit from this class.
 */
public abstract class Token {

    private boolean isVisible;

    public Token() {
        this.isVisible = true;
    }

    /**
     * Sets the visibility of the player
     * 
     * @param visibility true if visible, false if not
     */
    public void setVisibility(boolean visibility) {
        this.isVisible = visibility;
    }

    /**
     * Gets the visibilityvalue of a step
     * 
     * @return the visibility of the player
     */
    public boolean getVisibility() {
        return this.isVisible;
    }

    @Override
    public String toString() {
        return "Token{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

}
