package View.screen.GameScreenComponents;

public class MockedGame {
    private int currentTurn;
    private int currentTime;

    public MockedGame() {
        this.currentTurn = 0;
        this.currentTime = 0;
    }

    public int getTurn() {
        return currentTurn;
    }

    public void setTurn(int turn) {
        this.currentTurn = turn;
    }

    public int getTime() {
        return currentTime;
    }

    public void setTime(int time) {
        this.currentTime = time;
    }
}