package View.screen;

import Controller.GameController;

import Model.*;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Gdx;

import View.buildingBlocks.VisualBoard;
import View.screen.GameScreenComponents.AskButton;
import View.screen.GameScreenComponents.CaptureButton;
import View.screen.GameScreenComponents.EndGameWindow;
import View.screen.GameScreenComponents.RevealButton;
import View.screen.GameScreenComponents.PlayerBar;
import View.screen.GameScreenComponents.RecruiterWindow;
import View.buildingBlocks.MindMGMTStage;
import View.screen.GameScreenComponents.SettingWindow;
import View.screen.GameScreenComponents.TurnBar;
import View.screen.GameScreenComponents.FeatureSelection;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.github.MindMGMT.MindMGMT;

public class GameScreen implements Screen {
    private final MindMGMT application;
    private final GameController gameController;
    private MindMGMTStage stage;
    private Skin skin;
    private Texture boardTexture;
    private PlayerBar playerBar;
    private TurnBar turnBar;
    private SettingWindow settingWindow;
    private VisualBoard visualBoard;
    private FeatureSelection featureSelection;

    private int pollingFrequency;
    private Net.HttpResponseListener pollListener;
    private int frameCount;
    private boolean isHost;

    /**
     * Main game screen. This constructor is intended for client use.
     * @param application Reference to the application
     * @param gameState An initial cop of the hosts game state
     */
    public GameScreen(MindMGMT application, Game gameState) {
        this.application = application;
        this.gameController = new GameController(gameState);
        this.isHost = false;
        this.pollingFrequency = 30;
        this.frameCount = 0;
        this.pollListener = getPollListener();

        setupUI(application);
    }

    /**
     * Main game screen. This constructor is intended for host use.
     * @param application Reference to the application
     * @param users A list of users each representing a client
     */
    public GameScreen(MindMGMT application, ArrayList<User> users) {

        this.application = application;
        Csv boardCsv = application.assets.get("board-data.csv", Csv.class);
        this.gameController = new GameController(boardCsv, users);
        this.isHost = true;

        if (application.server != null) {
            // We are host
            application.server.setGameState(this.gameController.getGame());
        }

        setupUI(application);
    }

    private Net.HttpResponseListener getPollListener() {
        return new Net.HttpResponseListener() {

            private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new GeneralAdapter<>())
                .registerTypeAdapter(Token.class, new GeneralAdapter<>())
                .registerTypeAdapter(AbstractCell.class, new GeneralAdapter<>())
                .create();

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (gameController.pendingClientUpdate) {
                    // Do not overwrite existing game state if client has made an update
                    return;
                }

                String msg = httpResponse.getResultAsString();
                try {
                    Game gameState = gson.fromJson(msg, Game.class);
                    gameController.deeplySetGameState(gameState);
                } catch (JsonSyntaxException e) {
                    // Handle game end?
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t) {
                System.err.println(t.getMessage());
            }

            @Override
            public void cancelled() {}
        };
    }

    private void setupUI(MindMGMT application) {

        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        this.skin = application.skin;
        this.boardTexture = application.assets.get("basic-board.png", Texture.class);
        this.playerBar = new PlayerBar(gameController, skin);
        this.turnBar = new TurnBar(gameController, skin);
        this.settingWindow = new SettingWindow(skin, stage, application);
        this.featureSelection = new FeatureSelection(gameController, application);
        Gdx.input.setInputProcessor(stage);
        setupUI();
        EndGameWindow endGameWindow = new EndGameWindow(gameController, skin, application);
        endGameWindow.setPosition(
                Gdx.graphics.getWidth() / 2f - endGameWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - endGameWindow.getHeight() / 2);
        stage.addActor(endGameWindow);
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        setupSettings(root);
        setupPlayerBar(root);
        setupMainSection(root);
        setupActionBar(root);
        RecruiterWindow recruiterWindow = new RecruiterWindow(gameController.getGame().getRecruiter(),
                gameController, application);
        recruiterWindow.setPosition(
                Gdx.graphics.getWidth() / 2f - recruiterWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - recruiterWindow.getHeight() / 2);
        stage.addActor(recruiterWindow);
    }

    private void setupSettings(Table root) {
        TextButton settingButton = new TextButton("Settings", skin);
        root.add(settingButton).expandX().top().right().row();
        settingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: add some logic to pause the game in game controller
                stage.addActor(settingWindow);
            }
        });
    }

    private void setupPlayerBar(Table root) {
        root.add(playerBar).expandX().fillX().height(stage.getViewport().getWorldHeight() * 0.1f);
    }

    private void setupMainSection(Table root) {
        Table mainSection = new Table();
        root.row();
        root.add(mainSection).expand().fill();

        Table mindslipBar = new Table();
        mainSection.add(mindslipBar).expandY().fillY().width(Value.percentWidth(0.25f, mainSection));
        mindslipBar.add(featureSelection).expand().fill();

        this.visualBoard = new VisualBoard(gameController, this.application);
        Table boardSection = this.visualBoard.getVisualBoard();
        mainSection.add(boardSection).expandY().fillY().width(Value.percentWidth(0.5f, mainSection));

        // boardSection.add(boardImage).expand().fill();

        mainSection.add(turnBar).expandY().fillY().width(Value.percentWidth(0.25f, mainSection));
    }

    private void setupActionBar(Table root) {
        // Create a table for the action buttons
        Table actionBar = new Table();
        root.row();
        root.add(actionBar).width(Value.percentWidth(0.5f, root)).fillX().bottom()
                .height(stage.getViewport().getWorldHeight() * 0.1f);

        // Create an ask button
        AskButton askButton = new AskButton(gameController, application);
        actionBar.add(askButton).expand();

        // Create a reveal button
        RevealButton revealButton = new RevealButton(gameController, skin);
        actionBar.add(revealButton).expand();

        // Create a capture button
        CaptureButton captureButton = new CaptureButton(gameController, skin);
        actionBar.add(captureButton).expand();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (frameCount >= pollingFrequency && !gameController.pendingClientUpdate) {
            frameCount = 0;
            if (!isHost) {
                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder
                    .newRequest()
                    .method(Net.HttpMethods.GET)
                    .url("http://localhost:8080/poll")
                    .build();
                Gdx.net.sendHttpRequest(httpRequest, pollListener);
            }
        }

        stage.act(delta);
        stage.draw();
        turnBar.updateTurnbar();
        frameCount++;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        if (settingWindow != null) {
            settingWindow.updateSize();
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        boardTexture.dispose();
    }
}
