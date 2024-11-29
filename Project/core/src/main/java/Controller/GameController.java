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
    private Model.Player playerTurnOrder[] = new Player[6];
    private Recruiter recruiter = null;

    /**
     * The main gameController. Keeps an eye on victory conditions and which players
     * are next in queue to play. This is the constructor. Currently does not track
     * when
     * recruits and similar have been added timewise.
     * 
     * @param newGame A game model object meant to describe everything for view
     */

    public GameController(Game newGame) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
                               // controlled

        gameState = newGame;
        List<Player> gamePlayers = gameState.getPlayers();
        List<RougeAgent> agents = new ArrayList<RougeAgent>();

        boolean oneRecruiter = true;
        for (int i = 0; i < gamePlayers.size(); i++) {
            Player currPlayer = gamePlayers.get(i);
            if (currPlayer instanceof Recruiter) {
                recruiter = (Recruiter) currPlayer;
                gameState.setCurrentPlayer(recruiter);
                if (!oneRecruiter) {
                    throw new IllegalStateException("More than one recruiter");
                }
                oneRecruiter = false;
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

        for (int i = 0; i < playerTurnOrder.length; i++) {
            switch (i) {
                case 0:
                    playerTurnOrder[i] = recruiter;
                    break;
                case 3:
                    playerTurnOrder[i] = recruiter;
                    break;
                default:
                    playerTurnOrder[i] = agents.get(agentIterator);
                    agentIterator++;
                    if (agentIterator >= agents.size()) {
                        agentIterator = 0;
                    }
            }
        }
    }

    /**
     * Gets called when a player has completed their actions.
     * Decides new player and increments timer accordingly.
     */
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
            if (recruiter.getAmountRecruited() > 0) {
                gameState.addAmountRecruited(recruiter.getAmountRecruited());
            }
            if (gameState.getAmountRecruited() >= 9) {
                // RECRUITER WIN GAME
                gameState.setGameOver();
            }
        }

        activePlayer++;
        gameState.setCurrentPlayer(playerTurnOrder[activePlayer % playerTurnOrder.length]);
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            recruiterTurn = true;
        }
    }

}