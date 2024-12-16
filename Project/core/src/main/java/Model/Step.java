package Model;

import java.util.Objects;

public class Step extends Token {
    public int timestamp;

    public Step(int timestamp) {
        this.timestamp = timestamp;
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
