package Model;

import java.util.Objects;

public class Step extends Token {
    public int timestamp;
    private boolean isVisible;

    public Step(int timestamp) {
        super();
        this.isVisible = false;
        this.timestamp = timestamp;
    }

    /**
     * Sets the visibility of the recruiter
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
        return "Step{timestamp=" + timestamp + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        Step step = (Step) o;
        return timestamp == step.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timestamp);
    }

}
