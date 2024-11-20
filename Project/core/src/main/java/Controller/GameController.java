package Controller;

public class GameController {
    // Check for new turn
    // Think this would be better as view calling it to move from one turn to the
    // next as needed
    // Update recruits if odd turn
    // Activate player
    // Check win

    private int turnNumber = 0;
    private int recruits = 0;
    private boolean recruiterTurn = true;
    private int activePlayer = 0;
    private int[] players = { 0, 0, 0, 0, 0, 0 }; // TODO: Change to playertype

    public GameController(int recruiter, int[] agents) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
                               // controlled

        for (int i = 0; i < players.length; i++) {
            switch (i) {
                case 0:
                    players[i] = recruiter;
                    break;
                case 3:
                    players[i] = recruiter;
                    break;
                default:
                    players[i] = agents[agentIterator];
                    agentIterator++;
                    if (agentIterator >= agents.length) {
                        agentIterator = 0;
                    }
            }
        }
    }

    public void newTurn() {
        if (recruiterTurn) {
            turnNumber++;
            if (turnNumber >= 8) {// IDK how many turns were max
                // RECRUITER WIN
            }
            recruiterTurn = false;
        }
        if (turnNumber % 2 == 1) { // Need handling for first turn but I want a better idea of how that would be
                                   // structured
            // recruits += player[0].getRecruits();
            if (recruits >= 9) {
                // RECRUITER WIN GAME
            }
        }

        activePlayer++;
        // Activate next player. Unsure what this looks like but use function to
        // activate like this:
        // players[activePlayer % players.length].activatePlayer;
        // This should loop through the player list and activate them as they play.
    }

}
