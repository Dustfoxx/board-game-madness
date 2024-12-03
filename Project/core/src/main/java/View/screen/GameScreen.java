package View.screen;

import Controller.ActionController;
import Controller.CheckAction;
import Controller.GameController;
import Model.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import io.github.MindMGMT.MindMGMT;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameScreen implements Screen {

    // private final GameController gameController;
    private final MindMGMT application;
    private final GameController gameController;
    private final ActionController actionController;
    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Texture boardTexture;
    private final Image boardImage;
    private String selectedFeature;
    private final Array<TextButton> actionButtons = new Array<TextButton>();
    private final Array<TextButton> playerButtons = new Array<TextButton>();

    public GameScreen(MindMGMT application) {
        this.application = application;
        game = initializeGame();

        this.selectedFeature = "";
        this.gameController = new GameController(application.nrOfPlayers);
        this.actionController = new ActionController();

        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.boardTexture = new Texture("basic-board.png");
        this.boardImage = new Image(boardTexture);

        Gdx.input.setInputProcessor(stage);
        setupUI();
    }

    private Game initializeGame() {
        List<Player> players = new ArrayList<Player>();
        Feature[] recruiterFeatures = new Feature[] { Feature.FOUNTAIN, Feature.BILLBOARD, Feature.BUS };

        for (int i = 0; i < application.nrOfPlayers; i++) {
            players.add(i == 0 ? new Recruiter(i, "recruiter", recruiterFeatures) : new RougeAgent(i));
        }

        int rows = 6;
        int columns = 7;

        AbstractCell[][] cells = new AbstractCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new NormalCell(new Feature[] { Feature.BOOKSTORE, Feature.BILLBOARD });
            }
        }

        Board board = new Board(cells);
        return new Game(players, board, players.get(0));
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        setupPlayerBar(root);
        setupMainSection(root);
        setupActionBar(root);
    }

    private void setupPlayerBar(Table root) {
        Table playerBar = new Table();
        root.add(playerBar).expandX().fillX().top().height(stage.getViewport().getWorldHeight() * 0.1f);

        for (int i = 1; i <= application.nrOfPlayers; i++) {
            TextButton playerButton = new TextButton("Player " + i, skin);
            playerButtons.add(playerButton);
            playerBar.add(playerButton).expandX();
        }
    }

    private void setupMainSection(Table root) {
        Table mainSection = new Table();
        root.row();
        root.add(mainSection).expand().fill();

        // not useful in functionality now, but useful for layout
        Table iconBar = new Table();
        mainSection.add(iconBar).expandY().fillY().width(Value.percentWidth(0.1f, mainSection));

        Table boardSection = new Table();
        mainSection.add(boardSection).expandY().fillY().width(Value.percentWidth(0.5f, mainSection));
        boardSection.add(boardImage).expand().fill();

        Table mindslipBar = new Table();
        mainSection.add(mindslipBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));

        Table turnBar = new Table();
        mainSection.add(turnBar).expandY().fillY().width(Value.percentWidth(0.2f, mainSection));
    }

    private void setupActionBar(Table root) {
        Table actionBar = new Table();
        root.row();
        root.add(actionBar).expandX().fillX().bottom().height(stage.getViewport().getWorldHeight() * 0.1f);

        String[] actions = { "Ask", "Move", "Reveal" };
        for (String action : actions) {
            TextButton actionButton = new TextButton(action, skin);
            actionButtons.add(actionButton);
            actionBar.add(actionButton).expandX();

            actionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    askAction();
                    gameController.newTurn();
                }
            });
        }
    }

    private void askAction() {
        Window askActionWindow = createPopWindow("Ask Action", "Which feature do you want to ask?");
        stage.addActor(askActionWindow);
    }

    void updateButtonStates() {
        for (TextButton textButton : actionButtons) {
            textButton.getColor().a = 0.4f;
            textButton.setDisabled(true);
        }
    }

    private void updatePlayerButtonStates() {
        if (game.getPlayers() != null) {
            for (Player player : game.getPlayers()) {
                int currentPlayerIndex = game.getPlayers().indexOf(player);
                if (player == game.getCurrentPlayer()) {
                    playerButtons.get(currentPlayerIndex).getColor().a = 0.3f;
                } else {
                    playerButtons.get(currentPlayerIndex).getColor().a = 1f;
                }

            }
        }
    }

    private Window createPopWindow(String title, String message) {
        Window window = new Window(title, skin);

        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        window.getTitleTable().add(closeButton).padLeft(10).padTop(2).right();

        Label messageLabel = new Label(message, skin);
        window.add(messageLabel).pad(20).row();

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

        window.add(buttonTable).colspan(2).center().row();

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO:send the selected feature to the game controller
                System.out.println("Feature selected" + selectedFeature);
                gameController.newTurn();
                window.remove();
            }
        });
        window.add(confirmButton).colspan(2).padTop(10).center().row();

        window.pack();
        window.setSize(300, 200);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2,
                stage.getHeight() / 2 - window.getHeight() / 2);

        return window;
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
        updatePlayerButtonStates();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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
