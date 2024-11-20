package Model;

public class BrainFact extends Token {
    private int timestamp;

    public BrainFact(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return this.timestamp;
    }
}
