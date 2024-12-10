package Controller;

import Model.*;
import Model.Game.gameStates;

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
     * @param playerAmount The number of players this specific game should have
     */

    public GameController(int playerAmount, Csv boardCsv) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
                               // controlled
        gameState = initializeGame(playerAmount, boardCsv);
        List<Player> gamePlayers = gameState.getPlayers();
        List<RougeAgent> agents = new ArrayList<RougeAgent>();

        boolean oneRecruiter = true;
        for (Player currPlayer : gamePlayers) {
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

    private Game initializeGame(int playerAmount, Csv boardCsv) {
        if (playerAmount <= 1) {
            throw new IllegalArgumentException("Must be more than 1 player");
        }
        List<Player> players = new ArrayList<Player>();
        Feature[] recruiterFeatures = new Feature[] { Feature.FOUNTAIN, Feature.BILLBOARD, Feature.BUS };

        for (int i = 0; i < playerAmount; i++) {
            players.add(i == 0 ? new Recruiter(i, "recruiter", recruiterFeatures) : new RougeAgent(i));
        }

        Board board = new Board(boardCsv);
        return new Game(players, board, players.get(0));
    }

    public Game getGame() {
        return this.gameState;
    }

    /**
     * Gets called when a player has completed their actions.
     * Decides new player and increments timer accordingly.
     */
    public void newTurn() {
        switch (gameState.getGameState()) {
            case PREGAME:
                preGameLogic();
                break;
            case ONGOING:
                ongoingLogic();
                break;
            case ENDGAME:
                // Save stats?
                break;

            default:
                break;
        }

    }

    private void preGameLogic() {
        if (recruiterTurn) {
            gameState.incrementTime();
            if (gameState.getCurrentTime() > 5) {
                recruiterTurn = false;
                gameState.setCurrentPlayer(gameState.getPlayers().get(1)); // Gets first rogue agent and sets them as
                                                                           // next player
            }
        }

        List<Player> players = gameState.getPlayers();
        int currentIndex = players.indexOf(gameState.getCurrentPlayer());
        // If we are at the end of players, set game to started
        if (currentIndex == players.size() - 1) {
            gameState.setCurrentPlayer(recruiter);
            gameState.setGameState(gameStates.ONGOING);
        } else {
            // Set player to next rogue agent so they can place
            gameState.setCurrentPlayer(players.get(currentIndex + 1));
        }
    }

    private void ongoingLogic() {
        if (recruiterTurn) {
            gameState.incrementTime();
            if (gameState.getCurrentTime() >= 14) {// IDK how many turns were max
                // RECRUITER WIN
                gameState.setGameOver();
                // Should who won exist here or in model?
                // I think model
            }
            recruiterTurn = false;
        }
        if (gameState.getCurrentTime() % 2 == 1) { // Need handling for first turn but I want a better idea of
                                                   // how that
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
