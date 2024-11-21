package Controller;

import Model.Player;
import Model.Recruiter;
import Model.RougeAgent;
import Model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    // Check for new turn
    // Think this would be better as view calling it to move from one turn to the
    // next as needed
    // Update recruits if odd turn
    // Activate player
    // Check win

    private boolean recruiterTurn = true;
    private Game gameState;
    private int activePlayer = 0;
    private Model.Player players[] = new Player[6];
    private Recruiter recruiter = null;
    List<RougeAgent> agents = new ArrayList<RougeAgent>();

    public GameController(Game newGame) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
                               // controlled

        gameState = newGame;
        List<Player> gamePlayers = gameState.getPlayers();

        for (int i = 0; i < gamePlayers.size(); i++) {
            Player currPlayer = gamePlayers.get(i);
            if (currPlayer.getClass() == Recruiter.class) {
                recruiter = (Recruiter) currPlayer;
            } else {
                agents.add((RougeAgent) currPlayer);
            }
        }

        if (recruiter == null) {
            throw new IllegalStateException("Recruiter player not found");
        }
        if (agents.isEmpty()) {
            throw new IllegalStateException("No agents found");
        }

        for (int i = 0; i < players.length; i++) {
            switch (i) {
                case 0:
                    players[i] = recruiter;
                    break;
                case 3:
                    players[i] = recruiter;
                    break;
                default:
                    players[i] = agents.get(agentIterator);
                    agentIterator++;
                    if (agentIterator >= agents.size()) {
                        agentIterator = 0;
                    }
            }
        }
    }

    public void newTurn() {
        if (recruiterTurn) {
            gameState.incrementTime();
            if (gameState.getCurrentTime() >= 8) {// IDK how many turns were max
                // RECRUITER WIN
                gameState.setGameOver();
                // Should who won exist here or in model?
                // I think model
            }
            recruiterTurn = false;
        }
        if (gameState.getCurrentTime() % 2 == 1) { // Need handling for first turn but I want a better idea of how that
                                                   // would be
            // structured
            gameState.addAmountRecruited(recruiter.getAmountRecruited());
            if (gameState.getAmountRecruited() >= 9) {
                // RECRUITER WIN GAME
                gameState.setGameOver();
            }
        }

        activePlayer++;
        gameState.setCurrentPlayer(players[activePlayer % players.length]);
    }

}
