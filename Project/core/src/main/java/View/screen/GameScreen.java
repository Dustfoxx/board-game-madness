package View.screen;

import Controller.GameController;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Gdx;

import Model.Csv;
import Model.User;

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

import io.github.MindMGMT.MindMGMT;

public class GameScreen implements Screen {
    private final GameController gameController;
    private final MindMGMTStage stage;
    private final Skin skin;
    private final Texture boardTexture;
    private final PlayerBar playerBar;
    private final TurnBar turnBar;
    private final SettingWindow settingWindow;
    private VisualBoard visualBoard;
    private final FeatureSelection featureSelection;

    public GameScreen(MindMGMT application, ArrayList<User> users) {

        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        this.skin = application.skin;
        this.boardTexture = application.assets.get("basic-board.png", Texture.class);
        Csv boardCsv = application.assets.get("board-data.csv", Csv.class);
        this.gameController = new GameController(boardCsv, users);

        if (application.server != null) {
            // We are host
            application.server.setGameState(this.gameController.getGame());
        }
        this.playerBar = new PlayerBar(gameController, skin);
        this.turnBar = new TurnBar(gameController, skin);
        this.settingWindow = new SettingWindow(skin, stage, application);
        this.featureSelection = new FeatureSelection(gameController, skin);
        Gdx.input.setInputProcessor(stage);
        setupUI();
        EndGameWindow endGameWindow = new EndGameWindow(gameController.getGame(), skin, application);
        endGameWindow.setPosition(
                Gdx.graphics.getWidth() / 2 - endGameWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - endGameWindow.getHeight() / 2);
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
        RecruiterWindow recruiterWindow = new RecruiterWindow(skin, gameController.getGame().getRecruiter(),
                gameController);
        recruiterWindow.setPosition(
                Gdx.graphics.getWidth() / 2 - recruiterWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - recruiterWindow.getHeight() / 2);
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

        this.visualBoard = new VisualBoard(gameController, skin);
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
        AskButton askButton = new AskButton(gameController, skin);
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
        stage.act(delta);
        stage.draw();
        turnBar.updateTurnbar();
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
