package Controller;

import Model.*;
import Model.Game.gameStates;
import Model.Recruiter.RecruiterType;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    // Check for new turn
    // Think this would be better as view calling it to move from one turn to the
    // next as needed
    // Update recruits if odd turn
    // Activate player
    // Check win

    private Game gameState;
    private int activePlayer = 0;
    private Model.Player playerTurnOrder[] = new Player[6];
    private Recruiter recruiter = null;
    private ActionController actionController;
    private final int playerPieceAmount = 5;

    public enum Actions {
        MOVE,
        ASK,
        REVEAL,
        CAPTURE,
        MINDSLIP,
        RECRUITERCHOICE
    }

    /**
     * The main gameController. Keeps an eye on victory conditions and which players
     * are next in queue to play. This is the constructor. Currently does not track
     * when
     * recruits and similar have been added timewise.
     */

    public GameController(Csv boardCsv, ArrayList<String> names) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
                               // controlled
        gameState = initializeGame(boardCsv, names);
        List<Player> gamePlayers = gameState.getPlayers();
        List<RougeAgent> agents = new ArrayList<RougeAgent>();

        this.actionController = new ActionController();

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

    private Game initializeGame(Csv boardCsv, ArrayList<String> names) {
        int playerAmount = names.size();

        if (playerAmount <= 1) {
            throw new IllegalArgumentException("Must be more than 1 player");
        }
        List<Player> players = new ArrayList<Player>();
        List<User> users = new ArrayList<User>();
        Feature[] recruiterFeatures = new Feature[] { Feature.FOUNTAIN, Feature.BILLBOARD, Feature.BUS };

        for (int i = 0; i < playerPieceAmount; i++) {
            players.add(i == 0 ? new Recruiter(i, "Recruiter", recruiterFeatures) : new RougeAgent(i, "Agent" + i));
        }

        for (int i = 0; i < playerAmount; i++) {
            users.add(new User(i, names.get(i))); // TODO: integrate with multiplayer id
        }

        List<Player> rogueAgents = players.subList(1, players.size());

        users.get(0).addPlayerPiece(players.get(0));

        int agentIterator = 1;
        for (int i = 0; i < rogueAgents.size(); i++) { // TODO: Generalize as this is also done for players
            users.get(agentIterator).addPlayerPiece(rogueAgents.get(i));
            agentIterator++;
            if (agentIterator >= users.size()) {
                agentIterator = 1;
            }
        }

        Board board = new Board(boardCsv);
        return new Game(players, users, board, players.get(0));
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
        gameState.setMovementAvailability(true);
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            gameState.incrementTime();
            if (gameState.getCurrentTime() > 4) {
                gameState.addAmountRecruited(recruiter.getAmountRecruited());
                recruiter.resetAmountRecruited();
                gameState.setCurrentPlayer(gameState.getPlayers().get(1)); // Gets first rogue agent and sets them as
                                                                           // next player
            }
        } else {
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

    }

    private void ongoingLogic() {
        gameState.setMovementAvailability(true);
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            gameState.incrementTime();
        }
        if (gameState.getCurrentTime() % 2 == 1) {
            gameState.addAmountRecruited(recruiter.getAmountRecruited());
            recruiter.resetAmountRecruited();
            if (gameState.getAmountRecruited() >= gameState.getMaxRecruits()) {
                // RECRUITER WIN GAME
                gameState.setGameOver();
            }
        }

        activePlayer++;
        gameState.setCurrentPlayer(playerTurnOrder[activePlayer % playerTurnOrder.length]);
        if (gameState.getCurrentPlayer() instanceof RougeAgent) {
            gameState.setActionAvailability(true);
        } else if (gameState.getCurrentTime() >= gameState.getMaxTime()) {
            // RECRUITER WIN
            gameState.setGameOver();
            // TODO: Should who won exist here or in model?
            // I think model
        }
    }

    public boolean actionHandler(Actions action) {
        return actionHandler(action, new Object[] {});
    }

    public boolean actionHandler(Actions action, Object[] additionalInfo) {
        boolean returnValue = true;
        switch (action) {
            case ASK:
                actionController.ask((Feature) additionalInfo[0], gameState.getRecruiter(), gameState.getBoard());
                gameState.setActionAvailability(false); // TODO: add so that this makes sure action was valid
                break;
            case REVEAL:
                // TODO: int[] playerCoord =
                // gameState.getBoard().getPlayerCoord(gameState.getCurrentPlayer());
                // actionController.reveal(gameState.getBoard().getCell(playerCoord[0],
                // playerCoord[1]).getFootstep(),
                // gameState.getBoard(),
                // gameState.getBoard().getPlayerCoord(gameState.getCurrentPlayer()),
                // gameState.getRecruiter().getWalkedPath());
                gameState.setActionAvailability(false); // TODO: add so that this makes sure action was valid
                break;
            case CAPTURE:
                returnValue = actionController.capture(gameState.getCurrentPlayer(), gameState.getRecruiter(),
                        gameState.getBoard());
                gameState.setActionAvailability(false); // TODO: add so that this makes sure action was valid

                break;
            case MINDSLIP:

                break;
            case MOVE:
                if (gameState.isMovementAvailable()) {
                    int row = (int) additionalInfo[0];
                    int col = (int) additionalInfo[1];
                    actionController.movePlayer(gameState.getCurrentPlayer(), gameState.getBoard(),
                            new int[] { row, col });
                    gameState.setMovementAvailability(false);
                }
                break;
            case RECRUITERCHOICE:
                getGame().getRecruiter().setRecruiterType((RecruiterType) additionalInfo[0]);
        }

        if (action != Actions.MOVE) {
            gameState.setActionAvailability(false); // TODO: add so that this makes sure action was valid
        }

        if (!gameState.isActionAvailable() && !gameState.isMovementAvailable()) {
            newTurn();
        }
        return returnValue;
    }

}
