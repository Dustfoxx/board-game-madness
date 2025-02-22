package Controller;

import Model.*;
import Model.Game.gameStates;
import Model.Recruiter.RecruiterType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import View.screen.GameScreen;

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
    private GameScreen gameScreen;
    private int[] playerTurnOrder;
    private int recruiterIndex;
    private ActionController actionController;
    private CheckAction checkAction;
    private boolean isHost;
    private Net.HttpResponseListener updateHostListener;
    private String localName;
    private boolean boardIsActive;
    private boolean userIsRecruiter;
    private boolean localPlay;

    public boolean pendingClientUpdate;

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
     * are next in queue to play. This is the constructor intended for
     * clients.
     */
    public GameController(Game gameState, String localName, GameScreen gameScreen) {
        this.isHost = false;
        this.localName = localName;
        this.updateHostListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int status = httpResponse.getStatus().getStatusCode();
                if (status != 200) {
                    // Something went bad
                    System.err.println(httpResponse.getStatus());
                    System.err.println(httpResponse.getResultAsString());
                } else {
                    // Else all good
                    pendingClientUpdate = false;
                }
            }

            @Override
            public void failed(Throwable t) {
                System.err.println(t.getMessage());
            }

            @Override
            public void cancelled() {
            }
        };
        initController(gameState, gameScreen);

    }

    /**
     * The main gameController. Keeps an eye on victory conditions and which players
     * are next in queue to play. This is the constructor intended for the host.
     */
    public GameController(Csv boardCsv, ArrayList<User> users, GameScreen gameScreen) {
        Game gameState = initializeGame(boardCsv, users);
        this.isHost = true;
        initController(gameState, gameScreen);
    }

    /**
     * Returns wether the board should be active or not
     * 
     * @return boolean, true if active and false if not
     */
    public boolean getBoardIsActive() {
        return this.boardIsActive;
    }

    /**
     * Setter for boardIsActive
     * 
     * @param active new value
     */
    public void setBoardActive() {
        this.boardIsActive = activateBoardForUser();
    }

    public boolean getUserIsRecruiter() {
        return this.userIsRecruiter;
    }

    public boolean getLocalPlay() {
        return this.localPlay;
    }

    private void initController(Game gameState, GameScreen gameScreen) {
        // Create turn order
        // This controller will use this to know which player controls what unit
        int agentIterator = 1; // This is in case there are less than four agents. Every unit will still be
        // controlled
        // Used by clients to detect whenever polling the host should wait
        this.pendingClientUpdate = false;
        this.gameState = gameState;
        this.gameScreen = gameScreen;
        List<Player> gamePlayers = gameState.getPlayers();
        List<RougeAgent> agents = new ArrayList<>();
        this.playerTurnOrder = new int[6];
        this.recruiterIndex = this.gameState.getPlayers().indexOf(gameState.getRecruiter());

        this.actionController = new ActionController();
        this.checkAction = new CheckAction();

        boolean oneRecruiter = true;
        for (Player currPlayer : gamePlayers) {
            if (currPlayer instanceof Recruiter) {
                if (!oneRecruiter) {
                    throw new IllegalStateException("More than one recruiter");
                }
                oneRecruiter = false;
            } else {
                agents.add((RougeAgent) currPlayer);
            }
        }

        if (agents.isEmpty()) {
            throw new IllegalStateException("No agents found");
        }

        for (int i = 0; i < playerTurnOrder.length; i++) {
            switch (i) {
                case 0:
                case 3:
                    playerTurnOrder[i] = 0;
                    break;
                default:
                    playerTurnOrder[i] = agentIterator;
                    agentIterator++;
                    if (agentIterator > agents.size()) {
                        agentIterator = 1;
                    }
            }
        }

        this.userIsRecruiter = this.isHost;

        this.localPlay = gameState.getUsers().size() == 1;

        // TODO: Move this somewhere else since it's related to View and not Controller
        gameState.setMindSlipListener(new Game.MindSlipListener() {
            @Override
            public void onMindSlip(String message) {
                gameScreen.showMindSlipDialog(message);
            }
        });
    }

    private Game initializeGame(Csv boardCsv, ArrayList<User> users) {
        int userAmount = users.size();

        if (userAmount < 1) {
            throw new IllegalArgumentException("No users registered!");
        }
        this.localName = users.get(0).getUserName();

        // Randomly select three unique features
        List<Feature> allFeaturesList = new ArrayList<>(Arrays.asList(Feature.values()));
        Collections.shuffle(allFeaturesList);
        Feature[] recruiterFeatures = new Feature[3];
        for (int i = 0; i < 3; i++) {
            recruiterFeatures[i] = allFeaturesList.get(i);
        }

        Recruiter recruiter = new Recruiter(0, "Recruiter", recruiterFeatures);

        List<Player> players = new ArrayList<>();
        players.add(recruiter);
        int playerPieceAmount = 5;
        for (int i = 1; i < playerPieceAmount; i++) {
            players.add(new RougeAgent(i, "Agent" + i));
        }

        users.get(0).addPlayerPiece(recruiter);

        List<Player> rogueAgents = players.subList(1, players.size());
        if (users.size() == 1) {
            // Single player mode
            distributePlayers(rogueAgents, users, 0);
        } else {
            // multiplayer mode
            distributePlayers(rogueAgents, users, 1);
        }

        Board board = new Board(boardCsv);
        return new Game(players, users, board, 0);
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

    private boolean activateBoardForUser() {
        boolean[] returnVal = { false }; // Some weird workaround for using this inside forEach
        gameState.getUsers().forEach((user) -> {
            if (user.ownsPlayerPiece(gameState.getCurrentPlayer())) {
                if (user.getUserName().equals(this.localName)) {
                    returnVal[0] = true; // If a player owns the active piece we activate the board
                }
            }
            ;
        });
        return returnVal[0];
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
            case PAUSE:
                continueGame();
                break;
            case ENDGAME:
                break;
            default:
                break;
        }

        if (gameState.getGameState() != gameStates.PAUSE) {
            gameState.setValidityMask(checkAction.getValidMoves(gameState.getCurrentPlayer(), gameState.getBoard()));
            if (checkAction.isMaskEmpty(gameState.getValidityMask())) {
                gameState.setGameOver();
            }
        }

        if (this.localPlay) {
            if (gameState.getCurrentPlayer() instanceof Recruiter) {
                setRecruiterVisibility(true);
            } else {
                setRecruiterVisibility(false);
            }
        }
    }

    /**
     * Sets the visibility of all steps for the recruiter
     *
     * @param visibility what to set visibility to
     */
    public void setRecruiterVisibility(boolean visibility) {
        int[] dims = gameState.getBoard().getDims();
        for (int row = 0; row < dims[0]; row++) {
            for (int col = 0; col < dims[1]; col++) {
                AbstractCell cell = gameState.getBoard().getCell(row, col);
                List<Player> players = cell.getPlayers();
                List<Token> tokens = cell.getTokens();

                for (Player player : players) {
                    if (player instanceof Recruiter) {
                        player.setVisibility(visibility);
                    }
                }

                for (Token token : tokens) {
                    if (token instanceof Step) {
                        token.setVisibility(visibility);
                    }
                }
            }
        }
    }

    private void preGameLogic() {
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            gameState.setMovementAvailability(true);
            Recruiter recruiter = (Recruiter) gameState.getCurrentPlayer();
            gameState.incrementTime();
            if (gameState.getCurrentTime() > 4) {
                gameState.addAmountRecruited(recruiter.getAmountRecruited());
                recruiter.resetAmountRecruited();
                int[] firstStepCoord = recruiter.getWalkedPath().get(0);
                gameState.getBoard().getCell(firstStepCoord[0], firstStepCoord[1]).addToken(new BrainFact(1));
                gameState.setCurrentPlayer(1); // Gets first rogue agent and sets them as
                                               // next player
            }
        } else {
            List<Player> players = gameState.getPlayers();
            int currentIndex = players.indexOf(gameState.getCurrentPlayer());
            // If we are at the end of players, set game to started
            if (currentIndex == players.size() - 1) {
                if (localPlay) {
                    // Open blocker if in couch play mode
                    gameState.setGameState(gameStates.PAUSE);
                    gameScreen.showBlockerDialog("Agents look away!\nRecruiter, it's your turn, are you ready?");
                } else {
                    // Skip blocker if not in couch play mode
                    continueGame();
                }
            } else {
                // Set player to next rogue agent so they can place
                gameState.setMovementAvailability(true);
                gameState.setCurrentPlayer(currentIndex + 1);
            }
        }
    }

    private void ongoingLogic() {

        // Check if the next turn will be the recruiter's
        if (playerTurnOrder[(gameState.getPlayerTurnCounter() + 1) % playerTurnOrder.length] == 0) {
            if (localPlay) {
                // Open blocker if in couch play mode
                gameState.setGameState(gameStates.PAUSE);
                gameScreen.showBlockerDialog("Agents look away!\nRecruiter, it's your turn, are you ready?");
            } else {
                // Skip blocker if not in couch play mode
                continueGame();
            }
            return;
        }

        gameState.setMovementAvailability(true);
        if (gameState.getCurrentPlayer() instanceof Recruiter) {
            gameState.incrementTime();
        }
        if (gameState.getCurrentTime() % 2 == 1) {
            Recruiter recruiter = (Recruiter) gameState.getPlayers().get(recruiterIndex);
            gameState.addAmountRecruited(recruiter.getAmountRecruited());
            recruiter.resetAmountRecruited();
            if (gameState.getAmountRecruited() >= gameState.getMaxRecruits()) {
                // RECRUITER WIN GAME
                gameState.setGameOver();
            }
        }
        gameState.incrementPlayerTurnCounter();
        gameState.setCurrentPlayer(playerTurnOrder[gameState.getPlayerTurnCounter() % playerTurnOrder.length]);
        if (gameState.getCurrentPlayer() instanceof RougeAgent) {
            gameState.setActionAvailability(true);
        } else if (gameState.getCurrentTime() >= gameState.getMaxTime()) {
            // RECRUITER WIN
            gameState.setGameOver();
        }
    }

    private void continueGame() {
        gameState.setMovementAvailability(true);
        if (gameState.getPlayerTurnCounter() != 0) {
            gameState.incrementPlayerTurnCounter();
        }
        gameState.setCurrentPlayer(0);
        gameState.setGameState(gameStates.ONGOING);
        if (gameState.getCurrentTime() >= gameState.getMaxTime()) {
            // RECRUITER WIN
            gameState.setGameOver();
        }
    }

    public boolean actionHandler(Actions action) {
        return actionHandler(action, new Object[] {});
    }

    public boolean actionHandler(Actions action, Object[] additionalInfo) {
        if (!isHost) {
            this.pendingClientUpdate = true;
        }
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
                actionController.addBrainNote((String) additionalInfo[0], (Integer) additionalInfo[1],
                        (Integer) additionalInfo[2],
                        gameState);
                break;
        }

        if (!gameState.isActionAvailable() && !gameState.isMovementAvailable()) {
            newTurn();
        }

        if (!isHost) {
            updateHost();
        }

        return returnValue;
    }

    private void updateHost() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new GeneralAdapter<>())
                .registerTypeAdapter(Token.class, new GeneralAdapter<>())
                .registerTypeAdapter(AbstractCell.class, new GeneralAdapter<>())
                .create();

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder
                .newRequest()
                .method(Net.HttpMethods.POST)
                .url("http://localhost:8080/update")
                .content(gson.toJson(gameState))
                .build();
        Gdx.net.sendHttpRequest(httpRequest, this.updateHostListener);
    }

    public void deeplySetGameState(Game newGameState) {
        this.gameState.updateDeeply(newGameState);
    }

}
