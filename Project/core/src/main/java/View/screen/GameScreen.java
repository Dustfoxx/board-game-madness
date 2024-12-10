package View.screen;

import Controller.ActionController;
import Controller.GameController;
import View.buildingBlocks.MindMGMTStage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Model.Board;
import Model.Csv;
import Model.RougeAgent;
import View.buildingBlocks.VisualBoard;
import View.screen.GameScreenComponents.AskButton;
import View.screen.GameScreenComponents.MoveButton;
import View.screen.GameScreenComponents.PlayerBar;
import com.badlogic.gdx.Gdx;
import io.github.MindMGMT.MindMGMT;
import View.screen.GameScreenComponents.SettingWindow;
import com.badlogic.gdx.scenes.scene2d.Actor;
import View.screen.GameScreenComponents.TurnBar;

public class GameScreen implements Screen {
    private final GameController gameController;
    private final ActionController actionController;
    private final MindMGMTStage stage;
    private final Skin skin;
    private final Texture boardTexture;
    private final PlayerBar playerBar;
    private final TurnBar turnBar;
    private final Array<TextButton> actionButtons = new Array<TextButton>();
    private final SettingWindow settingWindow;
    VisualBoard visualBoard;

    public GameScreen(MindMGMT application) {

        this.stage = new MindMGMTStage(new ScreenViewport(), application.assets);
        this.skin = application.skin;
        this.boardTexture = application.assets.get("basic-board.png", Texture.class);

        Csv boardCsv = application.assets.get("board-data.csv", Csv.class);
        this.gameController = new GameController(application.nrOfPlayers, boardCsv);
        this.actionController = new ActionController();
        this.playerBar = new PlayerBar(gameController, skin);
        this.turnBar=new TurnBar(gameController,skin);
        this.settingWindow = new SettingWindow(skin, stage, application);

        Gdx.input.setInputProcessor(stage);
        setupUI();
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        setupSettings(root);
        setupPlayerBar(root);
        setupMainSection(root);
        setupActionBar(root);
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
        mainSection.add(mindslipBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));

        // TODO: Change so that the players are not hardcoded but chosen positions at
        // the start of the game
        Board board = gameController.getGame().getBoard();
        board.getCell(0, 0).addPlayer(new RougeAgent(1));
        board.getCell(0, 5).addPlayer(new RougeAgent(2));
        board.getCell(6, 0).addPlayer(new RougeAgent(3));
        board.getCell(6, 5).addPlayer(new RougeAgent(4));
        visualBoard = new VisualBoard(board);
        Table boardSection = visualBoard.getVisualBoard();
        mainSection.add(boardSection).expandY().width(Value.percentWidth(0.5f, mainSection));

        // boardSection.add(boardImage).expand().fill();

        mainSection.add(turnBar).expandY().fillY().width(Value.percentWidth(0.3f, mainSection));
    }

    private void setupActionBar(Table root) {
        Table actionBar = new Table();
        root.row();
        root.add(actionBar).expandX().fillX().bottom().height(stage.getViewport().getWorldHeight() * 0.1f);

        AskButton askButton = new AskButton(gameController, skin);
        MoveButton moveButton = new MoveButton(gameController, skin, visualBoard);
        actionButtons.add(moveButton);
        actionBar.add(moveButton);
        actionButtons.add(askButton);
        actionBar.add(askButton);
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
