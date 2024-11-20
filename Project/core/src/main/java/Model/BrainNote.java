package Model;

public class BrainNote extends Token {
    private String note;

    public BrainNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}