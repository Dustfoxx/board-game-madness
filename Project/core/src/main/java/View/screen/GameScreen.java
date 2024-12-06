package View.screen;

import Controller.ActionController;
import Controller.GameController;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Model.AbstractCell;
import Model.Board;
import Model.Csv;
import Model.NormalCell;
import Model.RougeAgent;
import View.buildingBlocks.VisualBoard;
import View.screen.GameScreenComponents.AskButton;
import View.screen.GameScreenComponents.PlayerBar;
import com.badlogic.gdx.Gdx;
import io.github.MindMGMT.MindMGMT;
import View.screen.GameScreenComponents.SettingWindow;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameScreen implements Screen {
    private final MindMGMT application;
    private final GameController gameController;
    private final ActionController actionController;
    private final Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Texture boardTexture;
    private final PlayerBar playerBar;
    private Label timeTracker;
    private final Array<TextButton> actionButtons = new Array<TextButton>();
    private final SettingWindow settingWindow;
    private String selectedFeature;

    public GameScreen(MindMGMT application) {
        this.application = application;
        this.selectedFeature = "";

        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = application.skin;
        this.boardTexture = application.assets.get("basic-board.png", Texture.class);

        Csv boardCsv = application.assets.get("board-data.csv", Csv.class);
        this.gameController = new GameController(application.nrOfPlayers, boardCsv);
        this.actionController = new ActionController();

        this.playerBar = new PlayerBar(gameController);
        this.timeTracker = new Label(String.valueOf(gameController.getGame().getCurrentTime()), skin);
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

        // not useful in functionality now, but useful for layout
        Table iconBar = new Table();
        mainSection.add(iconBar).expandY().fillY().width(Value.percentWidth(0.1f, mainSection));

        // TODO: Change so that the players are not hardcoded but chosen positions at
        // the start of the game
        Board board = gameController.getGame().getBoard();
        board.getCell(0, 0).addPlayer(new RougeAgent(1));
        board.getCell(0, 5).addPlayer(new RougeAgent(2));
        board.getCell(6, 0).addPlayer(new RougeAgent(3));
        board.getCell(6, 5).addPlayer(new RougeAgent(4));
        VisualBoard visualBoard = new VisualBoard(board);
        Table boardSection = visualBoard.getVisualBoard();
        mainSection.add(boardSection).expandY().fillY().width(Value.percentWidth(0.5f, mainSection));
        // boardSection.add(boardImage).expand().fill();

        Table mindslipBar = new Table();
        mainSection.add(mindslipBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));

        Table turnBar = new Table();
        mainSection.add(turnBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));
        turnBar.add(timeTracker).expandX();
    }

    private void setupActionBar(Table root) {
        Table actionBar = new Table();
        root.row();
        root.add(actionBar).expandX().fillX().bottom().height(stage.getViewport().getWorldHeight() * 0.1f);

        String[] actions = {"Move", "Reveal" };
        for (String action : actions) {
            TextButton actionButton = new TextButton(action, skin);
            actionButtons.add(actionButton);
            actionBar.add(actionButton).expandX();

            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    askAction();
                }
            });
        }
        // Create an Ask-button if the cell is not a temple
        AbstractCell cell = gameController.getGame().getBoard().getCell(0, 0);
        if (cell.getClass().equals(NormalCell.class)) {
            NormalCell normalCell = (NormalCell) cell;
            AskButton askButton = new AskButton("Ask", skin, stage, selectedFeature, gameController, normalCell);
            actionButtons.add(askButton);
            actionBar.add(askButton);
        }
    }

    private void askAction() {
        Window askActionWindow = createAskActionWindow("Ask Action", "Which feature do you want to ask?");
        stage.addActor(askActionWindow);
    }

    void updateButtonStates() {
        for (TextButton textButton : actionButtons) {
            textButton.getColor().a = 0.4f;
            textButton.setDisabled(true);
        }
    }

    private Window createAskActionWindow(String title, String message) {
        Window askWindow = new Window(title, skin);
        askWindow.setMovable(false);
        askWindow.setResizable(false);
        askWindow.setModal(true);

        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                askWindow.remove();
            }
        });
        askWindow.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();

        Label messageLabel = new Label(message, skin);
        askWindow.add(messageLabel).pad(20).row();

        Table buttonTable = new Table();

        // TODO: get the feature from the model
        TextButton featureButton1 = new TextButton("Feature 1", skin);
        featureButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // get the selected feature
                selectedFeature = "Feature 1";
            }
        });
        buttonTable.add(featureButton1).padRight(20).padBottom(10);

        // TODO: get the feature from the model
        TextButton featureButton2 = new TextButton("Feature 2", skin);
        featureButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedFeature = "Feature 2";
            }
        });
        buttonTable.add(featureButton2).padBottom(10);

        askWindow.add(buttonTable).colspan(2).center().row();

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO:send the selected feature to the game controller
                System.out.println("Feature selected" + selectedFeature);
                gameController.newTurn();
                askWindow.remove();
            }
        });
        askWindow.add(confirmButton).colspan(2).padTop(10).center().row();

        askWindow.pack();
        askWindow.setSize(500, 200);
        askWindow.setPosition(stage.getWidth() / 2 - askWindow.getWidth() / 2,
                stage.getHeight() / 2 - askWindow.getHeight() / 2);

        return askWindow;
    }

    @Override

    public void show() {
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        application.drawBackground();
        stage.act(delta);
        stage.draw();
        playerBar.update();
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
        batch.dispose();
        skin.dispose();
        stage.dispose();
        boardTexture.dispose();
    }
}
