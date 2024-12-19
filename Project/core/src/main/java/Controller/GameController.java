package Controller;

import Model.*;
import Model.Game.gameStates;
import Model.Recruiter.RecruiterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private CheckAction checkAction;
    private final int playerPieceAmount = 5;

    public enum Actions {
        MOVE,
        ASK,
        REVEAL,
        CAPTURE,
        MINDSLIP,
        RECRUITERCHOICE,
        BRAINNOTE
    }

    /**
     * The main gameController. Keeps an eye on victory conditions and which players
     * are next in queue to play. This is the constructor mainly inteded for clients.
     */
    public GameController(Game gameState) {
        initController(gameState);
    }

    /**
     * The main gameController. Keeps an eye on victory conditions and which players
     * are next in queue to play. This is the constructor intended for the host.
     */

    public GameController(Csv boardCsv, ArrayList<User> users) {
        Game game = initializeGame(boardCsv, users);
        initController(game);
    }

    private void initController(Game game) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 0; // This is in case there are less than four agents. Every unit will still be
        // controlled
        this.gameState = game;
        List<Player> gamePlayers = gameState.getPlayers();
        List<RougeAgent> agents = new ArrayList<>();

        this.actionController = new ActionController();
        this.checkAction = new CheckAction();

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

    private Game initializeGame(Csv boardCsv, ArrayList<User> users) {
        int userAmount = users.size();

        if (userAmount <= 1) {
            throw new IllegalArgumentException("Must be more than 1 user");
        }

        List<Player> players = new ArrayList<>();

        // Randomly select three unique features
        List<Feature> allFeaturesList = new ArrayList<>(Arrays.asList(Feature.values()));
        Collections.shuffle(allFeaturesList);
        Feature[] recruiterFeatures = new Feature[3];
        for (int i = 0; i < 3; i++) {
            recruiterFeatures[i] = allFeaturesList.get(i);
        }

        Recruiter recruiter = new Recruiter(0, "Recruiter", recruiterFeatures);

        players.add(recruiter);
        for (int i = 1; i < playerPieceAmount; i++) {
            players.add(new RougeAgent(i, "Agent" + i));
        }

        users.get(0).addPlayerPiece(recruiter);

        List<Player> rogueAgents = players.subList(1, players.size());
        distributePlayers(rogueAgents, users, 1);

        Board board = new Board(boardCsv);
        return new Game(players, users, board, recruiter);
    }

    private void distributePlayers(List<Player> players, List<User> users, int startUserIndex) {
        int userIterator = startUserIndex; // TODO: integrate with multiplayer id
        for (Player player : players) {
            users.get(userIterator).addPlayerPiece(player);
            userIterator++;
            if (userIterator >= users.size()) {
                userIterator = startUserIndex;
            }
        }
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

                break;

            default:
                break;
        }

        gameState.setValidityMask(checkAction.getValidMoves(gameState.getCurrentPlayer(), gameState.getBoard()));
        if (checkAction.isMaskEmpty(gameState.getValidityMask())) {
            gameState.setGameOver();
        }

    }

    private void preGameLogic() {
        gameState.setMovementAvailability(true);
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            gameState.incrementTime();
            if (gameState.getCurrentTime() > 4) {
                gameState.addAmountRecruited(recruiter.getAmountRecruited());
                recruiter.resetAmountRecruited();
                int[] firstStepCoord = recruiter.getWalkedPath().get(0);
                gameState.getBoard().getCell(firstStepCoord[0], firstStepCoord[1]).addToken(new BrainFact(1));
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
                AbstractCell cell = gameState.getCurrentPlayerCell();
                actionController.reveal(cell);
                gameState.setActionAvailability(false);
                break;
            case CAPTURE:
                returnValue = actionController.capture(gameState.getCurrentPlayer(), gameState.getRecruiter(),
                        gameState.getBoard());
                // If Recruiter captured set gameOver
                if (returnValue) {
                    gameState.setGameOver();
                }

                gameState.setActionAvailability(false); // TODO: add so that this makes sure action was valid

                break;
            case MINDSLIP:

                break;
            case MOVE:
                if (gameState.isMovementAvailable()) {
                    int row = (int) additionalInfo[0];
                    int col = (int) additionalInfo[1];

                    boolean didMove = actionController.movePlayer(gameState.getCurrentPlayer(), gameState,
                            new int[] { row, col });
                    if (didMove) {
                        gameState.setValidityMask(checkAction.createUniformMask(gameState.getBoard(), false));
                        gameState.setMovementAvailability(false);
                    }
                }
                break;
            case RECRUITERCHOICE:
                getGame().getRecruiter().setRecruiterType((RecruiterType) additionalInfo[0]);
                break;
            case BRAINNOTE:
                if (additionalInfo[0] instanceof String) {
                    actionController.addBrainNote((String) additionalInfo[0], (Integer) additionalInfo[1],
                            (Integer) additionalInfo[2],
                            gameState.getBoard());
                } else {
                    gameState.setActiveBrains(
                            actionController.fetchBrains((int) additionalInfo[0], (int) additionalInfo[1],
                                    gameState.getBoard()));
                }
                break;
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
